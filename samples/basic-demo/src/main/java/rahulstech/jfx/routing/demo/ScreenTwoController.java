package rahulstech.jfx.routing.demo;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

/**
 * if you are going to create the layout inside controller class then the
 * controller class must implement {@link rahulstech.jfx.routing.lifecycle.LifecycleAwareController LifecycleAwareController}
 * also you have to create your layout inside {@link rahulstech.jfx.routing.lifecycle.LifecycleAwareController#onLifecycleCreate() onLifecycleCreate()}
 * method and return root node from {@link rahulstech.jfx.routing.lifecycle.LifecycleAwareController#getRoot() getRoot()}. you can extend
 * {@link rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController SimpleLifecycleAwareController}  for simple usages. otherwise use
 * the interface {@link rahulstech.jfx.routing.lifecycle.LifecycleAwareController LifecycleAwareController}.
 */
public class ScreenTwoController extends SimpleLifecycleAwareController {

    private Node root;

    private Label label;

    int counter;

    @Override
    public void onLifecycleCreate() {
        String id = getRouter().getCurrentDestination().getId();

        Button button = new Button("Screen 1");
        button.setOnAction(e->handleButtonOneClick());

        Button backButton = new Button("Back");
        backButton.setOnAction(e->handleBackButtonClick());

        Label label = new Label(id);

        VBox root = new VBox(label,backButton,button);
        root.setSpacing(10);
        root.setBackground(Background.fill(Color.BURLYWOOD));

        this.root = root;
        this.label = label;
    }

    @Override
    public void onLifecycleInitialize() {
        RouterArgument data = getRouter().getCurrentData();
        counter = (int) data.getValue("counter");
        label.setText("Screen 2 (Counter "+counter+")");
    }

    @Override
    public Node getRoot() {
        return root;
    }

    private void handleBackButtonClick() {
        getRouter().popBackStack();
    }

    private void handleButtonOneClick() {
        RouterArgument data = new RouterArgument();
        data.addArgument("counter",++counter);
        getRouter().moveto("screen1",data);
    }
}