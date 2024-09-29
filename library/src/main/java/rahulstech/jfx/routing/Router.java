package rahulstech.jfx.routing;

import javafx.scene.layout.Pane;
import rahulstech.jfx.routing.backstack.Backstack;
import rahulstech.jfx.routing.backstack.BackstackEntry;
import rahulstech.jfx.routing.element.Destination;
import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;
import rahulstech.jfx.routing.parser.RouterXmlParser;
import rahulstech.jfx.routing.util.Disposable;

import java.io.InputStream;
import java.util.*;

/**
 * The {@code Router} class is responsible for handling all navigation requests
 * within an application. It manages the navigation between different destinations
 * by retrieving the requested destination's executor and executing the actual
 * transaction to complete the navigation.
 *
 * <p>The {@code Router} class also maintains a backstack to keep references of
 * previously visited destinations, allowing for navigation back to previous states.
 * It supports parsing of a router configuration file to set up destinations, arguments,
 * and animations, but destinations can also be added manually.</p>
 *
 * <p>The router configuration can define various attributes such as home destination,
 * animations for transitions, and default arguments for destinations.</p>
 *
 * <p>The router supports adding destinations and arguments manually or through
 * parsing an XML configuration file. It also provides methods to navigate to a
 * destination, pop the backstack, and handle lifecycle events.</p>
 *
 * <p>Get {@code Router} instance</p>
 * <p>If you are using {@code RouterPane} then get a {@code Router} instance like below</p>
 * <pre>{@code
 * Router router;
 * RouterPane content = ...
 * content.routerProperty().addListener((o,oldV,newV)->{router = newV;});
 * }</pre>
 *
 * @author Rahul Bagchi
 *
 * @since 1.0.0
 */
public class Router implements Disposable {

    /**
     * Name of the single scene screen {@link RouterExecutor}
     */
    public static final String KEY_SINGLE_SCENE_SCREEN_EXECUTOR = "rahulstech.jfx.routing.routerexecutor.SingleSceneScreenExecutor";

    /**
     * A flag that indicates the default router executor instance should be used
     * for destinations that do not have an explicitly defined executor.
     * <p>
     * <strong>Note:</strong> This is not the name of any router executor.
     */
    public static final String KEY_DEFAULT_ROUTER_EXECUTOR = "DEFAULT_ROUTER_EXECUTOR";

    /**
     * The root pane to use for single scene screes
     */
    private Pane content;

    private Map<String,Destination> destinations;

    private Map<String,RouterArgument> arguments;

    private Backstack<RouterBackstackEntry> backstack;

    private Destination homeDestination;

    private Router parentRouter;

    /**
     * Name or id of the home destination enter animation. if
     * nothing is specified then default enter animation is used
     */
    private String homeEnterAnimation;

    /**
     * Name or id of animation for a new screen enter
     */
    private String defaultEnterAnimation;

    /**
     * Name or id of animation for a screen exit
     */
    private String defaultExitAnimation;

    /**
     * Name or id of enter animation for screen in backstack
     */
    private String defaultPopEnterAnimation;

    /**
     * Name or id of animation for screen exiting and popped from backstack
     */
    private String defaultPopExitAnimation;

    private RouterContext context;

    private boolean disposed = false;

    /////////////////////////////////////////////////////////////
    //                     Constructors                       //
    ///////////////////////////////////////////////////////////

    /**
     * Create new {@code Router} instance with associated {@link RouterContext}
     *
     * @param context associated {@code RouterContext}
     */
    public Router(RouterContext context) {
        this(context,null);
    }

    /**
     * Creates new {@code Router} instance with {@link RouterContext} and
     * {@link Pane} as content parent for single scene screens
     *
     * @param context associated {@code RouterContext}
     * @param content {@code Pane} as content parent
     */
    public Router(RouterContext context, Pane content) {
        if (null==context) {
            throw new NullPointerException("context is null");
        }
        this.context = context;
        this.content = content;
        destinations = new HashMap<>();
        arguments = new HashMap<>();
        backstack = new Backstack<>();
    }

    /////////////////////////////////////////////////////////////
    //                     Getter and Setts                   //
    ///////////////////////////////////////////////////////////

    /**
     * Sets the parent {@code Router} of this Router, if any
     *
     * @param parentRouter the parent {@code Router} instnace or {@code null}
     */
    public void setParentRouter(Router parentRouter) {
        this.parentRouter = parentRouter;
    }

    /**
     * Returns the associated parent {@code Router}
     *
     * @return an instance of {@code Router} or {@code null}
     */
    public Router getParentRouter() {
        return parentRouter;
    }

