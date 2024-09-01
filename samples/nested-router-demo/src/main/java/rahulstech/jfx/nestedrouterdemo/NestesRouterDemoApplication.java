package rahulstech.jfx.nestedrouterdemo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.layout.RouterStackPane;

public class NestesRouterDemoApplication extends Application {

    private RouterStackPane root;

    @Override
    public void start(Stage stage) throws Exception {
        root = new RouterStackPane();
        root.setRouterConfig("main_router.xml");

        NestedRouterDemoRouterContext context = new NestedRouterDemoRouterContext();
        root.setContext(context);

        Scene scene = new Scene(root,800,600);
        stage.setScene(scene);
        stage.setTitle("Nested Router Demo");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (null!=root) {
            Router router = root.getRouter();
            if (null!=router) {
                router.dispose();
            }
        }
    }
}
