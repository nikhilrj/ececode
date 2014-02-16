package javaconcprog;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
  * Implements asynchronous or synchronous message passing.
  * Asynchronous:
  *    sends do not block (until the message is received),
  *    receives block of course until a message is received.
  * Synchronous:
  *    sends block until the message is received,
  *    receives block of course until a message is received.
  * Messages are delivered FIFO (in the order they are sent) and
  * receivers get messages FIFO (in the order they call receive).
  * Optional capacity control on sent but unreceived messages.
  *
  * @author Stephen J. Hartley
  * @version 2005 July
  */
public final class MessagePassing<T> {
   /**
     * Queue of sent but unreceived messages.
     */
   private final BlockingQueue<T> messages;

   /**
     * Constructor for asynchronous message passing with no limit
     * on sent but unreceived messages.
     */
   public MessagePassing() { this(0); }

   /**
     * Constructor for asynchronous message passing with capacity limit
     * on sent but unreceived messages and for synchronous message passing.
     * @param c The capacity limit (0 means none, negative means synchronous).
     */
   public MessagePassing(int c) {
      // A negative capacity is a sentinel value for synchronous
      // message passing.
      if (c < 0) messages = new SynchronousQueue<T>();
      // Asynchronous message passing with capacity control.
      else if (c > 0) messages = new ArrayBlockingQueue<T>(c);
      // A zero capacity is a sentinal value for asynchronous message
      // passing with an unbounded capacity for sent but not yet
      // received messages.
      else /* c == 0 */  messages = new LinkedBlockingQueue<T>();
   }

   /**
     * Send a message and block if capacity reached or synchronous.
     * @param m The message object to send.
     * @throws InterruptedException
     */
   public final void send(T m) throws InterruptedException {
      if (m == null) throw new NullPointerException("null message");
      messages.put(m);
   }

   /**
     * Receive a message if possible else block.
     * @return The received message.
     * @throws InterruptedException
     */
   public final T receive() throws InterruptedException {
      return messages.take();
   }

   public int remainingCapacity() { return messages.remainingCapacity(); }
}

