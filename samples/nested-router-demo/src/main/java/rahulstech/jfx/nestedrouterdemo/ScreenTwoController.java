package rahulstech.jfx.nestedrouterdemo;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

public class ScreenTwoController extends SimpleLifecycleAwareController {

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        Label message = new Label("This is Screen2");

        Button button0 = new Button("Back");
        button0.setOnAction(e->handleButton0Click());

        VBox root = new VBox(message,button0);
        root.setBackground(Utils.colorFillBackground(Utils.getRandomColor()));
        root.setSpacing(12);
        root.setPadding(new Insets(12,12,12,12));

        setRoot(root);
    }

    private void handleButton0Click() {
        getRouter().popBackStack();
    }
}
