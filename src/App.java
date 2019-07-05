import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class App extends Application {
    private static final double SMALL_NUM = 1e-2;

    private Board board;
    private GridPane grid;
    private int n;
    private long lastNanoTime;
    private double
            thresholdSec = 2;
    public static final int
            GAPS = 2,
            RECT_SIZE = 20,
            PADDING_SPACE = 10;


    public void init(){
        board = new Board("Assets/state-2.txt");
        n = board.getSize();
        grid = board.getGrid();
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Game of Life");
        Group root = new Group();
        Scene scene = new Scene(root, n * RECT_SIZE + n * GAPS + 2*PADDING_SPACE,
                n * RECT_SIZE + n * GAPS + 2*PADDING_SPACE);
        lastNanoTime = System.nanoTime();
        AnimationTimer animator = new AnimationTimer(){
            @Override
            public void handle(long curNanoTime) {

                double timeElapsed = (curNanoTime - lastNanoTime)/1000000000.0;
                System.out.printf("diff = %f%n", timeElapsed);
                if (timeElapsed - thresholdSec > -SMALL_NUM && timeElapsed - thresholdSec < SMALL_NUM) {
                    board.stepProcess();
                    board.updateGrid();
                    lastNanoTime = System.nanoTime();
                }
            }
        };
        animator.start();
        root.getChildren().add(grid);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}