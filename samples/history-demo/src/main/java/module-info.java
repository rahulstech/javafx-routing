module historydemo {

    requires javafx.fxml;
    requires javafx.controls;

    requires rahulstech.jfx.routing;

    exports rahulstech.jfx.historydemo;

    opens rahulstech.jfx.historydemo to javafx.fxml;
}