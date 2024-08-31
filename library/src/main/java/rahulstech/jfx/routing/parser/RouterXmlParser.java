package rahulstech.jfx.routing.parser;

import rahulstech.jfx.routing.element.Destination;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.util.StringUtil;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.*;

/**
 * The {@code RouterXmlParser} class is responsible for parsing router configuration xml file.
 * <p>Example router configuration xml</p>
 * <pre>{@code
 * <?xml version="1.0" encoding="utf=8"?>
 * <router xmlns="https://github.com/rahulstech/javafx-routing"
 *         home="dashboard"
 *         homeEnterAnimation="fade_in"
 *         enterAnimation="enter_animation"
 *         exitAnimation="slide_out_right"
 *         popEnterAnimation="slide_in_right"
 *         popExitAnimation="pop_exit_animation">
 *
 *     <animation id="enter_animation"
 *                name="slide_in_left"/>
 *
 *     <compound-animation id="pop_exit_animation"
 *                name="slide_left_fade_out"
 *                duration="duration_long">
 *          <!--
 *              customize the fade_out animation of slide_left_fade_out animation for custom pop_exit_animation.
 *              the slide_out_left animation will take its default values as defined in slide_left_fade_out animation.
 *          -->
 *          <animation name="fade_out"
 *                     toAlpha="0.35"/>
 *
 *     </compound-animation>
 *
 *     <arguments id="args_global">
 *          <argument name="arg0" required="false" type="long"/>
 *          <argument name="arg1" required="false" type="string|string_array"/>
 *     </arguments>
 *
 *     <!-- destinations -->
 *     <destination id="dashboard"
 *                  fxml="dashboard.fxml"
 *                  controllerClass="com.example.controller.Dashboard"/>
 *
 *     <destination id="screen0"
 *                  fxml="screen_zero.fxml">
 *         <arguments>
 *             <argument name="arg0" required="true" type="int"/>
 *         </arguments>
 *     </destination>
 *
 *     <destination id="screen1"
 *                  controllerClass="com.example.controller.ScreenOneController"
 *                  arguments="args_global"/>
 *
 *     <destination id="screen2"
 *                  fxml="screen_two.fxml"
 *                  arguments="args_global"/>
 *
 * </router>
 * }
 * </pre>
 * @author Rahul Bagchi
 * @since 1.0
 */
public class RouterXmlParser {

    /**
     * The default namespace for the XML elements in the routing configuration.
     */
    public static final String DEFAULT_NAMESPACE = "https://github.com/rahulstech/javafx-routing";

    /**
     * The XML element name for the router.
     */
    public static final String ELEMENT_ROUTER = "router";

    /**
     * The XML element name for a destination within the router.
     */
    public static final String ELEMENT_DESTINATION = "destination";

    /**
     * The XML element name for an animation within the router.
     */
    public static final String ELEMENT_ANIMATION = "animation";

    /**
     * The XML element name for a compound animation within the router.
     */
    public static final String ELEMENT_COMPOUND_ANIMATION = "compound-animation";

    /**
     * The XML element name for a set of arguments within a destination.
     */
    public static final String ELEMENT_ARGUMENTS = "arguments";

    /**
     * The XML element name for a single argument within a set of arguments.
     */
    public static final String ELEMENT_ARGUMENT = "argument";

    private AttributeSet routerAttrs = new AttributeSet();

    private Map<String,Destination> destinations = new HashMap<>();

    private Destination lastParsedDestination;

    private Map<String,AttributeSet> animations = new HashMap<>();

    private Stack<AttributeSet> animationStack = new Stack<>();

    private int compoundAnimationDepth = 0;

    private Map<String,RouterArgument> arguments = new HashMap<>();

    private String parsingArgumentsId = null;

    private RouterArgument parsingArguments = null;
    
    private Stack<String> elementHierarchy = new Stack<>();

    /**
     * Creates new {@code RouterXmlParser} instance
     */
    public RouterXmlParser() {}

