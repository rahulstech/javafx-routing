package rahulstech.jfx.routing.element;

import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.lifecycle.LifecycleAwareController;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;
import rahulstech.jfx.routing.util.StringUtil;

@SuppressWarnings({"unused","FieldMayBeFinal"})
public class Destination {

    private String id;
    private String fxml;
    private Class<? extends LifecycleAwareController> controllerClass;
    private String title;
    private String executor;
    private String arguments;

    Destination(Builder builder) {
        id = builder.id;
        fxml = builder.fxml;
        controllerClass = builder.controllerClass;
        title = builder.title;
        executor = builder.executor;
        arguments = builder.arguments;
    }

    protected Destination(Destination copy) {
        id = copy.id;
        fxml = copy.fxml;
        controllerClass = copy.controllerClass;
        title = copy.title;
        executor = copy.executor;
        arguments = copy.arguments;
    }

    public Destination(AttributeSet attrs) {
        if (attrs.beginIteration()) {
            while (attrs.iterateNext()) {
                Attribute attr = attrs.getNext();
                if (!attr.isDefaultNamespace()) {
                    continue;
                }
                switch (attr.getName()) {
                    case Attribute.ID: {
                        id = attr.getValue();
                    }
                    break;
                    case Attribute.CONTROLLER_CLASS: {
                        controllerClass = attr.getAsClass();
                    }
                    break;
                    case Attribute.EXECUTOR: {
                        executor = attr.getValue();
                    }
                    break;
                    case Attribute.FXML: {
                        fxml = attr.getValue();
                    }
                    break;
                    case Attribute.TITLE: {
                        title = attr.getValue();
                    }
                    break;
                    case Attribute.ARGUMENTS: {
                        arguments = attr.getValue();
                    }
                }
            }
        }
        if (StringUtil.isEmpty(executor)) {
            executor = Router.KEY_DEFAULT_ROUTER_EXECUTOR;
        }
    }

    public String getId() {
        return id;
    }

    public String getFXML() {
        return fxml;
    }

    public String getTitle() {
        return title;
    }

    public Class<? extends LifecycleAwareController> getControllerClass() {
        return controllerClass;
    }

    public String getExecutor() {
        return executor;
    }

    public String getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "Destination{" +
                "id='" + id + '\'' +
                ", fxml='" + fxml + '\'' +
                ", controllerClass=" + controllerClass +
                ", title='" + title + '\'' +
                ", executor='" + executor + '\'' +
                ", arguments='" + arguments + '\'' +
                '}';
    }

    public static class Builder {
        String id;
        String fxml;
        Class<? extends LifecycleAwareController> controllerClass;
        String title;
        String executor = Router.KEY_DEFAULT_ROUTER_EXECUTOR;
        String arguments;

        public Builder(String id) {
            if (null==id || id.trim().isEmpty()) {
                throw new IllegalArgumentException("empty destination id is not allowed");
            }
            this.id = id;
        }

        public Builder setFXML(String fxml) {
            this.fxml = fxml;
            return this;
        }

        public Builder setControllerClass(Class<? extends LifecycleAwareController> controllerClass) {
            this.controllerClass = controllerClass;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setExecutor(String executor) {
            this.executor = executor;
            return this;
        }

        public Builder setArguments(String arguments) {
            this.arguments = arguments;
            return this;
        }

        public Destination build() {
            return new Destination(this);
        }
    }
}
