package homework2;

/**
 * Controller
 * 
 * Instantiates 5 objects of
 * CPUBound and IOBound classes
 * All thread objects run concurrently
 *
 * Takes the start and stop time for
 * each thread and print out time
 * taken to run
 *
 * Take start and stop time to schedule
 * and run all threads and print out
 * the time taken to run
 *
 * 
 * 
 * 
 * Run program a couple of times
 *
 * Attach code + document
 * Document includes:
 *      snapshots showing it ran
 *      + results
 *      Numbers from runs in a table
 *      1 Paragraph on lessons learned
 *      Document issues
 * 
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
        
        IOBound[] ioThreads = new IOBound[NUM_THREADS];
        CPUBound[] cpuThreads = new CPUBound[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            ioThreads[i] = new IOBound();
            cpuThreads[i] = new CPUBound();
            
            ioThreads[i].setName("IOThread-" + String.valueOf(i));
            cpuThreads[i].setName("CPUThread-" + String.valueOf(i));
            
            ioThreads[i].start();
            cpuThreads[i].start();
        }
        

        
        // give child threads adequate time to complete
        Thread.sleep(3500);

        long end = System.currentTimeMillis();
        // overall runtime
        long runtime = end - start;
        
        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
        System.out.println("Controller thread. My name is: " +
                Thread.currentThread().getName());
        System.out.println();
        System.out.println("Overall Runtime: " + String.valueOf(runtime));
        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
        
        for (int i = 0; i < NUM_THREADS; i++) {
            System.out.println(ioThreads[i].getName() + " runtime: " 
                    + String.valueOf(ioThreads[i].getRuntime()));
            System.out.println(cpuThreads[i].getName() + " runtime: " 
                    + String.valueOf(cpuThreads[i].getRuntime()));
        }
    }
}
