package rahulstech.jfx.singlescenedemo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

import java.util.Arrays;

public class DashboardController extends SimpleLifecycleAwareController {

    @FXML
    private Label message;

    @FXML
    private Spinner<Integer> spinner;

    @Override
    public void onLifecycleShow() {
        super.onLifecycleShow();
        RouterArgument data = getRouter().getCurrentData();
        if (null!=data) {
            String arg0 = data.getValue("arg0");
            Object arg1 = data.getValue("arg1");

            StringBuilder builder = new StringBuilder();
            builder.append("arg0=").append(arg0).append("\n");
            if (null!=arg1) {
                builder.append("args1=");
                if (arg1 instanceof Integer) {
                    builder.append(arg1);
                }
                else {
                    builder.append(Arrays.toString((int[]) arg1));
                }
            }
            message.setText(builder.toString());
        }
    }

    @FXML
    private void handleGoToScreenButtonClick() {
        Router router = getRouter();

        Integer value = spinner.getValue();
        if (value==null) {
            return;
        }
        String targetDestinationId = "screen"+value;
        RouterArgument args = new RouterArgument();
        args.addArgument("message","this is screen"+value);
        args.addArgument("index",value);
        if ("screen1".equals(targetDestinationId)) {
            args.addArgument("abc",55);
        }
        else if ("screen2".equals(targetDestinationId)) {
            args.addArgument("xyz",new int[]{7,9});
        }
        router.moveto(targetDestinationId,args);
    }

    @FXML
    private void handleGoToConsumerButtonClick() {
        getRouter().moveto("consumer");
    }
}
