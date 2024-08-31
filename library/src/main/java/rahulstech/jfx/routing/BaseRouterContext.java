package rahulstech.jfx.routing;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.element.animation.*;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;
import rahulstech.jfx.routing.routerexecutor.SingleSceneScreenExecutor;
import rahulstech.jfx.routing.transaction.SingleSceneTransaction;
import rahulstech.jfx.routing.util.ReflectionUtil;
import rahulstech.jfx.routing.util.StringUtil;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

import static rahulstech.jfx.routing.Router.KEY_SINGLE_SCENE_SCREEN_EXECUTOR;

/**
 * The {@code BaseRouterContext} class extends the {@link RouterContext} to provide
 * concrete implementations for handling animations, resource loading, and managing
 * router executors and transactions.
 *
 * <p>This class also includes caching mechanisms for animations, router executors,
 * and transactions to optimize performance and resource usage.</p>
 *
 * @see RouterContext
 * @see RouterAnimation
 * @see RouterExecutor
 * @see Transaction
 * @since 1.0
 * @author Rahul Bagchi
 */
public abstract class BaseRouterContext extends RouterContext {

    /**
     * Creates new {@code BaseRouterContext} instance
     */
    public BaseRouterContext() {
        super();
    }
    
    /////////////////////////////////////////////////////////////////
    //              RouterAnimation Related Methods               //
    ///////////////////////////////////////////////////////////////

    private static final String PREFIX_ANIMATION_ATTRIBUTES = "--animation-attributes-";

    private static final String PREFIX_ANIMATION_NAME = "--animation-name-";

    private void cacheAnimationAttributes(String id, AttributeSet attrs) {
        addToCache(PREFIX_ANIMATION_ATTRIBUTES+id,attrs);
    }

    private AttributeSet getCachedAnimationAttributes(String id) {
        return (AttributeSet) getFromCache(PREFIX_ANIMATION_ATTRIBUTES+id);
    }

    private void cacheAnimationName(String id, String name) {
        addToCache(PREFIX_ANIMATION_NAME+id,name);
    }

    private String getCachedAnimationName(String id) {
        return (String) getFromCache(PREFIX_ANIMATION_NAME+id);
    }

    /**
     * Adds an {@link AttributeSet} to the animation attribute set cache.
     *
     * @param attrs the attribute set to add
     */
    public void addAnimationAttributeSet(AttributeSet attrs) {
        Attribute idAttr = attrs.get(Attribute.ID);
        Attribute nameAttr = attrs.get(Attribute.NAME);
        String id = idAttr.getValue();
        String name = nameAttr.getValue();
        cacheAnimationName(id,name);
        cacheAnimationAttributes(id,attrs);
    }

    /** {@inheritDoc} */
    @Override
    public void addAllAnimationAttributeSet(Collection<AttributeSet> attrs) {
        attrs.forEach(this::addAnimationAttributeSet);
    }

    /** {@inheritDoc} */
    @Override
    public RouterAnimation getAnimation(String nameOrId) {
        String name = getCachedAnimationName(nameOrId);
        if (null==name) {
            return getAnimationByName(nameOrId,null);
        }
        AttributeSet attrs = getCachedAnimationAttributes(nameOrId);
        return getAnimationByName(name,attrs);
    }

