package bankersalgorithm;

/**
 * Process
 * 
 * @author matthew.towles
 * @date Sep 13, 2019
 */
public class Process {

    private String name;
    private int[] maxClaim;
    private int[] allocated;

    /**
     * Returns array of Resource types needed
     * for process to be completed
     * @return int[] need 
     */
    public int[] getNeed() {
        int [] need = new int[maxClaim.length];
        for (int i = 0; i < maxClaim.length; i++) {
            need[i] = maxClaim[i] - allocated[i];
        }
        return need;
    }
        
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getMaxClaim() {
        return maxClaim;
    }

    public void setMaxClaim(int[] maxClaim) {
        this.maxClaim = maxClaim;
    }

    public int[] getAllocated() {
        return allocated;
    }

    public void setAllocated(int[] allocated) {
        this.allocated = allocated;
    }
   
}
