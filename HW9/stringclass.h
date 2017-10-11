// stringclass.h:  The API of the String data type.

#ifndef STRINGCLASS_H
#define STRINGCLASS_H

typedef struct string *String;

#endif

// Function String_new creates a new String value
// containing the specified characters, and returns it.

String String_new(char *a);


// Function String_copy creates a new String value
// having the same contents of String s.

String String_copy(String s);


// Function String_length returns the number of characters in s.

int String_length(String s);


// Function String_concat appends the contents of s1 and s2,
// creates a new String value having that contents, and returns it.

String String_concat(String s1, String s2);


// Function String_compare behaves the same as the strcmp function.

int String_compare(String s1, String s2);


// Function String_tochararray extracts the characters from the
// string s and returns the char* value.

char *String_tochararray(String s);


// Function String_free frees the space allocated for s.

void String_free(String s);



