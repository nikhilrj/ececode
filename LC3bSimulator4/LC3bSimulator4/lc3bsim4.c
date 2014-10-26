/***************************************************************/
/*                                                             */
/*   LC-3b Simulator                                           */
/*                                                             */
/*   EE 460N                                                   */
/*   The University of Texas at Austin                         */
/*                                                             */
/***************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/***************************************************************/
/*                                                             */
/* Files:  ucode        Microprogram file                      */
/*         isaprogram   LC-3b machine language program file    */
/*                                                             */
/***************************************************************/

/***************************************************************/
/* These are the functions you'll have to write.               */
/***************************************************************/

void eval_micro_sequencer();
void cycle_memory();
void eval_bus_drivers();
void drive_bus();
void latch_datapath_values();

/***************************************************************/
/* A couple of useful definitions.                             */
/***************************************************************/
#define FALSE 0
#define TRUE  1

/***************************************************************/
/* Use this to avoid overflowing 16 bits on the bus.           */
/***************************************************************/
#define Low16bits(x) ((x) & 0xFFFF)

/***************************************************************/
/* Definition of the control store layout.                     */
/***************************************************************/
#define CONTROL_STORE_ROWS 64
#define INITIAL_STATE_NUMBER 18

/***************************************************************/
/* Definition of bit order in control store word.              */
/***************************************************************/
enum CS_BITS {                                                  
    IRD,
    COND1, COND0,
    J5, J4, J3, J2, J1, J0,
    LD_MAR,
    LD_MDR,
    LD_IR,
    LD_BEN,
    LD_REG,
    LD_CC,
    LD_PC,
    GATE_PC,
    GATE_MDR,
    GATE_ALU,
    GATE_MARMUX,
    GATE_SHF,
    PCMUX1, PCMUX0,
    DRMUX,
    SR1MUX,
    ADDR1MUX,
    ADDR2MUX1, ADDR2MUX0,
    MARMUX,
    ALUK1, ALUK0,
    MIO_EN,
    R_W,
    DATA_SIZE,
    LSHF1,
/* MODIFY: you have to add all your new control signals */
	LD_USP,
	GATE_USP,
	LD_SSP,
	SSPMUX1, SSPMUX0,
	GATE_SSP,
	LD_INTV,
	SPR,
	LD_SPR,
	GATE_SPR,
	LD_PSR,
	PSRMUX,
	GATE_PSR,
	GATE_VEC,
	COND2,
/* END MODIFIED BITS*/
    CONTROL_STORE_BITS
} CS_BITS;

/***************************************************************/
/* Functions to get at the control bits.                       */
/***************************************************************/
int GetIRD(int *x)           { return(x[IRD]); }
int GetCOND(int *x)          { return((x[COND1] << 1) + x[COND0]); }
int GetJ(int *x)             { return((x[J5] << 5) + (x[J4] << 4) +
				      (x[J3] << 3) + (x[J2] << 2) +
				      (x[J1] << 1) + x[J0]); }
int GetLD_MAR(int *x)        { return(x[LD_MAR]); }
int GetLD_MDR(int *x)        { return(x[LD_MDR]); }
int GetLD_IR(int *x)         { return(x[LD_IR]); }
int GetLD_BEN(int *x)        { return(x[LD_BEN]); }
int GetLD_REG(int *x)        { return(x[LD_REG]); }
int GetLD_CC(int *x)         { return(x[LD_CC]); }
int GetLD_PC(int *x)         { return(x[LD_PC]); }
int GetGATE_PC(int *x)       { return(x[GATE_PC]); }
int GetGATE_MDR(int *x)      { return(x[GATE_MDR]); }
int GetGATE_ALU(int *x)      { return(x[GATE_ALU]); }
int GetGATE_MARMUX(int *x)   { return(x[GATE_MARMUX]); }
int GetGATE_SHF(int *x)      { return(x[GATE_SHF]); }
int GetPCMUX(int *x)         { return((x[PCMUX1] << 1) + x[PCMUX0]); }
int GetDRMUX(int *x)         { return(x[DRMUX]); }
int GetSR1MUX(int *x)        { return(x[SR1MUX]); }
int GetADDR1MUX(int *x)      { return(x[ADDR1MUX]); }
int GetADDR2MUX(int *x)      { return((x[ADDR2MUX1] << 1) + x[ADDR2MUX0]); }
int GetMARMUX(int *x)        { return(x[MARMUX]); }
int GetALUK(int *x)          { return((x[ALUK1] << 1) + x[ALUK0]); }
int GetMIO_EN(int *x)        { return(x[MIO_EN]); }
int GetR_W(int *x)           { return(x[R_W]); }
int GetDATA_SIZE(int *x)     { return(x[DATA_SIZE]); } 
int GetLSHF1(int *x)         { return(x[LSHF1]); }
/* MODIFY: you can add more Get functions for your new control signals */
int GetLD_USP(int *x) { return x[LD_USP]; }
int GetGATE_USP(int *x) { return x[GATE_USP]; }
int GetLD_SSP(int *x) { return x[LD_SSP]; }
int GetSSPMUX(int *x) { return (x[SSPMUX1] << 1) + x[SSPMUX0]; }
int GetGATE_SSP(int *x) { return x[GATE_SSP]; }
int GetLD_INTV(int *x) { return x[LD_INTV]; }
int GetSPR(int *x) { return x[SPR]; }
int GetLD_SPR(int *x) { return x[LD_SPR]; }
int GetGATE_SPR(int *x) { return x[GATE_SPR]; }
int GetLD_PSR(int *x) { return x[LD_PSR]; }
int GetPSRMUX(int *x) { return x[PSRMUX]; }
int GetGATE_PSR(int *x) { return x[GATE_PSR]; }
int GetGATE_VEC(int *x) { return x[GATE_VEC]; }
int GetCOND2(int *x) { return x[COND2]; }
/* END MODIFY new GET functions */

