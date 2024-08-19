package rahulstech.jfx.routing.lifecycle;

import javafx.scene.Node;
import rahulstech.jfx.routing.Router;

public interface LifecycleAwareController {

    void setRoot(Node root);

    Node getRoot();

    void setRouter(Router router);

    Router getRouter();

    void onLifecycleCreate();

    void onLifecycleInitialize();

    void onLifecycleShow();

    void onLifecycleHide();

    void onLifecycleDestroy();
}
