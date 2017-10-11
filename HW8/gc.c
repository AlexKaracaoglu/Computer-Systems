//Alex Karacaoglu
//HW8

#include "gc.h"
#include "heapmgr.h"
#include <stdio.h>

void *stackBottom() {
   unsigned long bottom;
   FILE *statfp = fopen("/proc/self/stat", "r");
   fscanf(statfp,
          "%*d %*s %*c %*d %*d %*d %*d %*d %*u "
          "%*u %*u %*u %*u %*u %*u %*d %*d "
          "%*d %*d %*d %*d %*u %*u %*d "
          "%*u %*u %*u %lu", &bottom);
   fclose(statfp);
   return (void *) bottom;

}

//Followed the marking and sweeping algs as in the notes

void mark(int*p){
    int * first = firstBlock();             //find first and last blocks
    int * last = lastBlock();
    if ((p >= first) && (p <= last)){       //if the pointer is in between these addresses *note: everything is looked at as a pointer, so we cast a as a pointer
        int * a = p;                        
        if (isAllocated(a-1) == 1){         //if the address is allocated, you change the header
            int l = (*a)/8*8;               //to be congruent to 3 mod 4
            *(a-1) = *(a-1) + 2;            //marking the block
            for(int i=0;i<l;i++){
                int * ptr = (int*) *(a+i);
                mark(ptr);                  //move to next address and call again
            }
        }
    }
    else{
        return;                             
    }
}

void sweep(){
    int * first = firstBlock();             //find the first and last blocks
    int * last = lastBlock();
    while (first != last){                  //if the blocks header is not 3 mod 4, means its not marked then deallocate it
        if (*(first)%4 !=3){                
            *first = (*first) / 2 * 2;
        }
        first = nextBlock(first);           //move on to next block and check again
    }   
    coalesce();                             
}

void gc(){                                  
    int * top;                              //set a pointer to the top and the bottom
    int ** topAddr = &top;                  
    int * bottomAddr = stackBottom();   
    while (topAddr < bottomAddr){           
        mark(*topAddr);                     //go through marking all the relevant addresses and repeat the process until you get to the end
        topAddr = topAddr + 1;
    }
    sweep();                                //sweep and you are done!
}
        
    
        