/***************************************************************/
/* The control store rom.                                      */
/***************************************************************/
int CONTROL_STORE[CONTROL_STORE_ROWS][CONTROL_STORE_BITS];

/***************************************************************/
/* Main memory.                                                */
/***************************************************************/
/* MEMORY[A][0] stores the least significant byte of word at word address A
   MEMORY[A][1] stores the most significant byte of word at word address A 
   There are two write enable signals, one for each byte. WE0 is used for 
   the least significant byte of a word. WE1 is used for the most significant 
   byte of a word. */

#define WORDS_IN_MEM    0x08000 
#define MEM_CYCLES      5
int MEMORY[WORDS_IN_MEM][2];

/***************************************************************/

/***************************************************************/

/***************************************************************/
/* LC-3b State info.                                           */
/***************************************************************/
#define LC_3b_REGS 8

int RUN_BIT;	/* run bit */
int BUS;	/* value of the bus */

typedef struct System_Latches_Struct{

int PC,		/* program counter */
    MDR,	/* memory data register */
    MAR,	/* memory address register */
    IR,		/* instruction register */
    N,		/* n condition bit */
    Z,		/* z condition bit */
    P,		/* p condition bit */
    BEN;        /* ben register */

int READY;	/* ready bit */
  /* The ready bit is also latched as you dont want the memory system to assert it 
     at a bad point in the cycle*/

int REGS[LC_3b_REGS]; /* register file. */

int MICROINSTRUCTION[CONTROL_STORE_BITS]; /* The microintruction */

int STATE_NUMBER; /* Current State Number - Provided for debugging */ 

/* For lab 4 */
int INTV; /* Interrupt vector register */
int EXCV; /* Exception vector register */
int SSP; /* Initial value of system stack pointer */
/* MODIFY: You may add system latches that are required by your implementation */
int USP;
int EXC_FLAG;
int INT_FLAG;
int PSR;
/* END MODIFY*/

} System_Latches;

/* Data Structure for Latch */

System_Latches CURRENT_LATCHES, NEXT_LATCHES;

/***************************************************************/
/* A cycle counter.                                            */
/***************************************************************/
int CYCLE_COUNT;

/***************************************************************/
/*                                                             */
/* Procedure : help                                            */
/*                                                             */
/* Purpose   : Print out a list of commands.                   */
/*                                                             */
/***************************************************************/
void help() {                                                    
    printf("----------------LC-3bSIM Help-------------------------\n");
    printf("go               -  run program to completion       \n");
    printf("run n            -  execute program for n cycles    \n");
    printf("mdump low high   -  dump memory from low to high    \n");
    printf("rdump            -  dump the register & bus values  \n");
    printf("?                -  display this help menu          \n");
    printf("quit             -  exit the program                \n\n");
}

/***************************************************************/
/*                                                             */
/* Procedure : cycle                                           */
/*                                                             */
/* Purpose   : Execute a cycle                                 */
/*                                                             */
/***************************************************************/
void cycle() {                                                

  eval_micro_sequencer();   
  cycle_memory();
  eval_bus_drivers();
  drive_bus();
  latch_datapath_values();

  CURRENT_LATCHES = NEXT_LATCHES;

  CYCLE_COUNT++;
}

