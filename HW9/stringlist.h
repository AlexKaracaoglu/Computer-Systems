// stringlist.h: The API for the data type List,
//               whose elements are of type String,
//               and in sorted order.

#ifndef STRINGLIST_H
#define STRINGLIST_H

// Define the type List as a pointer to a struct.
// The definition of the struct is in the implementing c file.

typedef struct listnode *List;

#endif

#include "stringclass.h"

// Function List_new allocs space for a new list, 
// and returns a pointer to it.

List List_new();

// Function List_insert nserts the string s into list L,
// in sorted order. 

void List_insert(List L, String s);


// Function List_contains returns 1 if the list 
// contains the specified string.

int List_contains(List L, String s);

// Function List_get returns the nth string on the list.
// The first string on the list has n=0.

String List_get(List L, int n);

// Function List_size returns the number of elements on the list.

int List_size(List L);


// Function List_free frees space previously malloc'ed 
// for the list. For each node in the chain, it should 
// free the node itself as well as its string.

void List_free(List L);

