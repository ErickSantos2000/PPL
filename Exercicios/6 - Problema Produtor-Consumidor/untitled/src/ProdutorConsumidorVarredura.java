import java.util.concurrent.Semaphore;
import java.util.Random;

public class ProdutorConsumidorVarredura {

    public static int[] vetor = new int[5];
    public static Semaphore[] travas = new Semaphore[5];

    public static void main(String[] args) {

        for(int i = 0; i < 5; i++) {
            travas[i] = new Semaphore(1);
        }

        Thread tProdutor = new ThreadProdutor();
        Thread tConsumidor = new ThreadConsumidor();

        tProdutor.start();
        tConsumidor.start();

        try {
            tProdutor.join();
            tConsumidor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("=== Fim da Execução ===");
        System.out.println("Estado final do vetor: " + java.util.Arrays.toString(vetor));
    }
}

class ThreadProdutor extends Thread {
    Random random = new Random();

    @Override
    public void run() {
        for (int k = 0; k < 100; k++) {

            int valorGerado = random.nextInt(100) + 1;
            boolean conseguiuInserir = false;

            for (int i = 0; i < 5; i++) {
                try {
                    ProdutorConsumidorVarredura.travas[i].acquire();

                    if (ProdutorConsumidorVarredura.vetor[i] == 0) {
                        ProdutorConsumidorVarredura.vetor[i] = valorGerado;
                        System.out.println("Produtor inseriu " + valorGerado + " na posição " + i);
                        conseguiuInserir = true;

                        ProdutorConsumidorVarredura.travas[i].release();
                        break;
                    }

                    ProdutorConsumidorVarredura.travas[i].release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!conseguiuInserir) {
                System.out.println("Produtor descartou o valor: " + valorGerado + " (Vetor cheio)");
            }

            try { Thread.sleep(50); } catch (Exception e) {}
        }
    }
}

class ThreadConsumidor extends Thread {
    @Override
    public void run() {
        for (int k = 0; k < 100; k++) {

            boolean conseguiuConsumir = false;

            for (int i = 0; i < 5; i++) {
                try {
                    ProdutorConsumidorVarredura.travas[i].acquire();

                    if (ProdutorConsumidorVarredura.vetor[i] != 0) {
                        int valorLido = ProdutorConsumidorVarredura.vetor[i];
                        ProdutorConsumidorVarredura.vetor[i] = 0;

                        System.out.println("\tConsumidor removeu " + valorLido + " da posição " + i);
                        conseguiuConsumir = true;

                        ProdutorConsumidorVarredura.travas[i].release();
                        break;
                    }

                    ProdutorConsumidorVarredura.travas[i].release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try { Thread.sleep(50); } catch (Exception e) {}
        }
    }
}
