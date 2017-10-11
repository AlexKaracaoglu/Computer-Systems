// Alex Karacaoglu
// PS7
// heapmgr.c


#include <stdio.h>
#include <stdlib.h>
#include "heapmgr.h"


void * myaclloc(int length);
void initmemory(int size);
int* firstBlock();
int isAllocated(int *p);
int * nextBlock(int* p);
void printallocation();
void myfree(void * ptr);
void coalesce();


int* first;

void * myalloc(int length){
    int space = length + 12;
    int mod = length % 8;
    space = space + (8 - mod);
    int * a = first;
    int * b = first - 1;
    int size = *b;
    while(length != 0){
        if (isAllocated(a)==0 && size > space){
            *b = space;
            *(a-1) = *(a-1)+1;
            *(nextBlock(a) -1) = size - space;
            return a;
        }
        if (size == space && isAllocated(a) == 0){
            *(a-1) = *(a-1)+1;
            return a;
        }
        else{
            a = nextBlock(a);
            b = a - 1;  
            size = *b;
        }
    }
    return NULL;
}

void initmemory(int size){
    int space = size + 12;
    int mod = size % 8;
    space = space + (8 - mod);
    first = (int *)malloc(space);
    int * head = first - 1;
    *head = space;
    int * next = nextBlock(first);
    next = next - 1;
    *next = 0;
}

int* firstBlock(){  //Implemented it bc Prof said to in writeup, but never used it,
    return first;  //Just used the global variable first
}

int isAllocated(int *p){
    int * q = p;
    q = q - 1;
    int mod = (*q) % 2;
    if (mod == 0){
        return 0;}
    else return 1;
}

int * nextBlock(int* p){
    int * q = p;
    q = q - 1;
    int * next = q + ((*q) / 4);
    return next;
}

void printallocation(){
    int size = *(first - 1);
    int * iter = first;
    int a = -1;
    while(*(iter - 1)!=0){
        a = a + 1;
        if (isAllocated(iter)==0){
            printf("block: %d, size: %d. Unallocated \n",a,size);
        }
        else{
            printf("block: %d, size: %d. Allocated \n",a,size);
        }
    iter = nextBlock(iter);
    size = *(iter-1);
    }
    printf("\n");
}

void myfree(void * ptr){
    int * q = ptr;
    q = q - 1;
    (*q) = (*q) - 1;
}

void coalesce(){
    int* iter1 = first;
    int * iter2;
    while (*iter1 != 0){
        iter2 = nextBlock(iter1);
        while(isAllocated(iter1) == 0 && isAllocated(iter2)==0 && *(iter2 - 1) != 0){
            int * q = iter1;
            q = q - 1;
            *q = *q + *(iter2 - 1);
            iter2 = nextBlock(iter2);
        }
    iter1 = iter2;
    }
    return NULL;
}





            

            
    
    




 
            
    






    
    


    
    
    
    
    