/***************************************************************/
/*                                                             */
/* Procedure : run n                                           */
/*                                                             */
/* Purpose   : Simulate the LC-3b for n cycles.                 */
/*                                                             */
/***************************************************************/
void run(int num_cycles) {                                      
    int i;

    if (RUN_BIT == FALSE) {
	printf("Can't simulate, Simulator is halted\n\n");
	return;
    }

    printf("Simulating for %d cycles...\n\n", num_cycles);
    for (i = 0; i < num_cycles; i++) {
	if (CURRENT_LATCHES.PC == 0x0000) {
	    RUN_BIT = FALSE;
	    printf("Simulator halted\n\n");
	    break;
	}
	cycle();
    }
}

/***************************************************************/
/*                                                             */
/* Procedure : go                                              */
/*                                                             */
/* Purpose   : Simulate the LC-3b until HALTed.                 */
/*                                                             */
/***************************************************************/
void go() {                                                     
    if (RUN_BIT == FALSE) {
	printf("Can't simulate, Simulator is halted\n\n");
	return;
    }

    printf("Simulating...\n\n");
    while (CURRENT_LATCHES.PC != 0x0000)
	cycle();
    RUN_BIT = FALSE;
    printf("Simulator halted\n\n");
}

/***************************************************************/ 
/*                                                             */
/* Procedure : mdump                                           */
/*                                                             */
/* Purpose   : Dump a word-aligned region of memory to the     */
/*             output file.                                    */
/*                                                             */
/***************************************************************/
void mdump(FILE * dumpsim_file, int start, int stop) {          
    int address; /* this is a byte address */

    printf("\nMemory content [0x%0.4x..0x%0.4x] :\n", start, stop);
    printf("-------------------------------------\n");
    for (address = (start >> 1); address <= (stop >> 1); address++)
	printf("  0x%0.4x (%d) : 0x%0.2x%0.2x\n", address << 1, address << 1, MEMORY[address][1], MEMORY[address][0]);
    printf("\n");

    /* dump the memory contents into the dumpsim file */
    fprintf(dumpsim_file, "\nMemory content [0x%0.4x..0x%0.4x] :\n", start, stop);
    fprintf(dumpsim_file, "-------------------------------------\n");
    for (address = (start >> 1); address <= (stop >> 1); address++)
	fprintf(dumpsim_file, " 0x%0.4x (%d) : 0x%0.2x%0.2x\n", address << 1, address << 1, MEMORY[address][1], MEMORY[address][0]);
    fprintf(dumpsim_file, "\n");
    fflush(dumpsim_file);
}

/***************************************************************/
/*                                                             */
/* Procedure : rdump                                           */
/*                                                             */
/* Purpose   : Dump current register and bus values to the     */   
/*             output file.                                    */
/*                                                             */
/***************************************************************/
void rdump(FILE * dumpsim_file) {                               
    int k; 

    printf("\nCurrent register/bus values :\n");
    printf("-------------------------------------\n");
    printf("Cycle Count  : %d\n", CYCLE_COUNT);
    printf("PC           : 0x%0.4x\n", CURRENT_LATCHES.PC);
    printf("IR           : 0x%0.4x\n", CURRENT_LATCHES.IR);
    printf("STATE_NUMBER : 0x%0.4d\n\n", CURRENT_LATCHES.STATE_NUMBER);
    printf("BUS          : 0x%0.4x\n", BUS);
    printf("MDR          : 0x%0.4x\n", CURRENT_LATCHES.MDR);
    printf("MAR          : 0x%0.4x\n", CURRENT_LATCHES.MAR);
    printf("CCs: N = %d  Z = %d  P = %d\n", CURRENT_LATCHES.N, CURRENT_LATCHES.Z, CURRENT_LATCHES.P);
    printf("Registers:\n");
    for (k = 0; k < LC_3b_REGS; k++)
	printf("%d: 0x%0.4x\n", k, CURRENT_LATCHES.REGS[k]);
    printf("\n");

    /* dump the state information into the dumpsim file */
    fprintf(dumpsim_file, "\nCurrent register/bus values :\n");
    fprintf(dumpsim_file, "-------------------------------------\n");
    fprintf(dumpsim_file, "Cycle Count  : %d\n", CYCLE_COUNT);
    fprintf(dumpsim_file, "PC           : 0x%0.4x\n", CURRENT_LATCHES.PC);
    fprintf(dumpsim_file, "IR           : 0x%0.4x\n", CURRENT_LATCHES.IR);
    fprintf(dumpsim_file, "STATE_NUMBER : 0x%0.4x\n\n", CURRENT_LATCHES.STATE_NUMBER);
    fprintf(dumpsim_file, "BUS          : 0x%0.4x\n", BUS);
    fprintf(dumpsim_file, "MDR          : 0x%0.4x\n", CURRENT_LATCHES.MDR);
    fprintf(dumpsim_file, "MAR          : 0x%0.4x\n", CURRENT_LATCHES.MAR);
    fprintf(dumpsim_file, "CCs: N = %d  Z = %d  P = %d\n", CURRENT_LATCHES.N, CURRENT_LATCHES.Z, CURRENT_LATCHES.P);
    fprintf(dumpsim_file, "Registers:\n");
    for (k = 0; k < LC_3b_REGS; k++)
	fprintf(dumpsim_file, "%d: 0x%0.4x\n", k, CURRENT_LATCHES.REGS[k]);
    fprintf(dumpsim_file, "\n");
    fflush(dumpsim_file);
}

