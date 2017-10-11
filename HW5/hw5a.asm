; ALEX KARACAOGLU
;
; 5a 
; 2 ints and return Y if result > 100 and N if result <= 100
                                                 
main:	setvalue r0 8			;allocating space for 2 ints
		subtract r13 r15 r0		;r13 = address of start of array, stays constant
		setvalue r1 0			;first int has offset 0
		add r1 r1 r13			;r1 = address of first int, gets changed, initially is address of a[0]
		pushreg r1				;push r1,r13 on stack
		pushreg r13
		jumpsub getint			;call getint, store int in r14 by convention
		restore r13				;restore address of start of array
		restore r1				;restore the address of a[0]
		store r14 r1			;r1 points to r14
		load r3 r1				;r3 holds the int that was read
		setvalue r2 4			;second int has offset 4 from beginning of array
		add r2 r2 r13			;r2 is address of a[1], a[1] = a + 4 (a is start of array)
		pushreg r3				;push r3 to save that value
		pushreg r2				;push r2 to store address of a[1]
		jumpsub getint			;call getint, store int in r14 by convention
		restore r2				;restore what was pushed
		restore r3
		store r14 r2			;r2 points to r14
		load r4 r2				;r4 holds the int that was read in, is a[1]
		setvalue r5 0			;result == 0	
		add r5 r3 r4			;result = a[0] + a[1] = r3 + r4
		setvalue r6 100			;set r6 to 100
		compare r6 r5			;compare 100 to sum
		jumplt y				;if 100 < sum jump to y
		jumpge n				;	else jump to n

y:		setvalue r7 89			;if sum > 100, r7='Y'
		system write r7			;print 'Y'
		system exit

n:		setvalue r8 78			;if sum <= 100, r7 = 'N'
		system write r8			;print 'N'
		system exit

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




