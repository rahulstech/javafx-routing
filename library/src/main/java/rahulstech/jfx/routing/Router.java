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
 *
 */
@SuppressWarnings("unused")
public class Router implements Disposable {

    public static final String KEY_SINGLE_SCENE_SCREEN_EXECUTOR = "rahulstech.jfx.routing.routerexecutor.SingleSceneScreenExecutor";

    public static final String KEY_DEFAULT_ROUTER_EXECUTOR = KEY_SINGLE_SCENE_SCREEN_EXECUTOR;

    public static final String KEY_RESULT = "rahulstech.jfx.routing.result";

    private Pane content;

    private Map<String,Destination> destinations;

    private Map<String,RouterArgument> arguments;

    private Backstack<RouterBackstackEntry> backstack;

    private Destination homeDestination;

    private String homeEnterAnimation;

    private RouterArgument homeData;

    private String defaultEnterAnimation;

    private String defaultExitAnimation;

    private String defaultPopEnterAnimation;

    private String defaultPopExitAnimation;

    private RouterContext context;

    private boolean disposed = false;

    /////////////////////////////////////////////////////////////
    //                     Constructors                       //
    ///////////////////////////////////////////////////////////

    public Router(RouterContext context) {
        this(context,null);
    }

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

    public RouterContext getContext() {
        return context;
    }

    public Pane getContentPane() {
        return content;
    }

    public void setContentPane(Pane content) {
        this.content = content;
    }

    public RouterExecutor getRouterExecutorForName(String name) {
        return context.getRouterExecutorForName(name,this);
    }

    public RouterExecutor getRouterExecutorForNameOrDefault(String name) {
        RouterExecutor executor = getRouterExecutorForName(name);
        if (null==executor) {
            return context.getDefaultRouterExecutor(this);
        }
        return executor;
    }

    public void setHomeDestination(String id) {
        Destination destination = destinations.get(id);
        if (null==destination) {
            throw new IllegalStateException("not destination found with id=\""+id+"\"," +
                    " make sure you have added the destination before calling this method");
        }
        homeDestination = destination;
    }

    public void setHomeDestination(Destination destination) {
        if (!destinations.containsKey(destination.getId())) {
            addAllDestination(destination);
        }
        homeDestination = destination;
    }

    public Destination getHomeDestination() {
        return homeDestination;
    }

    public void setHomeEnterAnimation(String homeEnterAnimation) {
        this.homeEnterAnimation = homeEnterAnimation;
    }

    public String getHomeEnterAnimation() {
        return homeEnterAnimation;
    }

    public void setDefaultAnimations(String enterAnimation, String exitAnimation, String popEnterAnimation, String popExitAnimation) {
        defaultEnterAnimation = enterAnimation;
        defaultExitAnimation = exitAnimation;
        defaultPopEnterAnimation = popEnterAnimation;
        defaultPopExitAnimation = popExitAnimation;
    }

    public String getDefaultEnterAnimation() {
        return defaultEnterAnimation;
    }

    public String getDefaultExitAnimation() {
        return defaultExitAnimation;
    }

    public String getDefaultPopEnterAnimation() {
        return defaultPopEnterAnimation;
    }

    public String getDefaultPopExitAnimation() {
        return defaultPopExitAnimation;
    }

    public void addAllDestination(Destination destination) {
        String id = destination.getId();
        if (destinations.containsKey(id)) {
            throw new IllegalStateException("destination with id "+id+" already added");
        }
        destinations.put(id,destination);
    }

    public void addAllDestination(Collection<Destination> destinations) {
        destinations.forEach(this::addAllDestination);
    }

    public Destination getDestination(String id) {
        return destinations.get(id);
    }

    public void addDestinationWithArgument(Destination destination, RouterArgument argument) {
        addAllDestination(destination);
        addArgument(destination.getId(),argument);
    }

    public RouterArgument setArgumentForDestination(String destinationId, RouterArgument argument) {
        return arguments.put(destinationId,argument);
    }

    public void addArgument(String id, RouterArgument argument) {
        if (arguments.containsKey(id)) {
            throw new IllegalStateException("argument with id '"+id+"' is already added");
        }
        arguments.put(id,argument);
    }

    public void addArguments(Map<String,RouterArgument> arguments) {
        arguments.forEach(this::addArgument);
    }

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

