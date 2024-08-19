package rahulstech.jfx.routing;

import javafx.fxml.FXMLLoader;
import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.parser.AttributeSet;
import rahulstech.jfx.routing.util.Disposable;
import rahulstech.jfx.routing.util.ReflectionUtil;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
public abstract class RouterContext implements Disposable {

    public RouterContext() {}

    /////////////////////////////////////////////////////////////////
    //                   Cache Related Methods                    //
    ///////////////////////////////////////////////////////////////

    private HashMap<Object,Object> mCache = new HashMap<>();

    public void addToCache(Object key, Object value) {
        if (null==key) {
            throw new NullPointerException("cache key is null");
        }
        mCache.put(key,value);
    }

    public Object getFromCache(Object key) {
        return mCache.get(key);
    }

    public Object removeCache(Object key){
        return mCache.remove(key);
    }

    public void clearCache() {
        mCache.clear();
    }

    public  abstract void addAllAnimationAttributeSet(Collection<AttributeSet> attrs);

    public abstract RouterAnimation getAnimation(String nameOrId);

    public abstract FXMLLoader getFxmlLoader(String fxml, Class<?> controller, ResourceBundle bundle, Charset charset);

    public Object newControllerInstance(Class<?> clazz, Object... args) {
        return ReflectionUtil.newInstance(clazz,args);
    }

    public abstract RouterExecutor getRouterExecutorForName(String name, Router router);

    public abstract RouterExecutor getDefaultRouterExecutor(Router router);

    public Transaction getTransaction(Class<? extends Transaction> clazz, Object... args) {
        return (Transaction) ReflectionUtil.newInstance(clazz,args);
    }

    public abstract URL getResource(String name);

    public abstract InputStream getResourceAsStream(String name);

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
