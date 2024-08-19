module basicdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires rahulstech.jfx.routing;

    opens rahulstech.jfx.routing.demo to javafx.fxml;
    exports rahulstech.jfx.routing.demo;
}