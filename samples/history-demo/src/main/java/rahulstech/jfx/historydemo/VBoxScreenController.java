package rahulstech.jfx.historydemo;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

public class VBoxScreenController extends SimpleLifecycleAwareController {

    private Label label;

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        label = new Label();
        label.setWrapText(true);

        VBox root = new VBox(label);
        root.setSpacing(12);
        root.setPadding(new Insets(12,12,12,12));

        setRoot(root);
    }

    public Label getLabel() {
        return label;
    }

    @Override
    public VBox getRoot() {
        return (VBox) super.getRoot();
    }
}
