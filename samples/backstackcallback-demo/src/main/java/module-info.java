module demo.backstackcallback {
    requires javafx.controls;
    requires javafx.fxml;

    requires rahulstech.jfx.routing;

    exports rahulstech.jfx.routing.demo.backstackcallback;

    opens rahulstech.jfx.routing.demo.backstackcallback to javafx.fxml, javafx.graphics;
}