/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bll;

import dal.ProcessManagementDAL;

/**
 * The ProcessManagementBLL class serves as the Business Logic Layer (BLL) for
 * managing process and resource allocation in a system, using the Banker's
 * algorithm to ensure that the system remains in a safe state.
 *
 * This class interacts with the Data Access Layer (DAL) encapsulated in the
 * ProcessManagementDAL class, and provides methods to: - Determine if the
 * system is in a safe state. - Handle resource requests from processes while
 * ensuring that the system does not enter an unsafe state.
 *
 * @author anibal
 */
public class ProcessManagementBLL {

    // The Data Access Layer object for managing matrices and resources
    private ProcessManagementDAL dal;

    /**
     * Constructor that initializes the ProcessManagementBLL with a given
     * ProcessManagementDAL object.
     *
     * @param dal the Data Access Layer object managing the process and resource
     * matrices
     */
    public ProcessManagementBLL(ProcessManagementDAL dal) {
        this.dal = dal;
    }

    /**
     * Determines if the system is in a safe state by simulating the Banker's
     * algorithm.
     *
     * The algorithm checks if there is at least one sequence of processes that
     * can finish without leading to a deadlock.
     *
     * @return true if the system is in a safe state, false otherwise
     */
    public boolean isSafeState() {
        int[] work = dal.getAvailableResources().clone();
        boolean[] finish = new boolean[dal.getMaximumMatrix().length];
        int count = 0;

        while (count < finish.length) {
            boolean foundProcess = false;

            for (int i = 0; i < finish.length; i++) {
                if (!finish[i]) {
                    boolean canProceed = true;

                    for (int j = 0; j < work.length; j++) {
                        if (dal.getNeedMatrix()[i][j] > work[j]) {
                            canProceed = false;
                            break;
                        }
                    }

                    if (canProceed) {
                        for (int k = 0; k < work.length; k++) {
                            work[k] += dal.getAllocationMatrix()[i][k];
                        }
                        finish[i] = true;
                        count++;
                        foundProcess = true;
                        break;
                    }
                }
            }

            if (!foundProcess) {
                return false;
            }
        }
        return true;
    }

    /**
     * Handles a resource request from a process, ensuring that the request does
     * not leave the system in an unsafe state.
     *
     * The method temporarily allocates resources to the process, checks if the
     * system remains in a safe state, and then either confirms the allocation
     * or rolls it back.
     *
     * @param processNumber the index of the process making the request
     * @param request an array representing the resources requested by the
     * process
     * @return true if the request is granted and the system remains in a safe
     * state, false otherwise
     */
    public boolean requestResources(int processNumber, int[] request) {
        int[] available = dal.getAvailableResources();
        int[][] allocation = dal.getAllocationMatrix();
        int[][] need = dal.getNeedMatrix();

        for (int i = 0; i < request.length; i++) {
            if (request[i] > need[processNumber][i] || request[i] > available[i]) {
                return false;
            }
        }

        for (int i = 0; i < request.length; i++) {
            available[i] -= request[i];
            allocation[processNumber][i] += request[i];
            need[processNumber][i] -= request[i];
        }

        dal.setAvailableResources(available);
        dal.setAllocationMatrix(allocation);
        dal.calculateNeedMatrix();

        boolean safe = isSafeState();

        if (!safe) {
            for (int i = 0; i < request.length; i++) {
                available[i] += request[i];
                allocation[processNumber][i] -= request[i];
                need[processNumber][i] += request[i];
            }

            dal.setAvailableResources(available);
            dal.setAllocationMatrix(allocation);
            dal.calculateNeedMatrix();

            return false;
        }

        return true;
    }
}
