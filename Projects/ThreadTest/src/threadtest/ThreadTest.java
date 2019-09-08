package threadtest;

/**
 * ThreadTest
 * 
 * @author matthew.towles
 * @date Sep 7, 2019
 */
// ThreadTest.java
public class ThreadTest extends Thread {

    static int numThread = 0;
    static int threadAllowedToRun = 1;

    int myThreadID;

    private static Object myLock = new Object();

    public ThreadTest() {
        // myThreadID auto increments upon creation of new ThreadTest
        this.myThreadID = 1 + numThread++;
    }

    @Override
    public void run() {
        synchronized (myLock) {
            for(int i = 0; i < 5; i++) {
                while (myThreadID != threadAllowedToRun) {
                    try {
                        myLock.wait();
                    } catch (InterruptedException ex) {} 
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {}

                System.out.println("Thread " + myThreadID + " - Iteration " 
                        + String.valueOf(i+1));
                threadAllowedToRun = 1 + threadAllowedToRun % numThread;
                myLock.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Thread thread1 = new ThreadTest();
        Thread thread2 = new ThreadTest();
        Thread thread3 = new ThreadTest();

        thread3.start();
        thread2.start();
        thread1.start();

    }
}