    public RouterArgument getArgument(String id) {
        RouterArgument args = arguments.get(id);
        if (null==args) {
            return null;
        }
        return args.copyWithoutValue();
    }

    public void setHomeData(RouterArgument data) {
        this.homeData = data;
    }

    public RouterArgument getHomeData() {
        return homeData;
    }

    /////////////////////////////////////////////////////////////
    //                      Backstack Methods                 //
    ///////////////////////////////////////////////////////////

    public Backstack<RouterBackstackEntry> getBackstack() {
        return backstack;
    }

    public RouterBackstackEntry getCurrentBackstackEntry() {
        if (!backstack.isEmpty()) {
            return backstack.peekBackstackEntry();
        }
        return null;
    }

    public RouterArgument getCurrentData() {
        RouterBackstackEntry top = getCurrentBackstackEntry();
        if (null==top) {
            return null;
        }
        return top.getData();
    }

    public Destination getCurrentDestination() {
        RouterBackstackEntry top = getCurrentBackstackEntry();
        if (null==top) {
            return null;
        }
        return top.getDestination();
    }

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

    public void moveto(String id) {
        Destination dest = getDestination(id);
        if (null==dest) {
            throw new IllegalStateException("no destination found with id '"+id+"'");
        }
        moveto(dest,null,null);
    }

    public void moveto(String id, RouterArgument data) {
        Destination dest = getDestination(id);
        if (null==dest) {
            throw new IllegalStateException("no destination found with id '"+id+"'");
        }
        moveto(dest,data,null);
    }

    public void moveto(String id, RouterArgument data, RouterOptions options) {
        Destination dest = getDestination(id);
        if (null==dest) {
            throw new IllegalStateException("no destination found with id '"+id+"'");
        }
        moveto(dest,data,options);
    }

