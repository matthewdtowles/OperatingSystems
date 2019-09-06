package homework3;

/**
 * Controller
 * Creates 3 new threads
 * Synchronizes threads to show id
 * in turn for 5 iterations
 * 
 * @author matthew.towles
 * @date Sep 6, 2019
 */
public class Controller {

    // file for I/O tests
    static final String MY_FILE = "C:/Users/matthew.towles/MyEdu/OperatingSystems/test/outputFile.txt";

    // number of iterations to print each thread
    static final int ITERATIONS = 5;
    
    // number of threads to create
    static final int THREAD_COUNT = 3;
    
    /**
     * Main class
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        
        // array of threads
        MyThread[] myThreads = new MyThread[THREAD_COUNT];
        
        // create and store threads in our array
        for (int i = 0; i < THREAD_COUNT; i++) {
            myThreads[i] = new MyThread();
            String threadNo = String.valueOf(i + 1);
            myThreads[i].setName("Thread " + threadNo);
            myThreads[i].start();
            myThreads[i].join();
            /**
             * You are close, but all of one thread goes before another begins
             */
        }
    }
}
