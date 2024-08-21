package rahulstech.jfx.routing.transaction;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import rahulstech.jfx.routing.Transaction;
import rahulstech.jfx.routing.backstack.Backstack;
import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.element.RouterCompoundAnimation;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class SingleSceneTransaction extends Transaction {

    private final Pane content;

    private boolean inTransaction = false;

    public SingleSceneTransaction(Pane content) {
        this.content = content;
    }

    ////////////////////////////////////////////////////////////////////////
    //                       Transaction Methods                         //
    //////////////////////////////////////////////////////////////////////

    /**
     * call this method to start enqueueing new operations like
     * {@link #add(SingleSceneTarget, RouterAnimation)},
     * {@link #replace(SingleSceneTarget, RouterAnimation, RouterAnimation)} ,
     * {@link #popBackstack(String, RouterAnimation, RouterAnimation)}
     * if it is not called any subsequent call to the above methods will
     * throw exception.
     *
     * @return this instance
     */
    public SingleSceneTransaction begin() {
        inTransaction = true;
        return this;
    }

    /**
     * adds the node over the current node without removing it. thus both
     * the nodes will be in the content. this is helpful when overlaying
     * the node over the existing node. for example showing dialog. to add backdrop
     * which will be shown exactly behind the new node use backdrop. but this is not
     * necessary. a new backstack entry will be created.
     *
     * @param target new target
     * @return this instance
     * @throws IllegalStateException if begin is not called
     */
    public SingleSceneTransaction add(SingleSceneTarget target, RouterAnimation enter_animation) {
        ensureInTransaction();
        enqueueOperation(()->{
            addInternal(target,enter_animation);
            getBackstack().pushBackstackEntry(target);
        });
        return this;
    }

    /**
     * Removes the top entry till the entry just before the target entry from backstack
     * and shows the entry with tag. If the top entry is the target entry then does nothing.
     * Also, if the top entry is not the target but no entry found with the tag then nothing happens.
     *
     * @param tag tag of the target
     * @param popEnter animation for pop entering target
     * @param popExit animation for pop exiting target
     * @return this instance
     * @throws IllegalStateException if begin is not called
     */
    public SingleSceneTransaction popBackstack(String tag, RouterAnimation popEnter, RouterAnimation popExit) {
        ensureInTransaction();
        if (getBackstack().isEmpty()) {
            return this;
        }
        enqueueOperation(()->{
            Backstack<Target> backstack = getBackstack();

            // the top entry is the target entry, i.e. we are already in the target, no transaction to perform
            SingleSceneTarget from = (SingleSceneTarget) backstack.peekBackstackEntry();
            if (from.getTag().equals(tag)) {
                return;
            }

            SingleSceneTarget top = (SingleSceneTarget) backstack.popBackstackEntry();

            // peek to Target to be shown next
            SingleSceneTarget next = (SingleSceneTarget) backstack.peekBackstackEntry();


            // perform the transaction
            addInternal(next,popEnter);
            destroyInternal(top,popExit);
        });
        return this;
    }

    /**
     * remove the current node from the content and adds the new node.a new backstack entry will be created.
     *
     * @param target new target
     * @return this instance
     * @throws IllegalStateException if begin is not called
     */
    public SingleSceneTransaction replace(SingleSceneTarget target,
                                          RouterAnimation enter_animation, RouterAnimation exit_animation) {
        ensureInTransaction();
        enqueueOperation(()->{
            if (!getBackstack().isEmpty()) {
                SingleSceneTarget top = (SingleSceneTarget) getBackstack().peekBackstackEntry();
                hideInternal(top,exit_animation);
            }
            addInternal(target,enter_animation);
            getBackstack().pushBackstackEntry(target);
        });
        return this;
    }

    /**
     * execute the queued operations
     *
     * @return true if anything executed, false otherwise
     * @throws IllegalStateException if begin is not called
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean commit() {
        ensureInTransaction();
        return executePendingOperations();
    }

    /** @InheritDoc */
    @Override
    public void doForcedShow(Target target) {
        SingleSceneTarget sst = (SingleSceneTarget) target;
        Node node = sst.getNode();

        RouterAnimation pending = RouterAnimation.getPendingAnimation(node);
        if (null!=pending) {
            pending.stop();
        }

        if (isInContent(node)) {
            removeFromContent(node);
        }

        addInternal(sst,sst.getCachedAnimation());
    }

    /** @InheritDoc */
    @Override
    public void doForcedHide(Target target) {
        SingleSceneTarget sst = (SingleSceneTarget) target;
        Node node = sst.getNode();

        RouterAnimation animation = RouterAnimation.getPendingAnimation(node);
        if (null!=animation) {
            animation.stop();
        }

        Platform.runLater(sst::onHide);
    }

    /** @InheritDoc */
    @Override
    public void doForcedDestroy(Target target) {
        SingleSceneTarget sst = (SingleSceneTarget) target;

        RouterAnimation pending = RouterAnimation.getPendingAnimation(sst.getNode());
        if (null!=pending) {
            pending.stop();
        }

        Platform.runLater(sst::onDestroy);
    }

    ////////////////////////////////////////////////////////////////////////
    //                          Public Methods                           //
    //////////////////////////////////////////////////////////////////////

    public Pane getContent() {
        return content;
    }

    ////////////////////////////////////////////////////////////////////////
    //                        Protected Methods                          //
    //////////////////////////////////////////////////////////////////////

    protected void addToContent(Node node) {
        getContent().getChildren().add(node);
        // unfortunately in javafx layout is not triggered as soon as
        // a child is added to it but in the next pulse.
        // some animations may need layout bounds to calculate the animation values,
        // therefore layout() is called manually so that the layout bounds are available.
        getContent().layout();
    }

    protected void removeFromContent(Node node) {
        getContent().getChildren().remove(node);
    }

    protected boolean isInContent(Node node) {
        return getContent().getChildren().contains(node);
    }

    ////////////////////////////////////////////////////////////////////////
    //                          Private Methods                          //
    //////////////////////////////////////////////////////////////////////

    private void addInternal(SingleSceneTarget target, RouterAnimation enter_animation) {
        Node front = target.getNode();
        Backdrop backdrop = target.getBackdrop();

        RouterAnimation pending = RouterAnimation.removePendingAnimation(front);
        if (null!=pending) {
            pending.stop();
        }

        addToContent(front);

        RouterAnimation animation;
        if (null!=backdrop) {
            backdrop.enter.setTarget(backdrop.backdrop);
            enter_animation.setTarget(front);
            animation = new RouterCompoundAnimation(backdrop.enter,enter_animation);
        }
        else {
            enter_animation.setTarget(front);
            animation = enter_animation;
        }
        target.setCachedAnimation(animation);
        animation.addRouterAnimationCallback(new RouterAnimation.SimpleRouterAnimationCallback(){
            @Override
            public void start(RouterAnimation animation) {
                target.onBeforeShow();
            }

            @Override
            public void finish(RouterAnimation animation) {
                RouterAnimation.removePendingAnimation(front,animation);
                target.onShow();
            }
        });
        RouterAnimation.addPendingAnimation(front,animation);
        animation.play();
    }

    private void hideInternal(SingleSceneTarget target, RouterAnimation exit_animation) {
        removeInternal(target,exit_animation,
                Target::onHide);
    }

    private void destroyInternal(SingleSceneTarget target,RouterAnimation exit_animation) {
        removeInternal(target,exit_animation, Target::onDestroy);
    }

    private void removeInternal(SingleSceneTarget target, RouterAnimation exit_animation, Consumer<Target> consume_after) {
        Node exiting_node = target.getNode();
        Backdrop backdrop = target.getBackdrop();
        RouterAnimation pending = RouterAnimation.removePendingAnimation(exiting_node);
        if (null!=pending) {
            pending.stop();
        }

        RouterAnimation animation;
        if (null!=backdrop) {
            backdrop.exit.setTarget(backdrop.backdrop);
            exit_animation.setTarget(exiting_node);
            animation = new RouterCompoundAnimation(backdrop.exit,exit_animation);
        }
        else {
            exit_animation.setTarget(exiting_node);
            animation = exit_animation;
        }
        target.setCachedAnimation(animation);
        animation.setAutoReset(true);
        animation.addRouterAnimationCallback(new RouterAnimation.SimpleRouterAnimationCallback(){
            @Override
            public void finish(RouterAnimation animation) {
                RouterAnimation.removePendingAnimation(exiting_node,animation);
                removeFromContent(exiting_node);
                if (null!=backdrop) {
                    removeFromContent(backdrop.backdrop);
                }
                consume_after.accept(target);
            }
        });
        RouterAnimation.addPendingAnimation(exiting_node,animation);
        animation.play();
    }

    private void ensureInTransaction() {
        if (!inTransaction) {
            throw new IllegalStateException("not in transaction, call begin() to start transaction");
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //                       Declared Sub Classes                         //
    ///////////////////////////////////////////////////////////////////////

    public static abstract class SingleSceneTarget extends Target {

        private final Object controller;

        private RouterAnimation cachedAnimation;

        private Backdrop backdrop;

        public SingleSceneTarget(String tag, Object controller) {
            super(tag);
            this.controller = Objects.requireNonNull(controller, "controller is null");
        }

        public Object getController() {
            return controller;
        }

        public abstract Node getNode();

        public void setBackdrop(Backdrop backdrop) {
            this.backdrop = backdrop;
        }

        public Backdrop getBackdrop() {
            return backdrop;
        }

        public RouterAnimation getCachedAnimation() {
            return cachedAnimation;
        }

        public void setCachedAnimation(RouterAnimation cachedAnimation) {
            this.cachedAnimation = cachedAnimation;
        }
    }

    public static class Backdrop {

        private final Node backdrop;
        private final RouterAnimation enter;
        private final RouterAnimation exit;

        public Backdrop(Node backdrop, RouterAnimation enter, RouterAnimation exit) {
            this.backdrop = backdrop;
            this.enter = null==enter ? RouterAnimation.getNoOpAnimation() : enter;
            this.exit = null==exit ? RouterAnimation.getNoOpAnimation() : exit;
        }

        public Node getBackdrop() {
            return backdrop;
        }

        public RouterAnimation getEnter() {
            return enter;
        }

        public RouterAnimation getExit() {
            return exit;
        }
    }
}
