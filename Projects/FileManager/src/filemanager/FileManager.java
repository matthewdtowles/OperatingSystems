package filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * FileManager
 * 
 * @author matthew.towles
 * @date Sep 16, 2019
 */
public class FileManager {
    
    static final String EXIT = "Exit";
    static final String SELECT_DIR = "Select directory";
    static final String LIST_DIR_CONTENT = "List directory content (first level)"; 
    static final String LIST_ALL = "List directory content (all levels)";
    static final String DELETE_FILE = "Delete file";
    static final String DISPLAY_FILE = "Display file (hexadecimal view)";
    static final String ENCRYPT_FILE = "Encrypt file (XOR with password)";
    static final String DECRYPT_FILE = "Decrypt file (XOR with password)";
    
    
    /**
     * Menu of options for user
     */
    static final String[] MENU = {
        EXIT,
        SELECT_DIR,
        LIST_DIR_CONTENT,
        LIST_ALL,
        DELETE_FILE,
        DISPLAY_FILE,
        ENCRYPT_FILE,
        DECRYPT_FILE
    };
    
    static final String FILE_NOT_FOUND_MSG = "Directory does not exist. Please "
            + "select a valid directory";
    
    static final String FILE_NOT_GIVEN_MSG = "Please provide a valid directory "
            + "by choosing option 1 first.";
    
    static final String INVALID_OPTION_MSG = "Invalid menu option. Select 0 - 7";
    
    static final String DIRECTORY_PROMPT = "Enter absolute path of directory: ";
    static final String FILE_PROMPT = "Enter file name in current directory: ";
    
    static final String DS = "\\";
    
    
    
