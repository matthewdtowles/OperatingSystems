package homework2;

/**
 * Controller
 * 
 * Instantiates 5 objects of
 * CPUBound and IOBound classes
 * All thread objects run concurrently
 *
 * Takes the start and stop time for
 * each thread and prints out time
 * taken to run
 *
 * Takes start and stop time to schedule
 * and run all threads and prints out
 * the time taken to run
 * 
 * @author matthew.towles
 * @date Aug 31, 2019
 */
public class Controller {
    
    /**
     * Number of loops child Threads will iterate operations
     */
    static final int LOOPS = 1000;
    
    /**
     * File that IOBound will write to
     * Update with value for your machine
     */
    static final String MY_FILE = "C:/Users/matthew.towles/MyEdu/OperatingSystems/test/outputFile.txt";
    
    /**
     * Amount of each thread type to run
     */
    private static final int NUM_THREADS = 5;
        
    /**
     * Runs and returns run time of given Thread
     * @param Thread    thread
     * @return long     runtime
     */
    static String getThreadRunTime(Thread thread) {
        long start = System.currentTimeMillis();
        thread.start();
        long finish = System.currentTimeMillis();
        long runtime = finish - start;
        String out = "Runtime for " + thread.getName() 
                + ": " + String.valueOf(runtime);
        return out;
    }
    
    /**
     * Main class
     * @param   args
     * @throws  InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        
        long start = System.currentTimeMillis();
        
        // arrays of IOBound and CPUBound Threads
        IOBound[] ioThreads = new IOBound[NUM_THREADS];
        CPUBound[] cpuThreads = new CPUBound[NUM_THREADS];

        // set Threads in arrays and start execution
        for (int i = 0; i < NUM_THREADS; i++) {
            ioThreads[i] = new IOBound();
            cpuThreads[i] = new CPUBound();
            
            ioThreads[i].setName("IOThread-" + String.valueOf(i));
            cpuThreads[i].setName("CPUThread-" + String.valueOf(i));
            
            ioThreads[i].start();
            cpuThreads[i].start();
        }
        
        // join each Thread in arrays to prevent main
        // thread from executing too early
        for (int i = 0; i < NUM_THREADS; i++) {
            ioThreads[i].join();
            cpuThreads[i].join();
        }
        
//        Thread.sleep(3500);

        long end = System.currentTimeMillis();
        // overall runtime
        long runtime = end - start;
        
        // display overall runtime results
        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
        System.out.println("Controller thread. My name is: " +
                Thread.currentThread().getName());
        System.out.println();
        System.out.println("Overall Runtime: " + String.valueOf(runtime));
        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
        
        // display results for each child Thread
        for (int i = 0; i < NUM_THREADS; i++) {
            System.out.println(ioThreads[i].getName() + " runtime: " 
                    + String.valueOf(ioThreads[i].getRuntime()));
            System.out.println(cpuThreads[i].getName() + " runtime: " 
                    + String.valueOf(cpuThreads[i].getRuntime()));
        }
    }
}
