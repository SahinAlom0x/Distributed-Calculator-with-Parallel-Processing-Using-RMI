/*
 * Author: sahinalom0x
 */

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * CalculatorClient class for interacting with the CalculatorRemote server.
 * Developed by sahinalom0x.
 */
public class CalculatorClient {
    public static void main(String[] args) {
        try {
            // Lookup the remote server
            CalculatorRemote server = (CalculatorRemote) Naming.lookup("rmi://localhost/CalculatorServer");

            Scanner scanner = new Scanner(System.in);
            int choice;

            while (true) {
                // Display menu to the user
                System.out.println("// Author: sahinalom0x");
                System.out.println("\nSelect an operation:");
                System.out.println("1. Matrix Multiplication");
                System.out.println("2. Sort Array");
                System.out.println("3. Prime Number Finder");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                
                // Author: sahinalom0x
                switch (choice) {
                    case 1:  // Matrix Multiplication
                        System.out.println("Matrix Multiplication selected by user. (Author: sahinalom0x)");
                        System.out.println("Enter dimensions of the first matrix:");
                        int m1 = scanner.nextInt();
                        int n1 = scanner.nextInt();
                        int[][] matrixA = new int[m1][n1];
                        System.out.println("Enter values for the first matrix:");
                        for (int i = 0; i < m1; i++) {
                            for (int j = 0; j < n1; j++) {
                                matrixA[i][j] = scanner.nextInt();
                            }
                        }
                        System.out.println("Enter dimensions of the second matrix:");
                        int m2 = scanner.nextInt();
                        int n2 = scanner.nextInt();
                        int[][] matrixB = new int[m2][n2];
                        System.out.println("Enter values for the second matrix:");
                        for (int i = 0; i < m2; i++) {
                            for (int j = 0; j < n2; j++) {
                                matrixB[i][j] = scanner.nextInt();
                            }
                        }

                        if (n1 != m2) {
                            System.out.println("Matrix multiplication is not possible. Incompatible dimensions.");
                        } else {
                            int[][] result = server.multiplyMatrices(matrixA, matrixB);
                            System.out.println("Matrix multiplication result:");
                            for (int i = 0; i < result.length; i++) {
                                for (int j = 0; j < result[i].length; j++) {
                                    System.out.print(result[i][j] + " ");
                                }
                                System.out.println();
                            }
                        }
                        break;
// Author: sahinalom0x
                    case 2:  // Sorting Array
                        System.out.println("Array Sorting initiated. (Author: sahinalom0x)");
                        System.out.print("Enter the size of the array: ");
                        int size = scanner.nextInt();
                        int[] array = new int[size];
                        System.out.println("Enter the elements of the array:");
                        for (int i = 0; i < size; i++) {
                            array[i] = scanner.nextInt();
                        }

                        array = server.sortArray(array);
                        System.out.println("Sorted array:");
                        for (int i : array) {
                            System.out.print(i + " ");
                        }
                        System.out.println();
                        break;

                    case 3:  // Prime Number Finder
                        System.out.println("Prime Number Finder accessed. (Author: sahinalom0x)");
                        System.out.print("Enter the range (start end): ");
                        int start = scanner.nextInt();
                        int end = scanner.nextInt();

                        List<Integer> primes = server.findPrimes(start, end);
                        System.out.println("Prime numbers in the range [" + start + ", " + end + "]:");
                        for (int prime : primes) {
                            System.out.print(prime + " ");
                        }
                        System.out.println();
                        break;

                    case 4:  // Exit
                        System.out.println("Exiting... (Author: sahinalom0x)");
                        scanner.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("CalculatorClient exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
