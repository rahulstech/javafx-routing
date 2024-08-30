package rahulstech.jfx.routing.transaction;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import rahulstech.jfx.routing.Transaction;
import rahulstech.jfx.routing.backstack.Backstack;
import rahulstech.jfx.routing.element.RouterAnimation;

import java.util.Objects;
import java.util.function.Consumer;

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
            target.showInContent(getContent(),enter_animation,null);
            getBackstack().pushBackstackEntry(target);
        });
        return this;
    }

    /**
     * Removes the top entry from backstack and shows the entry with tag.
     * If the top entry is the target entry then do nothing.If no entry found
     * then nothing happens.
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

            // peek the Target with the tag to show next
            backstack.findFirst(target -> target.getTag().equals(tag))
                    .ifPresent(target -> {
                        SingleSceneTarget to = (SingleSceneTarget) target;

                        // remove the top entry from backstack
                        backstack.remove(from);

                        // perform the transaction
                        to.showInContent(getContent(),popEnter,null);
                        from.hideFromContent(getContent(),popExit, Target::onDestroy);
                    });
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
                top.hideFromContent(getContent(),exit_animation,null);
            }
            target.showInContent(getContent(),enter_animation,null);
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
        sst.showInContent(getContent(),sst.getCachedAnimation(),null);
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public void doForcedDestroy(Target target) {
        SingleSceneTarget sst = (SingleSceneTarget) target;

        RouterAnimation pending = RouterAnimation.getPendingAnimation(sst.getNode());
        if (null!=pending) {
            pending.stop();
        }

        Platform.runLater(()->{
            target.onDestroy();
            getBackstack().remove(target);

        });
    }

    ////////////////////////////////////////////////////////////////////////
    //                          Public Methods                           //
    //////////////////////////////////////////////////////////////////////

    /**
     * Returns {@link Pane} where the screens are added as child
     *
     * @return parent to add child screen
     */
    public Pane getContent() {
        return content;
    }

    ////////////////////////////////////////////////////////////////////////
    //                          Private Methods                          //
    //////////////////////////////////////////////////////////////////////

    private void ensureInTransaction() {
        if (!inTransaction) {
            throw new IllegalStateException("not in transaction, call begin() to start transaction");
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //                       Declared Sub Classes                         //
    ///////////////////////////////////////////////////////////////////////

    /**
     * The {@code SingleSceneTarget} class represents a target in the single scene transaction.
     * It provides methods to manage the lifecycle, including showing, hiding, and destroying.
     * It performs the actual adding and remove the node associated the screen to the content pane
     * with animation. Implementation of the class must handle lifecycle methods on the controller.
     *
     * <p>Each target is associated with a controller and has a tag to identify it within the backstack.</p>
     */
    public static abstract class SingleSceneTarget extends Target {

        private final Object controller;

        private RouterAnimation cachedAnimation;

        public SingleSceneTarget(String tag, Object controller) {
            super(tag);
            this.controller = Objects.requireNonNull(controller, "controller is null");
        }

        /**
         * Returns the controller instance associated with the screen
         *
         * @return the controller instance
         */
        public Object getController() {
            return controller;
        }

        /**
         * Returns the JavaFx {@link Node} for the screen
         *
         * @return JavaFx {@link Node} for the screen
         */
        public abstract Node getNode();

        /**
         * Returns the last used {@link RouterAnimation}.
         * For example: the hide or show animation
         *
         * @return last used animation for the screen
         */
        public RouterAnimation getCachedAnimation() {
            return cachedAnimation;
        }

        /**
         * Sets last used {@link RouterAnimation} for the screen.
         * For example: the hide or show animation
         *
         * @param cachedAnimation the animation
         */
        public void setCachedAnimation(RouterAnimation cachedAnimation) {
            this.cachedAnimation = cachedAnimation;
        }

        /**
         * Adds screen node to the content screen as child, performs animations, handles lifecyle methods
         *
         * @param content instnace of {@link Pane} to add the screen node as child
         * @param enter_animation no null instance of {@link RouterAnimation}
         * @param OnShown {@link Consumer} to be called when screen is shown
         */
        public void showInContent(Pane content, RouterAnimation enter_animation, Consumer<SingleSceneTarget> OnShown) {
            Node front = getNode();

            RouterAnimation pending = RouterAnimation.removePendingAnimation(front);
            if (null!=pending) {
                pending.stop();
            }

            if (!isInContent(content,front)) {
                addToContent(content,front);
            }

            setCachedAnimation(enter_animation);
            enter_animation.setTarget(front);
            enter_animation.addRouterAnimationCallback(new RouterAnimation.SimpleRouterAnimationCallback(){
                @Override
                public void start(RouterAnimation animation) {
                    onBeforeShow();
                }

                @Override
                public void finish(RouterAnimation animation) {
                    RouterAnimation.removePendingAnimation(front,animation);
                    onShow();
                    if (null!=OnShown) {
                        OnShown.accept(SingleSceneTarget.this);
                    }
                }
            });
            RouterAnimation.addPendingAnimation(front,enter_animation);
            enter_animation.play();
        }

        /**
         * Removes screen node from the content screen, performs animation, handles lifecycle methods
         *
         * @param content instance of {@link Pane} to add the screen node as child
         * @param exit_animation non-null instance of {@link RouterAnimation}
         * @param OnHide {@link Consumer} to be called when screen is hidden
         */
        public void hideFromContent(Pane content, RouterAnimation exit_animation, Consumer<SingleSceneTarget> OnHide) {
            Node front = getNode();

            RouterAnimation pending = RouterAnimation.removePendingAnimation(front);
            if (null!=pending) {
                pending.stop();
            }

            setCachedAnimation(exit_animation);
            exit_animation.setTarget(front);
            exit_animation.setAutoReset(true);
            exit_animation.addRouterAnimationCallback(new RouterAnimation.SimpleRouterAnimationCallback(){
                @Override
                public void finish(RouterAnimation animation) {
                    RouterAnimation.removePendingAnimation(front,animation);
                    removeFromContent(content,front);
                    onHide();
                    if (null!=OnHide) {
                        OnHide.accept(SingleSceneTarget.this);
                    }
                }
            });
            RouterAnimation.addPendingAnimation(front,exit_animation);
            exit_animation.play();
        }

        /**
         * Adds the node to the parent as child
         *
         * @param content the parent {@link Pane}
         * @param child the child {@link Node}
         */
        public void addToContent(Pane content, Node child) {
            content.getChildren().add(child);
            // layout bounds of child node is calculated during layout phase
            // not just after adding to its parent. it's a drawback for many
            // animations that use the layout bounds property. that's why
            // layout() is manually called to calculate its bounds as soon as
            // it's added to parent
            content.layout();
        }

        /**
         * Removes the node from the parent
         *
         * @param content the parent {@link Pane}
         * @param child the child {@link Node}
         */
        public void removeFromContent(Pane content, Node child) {
            content.getChildren().remove(child);
        }

        /**
         * Checkes weather the node is added as child to the parent
         *
         * @param content the parent {@link Pane}
         * @param child the child {@link Node}
         * @return {@code true} is added as child, {@code false} not added as child
         */
        public boolean isInContent(Pane content, Node child) {
            return content.getChildren().contains(child);
        }
    }
}
