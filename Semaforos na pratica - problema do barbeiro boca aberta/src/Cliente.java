import java.time.LocalTime;

public class Cliente extends Thread {

    private final int idCliente;

    public Cliente(int id) {
        super("Cliente-" + id);
        this.idCliente = id;
    }

    @Override
    public void run() {
        try {
            System.out.println(String.format("[%s] [Cliente-Thread: %s] Chegou na barbearia.",
                    LocalTime.now(), getId()));

            // 1. Tenta adquirir a Exclusão Mútua para verificar/modificar as cadeiras livres
            BarbeiroDorminhoco.mutex.acquire();

            if (BarbeiroDorminhoco.cadeirasLivres > 0) {
                // HÁ CADEIRAS LIVRES: O cliente se senta e espera.
                BarbeiroDorminhoco.cadeirasLivres--;

                System.out.println(String.format("[%s] [Cliente-Thread: %s] Sentou na espera. Cadeiras livres: %d.",
                        LocalTime.now(), getId(), BarbeiroDorminhoco.cadeirasLivres));

                // Libera o mutex para permitir que outros clientes entrem/verifiquem cadeiras
                BarbeiroDorminhoco.mutex.release();

                // Acorda o Barbeiro (incrementa o semáforo 'barbeiro')
                BarbeiroDorminhoco.barbeiro.release();

                // O Cliente espera o corte (diminui o semáforo 'clientes' e BLOQUEIA)
                BarbeiroDorminhoco.clientes.acquire();

                // Cliente atendido. Agora, ele precisa liberar a cadeira de espera.

                // 2. Adquire o Mutex novamente para liberar a cadeira de espera (Região Crítica)
                BarbeiroDorminhoco.mutex.acquire();
                BarbeiroDorminhoco.cadeirasLivres++;
                System.out.println(String.format("[%s] [Cliente-Thread: %s] Saindo. Cadeiras livres: %d.",
                        LocalTime.now(), getId(), BarbeiroDorminhoco.cadeirasLivres));

                // Libera o mutex.
                BarbeiroDorminhoco.mutex.release();

            } else {
                // NÃO HÁ CADEIRAS LIVRES: O cliente vai embora.

                // Libera o mutex imediatamente, pois não modificou as cadeiras.
                BarbeiroDorminhoco.mutex.release();

                // Conta o cliente rejeitado
                BarbeiroDorminhoco.clientesRejeitados++;

                System.out.println(String.format("[%s] [Cliente-Thread: %s] REJEITADO. Sem cadeiras, foi embora. Rejeitados: %d.",
                        LocalTime.now(), getId(), BarbeiroDorminhoco.clientesRejeitados));
            }

        } catch (InterruptedException e) {
            System.out.println(String.format("[%s] [Cliente-Thread: %s] Interrompido.", LocalTime.now(), getId()));
        }
    }
}