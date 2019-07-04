import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Board {
    private static final int
            GAPS = 2,
            RECT_SIZE = 20,
            PADDING_SPACE = 10;

    // Length and Height of the grid.
    private int n;
    private int[][] board;
    // rects allows for 2D-Array-esque iteration to be performed on GridPanes of only Rectangle objects.
    private Rectangle[][] rects;
    private GridPane grid;
    private EventHandler<MouseEvent> mouseEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            Node source = (Node)e.getSource() ;
            // Gets the column and row int in which the mouse is over.
            int colIndex = GridPane.getColumnIndex(source),
                    rowIndex = GridPane.getRowIndex(source);

            board[rowIndex][colIndex] = (board[rowIndex][colIndex] == 1)? 0 : 1;
            updateGrid(rowIndex, colIndex);
        }
    };

    /**
     * Parses file to read initial game state.
     * Creates and fills grid.
     *
     * @param fileName Name of File.
     */
    public Board(String fileName) {
        // Reading initial state from file.
        // Debugger function to test.
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String text;
            int count = 0;

            while ((text = br.readLine()) != null) {
                // Reads n (size) of the grid
                if (count == 0) {
                    n = Integer.parseInt(text);
                    board = new int[n][];
                    rects = new Rectangle[n][];
                } else {
                    // Reads states.
                    board[count-1] = new int[n];
                    for (int i = 0; i < text.length(); ++i) {
                        char c = text.charAt(i);
                        board[count - 1][i] = Character.getNumericValue(c);
                    }
                }
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        createGrid();
        fillGrid();
    }


    // Deals with formatting of the board.
    private void createGrid() {
        grid = new GridPane();
        grid.setPadding(new Insets(PADDING_SPACE, PADDING_SPACE, PADDING_SPACE, PADDING_SPACE));
        grid.setVgap(GAPS);
        grid.setHgap(GAPS);
        // Sets background to grey.
        grid.setStyle("-fx-background-color:#808080; -fx-opacity:1;");
    }

    // Called only once when the board is initiated.
    private void fillGrid(){
        for (int row = 0; row < n; row++) {
            rects[row] = new Rectangle[n];
            for (int col = 0; col < n; col++) {
                Rectangle r = formatRect(row, col);
                grid.add(r, col, row);
                rects[row][col] = r;
            }
        }
    }

    /** Called to handle mouse clicks, updating the visualised board.
     *  Only changes the one rectangle that was clicked on -> should be O(1).
     *
     * @param row Row of Grid.
     * @param col Column of Grid.
     */
    private void updateGrid(int row, int col){
        Rectangle r = formatRect(row, col);
        grid.getChildren().remove(rects[row][col]);
        rects[row][col] = r;
        grid.add(r, col, row);
    }

    private Rectangle formatRect(int row, int col){
        Rectangle r = new Rectangle(RECT_SIZE, RECT_SIZE);
        r.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent);
        if (board[row][col] == 0) {
            r.setFill(Color.BLACK);
        } else {
            r.setFill(Color.WHITE);
        }
        return r;
    }

    /**
     * Returns the board to be drawn.
     *
     * @return Board to be drawn.
     * */
    public Scene getGrid() {
        return new Scene(grid, n * RECT_SIZE + n * GAPS + 2*PADDING_SPACE,
                               n * RECT_SIZE + n * GAPS + 2*PADDING_SPACE);
    }
}