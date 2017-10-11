//Alex Karacaoglu
//PS10 

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <string.h>

//Function declerations
int searchPosition(int i);
void * find(void *pat);

#define TEXT_LEN 1000000
#define numThreads 10    

//Global variables
char *pattern;
char text[TEXT_LEN];
int textLength = TEXT_LEN;
int patternLength;
int whichThread = 0;
int offset = TEXT_LEN/numThreads;
int result = -1;
pthread_mutex_t lock;


int main(int argc, char *argv[]) {   
   char* pattern = argv[1];
   patternLength = strlen(pattern);
   int count = 0;
   while (count < TEXT_LEN) {
       int status = scanf("%c", &text[count]);
       count++;
       if (status == EOF) {
          textLength = count;
          break;
       }
   }
    pthread_mutex_init(&lock,NULL);
    pthread_t tid[numThreads];
    for(int i=0;i<numThreads;i++){
        whichThread = i;
        pthread_create(&tid[i],NULL,find,pattern);
        if (result !=-1){
            break;
        }
    }
    if (result==-1){
       printf("Pattern not found\n");
    }
    else{
       printf("\nPattern begins at character %d\n", result);
    }
}

//Same exact function as the provided basicsearch.c
int searchPosition(int i) {
   int j;
   for (j=0;j<strlen(pattern); j++) 
       if (text[i+j] != pattern[j])
          return 0;
   return 1;
}

void * find(void * pat){
    pthread_mutex_lock(&lock); 
    int j = whichThread;
    for(int position= j*offset;position<=((j*offset)+offset);position++){
        if(searchPosition(position)==1){
            result = position;
            break;
        }
    }
    pthread_mutex_unlock(&lock);
}



