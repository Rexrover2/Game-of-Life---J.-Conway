import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class App extends Application {
    private Board board;
    private GridPane grid;
    private int n;
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
        AnimationTimer animator = new AnimationTimer(){
            @Override
            public void handle(long arg0) {
                board.stepProcess();
                board.updateGrid();
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