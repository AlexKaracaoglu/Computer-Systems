/*  

Alex Karacaoglu    
calendar.c
When you run, you will be prompted to enter which day of the week you will start on and then how many days will be in that month

*/

#include <stdio.h>
int main(){
printf("Enter starting day of the week (Sat=1, Sun=7): ");
int s = getchar();
printf("\n");
printf("Enter the number of days in the month: ");
int a;
scanf("%d",&a);
int start = (s - '0');
int width = (start * 3) ;
int count = start;
int i = 1;
while(i <= a){
        if(i==1){
            printf("%*d", width, i);}
        if(i > 1){
            if(count % 8 == 0){
                printf("\n");
                count = count + 1;}
            if(count % 8 != 0){
                printf("%3d",i);}}
        count = (count+1);
        i = i + 1;
        }
    }     
      

    
