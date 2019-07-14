import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class App extends Application {
    /**
     * These Constants allow organised formatting for the location of the
     * board and various buttons on the board.
     * */
    public static final int
            GAPS = 2,
            RECT_SIZE = 20,
            PADDING_SPACE = 10,
            BOTTOM_PADDING_SPACE = 35;
    private static final double SMALL_NUM = 1e-1;
    private Board board;
    private GridPane grid;
    private int n;
    private long lastNanoTime;
    private double thresholdSec = 0.5;
    private GUI gui = new GUI();
    private Button startStop;
    private String startStopText = "Stop";
    private boolean stop = false;

    public void init(){
        // Grid and Board creation.
        board = new Board("Assets/blank-state.txt");
        n = board.getSize();
        grid = board.getGrid();

        // Start-Stop Button
        startStop = gui.createStartStopButton(PADDING_SPACE/2,
                n * RECT_SIZE + n * GAPS + PADDING_SPACE + 2);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                stop = (stop) ? false : true;
                startStopText = (stop) ? "Stop" : "Start";
                startStop.setText(startStopText);
            }
        };
        // when button is pressed
        startStop.setOnAction(event);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Game of Life");
        Group root = new Group();
        int windowWidth = n * RECT_SIZE + n * GAPS + 2*PADDING_SPACE,
                windowHeight = n * RECT_SIZE + n * GAPS + PADDING_SPACE + BOTTOM_PADDING_SPACE + 2;
        Scene scene = new Scene(root, windowWidth, windowHeight, Color.GRAY);
        lastNanoTime = System.nanoTime();
        AnimationTimer animator = new AnimationTimer(){
            @Override
            public void handle(long curNanoTime) {
                double timeElapsed = (curNanoTime - lastNanoTime)/1000000000.0;
                if (timeElapsed - thresholdSec > -SMALL_NUM && timeElapsed - thresholdSec < SMALL_NUM) {
                    System.out.println("stop: " + stop);
                    if (stop) {
                        board.stepProcess();
                        board.updateGrid();
                    }

                    // Resets to current time.
                    lastNanoTime = System.nanoTime();
                }
            }
        };
        animator.start();
        root.getChildren().add(grid);
        root.getChildren().add(startStop);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}