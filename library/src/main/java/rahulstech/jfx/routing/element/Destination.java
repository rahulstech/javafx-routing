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
 *
 * <p>Below is the example of destination in router configuration xml file
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
 * }
 * </pre>
 * <p>or</p>
 * <pre>{@code
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
 *  }
 * </pre>
 *
 * @see Router
 * @see rahulstech.jfx.routing.RouterExecutor RouterExecutor
 * @see RouterArgument
 *
 * @author  Rahul Bagchi
 * @since 1.0
 */
public class Destination {

    private String id;
    private String fxml;
    private Class<?> controllerClass;
    private String title;
    private String executor;
    private String arguments;
    private boolean singleTop;

    /**
     * Constructor used by the Builder pattern to create a {@code Destination} instance.
     *
     * @param builder the Builder object containing the necessary fields
     */
    Destination(Builder builder) {
        id = builder.id;
        fxml = builder.fxml;
        controllerClass = builder.controllerClass;
        title = builder.title;
        executor = builder.executor;
        arguments = builder.arguments;
        singleTop = builder.singleTop;
    }

    /**
     * Constructs a {@code Destination} from an {@link AttributeSet}.
     * This constructor is typically used when creating a {@code Destination}
     * from XML attributes.
     *
     * @param attrs the AttributeSet containing the destination's attributes
     */
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
                    break;
                    case Attribute.SINGLE_TOP: {
                        singleTop = attr.getAsBoolean();
                    }
                }
            }
        }
        if (StringUtil.isEmpty(executor)) {
            executor = Router.KEY_DEFAULT_ROUTER_EXECUTOR;
        }
    }

    /**
     * Gets the ID of the destination.
     *
     * @return the destination's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the FXML layout file associated with the destination.
     *
     * @return the FXML layout file's path
     */
    public String getFXML() {
        return fxml;
    }

    /**
     * Gets the title of the destination.
     *
     * @return the title of the destination
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the controller class associated with the destination.
     *
     * @return the controller class
     */
    public Class<?> getControllerClass() {
        return controllerClass;
    }

    /**
     * Gets the executor responsible for handling the destination.
     *
     * @return the executor's full class name
     */
    public String getExecutor() {
        return executor;
    }

    /**
     * Gets the arguments associated with the destination.
     *
     * @return the arguments identifier or details
     */
    public String getArguments() {
        return arguments;
    }

    /**
     * Returns weather at most one instance of this {@code Destination} is allowed at any time in backstack
     *
     * @return {@code true} means keep at most one instnace of this {@code Destination} in backstack,
     *          {@code false} if more than one instances allowed at any time
     */
    public boolean isSingleTop() {
        return singleTop;
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

    /**
     * A Builder class for constructing {@code Destination} instances.
     */
    public static class Builder {
        String id;
        String fxml;
        Class<?> controllerClass;
        String title;
        String executor = Router.KEY_DEFAULT_ROUTER_EXECUTOR;
        String arguments;
        boolean singleTop;

        /**
         * Constructs a new Builder for a {@code Destination}.
         *
         * @param id the unique ID for the destination
         * @throws IllegalArgumentException if the ID is null or empty
         */
        public Builder(String id) {
            if (null==id || id.trim().isEmpty()) {
                throw new IllegalArgumentException("empty destination id is not allowed");
            }
            this.id = id;
        }

        /**
         * Sets the FXML layout file for the destination.
         *
         * @param fxml the FXML file's path
         * @return this Builder instance
         */
        public Builder setFXML(String fxml) {
            this.fxml = fxml;
            return this;
        }

        /**
         * Sets the controller class for the destination.
         *
         * @param controllerClass the controller class
         * @return this Builder instance
         */
        public Builder setControllerClass(Class<?> controllerClass) {
            this.controllerClass = controllerClass;
            return this;
        }

        /**
         * Sets the title for the destination.
         *
         * @param title the destination's title
         * @return this Builder instance
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the executor responsible for handling the destination.
         *
         * @param executor the executor's full class name
         * @return this Builder instance
         */
        public Builder setExecutor(String executor) {
            this.executor = executor;
            return this;
        }

        /**
         * Sets the arguments associated with the destination.
         *
         * @param arguments the arguments identifier or details
         * @return this Builder instance
         */
        public Builder setArguments(String arguments) {
            this.arguments = arguments;
            return this;
        }

        /**
         * Sets wheather to keep at most one instance in backstack at any time
         *
         * @param singleTop {@code true} means keep at most one instance, {@code false} otherwise
         * @return this Builder instance
         */
        public Builder setSingleTop(boolean singleTop) {
            this.singleTop = singleTop;
            return this;
        }

        /**
         * Builds and returns a {@code Destination} instance.
         *
         * @return a new {@code Destination} instance
         */
        public Destination build() {
            return new Destination(this);
        }
    }
}
