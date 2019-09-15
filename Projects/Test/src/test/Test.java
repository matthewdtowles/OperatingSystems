package test;

import java.io.*;
import java.util.StringTokenizer;
import java.util.Scanner;

/**
 * Test
 *
 * @author matthew.towles
 * @date Sep 14, 2019
 */
public class Test {

    public static void main(String[] args) throws IOException, FileNotFoundException {
        int NUMBER_OF_PROCESSES = 7;
        int numberOfResources = 5;
        int lineCount = 0;
        int[] sumColumn;
        int [] sumRow;
        int[] resourceVector;
        int[] availableVector;
        int[] current_available;
        int[] process_order;
        int index = 0;
        boolean finish[];
        int[][] max_resource_matrix;
        int[][] allocationMatrix;
        int[][] needMatrix;
        boolean isSafe = false;
        // Variables to read in line from a file and tokenize
        String line;
        String filename;
        StringTokenizer lines;
        
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the name of the input file : ");
        filename = input.nextLine();
        // build input stream
        FileReader fr = new FileReader(filename);
        // Use Buffered reader to read one line at a time
        BufferedReader br = new BufferedReader(fr);
        
        //Create 2D array for each of the m process and n resources
        max_resource_matrix = new int[NUMBER_OF_PROCESSES][numberOfResources];
        allocationMatrix = new int[NUMBER_OF_PROCESSES][numberOfResources];
        needMatrix = new int[NUMBER_OF_PROCESSES][numberOfResources];
        //Create resourceVector
        resourceVector = new int[numberOfResources];
        //Create availableVector
        availableVector = new int[numberOfResources];
        current_available = new int[numberOfResources];
        finish = new boolean[NUMBER_OF_PROCESSES];
        process_order = new int[NUMBER_OF_PROCESSES];
        sumColumn = new int[numberOfResources];
        sumRow = new int[NUMBER_OF_PROCESSES];
        line = br.readLine();
        // Read the file and get the claimMatrix from the file
        while (line != null && lineCount < NUMBER_OF_PROCESSES) {
            lines = new StringTokenizer(line);
            if (lines.hasMoreTokens()) {
                for (int j = 0; j < numberOfResources; j++) {
                    max_resource_matrix[lineCount][j] = Integer.parseInt(lines.nextToken());
                }
            }
            line = br.readLine();
            lineCount++;
        }
        lineCount = 0;
        // Read the file and get the Allocation Matrix         
        while (line != null && lineCount < NUMBER_OF_PROCESSES) {
            lines = new StringTokenizer(line);
            if (lines.hasMoreTokens()) {
                for (int j = 0; j < numberOfResources; j++) {
                    allocationMatrix[lineCount][j]
                            = Integer.parseInt(lines.nextToken());
                }
            }
            line = br.readLine();
            lineCount++;
        }
        // Read the last line and set Resource Vector
        lines = new StringTokenizer(line);
        while (lines.hasMoreTokens()) {
            for (int i = 0; i < numberOfResources; i++) {
                resourceVector[i] = Integer.parseInt(lines.nextToken());
            }
        }
        // Close the bufferreader and file
        br.close();
        fr.close();
        // Determine the initial need matrix
        for (int i = 0; i < NUMBER_OF_PROCESSES; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                needMatrix[i][j] = max_resource_matrix[i][j] - allocationMatrix[i][j];
            }
        }
        // Determine the initial available vector
        for (int i = 0; i < NUMBER_OF_PROCESSES; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                sumColumn[j] += allocationMatrix[i][j];
                sumRow[i] += needMatrix[i][j];
            }
        }
        for (int j = 0; j < numberOfResources; j++) {
            availableVector[j] = resourceVector[j] - sumColumn[j];
        }
        // Initialize Work and Finish
        System.arraycopy(availableVector, 0, current_available, 0, numberOfResources);
        for (int i = 0; i < NUMBER_OF_PROCESSES; i++) {
            finish[i] = false;
        }
        // Safety Algorithm (checks if the system is in a safe or unsafe state)
        boolean unsafe = false;
        do {
            // Process found flag
            unsafe = false;
            int i = 0;
            for (; i < NUMBER_OF_PROCESSES; i++) {
                if ((!finish[i])) {
                    boolean good = true;
                    for (int j = 0; j < numberOfResources; j++) {
                        // If the need is greater than the available,
                        //then the process is not able to run to completion
                        // So it is not good  
                        if (needMatrix[i][j] > current_available[j]) {
                            good = false;
                            break;
                        }
                    }
                    // Try another process
                    if (!good) {
                        continue;
                    }
                    unsafe = true;
                    break;
                }
            }
            // Process is found that can run to completion, simulate execution
            if (unsafe) {
                finish[i] = true;
                for (int j = 0; j < numberOfResources; j++) {
                    current_available[j] += allocationMatrix[i][j];
                }
                process_order[index++] = i;
            }
        } while (unsafe);
        //check whether all process are finished or not
        for (int i = 0; i < NUMBER_OF_PROCESSES; i++) {
            isSafe = finish[i];
        }
        // Display output
        System.out.println("Number of Processes : " + NUMBER_OF_PROCESSES);
        System.out.println("Number of Resources : " + numberOfResources + "\n");
        System.out.println("Max_resource_Matrix : ");
        for (int i = 0; i < NUMBER_OF_PROCESSES; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                System.out.print(max_resource_matrix[i][j] + " ");
            }
            System.out.println();
        }
        //Display allocation matrix
        System.out.println("\nAllocation Matrix : ");
        for (int i = 0; i < NUMBER_OF_PROCESSES; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                System.out.print(allocationMatrix[i][j] + " ");
            }
            System.out.println();
        }
        //Display resource matrix
        System.out.println("\nResource Vector : ");
        for (int i = 0; i < numberOfResources; i++) {
            System.out.print(resourceVector[i] + " ");
        }
        System.out.println();
        //Display the Need matrix
        System.out.println("\nNeed Matrix : ");
        for (int i = 0; i < NUMBER_OF_PROCESSES; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                System.out.print(needMatrix[i][j] + " ");
            }
            System.out.println();
        }
        //Display the Available Vector
        System.out.println();
        System.out.println("Initial Available Vector : ");
        for (int j = 0; j < numberOfResources; j++) {
            System.out.print(availableVector[j] + " ");
        }
        System.out.println("\n");
        //Print the output
        if (isSafe) {
            System.out.print("Process order of sequence : ");
            for (int i = 0; i < process_order.length; i++) {
                System.out.print((process_order[i] + 1) + " ");
            }
            System.out.println();
            System.out.println("This system is in a safe state!!!");
        } else {
            System.out.println("This system is not in a safe state!!!");
        }
    }
}