    /**
     * Parses the given XML input stream, processing its elements and attributes
     * according to the router configuration schema.
     *
     * @param in the input stream of the XML file to be parsed
     * @throws ParserException if an error occurs during parsing or if the XML structure
     *                         does not conform to the expected schema
     */
    public void parse(InputStream in)  {
        try {
            XMLInputFactory factory = XMLInputFactory.newDefaultFactory();
            XMLEventReader events = factory.createXMLEventReader(in);
            XMLEvent event;
            while (events.hasNext()) {
                event = events.nextEvent();

                if (event.isStartElement()) {
                    Location location = event.getLocation();
                    StartElement startElement = event.asStartElement();
                    QName elementName = startElement.getName();
                    Iterator<javax.xml.stream.events.Attribute> attrs = startElement.getAttributes();
                    onStartElement(getLocationString(location), elementName.getNamespaceURI(), elementName.getLocalPart(), attrs);
                } else if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    QName elementName = endElement.getName();
                    onEndElement(elementName.getNamespaceURI(), elementName.getLocalPart());
                }
            }
        }
        catch (XMLStreamException ex) {
            throw new ParserException("parser failed with message "+ex.getMessage());
        }
    }

    /**
     * Returns the attribute set of the router element.
     *
     * @return the attribute set of the router element, or an empty attribute set if none exists
     */
    public AttributeSet getRouterAttributeSet() {
        return routerAttrs==null ? AttributeSet.EMPTY : routerAttrs;
    }

    /**
     * Returns a collection of all parsed destinations.
     *
     * @return a collection of {@link Destination} objects
     */
    public Collection<Destination> getDestinations() {
        return destinations.values();
    }

    /**
     * Returns a collection of all parsed animations.
     *
     * @return a collection of {@link AttributeSet} objects representing animations
     */
    public Collection<AttributeSet> getAnimations() {
        return animations.values();
    }

    /**
     * Returns a map of all parsed arguments.
     *
     * @return a map of argument IDs to {@link RouterArgument} objects
     */
    public Map<String,RouterArgument> getArguments() {
        return arguments;
    }

    /**
     * Clears all parsed data, including router attributes, destinations, animations, and arguments.
     * This method should be called to reset the parser before parsing a new XML file.
     */
    public void clear() {
        routerAttrs.clear();
        destinations.clear();
        animations.clear();
        animationStack.clear();
        arguments.clear();
        elementHierarchy.clear();
        routerAttrs = null;
        destinations = null;
        animations = null;
        animationStack = null;
        arguments = null;
        elementHierarchy = null;
    }

    /**
     * Processes the start of an XML element, handling specific routing elements such as
     * router, destination, animation, compound-animation, arguments, and argument.
     *
     * @param location    the location of the element in the XML file
     * @param namespace   the namespace URI of the element
     * @param name        the local name of the element
     * @param attributes  an iterator over the attributes of the element
     * @throws ParserException if the element is not recognized or if it violates the expected hierarchy
     */
    private void onStartElement(String location, String namespace, String name, Iterator<javax.xml.stream.events.Attribute> attributes) {
        if (!DEFAULT_NAMESPACE.equals(namespace)) {
            throw new ParserException("unknown element '"+name+"' with namespace '"+namespace+"'");
        }
        checkHierarchyOrThrow(name,location);
        elementHierarchy.push(name);
        switch (name) {
            case ELEMENT_ROUTER: {
                routerAttrs = createAttributeSet(attributes);
            }
            break;
            case ELEMENT_DESTINATION: {
                AttributeSet attrs = createAttributeSet(attributes);
                Attribute id = attrs.get(Attribute.ID);
                if (null==id) {
                    throw new ParserException("destination has no id "+location);
                }
                if (!attrs.hasAttribute(Attribute.FXML)
                        && !attrs.hasAttribute(Attribute.CONTROLLER_CLASS)) {
                    throw new ParserException("destination neither has attribute fxml nor controllerClass "+location);
                }

                if (hasDestination(id.getValue())) {
                    throw new ParserException("destination with duplicate id '"+id.getValue()+"' added "+location);
                }
                Destination destination = new Destination(attrs);
                addDestination(destination);
                lastParsedDestination = destination;
            }
            break;
            case ELEMENT_ANIMATION: {
                AttributeSet attrs = createAttributeSet(attributes);
                Attribute id = attrs.get(Attribute.ID);
                if (!attrs.hasAttribute(Attribute.NAME)) {
                    throw new ParserException("animation has no name "+location);
                }
                if (animationStack.isEmpty()) {
                    // if stack is null it means current animation is not part of any compound animation
                    // add directly using addAnimation
                    if (null==id) {
                        throw new ParserException("animation has no id "+location);
                    }
                    if (hasAnimation(id.getValue())) {
                        throw new ParserException("animation with duplicate id '"+id.getValue()+"' added "+location);
                    }
                    addAnimation(id.getValue(),attrs);
                }
                else {
                    // now current animation is a child of a compound animation, add to stack
                    animationStack.add(attrs);
                }
            }
            break;
            case ELEMENT_COMPOUND_ANIMATION: {
                AttributeSet attrs = createAttributeSet(attributes);
                Attribute id = attrs.get(Attribute.ID);
                if (!attrs.hasAttribute(Attribute.NAME)) {
                    throw new ParserException("animation has no name "+location);
                }
                if (animationStack.isEmpty()) {
                    // it's a top level compound animation so id is required
                    if (null==id) {
                        throw new ParserException("animation has no id "+location);
                    }
                    if (hasAnimation(id.getValue())) {
                        throw new ParserException("animation with duplicate id '"+id.getValue()+"' added "+location);
                    }
                    addAnimation(id.getValue(),attrs);
                }
                animationStack.push(attrs);
                animationStack.add(null); // mark for start of its children
                compoundAnimationDepth++;
            }
            break;
            case ELEMENT_ARGUMENTS: {
                AttributeSet attrs = createAttributeSet(attributes);

                Attribute idAttr = attrs.get(Attribute.ID);
                if (null==lastParsedDestination) {
                    if (null==idAttr) {
                        // neither inside destination nor contains an id, an error
                        throw new ParserException("arguments element must contain an id " + location);
                    }
                    String id = idAttr.getValue();
                    if (arguments.containsKey(id)) {
                        // adding another arguments with same id, an error
                        throw new ParserException("argument with id '"+id+"' already exits");
                    }
                    parsingArguments = new RouterArgument();
                    parsingArgumentsId = id;
                }
                else {
                    // it is inside destination, each destination can contain at most one 'arguments' or 'argument' block
                    String id = lastParsedDestination.getId();
                    if (arguments.containsKey(lastParsedDestination.getId())) {
                        // adding another 'arguments' or argument block to the destination, an error
                        throw new ParserException("arguments for destination '"+id+"' already added "+location);
                    }
                    parsingArguments = new RouterArgument();
                    parsingArgumentsId = id;
                }
            }
            break;
            case ELEMENT_ARGUMENT: {
                // checking for argument element child of arguments element is already done, so no need for
                // null check of parsingArgument is needed
                AttributeSet attrs = createAttributeSet(attributes);
                RouterArgument.NameValue nv = new RouterArgument.NameValue(attrs);
                parsingArguments.addArgument(nv);
            }
            break;
        }
    }

    /**
     * Validates the hierarchy of XML elements and throws a {@code ParserException} if
     * the element's parent does not match the expected structure.
     *
     * @param element    the name of the XML element
     * @param location   the location of the element in the XML file
     * @throws ParserException if the element's position in the hierarchy is incorrect
     */
    private void checkHierarchyOrThrow(String element, String location) {
        boolean empty = elementHierarchy.isEmpty();
        String parent = empty ? null : elementHierarchy.peek();
        switch (element) {
            case ELEMENT_ROUTER: {
                if (!empty) {
                    throw new ParserException("'"+ELEMENT_ROUTER+"' must be root element, "+location);
                }
            }
            break;
            case ELEMENT_DESTINATION: {
                if (!ELEMENT_ROUTER.equals(parent)) {
                    throw new ParserException("'"+ELEMENT_DESTINATION+"' element as direct child of only" +
                            " '"+ELEMENT_ROUTER+"' element is allowed, "+location);
                }
            }
            break;
            case ELEMENT_ANIMATION: {
                if (!ELEMENT_ROUTER.equals(parent) && !ELEMENT_COMPOUND_ANIMATION.equals(parent)) {
                    throw new ParserException("'"+ELEMENT_ANIMATION+"' element as direct child of only " +
                            "'"+ELEMENT_ROUTER+"' or '"+ELEMENT_COMPOUND_ANIMATION+"' is allowed, "+location);
                }
            }
            break;
            case ELEMENT_COMPOUND_ANIMATION: {
                if (!ELEMENT_ROUTER.equals(parent) && !ELEMENT_COMPOUND_ANIMATION.equals(parent)) {
                    throw new ParserException("'"+ELEMENT_COMPOUND_ANIMATION+"' element as direct child of only " +
                            "'"+ELEMENT_ROUTER+"' or '"+ELEMENT_COMPOUND_ANIMATION+"' is allowed, "+location);
                }
            }
            break;
            case ELEMENT_ARGUMENTS: {
                if (!ELEMENT_ROUTER.equals(parent) && !ELEMENT_DESTINATION.equals(parent)) {
                    throw new ParserException("'"+ELEMENT_ARGUMENTS+"' element as direct child of only " +
                            "'"+ELEMENT_ROUTER+"' or '"+ELEMENT_DESTINATION+"' is allowed, "+location);
                }
            }
            break;
            case ELEMENT_ARGUMENT: {
                if (!ELEMENT_ARGUMENTS.equals(parent)) {
                    throw new ParserException("'"+ELEMENT_ARGUMENT+"' element as direct child of only " +
                            "'"+ELEMENT_ARGUMENTS+"' is allowed, "+location);
                }
            }
            break;
            default: {
                throw new ParserException("unknown element '"+element+"'");
            }
        }
    }

    /**
     * Processes the end of an XML element, performing necessary actions such as finalizing
     * the parsing of animations and arguments.
     *
     * @param namespace the namespace URI of the element
     * @param name      the local name of the element
     * @throws ParserException if there are errors in finalizing the element's data
     */
    private void onEndElement(String namespace, String name) {
        elementHierarchy.pop();
        switch (name) {
            case ELEMENT_ROUTER: {
                if (null == routerAttrs) {
                    throw new ParserException(ELEMENT_ROUTER + " element requires at least one attribute i.e. " + Attribute.HOME + "; found nothing");
                }
                if (!routerAttrs.hasAttribute(Attribute.HOME)) {
                    throw new ParserException("home destination not specified in " + ELEMENT_ROUTER +
                            "; use " + Attribute.HOME + "=<destination id> to set home destination");
                }
            }
            break;
            case ELEMENT_DESTINATION: {
                lastParsedDestination = null;
            }
            break;
            case ELEMENT_COMPOUND_ANIMATION: {
                Queue<AttributeSet> queue = new ArrayDeque<>();
                while (!animationStack.isEmpty()) {
                    AttributeSet top = animationStack.peek();
                    if (null == top) {
                        animationStack.pop(); // remove the mark
                        AttributeSet attrs = animationStack.peek();
                        queue.forEach(subset -> {
                            Attribute nameAttr = subset.get(Attribute.NAME);
                            attrs.addWithPrefix(nameAttr.getValue(), subset);
                        });
                        compoundAnimationDepth--;
                        break;
                    } else {
                        queue.add(animationStack.pop());
                    }
                }
                if (compoundAnimationDepth == 0) {
                    animationStack.clear();
                }
            }
            break;
            case ELEMENT_ARGUMENTS: {
                arguments.put(parsingArgumentsId,parsingArguments);
                parsingArgumentsId = null;
                parsingArguments = null;
            }
            break;
        }
    }

    /**
     * Creates an {@code AttributeSet} from the given iterator of XML attributes.
     *
     * @param it an iterator over XML attributes
     * @return an {@code AttributeSet} containing the attributes
     */
    private AttributeSet createAttributeSet(Iterator<javax.xml.stream.events.Attribute> it) {
        AttributeSet set = new AttributeSet();
        while (it.hasNext()) {
            javax.xml.stream.events.Attribute attr = it.next();
            QName name = attr.getName();
            String namespaceUri = name.getNamespaceURI();
            String namespace = StringUtil.isEmpty(namespaceUri) ? DEFAULT_NAMESPACE : namespaceUri;
            String value = attr.getValue();
            Attribute attribute
                    = new Attribute(namespace,name.getLocalPart(),value);
            set.add(attribute);
        }
        return set;
    }

    /**
     * Adds a destination to the collection of destinations.
     *
     * @param destination the {@link Destination} object to add
     */
    void addDestination(Destination destination) {
        String key = destination.getId();
        destinations.put(key,destination);
    }

    /**
     * Checks if a destination with the specified ID exists.
     *
     * @param id the ID of the destination to check
     * @return {@code true} if the destination exists, {@code false} otherwise
     */
    boolean hasDestination(String id) {
        return destinations.containsKey(id);
    }

    /**
     * Adds {@link AttributeSet} for animation with id
     *
     * @param id    the ID of the animation
     * @param attrs the {@link AttributeSet} representing the animation
     */
    void addAnimation(String id, AttributeSet attrs) {
        animations.put(id,attrs);
    }

    /**
     * Checks if an animation with the specified ID exists.
     *
     * @param id the ID of the animation to check
     * @return {@code true} if the animation exists, {@code false} otherwise
     */
    boolean hasAnimation(String id) {
        return animations.containsKey(id);
    }

    /**
     * Returns a string representation of the location in the XML file.
     *
     * @param location the location object from which to generate the string
     * @return a string representing the location in the format "@[line = X column = Y]"
     */
    private String getLocationString(Location location) {
        return "@[line = "+location.getLineNumber()+" column = "+location.getColumnNumber()+"]";
    }
}
