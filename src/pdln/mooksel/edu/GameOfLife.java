package pdln.mooksel.edu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class GameOfLife extends Application {

    private static GameOfLife instance;

    private GUIController controller = null;

    @Override
    public void start(Stage primaryStage) throws Exception {

        GameOfLife.instance = this;

        Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public GUIController getController() {
        return controller;
    }

    public void setController(GUIController controller) {
        this.controller = controller;
    }


    public static GameOfLife getInstance() {
        return instance;
    }

    public void updateMatrix(byte[][] matrix, int x, int y) {
        if (matrix != null && matrix.length > 0) {
            matrix[y][x] = (byte) (1 - matrix[y][x]);
        }
    }

    public void nextGeneration(byte[][] matrix) {
        if (matrix != null && matrix.length > 0) {
            byte[][] helpMatrix = new byte[matrix.length][matrix[0].length];

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if (matrix[i][j] == 0) {
                        if (checkForBirth(matrix, i, j)) {
                            helpMatrix[i][j] = 1;
                        } else helpMatrix[i][j] = 0;
                    } else {
                        if (checkForDie(matrix, i, j)) {
                            helpMatrix[i][j] = 0;
                        } else {
                            helpMatrix[i][j] = 1;
                        }
                    }
                }
            }
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    matrix[i][j] = helpMatrix[i][j];
                }
            }
        }
    }


    private boolean checkForBirth(byte[][] matrix, int i, int j) {
        int neighbors = calcNeighbors(matrix, i, j);
        if (neighbors == 3) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkForDie(byte[][] matrix, int i, int j) {
        int neighbors = calcNeighbors(matrix, i, j);
        if (neighbors < 2 || neighbors > 3) {
            return true;
        } else {
            return false;
        }
    }

    private int calcNeighbors(byte[][] matrix, int i, int j) {
        int neighbors = 0;
        if (i > 0 && matrix[i - 1][j] == 1) neighbors++;
        if (j > 0 && matrix[i][j - 1] == 1) neighbors++;
        if (i > 0 && j > 0 && matrix[i - 1][j - 1] == 1) neighbors++;

        if (i < matrix.length - 1 && matrix[i + 1][j] == 1) neighbors++;
        if (j < matrix[0].length - 1 && matrix[i][j + 1] == 1) neighbors++;
        if (i < matrix.length - 1 && j < matrix[0].length - 1 && matrix[i + 1][j + 1] == 1) neighbors++;

        if (i > 0 && j < matrix[0].length - 1 && matrix[i - 1][j + 1] == 1) neighbors++;
        if (i < matrix.length - 1 && j > 0 && matrix[i + 1][j - 1] == 1) neighbors++;

        return neighbors;
    }

    public void clearMAtrix(byte[][] matrix) {
        if (matrix != null && matrix.length > 0) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

}