    /**
     * Returns {@link RouterContext} instance associated with this {@code Router}
     *
     * @return non-null {@code RouterContext} instance
     */
    public RouterContext getContext() {
        return context;
    }

    /**
     * Returns {@link Pane} to use as content parent for single scene screens
     *
     * @return {@code Pane} to use as content parent
     */
    public Pane getContentPane() {
        return content;
    }

    /**
     * Sets {@link Pane} as content parent for single scene screens
     *
     * @param content set the root pane for single scene screens
     */
    public void setContentPane(Pane content) {
        this.content = content;
    }

    /**
     * Returns registered {@link RouterExecutor} for given {@code name} from the
     * {@link RouterContext}
     *
     * @param name the name of the executor
     * @return registered {@code RouterExecutor} for name or {@code null}
     */
    public RouterExecutor getRouterExecutorForName(String name) {
        return context.getRouterExecutorForName(name,this);
    }

    /**
     * Returns registered {@link RouterExecutor} instance for {@code name} from {@link RouterContext} or
     * the default {@code RouterExecutor} is nothing found for the {@code name}
     *
     * @param name the name of the executor
     * @return registered {@code RouterExecutor} for name or the default executor. always non-null.
     * @see RouterContext#getRouterExecutorForName(String, Router)
     * @see RouterContext#getDefaultRouterExecutor(Router)
     */
    public RouterExecutor getRouterExecutorForNameOrDefault(String name) {
        RouterExecutor executor = getRouterExecutorForName(name);
        if (null==executor) {
            return context.getDefaultRouterExecutor(this);
        }
        return executor;
    }

    /**
     * Set a destination as home destination. Home destination is first destination
     * to show automatically on first start. Call this method when all the destinations
     * are registered otherwise it will throw an exception.
     *
     * @param id the destination id
     * @throws IllegalArgumentException if no destination found for the id
     */
    public void setHomeDestination(String id) {
        Destination destination = destinations.get(id);
        if (null==destination) {
            throw new IllegalStateException("not destination found with id=\""+id+"\"," +
                    " make sure you have added the destination before calling this method");
        }
        homeDestination = destination;
    }

    /**
     * Register a new destination as home destination. Destination is registered if not exists.
     *
     * @param destination the  {@link Destination} as home destination
     */
    public void setHomeDestination(Destination destination) {
        if (!destinations.containsKey(destination.getId())) {
            addDestination(destination);
        }
        homeDestination = destination;
    }

    /**
     * Returns {@link Destination} set as home. Home destination is the
     * destination which is shown by the {@code Router} at start.
     *
     * @return {@code Destination} registered as home destination
     *          or {@code null}
     */
    public Destination getHomeDestination() {
        return homeDestination;
    }

    /**
     * Set the enter animation for home destination. Set the animation name or id
     * to be used as home enter animation.
     *
     * @param homeEnterAnimation the id or name of the animation
     */
    public void setHomeEnterAnimation(String homeEnterAnimation) {
        this.homeEnterAnimation = homeEnterAnimation;
    }

    /**
     * Returns name or id of the animation specified as home destination enter animation.
     * If no such animation is specified then default enter animation is used.
     *
     * @return get the home destination enter animation id or name or null if
     *          nothing is set
     * @see #getDefaultEnterAnimation()
     */
    public String getHomeEnterAnimation() {
        return homeEnterAnimation;
    }

    /**
     * Set the default enter exit popEnter and popExit animations for destinations.
     * These animations will be used when other animation explicitly not mentioned
     * for the navigation operation. To set navigation animations other than the default
     * animations, use {@link RouterOptions}.
     *
     * @param enterAnimation animation used for new screen enter
     * @param exitAnimation animation used for exiting screen
     * @param popEnterAnimation animation used for screen entering from backstack
     * @param popExitAnimation animation for screen pop exiting from backstack. after pop exiting
     *                         screen will no longer be available in backstack
     * @see RouterOptions
     * @see #moveto(String, RouterArgument, RouterOptions)
     * @see #moveto(Destination, RouterArgument, RouterOptions)
     */
    public void setDefaultAnimations(String enterAnimation, String exitAnimation, String popEnterAnimation, String popExitAnimation) {
        defaultEnterAnimation = enterAnimation;
        defaultExitAnimation = exitAnimation;
        defaultPopEnterAnimation = popEnterAnimation;
        defaultPopExitAnimation = popExitAnimation;
    }

    /**
     * Returns name or id of the {@link RouterAnimation} to use as screen enter animation.
     *
     * @return name or id of the screen enter animation
     */
    public String getDefaultEnterAnimation() {
        return defaultEnterAnimation;
    }