    /**
     * Retrieves a {@link RouterAnimation} based on its name and initializes it with the provided attributes.
     *
     * @param name the name of the animation
     * @param attrs the attributes to initialize the animation with
     * @return the corresponding {@link RouterAnimation} instance
     */
    protected RouterAnimation getAnimationByName(String name, AttributeSet attrs) {
        RouterAnimation animation;
        switch (name) {
            case RouterAnimation.NO_OP: animation =  RouterAnimation.getNoOpAnimation();
            break;

            // fade animations
            case FadeAnimation.FADE_IN: animation = FadeAnimation.getFadeIn();
            break;
            case FadeAnimation.FADE_OUT: animation = FadeAnimation.getFadeOut();
            break;

            // scale animations
            case ScaleAnimation.SCALE_UP_X: animation = ScaleAnimation.getScaleUpX();
            break;
            case ScaleAnimation.SCALE_DOWN_X: animation = ScaleAnimation.getScaleDownX();
            break;
            case ScaleAnimation.SCALE_UP_Y: animation = ScaleAnimation.getScaleUpY();
            break;
            case ScaleAnimation.SCALE_DOWN_Y: animation = ScaleAnimation.getScaleDownY();
            break;
            case ScaleAnimation.SCALE_UP_XY: animation = ScaleAnimation.getScaleUpXY();
            break;
            case ScaleAnimation.SCALE_DOWN_XY: animation = ScaleAnimation.getScaleDownXY();
            break;

            // slide animations
            case SlideAnimation.SLIDE_IN_LEFT: animation = SlideAnimation.getSlideInLeft();
            break;
            case SlideAnimation.SLIDE_OUT_LEFT: animation = SlideAnimation.getSlideOutLeft();
            break;
            case SlideAnimation.SLIDE_IN_RIGHT: animation = SlideAnimation.getSlideInRight();
            break;
            case SlideAnimation.SLIDE_OUT_RIGHT: animation = SlideAnimation.getSlideOutRight();
            break;
            case SlideAnimation.SLIDE_IN_TOP: animation = SlideAnimation.getSlideInTop();
            break;
            case SlideAnimation.SLIDE_IN_BOTTOM: animation = SlideAnimation.getSlideInBottom();
            break;
            case SlideAnimation.SLIDE_OUT_TOP: animation = SlideAnimation.getSlideOutTop();
            break;
            case SlideAnimation.SLIDE_OUT_BOTTOM: animation = SlideAnimation.getSlideOutBottom();
            break;

            // scale and fade animations
            case ScaleAndFadeAnimation.SCALE_UP_XY_FADE_IN: animation = ScaleAndFadeAnimation.getScaleUpXYFadeIn();
            break;
            case ScaleAndFadeAnimation.SCALE_DOWN_XY_FADE_OUT: animation = ScaleAndFadeAnimation.getScaleDownXYFadeOut();
            break;

            // slide fade
            case SlideAndFadeAnimation.SLIDE_LEFT_FADE_IN: animation = SlideAndFadeAnimation.getSlideLeftFadeIn();
            break;
            case SlideAndFadeAnimation.SLIDE_RIGHT_FADE_IN: animation = SlideAndFadeAnimation.getSlideRightFadeIn();
            break;
            case SlideAndFadeAnimation.SLIDE_TOP_FADE_IN: animation = SlideAndFadeAnimation.getSlideTopFadeIn();
            break;
            case SlideAndFadeAnimation.SLIDE_BOTTOM_FADE_IN: animation = SlideAndFadeAnimation.getSlideBottomFadeIn();
            break;
            case SlideAndFadeAnimation.SLIDE_LEFT_FADE_OUT: animation = SlideAndFadeAnimation.getSlideLeftFadeOut();
            break;
            case SlideAndFadeAnimation.SLIDE_RIGHT_FADE_OUT: animation = SlideAndFadeAnimation.getSlideRightFadeOut();
            break;
            case SlideAndFadeAnimation.SLIDE_TOP_FADE_OUT: animation = SlideAndFadeAnimation.getSlideTopFadeOut();
            break;
            case SlideAndFadeAnimation.SLIDE_BOTTOM_FADE_OUT: animation = SlideAndFadeAnimation.getSlideBottomFadeOut();
            break;
            default: animation = null;
            break;
        }
        if (null!=animation && null!=attrs) {
            animation.initialize(attrs);
        }
        return animation;
    }

    /////////////////////////////////////////////////////////////////
    //                 FXMLLoader Related Methods                 //
    ///////////////////////////////////////////////////////////////

    /** {@inheritDoc} */
    @Override
    public FXMLLoader getFxmlLoader(String fxml, Class<?> controller, ResourceBundle bundle, Charset charset) {
        URL location = getResource(fxml);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        if (null!=bundle) {
            loader.setResources(bundle);
        }
        if (null!=charset) {
            loader.setCharset(charset);
        }
        if (null==controller) {
            loader.setControllerFactory(this::newControllerInstance);
        }
        else {
            Object instance = newControllerInstance(controller);
            loader.setController(instance);
        }
        return loader;
    }

    /////////////////////////////////////////////////////////////////
    //               RouterExecutor Related Methods               //
    ///////////////////////////////////////////////////////////////

    private static final String PREFIX_ROUTER_EXECUTOR = "--router-executor-";

    /**
     * Returns cached instance of {@link RouterExecutor}
     *
     * @param name name of the {@code RouterExecutor}
     * @return cached {@code RouterExecutor} instance or {@code null}
     */
    public RouterExecutor getCachedRouterExecutor(String name) {
        return (RouterExecutor) getFromCache(PREFIX_ROUTER_EXECUTOR+name);
    }

