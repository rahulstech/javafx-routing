package rahulstech.jfx.singlescenedemo;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

@SuppressWarnings("unused")
public class DashboardController extends SimpleLifecycleAwareController {

    @FXML
    private Spinner<Integer> spinner;

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
    private void handleOpenDialogButtonClick() {
        Router router = getRouter();
        router.moveto("simple_dialog");
    }

    @Override
    public void onLifecycleDestroy() {
        super.onLifecycleDestroy();
        System.out.println("onDestroy");
    }
}
