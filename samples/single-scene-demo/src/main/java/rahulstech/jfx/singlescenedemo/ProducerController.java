package rahulstech.jfx.singlescenedemo;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import rahulstech.jfx.routing.element.RouterArgument;

public class ProducerController extends DemoBaseController {

    @FXML
    private TextField input;

    @FXML
    private void handleGoToProducerTwoButtonClick() {
        String text = input.getText();
        RouterArgument data = new RouterArgument();
        data.addArgument(ConsumerController.KEY_RESULT,text);
        getRouter().moveto("producer_two",data);
    }

    @FXML
    private void handleSendResultButtonClick() {
        String data = input.getText();
        RouterArgument result = new RouterArgument();
        result.addArgument(ConsumerController.KEY_RESULT,data);
        getRouter().popBackstack(result);
    }
}