    /**
     * Caches a {@link RouterExecutor} instance
     *
     * @param name name of the {@code RouterExecutor}
     * @param executor instance of {@code RouterExector}
     */
    public void cacheRouterExecutor(String name, RouterExecutor executor) {
        if (StringUtil.isEmpty(name)) {
            throw new IllegalArgumentException("router executor name can not be empty");
        }
        if (null==executor) {
            throw new NullPointerException("router executor is null");
        }
        addToCache(PREFIX_ROUTER_EXECUTOR+name,executor);
    }

    /** {@inheritDoc} */
    @Override
    public final RouterExecutor getRouterExecutorForName(String name, Router router) {
        RouterExecutor cached = getCachedRouterExecutor(name);
        if (null!=cached) {
            return cached;
        }
        RouterExecutor executor = createRouterExecutor(name,router);
        cacheRouterExecutor(name,executor);
        return executor;
    }

    /** {@inheritDoc} */
    @Override
    public RouterExecutor getDefaultRouterExecutor(Router router) {
        return getRouterExecutorForName(Router.KEY_DEFAULT_ROUTER_EXECUTOR,router);
    }

    /**
     * Creates new {@link RouterExecutor} for name
     *
     * @param name {@code RouterExecutor} name
     * @param router associated {@code Router}
     * @return instance of {@code RouterExecutor} or {@code null} for unknown name
     */
    protected RouterExecutor createRouterExecutor(String name, Router router) {
        if (KEY_SINGLE_SCENE_SCREEN_EXECUTOR.equals(name)) {
            return new SingleSceneScreenExecutor(router,
                    (SingleSceneTransaction) getTransaction(SingleSceneTransaction.class, router.getContentPane()));
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////
    //                 Transaction Related Methods                //
    ///////////////////////////////////////////////////////////////

    private void cacheTransaction(Class<? extends Transaction> clazz, Transaction transaction) {
        addToCache(clazz,transaction);
    }

    private Transaction getCachedTransaction(Class<? extends Transaction> clazz) {
        return (Transaction) getFromCache(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public final Transaction getTransaction(Class<? extends Transaction> clazz, Object... args) {
        Transaction cached = getCachedTransaction(clazz);
        if (null!=cached) {
            return cached;
        }
        Transaction transaction = createNewTransaction(clazz,args);
        if (null==transaction) {
            throw new NullPointerException("no new transaction created of type '"+clazz+"' with arguments "+ Arrays.toString(args));
        }
        cacheTransaction(clazz,transaction);
        return transaction;
    }

    /**
     * Creates a new instance of {@link Transaction}. It uses {@link ReflectionUtil}
     * to create instance of {@code Transaction} subclass which is not defined in this
     * framework.
     *
     * @param clazz {@code Class} value of {@code Transaction} subclass
     * @param args constructor arguments
     * @return new instnace of {@code Transaction}
     * @throws RuntimeException throwns if instance is not created
     * @throws IllegalStateException throws if no suitable constructor found
     * @see ReflectionUtil#newInstance(Class, Object[])
     */
    protected Transaction createNewTransaction(Class<? extends Transaction> clazz, Object... args) {
        if (clazz==SingleSceneTransaction.class) {
            Pane content = (Pane) args[0];
            return new SingleSceneTransaction(content);
        }
        return (Transaction) ReflectionUtil.newInstance(clazz,args);
    }

    /////////////////////////////////////////////////////////////////
    //             Resource Loading Related Methods               //
    ///////////////////////////////////////////////////////////////

    /** {@inheritDoc} */
    @Override
    public URL getResource(String name) {
        return getResource(name,getResourceType(name));
    }

    /** {@inheritDoc} */
    @Override
    public InputStream getResourceAsStream(String name) {
        return getResourceAsStream(name,getResourceType(name));
    }

    /**
     * Returns resource type
     *
     * @param name resource name
     * @return non-null {@code String} as resource type name based on the resource extension name,
     *          or empty string if extension name does not exist
     */
    public String getResourceType(String name) {
        int extensionStart = name.lastIndexOf(".");
        if (extensionStart>=0){
            return name.substring(extensionStart+1);
        }
        return "";
    }

    /**
     * Returns resource location {@link URL}
     *
     * @param name resource name
     * @param type resource type
     * @return the {@link URL} of the resource associated with the specified name, or {@code null} if not found
     * @see #getResourceType(String)
     */
    public abstract URL getResource(String name, String type);

    /**
     * Returns resource as {@link InputStream}
     *
     * @param name resource name
     * @param type resource type
     * @return the {@link InputStream} of the resource associated with the specified name, or {@code null} if not found
     * @see #getResourceType(String)
     */
    public abstract InputStream getResourceAsStream(String name, String type);
}
