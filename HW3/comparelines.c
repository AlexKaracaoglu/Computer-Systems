/*  

Alex Karacaoglu    
comparelines.c

*/

#include <stdio.h>
int main(){
    int a[40];
    int b[40];
    int sizea = 0;
    int sizeb = 0;
    int isreturn = 'a'; //This variable helps distinguish which array to fill
    int smalla = 0;
    int smallb = 0;
    int comparea = 0;
    int compareb = 0;
    int creturn = '\n';
    int c = getchar();
    while(c > 0){ //Keep reading til reach EOF
        if (c == creturn){
            isreturn = 'b'; //Switch to filling b if we reach a '\n'
            c = getchar();
        }
        if ((isreturn == 'a') && (sizea <= 40)){ //Filling a
            *(a + sizea) = c;
            sizea = sizea + 1;
            c = getchar();
        }
        if ((isreturn == 'b') && (sizeb <= 40)){ //Filling b
            *(b + sizeb) = c;
            sizeb = sizeb + 1;
            c = getchar();
        }
    }
    for (int i = 0; (i < sizea) && (i < sizeb); i++){
        if ((*(a + i)) != (*(b + i))){
            comparea = *(a + i);
            compareb = *(b + i);
            break;
        }
    }
    if ((comparea == compareb) && (sizea = sizeb)){
        printf("\nThese lines are lexicographically the same!");
    }
    else if (comparea > compareb){
        printf("\nThe second line is lexicographically smaller!");
    }
    else if ((comparea == compareb) && (sizea > sizeb)){
        printf("\nThe second line is lexicographically smaller!");
    }
    else{
        printf("\nThe first line is lexicographically smaller!");
    }
}
        
            
