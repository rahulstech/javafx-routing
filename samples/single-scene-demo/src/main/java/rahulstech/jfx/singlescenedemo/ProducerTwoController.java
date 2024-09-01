package rahulstech.jfx.singlescenedemo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.element.RouterArgument;

public class ProducerTwoController extends DemoBaseController {

    @FXML
    private Label producerData;

    @FXML
    private TextField input;

    @Override
    public void onLifecycleInitialize() {
        super.onLifecycleInitialize();
        RouterArgument data = getRouter().getCurrentData();
        if (null!=data && data.contains(ConsumerController.KEY_RESULT)) {
            String text = data.getValue(ConsumerController.KEY_RESULT);
            producerData.setText(text);
        }
    }

    @FXML
    private void handleSendResultButtonClick() {
        Router router = getRouter();
        String text = input.getText();
        RouterArgument data = router.getCurrentData();
        RouterArgument result = new RouterArgument();
        if (null!=data) {
            result.merge(data);
        }
        result.addArgument(ConsumerController.KEY_RESULT_TWO,text);
        router.popBackstackUpTo("consumer",false,result);
    }
}
