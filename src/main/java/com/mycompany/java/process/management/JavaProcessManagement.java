package com.mycompany.java.process.management;

import bll.ProcessManagementBLL;
import dal.ProcessManagementDAL;
import javax.swing.JOptionPane;

/**
 * The JavaProcessManagement class is the entry point of the application that
 * simulates the Banker's Algorithm for resource allocation in a multi-process
 * system. It provides a user interface to input data for the available
 * resources, maximum resource demands, and allocated resources for each
 * process, and then checks if the system is in a safe state after processing
 * resource requests.
 *
 * The application displays matrices of available resources, maximum resource
 * demands, allocated resources, and needed resources, and evaluates the safety
 * of the system's state based on resource requests.
 *
 * The main method guides the user through input prompts to initialize the
 * system's state and then iteratively allows the user to make resource requests
 * for specific processes. It notifies the user if the requests can be granted
 * without compromising the system's safety.
 *
 * This class depends on the ProcessManagementBLL for business logic and
 * ProcessManagementDAL for data access.
 */
public class JavaProcessManagement {

    /**
     * The main method of the application. Initializes the system's state and
     * manages user interaction for resource allocation requests.
     *
     * @param args the command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // Prompt the user to enter the number of processes
        String processInput = JOptionPane.showInputDialog("Enter the number of processes:");
        int numberOfProcesses = Integer.parseInt(processInput.trim());

        // Prompt the user to enter the number of resources
        String resourceInput = JOptionPane.showInputDialog("Enter the number of resources:");
        int numberOfResources = Integer.parseInt(resourceInput.trim());

        // Initialize DAL and BLL layers
        ProcessManagementDAL dal = new ProcessManagementDAL(numberOfProcesses, numberOfResources);
        ProcessManagementBLL bll = new ProcessManagementBLL(dal);

        // Prompt the user to enter available resources and parse the input
        String availableInput = JOptionPane.showInputDialog("Enter the Available Resources (separated by spaces):");
        String[] availableStrings = availableInput.trim().split(" ");
        int[] available = new int[numberOfResources];
        for (int i = 0; i < numberOfResources; i++) {
            available[i] = Integer.parseInt(availableStrings[i].trim());
        }
        dal.setAvailableResources(available);

        // Prompt the user to enter the Maximum Matrix and parse the input
        String maximumInput = JOptionPane.showInputDialog("Enter the Maximum Matrix (each row separated by '|', each number separated by space):");
        String[] maximumRows = maximumInput.trim().split("\\|");
        int[][] maximum = new int[numberOfProcesses][numberOfResources];
        for (int i = 0; i < numberOfProcesses; i++) {
            String[] maximumValues = maximumRows[i].trim().split(" ");
            for (int j = 0; j < numberOfResources; j++) {
                maximum[i][j] = Integer.parseInt(maximumValues[j].trim());
            }
        }
        dal.setMaximumMatrix(maximum);

        // Prompt the user to enter the Allocation Matrix and parse the input
        String allocationInput = JOptionPane.showInputDialog("Enter the Allocation Matrix (each row separated by '|', each number separated by space):");
        String[] allocationRows = allocationInput.trim().split("\\|");
        int[][] allocation = new int[numberOfProcesses][numberOfResources];
        for (int i = 0; i < numberOfProcesses; i++) {
            String[] allocationValues = allocationRows[i].trim().split(" ");
            for (int j = 0; j < numberOfResources; j++) {
                allocation[i][j] = Integer.parseInt(allocationValues[j].trim());
            }
        }
        dal.setAllocationMatrix(allocation);

        // Calculate the Need Matrix based on Maximum and Allocation matrices
        dal.calculateNeedMatrix();

        // Display the matrices to the user
        displayMatrices(dal);

        // Check if the initial state is safe and inform the user
        if (bll.isSafeState()) {
            JOptionPane.showMessageDialog(null, "Initial State is Safe");
        } else {
            JOptionPane.showMessageDialog(null, "Initial State is Unsafe");
        }

        // Loop to allow the user to make resource requests for specific processes
        while (true) {
            String processNumberInput = JOptionPane.showInputDialog("Enter the process number to request resources (or -1 to exit):");
            int processNumber = Integer.parseInt(processNumberInput.trim());
            if (processNumber == -1) {
                break;
            }

            String requestInput = JOptionPane.showInputDialog("Enter the resource request (separated by spaces):");
            String[] requestStrings = requestInput.trim().split(" ");
            int[] request = new int[numberOfResources];
            for (int i = 0; i < numberOfResources; i++) {
                request[i] = Integer.parseInt(requestStrings[i].trim());
            }

            // Check if the resource request can be granted without putting the system in an unsafe state
            boolean granted = bll.requestResources(processNumber, request);
            if (granted) {
                JOptionPane.showMessageDialog(null, "Request granted. System is in a safe state.");
            } else {
                JOptionPane.showMessageDialog(null, "Request denied. System would be in an unsafe state.");
            }
        }
    }

    /**
     * Displays the current state of Available, Maximum, Allocation, and Need
     * matrices.
     *
     * @param dal The data access layer containing the matrices to display.
     */
    private static void displayMatrices(ProcessManagementDAL dal) {
        StringBuilder sb = new StringBuilder();

        // Display the Available Resources
        sb.append("Available Resources:\n");
        for (int i = 0; i < dal.getAvailableResources().length; i++) {
            sb.append(dal.getAvailableResources()[i]).append(" ");
        }

        sb.append("\n\nMaximum Matrix:\n");
        // Display the Maximum Matrix
        for (int i = 0; i < dal.getMaximumMatrix().length; i++) {
            for (int j = 0; j < dal.getMaximumMatrix()[i].length; j++) {
                sb.append(dal.getMaximumMatrix()[i][j]).append(" ");
            }
            sb.append("\n");
        }

        // Display the Allocation Matrix
        sb.append("\nAllocation Matrix:\n");
        for (int i = 0; i < dal.getAllocationMatrix().length; i++) {
            for (int j = 0; j < dal.getAllocationMatrix()[i].length; j++) {
                sb.append(dal.getAllocationMatrix()[i][j]).append(" ");
            }
            sb.append("\n");
        }

        // Display the Need Matrix
        sb.append("\nNeed Matrix:\n");
        for (int i = 0; i < dal.getNeedMatrix().length; i++) {
            for (int j = 0; j < dal.getNeedMatrix()[i].length; j++) {
                sb.append(dal.getNeedMatrix()[i][j]).append(" ");
            }
            sb.append("\n");
        }

        // Show the matrices in a dialog box
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}