/***************************************************************/
/*                                                             */
/* Procedure : get_command                                     */
/*                                                             */
/* Purpose   : Read a command from standard input.             */  
/*                                                             */
/***************************************************************/
void get_command(FILE * dumpsim_file) {                         
    char buffer[20];
    int start, stop, cycles;

    printf("LC-3b-SIM> ");

    scanf("%s", buffer);
    printf("\n");

    switch(buffer[0]) {
    case 'G':
    case 'g':
	go();
	break;

    case 'M':
    case 'm':
	scanf("%i %i", &start, &stop);
	mdump(dumpsim_file, start, stop);
	break;

    case '?':
	help();
	break;
    case 'Q':
    case 'q':
	printf("Bye.\n");
	exit(0);

    case 'R':
    case 'r':
	if (buffer[1] == 'd' || buffer[1] == 'D')
	    rdump(dumpsim_file);
	else {
	    scanf("%d", &cycles);
	    run(cycles);
	}
	break;

    default:
	printf("Invalid Command\n");
	break;
    }
}

/***************************************************************/
/*                                                             */
/* Procedure : init_control_store                              */
/*                                                             */
/* Purpose   : Load microprogram into control store ROM        */ 
/*                                                             */
/***************************************************************/
void init_control_store(char *ucode_filename) {                 
    FILE *ucode;
    int i, j, index;
    char line[200];

    printf("Loading Control Store from file: %s\n", ucode_filename);

    /* Open the micro-code file. */
    if ((ucode = fopen(ucode_filename, "r")) == NULL) {
	printf("Error: Can't open micro-code file %s\n", ucode_filename);
	exit(-1);
    }

    /* Read a line for each row in the control store. */
    for(i = 0; i < CONTROL_STORE_ROWS; i++) {
	if (fscanf(ucode, "%[^\n]\n", line) == EOF) {
	    printf("Error: Too few lines (%d) in micro-code file: %s\n",
		   i, ucode_filename);
	    exit(-1);
	}

	/* Put in bits one at a time. */
	index = 0;

	for (j = 0; j < CONTROL_STORE_BITS; j++) {
	    /* Needs to find enough bits in line. */
	    if (line[index] == '\0') {
		printf("Error: Too few control bits in micro-code file: %s\nLine: %d\n",
		       ucode_filename, i);
		exit(-1);
	    }
	    if (line[index] != '0' && line[index] != '1') {
		printf("Error: Unknown value in micro-code file: %s\nLine: %d, Bit: %d\n",
		       ucode_filename, i, j);
		exit(-1);
	    }

	    /* Set the bit in the Control Store. */
	    CONTROL_STORE[i][j] = (line[index] == '0') ? 0:1;
	    index++;
	}

	/* Warn about extra bits in line. */
	if (line[index] != '\0')
	    printf("Warning: Extra bit(s) in control store file %s. Line: %d\n",
		   ucode_filename, i);
    }
    printf("\n");
}

/***************************************************************/
/*                                                             */
/* Procedure : init_memory                                     */
/*                                                             */
/* Purpose   : Zero out the memory array                       */
/*                                                             */
/***************************************************************/
void init_memory() {                                           
    int i;

    for (i=0; i < WORDS_IN_MEM; i++) {
	MEMORY[i][0] = 0;
	MEMORY[i][1] = 0;
    }
}

