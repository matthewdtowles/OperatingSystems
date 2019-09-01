/**
 * Controller class
 * Instantiates 5 objects of
 * CPUBound and IOBound classes
 * All thread objects run concurrently
 *
 * Take the start and stop time for
 * each thread and print out time
 * taken to run
 *
 * Take start and stop time to schedule
 * and run all threads and print out
 * the time taken to run
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
 */
public class Controller {


    public static void main(String[] args) throws InterruptedException {

        IOBound writer = new IOBound();
        writer.start();
        System.out.println("I am the Controller thread. My name is: " +
                Thread.currentThread().getName());
    }
}
