	.ORIG x1200
	STW R0, R6, #-2
	STW R1, R6, #-4
	LEA R0, A
	LDW R0, R0, #0
	LDW R1, R0, #0
	ADD R1, R1, #1
	STW R1, R0, #0
	LDW R0, R6, #-2
	LDW R1, R6, #-4
	RTI
A	.FILL x4000
	.END
