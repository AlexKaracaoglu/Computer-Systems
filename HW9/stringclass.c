//Alex Karacaoglu -- PS9

#include <stdio.h>
#include <stdlib.h>
#include "stringclass.h"

//The string structure will have a pointer and the size
struct string{
    char * pointer;
    int size;
};

//Creates a new string value from a char array
String String_new(char * a){
    String new = (struct string*) malloc(sizeof(struct string));
    int length = 0;
    while (a[length] != '\0'){
        length = length + 1;
    }
    char* p = (char*) malloc(length);
    new->size = length;
    new->pointer = p;
    for (int i = 0; i < length; i++){
        new->pointer[i] = a[i];
    }
    return new;
}

//Copes the values in one string to another and returns that new one, note: the old string is not changed
String String_copy(String s){   
    String new = (struct string*) malloc(sizeof(struct string));
    new->size = s->size;
    new->pointer = (char*) malloc(s->size);
    int end = s->size;
    for (int i = 0; i < end; i++){
        new->pointer[i] = s->pointer[i];
    }
    return new;
}

//Takes in a string and returns its size, which is part of the structure itself
int String_length(String s){
    return s->size;
}

//Takes 2 strings and concats the second to the end of the first, but it does this is a new string so the orignals are not changed
String String_concat(String s1, String s2){
    int totalSize = s1->size + s2->size;
    String new = (struct string*) malloc(sizeof(struct string));
    new->size = totalSize;
    new->pointer = (char*) malloc(totalSize);
    int s1Size = s1 -> size;
    for (int i = 0; i < s1Size; i++){
        new->pointer[i] = s1->pointer[i];
    }
    int j = 0;
    for (int i = s1Size; i < totalSize; i++){
        new->pointer[i] = s2->pointer[j];
        j = j + 1;
    }
    return new;
}

//Compares two strings and returns ints corresponding to which is lexicographically larger. This code corresponds to code shown in class as to an easy way to evaluate this problem 
int String_compare(String s1, String s2){
    int i = 0;
    while ((s2->pointer[i] != '\0') && (s1->pointer[i] != '\0') && (s1->pointer[i] == s2->pointer[i])){
        i++;
    }
    return s1->pointer[i] - s2->pointer[i];
    }

//Takes in a string and returns a pointer to the array that contains the chars
char * String_tochararray(String s){
    return s->pointer;
}

//Frees the string from the heap
void String_free(String s){
    free(s);
}

