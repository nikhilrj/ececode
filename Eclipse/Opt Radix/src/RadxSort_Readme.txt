RadixSort_Readme.txt
1. Author:  Nikhil Joglekar

2. In this folder we include:
	a. RadixSort.java: code used to implement random number generation and radix sorting algorithm
	d. StopWatch.java: which is used to measure the amount of time (in nanoseconds)


3. Some important functions in my code:
RadixSort.java :
	void RadixSorter(); - sorts rands using a Radix Sort algorithm, storing the result in rands
	void JCFSorter(); - sorts rands using Arrays.Sort, storing the result in rands
	(extra - HeapSort(); - sorts the rands using a PriorityQueue, stores results in rands)
	RadixSort() - default constructor that randomizes inputs using Seed 17
	

4. Our Work Log:
Requirement: 1 hours
Design: 1 hours
Implementation: 1 hours
Testing / Optimization: 3 hours
 

5. My runtime for Radix Sort was 43352138 ns. This is roughly 6.85 times slower than the runtime of Arrays.Sort, which as a runtime of 6466816 ns. 

I also demonstrated how a different sort algorithm could be implemented, by including a HeapSort usin a PriorityQueue. The runtime of this algorithm was 4555564 ns, which was 1.419 times faster than Arrays.Sort.