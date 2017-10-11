/*  

Alex Karacaoglu    
last10.c

*/

#include <stdio.h>
int main(){
    int a[10];
    int count = 0; // Total count
    int* p = a;

    int countmod10 = 0; 
    
    int c; 
    scanf("%d",&c);

    while (c != 0){ // Keep reading until you see a 0
        if (p > (a + 9)){
            p = a;         
        }
    * p = c;
    p = p + 1;
    count = (count + 1);
    scanf("%d",&c);
    }
    if (count > 10){ //If the final count > 10
        countmod10 = count % 10; 
        for (int i = countmod10; i < 10; i++){ //countmod10 is where you start printing
            printf("%d ", *(a + i));
        }
        for (int j = 0; j < countmod10; j++){ //Once you get to 10 you wrap around and print til countmod10
            printf("%d ", *(a + j));
        }
    }
    else{
        for (int k = 0; k < count; k++){ //If count<10 print from a[0] to a[count-1]
            printf("%d ", *(a + k));
        }
    }
}
   
    
