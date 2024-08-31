package rahulstech.jfx.routing;

import javafx.fxml.FXMLLoader;
import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.parser.AttributeSet;
import rahulstech.jfx.routing.util.Disposable;
import rahulstech.jfx.routing.util.ReflectionUtil;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * The {@code RouterContext} class serves as the base class for managing the context in which a {@link Router}
 * operates. It provides methods for handling resources, creating instances, caching, and obtaining animations.
 *
 * <p>This abstract class is designed to be extended to provide specific implementations for the
 * management of resources, animations, and the creation of objects needed during routing operations.</p>
 *
 * @see Router
 * @see Disposable
 * @author Rahul Bagchi
 * @since 1.0
 */
public abstract class RouterContext implements Disposable {

    /**
     * Creates new {@code RouterContext} instance
     */
    public RouterContext() {}

    /**
     * Returns data for home destination
     *
     * @return {@link RouterArgument} instance as data for {@link Router#getHomeDestination() Router Home Destination} 
     *          or {@code null}
     */
    public RouterArgument getHomeData() {
        return null;
    }

    /**
     * Returns router configuration xml resource as {@link InputStream}
     *
     * @param name the name router configuration xml resource name
     * @return {@link InputStream} of the router configuration xml resource associated with the name or {@code null} if nothing found
     * @see #getResourceAsStream(String) 
     */
    public InputStream getRouterConfigurationAsStrem(String name) {
        return getResourceAsStream(name);
    }
    
    /////////////////////////////////////////////////////////////////
    //                   Cache Related Methods                    //
    ///////////////////////////////////////////////////////////////

    private HashMap<Object,Object> mCache = new HashMap<>();

    /**
     * Adds an object to the context cache.
     *
     * @param key the key to associate with the cached value
     * @param value the value to cache
     * @throws NullPointerException if the key is null
     */
    public void addToCache(Object key, Object value) {
        if (null==key) {
            throw new NullPointerException("cache key is null");
        }
        mCache.put(key,value);
    }

    /**
     * Returns cached value for {@code Key}
     *
     * @param key the key associated with the cached value
     * @return the cached value, or {@code null} if not found
     */
    public Object getFromCache(Object key) {
        return mCache.get(key);
    }

    /**
     * Removes an object from the context cache.
     *
     * @param key the key associated with the cached value
     * @return the removed value, or {@code null} if not found
     */
    public Object removeCache(Object key){
        return mCache.remove(key);
    }

    /**
     * Clears the entire context cache.
     */
    public void clearCache() {
        mCache.clear();
    }

    /**
     * Adds a collection of {@link AttributeSet} objects to the animation attribute set.
     *
     * @param attrs the collection of attribute sets to add
     */
    public  abstract void addAllAnimationAttributeSet(Collection<AttributeSet> attrs);

    /**
     * Retrieves a {@link RouterAnimation} based on its name or ID.
     *
     * @param nameOrId the name or ID of the animation
     * @return the corresponding {@link RouterAnimation} instance
     */
    public abstract RouterAnimation getAnimation(String nameOrId);

    /**
     * Returns a configured {@link FXMLLoader} for loading the specified FXML file.
     *
     * @param fxml the FXML file path
     * @param controller the class of the controller
     * @param bundle the resource bundle
     * @param charset the charset for loading the FXML
     * @return the configured {@link FXMLLoader} instance
     */
    public abstract FXMLLoader getFxmlLoader(String fxml, Class<?> controller, ResourceBundle bundle, Charset charset);

    /**
     * Creates a new instance of the specified controller class with provided arguments.
     *
     * @param clazz the class to instantiate
     * @param args the arguments to pass to the constructor
     * @return a new instance of the specified class
     */
    public Object newControllerInstance(Class<?> clazz, Object... args) {
        return ReflectionUtil.newInstance(clazz,args);
    }

    /**
     * Returns {@link RouterExecutor} for the name
     *
     * @param name the name of the executor
     * @param router the associated router
     * @return the corresponding {@link RouterExecutor} instance for he specified name
     */
    public abstract RouterExecutor getRouterExecutorForName(String name, Router router);

    /**
     * Returns default {@link RouterExecutor} for the {@link Router}
     *
     * @param router the associated router
     * @return the default {@link RouterExecutor} instance
     */
    public abstract RouterExecutor getDefaultRouterExecutor(Router router);

    /**
     * Returns {@link Transaction} for the name
     *
     * @param clazz the transaction class to instantiate
     * @param args the arguments to pass to the constructor
     * @return a new instance of the specified {@link Transaction} class using the provided arguments.
     */
    public Transaction getTransaction(Class<? extends Transaction> clazz, Object... args) {
        return (Transaction) ReflectionUtil.newInstance(clazz,args);
    }

    /**
     * Returns resource location {@link URL}
     *
     * @param name the name of the resource
     * @return the {@link URL} of the resource associated with the specified name, or {@code null} if not found
     */
    public abstract URL getResource(String name);

    /**
     * Returns resource as {@link InputStream}
     *
     * @param name the name of the resource
     * @return the {@link InputStream} of the resource associated with the specified name, or {@code null} if not found
     */
    public abstract InputStream getResourceAsStream(String name);

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        mCache.forEach((key,val)->{
            if (key instanceof Disposable) {
                ((Disposable) key).dispose();
            }
            if (val instanceof Disposable) {
                ((Disposable) val).dispose();
            }
        });
        mCache.clear();
        mCache = null;
    }
}
