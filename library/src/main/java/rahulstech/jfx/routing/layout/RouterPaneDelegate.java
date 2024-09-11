package rahulstech.jfx.routing.layout;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.RouterContext;
import rahulstech.jfx.routing.util.ReflectionUtil;
import rahulstech.jfx.routing.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * The {@code RouterPaneDelegate} class manages a {@link Pane} within a routing context.
 * It facilitates the initialization and lifecycle management of a {@link Router} based
 * on changes in the attached {@link Scene} and {@link Parent} of the wrapped pane.
 *
 * <p>This class enables configuration of a router using an XML file and a custom
 * {@link RouterContext} implementation. The router is initialized, and its lifecycle
 * methods are triggered automatically when the pane's scene or parent changes.
 *
 * <p>Example usage:
 * <pre>{@code
 * RouterPaneDelegate delegate = new RouterPaneDelegate(myPane);
 * delegate.setRouterConfig("path/to/router-config.xml");
 * delegate.setContextClass("com.example.MyRouterContext");
 * }</pre>
 *
 * @author Rahul Bagchi
 * @since 1.0.0
 */
public class RouterPaneDelegate {

    private final Pane wrapped;

    private RouterContext context;

    private WeakReference<Scene> lastSceneRef = new WeakReference<>(null);

    private WeakReference<Parent> lastParentRef = new WeakReference<>(null);

    /*************************************************************
     *                      Constructor                          *
     ************************************************************/

    /**
     * Constructs a new {@code RouterPaneDelegate} with the specified {@link Pane}.
     *
     * @param wrapped the {@link Pane} to be managed by this delegate
     *
     * @throws NullPointerException if the wrapped {@link Pane} is null
     */
    public RouterPaneDelegate(Pane wrapped) {
        if (null==wrapped) {
            throw new NullPointerException("Pane is null");
        }
        this.wrapped = wrapped;
        initialize();
    }

    /*************************************************************
     *                      Properties                           *
     ************************************************************/

    /**
     * The {@code routerConfig} property specifies the path to the XML configuration
     * file for the {@link Router}.
     */
    private final StringProperty routerConfig = new SimpleStringProperty(this, "routerConfig");

    public StringProperty routerConfigProperty() {
        return routerConfig;
    }

    /**
     * Gets the value of the {@code routerConfig} property, representing the path
     * to the router configuration XML file.
     *
     * @return the router configuration XML file path
     */
    public String getRouterConfig() {
        return routerConfig.getValue();
    }

    /**
     * Sets the value of the {@code routerConfig} property, specifying the path
     * to the router configuration XML file.
     *
     * @param xml the path to the router configuration XML file
     */
    public void setRouterConfig(String xml) {
        routerConfig.setValue(xml);
    }

    /**
     * The {@code contextClass} property specifies the fully qualified class name
     * of the {@link RouterContext} implementation to be used.
     */
    private final StringProperty contextClass = new SimpleStringProperty(this, "contextClass");

    public final StringProperty contextClassProperty() {
        return contextClass;
    }

    /**
     * Sets the value of the {@code contextClass} property, specifying the fully
     * qualified class name of the {@link RouterContext} implementation.
     *
     * @param className the fully qualified class name of the {@link RouterContext} implementation
     */
    public void setContextClass(String className) {
        contextClass.setValue(className);
    }

    /**
     * Gets the value of the {@code contextClass} property, representing the fully
     * qualified class name of the {@link RouterContext} implementation.
     *
     * @return the fully qualified class name of the {@link RouterContext} implementation
     */
    public String getContextClass() {
        return contextClass.getValue();
    }

    /**
     * The {@code routerProperty} represents the {@link Router} instance currently in use.
     * This property is updated whenever a new {@link Router} is initialized.
     *
     * @return the property representing the current {@link Router} instance
     */
    private final ObjectProperty<Router> routerProperty = new SimpleObjectProperty<>(this,"router",null);

    /**
     * Gets the {@code routerProperty}.
     *
     * @return the router property
     */
    public final ObjectProperty<Router> routerProperty() {
        return routerProperty;
    }

    /**
     * Sets the {@code routerProperty} value, which represents the current {@link Router} instance.
     *
     * @param router the {@link Router} instance to set
     */
    public void setRouter(Router router) {
        routerProperty.setValue(router);
    }

