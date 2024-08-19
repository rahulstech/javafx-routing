package rahulstech.jfx.routing.demo;

import javafx.fxml.FXML;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

public class DashboardController extends SimpleLifecycleAwareController {

    @FXML
    private void handleButtonOneClick() {
        handleButtonClick(1);
    }

    @FXML
    private void handleButtonTwoClick() {
        handleButtonClick(2);
    }

    private void handleButtonClick(int which) {
        String targetId = "screen"+which;
        RouterArgument data = new RouterArgument();
        data.addArgument("counter",0);
        getRouter().moveto(targetId,data);
    }
}