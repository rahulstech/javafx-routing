package rahulstech.jfx.routing.layout;

import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.RouterContext;
import rahulstech.jfx.routing.RouterPane;

public class RouterStackPane extends StackPane implements RouterPane {

    private final RouterPaneDelegate delegate;

    public RouterStackPane() {
        super();
        delegate = new RouterPaneDelegate(this);
        delegate.initialize();
    }

    @Override
    public StringProperty contextClassProperty() {
        return delegate.contextClassProperty();
    }

    @Override
    public void setContextClass(String className) {
        delegate.setContextClass(className);
    }

    @Override
    public String getContextClass() {
        return delegate.getContextClass();
    }

    @Override
    public void setContext(RouterContext context) {
        delegate.setContext(context);
    }

    @Override
    public RouterContext getContext() {
        return delegate.getContext();
    }

    @Override
    public StringProperty routerConfigProperty() {
        return delegate.routerConfigProperty();
    }

    @Override
    public void setRouterConfig(String xml) {
        delegate.setRouterConfig(xml);
    }

    @Override
    public String getRouterConfig() {
        return delegate.getRouterConfig();
    }

    @Override
    public Router getRouter() {
        return delegate.getRouter();
    }

    @Override
    public Pane getPane() {
        return delegate.getWrapped();
    }
}