    /**
     * Returns name or id of the {@link RouterAnimation} to use as screen exit animation.
     *
     * @return name or id of the screen exit animation
     */
    public String getDefaultExitAnimation() {
        return defaultExitAnimation;
    }

    /**
     * Returns name or id of the {@link RouterAnimation} to use as screen pop enter animation.
     *
     * @return name or id of the screen pop enter animation
     */
    public String getDefaultPopEnterAnimation() {
        return defaultPopEnterAnimation;
    }

    /**
     * Returns name or id of the {@link RouterAnimation} to use as screen pop exit animation.
     *
     * @return name or id of the screen pop exit animation
     */
    public String getDefaultPopExitAnimation() {
        return defaultPopExitAnimation;
    }

    /**
     * Register a new {@link  Destination}. Destination id must be unique in this Router
     * otherwise it will throw an exception.
     *
     * @param destination the new destination to register
     * @throws IllegalStateException if any destination already registered for same id
     */
    public void addDestination(Destination destination) {
        String id = destination.getId();
        if (destinations.containsKey(id)) {
            throw new IllegalStateException("destination with id "+id+" already added");
        }
        destinations.put(id,destination);
    }

    /**
     * Register multiple {@link Destination}s
     *
     * @param destinations {@link Collection} of {@link Destination}s
     * @throws IllegalArgumentException if any of the destination id is not unique in this Router
     * @see #addDestination(Destination)
     */
    public void addAllDestination(Collection<Destination> destinations) {
        destinations.forEach(this::addDestination);
    }

    /**
     * Returns {@link Destination} by destination id
     *
     * @param id the destination id
     * @return  {@code Destination} instance or {@code null}
     */
    public Destination getDestination(String id) {
        return destinations.get(id);
    }

    /**
     * Register a new destination with its {@link RouterArgument}s
     *
     * @param destination the destination
     * @param argument the arguments
     * @see #addDestination(Destination)
     * @see #addArgument(String, RouterArgument)
     */
    public void addDestinationWithArgument(Destination destination, RouterArgument argument) {
        addDestination(destination);
        addArgument(destination.getId(),argument);
    }

    /**
     * Replace the {@link RouterArgument} for the destination
     *
     * @param destinationId the destination id
     * @param argument the new arguments
     * @return the old arguments or null
     */
    public RouterArgument setArgumentForDestination(String destinationId, RouterArgument argument) {
        return arguments.put(destinationId,argument);
    }

    /**
     * Register {@link RouterArgument} with given id. It will throw exception
     * if argument id is not unique in the Router.
     *
     * @param id the argument id
     * @param argument the argument
     * @throws IllegalStateException if id is not unique
     */
    public void addArgument(String id, RouterArgument argument) {
        if (arguments.containsKey(id)) {
            throw new IllegalStateException("argument with id '"+id+"' is already added");
        }
        arguments.put(id,argument);
    }

    /**
     * Register multiple {@link RouterArgument}s with id. Ids must be unique in Router
     * otherwise it will throw execution
     *
     * @param arguments {@link Map} of argument id and argument
     * @throws IllegalStateException if id is not unique
     * @see #addArgument(String, RouterArgument)
     */
    public void addArguments(Map<String,RouterArgument> arguments) {
        arguments.forEach(this::addArgument);
    }

    /**
     * Returns {@link RouterArgument} registered for destination with given id
     *
     * @param destinationId the destination id
     * @return {@code RouterArgument} instance or {@code null}
     * @throws IllegalArgumentException if no destination found for id
     */
    public RouterArgument getArgumentForDestination(String destinationId) {
        Destination destination = getDestination(destinationId);
        if (null==destination) {
            throw new IllegalArgumentException("no destination found for id "+destinationId);
        }
        RouterArgument arg = getArgument(destinationId);
        if (null!=arg) {
            return arg;
        }
        return getArgument(destination.getArguments());
    }

    /**
     * Returns {@link RouterArgument} by id
     *
     * @param id the argument id
     * @return {@code RouterArgument} instance or {@code null};
     */
    public RouterArgument getArgument(String id) {
        RouterArgument args = arguments.get(id);
        if (null==args) {
            return null;
        }
        return args.copyWithoutValue();
    }

    /**
     * Returns home data from {@link RouterContext}
     *
     * @return {@link RouterArgument} instance or {@code null}
     * @see RouterContext#getHomeData()
     */
    public RouterArgument getHomeData() {
        return context.getHomeData();
    }

    /////////////////////////////////////////////////////////////
    //                      Backstack Methods                 //
    ///////////////////////////////////////////////////////////

