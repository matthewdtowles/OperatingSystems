package prodconsync;

/**
 * ProdconSync
 * 
 * @author matthew.towles
 * @date Sep 10, 2019
 */
public class ProdconSync {
    // number of slots in buffer
    static final int N = 100;
    
    // main 
    public static void main (String args[]) {
        Semaphore mutex = new Semaphore (1);
        Semaphore full = new Semaphore (0);
        Semaphore empty = new Semaphore (N);
        Producer prod = new Producer (mutex, full, empty);
        Consumer cons = new Consumer (mutex, full, empty);
        prod.start();
        cons.start();
    }
}
