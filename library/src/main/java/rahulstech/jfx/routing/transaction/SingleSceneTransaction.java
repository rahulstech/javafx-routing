package rahulstech.jfx.routing.transaction;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import rahulstech.jfx.routing.RouterContext;
import rahulstech.jfx.routing.RouterOptions;
import rahulstech.jfx.routing.Transaction;
import rahulstech.jfx.routing.backstack.BackstackEntry;
import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.util.StringUtil;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A {@code SingleSceneTransaction} is an implementation of {@link Transaction} that handles single scene screen
 * transactions. Screens are shown inside the content parent provided via constructor while creating instance.
 * {@code SingleSceneTransaction} provides add, replace, pop transactions.
 *
 * @author Rahul Bagchi
 * @since 1.0.0
 */
public class SingleSceneTransaction extends BaseGenericOperationTransaction<SingleSceneTransaction.SingleSceneTarget> {

    private final Pane content;

    private boolean inTransaction = false;

    /**
     * Create new {@code SingleSceneTransaction} instance with the {@link Pane}
     * to use as content parent for all screens handled by this {@code SingleSceneTransaction}
     * and {@link RouterContext}
     *
     * @param context the associated {@code RouterContext}
     * @param content {@code Pane} as content parent for screens
     */
    public SingleSceneTransaction(RouterContext context, Pane content) {
        super(context);
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
     * @deprecated since 1.1.0
     */
    @Deprecated
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
     * @param enter_animation non-null {@link RouterAnimation} instance to apply on entering {@link Node}
     * @return this instance
     * @throws IllegalStateException if begin is not called
     * @deprecated since 1.1.0
     * @see #show(SingleSceneTarget, RouterOptions)
     * @see #showSingleTop(String, Supplier, RouterOptions)
     */
    @Deprecated
    public SingleSceneTransaction add(SingleSceneTarget target, RouterAnimation enter_animation) {
        return this;
    }

    /**
     * Removes the top entry from backstack and shows the entry with tag.
     * If the top entry is the target entry then do nothing.If no entry found
     * then nothing happens.
     *
     * @param tag tag of the target
     * @param popEnter non-null {@link RouterAnimation} instance to apply on pop entering {@link Node}
     * @param popExit non-null {@link RouterAnimation} instance to apply on pop exiting {@link Node}
     * @return this instance
     * @throws IllegalStateException if begin is not called
     * @deprecated since 1.1.0
     * @see #popShow(String, RouterOptions)
     * @see #popHide(String, RouterOptions)
     */
    @Deprecated
    public SingleSceneTransaction popBackstack(String tag, RouterAnimation popEnter, RouterAnimation popExit) {
        return this;
    }

    /**
     * remove the current node from the content and adds the new node.a new backstack entry will be created.
     *
     * @param target new target
     * @param enter_animation non-null {@link RouterAnimation} instance to apply on entering {@link Node}
     * @param exit_animation non-null {@link RouterAnimation} instance to apply on exiting {@link Node}
     * @return this instance
     * @throws IllegalStateException if begin is not called
     * @deprecated since 1.1.0
     * @see #show(SingleSceneTarget, RouterOptions)
     * @see #showSingleTop(String, Supplier, RouterOptions)
     * @see #hide(String, RouterOptions)
     */
    @Deprecated
    public SingleSceneTransaction replace(SingleSceneTarget target,
                                          RouterAnimation enter_animation, RouterAnimation exit_animation) {
        return this;
    }

    /**
     * execute the queued operations
     *
     * @return true if anything executed, false otherwise
     * @throws IllegalStateException if begin is not called
     * @deprecated since 1.1.0
     */
    @SuppressWarnings("UnusedReturnValue")
    @Deprecated
    public boolean commit() {
        return executePendingOperations();
    }

    /************************************************************
     *                   Transaction Methods                    *
     ***********************************************************/

    /**
     * Shows the {@code target} in the content pane returned by {@link #getContent()}
     * using the optional enter animation passed via {@code options}
     *
     * @param target  non-null subclass of {@link rahulstech.jfx.routing.Transaction.Target} to display.
     * @param options non-null {@link RouterOptions} for extra configuration .
     * @since 2.0
     * @see rahulstech.jfx.routing.backstack.Backstack#pushBackstackEntry(BackstackEntry)
     */
    @Override
    public void show(SingleSceneTarget target, RouterOptions options) {
        RouterAnimation animation = getAnimation(options.getEnterAnimation());
        target.showInContent(getContent(),animation,null);
        getBackstack().pushBackstackEntry(target);
    }

    /**
     * Shows the single-top target with tag in the content pane returned by {@link #getContent()}
     * using the optional enter animation passed via {@code options}.
     * <p>
     * It searches for the target with tag in the backstack,
     * if found then brings the entry at backstack top and shows it. If not found then creates a
     * new using the {@code supplier}, push to backstack and shows it.
     *
     * @param tag      the unique tag identifying the target.
     * @param supplier a non-null {@link Supplier} that provides the target instance if it does not exist.
     * @param options  non-null {@code RouterOptions} for extra configuration
     * @since 2.0
     * @see rahulstech.jfx.routing.backstack.Backstack#bringToTop(BackstackEntry)
     * @see rahulstech.jfx.routing.backstack.Backstack#pushBackstackEntry(BackstackEntry)
     */
    @Override
    public void showSingleTop(String tag, Supplier<SingleSceneTarget> supplier, RouterOptions options) {
        RouterAnimation animation = getAnimation(options.getEnterAnimation());
        Optional<Target> optional = getBackstack().findFirst(e->e.getTag().equals(tag));
        if (optional.isPresent()) {
            SingleSceneTarget target = (SingleSceneTarget) optional.get();
            target.showInContent(getContent(),animation,null);
            getBackstack().bringToTop(target);
        }
        else {
            SingleSceneTarget target = supplier.get();
            target.showInContent(getContent(),animation,null);
            getBackstack().pushBackstackEntry(target);
        }
    }

    /**
     * Shows the single-top target with tag in the content pane returned by {@link #getContent()}
     * using the optional pop enter animation passed via options.
     * <p>
     * It shows the target if and only if a target with tag is found.
     *
     * @param tag     the unique tag identifying the target to display.
     * @param options non-null {@code RouterOptions} for extra configuration
     * @since 2.0
     * @see rahulstech.jfx.routing.backstack.Backstack#findFirst(Predicate)
     */
    @Override
    public void popShow(String tag, RouterOptions options) {
        getBackstack().findFirst(entry->entry.getTag().equals(tag))
                .ifPresent(entry->{
                    RouterAnimation animation = getAnimation(options.getPopEnterAnimation());
                    SingleSceneTarget target = (SingleSceneTarget) entry;
                    target.showInContent(getContent(),animation,null);
                });
    }

    /**
     * Hides the target with tag from the content pane returned by {@link #getContent()}
     * using optional exit animation returned by {@code options}
     * <p>
     * It hides the target if and only if a target with tag is found.
     *
     * @param tag     the unique tag identifying the target to hide.
     * @param options non-null {@code RouterOptions} for extra configuration
     * @since 2.0
     * @see rahulstech.jfx.routing.backstack.Backstack#findFirst(Predicate)
     */
    @Override
    public void hide(String tag, RouterOptions options) {
        getBackstack()
                .findFirst(entry->entry.getTag().equals(tag))
                .ifPresent(entry->{
                    RouterAnimation animation = getAnimation(options.getExitAnimation());
                    SingleSceneTarget target = (SingleSceneTarget) entry;
                    target.hideFromContent(getContent(),animation,null);
                });
    }

    /**
     * Hides the target with tag from the content pane returned by {@link #getContent()}
     * using optional pop exit animation returned by {@code options} and destroys
     * <p>
     * It hides the target if and only if a target with tag is found.
     *
     * @param tag     the unique tag identifying the target to hide.
     * @param options nullable {@code RouterOptions} for extra configuration
     * @since 2.0
     * @see rahulstech.jfx.routing.backstack.Backstack#findFirst(Predicate)
     */
    @Override
    public void popHide(String tag, RouterOptions options) {
        if (getBackstack().isEmpty()) {
            return;
        }
        getBackstack().popBackstackEntryIf(entry->entry.getTag().equals(tag))
                .ifPresent(entry->{
                    RouterAnimation animation = getAnimation(options.getPopExitAnimation());
                    SingleSceneTarget target = (SingleSceneTarget) entry;
                    target.doDestroy(getContent(),animation,null);
                });
    }

    /**
     * Returns {@link RouterAnimation} byt name  or id from {@link RouterContext}
     *
     * @param nameOrId the animation name or id
     * @return {@code RouterAnimation} for the given animation name or id,
     * or {@link RouterAnimation#getNoOpAnimation() NO_OP} animation if not found
     * @since 2.0
     * @see RouterContext#getAnimation(String)
     */
    public RouterAnimation getAnimation(String nameOrId) {
        RouterContext context = getRouterContext();
        if (StringUtil.isEmpty(nameOrId)) {
            return RouterAnimation.getNoOpAnimation();
        }
        RouterAnimation animation = context.getAnimation(nameOrId);
        if (null==animation) {
            return RouterAnimation.getNoOpAnimation();
        }
        return animation;
    }

    ////////////////////////////////////////////////////////////////////////
    //                       Lifecycle Methods                           //
    //////////////////////////////////////////////////////////////////////

    /** {@inheritDoc} */
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

        private boolean singleTop;

        private RouterAnimation cachedAnimation;

        /**
         * Create new {@code SingleSceneTarget} instance with {@code tag} and
         * {@code controller} instance
         *
         * @param tag uniquely identify a target in backstack by tag
         * @param controller controller instance to handle lifecycle states
         */
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
         * Sets whether this target is single-top. For a single-top target, backstack will hold at most one instance.
         *
         * @param singleTop {@code true} means single-top, {@code false} is not single-top
         */
        public void setSingleTop(boolean singleTop) {
            this.singleTop = singleTop;
        }

        /**
         * Returns whether this target is single-top
         *
         * @return {@code true} means single-top, {@code false} is not single-top
         */
        public boolean isSingleTop() {
            return singleTop;
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
         * Adds screen node to the content screen as child, performs animations, handles lifecycle methods
         *
         * @param content instance of {@link Pane} to add the screen node as child
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
         * Removes screen node from the content screen, performs animation, handles lifecycle hide
         *
         * @param content {@link Pane} to remove the screen node
         * @param exit_animation non-null instance of {@link RouterAnimation}
         * @param OnHide nullable {@link Consumer} to call on screen hides
         */
        public void hideFromContent(Pane content, RouterAnimation exit_animation, Consumer<SingleSceneTarget> OnHide) {
            Node front = getNode();

            if (!isInContent(content,front)) {
                return;
            }

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
         * Removes the screen node from the content screen, performs animation, and finally does destroy the lifecycle
         *
         * @param content {@link Pane} to remove the screen node
         * @param exit_animation non-null instance of {@link RouterAnimation}
         * @param OnDestroy nullable {@link Consumer} to call on screen destroys
         */
        public void doDestroy(Pane content, RouterAnimation exit_animation, Consumer<SingleSceneTarget> OnDestroy) {
            hideFromContent(content,exit_animation,target->{
                target.onDestroy();
                if (null!=OnDestroy) {
                    OnDestroy.accept(target);
                }
            });
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
         * Checks weather the node is added as child to the parent
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
