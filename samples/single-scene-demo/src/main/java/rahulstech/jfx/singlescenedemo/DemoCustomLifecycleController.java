package rahulstech.jfx.singlescenedemo;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rahulstech.jfx.routing.Router;

public class DemoCustomLifecycleController {

    private Router router;

    private final VBox root;

    public DemoCustomLifecycleController() {

        Label title = new Label("Custom RouterExecutor Example");

        Button backButton = new Button("Back");
        backButton.setOnAction(e->handleBackButtonClick());

        Button gotoHome = new Button("Go To Home");
        gotoHome.setOnAction(e->handleGotoHomeButtonClick());

        VBox content =  new VBox(title,backButton,gotoHome);
        content.setPadding(new Insets(10,10,10,10));
        content.setBackground(Background.fill(Color.BURLYWOOD));
        content.setSpacing(10);

        this.root = content;
    }

    public Node getRootNode() {
        return root;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public void onShow() {}

    public void onHide() {}

    private void handleBackButtonClick() {
        router.popBackStack();
    }

    private void handleGotoHomeButtonClick() {
        router.moveto(router.getHomeDestination(),null,null);
    }
}
