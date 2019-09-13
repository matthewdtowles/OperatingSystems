public class Consumer extends Thread {
    private Semaphore mutex;
    private Semaphore full;
    private Semaphore empty;
    
    // constructor 
    public Consumer (Semaphore m, Semaphore f, Semaphore e) {
        mutex = m;
        full = f;
        empty = e;
    }

    @override
    public run() {
        while (true) {
            // are there any full slots?
            full.wait();
            
            // acquire exclusive access
            mutex.wait();
            
            //[ remove an item from a full slot of the buffer ]
            
            // release mutual exclusion
            mutex.signal();
            
            // increment empty slots
            empty.signal();
            
            //[ Consume data item ]
        }
    }
}