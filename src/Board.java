import com.sun.javafx.css.Rule;
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
            updateRect(rowIndex, colIndex);
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
            if (count == 1) {
                fillWithZeroes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        createGrid();
        fillGrid();
    }

    public void stepProcess(){
        // Create a new board and always read states from the previous board.
        int[][] curBoard = new int[n][];

        RuleSet rules = new RuleSet();
        for (int row = 0; row < n; ++row) {
            curBoard[row] = new int[n];
            for (int col = 0; col < n ; ++col) {

                int count = 0,
                    rStart = chooseIncrementerStart(row),
                    rEnd = chooseIncrementerEnd(row),
                    cStart = chooseIncrementerStart(col),
                    cEnd = chooseIncrementerEnd(col);

                for (int rowNbour = rStart; rowNbour <= rEnd; ++rowNbour) {
                    for (int colNbour = cStart; colNbour <= cEnd; ++colNbour) {
                        if (board[row + rowNbour][col + colNbour] == 1) {
                            ++count;
                        }
                    }
                }
                // Decrement as the rectangle counts itself if it is alive.
                if (board[row][col] == 1) {
                    --count;
                }

                curBoard[row][col] = rules.ruleSet(count, board[row][col]);
            }
        }
        // replace old board with new board.
        board = curBoard;
    }

    /**
     * For Debugging Purposes - Prints the entire board
     * */
    public void printBoard(){
        for (int row = 0; row < n; ++row) {
            for (int col = 0; col < n; ++col) {
                System.out.printf("%d ", board[row][col]);
            }
            System.out.printf("%n");
        }
        System.out.printf("%n");
    }

    private int chooseIncrementerStart(int num) {
        if (num == 0) { return 0; }
        else { return -1; }
    }

    private int chooseIncrementerEnd(int num) {
        if (num == n - 1) { return 0; }
        else { return 1; }
    }
    // Deals with formatting of the board.
    private void createGrid() {
        grid = new GridPane();
        grid.setPadding(new Insets(App.PADDING_SPACE, App.PADDING_SPACE, App.BOTTOM_PADDING_SPACE, App.PADDING_SPACE));
        grid.setVgap(App.GAPS);
        grid.setHgap(App.GAPS);
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

    private void fillWithZeroes() {
        for (int row = 0; row < n; ++row) {
            board[row] = new int[n];
            for (int col = 0; col < n; ++col) {
                board[row][col] = 0;
            }
        }
    }

    /** Updates the entire board.
     *
     */
    public void updateGrid(){
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                updateRect(row, col);
            }
        }
    }

    /** Called to handle mouse clicks, a single element on the board.
     *  Only changes the one rectangle that was clicked on -> should be O(1).
     *
     * @param row Row of Grid.
     * @param col Column of Grid.
     */
    private void updateRect(int row, int col){
        Rectangle r = formatRect(row, col);
        grid.getChildren().remove(rects[row][col]);
        rects[row][col] = r;
        grid.add(r, col, row);
    }

    private Rectangle formatRect(int row, int col){
        Rectangle r = new Rectangle(App.RECT_SIZE, App.RECT_SIZE);
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
    public GridPane getGrid() { return grid; }

    /**
     * Returns the size of the board.
     *
     * @return Size of Board.
     * */
    public int getSize() { return n; }
}