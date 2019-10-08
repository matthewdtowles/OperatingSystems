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

    /**
     * Menu options given to user
     * at prompt of program
     */
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
    
    /**
     * Maximum amount allowed of frames
     * Frames value given by user may
     * not exceed this limit
     */
    static final int PHYSICAL_FRAMES_LIMIT = 8;
    
    /**
     * The number of different values in
     * a reference string may have
     */
    static final int VIRTUAL_FRAMES_COUNT = 10;

    /**
     * Will hold current reference string
     */   
    private static ArrayList<Integer> referenceString;
    
    /**
     * Number of physical frames
     * Given by user as cmd line arg
     * Must be less than PHYSICAL_FRAMES_LIMIT
     * This is how many different reference
     * string values may be held at any time
     */
    private static int frameCount;
    
    /**
     * Physical frames with current values
     * held from referenceString
     */
    private static LinkedList<Integer> frames;
    
    /**
     * Keeps track of faults for each evaluation
     * of referenceString values
     */
    private static ArrayList<Boolean> faults;
    
    /**
     * Keeps track of victims for each evaluation
     * of referenceString values
     */
    private static ArrayList<Integer> victims;    
    
    /**
     * List of states of frames for each
     * evaluation of referenceString values
     */
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
        System.out.print("Select option: ");
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
                initVars();
                break;
            case 2:
                getRandomRefString();
                initVars();
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
        System.out.print("Reference string ||");
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
        // loop through each val in reference string
        for(int val : referenceString) {
            // check if val in memory/frames 
            // if not - page fault
            if (!frames.contains(val)) {
                faults.add(Boolean.TRUE);
                frames.addFirst(val);
                // remove last frame (first frame in)
                if (frames.size() > frameCount) {
                    int victim = frames.removeLast();
                    victims.add(victim);
                } else {
                    victims.add(null);
                }
            } else {
                // no page fault - add false as placeholder
                faults.add(Boolean.FALSE);
                // no victim - null as placeholder
                victims.add(null);
            }
            // add current state of frames as a snapshot
            // so that we can traverse snapShots to 
            // display in output table
            LinkedList<Integer> framesCopy = new LinkedList<>(frames);
            snapShots.add(framesCopy);

            // update user with progress in table
            displayTable();
            
            // prompt user to continue after showing table
            continuePrompt();
        }
        displayTotalFaults();
    }
    
    
    /**
     * Simulates OPT paging alg on referenceString
     * User presses a key after each step to continue
     * Total number of faults displayed at end
     * Menu Option 5
     */
    private static void simOPT() throws IOException {
        reset();
        for (int i = 0; i < referenceString.size(); i++) {
            int val = referenceString.get(i);
            if (!frames.contains(val)) {
                faults.add(Boolean.TRUE);                
                if (frames.size() >= frameCount) {
                    int victim = findVictimOPT(i);
                    victims.add(victim);
                    int index = findIndex(victim);   
                    frames.set(index, val);
                } else {
                    frames.add(val);
                    victims.add(null);
                }
            } else {
                faults.add(Boolean.FALSE);
                victims.add(null);
            }
            LinkedList<Integer> framesCopy = new LinkedList<>(frames);
            snapShots.add(framesCopy);
            displayTable();
            continuePrompt();
        }
        displayTotalFaults();
    }
    
    
    /**
     * Returns the value of next victim for OPT alg
     * @param index
     * @return val of victim for OPT alg
     */
    private static int findVictimOPT(int index) {
        LinkedList<Integer> framesCopy = new LinkedList<>(frames);
        // loop through referenceString starting at 
        for (int i = (index + 1); i < referenceString.size(); i++) {
            if (framesCopy.size() == 1) {
                return framesCopy.peek();
            }
            if (framesCopy.contains(referenceString.get(i))) {
                framesCopy.remove(referenceString.get(i));
                if (framesCopy.size() == 1) {
                    return framesCopy.peek();
                }
            }
        }
        // none returned - current frames do not repeat
        // so just pick the top one
        return framesCopy.peek();
    }
    
    
    /**
     * Simulates LRU paging alg on referenceString
     * User presses a key after each step to continue
     * Total number of faults displayed at end
     * Menu Option 6
     */
    private static void simLRU() throws IOException {
        reset();
        for (int i = 0; i < referenceString.size(); i++) {
            int val = referenceString.get(i);
            if (!frames.contains(val)) {
                faults.add(Boolean.TRUE);                
                if (frames.size() >= frameCount) {
                    int victim = findVictimLRU(i);
                    victims.add(victim);
                    int index = findIndex(victim);   
                    frames.set(index, val);
                } else {
                    frames.add(val);
                    victims.add(null);
                }
            } else {
                faults.add(Boolean.FALSE);
                victims.add(null);
            }
            LinkedList<Integer> framesCopy = new LinkedList<>(frames);
            snapShots.add(framesCopy);
            displayTable();
            continuePrompt();
        }
        displayTotalFaults();
    }
    
    
    /**
     * Returns the value of next victim for LRU alg
     * @param index
     * @return val of victim for LRU alg
     */
    private static int findVictimLRU(int index) {
        LinkedList<Integer> framesCopy = new LinkedList<>(frames);
        for (int i = (index - 1); i > 0; i--) {
            if (framesCopy.size() == 1) {
                return framesCopy.peek();
            }
            if (framesCopy.contains(referenceString.get(i))) {
                framesCopy.remove(referenceString.get(i));
                if (framesCopy.size() == 1) {
                    return framesCopy.peek();
                }
            }
        }
        return framesCopy.peek();
    }
        
    
    /**
     * Simulates LFU paging alg on referenceString
     * User presses a key after each step to continue
     * Total number of faults displayed at end
     * Menu Option 7
     */
    private static void simLFU() throws IOException {
        reset();
        for (int i = 0; i < referenceString.size(); i++) {
            int val = referenceString.get(i);
            if (!frames.contains(val)) {
                faults.add(Boolean.TRUE);                
                if (frames.size() >= frameCount) {
                    int victim = findVictimLFU(i);
                    victims.add(victim);
                    int index = findIndex(victim);   
                    frames.set(index, val);
                } else {
                    frames.add(val);
                    victims.add(null);
                }
            } else {
                faults.add(Boolean.FALSE);
                victims.add(null);
            }
            LinkedList<Integer> framesCopy = new LinkedList<>(frames);
            snapShots.add(framesCopy);
            displayTable();
            continuePrompt();
        }
        displayTotalFaults();
    }
    
    
    /**
     * Returns the value of next victim for LFU alg
     * @param index
     * @return val of victim for LFU alg
     */
    private static int findVictimLFU(int index) {
        // keep track of how many times each value
        // in reference string has occurred so far
        int[] frameFrequencies = new int[VIRTUAL_FRAMES_COUNT];
        for (int i = 0; i < index; i++) {
            // i = index for referenceString
            // val at i in referenceString is index
            //  for frameFrequencies
            frameFrequencies[referenceString.get(i)]++;
        }

        // find least frequently used val by finding 
        // array key that is  a value in frames and
        // less than all other array entries in frames
        int lfu = frames.get(0);
        for (int i = 0; i < frameFrequencies.length; i++) {
            // only check against values currently in frames
            if (frames.contains(i)) {
                for (int j = 0; j < frameFrequencies.length; j++) {
                    // only check values in frames
                    if (frames.contains(j)) {
                        // dont compare to self:
                        if (j != i) {
                            // find which of two comparing occurs less
                            if (frameFrequencies[j] < frameFrequencies[i]) {
                                // make sure it is less than previous lfu
                                if (frameFrequencies[j] < frameFrequencies[lfu]) {
                                    lfu = j;
                                }
                            } else {
                                // make sure it is less than previous lfu
                                if (frameFrequencies[i] < frameFrequencies[lfu]) {
                                    lfu = i;
                                }
                            }
                        }
                    }
                }
            }
        }
        return lfu;
    }
    
    
    /**
     * Returns index of the victim in frames
     * @param victim
     * @return index of victim in frames
     */
    private static int findIndex(int victim) {
        for (int i = 0; i < frames.size(); i++) {
            if (victim == frames.get(i)) {
                return i;
            }
        }
        return -1;
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
     * Prints out physical frame rows
     *  for the output table
     */
    private static void displayFrames() {
        for(int i = 0; i < frameCount; i++) {
            System.out.print("Physical frame " + String.valueOf(i) + " ||");
            for (int j = 0; j < snapShots.size(); j++) {
                if (i > (snapShots.get(j).size() - 1)) {
                    System.out.print("   |");
                } else {
                    System.out.print(" " + snapShots.get(j).get(i) + " |");
                }
            }
            System.out.println();
        }
    }
    
    
    /**
     * Prints out an 'F' for each column
     * in the output table where there
     * was a page fault
     */
    private static void displayFaults() {
        System.out.print("Page faults      ||");
        for (boolean b : faults) {
            if (b) {
                System.out.print(" F |");
            } else {
                System.out.print("   |");
            }
        }
        System.out.println();
    }
    
    
    /**
     * Displays the total amount of page faults
     */
    private static void displayTotalFaults() {
        int total = 0;
        for (boolean b : faults) {
            if (b) {
                total++;
            }
        }
        System.out.println("Total page faults: " + String.valueOf(total));
        displayLine();
        displayLine();
    }
    
    
    /**
     * Prints out the victim frame
     * for each column in the output
     * table if there was a victim
     */
    private static void displayVictims() {
        System.out.print("Victim frames    ||");
        for (int i = 0; i < victims.size(); i++) {
            String out;
            if (victims.get(i) == null) {
                out = "   |";
            } else {
                out = " " + String.valueOf(victims.get(i)) + " |";
            }
            System.out.print(out);
        }
        System.out.println();
    }
    
    
    /**
     * Resets variables for simulation algs
     */
    private static void reset() {
        faults.clear();
        snapShots.clear();
        frames.clear();
        victims.clear();
        initVars();
    }
    
    
    /**
     * Initializes all simulation alg vars
     */
    private static void initVars() {
        int size = referenceString.size();
        faults = new ArrayList<>(size);
        victims = new ArrayList<>(size);
        snapShots = new ArrayList<>(size);
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
        referenceString = new ArrayList<>();
        frames = new LinkedList<>();
        
        // menu prompt and beginning the program:
        promptUser();
    }
}
