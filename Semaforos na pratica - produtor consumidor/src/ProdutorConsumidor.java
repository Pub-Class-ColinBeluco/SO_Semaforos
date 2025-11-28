import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ProdutorConsumidor {

    static ArrayList<Integer> buffer = new ArrayList<>();
    static Semaphore mutex = new Semaphore(1);
    static Semaphore empty;
    static Semaphore full;
    static int item;        // Será retornado na execução do método do consumidor
    public static int produtores = 0,
            consumidores = 0,
            bufferSize = 0,
            itensPorProdutor = 0,
            min_delayProdutor = 0,
            max_delayProdutor = 0,
            min_delayConsumidor = 0,
            max_delayConsumidor = 0;

    public static void main(String[] args) throws Exception {
        abrirArquivoInput("input.txt");
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

                Thread.sleep(min_delayProdutor);     // atraso simples
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void consumidor(int id) {
        try {
            while (true) {
                boolean acquire = full.tryAcquire(max_delayConsumidor+5, TimeUnit.SECONDS);    //Espera item
                if(acquire)  {
                    mutex.acquire();    // entra na região crítica

                    item = buffer.remove(0);
                    System.out.println(LocalTime.now() + " [Consumidor-" + id + "] consumiu " + item);

                    mutex.release();
                    empty.release();    // libera vaga

                    Thread.sleep(max_delayProdutor);    // atraso simples
                }   else {
                    System.out.println(LocalTime.now() + " Consumidor-" + id + " Saindo do loop, tempo esgotado");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void abrirArquivoInput(String nomeArquivo) {
        Properties prop = new Properties();
        try {
            BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo));
            prop.load(leitor);
            produtores = Integer.parseInt(prop.getProperty("produtores"));
            consumidores = Integer.parseInt(prop.getProperty("consumidores"));
            bufferSize = Integer.parseInt(prop.getProperty("buffer"));
            itensPorProdutor = Integer.parseInt(prop.getProperty("itens_por_produtor"));
            String[] delayProdutor = prop.getProperty("delay_produtor_ms").split("-");
            min_delayProdutor = Integer.parseInt(delayProdutor[0]);
            max_delayProdutor = Integer.parseInt(delayProdutor[1]);
            String[] delayConsumidor = prop.getProperty("delay_consumidor_ms").split("-");
            min_delayConsumidor = Integer.parseInt(delayConsumidor[0]);
            max_delayConsumidor = Integer.parseInt(delayConsumidor[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
