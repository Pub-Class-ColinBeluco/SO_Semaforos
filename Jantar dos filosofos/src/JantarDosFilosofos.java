import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class JantarDosFilosofos {

    // --- ENUM e Variáveis de Estado ---
    public enum Estado { PENSANDO, FAMINTO, COMENDO }

    public static int NUM_FILOSOFOS;
    public static long DURACAO_SEG;
    public static Estado[] estados;
    public static Filosofo[] filosofos;

    // --- Semáforos e Sincronização ---
    // Mutex global para exclusão mútua ao acessar os estados (Região Crítica)
    public static Semaphore mutex;
    // Semáforos individuais para cada filósofo esperar quando FAMINTO e garfos ocupados
    public static Semaphore[] s;

    // --- Configurações de Tempo e Estatísticas ---
    public static final Random random = new Random();
    public static int THINK_MIN_MS, THINK_MAX_MS;
    public static int EAT_MIN_MS, EAT_MAX_MS;
    public static long TEMPO_FIM_SIMULACAO;
    public static long[] tempoEsperaTotal;

    public static void main(String[] args) throws InterruptedException {
        // 1. Configuração e Nome do Host
        imprimirNomeHost();
        carregarConfiguracoes("input.txt");

        // 2. Inicialização do Ambiente
        estados = new Estado[NUM_FILOSOFOS];
        filosofos = new Filosofo[NUM_FILOSOFOS];
        s = new Semaphore[NUM_FILOSOFOS];
        tempoEsperaTotal = new long[NUM_FILOSOFOS];

        mutex = new Semaphore(1); // Mutex para exclusão mútua

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            estados[i] = Estado.PENSANDO;
            // Semáforo individual inicia com 0 permissões (filósofo deve esperar)
            s[i] = new Semaphore(0);
            filosofos[i] = new Filosofo(i);
            filosofos[i].start();
        }

        TEMPO_FIM_SIMULACAO = System.currentTimeMillis() + DURACAO_SEG * 1000;

        System.out.println(String.format("[%s] [Main] INÍCIO da simulação. Duração: %d segundos.",
                LocalTime.now(), DURACAO_SEG));
        System.out.println("Variação Usada: verificacao = solução (A) “come se ambos livres”.");

        // 3. Espera o tempo de simulação
        Thread.sleep(DURACAO_SEG * 1000);

        // 4. Finalização e Estatísticas
        System.out.println(String.format("\n[%s] [Main] FIM da simulação. Calculando métricas.",
                LocalTime.now()));

        for (Filosofo f : filosofos) {
            f.interrupt();
        }

        // Espera um pouco para garantir que as threads interrompidas saiam dos blocos synchronized
        Thread.sleep(100);

        imprimirResultados();
    }

    // --- Lógica do Monitor (Região Crítica) ---

    // Chamado pelo filósofo 'i' quando está com fome
    public static void pegarGarfos(int i) throws InterruptedException {
        long tempoInicioEspera = System.currentTimeMillis();

        // 1. Entra na Região Crítica (Exclusão Mútua)
        mutex.acquire();

        // 2. Altera o estado para FAMINTO
        estados[i] = Estado.FAMINTO;
        System.out.println(String.format("[%s] [Filosofo-%d] Mudou para FAMINTO. (Estado: %s)",
                LocalTime.now(), i, Arrays.toString(estados)));

        // 3. Tenta comer
        test(i);

        // 4. Sai da Região Crítica
        mutex.release();

        // 5. Se não conseguiu comer (s[i] == 0), ele bloqueia aqui.
        // Se conseguiu (test() fez um release), ele passa direto.
        s[i].acquire();

        // Cálculo do tempo de espera total
        long tempoFimEspera = System.currentTimeMillis();
        tempoEsperaTotal[i] += (tempoFimEspera - tempoInicioEspera);
    }

    // Chamado pelo filósofo 'i' quando termina de comer
    public static void largarGarfos(int i) {
        try {
            // 1. Entra na Região Crítica (Exclusão Mútua)
            mutex.acquire();

            // 2. Altera o estado para PENSANDO
            estados[i] = Estado.PENSANDO;
            System.out.println(String.format("[%s] [Filosofo-%d] Mudou para PENSANDO. (Estado: %s)",
                    LocalTime.now(), i, Arrays.toString(estados)));

            // 3. Testa os vizinhos para ver se podem comer agora
            test(vizinhoEsquerda(i));
            test(vizinhoDireita(i));

            // 4. Sai da Região Crítica
            mutex.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Lógica para verificar se o filósofo 'i' pode comer (Testar Condição)
    private static void test(int i) {
        int esq = vizinhoEsquerda(i);
        int dir = vizinhoDireita(i);

        // A condição para comer:
        // 1. Está faminto E
        // 2. O vizinho da esquerda NÃO está comendo E
        // 3. O vizinho da direita NÃO está comendo
        if (estados[i] == Estado.FAMINTO &&
                estados[esq] != Estado.COMENDO &&
                estados[dir] != Estado.COMENDO) {

            estados[i] = Estado.COMENDO;
            System.out.println(String.format("[%s] [Filosofo-%d] COMEÇOU a COMER. (Estado: %s)",
                    LocalTime.now(), i, Arrays.toString(estados)));
            // Libera o semáforo individual, permitindo que o filósofo saia do .acquire() e coma
            s[i].release();
        }
    }

    // --- Métodos de Utilidade ---

    private static int vizinhoEsquerda(int i) {
        return (i + NUM_FILOSOFOS - 1) % NUM_FILOSOFOS;
    }

    private static int vizinhoDireita(int i) {
        return (i + 1) % NUM_FILOSOFOS;
    }

    // --- Configuração e Output ---

    private static void imprimirNomeHost() {
        try {
            String host = InetAddress.getLocalHost().getHostName();
            System.out.println("Executando em: " + host);
            System.out.println("IP: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("Não foi possível determinar o nome do host.");
        }
        System.out.println("----------------------------------------------");
    }

    private static void carregarConfiguracoes(String arquivo) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(arquivo)) {
            prop.load(fis);

            NUM_FILOSOFOS = Integer.parseInt(prop.getProperty("filosofos"));
            DURACAO_SEG = Long.parseLong(prop.getProperty("duracao_seg"));

            String[] think = prop.getProperty("think_ms").split("-");
            THINK_MIN_MS = Integer.parseInt(think[0]);
            THINK_MAX_MS = Integer.parseInt(think[1]);

            String[] eat = prop.getProperty("eat_ms").split("-");
            EAT_MIN_MS = Integer.parseInt(eat[0]);
            EAT_MAX_MS = Integer.parseInt(eat[1]);

        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar ou parsear o arquivo de configuração 'input.txt'. Usando valores padrão.");
            NUM_FILOSOFOS = 5;
            DURACAO_SEG = 20;
            THINK_MIN_MS = 50;
            THINK_MAX_MS = 120;
            EAT_MIN_MS = 40;
            EAT_MAX_MS = 90;
        }
    }

    private static void imprimirResultados() {
        System.out.println("\n--- RESULTADOS FINAIS ---");
        System.out.println("Variação: verificacao = solução (A) “come se ambos livres”.");

        long totalRefeicoes = 0;
        double tempoEsperaTotalGeral = 0;

        // Cabeçalho da Tabela
        System.out.println(String.format("%-10s | %-15s | %-10s",
                "Filósofo", "Refeições", "T. Médio Espera (ms)"));
        System.out.println("---------------------------------------------");

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            long total = filosofos[i].refeicoesComidas;
            totalRefeicoes += total;

            double tempoMedioEspera = 0.0;
            if (total > 0) {
                tempoMedioEspera = (double) tempoEsperaTotal[i] / total;
            }
            tempoEsperaTotalGeral += tempoMedioEspera;

            System.out.println(String.format("%-10d | %-15d | %-10.2f",
                    i, total, tempoMedioEspera));
        }

        System.out.println("---------------------------------------------");
        System.out.println(String.format("Refeições Totais: %d", totalRefeicoes));
        System.out.println(String.format("T. Médio Espera Total: %.2f ms", tempoEsperaTotalGeral / NUM_FILOSOFOS));
        System.out.println("-------------------------");
    }
}