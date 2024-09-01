package rahulstech.jfx.singlescenedemo;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rahulstech.jfx.routing.Router;

public class NoFxmlController extends DemoBaseController {

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        Label title = new Label("No Fxml Controller Example");

        Button backButton = new Button("Back");
        backButton.setOnAction(e->handleBackButtonClick());

        Button gotoHome = new Button("Go To Home");
        gotoHome.setOnAction(e->handleGotoHomeButtonClick());

        VBox content =  new VBox(title,backButton,gotoHome);
        content.setPadding(new Insets(10,10,10,10));
        content.setBackground(Background.fill(Color.BURLYWOOD));
        content.setSpacing(10);

        setRoot(content);
    }

    private void handleBackButtonClick() {
        getRouter().popBackStack();
    }

    private void handleGotoHomeButtonClick() {
        Router router = getRouter();
        router.moveto(router.getHomeDestination(),null,null);
    }
}
