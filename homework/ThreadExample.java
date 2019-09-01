/**
 * Author: Mike Tarquinio
 * Description:
 * 		   This file contains the ThreadExample class.  
 *         It provides an example of two threads running.
 */


/**
 * This class supports running a thread that adds money
 * to an account balance.  The account balance is a class
 * variable.
 */
public class ThreadExample implements Runnable {
	
	// class variable holding an accountBalance	 
	private static int accountBalance = 0;
	
	// static holding the number of times to increment 
	// the accountBalance
	private static final int ITERATIONCOUNT = 200;
	
	// name of the running thread	 
	private String threadName = "";
	
	/**
	 * Constructor
	 * @param threadName - name of the thread
	 */
	public ThreadExample(String threadName) {
		this.threadName = threadName;
	}
	
	/**
	 * run method
	 */
	public void run() {
		
		accountBalance = 0;
		
		for (int i = 0; i < ITERATIONCOUNT; i++) {			
			accountBalance += 1;
			System.out.println("Thread: " + threadName + " balance= " + accountBalance);
		}
	}

	/**
	 * main - it creates and starts 4 threads and then sleeps
	 *        for 15 seconds
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println("starting thread example");
		
		// create four threads
		Thread thread1 = new Thread(new ThreadExample("thread1"));
		Thread thread2 = new Thread(new ThreadExample("thread2"));
		Thread thread3 = new Thread(new ThreadExample("thread3"));
		Thread thread4 = new Thread(new ThreadExample("thread4"));
		
		
		// start the threads		 
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		
		// put the main threads asleep for 15 seconds		 
		Thread.sleep(15000);
		
		System.out.println("ending thread example");
	}
}
