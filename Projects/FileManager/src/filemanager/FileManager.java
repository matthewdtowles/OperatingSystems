package filemanager;

import java.io.File;
import java.io.FileNotFoundException;
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
    
    static final String DIRECTORY_PROMPT = "Enter absolute path of directory: ";
    
    private static File directory;
    
    
    /**
     * Displays the menu options and prompts user
     */
    private static void displayMenu() {
        for (int i = 0; i < MENU.length; i++) {
            System.out.println(String.valueOf(i) + " - " + MENU[i]);
        }
        System.out.println("Select option:");
        getSelection();
    }
    
    
    private static void getSelection() {
        Scanner scannerIn = new Scanner(System.in);
        int selection = scannerIn.nextInt();
        processSelection(selection);
    }
    
    
    private static void processSelection(int selection) {
        switch (selection) {
            case 0:
                System.exit(0);
                break;
            case 1:
                selectDirectory();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            default:
                displayMenu();
                break;
        }
    }
    
    
    private static void selectDirectory() {
        System.out.println(DIRECTORY_PROMPT);
        Scanner scannerIn = new Scanner(System.in);
        String fileName = scannerIn.nextLine();
        directory = new File(fileName);
    }
    
    
    /**
     * Main
     * @param args 
     * @throws java.io.FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException {
        displayMenu();
        getSelection();
        
        Scanner scannerIn = new Scanner(System.in);
        String fileName = scannerIn.nextLine();
        directory = new File(fileName);
        
        File[] files = directory.listFiles();
        ArrayList<File> fileArray = new ArrayList<>();
        ArrayList<File> dirArray = new ArrayList<>();
       
        for (File file : files) {
            if (file.isFile()) {
                fileArray.add(file);
            }
            if (file.isDirectory()) {
                dirArray.add(file);
            }
        }
        
        System.out.println("\r\nFiles:");
        for (File file : fileArray) {
            System.out.println(file.getName());
        }
        
        System.out.println("\r\nDirectories:");
        for(File dir : dirArray) {
            System.out.println(dir.getName());
        }
    }
}
