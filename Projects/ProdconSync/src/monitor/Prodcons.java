package monitor;

/**
 * Prodcons
 * 
 * @author matthew.towles
 * @date Sep 11, 2019
 */
public class Prodcons { 
  static final int N = 0;
  static Producer prod = new Producer();
  static Consumer cons = new Consumer();
  static Monitor monitor = new Monitor();
  //
  // insert the three classes, Monitor, Producer, and Consumer
  //
  public static void main (String args[]) {
     // start the two threads
     prod.start();
     cons.start();
  }
 // end main
 // end class Prodcons
}

static class Monitor {
  private int data [] = new int[N]; // buffer with N slots
  private int num = 0; // number of full slots in buffer
  private int i = 0;
  private int j 0;
//
 public synchronized void insert (int item) {
   if (num == N)     {       // is buffer full?
      try {
        wait();

        } catch ( InterruptedException e){}
   }
      data[j] = item;  // insert data item in buffer
      j = (j+1) % N;   // index for next slot to insert
      num++;
      if (num == 1) {
        notify();    
        }
     // end insert

 }
   public synchronized int remove() {
     int item;
     if (num == 0) {    // is buffer empty?
        try {
          wait();
        } catch(InterruptedException e) {}
     }
     item = data[i];  // get data item from buffer
     i = (i+1) % N;             // index for next slot to remove
     num--;
     if (num == N-1) {
        notify();    
    }
     return item;
    }// end remove
}// end class Monitor

   static class Producer extends Thread {
     public void run() {
       int item;
       while(true) {
         item = generateItem();      // produce item
         monitor.insert(item);
       }
     }// end run
   }// end class Producer

   static class Consumer extends Thread {
     public void run() {
       int item;
       while(true) {
          item = monitor.remove();
          useItem(item);    // consume item
       }
     }
   }
}