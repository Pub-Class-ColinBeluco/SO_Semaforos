import java.time.LocalTime;

public class Barbeiro extends Thread {

    public static long tempoInicioSimulacao;

    public Barbeiro() {
        super("Barbeiro-0");
        tempoInicioSimulacao = System.currentTimeMillis();
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                // 1. O barbeiro tenta adquirir uma permissão do semáforo 'clientes'. 
                // Se for 0, ele BLOQUEIA (dorme).
                BarbeiroDorminhoco.barbeiro.acquire();

                // --- Região Crítica (Exclusão Mútua para a Cadeira de Corte) ---
                // O cliente que o acordou vai para a cadeira de corte, liberando o
                // semáforo do barbeiro e liberando a cadeira de espera.

                // Barbeiro atende.
                int tempoServico = BarbeiroDorminhoco.getTempoServico();
                long tempoInicio = System.currentTimeMillis();

                System.out.println(String.format("[%s] [Barbeiro-Thread: %s] ️ Atendendo Cliente. Serviço de %d ms.",
                        LocalTime.now(), getId(), tempoServico));

                // Simula o corte
                Thread.sleep(tempoServico);

                long tempoFim = System.currentTimeMillis();

                // BarbeiroDorminhoco.mutex.acquire(); // Já garantido pelo protocolo do cliente
                BarbeiroDorminhoco.clientesAtendidos++;
                BarbeiroDorminhoco.tempoTotal += (tempoFim - tempoInicio);
                // BarbeiroDorminhoco.mutex.release(); // Já garantido pelo protocolo do cliente

                System.out.println(String.format("[%s] [Barbeiro-Thread: %s] Cliente atendido. Total atendidos: %d.",
                        LocalTime.now(), getId(), BarbeiroDorminhoco.clientesAtendidos));

                // Após terminar, ele libera o semáforo 'barbeiro' para o próximo cliente entrar.
                BarbeiroDorminhoco.clientes.release();
            }
        } catch (InterruptedException e) {
            // Interrompido (fim da simulação)
            System.out.println(String.format("[%s] [Barbeiro-Thread: %s] Encerrando o Barbeiro.",
                    LocalTime.now(), getId()));
        }
    }
}