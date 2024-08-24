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
     * The {@code NameValue} class represents a key-value pair where the key is
     * a string name and the value can be of any object type. It also contains
     * metadata such as the value's expected type and whether the value is required.
     */
    public static class NameValue {
        private final String name;
        private final Type type;
        private final boolean required;
        private Object value;

        public NameValue(String name, Type type) {
            this(name,type,false);
        }

        public NameValue(String name, Type type, boolean required) {
            this(name,type,required,null);
        }

        public NameValue(String name, Object value) {
            this(name,Type.ANY,false,value);
        }

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

        public String getName() {
            return name;
        }

        public Type getType() {
            return type;
        }

        public boolean isRequired() {
            return required;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        public <T> T getValue() {
            return (T) value;
        }

        public char getAsChar() {
            return (char) value;
        }

        public char[] getAsCharArray() {
            return (char[]) value;
        }

        public boolean getAsBoolean() {
            return (Boolean) value;
        }

        public boolean[] getAsBooleanArray() {
            return (boolean[]) value;
        }

        public Boolean[] getAsBooleanObjectArray() {
            return (Boolean[]) value;
        }

        public short getAsShort() {
            return ((Number) value).shortValue();
        }

        public short[] getAsShortArray() {
            return (short[]) value;
        }

        public Short[] getAsShortObjectArray() {
            return (Short[]) value;
        }

        public int getAsInt() {
            return ((Number) value).intValue();
        }

        public int[] getAsIntArray() {
            return (int[]) value;
        }

        public Integer[] getAsIntegerArray() {
            return (Integer[]) value;
        }

        public long getAsLong() {
            return ((Number) value).longValue();
        }

        public long[] getAsLongArray() {
            return (long[]) value;
        }

        public Long[] getAsLongObjectArray() {
            return (Long[]) value;
        }

        public float getAsFloat() {
            return ((Number) value).floatValue();
        }

        public float[] getAsFloatArray() {
            return (float[]) value;
        }

        public Float[] getAsFloatObjectArray() {
            return (Float[]) value;
        }

        public double getAsDouble() {
            return ((Number) value).doubleValue();
        }

        public double[] getAsDoubleArray() {
            return (double[]) value;
        }

        public Double[] getAsDoubleObjectArray() {
            return (Double[]) value;
        }

        public Number getAsNumber() {
            return (Number) value;
        }

        public String getAsString()  {
            return (String) value;
        }

        public String[] getAsStringArray() {
            return (String[]) value;
        }

        public boolean checkType() {
                return type.check(value);
        }

        public NameValue copyWithoutValue() {
            return new NameValue(name,type,required);
        }

        public void accept() {
            if (required && null==value) {
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
     * {@code INT}, as well as support for custom types.
     */
    public static final class Type {
        public static final Type ANY = new Type("any",Object.class);

        public static final Type BOOLEAN = new Type("boolean",boolean.class,Boolean.class);

        public static final Type BOOLEAN_ARRAY = new Type("boolean_array",boolean[].class,Boolean[].class);

        public static final Type CHAR = new Type("char",char.class,Character.class);

        public static final Type CHAR_ARRAY = new Type("char_array",char[].class,Character[].class);

        public static final Type SHORT = new Type("short",short.class,Short.class);

        public static final Type SHORT_ARRAY = new Type("short_array",short[].class,Short[].class);

        public static final Type INT = new Type("int",int.class, Integer.class);

        public static final Type INT_ARRAY = new Type("int_array",int[].class,Integer[].class);

        public static final Type LONG = new Type("long",long.class,Long.class);

        public static final Type LONG_ARRAY = new Type("long_array",long[].class,Long[].class);

        public static final Type FLOAT = new Type("float",float.class,Float.class);

        public static final Type FLOAT_ARRAY = new Type("float_array",float[].class,Float[].class);

        public static final Type DOUBLE = new Type("double",double.class,Double.class);

        public static final Type DOUBLE_ARRAY = new Type("double_array",double[].class,Double[].class);

        public static final Type STRING = new Type("string",String.class);

        public static final Type STRING_ARRAY = new Type("string_array",String[].class);

        private final String name;

        private final Class<?>[] types;

        public Type(String name, Class<?>... types) {
            this.name = name;
            this.types = types;
        }

        public Class<?>[] getTypes() {
            return types;
        }

        /**
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
    
