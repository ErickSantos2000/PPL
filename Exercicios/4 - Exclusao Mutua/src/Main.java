import java.util.concurrent.Semaphore;

public class Main {

    public static int count = 100000;
    public static final int interacoes = 20000;

    private static Semaphore semaforo = new Semaphore(1);

    public static void main(String[] args) {

        Thread t1 = new Thread(new FuncaoHelper(1));
        Thread t2 = new Thread(new FuncaoHelper(2));
        Thread t3 = new Thread(new FuncaoHelper(3));
        Thread t4 = new Thread(new FuncaoHelper(4));
        Thread t5 = new Thread(new FuncaoHelper(5));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        System.out.println("Thread principal aguardando...");

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("============================");
        System.out.println("Valor esperado de count = 0");
        System.out.println("Valor final de count = " + count);
        System.out.println("============================");
    }

    static class FuncaoHelper implements Runnable {
        private int idThread;

        public FuncaoHelper(int id) {
            this.idThread = id;
        }

        @Override
        public void run() {
            for (int i = 0; i < interacoes; i++) {

                try {
                    semaforo.acquire();
                    count--;
                    semaforo.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (i % 5000 == 0) {
                    System.out.printf("[THREAD %d] Count = %d%n", idThread, count);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.printf("THREAD %d FINALIZADA.%n", idThread);
        }
    }
}

