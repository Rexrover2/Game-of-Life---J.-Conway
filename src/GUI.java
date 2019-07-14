import javafx.scene.control.Button;

public class GUI {
    // Start Stop Button variables
    private Button
            startStop,
            save;


    public Button createStartStopButton(int layoutX, int layoutY) {
        startStop = new Button("Start");
        startStop.setLayoutX(layoutX);
        startStop.setLayoutY(layoutY);
        return startStop;
    }
}
