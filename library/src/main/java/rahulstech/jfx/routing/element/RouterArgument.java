package rahulstech.jfx.routing.element;

import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;
import rahulstech.jfx.routing.util.ReflectionUtil;
import rahulstech.jfx.routing.util.StringUtil;

import java.util.*;

/**
 * The {@code RouterArgument} class represents a collection of key-value pairs
 * (arguments) that can be used within a routing context. Each argument is
 * represented by a {@link NameValue} pair, where the key is a string name and
 * the value can be of any object type.
 *
 * <p>This class provides methods to add, retrieve, and manage these arguments.
 * It supports operations such as merging arguments from another {@code RouterArgument},
 * copying arguments without their values, and accepting the arguments based on
 * predefined constraints.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * RouterArgument args = new RouterArgument();
 * args.addArgument("userId", 123);
 * args.addArgument("token", "abc123");
 * args.addArgument(new NameValue("height",156.23));
 * }</pre>
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class RouterArgument {

    private Map<String,NameValue> map;

    /**
     * Creates new instance of {@code RouterArgument}
     */
    public RouterArgument() {}

    /**
     * Adds or updates an argument with the specified name and value.
     *
     * @param name the name of the argument
     * @param value the value of the argument
     */
    public void addArgument(String name, Object value) {
        NameValue nv = getArgument(name);
        if (null==nv) {
            addArgument(new NameValue(name,value));
        }
        else {
            nv.setValue(value);
        }
    }

    /**
     * Adds a {@link NameValue} argument to this {@code RouterArgument}.
     *
     * @param nv the {@link NameValue} instance to add
     * @throws NullPointerException if the {@code NameValue} instance is {@code null}
     */
    public void addArgument(NameValue nv) {
        if (null==nv) {
            throw new NullPointerException("NameValue must be non null");
        }
        createStorage();
        map.put(nv.getName(),nv);
    }

    /**
     * Checks if an argument with the specified name exists.
     *
     * @param name the name of the argument to check
     * @return {@code true} if the argument exists, {@code false} otherwise
     */
    public boolean contains(String name) {
        return null!=map && map.containsKey(name);
    }

    /**
     * Retrieves the {@link NameValue} argument associated with the specified name.
     *
     * @param name the name of the argument to retrieve
     * @return the {@link NameValue} instance, or {@code null} if it does not exist
     */
    public NameValue getArgument(String name) {
        if (null==map) {
            return null;
        }
        return map.get(name);
    }

    /**
     * Retrieves the value associated with the specified argument name, cast to the
     * desired type.
     *
     * @param <T> the desired type of the value
     * @param name the name of the argument to retrieve the value for
     * @return the value associated with the argument, or {@code null} if it does not exist
     */
    public <T> T getValue(String name) {
        NameValue nv = getArgument(name);
        if (null==nv) {
            return null;
        }
        return nv.getValue();
    }

    /**
     * Removes the argument with the specified name.
     *
     * @param name the name of the argument to remove
     * @return the removed {@link NameValue} instance, or {@code null} if it did not exist
     */
    public NameValue removeArgument(String name) {
        if (null==map) {
            return null;
        }
        return map.remove(name);
    }

    /**
     * Validates and accepts all the arguments in this {@code RouterArgument}.
     * If an argument is required but has no value, or if its value does not match
     * the expected type, an exception is thrown.
     *
     * @throws NullPointerException if a required argument has no value
     * @throws IllegalArgumentException if an argument's value does not match the expected type
     * @see NameValue#accept()
     */
    public void accept() {
        if (null!=map) {
            Collection<NameValue> nvs = map.values();
            for (NameValue nv : nvs) {
                nv.accept();
            }
        }
    }

    /**
     * Merges the arguments from another {@code RouterArgument} into this one.
     * If an argument with the same name exists in both instances, the value from
     * the other instance is used.
     *
     * @param other the {@code RouterArgument} to merge from
     */
    public void merge(RouterArgument other) {
        Map<String,NameValue> omap = other.map;
        if (null==omap) {
            return;
        }
        createStorage();
        map.forEach((k,v)->{
            NameValue nv = omap.remove(k);
            if (null!=nv) {
                v.setValue(nv.getValue());
            }
        });
        map.putAll(omap);
    }

    /**
     * Creates a copy of this {@code RouterArgument} without including the values
     * of the arguments.
     *
     * @return a new {@code RouterArgument} instance with the same argument names but without values
     */
    public RouterArgument copyWithoutValue() {
        RouterArgument args = new RouterArgument();
        if (map!=null) {
            args.map = new HashMap<>();
            map.forEach((k,v)-> args.map.put(k,v.copyWithoutValue()));
        }
        return args;
    }

    @Override
    public String toString() {
        return null==map ? "[]" : "["+map+"]";
    }

    private void createStorage() {
        if (null==map) {
            map = new HashMap<>();
        }
    }

    /**
     * The {@code NameValue} class represents a named value with a specific type,
     * and whether it is required or optional.
     * <p>
     * Each {@code NameValue} object holds a name, a type, a required flag.
     * The class provides methods to retrieve the value in various
     * data types (e.g., {@code int}, {@code boolean}, {@code String}), and to
     * validate that the value matches the specified type.
     * </p>
     * <p>
     * Usage example:
     * <pre>{@code
     *     NameValue param = new NameValue("example", Type.STRING, true, "Sample");
     *     param.accept(); // Validates the value and type
     *     String value = param.getAsString();
     *  }
     * </pre>
     */
    public static class NameValue {
        private final String name;
        private final Type type;
        private final boolean required;
        private Object value;

        /**
         * Constructs a {@code NameValue} with the specified name and type. The value is
         * not required by default.
         *
         * @param name the name of the parameter or attribute
         * @param type the data type of the value
         */
        public NameValue(String name, Type type) {
            this(name,type,false);
        }

        /**
         * Constructs a {@code NameValue} with the specified name, type, and required flag.
         *
         * @param name     the name of the parameter or attribute
         * @param type     the data type of the value
         * @param required whether the value is required
         */
        public NameValue(String name, Type type, boolean required) {
            this(name,type,required,null);
        }

        /**
         * Constructs a {@code NameValue} with the specified name and initial value.
         * The type is inferred as {@code Type.ANY}, and the value is not required by default.
         *
         * @param name  the name of the parameter or attribute
         * @param value the initial value of the parameter
         */
        public NameValue(String name, Object value) {
            this(name,Type.ANY,false,value);
        }

        /**
         * Constructs a {@code NameValue} from an {@code AttributeSet}. The name, required
         * flag, and type are extracted from the set.
         *
         * @param set the {@code AttributeSet} containing attributes for name, type, and required flag
         */
        public NameValue(AttributeSet set) {
            String name = set.get(Attribute.NAME).getValue();
            boolean required = set.getOrDefault(Attribute.REQUIRED,"false").getAsBoolean();
            String typeName = set.getOrDefault(Attribute.TYPE,"any").getValue();
            Type type = Type.get(typeName);
            this.name = name;
            this.required = required;
            this.type = type;
        }

        NameValue(String name, Type type, boolean required, Object value) {
            if (StringUtil.isEmpty(name)) {
                throw new IllegalArgumentException("empty name");
            }
            this.name = name;
            this.type = null==type ? Type.ANY : type;
            this.required = required;
            this.value = value;
        }

        /**
         * Returns the name of the parameter or attribute.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the type of the value.
         *
         * @return the data type
         */
        public Type getType() {
            return type;
        }

        /**
         * Returns whether the value is required.
         *
         * @return {@code true} if the value is required; {@code false} otherwise
         */
        public boolean isRequired() {
            return required;
        }

        /**
         * Sets the value of the parameter or attribute.
         *
         * @param value the value to set
         */
        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * Returns the value of the parameter or attribute cast to the specified type.
         *
         * @param <T> the expected type of the value
         * @return the value cast to the specified type
         */
        @SuppressWarnings("unchecked")
        public <T> T getValue() {
            return (T) value;
        }

        /**
         * Returns the value as a {@code byte}.
         *
         * @return the value as a {@code byte}
         */
        public byte getAsByte() {
            return (byte) value;
        }

        /**
         * Returns the value as a {@code byte[]}.
         *
         * @return the value as a {@code byte[]}
         */
        public char[] getAsByteArray() {
            return (char[]) value;
        }

        /**
         * Returns the value as a {@code char}.
         *
         * @return the value as a {@code char}
         */
        public char getAsChar() {
            return (char) value;
        }

        /**
         * Returns the value as a {@code char[]}.
         *
         * @return the value as a {@code char[]}
         */
        public char[] getAsCharArray() {
            return (char[]) value;
        }

        /**
         * Returns the value as a {@code boolean}.
         *
         * @return the value as a {@code boolean}
         */
        public boolean getAsBoolean() {
            return (Boolean) value;
        }

        /**
         * Returns the value as a {@code boolean[]}.
         *
         * @return the value as a {@code boolean[]}
         */
        public boolean[] getAsBooleanArray() {
            return (boolean[]) value;
        }

        /**
         * Returns the value as a {@code Boolean}.
         *
         * @return the value as a {@code Boolean}
         */
        public Boolean getAsBooleanObject() {
            return (Boolean) value;
        }

        /**
         * Returns the value as a {@code Boolean[]}.
         *
         * @return the value as a {@code Boolean[]}
         */
        public Boolean[] getAsBooleanObjectArray() {
            return (Boolean[]) value;
        }

        /**
         * Returns the value as a {@code short}.
         *
         * @return the value as a {@code short}
         */
        public short getAsShort() {
            return ((Number) value).shortValue();
        }

        /**
         * Returns the value as a {@code short[]}.
         *
         * @return the value as a {@code short[]}
         */
        public short[] getAsShortArray() {
            return (short[]) value;
        }

        /**
         * Returns the value as a {@code Short}.
         *
         * @return the value as a {@code Short}
         */
        public Short getAsShortObject() {
            return (Short) value;
        }

        /**
         * Returns the value as a {@code Short[]}.
         *
         * @return the value as a {@code Short[]}
         */
        public Short[] getAsShortObjectArray() {
            return (Short[]) value;
        }

        /**
         * Returns the value as an {@code int}.
         *
         * @return the value as an {@code int}
         */
        public int getAsInt() {
            return ((Number) value).intValue();
        }

        /**
         * Returns the value as an {@code int[]}.
         *
         * @return the value as an {@code int[]}
         */
        public int[] getAsIntArray() {
            return (int[]) value;
        }

        /**
         * Returns the value as an {@code Integer}.
         *
         * @return the value as an {@code Integer}
         */
        public Integer getAsInteger() {
            return (Integer) value;
        }

        /**
         * Returns the value as an {@code Integer[]}.
         *
         * @return the value as an {@code Integer[]}
         */
        public Integer[] getAsIntegerArray() {
            return (Integer[]) value;
        }

        /**
         * Returns the value as a {@code long}.
         *
         * @return the value as a {@code long}
         */
        public long getAsLong() {
            return ((Number) value).longValue();
        }

        /**
         * Returns the value as a {@code long[]}.
         *
         * @return the value as a {@code long[]}
         */
        public long[] getAsLongArray() {
            return (long[]) value;
        }

        /**
         * Returns the value as a {@code Long}.
         *
         * @return the value as a {@code Long}
         */
        public long getAsLongObject() {
            return (Long) value;
        }

        /**
         * Returns the value as a {@code Long[]}.
         *
         * @return the value as a {@code Long[]}
         */
        public Long[] getAsLongObjectArray() {
            return (Long[]) value;
        }

        /**
         * Returns the value as a {@code float}.
         *
         * @return the value as a {@code float}
         */
        public float getAsFloat() {
            return ((Number) value).floatValue();
        }

        /**
         * Returns the value as a {@code float[]}.
         *
         * @return the value as a {@code float[]}
         */
        public float[] getAsFloatArray() {
            return (float[]) value;
        }

        /**
         * Returns the value as a {@code Float}.
         *
         * @return the value as a {@code Float}
         */
        public Float getAsFloatObject() {
            return (Float) value;
        }

        /**
         * Returns the value as a {@code Float[]}.
         *
         * @return the value as a {@code Float[]}
         */
        public Float[] getAsFloatObjectArray() {
            return (Float[]) value;
        }

        /**
         * Returns the value as a {@code double}.
         *
         * @return the value as a {@code double}
         */
        public double getAsDouble() {
            return ((Number) value).doubleValue();
        }

        /**
         * Returns the value as a {@code double[]}.
         *
         * @return the value as a {@code double[]}
         */
        public double[] getAsDoubleArray() {
            return (double[]) value;
        }

        /**
         * Returns the value as a {@code Double}.
         *
         * @return the value as a {@code Double}
         */
        public double getAsDoubleObject() {
            return (Double) value;
        }

        /**
         * Returns the value as a {@code Double[]}.
         *
         * @return the value as a {@code Double[]}
         */
        public Double[] getAsDoubleObjectArray() {
            return (Double[]) value;
        }

        /**
         * Returns the value as a {@code Number}.
         *
         * @return the value as a {@code Number}
         */
        public Number getAsNumber() {
            return (Number) value;
        }

        /**
         * Returns the value as a {@code String}.
         *
         * @return the value as a {@code String}
         */
        public String getAsString() {
            return (String) value;
        }

        /**
         * Returns the value as a {@code String[]}.
         *
         * @return the value as a {@code String[]}
         */
        public String[] getAsStringArray() {
            return (String[]) value;
        }

        /**
         * Checks if the value matches the specified type.
         *
         * @return {@code true} if the value matches the type; {@code false} otherwise
         */
        public boolean checkType() {
            return type.check(value);
        }

        /**
         * Creates a copy of this {@code NameValue} without the value.
         *
         * @return a copy of this {@code NameValue} without the value
         */
        public NameValue copyWithoutValue() {
            return new NameValue(name, type, required);
        }

        /**
         * Validates the value against the type and required flag. If the value is required
         * and not provided, or if the value does not match the type, an exception is thrown.
         *
         * @throws NullPointerException     if the value is required and not provided
         * @throws IllegalArgumentException if the value does not match the specified type
         */
        public void accept() {
            if (!type.equals(Type.ANY) && required && null==value) {
                throw new NullPointerException("argument '"+name+"' is required");
            }
            if (!checkType()) {
                throw new IllegalArgumentException(name+" requires type '"+type.name+"' but found "+(null==value ? "null" : value.getClass().getName()));
            }
        }

        @Override
        public String toString() {
            return "NameValue{" +
                    "name='" + name + '\'' +
                    ", type=" + type +
                    ", required=" + required +
                    ", value=" + value +
                    '}';
        }
    }

    /**
     * The {@code Type} class represents a data type for {@link NameValue} instances.
     * It includes predefined types such as {@code BOOLEAN}, {@code STRING}, and
     * {@code INT}, as well as support for custom types. Also supports combination of
     * multiple types. To define combination of multiple types in xml use {@code |} syntax like below:
     * <pre>{@code
     * int|int_array|string
     * }
     * </pre>
     */
    public static final class Type {

        /**
         * Accept any type including {@code null}
         */
        public static final Type ANY = new Type("any",Object.class);

        /**
         * Accept only {@code boolean} or {@code Boolean}
         */
        public static final Type BOOLEAN = new Type("boolean",boolean.class,Boolean.class);

        /**
         * Accept only {@code boolean[]} or {@code Boolean[]}
         */
        public static final Type BOOLEAN_ARRAY = new Type("boolean_array",boolean[].class,Boolean[].class);

        /**
         * Accept only {@code char} or {@code Character}
         */
        public static final Type CHAR = new Type("char",char.class,Character.class);

        /**
         * Accept only {@code char[]} or {@code Character[]}
         */
        public static final Type CHAR_ARRAY = new Type("char_array",char[].class,Character[].class);

        /**
         * Accept only {@code short} or {@code Short}
         */
        public static final Type SHORT = new Type("short",short.class,Short.class);

        /**
         * Accept only {@code short[]} or {@code Short[]}
         */
        public static final Type SHORT_ARRAY = new Type("short_array",short[].class,Short[].class);

        /**
         * Accept only {@code int} or {@code Integer}
         */
        public static final Type INT = new Type("int",int.class, Integer.class);

        /**
         * Accept only {@code int[]} or {@code Integer[]}
         */
        public static final Type INT_ARRAY = new Type("int_array",int[].class,Integer[].class);

        /**
         * Accept only {@code long} or {@code Long}
         */
        public static final Type LONG = new Type("long",long.class,Long.class);

        /**
         * Accept only {@code long[]} or {@code Long[]}
         */
        public static final Type LONG_ARRAY = new Type("long_array",long[].class,Long[].class);

        /**
         * Accept only {@code float} or {@code Float}
         */
        public static final Type FLOAT = new Type("float",float.class,Float.class);

        /**
         * Accept only {@code float[]} or {@code Float[]}
         */
        public static final Type FLOAT_ARRAY = new Type("float_array",float[].class,Float[].class);

        /**
         * Accept only {@code double} or {@code Double}
         */
        public static final Type DOUBLE = new Type("double",double.class,Double.class);

        /**
         * Accept only {@code double[]} or {@code Double[]}
         */
        public static final Type DOUBLE_ARRAY = new Type("double_array",double[].class,Double[].class);

        /**
         * Accept only {@code String}
         */
        public static final Type STRING = new Type("string",String.class);

        /**
         * Accept only {@code String[]}
         */
        public static final Type STRING_ARRAY = new Type("string_array",String[].class);

        private final String name;

        private final Class<?>[] types;

        /**
         * Creates new instance of {@code Type} with name and accepted {@link Class}s
         *
         * @param name name of the type
         * @param types one or more accepted {@code Class}s
         */
        public Type(String name, Class<?>... types) {
            this.name = name;
            this.types = types;
        }

        /**
         * Returns {@link Class}s accepted by this {@code Type}
         *
         * @return non-null array of one or more accepted {@code Class}s
         */
        public Class<?>[] getTypes() {
            return types;
        }

        /**
         * Checkes if the {@code value} object type is accepted by this {@code NameValue}
         *
         * @param test the target value for type test
         * @return checks if the value type is accepted by this {@code Type};
         *          {@code true} if the value type matches of the accepted types,
         *          {@code false} otherwise
         */
        public boolean check(Object test) {
            if (null==test) {
                return nullTest();
            }
            int count = null==types ? 0 : types.length;
            for (int i=0; i<count; i++) {
                Class<?> type = types[i];
                if (type.isInstance(test)) {
                    return true;
                }
            }
            return false;
        }

        private boolean nullTest() {
            for (java.lang.reflect.Type type : types) {
                if (type==Object.class) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Type type = (Type) o;
            return Objects.equals(name, type.name) && Objects.deepEquals(types, type.types);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, Arrays.hashCode(types));
        }

        /**
         * Returns a {@code Type} instance corresponding to the given type name.
         *
         * @param type the name of the type
         * @return the {@code Type} instance
         * @throws IllegalArgumentException if the type name is unknown
         */
        public static Type get(String type) {
            switch (type) {
                case "any": return ANY;
                case "boolean": return BOOLEAN;
                case "boolean_array": return BOOLEAN_ARRAY;
                case "char": return CHAR;
                case "char_array": return CHAR_ARRAY;
                case "short": return SHORT;
                case "short_array": return SHORT_ARRAY;
                case "int": return INT;
                case "int_array": return INT_ARRAY;
                case "long": return LONG;
                case "long_array": return LONG_ARRAY;
                case "float": return FLOAT;
                case "float_array": return FLOAT_ARRAY;
                case "double": return DOUBLE;
                case "double_array": return DOUBLE_ARRAY;
                case "string": return STRING;
                case "string_array": return STRING_ARRAY;
            }

            if (type.matches("[A-z]+(\\|[A-z]+)+")) {
                String[] types = type.split("\\|");
                ArrayList<Class<?>> classes = new ArrayList<>();
                for (String _type : types) {
                    Type __type = get(_type);
                    classes.addAll(Arrays.asList(__type.getTypes()));
                }
                Class<?>[] _types = classes.toArray(new Class<?>[0]);
                return new Type(type,_types);
            }

            try {
                Class<?> _type = ReflectionUtil.getClassForName(type);
                return new Type(type,_type);
            }
            catch (Exception ignore){}

            throw new IllegalArgumentException("unknown type '"+type+"'");
        }
    }
}
    
