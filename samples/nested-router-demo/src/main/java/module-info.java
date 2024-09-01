module nestedrouterdemo {

    requires javafx.fxml;
    requires javafx.controls;

    requires rahulstech.jfx.routing;

    exports rahulstech.jfx.nestedrouterdemo;

    opens rahulstech.jfx.nestedrouterdemo to javafx.fxml;
}