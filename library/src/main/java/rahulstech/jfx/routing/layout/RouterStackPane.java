package rahulstech.jfx.routing.layout;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.StackPane;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.RouterContext;
import rahulstech.jfx.routing.RouterPane;

/**
 * The {@code RouterStackPane} class is an extension of {@link StackPane} that
 * implements the {@link RouterPane} interface, providing routing capabilities
 * for single scene application. This class uses a {@link RouterPaneDelegate} to
 * manage the routing logic, including the context class and router configuration.
 *
 * <p>The {@code RouterStackPane} allows you to define a routing context and
 * configuration via properties. These properties can be set to manage routing
 * behavior dynamically within the application.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * RouterStackPane routerPane = new RouterStackPane();
 * routerPane.setContextClass("com.example.MyRouterContext");
 * routerPane.setRouterConfig("path/to/router-config.xml");
 *
 * // or
 * RouterContext context = ...
 * RouterStackPane routerPane = new RouterStackPane();
 * routerPane.setContext(context);
 * routerPane.setRouterConfig("path/to/router-config.xml");
 *
 * }</pre>
 *
 * <p>Example usage in fxml:</p>
 * <pre>{@code
 * <?xml version="1.0" encoding="utf-8"?>
 *
 * <?import rahulstecch.jfx.routing.layout.RouterStackPane; ?>
 *
 * <RouterStackPane xmlns="http://javafx.com/javafx"
 *                  xmlns:fx="http://javafx.com/fxml"
 *                  fx:controller="com.example.MyController"
 *                  fx:id="my_pane"
 *                  routerConfig="path/to/router-config.xml"
 *                  contextClass="com.example.MyRouterContext"/>
 * }</pre>
 *
 * @see StackPane
 * @see RouterPane
 * @see RouterPaneDelegate
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class RouterStackPane extends StackPane implements RouterPane {

    private final RouterPaneDelegate delegate;

    public RouterStackPane() {
        super();
        delegate = new RouterPaneDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public StringProperty contextClassProperty() {
        return delegate.contextClassProperty();
    }

    /** {@inheritDoc} */
    @Override
    public void setContextClass(String className) {
        delegate.setContextClass(className);
    }

    /** {@inheritDoc} */
    @Override
    public String getContextClass() {
        return delegate.getContextClass();
    }

    /** {@inheritDoc} */
    @Override
    public void setContext(RouterContext context) {
        delegate.setContext(context);
    }

    /** {@inheritDoc} */
    @Override
    public RouterContext getContext() {
        return delegate.getContext();
    }

    /** {@inheritDoc} */
    @Override
    public StringProperty routerConfigProperty() {
        return delegate.routerConfigProperty();
    }

    /** {@inheritDoc} */
    @Override
    public void setRouterConfig(String xml) {
        delegate.setRouterConfig(xml);
    }

    /** {@inheritDoc} */
    @Override
    public String getRouterConfig() {
        return delegate.getRouterConfig();
    }

    /** {@inheritDoc} */
    @Override
    public ObjectProperty<Router> routerProperty() {
        return delegate.routerProperty();
    }

    /** {@inheritDoc} */
    @Override
    public Router getRouter() {
        return delegate.getRouter();
    }
}
