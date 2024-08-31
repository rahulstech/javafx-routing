package rahulstech.jfx.routing.routerexecutor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import rahulstech.jfx.routing.*;
import rahulstech.jfx.routing.element.Destination;
import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.lifecycle.LifecycleAwareController;
import rahulstech.jfx.routing.transaction.SingleSceneTransaction;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * The {@code SingleSceneScreenExecutor} class is a concrete implementation of {@link RouterExecutor}
 * designed for managing single-scene transitions in a JavaFX application. This executor handles the
 * display and lifecycle management of a screen, utilizing
 * {@link SingleSceneTransaction} for the transaction operations.
 *
 * <p>The {@code SingleSceneScreenExecutor} allows for the addition of new scenes, navigation between
 * scenes, and the management of their lifecycle events such as show, hide, and destroy.</p>
 *
 * @see RouterExecutor
 * @see SingleSceneTransaction
 * @see LifecycleAwareController
 * @author Rahul Bagchi
 * @since 1.0
 */
public class SingleSceneScreenExecutor extends RouterExecutor {

    private SingleSceneTransaction transaction;

    /**
     * Constructs a new {@code SingleSceneScreenExecutor} with the given {@link Router} and {@link SingleSceneTransaction}.
     *
     * @param router the router managing the navigation
     * @param transaction the transaction handler for single scene operations
     */
    public SingleSceneScreenExecutor(Router router, SingleSceneTransaction transaction) {
        super(router);
        this.transaction = transaction;
    }

    /**
     * Returns {@link SingleSceneTransaction} associated with this {@code SingleSceneScreenExecutor}
     *
     * @return non-null {@code SingleSceneTransaction} instance
     */
    public SingleSceneTransaction getTransaction() {
        return transaction;
    }

    /**
     * Replace the current screen in the {@link Router#getContentPane() content} with the requested screen.
     * The current screen i.e. the screen in the backstack top of the {@code transaction}, if any, is hidden.
     * A new screen is created, if required, is shown. It handles enter and exit animations for screens
     * if specified.
     *
     * @param destination the destination to be shown
     * @param options the options containing routing configurations
     * @see SingleSceneTransaction#replace(SingleSceneTransaction.SingleSceneTarget, RouterAnimation, RouterAnimation)
     */
    @Override
    public void show(Destination destination, RouterOptions options) {
        String enterAnimationId = options.getEnterAnimation();
        String exitAnimationId = options.getExitAnimation();
        RouterAnimation enterAnimation = getAnimation(enterAnimationId);
        RouterAnimation exitAnimation = getAnimation(exitAnimationId);
        SingleSceneTransaction.SingleSceneTarget target;
        // if destination is single top then find for existing target, if not found then create a new
        // if destination is not single top then simplely create a new target instance
        if (destination.isSingleTop()) {
            target = (SingleSceneTransaction.SingleSceneTarget) transaction.getBackstack()
                    .findFirst(t -> t.getTag().equals(destination.getId()))
                    .orElse(createTarget(destination,options));
        }
        else {
            target = createTarget(destination,options);
        }
        transaction.begin().replace(target,enterAnimation,exitAnimation).commit();
    }

    /**
     * Removes the current screen from the {@link Router#getContentPane() content} as well as backstack and
     * shows the requested screen from backstack. If the requested screen does not exist in the backstack then
     * no operation is performed. It handles pop enter and pop exits animations for screen if specified.
     *
     * @param destination the destination to be shown next
     * @param options     the options containing routing configuration
     * @see SingleSceneTransaction#popBackstack(String, RouterAnimation, RouterAnimation)
     */
    @Override
    public void popBackstack(Destination destination, RouterOptions options) {
        String id = destination.getId();
        String enterAnimationId = options.getPopEnterAnimation();
        String exitAnimationId = options.getPopExitAnimation();
        RouterAnimation enterAnimation = getAnimation(enterAnimationId);
        RouterAnimation exitAnimation = getAnimation(exitAnimationId);
        transaction.begin().popBackstack(id,enterAnimation,exitAnimation).commit();
    }

    /** {@inheritDoc} */
    @Override
    public void doLifecycleShow(Destination destination) {
        String id = destination.getId();
        transaction.getBackstack()
                .findFirst(target -> id.equals(target.getTag()))
                .ifPresent(target -> transaction.doForcedShow(target));
    }

    /** {@inheritDoc} */
    @Override
    public void doLifecycleHide(Destination destination) {
        String id = destination.getId();
        transaction.getBackstack()
                .findFirst(target -> id.equals(target.getTag()))
                .ifPresent(target -> transaction.doForcedHide(target));
    }

    /** {@inheritDoc} */
    @Override
    public void doLifecycleDestroy(Destination destination) {
        String id = destination.getId();
        transaction.getBackstack()
                .findFirst(target -> id.equals(target.getTag()))
                .ifPresent(target -> transaction.doForcedDestroy(target));
    }

    /**
     * {@code SingleSceneExecutor} does not implementes this method because
     * screen hide transition and transaction is handled by either of the
     * methods {@link #show(Destination, RouterOptions) show}
     * or {@link #popBackstack(Destination, RouterOptions) popBackstack}
     */
    /** {@inheritDoc} */
    @Override
    public void hide(Destination destination, RouterOptions options) {}

