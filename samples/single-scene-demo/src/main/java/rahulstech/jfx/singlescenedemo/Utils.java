package rahulstech.jfx.singlescenedemo;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class Utils {

    public static Background colorFillBackground(Color color) {
        return new Background(new BackgroundFill(color,null,null));
    }
}
