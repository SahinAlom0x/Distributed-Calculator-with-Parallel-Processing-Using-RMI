import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class CalculatorClientGUI extends Application {
    
    private CalculatorRemote server;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Look up the RMI registry and get the remote server object
            server = (CalculatorRemote) Naming.lookup("rmi://localhost/CalculatorServer");

            // Set up the UI
            primaryStage.setTitle("RMI Calculator");

            // GridPane Layout
            GridPane grid = new GridPane();
            grid.setVgap(10);
            grid.setHgap(10);
            grid.setPadding(new Insets(20, 20, 20, 20));
            
            // Buttons
            Button matrixButton = new Button("Matrix Multiplication");
            Button sortButton = new Button("Sort Array");
            Button primeButton = new Button("Find Prime Numbers");

            // Text Fields and Labels
            TextArea resultArea = new TextArea();
            resultArea.setEditable(false);
            
            TextField matrixAField = new TextField();
            TextField matrixBField = new TextField();
            TextField arrayField = new TextField();
            TextField rangeStartField = new TextField();
            TextField rangeEndField = new TextField();

            // Adding components to grid
            grid.add(new Label("Matrix A (comma separated):"), 0, 0);
            grid.add(matrixAField, 1, 0);
            grid.add(new Label("Matrix B (comma separated):"), 0, 1);
            grid.add(matrixBField, 1, 1);

            grid.add(matrixButton, 0, 2);
            
            grid.add(new Label("Array (comma separated):"), 0, 3);
            grid.add(arrayField, 1, 3);
            grid.add(sortButton, 0, 4);
            
            grid.add(new Label("Prime Range Start:"), 0, 5);
            grid.add(rangeStartField, 1, 5);
            grid.add(new Label("Prime Range End:"), 0, 6);
            grid.add(rangeEndField, 1, 6);
            grid.add(primeButton, 0, 7);
            
            grid.add(resultArea, 0, 8, 2, 1);

            // Button Actions
            matrixButton.setOnAction(e -> handleMatrixMultiplication(matrixAField, matrixBField, resultArea));
            sortButton.setOnAction(e -> handleSortArray(arrayField, resultArea));
            primeButton.setOnAction(e -> handlePrimeFinder(rangeStartField, rangeEndField, resultArea));

            // Create scene and set stage
            Scene scene = new Scene(grid, 500, 400);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            showError("Error connecting to the server. Make sure the server is running.");
        }
    }

    // Matrix Multiplication handler
    private void handleMatrixMultiplication(TextField matrixAField, TextField matrixBField, TextArea resultArea) {
        try {
            String[] matrixAStrings = matrixAField.getText().split(",");
            String[] matrixBStrings = matrixBField.getText().split(",");
            
            // Convert string inputs to matrices
            int[][] matrixA = convertToMatrix(matrixAStrings);
            int[][] matrixB = convertToMatrix(matrixBStrings);
            
            // Check for valid matrix multiplication
            if (matrixA[0].length != matrixB.length) {
                resultArea.setText("Error: Matrix dimensions do not match for multiplication.");
                return;
            }

            // Call the RMI method for matrix multiplication
            int[][] result = server.multiplyMatrices(matrixA, matrixB);
            resultArea.setText(formatMatrix(result));
        } catch (Exception e) {
            showError("Error during Matrix Multiplication.");
        }
    }

    // Array Sorting handler
    private void handleSortArray(TextField arrayField, TextArea resultArea) {
        try {
            String[] arrayStrings = arrayField.getText().split(",");
            int[] array = new int[arrayStrings.length];
            
            for (int i = 0; i < arrayStrings.length; i++) {
                array[i] = Integer.parseInt(arrayStrings[i].trim());
            }
            
            int[] sortedArray = server.sortArray(array);
            resultArea.setText("Sorted Array: " + formatArray(sortedArray));
        } catch (Exception e) {
            showError("Error during Array Sorting.");
        }
    }

    // Prime Finder handler
    private void handlePrimeFinder(TextField rangeStartField, TextField rangeEndField, TextArea resultArea) {
        try {
            int start = Integer.parseInt(rangeStartField.getText());
            int end = Integer.parseInt(rangeEndField.getText());
            
            List<Integer> primes = server.findPrimes(start, end);
            resultArea.setText("Prime Numbers: " + primes.toString());
        } catch (Exception e) {
            showError("Error during Prime Number Search.");
        }
    }

    // Utility method for showing error messages
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    // Utility method to format matrices
    private String formatMatrix(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : matrix) {
            for (int num : row) {
                sb.append(num).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Utility method to format array
    private String formatArray(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int num : array) {
            sb.append(num).append(" ");
        }
        return sb.toString();
    }

    // Convert input string to matrix
    private int[][] convertToMatrix(String[] matrixStrings) {
        String[] dimensions = matrixStrings[0].split("x");
        int rows = Integer.parseInt(dimensions[0]);
        int cols = Integer.parseInt(dimensions[1]);
        
        int[][] matrix = new int[rows][cols];
        int index = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Integer.parseInt(matrixStrings[index++].trim());
            }
        }
        return matrix;
    }
}
