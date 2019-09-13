public class Producer extends Thread {
    private Semaphore mutex;
    private Semaphore full;
    private Semaphore empty;
    
    // constructor
    public Producer (Semaphore m, Semaphore f, Semaphore e) {
        mutex = m;
        full = f;
        empty = e;
    }

    @override
    public void run() {
        while (true) {
        
            //[ produce an item here ]
            
            // are there any empty slots?
            empty.wait();
            
            // acquire exclusive access
            mutex.wait();
            
            // [ deposit an item into an empty slot of the buffer here ]
            
            // release mutual exclusion
            mutex.signal();

            // increment full slots 
            full.signal();
        }
    }
}