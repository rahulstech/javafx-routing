package rahulstech.jfx.routing.element;

import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;
import rahulstech.jfx.routing.util.StringUtil;

/**
 * {@code Destination} is the basic building block of routing. {@link Router} navigates among
 * different destinations. Destination defines how the screen will be created but does
 * not hold the state of the screen. Each destination is identified by its unique id within
 * the router. Destination must have at least <b>fxml</b> or <b>controllerClass</b> to set
 * fxml layout resource file location or controller class full name. If both the values available
 * then what will be prioritized is completely depends on {@link rahulstech.jfx.routing.RouterExecutor RouterExcutor}
 * implementation. Set the <b>executor</b> that handles a particular type of destination. If executor value
 * explicitly not set then default executor {@link Router#KEY_DEFAULT_ROUTER_EXECUTOR default executor} will
 * be used. Sometime destination may require data with certain data type. Set the destination input data rules using
 * <b>arguments</b>. You are not restricted to set only those data for which rules are defined; but you must set
 * arguments which required is {@code true}.
 * <p>
 * <p>Below is the example of declaring destination in router configuration xml file</p>
 * <pre> {@code
 *     <arguments id="args0">
 *          <argument name="xyz" type="string" />
 *          <argument name="abc" type="int" required="true"/>
 *     </arguments>
 *
 *     <destination id="screen0"
 *                     fxml="screen_zero.fxml"
 *                     controllerClass="com.example.controller.MyController"
 *                     executor="com.example.executor.MyExecutor"
 *                     title="This is Screen0"
 *                     arguments="args0"/>
 *     or
 *
 *    <destination id="screen0"
 *                    fxml="screen_zero.fxml"
 *                    controllerClass="com.example.controller.MyController"
 *                    executor="com.example.executor.MyExecutor"
 *                    title="This is Screen0">
 *
 *          <!-- arguments defined inside "destination" does not require "id" attribute -->
 *          <!-- you can not define multiple "arguments" tags inside "destination" -->
 *           <arguments&gt;
 *              <argument name="xyz" type="string" />
 *              <argument name="abc" type="int" required="true" />
 *           </arguments>
 *    </destination>
 *   }
 * </pre>
 *
 * <p>Create {@code Destination} in java code as below</p>
 * <pre>{@code
 *
 *     Router router = ...
 *
 *     RouterArgument args0 = new RouterArgument();
 *     args0.addArgument(new NameValue("xyz",Type.STRING,false));
 *     args0.addArgument(new NameValue("abc",Type.INT,true));
 *
 *     router.addArgument("args0",args0);
 *
 *     Destination destination = new Destination.Builder("screen0")
 *                               .setFxml("screen_zero.fxml")
 *                               .setControllerClass(MyController.class)
 *                               .setExecutor("com.example.executor.MyExecutor") // omit this line to use default executor
 *                               .setTitle("This is screen")
 *                               .setArgument("args0")
 *                               .build();
 *      router.addDestination(destination);
 *
 *      or
 *
 *      Router router = ...
 *
 *      RouterArgument args0 = new RouterArgument();
 *      args0.addArgument(new NameValue("xyz",Type.STRING,false));
 *      args0.addArgument(new NameValue("abc",Type.INT,true));
 *
 *      router.addArgument("args0",args0);
 *
 *      Destination destination = new Destination.Builder("screen0")
 *                               .setFxml("screen_zero.fxml")
 *                               .setControllerClass(MyController.class)
 *                               .setExecutor("com.example.executor.MyExecutor") // omit this line to use default executor
 *                               .setTitle("This is screen")
 *                               .build();
 *
 *      router.addDestinationWithArgument(destination,args0);
 *
 *  }
 * </pre>
 *
 * @see Router
 * @see rahulstech.jfx.routing.RouterExecutor RouterExecutor
 * @see RouterArgument
 *
 * @author  Rahul Bagchi
 *
 * @since 1.0
 */
public class Destination {

    private String id;
    private String fxml;
    private Class<?> controllerClass;
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

    public Class<?> getControllerClass() {
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
        Class<?> controllerClass;
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

        public Builder setControllerClass(Class<?> controllerClass) {
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
