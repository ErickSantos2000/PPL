import java.util.concurrent.Semaphore;
import java.util.Random;

public class Problema1 {

    // O vetor compartilhado de 6 posições
    public static int[] vetor = new int[6];

    // Criamos 3 semáforos, um para cada par de índices.
    // "new Semaphore(1)" significa que apenas UMA thread passa por vez (Mutex).
    public static Semaphore semaforoPar01 = new Semaphore(1);
    public static Semaphore semaforoPar23 = new Semaphore(1);
    public static Semaphore semaforoPar45 = new Semaphore(1);

    public static void main(String[] args) {

        // --- 1. CRIAÇÃO DAS THREADS ---
        // Passamos: o semáforo que ela deve respeitar e as duas posições que ela pode mexer

        // Par 1 (mexem nos índices 0 e 1)
        Thread t1 = new ThreadSimples(semaforoPar01, 0, 1);
        Thread t2 = new ThreadSimples(semaforoPar01, 0, 1);

        // Par 2 (mexem nos índices 2 e 3)
        Thread t3 = new ThreadSimples(semaforoPar23, 2, 3);
        Thread t4 = new ThreadSimples(semaforoPar23, 2, 3);

        // Par 3 (mexem nos índices 4 e 5)
        Thread t5 = new ThreadSimples(semaforoPar45, 4, 5);
        Thread t6 = new ThreadSimples(semaforoPar45, 4, 5);

        // --- 2. EXECUÇÃO ---
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();

        // --- 3. ESPERA (JOIN) ---
        // O main espera cada uma terminar antes de seguir
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // --- 4. RESULTADO ---
        System.out.println("=== FIM DO PROCESSAMENTO ===");
        System.out.println("Vetor final: [" + vetor[0] + ", " + vetor[1] + ", " +
                vetor[2] + ", " + vetor[3] + ", " +
                vetor[4] + ", " + vetor[5] + "]");

        System.out.println("Soma Par 1 (0+1): " + (vetor[0] + vetor[1]));
        System.out.println("Soma Par 2 (2+3): " + (vetor[2] + vetor[3]));
        System.out.println("Soma Par 3 (4+5): " + (vetor[4] + vetor[5]));
    }
}

// Classe da Thread (Simples e direta)
class ThreadSimples extends Thread {

    Semaphore meuSemaforo;
    int posA; // Primeira posição que posso mexer
    int posB; // Segunda posição que posso mexer
    Random random = new Random();

    // Construtor: Recebe exatamente o que precisa para trabalhar
    public ThreadSimples(Semaphore s, int p1, int p2) {
        this.meuSemaforo = s;
        this.posA = p1;
        this.posB = p2;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {

            // Escolhe aleatoriamente entre a primeira ou segunda posição
            int posicaoEscolhida;
            if (random.nextBoolean()) {
                posicaoEscolhida = posA;
            } else {
                posicaoEscolhida = posB;
            }

            try {
                // Tenta pegar a chave do semáforo
                meuSemaforo.acquire();

                // --- SEÇÃO CRÍTICA (Só uma thread entra aqui por vez) ---
                Problema1.vetor[posicaoEscolhida]++;
                // --------------------------------------------------------

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Devolve a chave, não importa o que aconteça
                meuSemaforo.release();
            }
        }
    }
}