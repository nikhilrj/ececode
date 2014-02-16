package javaconcprog;
import static javaconcprog.AgeRandom.*;

import javaconcprog.MessagePassing;

/**
  * After all communication among the peer threads completes, each peer
  * has calculated the count of how many other peers have an array value
  * less than its own array value.  That information is sent back to the
  * driver.
  *
  * @author Stephen J. Hartley
  * @version 2005 July
  */
class Result {
   /**
     * A value from the array to be sorted.
     */
   public final int number;
   /**
     * Count of how many other peers have an array value less than this one.
     */
   public final int count;

   /**
     * Constructor.
     * @param n A value from the array to be sorted.
     * @param c Count of how many other peers have a smaller array value.
     */
   public Result(int n, int c) { number = n; count = c; }
}

/**
  * Each peer or worker thread sends to all other peers an item containing
  * the value from the array assigned to the sending peer and the array
  * index of that value.
  *
  * @author Stephen J. Hartley
  * @version 2005 July
  */
class Item {
   /**
     * Value from the array assigned to the sending peer.
     */
   public final int number;
   /**
     * Index in the array of that value.
     */
   public final int id;

   /**
     * Constructor.
     * @param n Value from the array assigned to the sending peer.
     * @param i Index in the array of that value.
     */
   public Item(int n, int i) { number = n; id = i; }
}

/**
  * Peer thread.  It has an array value assigned to it, a communication
  * channel to each other peer thread, and a communication channel to the
  * driver.
  *
  * @author Stephen J. Hartley
  * @version 2005 July
  */
class Peer implements Runnable {
   /**
     * The number of values in the array to be sorted.
     */
   private int N = -1;
   /**
     * Identifying number for this peer.
     * Index in the array of this peer's value.
     */
   private int id = -1;
   /**
     * This peer's value from the array to be sorted.
     */
   private int mine = 0;
   /**
     * Each peer has its own message passing channel in this array.
     */
   private MessagePassing<Item>[] channel = null;
   /**
     * Channel to send the results to the driver.
     */
   private MessagePassing<Result> reply = null;
   /**
     * Internal thread in this peer.
     */
   private Thread me = null;

   /**
     * Constructor.
     * @param N The number of values in the array to be sorted.
     * @param id Index in the array of the value.
     * @param mine Value from the array to be sorted.
     * @param channel Array of message passing channels to all peers.
     * @param reply Channel to send the results to the driver.
     */
   private Peer(int N, int id, int mine, MessagePassing<Item>[] channel,
         MessagePassing<Result> reply) {
      this.N = N;
      this.id = id;
      this.mine = mine;
      this.channel = channel;
      this.reply = reply;
   }

   /**
     * Factory method.
     * @param N The number of values in the array to be sorted.
     * @param id Index in the array of the value.
     * @param mine Value from the array to be sorted.
     * @param channel Array of message passing channels to all peers.
     * @param reply Channel to send the results to the driver.
     * @return A peer object with internal thread.
     */
   public static Peer newInstance(int N, int id, int mine,
         MessagePassing<Item>[] channel, MessagePassing<Result> reply) {
      Peer instance = new Peer(N, id, mine, channel, reply);
      (instance.me = new Thread(instance)).start();
      return instance;
   }

   /**
     * Code for thread inside this object to execute.
     */
   public void run() {
      int count = 0;
      try {
         // Send my number to all the other workers.
         for (int i = 0; i < N; i++)
            if (i != id) channel[i].send(new Item(mine, id));
         // Of the N-1 numbers sent to me by the other workers,
         // count how many are less than my number.
         for (int i = 1; i < N; i++) {
            Item other = channel[id].receive();
            if (other.number < mine ||
                  (other.number == mine && other.id < id))
               count++;
         }
         // Send my count of less-than-mine-seen back to main().
         reply.send(new Result(mine, count));
      } catch (InterruptedException e) { return; }
   }
}

/**
  * Parallel radix sort using one thread and one communication channel per
  * value in the array to be sorted.
  *
  * @author Stephen J. Hartley
  * @version 2005 July
  */
class RadixSort {

   /**
     * Driver.
     * @param args Command line arguments.
     */
   public static void main(String[] args) {
      int N = 5000;
      int RANGE = Integer.MAX_VALUE;
      int[] nums = null;
      MessagePassing<Item>[] channel = null;
      MessagePassing<Result> reply = null;
      try {
         N = Integer.parseInt(args[0]);
         RANGE = Integer.parseInt(args[1]);
      } catch (Exception e) { /* use defaults */ }
      System.out.println("Radix sorting " + N + " random integers between 1 and " + RANGE);
      nums = new int[N];
      for (int i = 0; i < N; i++) nums[i] = 1 + (int)random(RANGE);
      System.out.println("Original numbers:");
      for (int i = 0; i < 20; i++)
         System.out.print(" " + nums[i]);  System.out.println();
      // Set up the reply channel.
      reply = new MessagePassing<Result>();
      // following line generates an unchecked cast warning
      channel = (MessagePassing<Item>[]) new MessagePassing[N];
      // Set up the communication channels.
      for (int i = 0; i < N; i++) channel[i] = new MessagePassing<Item>();
      // Start the worker threads.
      for (int i = 0; i < N; i++)
         Peer.newInstance(N, i, nums[i], channel, reply);
      try {
         // Gather the results.
         for (int i = 0; i < N; i++) {
            Result r = reply.receive();
            // Put the number where it belongs in the sorted order,
            // which is the value of the counter in which it recorded
            // the number of less-than-its-own numbers it saw.
            nums[r.count] = r.number;
         }
      } catch (InterruptedException e) { /* ignored */ }
      System.out.println("Sorted   numbers:");
      for (int i = 0; i < 20; i++)
         System.out.print(" " + nums[i]);  System.out.println();
      System.out.println("age()=" + age() + ", done");
      System.exit(0);
   }
}

