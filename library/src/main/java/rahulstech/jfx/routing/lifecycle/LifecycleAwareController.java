package rahulstech.jfx.routing.lifecycle;

import javafx.scene.Node;
import rahulstech.jfx.routing.Router;

/**
 *
 */
public interface LifecycleAwareController {

    /**
     * set the {@link Node} handled by this controller
     *
     * @param root the {@link Node} instance
     */
    void setRoot(Node root);

    /**
     * get the {@link Node} handled by this controller
     *
     * @return the {@link Node} instance
     */
    Node getRoot();

    /**
     * set the current {@link Router} instance
     *
     * @param router the {@link Router} instance
     */
    void setRouter(Router router);

    /**
     * get the current {@link Router} instance
     *
     * @return the {@link Router} instance
     */
    Router getRouter();

    /**
     * Called when this controller is created for the first time.
     * This is best place to set up and initialize objects which
     * will persist throughout the whole lifecycle of the controller.
     * If your controller creates the layout inside it then this method
     * is the ideal place for it. Don't forget to set the root node
     * using {@link #setRoot(Node)}
     * After this method is called, {@link #getRoot()} must return
     * non-null value
     */
    void onLifecycleCreate();

    /**
     * Called every time before showing. This is the ideal place to set up based on
     * passed value to controller. For example: changing the global toolbar title based
     * or setting up the menus etc.
     */
    void onLifecycleInitialize();

    /**
     * Called when the root node is completely visible and gains focus
     */
    void onLifecycleShow();

    /**
     * Called when root node loose focus. It is not always necessary
     * that hide will make the root node invisible. In hidden state
     * not interactions with root not and the controller
     * is allowed. For example: in a video playback application you must
     * pause the playback when in hidden state.
     */
    void onLifecycleHide();

    /**
     * Called when this controller is no longer needed. Release all the
     * resource here. For example: close db connection and any open stream.
     */
    void onLifecycleDestroy();
}
