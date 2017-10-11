/* Alex Karacaoglu
   PS6
*/


#include <stdio.h>
#include <string.h>
#include <stdlib.h>



struct vstack{
    int stack[50];
    int top;
};

struct ostack{
    char *stack[50];
    int top; 
};

void pushValStack(struct vstack *s, int val){
    int top = s->top + 1;
    s->top = top;
    s->stack[top] = val;
};

int popValStack(struct vstack *s){
    int x = s->stack[s->top];
    s->top = s->top - 1;
    return x;
};

void pushOpStack(struct ostack *s, char *op){
    int top = s->top + 1;
    s->top = top;
    s->stack[top] = op;
};

char *popOpStack(struct ostack *s){
    char * x = s->stack[s->top];
    (s->top) = s->top - 1;
    return x;
};


int isNumber(char *x){
    if ((strcmp(x, "+") == 0) || (strcmp(x, "x") == 0) || (strcmp(x, "[") == 0) || (strcmp(x, "]") == 0))
        return 0;
    else
        return 1;
};
    

int main(int argc, char *argv[]){
    char j;
    int sum;
    int product;
    struct vstack value = {{},0};
    struct ostack operator = {{},0};
    struct vstack *valuePointer = &value;
    struct ostack *opPointer = &operator;
    int i = 1;
    while(i<argc){  
      if (isNumber(argv[i]) == 1)
          pushValStack(valuePointer,atoi(argv[i]));
        if ((strcmp(argv[i], "[") == 0) || (strcmp(argv[i], "+") == 0) || (strcmp(argv[i], "x") == 0))
            pushOpStack(opPointer,argv[i]);
        if (strcmp(argv[i], "]") == 0)
            j = popOpStack(opPointer);
            while (strcmp(j,"[") != 0){
                int v1 = popValStack(valuePointer);
                int v2 = popValStack(valuePointer);
                if (strcmp(j,"+") ==0)                  
                    sum = v1 + v2;
                    pushValStack(valuePointer,product);          //SEGMENTATION FAULT
                if (strcmp(j,"x") ==0)               
                    product = v1*v2;
                    pushValStack(valuePointer,product);
                j = popOpStack(opPointer);}
        i= i+1; }
       
    int result = popValStack(valuePointer);
    printf("%d",result);
}
                             
 
  
    


