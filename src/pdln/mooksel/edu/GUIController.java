package pdln.mooksel.edu;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GUIController implements Initializable {

    private static GUIController instance = null;

    private byte[][] matrix;

    private GameOfLife game = null;

    private int speed = Config.MIN_SPEED;

    private Timeline timer = null;

    private boolean isRunning = false;

    @FXML
    private Slider speedSlider;

    @FXML
    private Label speedLabel;

    @FXML
    private Canvas display;

    @FXML
    private Button playButton;

    @FXML
    void onPlayButtonClock(ActionEvent event) {
        if (!isRunning) {
            timer = new Timeline(new KeyFrame(Duration.millis(1000 / speed), tEvent -> {
                game.nextGeneration(matrix);
                updateDisplay();
            }));
            timer.setCycleCount(Timeline.INDEFINITE);
            timer.play();
            isRunning = true;

            playButton.setText("Pause");
        } else {
            timer.pause();
            timer.stop();
            isRunning = false;

            playButton.setText("Play");
        }
    }

    public static GUIController getInstance() {
        return instance;
    }

    public GameOfLife getGame() {
        return game;
    }

    public void setGame(GameOfLife game) {
        this.game = game;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.game = GameOfLife.getInstance();
        matrix = new byte[Config.DISPLAY_HEIGHT][Config.DISPLAY_WIDTH];
        game.setController(this);
        initDisplay();

        display.setOnMouseClicked(event -> {
            int x = (int)event.getX() / (Config.PPP + Config.GRID_PPL);
            int y = (int)event.getY() / (Config.PPP + Config.GRID_PPL);
            game.updateMatrix(matrix, x, y);
            updateDisplay();
        });


        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                speed = (int) (Config.MIN_SPEED + speedSlider.getValue() * (Config.MAX_SPEED - Config.MIN_SPEED) / 100);
                speedLabel.setText(speed + "");
                timer.stop();
                timer = new Timeline(new KeyFrame(Duration.millis(1000 / speed), event -> {
                    game.nextGeneration(matrix);
                    updateDisplay();
                }));
                timer.setCycleCount(Timeline.INDEFINITE);

                if (isRunning) timer.play();
            }
        });

    }

    private void initDisplay() {

        int displayWidth = Config.DISPLAY_WIDTH * (Config.PPP + Config.GRID_PPL) + Config.GRID_PPL;
        int displayHeight = Config.DISPLAY_HEIGHT* (Config.PPP + Config.GRID_PPL) + Config.GRID_PPL;

        display.setWidth(displayWidth);
        display.setHeight(displayHeight);

        GraphicsContext gc = display.getGraphicsContext2D();
        gc.setFill(Config.BACKGROUND_COLOR);
        gc.fillRect(0, 0, display.getWidth(), display.getHeight());

        //draw vertical lines
        gc.setFill(Config.GRID_COLOR);
        int x = 0;
        while (x <= display.getWidth()) {
            gc.fillRect(x, 0, Config.GRID_PPL, display.getHeight());
            x += Config.PPP + 1;
        }

        //draw horizontal lines
        gc.setFill(Config.GRID_COLOR);
        int y = 0;
        while (y <= display.getHeight()) {
            gc.fillRect(0, y, display.getWidth(), Config.GRID_PPL);
            y += Config.PPP + 1;
        }

    }

    private void updateDisplay() {

        GraphicsContext gc = display.getGraphicsContext2D();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 1) {
                    gc.setFill(Config.POINT_COLOR);

                    int x = Config.GRID_PPL + j * (Config.PPP + Config.GRID_PPL);
                    int y = Config.GRID_PPL + i * (Config.PPP + Config.GRID_PPL);
                    gc.fillRect(x, y,Config.PPP , Config.PPP);

                } else {
                    gc.setFill(Config.BACKGROUND_COLOR);

                    int x = Config.GRID_PPL + j * (Config.PPP + Config.GRID_PPL);
                    int y = Config.GRID_PPL + i * (Config.PPP + Config.GRID_PPL);
                    gc.fillRect(x, y, Config.PPP, Config.PPP);
                }

            }
        }
    }

}