    /**
     * Gets the {@code Router} instance currently in use.
     *
     * @return the current {@link Router} instance
     */
    public Router getRouter() {
        return routerProperty.getValue();
    }

    /*************************************************************
     *                    Public Methods                         *
     ************************************************************/

    /**
     * Returns the {@link Pane} managed by this {@code RouterPaneDelegate}.
     *
     * @return the {@link Pane} managed by this delegate
     */
    public Pane getWrapped() {
        return wrapped;
    }

    /**
     * Sets the {@link RouterContext} for this {@code RouterPaneDelegate}.
     *
     * @param context the {@link RouterContext} to set
     */
    public void setContext(RouterContext context) {
        this.context = context;
    }

    /**
     * Gets the {@link RouterContext} used by this {@code RouterPaneDelegate}.
     *
     * @return the current {@link RouterContext}
     */
    public RouterContext getContext() {
        return context;
    }

    /*************************************************************
     *                      Private Methods                      *
     ************************************************************/

    /**
     * Initializes the {@code RouterPaneDelegate}. This method sets up listeners on the
     * {@link Pane}'s {@link Scene} and {@link Parent} properties to trigger the
     * router's lifecycle methods when necessary. It also sets up listeners on the
     * {@code contextClass} property to create a new {@link RouterContext} when the class name changes.
     */
    private void initialize() {
        wrapped.sceneProperty().addListener((observable, oldValue, newValue) -> begin());
        contextClass.addListener((observable, oldValue, newValue) -> {
            if (StringUtil.isEmpty(newValue)) {
                setContext(null);
            } else {
                setContext((RouterContext) ReflectionUtil.newInstance(newValue));
            }
        });
    }

    /**
     * Initializes a new {@link Router} based on the current configuration. The router is
     * created using the XML configuration file specified by {@link #getRouterConfig()}
     * and the {@link RouterContext} specified by {@link #getContextClass()}.
     *
     * @return the initialized {@link Router}, or {@code null} if initialization fails
     */
    private Router initRouter() {
        String xml = getRouterConfig();
        RouterContext context = this.context;
        if (null!=context) {
            if (!StringUtil.isEmpty(xml)) {
                Router router = new Router(context, wrapped);
                try (InputStream in = context.getRouterConfigurationAsStrem(xml)) {
                    router.parse(in);
                    return router;
                } catch (IOException e) {
                    throw new RuntimeException("Failed to parse router configuration '" + xml + "' with exception: " + e.getMessage(), e);
                }
            }
            else {
                return new Router(context,getWrapped());
            }
        }
        return null;
    }

    /**
     * Begins the lifecycle of the {@link Router} associated with this {@code RouterPaneDelegate}.
     * This method is called when the {@link Pane}'s {@link Scene} or {@link Parent} changes.
     *
     * <p>If the {@link Pane} is removed from the scene, the router's {@link Router#doLifecycleHide()}
     * method is called. If the pane is reattached to the same scene and parent, the router's
     * {@link Router#doLifecycleShow()} method is called. If the pane is attached to a new
     * scene or parent, the old router is disposed, and a new router is created and initialized.</p>
     *
     * @see Router#doLifecycleHide()
     * @see Router#doLifecycleShow()
     */
    private void begin() {
        Scene scene = wrapped.getScene();
        Parent parent = wrapped.getParent();
        Scene oldScene = lastSceneRef.get();
        Parent oldParent = lastParentRef.get();
        Router old = getRouter();

        // if it is removed from scene , the trigger lifecycle hide
        if (null == scene) {
            if (null != old) {
                old.doLifecycleHide();
            }
            return;
        }

        // if it is removed first then attached to the same scene or parent then trigger lifecycle show
        if (scene == oldScene && parent == oldParent) {
            if (null != old) {
                old.doLifecycleShow();
            }
            return;
        }

        // if it is added to a different scene or parent then destroy the old router and create a new

        lastSceneRef = new WeakReference<>(scene);
        lastParentRef = new WeakReference<>(parent);

        if (null != old) {
            old.dispose();
        }
        Router router = initRouter();
        setRouter(router);
        if (null != router) {
            router.begin();
        }
    }
}
