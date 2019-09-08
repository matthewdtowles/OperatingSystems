package homework3;

/**
 * SomeThread
 * Creates 3 new threads
 * Synchronizes threads to show id
 * in turn for 5 iterations
 * 
 * @author matthew.towles
 * @date Sep 6, 2019
 */
public class SomeThread extends Thread {

    // number of iterations to print each thread
    private static final int ITERATIONS = 5;
    
    // number of threads to create
    private static final int THREADS_TO_RUN = 3;
    
    // used to: pause thread execution
    private static final Object LOCK = new Object();
    
    // total number of SomeThreads insantiated
    private static int threadsMade = 0;
    
    /**
     * thread with ID equal to threadTurn 
     * is the only thread allowed to run
     */
    private static int threadTurn = 1;
    
    // auto-incrementing ID
    private int id;
    
    /**
     * Constructor
     * Increments threadsMade
     * Assigns an ID and name
     */
    public SomeThread() {
        threadsMade++;
        this.id = threadsMade;
        String name = "Thread " + this.id;
        this.setName(name);
    }

    /**
     * Execute a loop a number of times
     * specified by SomeThread.ITERATIONS
     * Pause execution after each loop and
     * begin next loop when notified to 
     * stay in sync with other threads
     * 
     * How it works:
     * 1. Lock method down with LOCK
     * 2. Loop ITERATIONS times
     * 3. If not this thread's turn, wait
     * -- Once it is this thread's turn: --
     * 4. Print message to console
     * 5. Update flag for next thread's turn
     * 6. Wake up methods since they're waiting
     * 7. Go back to step 3 until 2 terminates
     * 
     * Other considerations:
     * Rather than controlling which thread
     * will execute next a method which called
     * sleep with an increasing integer was
     * called, but this was not elegant nor
     * did it guarantee the correct order
     */
    @Override
    public void run() {
        synchronized(LOCK) {
            for (int i = 1; i <= ITERATIONS; i++) {
                while(this.id != threadTurn) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException ex) {}
                }
                printMsg(i);
                threadTurn = getNextThread();
                LOCK.notifyAll();
            }
        }
    }
        
    /**
     * Returns ID of the next thread that
     * should continue to execute
     * @return int nextThreadId
     */
    public int getNextThread() {
        int nextThreadId = (threadTurn % THREADS_TO_RUN);
        nextThreadId++;
        return nextThreadId;
    }
    
    /**
     * Prints thread name and iteration number
     * @param i
     */
    public void printMsg(int i) {
        System.out.println(this.getName() + " - iteration no. " + i);
        System.out.println();
    }
        
        
    /**
     * Main class
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        
        // array of threads
        SomeThread[] threads = new SomeThread[THREADS_TO_RUN];
        
        // create and store threads in our array
        for (int i = 0; i < THREADS_TO_RUN; i++) {
            threads[i] = new SomeThread();
        }
        
        // run each thread - separated from above for testing
        for (int i = 0; i < THREADS_TO_RUN; i++) {
            threads[i].start();
        }
    }
}