/**************************************************************/
/*                                                            */
/* Procedure : load_program                                   */
/*                                                            */
/* Purpose   : Load program and service routines into mem.    */
/*                                                            */
/**************************************************************/
void load_program(char *program_filename) {                   
    FILE * prog;
    int ii, word, program_base;

    /* Open program file. */
    prog = fopen(program_filename, "r");
    if (prog == NULL) {
	printf("Error: Can't open program file %s\n", program_filename);
	exit(-1);
    }

    /* Read in the program. */
    if (fscanf(prog, "%x\n", &word) != EOF)
	program_base = word >> 1;
    else {
	printf("Error: Program file is empty\n");
	exit(-1);
    }

    ii = 0;
    while (fscanf(prog, "%x\n", &word) != EOF) {
	/* Make sure it fits. */
	if (program_base + ii >= WORDS_IN_MEM) {
	    printf("Error: Program file %s is too long to fit in memory. %x\n",
		   program_filename, ii);
	    exit(-1);
	}

	/* Write the word to memory array. */
	MEMORY[program_base + ii][0] = word & 0x00FF;
	MEMORY[program_base + ii][1] = (word >> 8) & 0x00FF;
	ii++;
    }

    if (CURRENT_LATCHES.PC == 0) CURRENT_LATCHES.PC = (program_base << 1);

    printf("Read %d words from program into memory.\n\n", ii);
}

/***************************************************************/
/*                                                             */
/* Procedure : initialize                                      */
/*                                                             */
/* Purpose   : Load microprogram and machine language program  */ 
/*             and set up initial state of the machine.        */
/*                                                             */
/***************************************************************/
void initialize(char *ucode_filename, char *program_filename, int num_prog_files) { 
    int i;
    init_control_store(ucode_filename);

    init_memory();
    for ( i = 0; i < num_prog_files; i++ ) {
	load_program(program_filename);
	while(*program_filename++ != '\0');
    }
    CURRENT_LATCHES.Z = 1;
    CURRENT_LATCHES.STATE_NUMBER = INITIAL_STATE_NUMBER;
    memcpy(CURRENT_LATCHES.MICROINSTRUCTION, CONTROL_STORE[INITIAL_STATE_NUMBER], sizeof(int)*CONTROL_STORE_BITS);
    CURRENT_LATCHES.SSP = 0x3000; /* Initial value of system stack pointer */
	CURRENT_LATCHES.PSR = 0x8000; /*Initializes to User Privilege*/

    NEXT_LATCHES = CURRENT_LATCHES;

    RUN_BIT = TRUE;
}

/***************************************************************/
/*                                                             */
/* Procedure : main                                            */
/*                                                             */
/***************************************************************/
int main(int argc, char *argv[]) {                              
    FILE * dumpsim_file;

    /* Error Checking */
    if (argc < 3) {
	printf("Error: usage: %s <micro_code_file> <program_file_1> <program_file_2> ...\n",
	       argv[0]);
	exit(1);
    }

    printf("LC-3b Simulator\n\n");

    initialize(argv[1], argv[2], argc - 2);

    if ( (dumpsim_file = fopen( "dumpsim", "w" )) == NULL ) {
	printf("Error: Can't open dumpsim file\n");
	exit(-1);
    }

    while (1)
	get_command(dumpsim_file);

}

/***************************************************************/
/* Do not modify the above code, except for the places indicated 
   with a "MODIFY:" comment.

   Do not modify the rdump and mdump functions.

   You are allowed to use the following global variables in your
   code. These are defined above.

   CONTROL_STORE
   MEMORY
   BUS

   CURRENT_LATCHES
   NEXT_LATCHES

   You may define your own local/global variables and functions.
   You may use the functions to get at the control bits defined
   above.

   Begin your code here 	  			       */
/***************************************************************/

#define TIMER_INTERRUPT 300
int MEM_CYCLE = 0;
int SR_SIGNAL2 = 0;

