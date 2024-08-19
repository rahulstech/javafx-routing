module singlescenedemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires rahulstech.jfx.routing;

    opens rahulstech.jfx.singlescenedemo to javafx.fxml;
    exports rahulstech.jfx.singlescenedemo;
}