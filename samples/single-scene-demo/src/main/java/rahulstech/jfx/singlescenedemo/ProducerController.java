package rahulstech.jfx.singlescenedemo;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

public class ProducerController extends SimpleLifecycleAwareController {

    @FXML
    private TextField input;

    @FXML
    private void handleSendResultButtonClick() {
        String data = input.getText();
        RouterArgument result = new RouterArgument();
        result.addArgument(ConsumerController.KEY_RESULT,data);
        getRouter().popBackstack(result);
    }
}