    /**
     * User provided directory
     */
    private static File directory;
    
    
    /**
     * 
     */
    public static void promptUser() {
        try {
            displayMenu();
            int selection = getSelection();
            processSelection(selection);
        } catch (NullPointerException e) {
            System.out.println(FILE_NOT_GIVEN_MSG);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println();
            promptUser();
        }
    }
    
    
    /**
     * Displays the menu options
     * Note: This only displays the menu
     * FileManager.getSelection() must be
     * called after this to capture input
     */
    private static void displayMenu() {
        for (int i = 0; i < MENU.length; i++) {
            System.out.println(String.valueOf(i) + " - " + MENU[i]);
        }
        System.out.println("Select option:");
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
     * for execution
     * @param selection - menu option selected
     * @throws IOException 
     */
    private static void processSelection(int selection) throws IOException  {
        switch (selection) {
            case 0:
                System.out.println("\r\nGoodbye!");
                System.exit(0);
                break;
            case 1:
                selectDirectory();
                break;
            case 2:
                listDirectory();
                break;
            case 3:
                // list all directories and files recursively
                printDashes();
                listDirectoryAll(directory, 0);
                printDashes();
                break;
            case 4:
                // delete option: prompt user for filename to delete
                // if file spec'd not there, error message displayed
                delete();
                break;
            case 5:
                // prompt user for filename
                // display file in hex -- display each byte of file in hex
                displayFileHex();
                break;
            case 6:
                // encrypt XOR w pw
                // prompt user for pw
                // prompt user for filename
                // encrypt content of file using pw
                // xor the pw with file content byte after byte
                // repeat pw as needed since it's shorter than file content
                encrypt();
                break;
            case 7:
                // decrypt file 
                // prompt for pw
                // prompt for file
                // decrypt content using that pw
                decrypt();
                break;
            default:
                throw new IOException(INVALID_OPTION_MSG);
        }
    }
    
    
    /**
     * Prompts user for the absolute path
     * for and sets directory to input
     * Corresponds with option 1 in processSelection()
     */
    private static void selectDirectory() throws FileNotFoundException {
        System.out.println(DIRECTORY_PROMPT);
        Scanner scannerIn = new Scanner(System.in);
        String fileName = scannerIn.nextLine();
        directory = new File(fileName);
        if (!directory.exists()) {
            throw new FileNotFoundException(FILE_NOT_FOUND_MSG);
        }
    }
    
    
    /**
     * List first level content of directory
     * Lists files and sub-directories separately
     * Corresponds with optino 2 in processSelection()
     * @throws FileNotFoundException
     */
    private static void listDirectory()  {
        String prepend = getPrepend(1);
        
        File[] files = directory.listFiles();
        ArrayList<File> dirList = new ArrayList<>();
        
        System.out.println();
        printDashes();
        System.out.println(directory.getName() + ":");
        
        for(File file : files) {
            if (file.isFile()) {
                System.out.println(prepend + file.getName());
            } else {
                dirList.add(file);
            }
        }
        
        for (File dir : dirList) {
            System.out.println(prepend + dir.getName());
        }
        printDashes();
        System.out.println();
    }
    
    
    /**
     * Prints each file and directory for each
     * directory inside of given directory
     * Corresponds with option 3 in processSelection()
     * @param dirIn - starting directory
     * @param level - determines how many tabs to prepend
     * @throws IOException 
     */
    private static void listDirectoryAll(File dirIn, int level) throws IOException {
        String prepend = getPrepend(level);
        
        File[] files = dirIn.listFiles();
        ArrayList<File> dirList = new ArrayList<>();
        System.out.println(prepend + dirIn.getName() + ":");
        
        for(File file : files) {
            if (file.isFile()) {
                System.out.println(prepend + "\t" + file.getName());
            } else {
                dirList.add(file);
            }
        }
        
        for(File dir : dirList) {
            listDirectoryAll(dir, ++level);
            --level;
        }
    }
    
    
    /**
     * 
     * Corresponds with option 4 in processSelection()
     * @param file 
     */
    private static void delete() throws FileNotFoundException {
        File file = promptUserFile();
    }
    
    
    /**
     * Displays the offset and hex respresentation
     * of file in directory given by user 
     * Corresponds with option 5 in processSelection()
     */
    private static void displayFileHex() throws FileNotFoundException, IOException {
        File file = promptUserFile();
        FileInputStream filestream = new FileInputStream(file);
        int lineNum;
        int lineItem = 0;
        System.out.println("Offset   | Hexadecimal Code");
        int offset = 0;
        System.out.printf("%08X | ", offset);
        while ((lineNum = filestream.read()) != -1) {
            System.out.printf("%02X ", lineNum);
            lineItem++;
            if (lineItem >= 16) {
                // go to next line
                System.out.println();
                lineItem = 0;
                offset += 10;
                System.out.printf("%08X | ", offset); // this is not correct but we are printing it out for now to look nice
            }
        }
    }
    
    
    /**
     * 
     * Corresponds with option 6 in processSelection()
     */
    private static void encrypt() {
        String password = promptUserPassword();
    }
    
    
    /**
     * 
     * Corresponds with option 7 in processSelection()
     */
    private static void decrypt() {
        String password = promptUserPassword();
    }
    
    
    /**
     * Prompts user for file selection
     * Returns file selected
     * @return file - selected by user in current dir
     * @throws FileNotFoundException 
     */
    private static File promptUserFile() throws FileNotFoundException {
        System.out.println(FILE_PROMPT);
        Scanner scannerIn = new Scanner(System.in);
        String fileName = scannerIn.nextLine();
        File file = new File(directory.getAbsolutePath() + DS + fileName);
        if (!file.exists()) {
            throw new FileNotFoundException(FILE_NOT_FOUND_MSG);
        }
        return file;
    }
    
    
    private static String promptUserPassword() {
        System.out.println("Enter password for encryption: ");
        Scanner scannerIn = new Scanner(System.in);
        String pw = scannerIn.nextLine();      
        return pw;
    }
    
    
    /**
     * Returns String to prepend to file/dirs
     * for printing to screen
     * @param level - how many times to add prepend
     * @return prepend - String
     */
    private static String getPrepend(int level) {
        String prepend = "";
        for (int i = 0; i < level; i++) {
            prepend = prepend.concat("\t");
        }
        return prepend;
    }
    
    
    /**
     * Prints out dashes for formatting
     */
    private static void printDashes() {
        System.out.println("- - - - - - - - - - - - -");
    }
    
    
    /**
     * Main
     * @param args 
     */
    public static void main(String[] args) {
      
        promptUser();
        
    }
}
