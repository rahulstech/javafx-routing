package rahulstech.jfx.singlescenedemo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.layout.RouterStackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DemoRoutingController implements Initializable {

    @FXML
    private BorderPane borderPane;

    private RouterStackPane routerPane;

    public DemoRoutingController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        routerPane = (RouterStackPane) borderPane.getCenter();
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
