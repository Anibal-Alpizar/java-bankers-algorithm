/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 * The ProcessManagementDAL class represents the Data Access Layer (DAL) for
 * managing process and resource matrices in a system that simulates resource
 * allocation for processes.
 *
 * It encapsulates the data and provides methods to set and retrieve the
 * matrices required for determining the system's state (safe or unsafe) using
 * the Banker's algorithm.
 *
 * This class includes: - `available`: The available resources vector. -
 * `maximum`: The maximum demand matrix of each process. - `allocation`: The
 * resources currently allocated to each process. - `need`: The matrix
 * representing the remaining resource need for each process.
 *
 * The class offers methods for setting and getting these matrices and
 * calculates the need matrix, which is derived from the maximum demand and
 * current allocation.
 *
 * @author anibal
 */
public class ProcessManagementDAL {

    // Number of processes in the system
    private int numberOfProcesses;

    // Number of resources available in the system
    private int numberOfResources;

    // Array representing the currently available resources
    private int[] available;

    // Matrix representing the maximum resources each process may request
    private int[][] maximum;

    // Matrix representing the resources currently allocated to each process
    private int[][] allocation;

    // Matrix representing the remaining resources each process needs
    private int[][] need;

    /**
     * Constructor to initialize the ProcessManagementDAL object with the number
     * of processes and resources. It also initializes the matrices based on
     * these values.
     *
     * @param numberOfProcesses the number of processes in the system
     * @param numberOfResources the number of resources in the system
     */
    public ProcessManagementDAL(int numberOfProcesses, int numberOfResources) {
        this.numberOfProcesses = numberOfProcesses;
        this.numberOfResources = numberOfResources;
        this.available = new int[numberOfResources];
        this.maximum = new int[numberOfProcesses][numberOfResources];
        this.allocation = new int[numberOfProcesses][numberOfResources];
        this.need = new int[numberOfProcesses][numberOfResources];
    }

    /**
     * Sets the available resources vector.
     *
     * @param available an array representing the currently available resources
     */
    public void setAvailableResources(int[] available) {
        this.available = available;
    }

    /**
     * Sets the maximum demand matrix for each process.
     *
     * @param maximum a 2D array representing the maximum resources each process
     * may request
     */
    public void setMaximumMatrix(int[][] maximum) {
        this.maximum = maximum;
    }

    /**
     * Sets the current allocation matrix for each process.
     *
     * @param allocation a 2D array representing the resources currently
     * allocated to each process
     */
    public void setAllocationMatrix(int[][] allocation) {
        this.allocation = allocation;
    }

    /**
     * Calculates the need matrix by subtracting the allocation matrix from the
     * maximum matrix. The need matrix represents the remaining resources each
     * process needs.
     */
    public void calculateNeedMatrix() {
        for (int i = 0; i < numberOfProcesses; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                need[i][j] = maximum[i][j] - allocation[i][j];
            }
        }
    }

    /**
     * Returns the currently available resources vector.
     *
     * @return an array representing the currently available resources
     */
    public int[] getAvailableResources() {
        return available;
    }

    /**
     * Returns the maximum demand matrix for each process.
     *
     * @return a 2D array representing the maximum resources each process may
     * request
     */
    public int[][] getMaximumMatrix() {
        return maximum;
    }

    /**
     * Returns the current allocation matrix for each process.
     *
     * @return a 2D array representing the resources currently allocated to each
     * process
     */
    public int[][] getAllocationMatrix() {
        return allocation;
    }

    /**
     * Returns the need matrix for each process.
     *
     * @return a 2D array representing the remaining resources each process
     * needs
     */
    public int[][] getNeedMatrix() {
        return need;
    }
}
