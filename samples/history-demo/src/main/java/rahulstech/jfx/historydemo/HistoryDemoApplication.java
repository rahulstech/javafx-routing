package rahulstech.jfx.historydemo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.element.Destination;
import rahulstech.jfx.routing.layout.RouterStackPane;

import java.util.Arrays;

public class HistoryDemoApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        RouterStackPane root = new RouterStackPane();
        root.setContext(new HistoryDemoRouterContext());
        root.routerProperty().addListener((observableValue, oldRouter, newRouter) -> {
            if (null!=newRouter) {
                prepareRouter(newRouter);
            }
        });

        Scene scene = new Scene(root,500,600);
        stage.setScene(scene);
        stage.show();
    }

    private void prepareRouter(Router router) {
        Destination home = new Destination.Builder("home")
                .setFXML("home.fxml")
                .build();

        Destination noHistoryScreen = new Destination.Builder("no_history_screen")
                .setControllerClass(NoHistoryScreenController.class)
                .setRemoveHistory(true)
                .build();

        Destination noHistoryNextScreen = new Destination.Builder("no_history_next_screen")
                .setControllerClass(NoHistoryNextScreenController.class)
                .build();

        Destination signupStep1 = new Destination.Builder("signup_step1")
                .setControllerClass(StepOneController.class)
                .build();

        Destination signupStep2 = new Destination.Builder("signup_step2")
                .setControllerClass(StepTwoController.class)
                .build();

        Destination profile = new Destination.Builder("profile")
                .setControllerClass(ProfileController.class)
                .build();

        router.addAllDestination(Arrays.asList(
                home,
                noHistoryScreen,noHistoryNextScreen,
                signupStep1,signupStep2,profile
        ));

        router.setHomeDestination(home);
        router.setDefaultAnimations("fade_in","fade_out","fade_in","fade_out");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
