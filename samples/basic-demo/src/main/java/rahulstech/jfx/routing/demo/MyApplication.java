package rahulstech.jfx.routing.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.RouterContext;
import rahulstech.jfx.routing.layout.RouterStackPane;

public class MyApplication extends Application {

    private Router router;

    @Override
    public void start(Stage primaryStage) {

        RouterContext context = new MyContext();

        RouterStackPane root = new RouterStackPane();
        root.setContext(context);
        root.setRouterConfig("router.xml");

        Scene scene = new Scene(root,800,600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Routing Example");
        primaryStage.setOnShown(e-> router = root.getRouter());
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (null!=router) {
            router.dispose();
        }
        super.stop();
    }

    public static void main(String... args) {
        launch(args);
    }
}