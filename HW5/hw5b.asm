; ALEX KARACAOGLU
;
; 5b
                          
main:	setvalue r0 20			;allocate space for 5 ints
		subtract r0 r15 r0
		setvalue r1 5			;len = 5
		setvalue r2 1			;r2 = 1, used in increment i
		setvalue r3 0			;int i = 0 (while loop and for comparing to r1 == 5)
		setvalue r5 0			;result == 0

while:	compare r1 r3			;check if you have read in 5 ints
		jumpeq ret				;if r3 = 5 then jump out of loop
		pushreg r5				;push all declared registers onto stack
		pushreg r3
		pushreg r2
		pushreg r1
		pushreg r0			
		jumpsub getint			;jump to get int, store int in r14 by convention
		restore r0				;restore all the pushed registers
		restore r1
		restore r2
		restore r3
		restore r5
		setvalue r6 4			;r6 = 4
		multiply r6 r3 r6		;r6 = i * 4
		add r7 r0 r6			;r7 = address of a[n]
		store r14 r7			;r7 points to r14
		load r8 r7				;load value from r14 (getint) into r8
		add r5 r5 r8			;result = result + value of r8
		add r3 r3 r2			;i++
		jump while				

ret:	setvalue r10 100		;r10 = 100	
		compare r10 r5			;compare 100 and result
		jumplt y				;if 100 < result, jump to y
		jumpge n				;else if 100 >= result, jump to n

y:		setvalue r11 89			;r11 = 'Y'	
		system write r11			;print 'Y'
		system exit

n:		setvalue r11 78			;r11 = 'N'
		system write r11			;print 'N'
		system exit

; --- code for the getint function, taken right from canvas ---

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
		
			
		