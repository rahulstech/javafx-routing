package rahulstech.jfx.routing.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;
import rahulstech.jfx.routing.Router;

public class ScreenOneController extends SimpleLifecycleAwareController {

    @FXML
    private Label label;

    private int counter;

    @Override
    public void onLifecycleInitialize() {
        RouterArgument args = getRouter().getCurrentData();
        counter = (int) args.getValue("counter");
        label.setText("Screen 1 (counter "+counter+")");
    }

    @FXML
    private void handleBackButtonClick() {
        getRouter().popBackStack();
    }

    @FXML
    private void handleGoToButtonClick() {
        Router router = getRouter();
        String targetId = "screen2";
        RouterArgument args = new RouterArgument();
        args.addArgument("counter",++counter);
        router.moveto(targetId,args);
    }
}