    /**
     * Returns {@link Backstack} used by this {@code Router}
     *
     * @return non-null {@code Backstack} instance
     */
    public Backstack<RouterBackstackEntry> getBackstack() {
        return backstack;
    }

    /**
     * Returns top {@link RouterBackstackEntry} without removing from backstack
     *
     * @return {@code RouterBackstackEntry} instance or {@code null}
     *
     * @see Backstack#peekBackstackEntry()
     */
    public RouterBackstackEntry getCurrentBackstackEntry() {
        if (!backstack.isEmpty()) {
            return backstack.peekBackstackEntry();
        }
        return null;
    }

    /**
     * Returns data for the destination at the backstack top
     *
     * @return {@link RouterArgument} instance or {@code null}
     * @see RouterBackstackEntry#getData()
     */
    public RouterArgument getCurrentData() {
        RouterBackstackEntry top = getCurrentBackstackEntry();
        if (null==top) {
            return null;
        }
        return top.getData();
    }

    /**
     * Returns {@link Destination} at the backstack top
     *
     * @return {@code Destination} isnan or {@code null}
     * @see RouterBackstackEntry#getDestination()
     */
    public Destination getCurrentDestination() {
        RouterBackstackEntry top = getCurrentBackstackEntry();
        if (null==top) {
            return null;
        }
        return top.getDestination();
    }

    /**
     * Returns result data for the destination at the backstack top.
     *
     * @return {@link RouterArgument} instance or {@code null}
     * @see RouterBackstackEntry#getResult()
     */
    public RouterArgument getCurrentResult() {
        RouterBackstackEntry top = getCurrentBackstackEntry();
        if (null==top) {
            return null;
        }
        return top.getResult();
    }

    /////////////////////////////////////////////////////////////
    //                      Routing Methods                   //
    ///////////////////////////////////////////////////////////

    /**
     * Navigate to the destination or throws execution if no destination found for id
     *
     * @param id the target destination id
     * @throws IllegalStateException if no destination found for id
     * @see #moveto(Destination, RouterArgument, RouterOptions)
     */
    public void moveto(String id) {
        moveto(id,null,null);
    }

    /**
     * Navigate to the destination with data or throws exception if no destination found for id
     *
     * @param id the target destination id
     * @param data the destination data
     * @throws IllegalStateException if no destination found for id
     * @see #moveto(Destination, RouterArgument, RouterOptions)
     */
    public void moveto(String id, RouterArgument data) {
        moveto(id,data,null);
    }

    /**
     * Navigate to the destination with navigation realted options or throws exception if no destination found
     * 
     * @param id the target destination id
     * @param options the options
     * @throws IllegalStateException if no destination found for id
     * @see #moveto(Destination, RouterArgument, RouterOptions) 
     */
    public void moveto(String id, RouterOptions options) {
        moveto(id,null,options);
    }

    /**
     * Navigate to the destination with data and navigation related options
     * or throws exception if no destination found for the id.
     *
     * @param id the target destination id
     * @param data the destination data
     * @param options options for navigation
     * @throws IllegalStateException if no destination found for id
     * @see #moveto(Destination, RouterArgument, RouterOptions) 
     */
    public void moveto(String id, RouterArgument data, RouterOptions options) {
        Destination dest = getDestinationOrThrow(id);
        moveto(dest,data,options);
    }

    /**
     * Navigate to the destination with data nda navigation related options
     *
     * @param target the target {@link  Destination} must non-null
     * @param data {@link RouterArgument} instance as destination data, may be null
     * @param apply an {@link RouterOptions} for navigation options, may be null
     */
    public void moveto(Destination target, RouterArgument data, RouterOptions apply) {
        moveForward(target,apply,data);
    }

    /**
     * Pop the top entry from a non-empty backstack.
     *
     * @return  {@code true} if backstack popped successfully, {@code false} otherwise
     */
    public boolean popBackStack() {
        return popBackstack(null,null);
    }

    /**
     * Pop the top backstack entry and set result for the next destination.
     *
     * @param result the result to set to the next destination
     * @return {@code true} if backstack popped successfully, {@code false} otherwise
     */
    public boolean popBackstack(RouterArgument result) {
        return popBackstack(result,null);
    }

    /**
     * Pop the top backstack entry and set result for the next destination with options for navigation
     *
     * @param result the result to set to the next destination
     * @param options options for navigation
     * @return {@code true} if backstack popped successfully, {@code false} otherwise
     */
    public boolean popBackstack(RouterArgument result, RouterOptions options) {
        String targetId = getCurrentDestination().getId();
        return popBackstackUpTo(targetId,true,result,options);
    }

