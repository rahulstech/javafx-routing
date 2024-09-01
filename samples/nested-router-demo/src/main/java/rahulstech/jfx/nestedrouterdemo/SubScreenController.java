package rahulstech.jfx.nestedrouterdemo;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

public class SubScreenController extends SimpleLifecycleAwareController {

    private Label title;

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        title = new Label();

        Button back = new Button("back");
        back.setOnAction(e->handleBackButtonClick());

        Button next = new Button("next");
        next.setOnAction(e->handleNextButtonClick());

        Button nextInParent = new Button("Goto Screen2 of Parent");
        nextInParent.setOnAction(e->handleNextInParentButtonClick());

        VBox root = new VBox(title, back, next, nextInParent);
        root.setBackground(Background.fill(Utils.getRandomColor()));
        root.setSpacing(12);
        root.setPadding(new Insets(12,12,12,12));

        setRoot(root);
    }

    @Override
    public void onLifecycleInitialize() {
        super.onLifecycleInitialize();
        Router router = getRouter();
        String id = router.getCurrentDestination().getId();
        title.setText(id+" of screen1");
    }

    private void handleBackButtonClick() {
        // clicking back on sub_screen0 will back to the screen0 of the parent router
        // otherwise it will pop the sub screens from the backstack of router handeling sub screens
        String id = getCurrentDestinationId();
        if (id.equals("sub_screen0")) {
            getParentRouter().popBackStack();
        }
        else {
            getRouter().popBackStack();
        }
    }

    private void handleNextButtonClick() {
        // next button will open the sub screen in the sequence
        // sub_screen0 -> sub_screen1 -> sub_screen2
        // on clicking next on  sub_screen2 it will goto screen2 of parent router
        String id = getCurrentDestinationId();
        if (id.equals("sub_screen0")) {
            gotoScreen("sub_screen1");
        }
        else if (id.equals("sub_screen1")){
            gotoScreen("sub_screen2");
        }
        else {
            getParentRouter().moveto("screen2");
        }
    }

    private void handleNextInParentButtonClick() {
        // directly to goto the screen2 in parent router
        getParentRouter().moveto("screen2");
    }

    private String getCurrentDestinationId() {
        Router router = getRouter();
        return router.getCurrentDestination().getId();
    }

    private void gotoScreen(String id) {
        getRouter().moveto(id);
    }

    private Router getParentRouter() {
        NestedRouterDemoRouterContext context = (NestedRouterDemoRouterContext) getRouter().getContext();
        return context.getParentRouter();
    }
}
