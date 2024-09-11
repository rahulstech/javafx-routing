package rahulstech.jfx.historydemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

public class HomeController extends SimpleLifecycleAwareController {

    @FXML
    private void handleGoToNoHistoryScreenButtonClick() {
        getRouter().moveto("no_history_screen");
    }

    @FXML
    private void handleSignUpButtonClick() {
        getRouter().moveto("signup_step1");
    }
}