    /**
     * Returns {@link RouterAnimation} byt name  or id from {@link RouterContext}
     *
     * @param id the animation name or id
     * @return {@link RouterAnimation} for the given animation name or id, if
     *          no animation found then {@link RouterAnimation#getNoOpAnimation() NO_OP} animation
     *          as default. Return value is always non-null
     * @see RouterContext#getAnimation(String)
     */
    public RouterAnimation getAnimation(String id) {
        RouterContext context = getRouter().getContext();
        return Objects.requireNonNullElse(context.getAnimation(id),context.getAnimation(RouterAnimation.NO_OP));
    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        super.dispose();
        transaction = null;
    }

    /**
     * Creates a new {@link SingleSceneTransaction.SingleSceneTarget} for the specified destination using the provided options.
     *
     * @param destination the destination for which to create the target
     * @param options the options containing configuration details
     * @return a new {@link SingleSceneTransaction.SingleSceneTarget} instance
     */
    protected SingleSceneTransaction.SingleSceneTarget createTarget(Destination destination, RouterOptions options) {
        Class<?> controllerClass = destination.getControllerClass();
        String fxml = destination.getFXML();
        LifecycleAwareController controller;
        if (null!=fxml) {
            controller = doCreateFromFxml(fxml,controllerClass,options);
        }
        else {
            controller = doCreateFromClass(controllerClass,options);
        }
        SingleSceneTransaction.SingleSceneTarget target
                = new LifecycleAwareControllerTarget(destination.getId(),controller);
        target.onCreate();
        return target;
    }

    /**
     * Creates a new {@link LifecycleAwareController} by loading it from the specified FXML file.
     *
     * @param fxml the FXML file path
     * @param controllerClass the class of the controller
     * @param options the options containing bundle and charset configuration
     * @return a new {@link LifecycleAwareController} instance
     */
    protected LifecycleAwareController doCreateFromFxml(String fxml, Class<?> controllerClass, RouterOptions options) {
        Router router = getRouter();
        FXMLLoader loader = router.getContext()
                .getFxmlLoader(fxml,controllerClass,options.getBundle(),options.getCharset());
        try {
            Node root = loader.load();
            LifecycleAwareController controller = loader.getController();
            controller.setRoot(root);
            controller.setRouter(router);
            return controller;
        }
        catch (Exception ex) {
            throw new RuntimeException("fxml load failed with exception "+ex.getMessage());
        }
    }

    /**
     * Creates a new {@link LifecycleAwareController} instance using the specified controller class.
     *
     * @param controllerClass the class of the controller
     * @param options the options containing configuration details
     * @return a new {@link LifecycleAwareController} instance
     */
    protected LifecycleAwareController doCreateFromClass(Class<?> controllerClass, RouterOptions options) {
        Router router = getRouter();
        RouterContext context = router.getContext();
        LifecycleAwareController controller = (LifecycleAwareController) context.newControllerInstance(controllerClass);
        controller.setRouter(router);
        return controller;
    }

    /////////////////////////////////////////////////////////////////////////
    //                       Declared Sub Classes                         //
    ///////////////////////////////////////////////////////////////////////

    /**
     * The {@code LifecycleAwareControllerTarget} class is a specialized implementation of
     * {@link SingleSceneTransaction.SingleSceneTarget} that integrates with a {@link LifecycleAwareController}.
     * This class manages the lifecycle events and node retrieval for the controller.
     */
    public static class LifecycleAwareControllerTarget extends SingleSceneTransaction.SingleSceneTarget {

        /**
         * Constructs a new {@code LifecycleAwareControllerTarget} with the given tag and controller.
         *
         * @param tag the tag identifying this target
         * @param controller the {@link LifecycleAwareController} instance associated with this target
         */
        public LifecycleAwareControllerTarget(String tag, LifecycleAwareController controller) {
            super(tag,controller);
        }

        /**
         * @return the {@link LifecycleAwareController} instance
         */
        @Override
        public LifecycleAwareController getController() {
            return (LifecycleAwareController) super.getController();
        }

        /**
         * @return the root {@link Node} of the controller
         */
        public Node getNode() {
            return getController().getRoot();
        }

        /** {@inheritDoc} */
        @Override
        public void onCreate() {
            callOnLifecycleAwareController(LifecycleAwareController::onLifecycleCreate);
        }

        /** {@inheritDoc} */
        @Override
        public void onBeforeShow() {
            callOnLifecycleAwareController(LifecycleAwareController::onLifecycleInitialize);
        }

        /** {@inheritDoc} */
        @Override
        public void onShow() {
            callOnLifecycleAwareController(LifecycleAwareController::onLifecycleShow);
        }

        /** {@inheritDoc} */
        @Override
        public void onHide() {
            callOnLifecycleAwareController(LifecycleAwareController::onLifecycleHide);
        }

        /** {@inheritDoc} */
        @Override
        public void onDestroy() {
            callOnLifecycleAwareController(LifecycleAwareController::onLifecycleDestroy);
        }

        private void callOnLifecycleAwareController(Consumer<LifecycleAwareController> consumer) {
            LifecycleAwareController controller = getController();
            if (null!=controller) {
                consumer.accept(controller);
            }
        }
    }
}