void eval_micro_sequencer() {

	/*
	* Evaluate the address of the next state according to the
	* micro sequencer logic. Latch the next microinstruction.
	*/

	int* CMI = CURRENT_LATCHES.MICROINSTRUCTION;
	int CS_LOC;

	if (GetIRD(CMI)){
		CS_LOC = (CURRENT_LATCHES.IR & 0xF000) >> 12;
	}
	else{
		int j = GetJ(CMI);

		switch (GetCOND(CMI)){
		case 0: CS_LOC = j; break;
		case 1: CS_LOC = j | (CURRENT_LATCHES.READY << 1); break;
		case 2: CS_LOC = j | (CURRENT_LATCHES.BEN << 2); break;
		case 3: CS_LOC = j | ((CURRENT_LATCHES.IR & 0x0800) >> 11); break;
		}

		if (GetCOND2(CMI)){
			/*Interrupt was triggered in the previous instruction*/
			CS_LOC |= (CURRENT_LATCHES.INT_FLAG << 3);
		}
	}


	int i;
	for (i = 0; i < CONTROL_STORE_BITS; i++){
		NEXT_LATCHES.MICROINSTRUCTION[i] = CONTROL_STORE[CS_LOC][i];
	}
	NEXT_LATCHES.STATE_NUMBER = CS_LOC;

	if (CYCLE_COUNT == TIMER_INTERRUPT){
		/*Raise interrupt flag*/
		NEXT_LATCHES.INT_FLAG = 1;
	}
}


void cycle_memory() {

	/*
	* This function emulates memory and the WE logic.
	* Keep track of which cycle of MEMEN we are dealing with.
	* If fourth, we need to latch Ready bit at the end of
	* cycle to prepare microsequencer for the fifth cycle.
	*/

	int* CMI = CURRENT_LATCHES.MICROINSTRUCTION;

	if (GetMIO_EN(CURRENT_LATCHES.MICROINSTRUCTION)){
		MEM_CYCLE++;
		if (MEM_CYCLE % 4 == 0){
			NEXT_LATCHES.READY = 1;
		}


		if (CURRENT_LATCHES.READY){
			if ((CURRENT_LATCHES.MAR < 0x3000) && (CURRENT_LATCHES.PSR & 0x8000) && (CURRENT_LATCHES.STATE_NUMBER != 48) && (CURRENT_LATCHES.STATE_NUMBER != 28)){
				/*Not in Super Mode, but attempted to access memory! We done goofed. */
				NEXT_LATCHES.STATE_NUMBER = 63;
				NEXT_LATCHES.INTV = 0x02;
				NEXT_LATCHES.EXCV = 0x02;
			}

			if (CURRENT_LATCHES.STATE_NUMBER == 28) CURRENT_LATCHES.MAR <<= 1;

			if (GetDATA_SIZE(CMI) && (CURRENT_LATCHES.MAR & 0x01)){
				/*Unaligned access! We done goofed again */
				NEXT_LATCHES.STATE_NUMBER = 63;
				NEXT_LATCHES.INTV = 0x03;
				NEXT_LATCHES.EXCV = 0x03;
			}

			if (GetR_W(CMI)) {
				if (GetDATA_SIZE(CMI)){
					/*WRITE WORD*/
					MEMORY[CURRENT_LATCHES.MAR >> 1][0] = CURRENT_LATCHES.MDR & 0x00FF;
					MEMORY[CURRENT_LATCHES.MAR >> 1][1] = (CURRENT_LATCHES.MDR & 0xFF00) >> 8;
				}
				else{
					/*WRITE BYTE*/
					MEMORY[CURRENT_LATCHES.MAR >> 1][CURRENT_LATCHES.MAR & 0x01] = CURRENT_LATCHES.MDR & 0x00FF;
				}
			}
			else{
				if (GetDATA_SIZE(CMI)){
					/*READ WORD*/
					NEXT_LATCHES.MDR = MEMORY[CURRENT_LATCHES.MAR >> 1][0] + (MEMORY[CURRENT_LATCHES.MAR >> 1][1] << 8);
				}
				else{
					/*READ BYTE*/
					NEXT_LATCHES.MDR = ((MEMORY[CURRENT_LATCHES.MAR >> 1][CURRENT_LATCHES.MAR & 0x01]) << 24) >> 24;
				}
			}

			MEM_CYCLE = 0;
			NEXT_LATCHES.READY = 0;
			SR_SIGNAL2 = 1;
		}
	}

}


int MARMUX_OUT, ALU_OUT, PCMUX_OUT, SHF_OUT, MDR_OUT;
int ADDER_OUTPUT;
int SR_SIGNAL = 0;

int SSPMUX_OUT;

