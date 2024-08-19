package rahulstech.jfx.routing.parser;

import javafx.util.Duration;
import rahulstech.jfx.routing.parser.converter.*;
import rahulstech.jfx.routing.util.Size;

import java.util.Objects;

@SuppressWarnings("unused")
public class Attribute {

    public static final int TYPE_CLASS = 1;

    public static final int TYPE_DURATION = 2;

    public static final int TYPE_NUMBER = 3;

    public static final int TYPE_SIZE = 5;

    public static final int TYPE_STRING = 6;

    public static final int TYPE_BOOLEAN = 7;

    // common attributes
    public static final String ID = "id";
    public static final  String NAME = "name";
    public static final String TYPE = "type";

    // attributes for Router
    public static final  String HOME = "home";
    public static final  String HOME_ENTER_ANIMATION = "homeEnterAnimation";
    public static final  String ENTER_ANIMATION = "enterAnimation";
    public static final  String EXIT_ANIMATION = "exitAnimation";
    public static final  String POP_ENTER_ANIMATION = "popEnterAnimation";
    public static final  String POP_EXIT_ANIMATION = "popExitAnimation";

    // attributes RouterAnimation
    public static final  String DURATION = "duration";
    public static final String AUTO_RESET = "autoReset";
    public static final String FROM_ALPHA = "fromAlpha";
    public static final String TO_ALPHA = "toAlpha";
    public static final  String FROM_X_SCALE = "fromXScale";
    public static final  String TO_X_SCALE = "toXScale";
    public static final  String FROM_Y_SCALE = "fromYScale";
    public static final  String TO_Y_SCALE = "toYScale";
    public static final  String FROM_X_TRANSLATE = "fromXTranslate";
    public static final  String TO_X_TRANSLATE = "toXTranslate";
    public static final  String FROM_Y_TRANSLATE = "fromYTranslate";
    public static final  String TO_Y_TRANSLATE = "toYTranslate";
    public static final  String FROM_X_ROTATION = "fromXRotation";
    public static final  String TO_X_ROTATION = "toXRotation";
    public static final  String FROM_Y_ROTATION = "fromYRotation";
    public static final  String TO_Y_ROTATION = "toYRotation";

    // attribute RouterCompoundAnimation
    public static final String PLAY_MODE = "playMode";

    // attributes for Destination
    public static final  String CONTROLLER_CLASS = "controllerClass";
    public static final  String EXECUTOR = "executor";
    public static final  String FXML = "fxml";
    public static final  String TITLE = "title";
    public static final String ARGUMENTS = "arguments";


    // attribute for argument
    public static final String REQUIRED = "required";

    private final String namespace;

    private final String name;

    private final String value;

    public Attribute(String name, String value) {
        this(RouterXmlParser.DEFAULT_NAMESPACE,name,value);
    }

    public Attribute(String namespace, String name, String value) {
        this.namespace = namespace;
        this.name = name;
        this.value = value;
    }

    public String getNamespace() {
        return namespace;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isDefaultNamespace() {
        return RouterXmlParser.DEFAULT_NAMESPACE.equals(namespace);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int getAsInt() {
        return NumberConverter.getInstance().convert(this).intValue();
    }

    public long getAsLong() {
        return NumberConverter.getInstance().convert(this).longValue();
    }

    public float getAsFloat() {
        return NumberConverter.getInstance().convert(this).floatValue();
    }

    public double getAsDouble() {
        return NumberConverter.getInstance().convert(this).doubleValue();
    }

    public Duration getAsDuration() {
        return DurationConverter.getInstance().convert(this);
    }

    public Size getAsSize() {
        return SizeConverter.getInstance().convert(this);
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getAsClass() {
        return (Class<T>) ClassConverter.getInstance().convert(this);
    }

    public boolean getAsBoolean() {
        return BooleanConverter.getInstance().convert(this);
    }

    public int getType() {
        int type;
        if (ClassConverter.getInstance().check(value)) {
            type = TYPE_CLASS;
        }
        else if (DurationConverter.getInstance().check(value)) {
            type = TYPE_DURATION;
        }
        else if (NumberConverter.getInstance().check(value)) {
            type = TYPE_NUMBER;
        }
        else if (SizeConverter.getInstance().check(value)) {
            type = TYPE_SIZE;
        }
        else if (BooleanConverter.getInstance().check(value)) {
            type = TYPE_BOOLEAN;
        }
        else {
            type = TYPE_STRING;
        }
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attribute)) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(namespace, attribute.namespace) && Objects.equals(name, attribute.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name, value);
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "namespace='" + namespace + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
