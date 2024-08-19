package rahulstech.jfx.routing.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused")
public class ReflectionUtil {

    private ReflectionUtil() {}

    public static Class<?> getClassForName(String name) {
        try {
            return Class.forName(name);
        }
        catch (ClassNotFoundException ex) {
            throw new RuntimeException("no class found for name "+name);
        }
    }

    public static Object newInstance(String clazz) {
        return newInstance(clazz,null);
    }

    public static Object newInstance(String name, Object[] args) {
        Class<?> clazz = getClassForName(name);
        return newInstance(clazz,args);
    }

    public static Object newInstance(Class<?> clazz) {
        return newInstance(clazz,null);
    }

    public static Object newInstance(Class<?> clazz, Object[] args) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(getTypes(args));
            if (constructor.trySetAccessible()) {
                return constructor.newInstance(args);
            }
        }
        catch (NoSuchMethodException|InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("create new instance of class "+clazz.getName()+" failed with exception",e);
        }
        throw new IllegalStateException("no suitable constructor found for class "+clazz.getName());
    }

    static Class<?>[] getTypes(Object[] args) {
        if (null==args || args.length==0) {
            return new Class<?>[0];
        }
        int count = args.length;
        Class<?>[] types = new Class<?>[count];
        for (int i=0; i<count; i++) {
            types[i] = args[i].getClass();
        }
        return types;
    }
}
