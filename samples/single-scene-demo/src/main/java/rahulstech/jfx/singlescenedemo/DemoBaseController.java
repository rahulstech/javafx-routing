package rahulstech.jfx.singlescenedemo;

import javafx.stage.Stage;
import rahulstech.jfx.routing.element.Destination;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

public class DemoBaseController extends SimpleLifecycleAwareController {

    @Override
    public void onLifecycleInitialize() {
        super.onLifecycleInitialize();
        setStageTitle();
    }

    void setStageTitle() {
        if (null==getRoot()) {
            return;
        }
        if (null==getRoot().getScene()) {
            return;
        }
        if (null==getRoot().getScene().getWindow()) {
            return;
        }
        Destination destination = getRouter().getCurrentDestination();
        String title = destination.getTitle();
        String id = destination.getId();
        Stage stage = (Stage) getRoot().getScene().getWindow();
        if (title==null) {
            stage.setTitle(id);
        }
        else {
            stage.setTitle(title);
        }
    }
}
