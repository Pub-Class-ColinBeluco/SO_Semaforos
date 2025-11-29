import java.time.LocalTime;
import java.util.Random;

public class Filosofo extends Thread {

    private final int id;
    public long refeicoesComidas = 0;
    private final Random random = new Random();

    public Filosofo(int id) {
        super("Filosofo-" + id);
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted() && System.currentTimeMillis() < JantarDosFilosofos.TEMPO_FIM_SIMULACAO) {

                // 1. PENSAR
                pensar();

                // 2. FICAR FAMINTO e PEGAR GARFOS
                JantarDosFilosofos.pegarGarfos(id);

                // O código só continua aqui se o filósofo conseguiu o semáforo individual (s[id].acquire())

                // 3. COMER
                comer();

                // 4. LARGAR GARFOS (libera os vizinhos famintos, se possível)
                JantarDosFilosofos.largarGarfos(id);
            }
        } catch (InterruptedException e) {
            // Fim da simulação
            System.out.println(String.format("[%s] [Filosofo-%d] Interrompido.", LocalTime.now(), id));
        }
    }

    private void pensar() throws InterruptedException {
        int tempoPensar = random.nextInt(JantarDosFilosofos.THINK_MAX_MS - JantarDosFilosofos.THINK_MIN_MS + 1)
                + JantarDosFilosofos.THINK_MIN_MS;

        System.out.println(String.format("[%s] [Filosofo-%d] Pensando por %d ms.",
                LocalTime.now(), id, tempoPensar));
        Thread.sleep(tempoPensar);
    }

    private void comer() throws InterruptedException {
        int tempoComer = random.nextInt(JantarDosFilosofos.EAT_MAX_MS - JantarDosFilosofos.EAT_MIN_MS + 1)
                + JantarDosFilosofos.EAT_MIN_MS;

        System.out.println(String.format("[%s] [Filosofo-%d] Comendo por %d ms.",
                LocalTime.now(), id, tempoComer));
        Thread.sleep(tempoComer);
        refeicoesComidas++;
    }
}