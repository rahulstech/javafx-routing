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
 * methods are triggered automatically when the pane's scene or parent changes.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * RouterPaneDelegate delegate = new RouterPaneDelegate(myPane);
 * delegate.setRouterConfig("path/to/router-config.xml");
 * delegate.setContextClass("com.example.MyRouterContext");
 * }</pre>
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
     * Set the router configuration xml file path in fxml. The InputStream of the configuration file
     * will be obtained from {@link RouterContext#getResourceAsStream(String) getResourceAsStrem(String)}.
     * Therefor you should set a value such that getResourceAsStream(String) method can understand.
     */
    private final StringProperty routerConfig = new SimpleStringProperty(this, "routerConfig");

    public StringProperty routerConfigProperty() {
        return routerConfig;
    }

    public String getRouterConfig() {
        return routerConfig.getValue();
    }

    public void setRouterConfig(String xml) {
        routerConfig.setValue(xml);
    }

    /**
     * Set the full class name of your {@link RouterContext} implementation.
     */
    private final StringProperty contextClass = new SimpleStringProperty(this, "contextClass");

    public final StringProperty contextClassProperty() {
        return contextClass;
    }

    public void setContextClass(String className) {
        contextClass.setValue(className);
    }

    public String getContextClass() {
        return contextClass.getValue();
    }

    /**
     * The {@link Router} instance currently in used
     *
     * @see #begin()
     */
    private final ObjectProperty<Router> routerProperty = new SimpleObjectProperty<>(this,"",null);

    public final ObjectProperty<Router> routerProperty() {
        return routerProperty;
    }

    public void setRouter(Router router) {
        routerProperty.setValue(router);
    }

    public Router getRouter() {
        return routerProperty.getValue();
    }

    /*************************************************************
     *                    Public Methods                        *
     ************************************************************/

    /**
     * @return the {@link rahulstech.jfx.routing.RouterPane RouterPane} managed by
     *          this delegate
     */
    public Pane getWrapped() {
        return wrapped;
    }

    public void setContext(RouterContext context) {
        this.context = context;
    }

    public RouterContext getContext() {
        return context;
    }

    /*************************************************************
     *                      Private Methods                     *
     ************************************************************/

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

    private Router initRouter() {
        String xml = getRouterConfig();
        RouterContext context = this.context;
        if (!StringUtil.isEmpty(xml) && null != context) {
            Router router = new Router(context, wrapped);
            try (InputStream in = context.getResourceAsStream(xml)) {
                router.parse(in);
                return router;
            } catch (IOException e) {
                throw new RuntimeException("fail to parse router configuration '" + xml + "' with exception " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * This method is called in two cases: the {@link  Scene} where it is
     * attached to changes and its {@link Parent} changes. On detached from
     * its Scene or Parent triggers {@link Router#doLifecycleHide() Lifecyle Hide}
     * on old Router. On reattached to the same Scene and Parent simply triggers
     * {@link Router#doLifecycleShow() Lifecycle Show} on old Router. But
     * if this RouterPane is attached to a new Scene or Parent then old Router is
     * disposed and a new Router is created.
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
        if (null != router) {
            router.begin();
        }
        setRouter(router);
    }
}