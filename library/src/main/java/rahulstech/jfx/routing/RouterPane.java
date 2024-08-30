package rahulstech.jfx.routing;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * The {@code RouterPane} interface represents a customizable pane
 * that can manage routing and navigation using a {@link Router}.
 * This interface provides methods to set and retrieve the routing context, router configuration,
 * and router instances. It also exposes properties for these attributes, allowing them to be
 * bound to other JavaFX components.
 *
 * <p>The {@code RouterPane} is a container for single scene screens,
 * provides dynamic loading and transitioning between different scenes.
 * The framework provides an implementation with {@link javafx.scene.layout.StackPane StackPane}.
 * One can easily implement custom {@code RouterPane} with
 * {@link rahulstech.jfx.routing.layout.RouterPaneDelegate RouterPaneDelegate}.
 * {@code RouterPane} implementation must be a subclass of {@link javafx.scene.layout.Pane Pane}
 * </p>
 *
 * <p>
 * <strong>Note:</strong> {@code RouterPane} must layout its children in stacked manner where only the top child is
 * visible. Therefore {@link javafx.scene.layout.VBox VBox}, {@link javafx.scene.layout.HBox HBox}
 * etc. are not good choice for implementing {@code RouterPane}
 * </p>
 *
 * @see Router
 * @see RouterContext
 * @see rahulstech.jfx.routing.layout.RouterStackPane RouterStackPane
 * @see rahulstech.jfx.routing.layout.RouterPaneDelegate RouterPaneDelegate
 * @author Rahul Bagchi
 * @since 1.0
 */
public interface RouterPane {

    /**
     * Gets the {@code StringProperty} representing the class name of the {@link RouterContext}.
     *
     * @return the {@code StringProperty} for the context class name
     */
    StringProperty contextClassProperty();

    /**
     * Sets the class name for the {@link RouterContext}.
     *
     * <p>This method allows you to dynamically specify the context class for the router,
     * which can then be used to manage routing in the application.</p>
     *
     * @param className the fully qualified name of the {@link RouterContext} class
     */
    void setContextClass(String className);

    /**
     * Gets the class name of the current {@link RouterContext}.
     *
     * <p>This method returns the fully qualified name of the context class used by the router.</p>
     *
     * @return the class name of the current {@link RouterContext}
     */
    String getContextClass();

    /**
     * Sets the {@link RouterContext} for the router pane.
     *
     * <p>This method allows you to directly set the context object used for routing,
     * which defines how routing operations are performed within the pane.</p>
     *
     * @param context the {@link RouterContext} to set
     */
    void setContext(RouterContext context);

    /**
     * Gets the current {@link RouterContext} of the router pane.
     *
     * <p>This method returns the context object that the router pane is currently using
     * to manage routing operations.</p>
     *
     * @return the current {@link RouterContext}
     */
    RouterContext getContext();

    /**
     * Gets the {@code StringProperty} representing the router configuration as an XML string.
     *
     * <p>This property can be used to bind the router configuration to other JavaFX components,
     * enabling dynamic updates to the router's configuration.</p>
     *
     * @return the {@code StringProperty} for the router configuration
     */
    StringProperty routerConfigProperty();

    /**
     * Sets the router configuration using an XML string.
     *
     * <p>This method allows you to specify the router's configuration in XML format,
     * which can define routes, transitions, and other routing-related settings.</p>
     *
     * @param xml the XML string representing the router configuration
     */
    void setRouterConfig(String xml);

    /**
     * Gets the router configuration as an XML string.
     *
     * <p>This method returns the current router configuration in XML format,
     * which is used to manage routing within the pane.</p>
     *
     * @return the XML string representing the current router configuration
     */
    String getRouterConfig();

    /**
     * Gets the {@code ObjectProperty} representing the {@link Router} instance.
     *
     * <p>This property can be used to bind the router instance to other JavaFX components,
     * allowing for dynamic interaction with the router.</p>
     *
     * @return the {@code ObjectProperty} for the router
     */
    ObjectProperty<Router> routerProperty();

    /**
     * Gets the current {@link Router} instance associated with the pane.
     *
     * <p>This method returns the {@link Router} instance that manages routing operations
     * within the pane.</p>
     *
     * @return the current {@link Router} instance
     */
    Router getRouter();

    /**
     * Gets the list of child nodes managed by the router pane.
     *
     * <p>This method returns an observable list of {@link Node} objects, representing the
     * children of the router pane. This list can be used to add or remove nodes dynamically
     * based on routing operations.</p>
     *
     * @return an observable list of child nodes
     */
    ObservableList<Node> getChildren();
}
