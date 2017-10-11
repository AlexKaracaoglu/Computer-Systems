//Alex Karacaoglu -- PS9

#include <stdio.h>
#include <stdlib.h>
#include "stringlist.h"

struct listnode{
    String s;
    struct listnode * next;
};

List List_new(){
    List header = (struct listnode*) malloc(sizeof(struct listnode));
    header->s = NULL;
    header->next = NULL;
    return header;
}

//Takes a list and a string and adds a node on in its correct alphabetical order and re sets the chain accordingly
void List_insert(List L, String s){
    List node = L;
    String compare = L->s;
    List old;
    while(String_compare(compare,s)<0){
        old = node;
        node = node->next;
        compare = node->s;
    }
    List insert;
    insert->s = s;
    insert->next=node;
    old->next = insert;
}

//Goes through the List and compares each string value of the node to the input string and if equal return a 1, if you reach the end, return 0
int List_contains(List L, String s){
    List node = L;
    while (node != NULL){
        if(String_compare(node->s, s)==0){
            return 1;}
        node = node->next;
    }
    return 0;
}

//Iterates through the list, and whrn the count == n, the string that we are at is returned
String List_get(List L, int n){
    int counter = 0;
    List node = L;
    while (counter!=n){
        node = node->next;
        counter = counter+1;
    }
    return node->s;
}

//Iterates through the list and when we get to the end (next == NULL) you return the count
int List_size(List L){
    int count = 0;
    List node = L;
    while (node!=NULL){
        count = count+1;
        node = node->next;
    }
    return count;
}

//Free the list
void List_free(List L){
    free(L);
}


 


    

        

        
    
    
    
