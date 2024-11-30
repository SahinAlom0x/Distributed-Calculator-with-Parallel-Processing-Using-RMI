/*
 * Author: sahinalom0x
 */
import java.rmi.Remote;
import java.util.List;
import java.rmi.RemoteException;
import java.util.ArrayList;
// Author: sahinalom0x
public interface CalculatorRemote extends Remote {
    int[][] multiplyMatrices(int[][] matrixA, int[][] matrixB) throws RemoteException;
    int[] sortArray(int[] array) throws RemoteException;
    List<Integer> findPrimes(int start, int end) throws RemoteException;
}