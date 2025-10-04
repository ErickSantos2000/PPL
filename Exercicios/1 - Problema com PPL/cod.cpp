#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

int count = 100000; // variavel global compartilhada

void* decrementar1(void* ptr) {
    for (int i = 0; i < 50000; i++) {
        count--;
    }
    pthread_exit(NULL);
}

void* decrementar2(void* ptr) {
    for (int i = 0; i < 50000; i++) {
        count--;
    }
    pthread_exit(NULL);
}

int main() {
    // declaração
    pthread_t t1, t2;

    // criação das threads
    if (pthread_create(&t1, NULL, decrementar1, NULL) != 0) {
        perror("Erro ao criar thread 1");
        exit(EXIT_FAILURE);
    }
    if (pthread_create(&t2, NULL, decrementar2, NULL) != 0) {
        perror("Erro ao criar thread 2");
        exit(EXIT_FAILURE);
    }

    // espera as threads terminarem
    pthread_join(t1, NULL);
    pthread_join(t2, NULL);

    // resultado final
    printf("============================\n");
    printf("Valor final de count = %d\n", count);
    printf("============================\n");

    return 0;
}