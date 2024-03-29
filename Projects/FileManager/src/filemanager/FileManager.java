package filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * FileManager
 * 
 * @author matthew.towles
 * @date Sep 16, 2019
 */
public class FileManager {
    
    // individual menu options 0 - 7 in order
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
    
    // prompts and messages:
    static final String DIR_NOT_FOUND_MSG = "Directory does not exist. Please "
            + "select a valid directory.";
    
    static final String FILE_NOT_FOUND_MSG = "File does not exist. Please "
            + "select a valid file.";
    
    static final String DIR_NOT_GIVEN_MSG = "Please provide a valid directory "
            + "by choosing option 1 first.";
    
    static final String INVALID_OPTION_MSG = "Invalid menu option. Select 0 - 7";
    static final String DIRECTORY_PROMPT = "Enter absolute path of directory: ";
    static final String FILE_PROMPT = "Enter file name in current directory: ";
    
    /**
     * Directory Separator for FileManager.path
     * Set as \\ for Windows systems
     */
    static final String DS = "\\";
    
    /**
     * Max no. of bytes for user password
     */
    static final int PW_BYTE_LIMIT = 256;
    
    
    /**
     * User provided directory
     */
    private static File directory;
    
    /**
     * Path to the FileManager.directory
     */
    private static String path;
    
    
    /**
     * When called - this runs the application
     * Keeps running application until user 
     * selects 0 aka "Exit"
     */
    public static void promptUser() {
        try {
            displayMenu();
            int selection = getSelection();
            processSelection(selection);
        } catch (NullPointerException e) {
            System.out.println(DIR_NOT_GIVEN_MSG);
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
        if (selection > 1) {
            if (!directory.exists()) {
                throw new FileNotFoundException(DIR_NOT_FOUND_MSG);
            }
        }
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
                printDashes();
                listDirectoryAll(directory, 0);
                printDashes();
                break;
            case 4:
                delete();
                break;
            case 5:
                displayFileHex();
                break;
            case 6:
                encrypt();
                break;
            case 7:
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
            throw new FileNotFoundException(DIR_NOT_FOUND_MSG);
        }
        path = directory.getAbsolutePath() + DS;
    }
    
    
    /**
     * List first level content of directory
     * Lists files and sub-directories separately
     * Corresponds with option 2 in processSelection()
     */
    private static void listDirectory() {
        String prepend = getPrepend(1);
        
        File[] files = directory.listFiles();
        ArrayList<File> dirList = new ArrayList<>();
        
        System.out.println();
        printDashes();
        System.out.println(directory.getName() + "/");
        
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
     * @throws FileNotFoundException 
     */
    private static void listDirectoryAll(File dirIn, int level) throws FileNotFoundException {
        String prepend = getPrepend(level);
        
        File[] files = dirIn.listFiles();
        ArrayList<File> dirList = new ArrayList<>();
        System.out.println(prepend + dirIn.getName() + "/");
        
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
     * Deletes file specified by user
     * Prompts user for file to be deleted
     * inside of FileManager.directory
     * Corresponds with option 4 in processSelection()
     */
    private static void delete() throws FileNotFoundException {
        File file = promptUserFile();
        if (file.delete()) {
            System.out.println(file.getName() + " deleted.");
        }
    }
    
    
    /**
     * Displays the offset and hex respresentation
     * of file in directory given by user 
     * Corresponds with option 5 in processSelection()
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private static void displayFileHex() throws FileNotFoundException, IOException {
        File file = promptUserFile();
        FileInputStream filestream = new FileInputStream(file);
        int line;
        int lineItem = 0;
        System.out.println("Offset   | Hexadecimal Code");
        int offset = 0;
        System.out.printf("%08X | ", offset);
        while ((line = filestream.read()) != -1) {
            System.out.printf("%02X ", line);
            lineItem++;
            if (lineItem >= 16) {
                // go to next line
                System.out.println();
                lineItem = 0;
                offset += 16;
                System.out.printf("%08X | ", offset);
            }
        }
    }
    
    
    /**
     * Encrypts a file given by user
     * User gives file A to be encrypted
     * User gives password to encrypt
     * User gives file B which will be the
     * encrypted copy of file A
     * Uses XOR encryption
     * Corresponds with option 6 in processSelection()
     */
    private static void encrypt() throws FileNotFoundException, IOException {
        System.out.println("Enter file to encrypt.");
        File originalFile = promptUserFile();
        byte[] fileBytes = Files.readAllBytes(originalFile.toPath());
        byte[] password = promptUserPassword();
        System.out.println("Enter encrypted file destination.");
        File encryptedFile = promptUserFile();
        for(int i = 0; i < fileBytes.length; i++) {
            // repeat password bytes over file bytes
            int j = i % password.length;
            fileBytes[i] = (byte) (fileBytes[i] ^ password[j]);
        }
        try (FileOutputStream outstream = new FileOutputStream(encryptedFile)) {
            outstream.write(fileBytes);
        }
    }
    
    
    /**
     * Decrypts a file given by user
     * User gives file A to be decrypted
     * User gives password to decrypt
     * User gives file B which will be the
     * decrypted copy of file A
     * Uses XOR encryption
     * Corresponds with option 7 in processSelection()
     */
    private static void decrypt() throws FileNotFoundException, IOException {
        System.out.println("Enter encrypted file name to decrypt.");
        File originalFile = promptUserFile();
        byte[] fileBytes = Files.readAllBytes(originalFile.toPath());
        byte[] password = promptUserPassword();
        System.out.println("Enter decrypted file destination.");
        File encryptedFile = promptUserFile();
        for(int i = 0; i < fileBytes.length; i++) {
            // repeat password bytes over file bytes
            int j = i % password.length;
            fileBytes[i] = (byte) (fileBytes[i] ^ password[j]);
        }
        try (FileOutputStream outstream = new FileOutputStream(encryptedFile)) {
            outstream.write(fileBytes);
        }
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
        File file = new File(path + fileName);
        if (!file.exists()) {
            throw new FileNotFoundException(FILE_NOT_FOUND_MSG);
        }
        return file;
    }
    

    /**
     * Prompts user for password
     * Returns the password as a byte[]
     * Used for encrypt/decrypt methods
     * @return pwBytes - a byte[] of password
     */
    private static byte[] promptUserPassword() {
        System.out.println("Enter password for encryption: ");
        Scanner scannerIn = new Scanner(System.in);
        String pw = scannerIn.nextLine();
        byte[] pwBytes = pw.getBytes();
        if (pwBytes.length > PW_BYTE_LIMIT) {
            System.out.println("Password may not exceed " 
                    + PW_BYTE_LIMIT + " bytes.");
            return promptUserPassword();
        }
        return pwBytes;
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
