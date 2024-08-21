package rahulstech.jfx.routing;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

@SuppressWarnings("unused")
public interface RouterPane {

    StringProperty contextClassProperty();

    void setContextClass(String className);

    String getContextClass();

    void setContext(RouterContext context);

    RouterContext getContext();

    StringProperty routerConfigProperty();

    void setRouterConfig(String xml);

    String getRouterConfig();

    Router getRouter();

    ObservableList<Node> getChildren();
}
