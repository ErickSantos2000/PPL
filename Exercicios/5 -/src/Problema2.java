
public class Problema2 {

    // A variável é compartilhada e NÃO tem proteção (volatile não resolve atomicidade)
    static int numero = 0;

    public static void main(String[] args) throws InterruptedException {

        // Thread que incrementa 100x
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                numero++;
            }
        });

        // Thread que decrementa 100x (com a condicional)
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                if (numero > 0) {
                    numero--;
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Valor Final: " + numero);
    }
}
