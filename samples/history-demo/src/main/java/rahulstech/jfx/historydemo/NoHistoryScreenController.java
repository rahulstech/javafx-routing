package rahulstech.jfx.historydemo;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class NoHistoryScreenController extends VBoxScreenController {

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        VBox root = getRoot();

        root.setBackground(Utils.colorFillBackground(Color.LIGHTCYAN));

        getLabel().setText("This screen will not be in backstack as 'removeHistory' is set to 'true' for this destination. " +
                "To test this click on 'GoTo Next Screen' and then back," +
                " you will be navigated to the screen that opened this screen i.e. 'dashboard'.");

        Button button = new Button("GoTo Next Screen");
        button.setOnAction(e->handleButtonClick());
        root.getChildren().add(button);
    }

    protected void handleButtonClick() {
        getRouter().moveto("no_history_next_screen");
    }
}
