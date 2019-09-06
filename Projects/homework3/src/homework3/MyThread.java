package homework3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MyThread
 * 
 * @author matthew.towles
 * @date Sep 6, 2019
 */
public class MyThread extends Thread {

    /**
     * Implementation of Thread.run() 
     * Writes 1000 lines to a file 
     * Used for testing Threads
     */
    @Override
    public void run() {
        try (FileWriter writer = new FileWriter(Controller.MY_FILE)) {
            for (int i = 0; i < Controller.ITERATIONS; i++) {
                String output = this.getName() +  " - iteration no. " 
                        + String.valueOf(i + 1);
                writer.write(String.valueOf(i) + ". " + output + "\r\n");
                System.out.println(output);
            }
        } catch (IOException ex) {
            Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