    /**
     * Pop the destinations from top up to destination with the given id from the backstack. The target
     * destination will be popped if inclusive is {@code true}.If inclusive is {@code false}
     * then destinations till just previous to the target is popped. If destination with
     * the given id is not found in the <strong>backstack</strong> then the pop is canceled.
     * <p>Note: if target is the home destination then even inclusive is {@code true} it is not popped.
     *
     * @param targetId the target destination id
     * @param inclusive {@code true} means pop the target, {@code false} don't pop the target
     * @return {@code true} if successfully popped, {@code false} otherwise
     */
    public boolean popBackstackUpTo(String targetId, boolean inclusive) {
        return popBackstackUpTo(targetId,inclusive,null,null);
    }

    /**
     * Pop the destinations from top up to destination with the given id from the backstack
     * and send the result to next destination.
     * The target destination will be popped if inclusive is {@code true}.If inclusive is {@code false}
     * then destinations till just previous to the target is popped. If destination with
     * the given id is not found in the <strong>backstack</strong> then the pop is canceled.
     * <p>Note: if target is the home destination then even inclusive is {@code true} it is not popped.
     *
     * @param targetId the target destination id
     * @param inclusive {@code true} means pop the target, {@code false} don't pop the target
     * @param result the result for the next destination
     * @return {@code true} if successfully popped, {@code false} otherwise
     */
    public boolean popBackstackUpTo(String targetId,boolean inclusive, RouterArgument result) {
        return popBackstackUpTo(targetId,inclusive,result,null);
    }

    /**
     * Pop the destinations from top up to destination with the given id from the backstack
     * and send the result to next destination with options for navigation.
     * The target destination will be popped if inclusive is {@code true}.If inclusive is {@code false}
     * then destinations till just previous to the target is popped. If destination with
     * the given id is not found in the <strong>backstack</strong> then the pop is canceled.
     * <p>Note: if target is the home destination then even inclusive is {@code true} it is not popped.
     *
     * @param targetId the target destination id
     * @param inclusive {@code true} means pop the target, {@code false} don't pop the target
     * @param result the result for the next destination
     * @param options options for navigation
     * @return {@code true} if successfully popped, {@code false} otherwise
     */
    public boolean popBackstackUpTo(String targetId,boolean inclusive, RouterArgument result, RouterOptions options) {
        return moveBackward(targetId,inclusive,result,options);
    }

    /**
     * Move to the {@link Destination} with {@code targetId} and pop backstack just before
     * the {@code Destination} with id {@code popuptoTargetId}. If the {@code popuptoTargetId} does not
     * exist then no popping takes place; but destination with targetId is shown.
     *
     * @param targetId id of {@code Destination} to show
     * @param popuptoTargetId id of {@code Destination} to pop up to, if exists, excluding it
     */
    public void movePoppingUpto(String targetId, String popuptoTargetId) {
        movePoppingUpto(targetId,popuptoTargetId,false,null,null);
    }

    /**
     * Move to the {@link Destination} with {@code targetId} and pop backstack up to
     * the {@code Destination} with id {@code popuptoTargetId}. The popping of destination
     * with popuptoTargetId depends on {@code popuptoInclusive} value  If the {@code popuptoTargetId} does not
     * exist then no popping takes place; but destination with targetId is shown.
     *
     * @param targetId id of {@code Destination} to show
     * @param popuptoTargetId id of {@code Destination} to pop up to, if exists
     * @param popuptoInclusive {@code  true} means pop {@code popuptoTargetId}, {@code false} otherwise
     */
    public void movePoppingUpto(String targetId, String popuptoTargetId, boolean popuptoInclusive) {
        movePoppingUpto(targetId,popuptoTargetId,popuptoInclusive,null,null);
    }

    /**
     * Move to the {@link Destination} with {@code targetId} and pass optional {@link RouterArgument} data, popping backstack up to
     * the {@code Destination} with id {@code popuptoTargetId}. The popping of destination
     * with popuptoTargetId depends on {@code popuptoInclusive} value  If the {@code popuptoTargetId} does not
     * exist then no popping takes place; but destination with targetId is shown.
     *
     * @param targetId id of {@code Destination} to show
     * @param popuptoTargetId id of {@code Destination} to pop up to, if exists
     * @param popuptoInclusive {@code  true} means pop {@code popuptoTargetId}, {@code false} otherwise
     * @param data destination data as {@code RouterArgument} or {@code null}
     */
    public void movePoppingUpto(String targetId, String popuptoTargetId, boolean popuptoInclusive, RouterArgument data) {
        movePoppingUpto(targetId,popuptoTargetId,popuptoInclusive,data,null);
    }

