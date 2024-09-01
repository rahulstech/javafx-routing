package rahulstech.jfx.singlescenedemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DemoApplication extends Application {

    DemoRoutingController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/demo_routing.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root,600,600);
        primaryStage.setTitle("Router Demo App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (null!=controller) {
            controller.dispose();
        }
    }
}