    @SuppressWarnings("SameParameterValue")
    public void moveto(Destination target, RouterArgument data, RouterOptions apply) {
        RouterOptions options = new RouterOptions();
        if (null!=apply) {
            options.apply(apply);
        }
        moveForward(target,options,data);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean popBackStack() {
        return moveBackward(getCurrentDestination().getId(),null,true);
    }

    public boolean popBackstack(RouterArgument result) {
        return moveBackward(getCurrentDestination().getId(),result,true);
    }

    public boolean popBackstackUpTo(String targetId, boolean inclusive) {
        return moveBackward(targetId,null,inclusive);
    }

    public boolean popBackstackUpTo(String targetId,boolean inclusive, RouterArgument result) {
        return moveBackward(targetId,result,inclusive);
    }

    public void begin() {
        if (null==homeDestination) {
            throw new IllegalStateException("home not set; use setHomeDestination(String) to set home " +
                    "or add homeDestination attribute in router configuration xml file");
        }
        RouterOptions options = new RouterOptions();
        options.setEnterAnimation(homeEnterAnimation);
        moveForward(homeDestination,options,homeData);
    }

    /////////////////////////////////////////////////////////////
    //                      Lifecycle Methods                 //
    ///////////////////////////////////////////////////////////

    public void doLifecycleShow() {
        if (backstack.isEmpty()) {
            return;
        }
        Destination destination = getCurrentDestination();
        RouterExecutor executor = getRouterExecutorForNameOrDefault(destination.getExecutor());
        executor.doLifecycleShow(destination);
    }

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

    public void parse(String xml) {
        parse(context.getResourceAsStream(xml));
    }

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

    private void moveForward(Destination showing, RouterOptions options, RouterArgument data) {

        RouterArgument args = getArgumentForDestination(showing.getId());
        if (null!=data) {
            if (null==args) {
                args = data;
            }
            else {
                args.merge(data);
            }
        }
        if (null!=args) {
            args.accept();
        }

        options.setEnterAnimation(options.getEnterAnimation(getDefaultEnterAnimation()));
        options.setExitAnimation(options.getExitAnimation(getDefaultExitAnimation()));
        options.setPopEnterAnimation(options.getPopEnterAnimation(getDefaultPopEnterAnimation()));
        options.setPopExitAnimation(options.getPopExitAnimation(getDefaultPopExitAnimation()));

        RouterExecutor showExecutor = getRouterExecutorForNameOrDefault(showing.getExecutor());

        Destination hiding = null;
        if (!backstack.isEmpty()) {
            RouterBackstackEntry entry = backstack.peekBackstackEntry();
            hiding = entry.getDestination();
        }

        RouterBackstackEntry entry = new RouterBackstackEntry(showing);
        entry.setData(args);
        entry.setEnterAnimation(options.getEnterAnimation());
        entry.setExitAnimation(options.getExitAnimation());
        entry.setPopEnterAnimation(options.getPopEnterAnimation());
        entry.setPopExitAnimation(options.getPopExitAnimation());
        backstack.pushBackstackEntry(entry);

        if (null!=hiding) {
            RouterExecutor hideExecutor = getRouterExecutorForNameOrDefault(hiding.getExecutor());
            hideExecutor.hide(hiding,options);
        }
        showExecutor.show(showing,options);
    }

    private boolean moveBackward(String targetId, RouterArgument result, boolean inclusive) {
        if (backstack.size()==1) {
            // backstack contains single entry so can not perform pop
            return false;
        }

        // get the popped entries
        List<RouterBackstackEntry> popentries = backstack
                .popBackstackEntriesUpTo(entry-> targetId.equals(entry.getDestination().getId()),inclusive);

        // popentries will be empty if target not found
        if (popentries.isEmpty()) {
            return false;
        }

        RouterBackstackEntry top = popentries.remove(0);
        RouterBackstackEntry next = backstack.peekBackstackEntry();

        Destination popping = top.getDestination();
        Destination showing = next.getDestination();

        RouterOptions options = new RouterOptions();
        options.setPopEnterAnimation(top.getPopEnterAnimation());
        options.setPopExitAnimation(top.getPopExitAnimation());

        next.setResult(result);

        RouterExecutor poppingExecutor = getRouterExecutorForNameOrDefault(popping.getExecutor());
        RouterExecutor showingExecutor = getRouterExecutorForNameOrDefault(showing.getExecutor());

        showingExecutor.popBackstack(showing,options);
        poppingExecutor.hide(popping,options);

        // destroying all intermediate backstack entries
        popentries.forEach(entry->{
            Destination d = entry.getDestination();
            RouterExecutor executor = getRouterExecutorForNameOrDefault(d.getExecutor());
            executor.doLifecycleDestroy(d);
        });

        return true;
    }

    /////////////////////////////////////////////////////////////
    //                      Sub Class                         //
    ///////////////////////////////////////////////////////////

    public static class RouterBackstackEntry implements BackstackEntry {

        @SuppressWarnings("FieldMayBeFinal")
        private Destination destination;

        private RouterArgument data;

        private String enterAnimation;

        private String exitAnimation;

        private String popExitAnimation;

        private String popEnterAnimation;

        private RouterArgument result;

        public RouterBackstackEntry(Destination destination) {
            this.destination = destination;
        }

        public Destination getDestination() {
            return destination;
        }

        public RouterArgument getData() {
            return data;
        }

        public void setData(RouterArgument data) {
            this.data = data;
        }

        public String getEnterAnimation() {
            return enterAnimation;
        }

        public void setEnterAnimation(String enterAnimation) {
            this.enterAnimation = enterAnimation;
        }

        public String getExitAnimation() {
            return exitAnimation;
        }

        public void setExitAnimation(String exitAnimation) {
            this.exitAnimation = exitAnimation;
        }

        public String getPopExitAnimation() {
            return popExitAnimation;
        }

        public void setPopExitAnimation(String popExitAnimation) {
            this.popExitAnimation = popExitAnimation;
        }

        public String getPopEnterAnimation() {
            return popEnterAnimation;
        }

        public void setPopEnterAnimation(String popEnterAnimation) {
            this.popEnterAnimation = popEnterAnimation;
        }

        public RouterArgument getResult() {
            return result;
        }

        public void setResult(RouterArgument result) {
            this.result = result;
        }

        @Override
        public void dispose() {}

        @Override
        public String toString() {
            return "RouterBackstackEntry{" +
                    "destination=" + destination +
                    ", data=" + data +
                    ", enterAnimation='" + enterAnimation + '\'' +
                    ", exitAnimation='" + exitAnimation + '\'' +
                    ", popExitAnimation='" + popExitAnimation + '\'' +
                    ", popEnterAnimation='" + popEnterAnimation + '\'' +
                    ", result=" + result +
                    '}';
        }
    }
}
