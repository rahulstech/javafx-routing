package rahulstech.jfx.singlescenedemo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.RouterOptions;
import rahulstech.jfx.routing.element.Destination;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

import java.util.Arrays;

public class ScreenController extends SimpleLifecycleAwareController {

    static final Color[] COLORS = new Color[]{
            Color.BLUEVIOLET, Color.ALICEBLUE, Color.CADETBLUE, Color.DARKBLUE,
            Color.CORNFLOWERBLUE, Color.GREENYELLOW, Color.LIMEGREEN, Color.FORESTGREEN,
            Color.LIGHTYELLOW, Color.LIGHTGOLDENRODYELLOW, Color.HOTPINK
    };

    @FXML
    private Label label;

    @FXML
    private Spinner<Integer> spinner;

    @Override
    public void onLifecycleInitialize() {
        super.onLifecycleInitialize();
        Router router = getRouter();
        RouterArgument args = router.getCurrentData();
        String message = args.getValue("message");
        Integer index = args.getValue("index");
        Integer abc = args.getValue("abc");
        int[] xyz = args.getValue("xyz");
        Color color = COLORS[index];
        ((VBox) getRoot()).setBackground(Background.fill(color));
        if (null!=abc) {
            label.setText(message+" "+abc);
        }
        else if (null!=xyz) {
            label.setText(message+" "+ Arrays.toString(xyz));
        }
        else {
            label.setText(message);
        }
    }

    @FXML
    private void handleBackButtonClick() {
        getRouter().popBackStack();
    }

    @FXML
    private void handleNextDestinationButtonClick() {
        Integer value = spinner.getValue();
        if (value==null) {
            return;
        }
        Router router = getRouter();
        Destination destination = router.getCurrentDestination();
        String targetDestinationId = "screen"+value;
        if (destination.getId().equals(targetDestinationId)) {
            return;
        }
        RouterArgument args = new RouterArgument();
        args.addArgument("message","this is screen"+value);
        args.addArgument("index",value);
        if ("screen1".equals(targetDestinationId)) {
            args.addArgument("abc",77);
        }
        else if ("screen2".equals(targetDestinationId)) {
            args.addArgument("xyz",new int[]{2,5});
        }

        if (targetDestinationId.equals("screen3")) {
            RouterOptions options = new RouterOptions();
            options.setEnterAnimation(DemoRouterContext.ANIMATION_SCALE_UP_XY_SLIDE_IN_BOTTOM);
            router.moveto(targetDestinationId,args,options);
        }
        else {
            router.moveto(targetDestinationId,args);
        }
    }

    @FXML
    private void handleHomeButtonClick() {
        Router router = getRouter();
        String currentScreen = router.getCurrentDestination().getId();
        if (currentScreen.equals("screen1")) {
            RouterArgument data = new RouterArgument();
            data.addArgument("arg0","data from screen1");
            data.addArgument("arg1",55);
            router.moveto("dashboard",data);
        }
        else if (currentScreen.equals("screen3")) {
            RouterArgument data = new RouterArgument();
            data.addArgument("arg0","data from screen3");
            data.addArgument("arg1",new int[]{51,52});
            router.moveto("dashboard",data);
        }
        else {
            getRouter().moveto("dashboard");
        }
    }
}
