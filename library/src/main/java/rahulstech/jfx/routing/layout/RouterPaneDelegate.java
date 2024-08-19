package rahulstech.jfx.routing.layout;

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

@SuppressWarnings("unused")
public class RouterPaneDelegate {

    private final Pane wrapped;

    private Router router;

    private RouterContext context;

    private WeakReference<Scene> lastSceneRef = new WeakReference<>(null);

    private WeakReference<Parent> lastParentRef = new WeakReference<>(null);

    /*************************************************************
     *                      Properties                           *
     ************************************************************/

    private final StringProperty routerConfig = new SimpleStringProperty(null, "routerConfig");
    private final StringProperty contextClass = new SimpleStringProperty(null, "contextClass");

    public RouterPaneDelegate(Pane wrapped) {
        if (null==wrapped) {
            throw new NullPointerException("Pane is null");
        }
        this.wrapped = wrapped;
    }

    public StringProperty routerConfigProperty() {
        return routerConfig;
    }

    public String getRouterConfig() {
        return routerConfig.getValue();
    }

    public void setRouterConfig(String xml) {
        routerConfig.setValue(xml);
    }

    public final StringProperty contextClassProperty() {
        return contextClass;
    }

    public void setContextClass(String className) {
        contextClass.setValue(className);
    }

    public String getContextClass() {
        return contextClass.getValue();
    }

    /*************************************************************
     *                    Public Methods                        *
     ************************************************************/

    public void initialize() {
        wrapped.sceneProperty().addListener((observable, oldValue, newValue) -> begin());
        contextClass.addListener((observable, oldValue, newValue) -> {
            if (StringUtil.isEmpty(newValue)) {
                setContext(null);
            } else {
                setContext((RouterContext) ReflectionUtil.newInstance(newValue));
            }
        });
    }

    public Pane getWrapped() {
        return wrapped;
    }

    public Router getRouter() {
        return router;
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

    private void begin() {
        Scene scene = wrapped.getScene();
        Parent parent = wrapped.getParent();
        Scene oldScene = lastSceneRef.get();
        Parent oldParent = lastParentRef.get();
        Router old = this.router;

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
        this.router = router;
    }
}