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

@SuppressWarnings("unused")
public class RouterXmlParser {

    public static final String DEFAULT_NAMESPACE = "https://github.com/rahulstech/javafx-routing";

    public static final String ELEMENT_ROUTER = "router";

    public static final String ELEMENT_DESTINATION = "destination";

    public static final String ELEMENT_ANIMATION = "animation";

    public static final String ELEMENT_COMPOUND_ANIMATION = "compound-animation";

    public static final String ELEMENT_ARGUMENTS = "arguments";

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

    public AttributeSet getRouterAttributeSet() {
        return routerAttrs==null ? AttributeSet.EMPTY : routerAttrs;
    }

    public Collection<Destination> getDestinations() {
        return destinations.values();
    }

    public Collection<AttributeSet> getAnimations() {
        return animations.values();
    }

    public Map<String,RouterArgument> getArguments() {
        return arguments;
    }

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

    void addDestination(Destination destination) {
        String key = destination.getId();
        destinations.put(key,destination);
    }

    boolean hasDestination(String id) {
        return destinations.containsKey(id);
    }

    void addAnimation(String id, AttributeSet attrs) {
        animations.put(id,attrs);
    }

    boolean hasAnimation(String id) {
        return animations.containsKey(id);
    }

    private String getLocationString(Location location) {
        return "@[line = "+location.getLineNumber()+" column = "+location.getColumnNumber()+"]";
    }


}
