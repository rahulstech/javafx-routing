package rahulstech.jfx.singlescenedemo;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.RouterPane;
import rahulstech.jfx.routing.layout.RouterStackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DemoRoutingController implements Initializable {

    public BorderPane borderPane;

    RouterPane routerPane;

    final Label simpleMessage;

    public DemoRoutingController() {
        simpleMessage = new Label("this is a simple message");
        simpleMessage.setPrefHeight(200);
        simpleMessage.setPrefWidth(300);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        routerPane = (RouterStackPane) borderPane.getCenter();
    }

    @SuppressWarnings("unused")
    public void handleChangeCenterButtonClick() {
        Node center = borderPane.getCenter();
        if (center==simpleMessage) {
            borderPane.setCenter(routerPane.getPane());
        }
        else {
            borderPane.setCenter(simpleMessage);
        }
    }

    public void dispose() {
        if (null!=routerPane) {
            Router router = routerPane.getRouter();
            if (null!=router) {
                router.dispose();
            }
        }
    }
}
