package bankersalgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * BankersAlgorithm
 *
 * Asks user for an input file
 * Input file expected to be formatted:
 * First line: amount of each resource
 * Subsequent lines are max claims
 * Followed by Allocated resources
 * Separated by ';'
 * 
 * Then BankersAlgorithm is applied to
 * the input provided in the formatted
 * file
 * 
 * @author matthew.towles
 * @date Sep 13, 2019
 */
public class BankersAlgorithm {
    static final int TOTAL_PROCESSES = 7;
    static final int TOTAL_RESOURCE_TYPES = 5;
    static final String USER_PROMPT = "Enter file name";
    static final String RESOURCES_FILE_KEY = "Resources";
    static final String FILE_LINE_DELIMITER = ";";
    static final String FILE_INT_DELIMITER = ",";
    
    private ArrayList<ArrayList<String>> safePaths;
    
    private Process[] processes;
    private int[] resources;
    private int[] allocated;
    private int[] available;
    
    
    /**
     * Constructor
     */
    public BankersAlgorithm() {
        processes = new Process[TOTAL_PROCESSES];
        resources = new int[TOTAL_RESOURCE_TYPES];
        allocated = new int[TOTAL_RESOURCE_TYPES];
        available = new int[TOTAL_RESOURCE_TYPES];
        safePaths = new ArrayList<>();
    } 
    
    
    /**
     * Updates this.allocated with all
     * currently allocated resources
     * Checks each process and each process'
     * resource types allocated to it
     */
    private void initAllocated() {
        for (Process process : processes) {
            for (int i = 0; i < process.getAllocated().length; i++) {
                allocated[i] += process.getAllocated()[i];
            }
        }
    }
    
    
    /**
     * Updates this.available with all 
     * currently available resources
     */
    private void initAvailable() {
        for (int i = 0; i < resources.length; i++) {
            available[i] = resources[i] - allocated[i];
        }
    }
    
    
    /**
     * Finds a valid path and adds it to safePaths
     */
    private void findSafePaths() {
        int[] availableCopy = new int[TOTAL_RESOURCE_TYPES];
        System.arraycopy(available, 0, availableCopy, 0, TOTAL_RESOURCE_TYPES);
        
        // next valid process' name is added to sequence
        ArrayList<String> sequence = new ArrayList<>();
        
        int i = 0;
        // loop through each process
        while (true) {
            if (i >= TOTAL_PROCESSES) {
                return;
            }
            // whether we add process to sequence
            boolean addToSequence = true;
            
            // loop through process resource types
            // compare what it needs to what we have
            for (int j = 0; j < TOTAL_RESOURCE_TYPES; j++) {
                if (sequence.contains(processes[i].getName()) 
                        || processes[i].getNeed()[j] > availableCopy[j]) {
                    addToSequence = false;
                }
            }
            if (addToSequence) {
                sequence.add(processes[i].getName());
                
                // free up the resources that process used
                for (int k = 0; k < TOTAL_RESOURCE_TYPES; k++) {
                    availableCopy[k] += processes[i].getAllocated()[k];
                }

                // reset outerloop to iterate over
                // processes again
                i = 0;   
            } else {
                i++;
            }
            // all processes added to sequence
            // add sequence to safePaths and exit
            if (sequence.size() == TOTAL_PROCESSES) {
                // add sequence to safePaths
                safePaths.add(sequence);
                return;
            }
        }
    }
    
    
    /**
     * Printing for debugging
     * @param arr
     * @param name 
     */
    private void printr(int[] arr, String name) {
        String str = name + ": ";
        for (int i = 0; i < arr.length; i++) {
            str = str.concat(String.valueOf(arr[i]));
            str = str.concat(", ");
        }
        System.out.println("BEGIN " + name);
        System.out.println(str);
        System.out.println("END " + name);
    }
    
    
    /**
     * Print out the formatted results
     */
    private void printSafePaths() {
        String str = "\r\nSafe Paths:\r\n";
        for (int i = 0; i < safePaths.size(); i++) {
            str = str.concat("<");
            for (int j = 0; j < safePaths.get(i).size(); j++) {
                str = str.concat(safePaths.get(i).get(j));
                if (j < (safePaths.get(i).size() - 1)) {
                    str = str.concat(", ");
                }
            }
            str = str.concat(">\r\n");
        }
        if (safePaths.isEmpty()) {
            str = str.concat("No safe paths exist!");
        }
        System.out.println(str);
        System.out.println();
    }
    
    
    /**
     * Main
     * @param args
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println(USER_PROMPT);
        String fileName = scanner.nextLine();
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
    
        
        BankersAlgorithm bankers = new BankersAlgorithm();
        
        System.out.println("Input file:");
        String line;
        int n = 0;
        while ((line = br.readLine()) != null) {
            // display the input file contents
            System.out.println(line);
            String[] explodedLine = line.split(FILE_LINE_DELIMITER);
            if (explodedLine[0].equals(RESOURCES_FILE_KEY)) {
                String[] resStr = explodedLine[1].split(FILE_INT_DELIMITER);
                bankers.resources = new int[resStr.length];
                for (int i = 0; i < resStr.length; i++) {
                    bankers.resources[i] = Integer.parseInt(resStr[i]);
                }
                n--;
            } else {
                Process process = new Process();
                
                String name = "P" + String.valueOf(n+1);
                process.setName(name);
                
                String[] maxClaimString = explodedLine[0].split(FILE_INT_DELIMITER);
                int[] maxClaim = new int[maxClaimString.length];
                
                String[] allocatedString = explodedLine[1].split(FILE_INT_DELIMITER);
                int[] allocated = new int[allocatedString.length];
                
                // allocated and maxClaim will have same length
                for (int i = 0; i < maxClaimString.length; i++) {
                    maxClaim[i] = Integer.parseInt(maxClaimString[i]);
                    allocated[i] = Integer.parseInt(allocatedString[i]);
                }
                process.setMaxClaim(maxClaim);
                process.setAllocated(allocated);
                bankers.processes[n] = process;
            }
            n++;
        }

        // indicate end of input file display
        System.out.println("- - - - - - - - - -");
        
        // update allocated and available resources
        bankers.initAllocated();
        bankers.initAvailable();
        
        bankers.findSafePaths();
        bankers.printSafePaths();
    }
}