void eval_bus_drivers() {

	/*
	* Datapath routine emulating operations before driving the bus.
	* Evaluate the input of tristate drivers
	*             Gate_MARMUX,
	*		 Gate_PC,
	*		 Gate_ALU,
	*		 Gate_SHF,
	*		 Gate_MDR.
	*/

	int* CMI = CURRENT_LATCHES.MICROINSTRUCTION;
	int inst = CURRENT_LATCHES.IR;

	/**************************SR1*****************************/
	int SR1MUX_OUT = GetSR1MUX(CMI) ? CURRENT_LATCHES.REGS[(CURRENT_LATCHES.IR & 0x01C0) >> 6] : CURRENT_LATCHES.REGS[(CURRENT_LATCHES.IR & 0x0E00) >> 9];

	/**************************SR2*****************************/
	int SR2MUX_OUT = (inst & 0x20) ? (((0x001F & inst) << 27) >> 27) : CURRENT_LATCHES.REGS[inst & 0x07];

	/**************************ADDR1MUX************************/
	int ADDR1MUX_OUT = GetADDR1MUX(CMI) ? SR1MUX_OUT : CURRENT_LATCHES.PC;


	/**************************ADDR2MUX************************/
	int ADDR2MUX_OUT;
	switch (GetADDR2MUX(CMI)) {
	case 0:	ADDR2MUX_OUT = 0; break;
	case 1:	ADDR2MUX_OUT = (((inst & 0x03F) << 26) >> 26); break;
	case 2: ADDR2MUX_OUT = (((inst & 0x1FF) << 23) >> 23); break;
	case 3: ADDR2MUX_OUT = (((inst & 0x7FF) << 21) >> 21); break;
	}

	/**************************ADDER***************************/
	ADDER_OUTPUT = (ADDR2MUX_OUT << GetLSHF1(CMI)) + ADDR1MUX_OUT;

	/**************************MARMUX**************************/
	MARMUX_OUT = GetMARMUX(CMI) ? ADDER_OUTPUT : ((inst & 0x00FF) << GetLSHF1(CMI));


	/**************************ALU*****************************/
	switch (GetALUK(CMI)) {
	case 0: ALU_OUT = SR1MUX_OUT + SR2MUX_OUT; break;
	case 1: ALU_OUT = SR1MUX_OUT & SR2MUX_OUT; break;
	case 2: ALU_OUT = SR1MUX_OUT ^ SR2MUX_OUT; break;
	case 3: ALU_OUT = SR1MUX_OUT; SR_SIGNAL = 1;  break;
	}

	/**************************SHF*****************************/
	switch ((inst & 0x30) >> 4){
	case 0: SHF_OUT = Low16bits(SR1MUX_OUT << (inst & 0x0F)); break;
	case 1: SHF_OUT = SR1MUX_OUT >> (inst & 0x0F); break;
	case 3: SHF_OUT = Low16bits(((SR1MUX_OUT << 16) >> 16) >> (inst & 0x0F)); break;
	}

	/**************************MDR*****************************/
	/*TODO: CHECK IF THIS IS RIGHT*/
	MDR_OUT = CURRENT_LATCHES.MDR;


	/**************************SSPMUX**************************/
	switch (GetSSPMUX(CMI)){
	case 0: SSPMUX_OUT = CURRENT_LATCHES.SSP; break;
	case 1: SSPMUX_OUT = CURRENT_LATCHES.SSP + 2; break;
	case 2: SSPMUX_OUT = CURRENT_LATCHES.SSP - 2; break;
	}

}


void drive_bus() {

	/*
	* Datapath routine for driving the bus from one of the 5 possible
	* tristate drivers.
	*         Gate_MARMUX,
	*		 Gate_PC,
	*		 Gate_ALU,
	*		 Gate_SHF,
	*		 Gate_MDR.
	*/
	int* CMI = CURRENT_LATCHES.MICROINSTRUCTION;
	if (GetGATE_MARMUX(CMI))
		BUS = MARMUX_OUT;
	else if (GetGATE_PC(CMI))
		BUS = CURRENT_LATCHES.PC;
	else if (GetGATE_ALU(CMI))
		BUS = ALU_OUT;
	else if (GetGATE_SHF(CMI))
		BUS = SHF_OUT;
	else if (GetGATE_MDR(CMI))
		BUS = MDR_OUT;
	else if (GetGATE_USP(CMI))
		BUS = CURRENT_LATCHES.USP;
	else if (GetGATE_SSP(CMI))
		BUS = SSPMUX_OUT;
	else if (GetGATE_SPR(CMI))
		BUS = CURRENT_LATCHES.REGS[6];
	else if (GetGATE_PSR(CMI)) {
		BUS = CURRENT_LATCHES.PSR;
		SR_SIGNAL = 1;
	}
	else if (GetGATE_VEC(CMI))
		BUS = 0x0200 + (CURRENT_LATCHES.INTV << 1);
	else BUS = 0;

}


