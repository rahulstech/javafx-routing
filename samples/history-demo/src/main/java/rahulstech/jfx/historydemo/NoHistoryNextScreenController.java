package rahulstech.jfx.historydemo;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class NoHistoryNextScreenController extends VBoxScreenController {

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        VBox root = getRoot();

        root.setBackground(Utils.colorFillBackground(Color.LIGHTSTEELBLUE));

        getLabel().setText("You are navigated from a no history screen. " +
                "Click the back button then you will see the screen with no history is skipped and " +
                "you will be navigated to previous screen of the no histroy screen i.e. 'dashboard'");

        Button button = new Button("Back");
        button.setOnAction(e->handleButtonClick());
        root.getChildren().add(button);
    }

    private void handleButtonClick() {
        getRouter().popBackStack();
    }
}
