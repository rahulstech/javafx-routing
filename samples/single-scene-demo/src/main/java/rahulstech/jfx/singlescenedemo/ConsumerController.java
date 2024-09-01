package rahulstech.jfx.singlescenedemo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import rahulstech.jfx.routing.element.RouterArgument;

public class ConsumerController extends DemoBaseController {

    public static final String KEY_RESULT = "result";

    public static final String KEY_RESULT_TWO = "result2";

    @FXML
    private Label message;

    @Override
    public void onLifecycleInitialize() {
        super.onLifecycleInitialize();
        RouterArgument result = getRouter().getCurrentResult();
        if (null!=result) {
            String result_data = result.getValue(KEY_RESULT);
            String result2_data = result.getValue(KEY_RESULT_TWO);
            String data = "result1="+result_data+"\nresult2="+result2_data;
            message.setText(data);
        }
    }

    @FXML
    private void handleGoToProducerButtonClick() {
        getRouter().moveto("producer");
    }

    @FXML
    private void handleBackButtonClick() {
        getRouter().popBackStack();
    }
}
