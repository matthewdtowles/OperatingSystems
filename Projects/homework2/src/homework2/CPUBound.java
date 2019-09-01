package homework2;

/**
 * CPUBound
 * Thread that runs a computationally intensive operation
 * @author matthew.towles
 * @date Aug 31, 2019
 */
public class CPUBound extends Thread {

    private long runtime;
            
    /**
     * The number to get square of
     */
    private int operand = 2701;
    
    
    /**
     * Calculates the square of the operand
     * for each iteration of the for loop
     * This method is called by CPUBound.start() 
     */
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < Controller.LOOPS; i++) {
            int product = operand * operand;
            String output = "CPUBound " + this.getName() + " - solving " 
                    + String.valueOf(operand) + "^2 " 
                    + " = " + String.valueOf(product);
            System.out.println(output);
        }
        long end = System.currentTimeMillis();
        runtime = end - start;
    }
    
    
    /**
     * Getter for runtime
     * @return long runtime
     */
    public long getRuntime() {
        return runtime;
    }
}
