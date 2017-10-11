/*  

Alex Karacaoglu    
wordcount.c
When you run, you will be prompted to enter a bunch of characters, it reads til EOF, so to get a result, you must type CTR-D. Or
if you run it with a text file (ie sonnets.txt) it will return a result immediately

*/

#include <stdio.h>
int main(){
int charC = 0;
int wordC = 0;
int lineC = 0;
int c = getchar();
int a = c;       
while (c >= 0){
    charC = charC + 1;
    if(c == '\n')
        lineC = lineC + 1;
    if(a >= 33 && a <=126 && (c > 126 || c < 33) )
        wordC = wordC + 1;
    a = c;
    c = getchar();}
printf("\nYour text has %d chars, %d lines, and %d words!\n",charC, lineC, wordC);}
