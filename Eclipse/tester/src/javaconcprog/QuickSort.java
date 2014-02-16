package javaconcprog;

import static javaconcprog.AgeRandom.*;

import javaconcprog.MessagePassing;

/**
  * A task for a quicksort thread consists of a range in the array to sort.
  * Immutable object.
  *
  * @author Stephen J. Hartley
  * @version 2005 July
  */
class Task {
   /**
     * Left end of the array range to sort.
     */
   public final int left;
   /**
     * Right end of the array range to sort.
     */
   public final int right;

   /**
     * Constructor.
     * @param left Left end of the array range to sort.
     * @param right Right end of the array range to sort.
     */
   public Task(int left, int right)
      { this.left = left; this.right = right; }
}

/**
  * Quicksort performed by a pool of threads (CPUs).
  *
  * @author Stephen J. Hartley
  * @version 2005 July
  */
class QuickSort implements Runnable {
   // Shared by all quicksort threads (static fields).
   /**
     * Number of numbers to sort (default).
     */
   private static int N = 10;
   /**
     * Upper range of numbers to sort (default).
     */
   private static int RANGE = 100;
   /**
     * Number of threads in the pool (number of CPUs).
     */
   private static int NCPU = 4;
   /**
     * When a thread is done, send a message to the driver using this channel.
     */
   private static final MessagePassing<Integer> doneCount = new MessagePassing<Integer>();
   /**
     * When a thread is available, get a task (array range) to quicksort
     * using this channel.
     */
   private static final MessagePassing<Task> task = new MessagePassing<Task>();
   /**
     * The array of numbers to sort; done in place, i.e., sorted array
     * appears here.
     */
   private static int[] nums = null;

   /**
     * Identifying name for this thread (CPU).
     */
   private String name = null;
   /**
     * Identifying number for this thread (CPU).
     */
   private int id = -1;
   /**
     * Internal thread.
     */
   private Thread me = null;

   /**
     * Constructor.
     * @param id Identifying number for this thread (CPU).
     */
   private QuickSort(int id) {
      this.name = "Worker" + id;
      this.id = id;
   }

   /**
     * Factory method.
     * @param id Identifying number for this thread (CPU).
     * returns A quicksort object with internal thread (CPU).
     */
   private static QuickSort newInstance(int id) {
      QuickSort instance = new QuickSort(id);
      (instance.me = new Thread(instance)).start();
      return instance;
   }

   /**
     * A thread (CPU), having gotten a task (array range) to perform (sort)
     * calls this static method.
     * @param worker Identifying number for this thread (CPU).
     * @param left Left end of the array range to sort.
     * @param right Right end of the array range to sort.
     * @throws InterruptedException
     */
   private static void quickSort(int worker, int left, int right)
         throws InterruptedException {
      int pivot = nums[left];
      int l = left, r = right;
      boolean done = false;
      while (!done) {
         if (nums[l+1] > pivot) { // needs to be moved to other end of nums
            while (r > l+1 && nums[r] > pivot) { r--; } // find one to swap
            if (r > l+1) {
               l++;
               int temp = nums[r]; nums[r] = nums[l]; nums[l] = temp; // swap
               done = l >= r-1;
            } else done = true;         // if can't find one to swap, then
         } else { l++; done = l >= r; } // need not be moved to other end
      }  // when this loop finishes, nums[left] is the pivot,
         // nums[left:l] <= pivot and nums[l+1:right] > pivot
                              //   [pivot, <= | > ]
                              //    ^        ^ ^ ^
                              //    |        | | |
                              // left        l r right
                              //    |        | | |
                              //    v        v v v
                              //   [<=, pivot | > ]
      int temp = nums[left]; nums[left] = nums[l]; nums[l] = temp; // swap
                              // nums[left,l-1] <= pivot,
                              // nums[l] == pivot, and
                              // nums[l+1,right] > pivot
      // spin off additional task of array entries to right of pivot
      if (right-(l+1) > 0) task.send(new Task(l+1, right));
      // or send done message to driver for sorted singleton nums[l+1:right]
      else if (right-(l+1) == 0) doneCount.send(worker);
      // send done message to driver for pivot sorted singleton
      doneCount.send(worker);
      // spin off additional task of array entries to left of pivot
      if ((l-1)-left > 0) task.send(new Task(left, l-1));
      // or send done message to driver for sorted singleton nums[left:l-1]
      else if ((l-1)-left == 0) doneCount.send(worker);
   }

   /**
     * Tell the thread inside this object that it is time to terminate.
     */
   public void timeToQuit() { me.interrupt(); }

   /**
     * Caller blocks until the thread inside this object terminates.
     * @throws InterruptedException
     */
   public void pauseTilDone() throws InterruptedException { me.join(); }

   /**
     * Code for thread inside this object to execute.
     */
   public void run() {
      Task m = null;
      while (true) {
         if (Thread.interrupted()) {
            System.out.println("age=" + age() + ", " + name
               + " interrupted");
            return;
         }
         try {
            m = task.receive();
            quickSort(id, m.left, m.right);
         } catch (InterruptedException e) {
            System.out.println("age=" + age() + ", " + name
               + " interrupted out of send/receive");
            return;
         }
      }
   }

   /**
     * Driver.
     * @param args Command line arguments.
     */
   public static void main(String[] args) {
      try {
         N = Integer.parseInt(args[0]);
         RANGE = Integer.parseInt(args[1]);
         NCPU = Integer.parseInt(args[2]);
      } catch (Exception e) { /* use defaults */ }
      System.out.println("Quick sorting " + N
         + " random numbers between 1 and " + RANGE
         + " using " + NCPU + " CPUs");
      nums = new int[N];
      for (int i = 0; i < N; i++) nums[i] = 1 + (int) random(RANGE);
      System.out.println("Original numbers:");
      for (int i = 0; i < N; i++)
         System.out.print(" " + nums[i]);  System.out.println();

      // create the workers with self-starting threads
      QuickSort[] q = new QuickSort[NCPU];
//    PseudoTimeSlicing.activate(); // for Solaris green threads only
      for (int i = 0; i < NCPU; i++) q[i] = QuickSort.newInstance(i);
      try {
         task.send(new Task(0, N-1));
         // wait for enough "singletons" to be produced
         for (int i = 0; i < N; i++) doneCount.receive();
         System.out.println("Sorted   numbers:");
         for (int i = 0; i < N; i++) System.out.print(" " + nums[i]);
         System.out.println();
         for (int i = 0; i < NCPU; i++) q[i].timeToQuit();
         Thread.sleep(1000);
         for (int i = 0; i < NCPU; i++) q[i].pauseTilDone();
      } catch (InterruptedException e) { /* ignored */ }
      System.out.println("age()=" + age() + ", done");
      System.exit(0);
   }
}

