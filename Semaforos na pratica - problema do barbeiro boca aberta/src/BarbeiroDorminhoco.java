import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.io.FileInputStream;
import java.io.IOException;

public class BarbeiroDorminhoco {

    // Semáforos
    // Controla o acesso à variável cadeiras_livres (Exclusão Mútua)
    public static Semaphore mutex;
    // Semáforo para o Barbeiro (bloqueia se não há clientes)
    public static Semaphore barbeiro;
    // Semáforo para os Clientes (bloqueia se o barbeiro está ocupado/dormindo e não há cadeiras)
    public static Semaphore clientes;

    // Variáveis de Estado
    public static int cadeirasEspera;
    public static int cadeirasLivres;
    public static int clientesTotais;
    public static int clientesAtendidos = 0;
    public static int clientesRejeitados = 0;
    public static long tempoTotal = 0;

    // Configurações de Tempo (lidas do input.txt)
    public static int minInterChegada;
    public static int maxInterChegada;
    public static int minServico;
    public static int maxServico;
    public static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        // 1. Configuração e Nome do Host
        imprimirNomeHost();
        carregarConfiguracoes("input.txt");

        // 2. Inicialização do Ambiente
        cadeirasLivres = cadeirasEspera;
        mutex = new Semaphore(1);       // Semáforo binário para exclusão mútua
        barbeiro = new Semaphore(0);    // Barbeiro começa dormindo (0 permissões)
        clientes = new Semaphore(0);    // Clientes esperam (0 permissões)

        // Inicia a thread do Barbeiro
        Barbeiro barbeiroThread = new Barbeiro();
        barbeiroThread.start();

        // 3. Simulação da Chegada de Clientes
        System.out.println(String.format("[%s] [Main-Thread: %s] INÍCIO DA SIMULAÇÃO: %d cadeiras de espera, %d clientes totais.",
                LocalTime.now(), Thread.currentThread().getId(), cadeirasEspera, clientesTotais));

        for (int i = 1; i <= clientesTotais; i++) {
            Cliente cliente = new Cliente(i);
            cliente.start();

            // Espera aleatória entre chegadas
            int tempoEspera = random.nextInt(maxInterChegada - minInterChegada + 1) + minInterChegada;
            Thread.sleep(tempoEspera);
        }

        // 4. Espera e Finalização
        // Espera um tempo extra para garantir que os clientes em serviço/espera terminem.
        Thread.sleep(2000);

        // Interrompe o barbeiro e calcula as métricas
        barbeiroThread.interrupt();

        // 5. Impressão dos Resultados
        System.out.println("\n--- RESULTADOS FINAIS ---");
        System.out.println(String.format("Clientes Atendidos: %d", clientesAtendidos));
        System.out.println(String.format("Clientes Rejeitados: %d", clientesRejeitados));

        double utilizacaoBarbeiro = 0.0;
        if (tempoTotal > 0) {
            // A utilização é calculada sobre o tempo total de simulação (desde o primeiro cliente até a finalização)
            long tempoSimulacaoTotal = System.currentTimeMillis() - Barbeiro.tempoInicioSimulacao;
            utilizacaoBarbeiro = (double) tempoTotal / tempoSimulacaoTotal * 100;
        }

        System.out.println(String.format("Utilização do Barbeiro: %.2f%%", utilizacaoBarbeiro));
        System.out.println("-------------------------");
    }

    // --- Métodos de Utilidade ---
    private static void imprimirNomeHost() {
        try {
            String host = InetAddress.getLocalHost().getHostName();
            String hostIP = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Executando em: " + host + '\n' +
                                "IP: " + hostIP);
        } catch (UnknownHostException e) {
            System.out.println("Não foi possível determinar o nome do host.");
        }
        System.out.println("----------------------------------------------");
    }

    private static void carregarConfiguracoes(String arquivo) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(arquivo)) {
            System.out.println("Arquivo carregado com sucesso");
            prop.load(fis);

            cadeirasEspera = Integer.parseInt(prop.getProperty("cadeiras_espera"));
            clientesTotais = Integer.parseInt(prop.getProperty("clientes_totais"));

            String[] interchegada = prop.getProperty("interchegada_ms").split("-");
            minInterChegada = Integer.parseInt(interchegada[0]);
            maxInterChegada = Integer.parseInt(interchegada[1]);

            String[] servico = prop.getProperty("servico_ms").split("-");
            minServico = Integer.parseInt(servico[0]);
            maxServico = Integer.parseInt(servico[1]);

        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar ou parsear o arquivo de configuração 'input.txt'. Usando valores padrão.");
            // Valores padrão de fallback
            cadeirasEspera = 4;
            clientesTotais = 20;
            minInterChegada = 10;
            maxInterChegada = 40;
            minServico = 30;
            maxServico = 60;
        }
    }

    // Método para calcular um tempo de serviço aleatório
    public static int getTempoServico() {
        return random.nextInt(maxServico - minServico + 1) + minServico;
    }

}
