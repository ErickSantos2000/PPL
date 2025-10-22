#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

#define NUM_THREADS 5
#define interacoes 20000

int count = 100000;

void* funcaoHelper (void* idThread) {
    int id = (int)(long) idThread; 
    
    for (int i = 0; i < interacoes; i++) {
        count--; 
        
        if ((i % 5000) == 0) {
            printf("THREAD %d / Count: %d\n", id, count);
            sleep(1);
        }
    }

    printf("THREAD %d FINALIZADA.\n", id);
    pthread_exit(NULL);
}

int main (int argc, char *argv []) { 
    pthread_t vetorThreads [NUM_THREADS];
    int statusRetorno;

    printf("--- MAIN: CRIANDO THREADS ---\n");

    for (int i = 0; i < NUM_THREADS; i++) {
        printf ("MAIN: Criando a thread %d\n", i);
        statusRetorno = pthread_create (&vetorThreads[i], NULL, funcaoHelper, (void*) (long) i);

        if (statusRetorno) {
            fprintf(stderr, " Erro ao criar a thread : %d \n", statusRetorno);
            exit(EXIT_FAILURE);
        }
    }
    
    printf("\nMAIN\n");

    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(vetorThreads[i], NULL);
    }

    printf("\n============================\n");
    printf("O valor esperado: 0\n");
    printf("Valor final de count: %d\n", count); 
    printf("============================\n");

    return 0;
}
