; ALEX KARACAOGLU
;
; 5c
                                 
main:	setvalue r0 20			;allocate space for 5 ints
		subtract r15 r15 r0
		setvalue r1 0			;r1 = 0
		add r1 r1 r15			;r1 = pointer to start of array
		pushreg r1				;push r1 onto stack			
		jumpsub populate		;populate array
		restore r1				;r1 still points to the start of the array, restore it
		jumpsub test			;perform the test with the array, return 0 or 1 (store in r14)
		setvalue r13 0			;r13 = 0
		compare r14 r13			;compare r14 to r13, r14 is the return from test: 1 if result > 100, 0 if result <= 100
		jumpeq n				;if r14 == 0 jump to n       (result <= 100)
		jumpne y				;if r14 /= 0 (r14 == 1) jump to y      (result > 100)

n:		setvalue r12 78			;r12 = 'N'
		system write r12			;print 'N'
		system exit

y:		setvalue r12 89			;r12 = 'Y'
		system write r12			;'print 'Y'
		system exit

; populate function

populate:	setvalue r2 5		;r2 = 5 = len	
		setvalue r4 1			;r4 = 1, used to increment r5 == i
		setvalue r5 0			;i = 0, used to compare to 5 to see how many elts are in the array, as well as find the positional offset

while:	compare r2 r5			;compare 5 and r5		while r5 < 5 == i < 5   	
		jumpeq ret				;if r5 == 5 jump down and return
		setvalue r6 4			;r6 = 4 
		multiply r6 r6 r5		;r6 = 4 * i     "offset", this way of thinking of offset is in the Prof's writeup
		add r7 r6 r1			;r7 = r6  + address of first thing in array
		pushreg r7				;push all used variables for later use
		pushreg r6
		pushreg r5
		pushreg r4
		pushreg r2     
		pushreg r1
		jumpsub getint			;getint, store the int in r14 by convention
		restore r1				;restore variables
		restore r2
		restore r4
		restore r5
		restore r6
		restore r8
		store r14 r7			;r7 points to r14
		load r10 r7				;deal with the int so we can use it later
		add r5 r5 r4			;r5++ == i++			
		jump while

ret:	return

; test function

test:	setvalue r0 0			;i = 0: used to compare #elts in array to 5 and to find the positional offset      ^^^^ r0,r1,r2 are used
		setvalue r2 5			;r2 = len = 5
		setvalue r3	0			;result = 0
		setvalue r4 1			;used to increment i (r0)

while2: compare r2 r0			;compare r2 to r0, compare len to i,    while i < 5 continue
		jumpeq ret2				;if equal, jump to ret2
		setvalue r5 4			;4 is how much we need to move by in memory bc ints take up 4
		multiply r5 r5 r0		;r5 = r5 * i --> pointer to a[i]   "offset"
		add r6 r1 r5			;r6 = r1 + r5, r1 is pointer to the first elt. in the array
		load r7 r6				;r7 stores whatever was in r6 and this is a[i]
		add r3 r3 r7			;result = result + whatever is in r7
		add r0 r0 r4			;i++
		jump while2

ret2:	setvalue r2 100			;r2 = 100	
		compare r3 r2			;compare result to 100
		jumpgt o				;if result > 100 jump o   (one)
		jumple z				;else jump z			  (zero)

z:		setvalue r14 0			;set r14 to 0
		return

o:		setvalue r14 1			;set r14 to 1
		return

; --- code for the getint function, as seen on canvas ---

getint: setvalue r0 32 ; ' '
    setvalue r1 13     ; '\n'

   ;  -- skip over whitespace --
G1: system read r3     ; r3 = c
    compare  r3 r0
    jumpeq   G1
    compare  r3 r1
    jumpeq   G1

   ; -- handle the +/- signs --
    setvalue r4  1     ; r4 = sign
    setvalue r0 43     ; r0 = '+'
    compare  r3 r0     ; test for '+'
    jumpne   G2
    system  read r3    ; read another (skip over the +)
G2: setvalue r0 45     ; r0 = '-'
    compare  r3 r0     ; test for '-'
    jumpne   G3
    setvalue r4 -1     ; set sign = -1
    system   read r3   ;   and read another char

  ; -- read the digits --
G3: setvalue r0 48     ; r0 = '0'
    setvalue r1 57     ; r1 = '9'
    setvalue r2 10     ; r2 = 10
    setvalue r5 0      ; r5 = result = 0

G4: compare  r3 r0     ; if c < '0'
    jumplt   G5        ;    exit loop
    compare  r3 r1     ; if c > '9'
    jumpgt   G5        ;    exit loop
    multiply r5 r5 r2  ; result = result * 10
    subtract r6 r3 r0  ; r6 = c - '0'
    add      r5 r5 r6  ; result = result + r6
    system   read r3   ; read another character
    jump     G4        ; continue loop

   ; -- return the result --
G5: multiply r14 r5 r4 ; result = result * sign
    return
