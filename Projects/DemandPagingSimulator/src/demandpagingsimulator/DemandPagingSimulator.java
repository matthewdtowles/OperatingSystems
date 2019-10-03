package demandpagingsimulator;

import java.io.IOException;
import java.util.Scanner;

/**
 * DemandPagingSimulator
 * Final Project for CMSC 412 - OS
 * Simulates the execution of:
 * FIFO, OPT, LRU, & LFU
 * for a computer with N frames [N lt 8]
 * N provided by user as argument
 * Assumes single process running has 10 frames
 * Algorithms sim'd based on ref string 
 * Ref string read from keyboard or random
 * 
 * @author matthew.towles
 * @date Sep 26, 2019
 */
public class DemandPagingSimulator {

    static final String[] MENU = {
        "Exit",
        "Read reference string",
        "Generate reference string",
        "Display current reference string",
        "Simulate FIFO",
        "Simulate OPT",
        "Simulate LRU",
        "Simulate LFU"
    };
    
    static final int PHYSICAL_FRAMES_LIMIT = 8;
    static final int VIRTUAL_FRAMES_COUNT = 10;
    
    static final String INVALID_OPTION_MSG = "Invalid menu option. Select 0 - 7";

    /**
     * Will hold current reference string
     */
    private static String referenceString;
    
    
    /**
     * When called - this runs the application
     * Keeps running application until user exits
     */
    public static void promptUser() {
        try {
            displayMenu();
            int selection = getSelection();
            processSelection(selection);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println();
            promptUser();
        }
    }
    
    
    /**
     * Displays menu options
     */
    private static void displayMenu() {
        for (int i = 0; i < MENU.length; i++) {
            System.out.println(String.valueOf(i) + " - " + MENU[i]);
        }
        System.out.println("Select option: ");
    }
    
    
    /**
     * Returns user's selection from menu
     * @return int selection
     */
    private static int getSelection() {
        Scanner scannerIn = new Scanner(System.in);
        int selection = scannerIn.nextInt();
        return selection;
    }
    
    
    /**
     * Routes user's menu selection
     * @param selection - selected menu option
     */
    private static void processSelection(int selection) throws IOException {
        
        switch (selection) {
            case 0:
                System.out.println("\r\nGoodbye!");
                System.exit(0);
                break;
            case 1:
                // read reference string from keyboard
                // save reference string in a buffer
                // verify/validate each value in reference string OR reject
                break;
            case 2:
                // randomly generate a reference string
                // length of reference string given by user interactively
                // save reference string in a buffer
                break;
            case 3:
                // display current reference string
                // if none - NO_REF_STRING_ERROR
                break;
            case 4:
                // simulate FIFO
                // if none - NO_REF_STRING_ERROR
                // user presses a key after each step to continue simulation
                // total faults displayed at end of simulation
                break;
            case 5:
                // simulate OPT
                // if none - NO_REF_STRING_ERROR
                // user presses a key after each step to continue simulation
                // total faults displayed at end of simulation
                break;
            case 6:
                // simulate LRU
                // if none - NO_REF_STRING_ERROR
                // user presses a key after each step to continue simulation
                // total faults displayed at end of simulation
                break;
            case 7:
                // simulate LFU
                // if none - NO_REF_STRING_ERROR
                // user presses a key after each step to continue simulation
                // total faults displayed at end of simulation
                break;
            default:
                throw new IOException(INVALID_OPTION_MSG);
        }
    }
    
    
    /**
     * Main
     * @param args 
     */
    public static void main(String[] args) {
        promptUser();
    }
}