    /**
     * Move to the {@link Destination} with {@code targetId} and pass optional {@link RouterArgument} data, popping backstack up to
     * the {@code Destination} with id {@code popuptoTargetId}. The popping of destination
     * with popuptoTargetId depends on {@code popuptoInclusive} value  If the {@code popuptoTargetId} does not
     * exist then no popping takes place; but destination with targetId is shown. Pass optional {@link RouterOptions} to perform
     * the navigation.
     *
     * @param targetId id of {@code Destination} to show
     * @param popuptoTargetId id of {@code Destination} to pop up to, if exists
     * @param popuptoInclusive {@code  true} means pop {@code popuptoTargetId}, {@code false} otherwise
     * @param data destination data as {@code RouterArgument} or {@code null}
     * @param options navigation extras as {@code RouterOptions} or {@code null}
     */
    public void movePoppingUpto(String targetId, String popuptoTargetId, boolean popuptoInclusive, RouterArgument data, RouterOptions options) {
        Destination target = getDestinationOrThrow(targetId);
        movePoppingUpto(target,popuptoTargetId,popuptoInclusive,data,options);
    }

    /**
     * Move to the {@link Destination} and pass optional {@link RouterArgument} data, popping backstack up to
     * the {@code Destination} with id {@code popuptoTargetId}. The popping of destination
     * with popuptoTargetId depends on {@code popuptoInclusive} value  If the {@code popuptoTargetId} does not
     * exist then no popping takes place; but destination with target is shown. Pass optional {@link RouterOptions} to perform
     * the navigation.
     *
     * @param target {@code Destination} to show
     * @param popuptoTargetId id of {@code Destination} to pop up to, if exists
     * @param popuptoInclusive {@code  true} means pop {@code popuptoTargetId}, {@code false} otherwise
     * @param data destination data as {@code RouterArgument} or {@code null}
     * @param options navigation extras as {@code RouterOptions} or {@code null}
     */
    public void movePoppingUpto(Destination target, String popuptoTargetId, boolean popuptoInclusive, RouterArgument data, RouterOptions options) {
        moveForwardPoppingUpto(target,data,options,popuptoTargetId,popuptoInclusive);
    }

    /**
     * Show the home destination.
     */
    public void begin() {
        if (null==homeDestination) {
            throw new IllegalStateException("home not set; use setHomeDestination(String) to set home " +
                    "or add homeDestination attribute in router configuration xml file");
        }
        RouterOptions options = new RouterOptions();
        options.setEnterAnimation(homeEnterAnimation);
        moveForward(homeDestination,options,getHomeData());
    }

    /////////////////////////////////////////////////////////////
    //                      Lifecycle Methods                 //
    ///////////////////////////////////////////////////////////

    /**
     * Shows the backstack top destination again.
     *
     * @see #getCurrentDestination()
     */
    public void doLifecycleShow() {
        if (backstack.isEmpty()) {
            return;
        }
        Destination destination = getCurrentDestination();
        RouterExecutor executor = getRouterExecutorForNameOrDefault(destination.getExecutor());
        executor.doLifecycleShow(destination);
    }

    /**
     * Hides the backstack top destination
     *
     * @see #getCurrentDestination()
     */
    public void doLifecycleHide() {
        if (backstack.isEmpty()) {
            return;
        }
        Destination destination = getCurrentDestination();
        RouterExecutor executor = getRouterExecutorForNameOrDefault(destination.getExecutor());
        executor.doLifecycleHide(destination);
    }

    /////////////////////////////////////////////////////////////
    //                      Other Methods                     //
    ///////////////////////////////////////////////////////////

    /**
     * Parse the router configuration xml file
     *
     * @param xml resource path of the xml file
     * @see #parse(InputStream)
     */
    public void parse(String xml) {
        parse(context.getResourceAsStream(xml));
    }

    /**
     * Parse the router configuration from input stream
     *
     * @param in the input stream of the configuration file resource
     */
    public void parse(InputStream in) {
        RouterXmlParser parser = new RouterXmlParser();
        parser.parse(in);
        AttributeSet routerAttrs = parser.getRouterAttributeSet();
        Collection<Destination> destinations = parser.getDestinations();
        Collection<AttributeSet> animations = parser.getAnimations();
        Map<String,RouterArgument> arguments = parser.getArguments();

        addAllDestination(destinations);
        addArguments(arguments);

        String home = routerAttrs.get(Attribute.HOME).getValue();
        String homeEnterAnimation = routerAttrs.getOrDefault(Attribute.HOME_ENTER_ANIMATION,RouterAnimation.NO_OP).getValue();
        String enterAnimation = routerAttrs.getOrDefault(Attribute.ENTER_ANIMATION,RouterAnimation.NO_OP).getValue();
        String exitAnimation = routerAttrs.getOrDefault(Attribute.EXIT_ANIMATION,RouterAnimation.NO_OP).getValue();
        String popEnterAnimation = routerAttrs.getOrDefault(Attribute.POP_ENTER_ANIMATION,RouterAnimation.NO_OP).getValue();
        String popExitAnimation = routerAttrs.getOrDefault(Attribute.POP_EXIT_ANIMATION,RouterAnimation.NO_OP).getValue();

        setHomeDestination(home);
        setHomeEnterAnimation(homeEnterAnimation);
        setDefaultAnimations(enterAnimation,exitAnimation,popEnterAnimation,popExitAnimation);

        context.addAllAnimationAttributeSet(animations);

        parser.clear();
    }

