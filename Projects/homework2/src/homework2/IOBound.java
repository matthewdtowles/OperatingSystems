package homework2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * IOBound
 * Thread class that runs IO intensive operation
 * 
 * @author matthew.towles
 * @date Aug 31, 2019
 */
class IOBound extends Thread {
    
    /**
     * Runtime of this Thread
     * Recorded by run() 
     */
    private long runtime;
    
    
    /**
     * Writes to MY_FILE specified in Controller
     * Method called when IOBound thread start() called
     */
    @Override
    public void run() {
        try (FileWriter writer = new FileWriter(Controller.MY_FILE)) {
            long start = System.currentTimeMillis();
            for (int i = 1; i <= Controller.LOOPS; i++) {
                String output = "IOBound " + this.getName() + 
                        " - writing to " + Controller.MY_FILE;
                System.out.println(output);
                writer.write(String.valueOf(i) + ". " + output + "\r\n");
            }
            long end = System.currentTimeMillis();
            runtime = end - start;
        } catch (IOException ex) {
            Logger.getLogger(IOBound.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    /**
     * Getter for runtime
     * @return long runtime
     */
    public long getRuntime() {
        return runtime;
    }
}
