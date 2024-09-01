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

import java.util.Arrays;
import java.util.Optional;

public class ScreenController extends DemoBaseController {

    static final Color[] COLORS = new Color[]{
            Color.BLUEVIOLET, Color.ALICEBLUE, Color.CADETBLUE, Color.DARKBLUE,
            Color.CORNFLOWERBLUE
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

        // setting the background accoring to the passed screen index passed via data
        // this is done in lifecycle initilization therefore it is executed every time before showing
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

    Optional<String> getNextDestinationId() {
        Integer value = spinner.getValue();
        if (value==null) {
            return Optional.empty();
        }
        Router router = getRouter();
        Destination destination = router.getCurrentDestination();
        String targetDestinationId = "screen"+value;
        if (destination.getId().equals(targetDestinationId)) {
            return Optional.empty();
        }
        return Optional.of(targetDestinationId);
    }

    @FXML
    private void handleNextDestinationButtonClick() {
        Integer value = spinner.getValue();
        Optional<String> oNextDestination = getNextDestinationId();
        if (!oNextDestination.isPresent()) {
            return;
        }
        Router router = getRouter();
        String targetDestinationId = oNextDestination.get();
        RouterArgument args = new RouterArgument();
        args.addArgument("message","this is screen"+value);
        args.addArgument("index",value);
        if ("screen1".equals(targetDestinationId)) {
            args.addArgument("abc",77);
        }
        else if ("screen2".equals(targetDestinationId)) {
            args.addArgument("xyz",new int[]{2,5});
        }

        try {

            if (targetDestinationId.equals("screen3")) {
                // while navigating to "screen3" use the provided transition animation
                // instead of default transition animation
                RouterOptions options = new RouterOptions();
                options.setEnterAnimation(DemoRouterContext.ANIMATION_SCALE_UP_XY_SLIDE_IN_BOTTOM);
                router.moveto(targetDestinationId, args, options);
            } else {
                router.moveto(targetDestinationId, args);
            }
        }
        catch (Exception ex) {
            // hadles execption like unknown target destination
            System.err.println("handleNextDestinationButtonClick: "+ex.getMessage());
        }
    }

    @FXML
    private void handleHomeButtonClick() {
        Router router = getRouter();
        String currentScreen = router.getCurrentDestination().getId();

        // passing different types of data to home destination depending on current destination
        // this is example of passing data for compound type argument. here "arg1" is "int" or "int_array"
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

    @FXML
    private void handleNextDestinationWihtoutDataButtonClick() {
        Optional<String> oNextDestination = getNextDestinationId();
        if (!oNextDestination.isPresent()) {
            return;
        }
        Router router = getRouter();
        String targetDestinationId = oNextDestination.get();
        try {
            // though some of the destinations requires data but no data passed for all destinations
            router.moveto(targetDestinationId);
        }
        catch (Exception ex) {
            // handles execption for not passing required data
            // or trying to navigate unknown destination
            System.err.println("handleNextDestinationWihtoutDataButtonClick: "+ex.getMessage());
        }
    }
}
