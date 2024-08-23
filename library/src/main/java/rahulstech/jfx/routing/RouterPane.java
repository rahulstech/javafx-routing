package rahulstech.jfx.routing;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 *
 * @see rahulstech.jfx.routing.layout.RouterStackPane RouterStackPane
 */
public interface RouterPane {

    StringProperty contextClassProperty();

    void setContextClass(String className);

    String getContextClass();

    void setContext(RouterContext context);

    RouterContext getContext();

    StringProperty routerConfigProperty();

    void setRouterConfig(String xml);

    String getRouterConfig();

    ObjectProperty<Router> routerProperty();

    Router getRouter();

    ObservableList<Node> getChildren();
}
