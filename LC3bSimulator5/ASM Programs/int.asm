	.ORIG x1200
	STW R0, R6, #-2
	STW R1, R6, #-4
	STW R2, R6, #-6

	LEA R0, PT		;R0 = PT
	LDW R0, R0, #0
	LEA R2, PS		;R2 = counter
	LDW R2, R2, #0
	LDW R2, R2, #0

back LDW R1, R0, #0
	AND R1, R1, #-2	;set R=0
	STW R1, R0, #0
	ADD R0, R0, #2
	ADD R2, R2, #-1 ;decrement counter
	BRp back

	LDW R0, R6, #-2
	LDW R1, R6, #-4
	LDW R2, R6, #-6
	RTI
PT	.FILL x1000
PS	.FILL x0080
	.END