package rahulstech.jfx.routing.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * Utility class providing methods for dynamic class loading and instance creation using reflection.
 *
 * <p>The {@code ReflectionUtil} class offers methods to:
 * <ul>
 *   <li>Get a {@link Class} object from its fully qualified name.</li>
 *   <li>Create new instances of classes by dynamically invoking constructors.</li>
 * </ul>
 *
 * <p>This class is designed to be a helper for scenarios where dynamic behavior is required, such as
 * loading classes at runtime, and is particularly useful in frameworks or libraries that need to
 * instantiate classes with varying constructors.
 *
 * <p>All methods in this utility class are static, and the class itself is not instantiable.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 *     // Get a Class object
 *     Class<?> myClass = ReflectionUtil.getClassForName("com.example.MyClass");
 *
 *     // Create a new instance with no arguments
 *     Object instance = ReflectionUtil.newInstance("com.example.MyClass");
 *
 *     // Create a new instance with arguments
 *     Object instanceWithArgs = ReflectionUtil.newInstance("com.example.MyClass", new Object[] { "arg1", 123 });
 * }</pre>
 *
 * <p>This class handles exceptions by rethrowing them as unchecked {@link RuntimeException}s,
 * making it easier to use in contexts where checked exceptions would be cumbersome.</p>
 *
 * @see Class#forName(String)
 * @see Constructor
 * @see RuntimeException
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class ReflectionUtil {

    private ReflectionUtil() {}

    /**
     * Returns the {@link Class} object associated with the class or interface with the given string name.
     *
     * @param name the fully qualified name of the desired class
     * @return the {@code Class} object for the class with the specified name
     * @throws RuntimeException if the class cannot be located
     */
    public static Class<?> getClassForName(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("No class found for name " + name, ex);
        }
    }

    /**
     * Creates a new instance of the class specified by its fully qualified name using the default constructor.
     *
     * @param clazz the fully qualified name of the class
     * @return a new instance of the specified class
     * @throws RuntimeException if the instance cannot be created
     */
    public static Object newInstance(String clazz) {
        return newInstance(clazz, null);
    }

    /**
     * Creates a new instance of the class specified by its fully qualified name and constructor arguments.
     *
     * @param name the fully qualified name of the class
     * @param args the arguments to pass to the constructor, or {@code null} for the default constructor
     * @return a new instance of the specified class
     * @throws RuntimeException if the instance cannot be created
     */
    public static Object newInstance(String name, Object[] args) {
        Class<?> clazz = getClassForName(name);
        return newInstance(clazz, args);
    }

    /**
     * Creates a new instance of the specified class using the default constructor.
     *
     * @param clazz the {@code Class} object
     * @return a new instance of the specified class
     * @throws RuntimeException if the instance cannot be created
     */
    public static Object newInstance(Class<?> clazz) {
        return newInstance(clazz, null);
    }

    /**
     * Creates a new instance of the specified class using the constructor that matches the given arguments.
     *
     * @param clazz the {@code Class} object
     * @param args the arguments to pass to the constructor, or {@code null} for the default constructor
     * @return a new instance of the specified class
     * @throws RuntimeException if the instance cannot be created
     * @throws IllegalStateException if no suitable constructor is found
     */
    public static Object newInstance(Class<?> clazz, Object[] args) {
        try {
            Constructor<?> constructor = findConstructor(clazz,args);
            if (constructor.trySetAccessible()) {
                return constructor.newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException("Can not create a new instance of class '" + clazz + "'", e);
        }
        throw new IllegalStateException("No accessible constructor found for class '" + clazz +"'");
    }

    static Constructor<?> findConstructor(Class<?> clazz, Object[] args) throws NoSuchMethodException {
        int argsCount = null==args ? 0 : args.length;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (argsCount != constructor.getParameterCount()) {
                continue;
            }
            if (argsCount==0) {
                return constructor;
            }
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            boolean matched = true;
            for (int i=0; i<argsCount; i++) {
                if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                return constructor;
            }
        }

        String[] argTypeNames = null==args ? new String[0]
                : Arrays.stream(args).map(arg->arg.getClass().getName()).toArray(String[]::new);

        throw new NoSuchMethodException("no suitable constructor found for class '"+clazz+"'" +
                " with parameter types "+Arrays.toString(argTypeNames));
    }
}
