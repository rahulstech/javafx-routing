module rahulstech.jfx.routing {
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive java.xml;

    opens rahulstech.jfx.routing.lifecycle to javafx.fxml;

    exports rahulstech.jfx.routing;
    exports rahulstech.jfx.routing.backstack;
    exports rahulstech.jfx.routing.lifecycle;
    exports rahulstech.jfx.routing.element;
    exports rahulstech.jfx.routing.element.animation;
    exports rahulstech.jfx.routing.parser;
    exports rahulstech.jfx.routing.parser.converter;
    exports rahulstech.jfx.routing.routerexecutor;
    exports rahulstech.jfx.routing.transaction;
    exports rahulstech.jfx.routing.util;
    exports rahulstech.jfx.routing.layout;

}