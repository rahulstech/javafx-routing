package rahulstech.jfx.nestedrouterdemo;

import javafx.scene.paint.Color;

import java.util.Random;

public class Utils {

    static final Color[] COLORS = new Color[]{
            Color.HOTPINK, Color.AQUA, Color.LIMEGREEN,
            Color.MINTCREAM, Color.GREENYELLOW, Color.HOTPINK,
            Color.CORAL, Color.CYAN, Color.AQUAMARINE
    };

    public static Color getRandomColor() {
        int index = new Random().nextInt(COLORS.length);
        return COLORS[index];
    }
}