    /**
     * Dispose all resources and release memory when this router
     * no longer be used
     */
    @Override
    public void dispose() {
        if (disposed) {
            // it's already disposed
            return;
        }
        backstack.dispose();
        destinations.clear();
        arguments.clear();
        context.dispose();
        backstack = null;
        destinations = null;
        arguments = null;
        homeDestination = null;
        context = null;
        content = null;
        disposed = true;
    }

    /////////////////////////////////////////////////////////////
    //                Private Related Methods                 //
    ///////////////////////////////////////////////////////////

    private Destination getDestinationOrThrow(String id) {
        Destination destination = getDestination(id);
        if (null==destination) {
            throw new NullPointerException("no destination found for id '"+id+"'");
        }
        return destination;
    }

    private void moveForward(Destination target, RouterOptions apply, RouterArgument data) {
        moveForwardPoppingUpto(target,data,apply,null,false);
    }

    private void moveForwardPoppingUpto(Destination target, RouterArgument data, RouterOptions apply,
                                        String popuptoTargetId, boolean popuptoInclusive) {
        String targetId = target.getId();

        Optional<RouterBackstackEntry> optional = backstack.findFirst(e -> e.getDestination().getId().equals(targetId));

        RouterArgument args = getArgumentForDestination(targetId);
        if (null!=data) {
            if (null==args) {
                args = data;
            }
            else {
                args.merge(data);
            }
        }
        else if (optional.isPresent()) {
            // in case of single top if no data provided then use the old data as saved in the backstack
            RouterArgument oldData = optional.get().getData();
            if (null==args) {
                args = oldData;
            }
            else {
                args.merge(oldData);
            }
        }
        if (null!=args) {
            args.accept();
        }

        if (null!=popuptoTargetId) {
            popBackstackInternal(popuptoTargetId,popuptoInclusive,apply);
        }
        else if (!backstack.isEmpty()) {
            RouterBackstackEntry entry = backstack.peekBackstackEntry();
            Destination exiting = entry.getDestination();
            RouterExecutor executor = getRouterExecutorForNameOrDefault(exiting.getExecutor());
            RouterOptions options = new RouterOptions(apply);
            if (exiting.isRemoveHistory()) {
                backstack.popBackstackEntry();
                options.setPopExitAnimation(options.getPopExitAnimation(getDefaultPopExitAnimation()));
                executor.popBackstack(exiting,options);
            }
            else {
                options.setExitAnimation(options.getExitAnimation(getDefaultExitAnimation()));
                executor.hide(exiting,options);
            }
        }

        RouterBackstackEntry entry;
        if (target.isSingleTop() && optional.isPresent()) {
            entry = optional.get();
            backstack.bringToTop(entry);
        }
        else {
            entry = new RouterBackstackEntry(target);
            backstack.pushBackstackEntry(entry);
        }
        entry.setData(args);

        RouterExecutor executor = getRouterExecutorForNameOrDefault(target.getExecutor());
        RouterOptions options = new RouterOptions(apply);
        options.setEnterAnimation(options.getEnterAnimation(getDefaultEnterAnimation()));
        executor.show(target,options);
    }

    private boolean moveBackward(String targetId, boolean inclusive, RouterArgument result, RouterOptions apply) {
        if (backstack.size()==1) {
            // backstack contains single entry so can not perform pop
            return false;
        }

        if (!popBackstackInternal(targetId,inclusive,apply)) {
            return false;
        }

        RouterBackstackEntry next = backstack.peekBackstackEntry();
        Destination showing = next.getDestination();
        RouterExecutor showingExecutor = getRouterExecutorForNameOrDefault(showing.getExecutor());
        RouterOptions options = new RouterOptions(apply);
        options.setPopBackstack(true);
        options.setPopEnterAnimation(options.getPopEnterAnimation(getDefaultPopEnterAnimation()));
        next.setResult(result);
        showingExecutor.show(showing,options);

        return true;
    }

