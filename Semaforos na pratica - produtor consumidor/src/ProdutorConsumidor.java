import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ProdutorConsumidor {

    static ArrayList<Integer> buffer = new ArrayList<>();
    static Semaphore mutex = new Semaphore(1);
    static Semaphore empty;
    static Semaphore full;

    public static void main(String[] args) throws Exception {

        int produtores = 3;
        int consumidores = 3;
        int bufferSize = 10;
        int itensPorProdutor = 20;

        empty = new Semaphore(bufferSize); // vagas
        full = new Semaphore(0); // itens

        // Mostrar host
        String host = java.net.InetAddress.getLocalHost().getHostName();
        System.out.println("Executando em: " + host);

        // Criando produtores
        for (int i = 0; i < produtores; i++) {
            int id = i;
            new Thread(() -> produtor(id, itensPorProdutor)).start();
        }

        // Criando consumidores
        for (int i = 0; i < consumidores; i++) {
            int id = i;
            new Thread(() -> consumidor(id)).start();
        }
    }

    static void produtor(int id, int total) {
        try {
            for (int i = 1; i <= total; i++) {

                empty.acquire();     // espera vaga
                mutex.acquire();     // entra na região crítica

                buffer.add(i);
                System.out.println(LocalTime.now() + " [Produtor-" + id + "] produziu " + i);

                mutex.release();
                full.release();      // libera item

                Thread.sleep(5);     // atraso simples
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void consumidor(int id) {
        try {
            while (true) {
                full.acquire();     // espera item
                mutex.acquire();    // entra na região crítica

                int item = buffer.remove(0);
                System.out.println(LocalTime.now() + " [Consumidor-" + id + "] consumiu " + item);

                mutex.release();
                empty.release();    // libera vaga

                Thread.sleep(5);    // atraso simples
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
