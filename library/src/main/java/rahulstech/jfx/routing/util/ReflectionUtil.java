package rahulstech.jfx.routing.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
            Constructor<?> constructor = clazz.getDeclaredConstructor(getTypes(args));
            if (constructor.trySetAccessible()) {
                return constructor.newInstance(args);
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Creating a new instance of class " + clazz.getName() + " failed with an exception", e);
        }
        throw new IllegalStateException("No suitable constructor found for class " + clazz.getName());
    }

    /**
     * Returns an array of {@code Class} objects that match the types of the given arguments.
     *
     * @param args the arguments to determine the types of, or {@code null} if no arguments
     * @return an array of {@code Class} objects representing the types of the arguments
     */
    static Class<?>[] getTypes(Object[] args) {
        if (args == null || args.length == 0) {
            return new Class<?>[0];
        }
        int count = args.length;
        Class<?>[] types = new Class<?>[count];
        for (int i = 0; i < count; i++) {
            types[i] = args[i].getClass();
        }
        return types;
    }
}
