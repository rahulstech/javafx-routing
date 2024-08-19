package rahulstech.jfx.routing.element;

import rahulstech.jfx.routing.parser.AttributeSet;

public class DialogDestination extends Destination{

    DialogDestination(Builder builder) {
        super(builder);
    }

    protected DialogDestination(Destination copy) {
        super(copy);
    }

    public DialogDestination(AttributeSet attrs) {
        super(attrs);
    }

    public static class Builder extends Destination.Builder {

        public Builder(String id) {
            super(id);
        }
    }
}
