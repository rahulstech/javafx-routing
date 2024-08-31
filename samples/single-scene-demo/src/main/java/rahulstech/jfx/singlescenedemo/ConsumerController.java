package rahulstech.jfx.singlescenedemo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

public class ConsumerController extends SimpleLifecycleAwareController {

    public static final String KEY_RESULT = "result";

    @FXML
    private Label message;

    @Override
    public void onLifecycleInitialize() {
        super.onLifecycleInitialize();
        RouterArgument result = getRouter().getCurrentResult();
        if (null!=result) {
            String result_data = result.getValue(KEY_RESULT);
            message.setText(result_data);
        }
    }

    @FXML
    private void handleGoToProducerButtonClick() {
        getRouter().moveto("producer");
    }

    @FXML
    private void handleGoToHomeButtonClick() {
        getRouter().moveto("dashboard");
    }
}
