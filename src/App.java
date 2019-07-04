import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {
    private Board board;

    public void init(){
        board = new Board("Assets/state-1.txt");
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Game of Life");

        primaryStage.setScene(board.getGrid());
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}