void latch_datapath_values() {

	/*
	* Datapath routine for computing all functions that need to latch
	* values in the data path at the end of this cycle.  Some values
	* require sourcing the bus; therefore, this routine has to come
	* after drive_bus.
	*/

	int* CMI = CURRENT_LATCHES.MICROINSTRUCTION;
	int inst = CURRENT_LATCHES.IR;

	/**************************DRMUX***************************/
	int DRMUX_OUT = GetDRMUX(CMI) ? 7 : ((CURRENT_LATCHES.IR & 0x0E00) >> 9);

	/**************************PCMUX***************************/
	switch (GetPCMUX(CMI)) {
	case 0: PCMUX_OUT = CURRENT_LATCHES.PC + 2; break;
	case 1: PCMUX_OUT = BUS; break;
	case 2: PCMUX_OUT = ADDER_OUTPUT;  break;
	}

	/**************************MAR*****************************/
	if (GetLD_MAR(CMI)) NEXT_LATCHES.MAR = Low16bits(BUS);

	/**************************MDR*****************************/
	/*TODO: THIS IS BROKEN AS SHIT*/
	if (SR_SIGNAL){
		NEXT_LATCHES.MDR = Low16bits(BUS);
		SR_SIGNAL = 0;
	}
	else if ((CURRENT_LATCHES.PSR & 0x8000) == 0 && GetLD_MDR(CMI)){
		if (!SR_SIGNAL2)
			NEXT_LATCHES.MDR = Low16bits(BUS);
	}
	SR_SIGNAL2 = 0;

	/**************************IR******************************/
	if (GetLD_IR(CMI)) NEXT_LATCHES.IR = Low16bits(BUS);

	/**************************BEN*****************************/
	if (GetLD_BEN(CMI)) NEXT_LATCHES.BEN = (((inst & 0x0800) >> 11) & CURRENT_LATCHES.N) | (((inst & 0x0400) >> 10) & CURRENT_LATCHES.Z) | (((inst & 0x0200) >> 9) & CURRENT_LATCHES.P);

	/**************************REG*****************************/
	if (GetLD_REG(CMI)) NEXT_LATCHES.REGS[DRMUX_OUT] = Low16bits(BUS);

	/**************************CC******************************/
	if (GetLD_CC(CMI)){
		NEXT_LATCHES.N = 0;
		NEXT_LATCHES.Z = 0;
		NEXT_LATCHES.P = 0;

		if (BUS < 0) NEXT_LATCHES.N = 1;
		else if (BUS == 0) NEXT_LATCHES.Z = 1;
		else NEXT_LATCHES.P = 1;

		NEXT_LATCHES.PSR = (NEXT_LATCHES.PSR & 0xFFF8) + (NEXT_LATCHES.N << 2) + (NEXT_LATCHES.Z << 1) + NEXT_LATCHES.P;
	}

	/**************************PC******************************/
	if (GetLD_PC(CMI)) NEXT_LATCHES.PC = Low16bits(PCMUX_OUT);


	/**************************SSP*****************************/
	if (GetLD_SSP(CMI)) NEXT_LATCHES.SSP = Low16bits(SSPMUX_OUT);


	/**************************USP*****************************/
	if (GetLD_USP(CMI)) NEXT_LATCHES.USP = Low16bits(CURRENT_LATCHES.REGS[6]);


	/**************************PSR*****************************/
	if (GetLD_PSR(CMI)){
		if (GetPSRMUX(CMI)){
			NEXT_LATCHES.PSR &= 0x7FFF;
		}
		else {
			NEXT_LATCHES.PSR = Low16bits(BUS);
		}
	}

	/**************************SPR*****************************/
	if (GetLD_SPR(CMI)) NEXT_LATCHES.REGS[6] = Low16bits(BUS);

	/**************************VECTORS*************************/
	if (GetLD_INTV(CMI)) {
		/*Preps interrupt vector*/
		NEXT_LATCHES.INTV = 0x01;

		/*Decrements PC for Interrupt Execution*/
		if (CURRENT_LATCHES.INT_FLAG) NEXT_LATCHES.PC = CURRENT_LATCHES.PC - 2;

		/*Unkown Upcode Exception*/
		if ((CURRENT_LATCHES.IR >> 12) == 10 || (CURRENT_LATCHES.IR >> 12) == 11) {
			NEXT_LATCHES.INTV = 0x04;
			NEXT_LATCHES.EXCV = 0x04;
		}
			
		/*Lower INT_FLAG*/
		NEXT_LATCHES.INT_FLAG = 0;
	}
}