    private boolean popBackstackInternal(String targetId, boolean inclusive, RouterOptions apply) {
        if (backstack.isEmpty()) {
            return false;
        }

        List<RouterBackstackEntry> popEntries = backstack.popBackstackEntriesUpTo(entry->entry.getDestination().getId().equals(targetId),inclusive);

        if (popEntries.isEmpty()) {
            return false;
        }

        for (RouterBackstackEntry entry : popEntries) {
            Destination destination = entry.getDestination();
            RouterExecutor executor = getRouterExecutorForNameOrDefault(destination.getExecutor());
            RouterOptions options = new RouterOptions(apply);
            options.setPopExitAnimation(options.getPopExitAnimation(getDefaultPopExitAnimation()));
            executor.popBackstack(destination,options);
        }

        return true;
    }

    /////////////////////////////////////////////////////////////
    //                      Sub Class                         //
    ///////////////////////////////////////////////////////////

    /**
     * Represents a single entry in backstack
     *
     * @see BackstackEntry
     */
    public static class RouterBackstackEntry implements BackstackEntry {

        private final Destination destination;

        private RouterArgument data;

        private String enterAnimation;

        private String exitAnimation;

        private String popExitAnimation;

        private String popEnterAnimation;

        private RouterArgument result;

        /**
         * Creates new {@code RouterBackstackEntry} instance for {@link Destination}
         *
         * @param destination {@code Destination} in the backstack entry
         */
        public RouterBackstackEntry(Destination destination) {
            this.destination = destination;
        }

        /**
         * Returns the {@link Destination} of the backstack entry
         *
         * @return non-null {@code Destination} instance
         */
        public Destination getDestination() {
            return destination;
        }

        /**
         * Returns the destination data
         *
         * @return a {@link RouterArgument} instance or {@code null}
         */
        public RouterArgument getData() {
            return data;
        }

        /**
         * Sets destination data
         *
         * @param data a {@link RouterArgument} instance
         */
        public void setData(RouterArgument data) {
            this.data = data;
        }

        /**
         * Returns screen enter animation name or id
         *
         * @return name or id of the animation
         * @deprecated since 1.1.0
         */
        @Deprecated
        public String getEnterAnimation() {
            return enterAnimation;
        }

        /**
         * Sets the screen enter animation name or id
         *
         * @param enterAnimation name or id of the animation
         * @deprecated since 1.1.0
         */
        @Deprecated
        public void setEnterAnimation(String enterAnimation) {
            this.enterAnimation = enterAnimation;
        }

        /**
         * Returns screen exit animation name or id
         *
         * @return name or id of the animation
         * @deprecated since 1.1.0
         */
        @Deprecated
        public String getExitAnimation() {
            return exitAnimation;
        }

        /**
         * Sets the screen exit animation name or id
         *
         * @param exitAnimation name or id of the animation
         * @deprecated since 1.1.0
         */
        @Deprecated
        public void setExitAnimation(String exitAnimation) {
            this.exitAnimation = exitAnimation;
        }

        /**
         * Returns screen pop exit animation name or id
         *
         * @return name or id of the animation
         * @deprecated since 1.1.0
         */
        @Deprecated
        public String getPopExitAnimation() {
            return popExitAnimation;
        }

        /**
         * Sets the screen pop exit animation name or id
         *
         * @param popExitAnimation name or id of the animation
         * @deprecated since 1.1.0
         */
        @Deprecated
        public void setPopExitAnimation(String popExitAnimation) {
            this.popExitAnimation = popExitAnimation;
        }

        /**
         * Returns screen pop enter animation name or id
         *
         * @return name or id of the animation
         * @deprecated since 1.1.0
         */
        @Deprecated
        public String getPopEnterAnimation() {
            return popEnterAnimation;
        }

        /**
         * Sets the screen pop enter animation name or id
         *
         * @param popEnterAnimation name or id of the animation
         * @deprecated since 1.1.0
         */
        @Deprecated
        public void setPopEnterAnimation(String popEnterAnimation) {
            this.popEnterAnimation = popEnterAnimation;
        }

        /**
         * Returns the destination result
         *
         * @return a {@link RouterArgument} instance or {@code null}
         */
        public RouterArgument getResult() {
            return result;
        }

        /**
         * Sets result for the destination
         *
         * @param result a {@link RouterArgument} instance
         */
        public void setResult(RouterArgument result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "RouterBackstackEntry{" +
                    "destination=" + destination +
                    ", data=" + data +
                    ", result=" + result +
                    '}';
        }
    }
}
