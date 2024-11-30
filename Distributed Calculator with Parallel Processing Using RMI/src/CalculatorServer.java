import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.Arrays;

public class CalculatorServer extends UnicastRemoteObject implements CalculatorRemote {

    private ExecutorService executorService;

    public CalculatorServer() throws RemoteException {
        super();
        executorService = Executors.newFixedThreadPool(4);  // Thread pool to handle concurrent tasks
    }

    @Override
    public int[][] multiplyMatrices(int[][] matrixA, int[][] matrixB) throws RemoteException {
        int numRows = matrixA.length;
        int numCols = matrixB[0].length;
        int[][] result = new int[numRows][numCols];

        // Split the multiplication task into sub-tasks for each row of the result matrix
        List<Callable<Void>> tasks = new ArrayList<>();
        
        for (int i = 0; i < numRows; i++) {
            final int row = i;
            tasks.add(() -> {
                for (int j = 0; j < numCols; j++) {
                    for (int k = 0; k < matrixA[0].length; k++) {
                        result[row][j] += matrixA[row][k] * matrixB[k][j];
                    }
                }
                return null;
            });
        }

        try {
            executorService.invokeAll(tasks);  // Run all sub-tasks in parallel
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return result;
    }

    @Override
    public int[] sortArray(int[] array) throws RemoteException {
        // Parallel sorting (split the array into smaller chunks)
        Arrays.parallelSort(array);  // Java's built-in parallel sort
        return array;
    }

    @Override
    public List<Integer> findPrimes(int start, int end) throws RemoteException {
        List<Integer> primes = new ArrayList<>();
        List<Callable<Void>> tasks = new ArrayList<>();

        // Split the range into chunks for parallel prime checking
        int range = end - start;
        int chunkSize = range / 4;  // Split range into 4 chunks
        
        for (int i = 0; i < 4; i++) {
            final int chunkStart = start + i * chunkSize;
            final int chunkEnd = (i == 3) ? end : chunkStart + chunkSize;
            tasks.add(() -> {
                for (int num = chunkStart; num < chunkEnd; num++) {
                    if (isPrime(num)) {
                        synchronized (primes) {  // Synchronize access to shared resource
                            primes.add(num);
                        }
                    }
                }
                return null;
            });
        }

        try {
            executorService.invokeAll(tasks);  // Execute all tasks concurrently
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return primes;
    }

    private boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    // Main method to start the RMI server
    public static void main(String[] args) {
        try {
            // Start the RMI registry (if not already running)
            Registry registry = LocateRegistry.createRegistry(1099);

            // Create the CalculatorServer object
            CalculatorServer server = new CalculatorServer();

            // Bind the server to the RMI registry under the name "CalculatorServer"
            registry.rebind("CalculatorServer", server);
            System.out.println("CalculatorServer is ready.");
        } catch (Exception e) {
            System.out.println("CalculatorServer exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}