package rahulstech.jfx.nestedrouterdemo;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

public class ScreenZeroController extends SimpleLifecycleAwareController {

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        Label message = new Label("This is Screen0");

        Button button0 = new Button("Go To Screen With Own Router");
        button0.setOnAction(e->handleButton0Click());

        VBox root = new VBox(message,button0);
        root.setBackground(Background.fill(Utils.getRandomColor()));
        root.setSpacing(12);
        root.setPadding(new Insets(12,12,12,12));

        setRoot(root);
    }

    private void handleButton0Click() {
        getRouter().moveto("screen1");
    }

}
