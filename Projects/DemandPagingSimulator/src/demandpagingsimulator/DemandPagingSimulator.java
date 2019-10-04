package demandpagingsimulator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
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
    
    /**
     * The number of different values in
     * a reference string may have
     */
    static final int VIRTUAL_FRAMES_COUNT = 10;

    /**
     * Will hold current reference string
     */
//    private static String referenceString;
    
    private static ArrayList<Integer> referenceString;
    
    /**
     * Number of physical frames
     * Given by user as cmd line arg
     * Must be less than PHYSICAL_FRAMES_LIMIT
     * This is how many different reference
     * string values may be held at any time
     */
    private static int frameCount;
    
    private static LinkedList<Integer> frames;
    
    private static ArrayList<Boolean> faults;
    
    private static ArrayList<Integer> victims;    
    
    private static ArrayList<LinkedList<Integer>> snapShots;
    
    
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
        
        if (selection > 2 && referenceString.isEmpty()) {
            throw new IOException("Reference string must be set first.");
        }
        
        switch (selection) {
            case 0:
                System.out.println("\r\nGoodbye!");
                System.exit(0);
                break;
            case 1:
                getUserRefString();
                break;
            case 2:
                getRandomRefString();
                break;
            case 3:
                displayReferenceString();
                break;
            case 4:
                simFIFO();
                break;
            case 5:
                simOPT();
                break;
            case 6:
                simLRU();
                break;
            case 7:
                simLFU();
                break;
            default:
                throw new IOException("Invalid menu option. Select 0 - 7");
        }
    }
    
    
    /**
     * Sets the frameCount from cmd line arg
     * If no valid argument given, sets a 
     * default value for the frameCount
     * @param count 
     */
    private static void setFrameCount(int count) {
        if (!validFrameCount(count)) {
            count = (PHYSICAL_FRAMES_LIMIT - 1);
        }
        frameCount = count;
    }
    
    
    /**
     * Returns whether given count is a
     * valid frameCount
     * @param count
     * @return boolean
     */
    private static boolean validFrameCount(int count) {
        return (count < PHYSICAL_FRAMES_LIMIT && count > 0);
    }
    
    
    /**
     * Saves reference string given by user
     * Validates user's input
     * Will re-prompt for new string if input
     * is not valid -- int or whitespace
     * Menu Option 1
     */
    private static void getUserRefString() {
        referenceString.clear();
        System.out.println("Enter reference string: ");
        Scanner scanner = new Scanner(System.in);
        String refString = scanner.nextLine();

        // check for invalid chars: non-digits and non-ws
        for (char c : refString.toCharArray()) {
            if (!Character.isDigit(c) && !Character.isWhitespace(c)) {
                System.out.println("Reference string may only have numbers and "
                        + "white space.");
                System.out.println();
                getUserRefString();
                return;
            }
            
            // add only ints to referenceString
            if (Character.isDigit(c)) {
                referenceString.add(Character.getNumericValue(c));
            }
        }
    }
    
    
    /**
     * Saves a randomly gen'd reference string
     * Length of reference string given by user
     * Menu Option 2
     */
    private static void getRandomRefString() {
        referenceString.clear();
        System.out.println("Enter length of reference string: ");
        Scanner scanner = new Scanner(System.in);
        int strLength = scanner.nextInt();
        for (int i = 0; i < strLength; i++) {
            int next = (int)Math.floor(Math.random()*10);
            referenceString.add(next);
        }
    }
    
    
    /**
     * Prints out the current referenceString
     * Also used for first row of output table
     * Menu Option 3
     */
    private static void displayReferenceString() {
        displayLine();
        System.out.print("Reference string |");
        for(int val : referenceString) {
            System.out.print(" " + val + " |");
        }
        System.out.println();
    }
    

    /**
     * Simulates FIFO paging alg on referenceString
     * User presses a key after each step to continue
     * Total number of faults displayed at end
     * Menu Option 4
     */
    private static void simFIFO() throws IOException {
        reset();
        for(int val : referenceString) {
            if (!frames.contains(val)) {
                faults.add(Boolean.TRUE);
                frames.addFirst(val);
                if (frames.size() >= frameCount) {
                    int victim = frames.removeLast();
                    victims.add(victim);
                } else {
                    victims.add(null);
                }
            } else {
                faults.add(Boolean.FALSE);
                victims.add(null);
            }
            snapShots.add(frames);

            displayTable();
            
            continuePrompt();
        }
    }
    
    
    /**
     * Simulates OPT paging alg on referenceString
     * User presses a key after each step to continue
     * Total number of faults displayed at end
     * Menu Option 5
     */
    private static void simOPT() {
        reset();
    }
    
    
    /**
     * Simulates LRU paging alg on referenceString
     * User presses a key after each step to continue
     * Total number of faults displayed at end
     * Menu Option 6
     */
    private static void simLRU() {
        reset();
    }
    
    
    /**
     * Simulates LFU paging alg on referenceString
     * User presses a key after each step to continue
     * Total number of faults displayed at end
     * Menu Option 7
     */
    private static void simLFU() {
        reset();
    }
    
    
    /**
     * Print out a table with reference string,
     * physical frames, page faults, victim frames
     */
    private static void displayTable() {
        displayReferenceString();
        displayFrames();
        displayFaults();
        displayVictims();
        displayLine();
        System.out.println();
    }
    
    
    /**
     * Prints out a physical frame row for the
     * output table
     */
    private static void displayFrames() {
        for(int i = 0; i < snapShots.size(); i++) {
            System.out.print("Physical frame " + String.valueOf(i) + " |");
            for (int val : snapShots.get(i)) {
                System.out.print(" " + String.valueOf(val) + " |");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private static void displayFaults() {}
    private static void displayVictims() {}
    
    
    /**
     * Resets variables for simulation algs
     */
    private static void reset() {
        faults.clear();
        snapShots.clear();
        frames.clear();
        victims.clear();
    }
    
    
    /**
     * Initializes all simulation alg vars
     */
    private static void initVars() {
        referenceString = new ArrayList<>();
        frames = new LinkedList<>();
        faults = new ArrayList<>();
        victims = new ArrayList<>();
        snapShots = new ArrayList<>();
    }
        
    
    /**
     * Displays a standardized line separator
     */
    private static void displayLine() {
        System.out.println("- - - - - - - - - - - - - - - - - - - -");
    }
    
    
    /**
     * Prompts user to continue
     * @throws IOException 
     */
    private static void continuePrompt() throws IOException {
        System.out.println("Press ENTER to continue.");
        System.in.read();
    }
    
    /**
     * Main
     * @param args 
     */
    public static void main(String[] args) {
        // set up frame count
        if (args.length > 0) {
            setFrameCount(Integer.parseUnsignedInt(args[0]));
        } else {
            setFrameCount(PHYSICAL_FRAMES_LIMIT - 1);
        }
        
        // display frame count for user
        displayLine();
        System.out.println("FRAME COUNT = " + String.valueOf(frameCount));
        displayLine();
        System.out.println();
        
        // initialize variables:
        initVars();
        
        // menu prompt and beginning the program:
        promptUser();
    }
}
