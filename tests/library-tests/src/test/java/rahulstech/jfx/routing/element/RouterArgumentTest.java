package rahulstech.jfx.routing.element;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RouterArgumentTest {

    static RouterArgument.Type compount_type = new RouterArgument.Type("int|boolean|string",
            int.class,Integer.class,boolean.class,Boolean.class,String.class);

    @ParameterizedTest
    @ArgumentsSource(TypeTestArgumentProvider.class)
    public void typeTest(RouterArgument.Type type, Object test) {
        boolean result = type.check(test);
        assertTrue(result,"type="+type+" test="+test);
    }

    @ParameterizedTest
    @ArgumentsSource(NameValueTestArgumentProvider.class)
    public void nameValueGetterMethodTest(RouterArgument.NameValue nv, Consumer<RouterArgument.NameValue> consumer) {
        consumer.accept(nv);
    }

    @ParameterizedTest
    @ArgumentsSource(AcceptTestArgumentsProvider.class)
    public void acceptTest(RouterArgument.NameValue arg, Class<? extends Exception> exception) {
        String message = "argument type="+arg.getType()+" required="+arg.isRequired()+" value="+arg.getValue()+" fails accept test";
        if (null==exception) {
            assertDoesNotThrow(arg::accept,message);
        }
        else {
            assertThrows(exception,arg::accept,message);
        }
    }

    @Test
    public void nameValueContructorTest() {

        AttributeSet attrs = new AttributeSet();
        attrs.add(new Attribute("name","arg"));
        attrs.add(new Attribute("required","true"));
        attrs.add(new Attribute("type","string"));

        RouterArgument.NameValue arg = new RouterArgument.NameValue(attrs);

        assertEquals("arg",arg.getName(),"name-value name mismatch");
        assertEquals(true,arg.isRequired(),"name-value required mismatch");
        assertEquals(RouterArgument.Type.STRING,arg.getType(),"name-value type mismatch");
    }

    static class TypeTestArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.arguments(RouterArgument.Type.ANY,null),
                    Arguments.arguments(RouterArgument.Type.BOOLEAN,true),
                    Arguments.arguments(RouterArgument.Type.BOOLEAN_ARRAY,new boolean[]{true}),
                    Arguments.arguments(RouterArgument.Type.BOOLEAN_ARRAY,new Boolean[]{Boolean.TRUE}),
                    Arguments.arguments(RouterArgument.Type.CHAR,'c'),
                    Arguments.arguments(RouterArgument.Type.CHAR_ARRAY,new char[]{'c'}),
                    Arguments.arguments(RouterArgument.Type.CHAR_ARRAY,new Character[]{'c'}),
                    Arguments.arguments(RouterArgument.Type.SHORT,(short) 1),
                    Arguments.arguments(RouterArgument.Type.SHORT_ARRAY,new short[]{1}),
                    Arguments.arguments(RouterArgument.Type.SHORT_ARRAY,new Short[]{1}),
                    Arguments.arguments(RouterArgument.Type.INT,1),
                    Arguments.arguments(RouterArgument.Type.INT_ARRAY,new int[]{1}),
                    Arguments.arguments(RouterArgument.Type.INT_ARRAY,new Integer[]{1}),
                    Arguments.arguments(RouterArgument.Type.LONG,1L),
                    Arguments.arguments(RouterArgument.Type.LONG_ARRAY,new long[]{1L}),
                    Arguments.arguments(RouterArgument.Type.LONG_ARRAY,new Long[]{1L}),
                    Arguments.arguments(RouterArgument.Type.FLOAT,1.0F),
                    Arguments.arguments(RouterArgument.Type.FLOAT_ARRAY,new float[]{1.0F}),
                    Arguments.arguments(RouterArgument.Type.FLOAT_ARRAY,new Float[]{1.0F}),
                    Arguments.arguments(RouterArgument.Type.DOUBLE,1.0),
                    Arguments.arguments(RouterArgument.Type.DOUBLE_ARRAY,new double[]{1.0}),
                    Arguments.arguments(RouterArgument.Type.DOUBLE_ARRAY,new Double[]{1.0}),
                    Arguments.arguments(RouterArgument.Type.STRING,"string"),
                    Arguments.arguments(RouterArgument.Type.STRING_ARRAY,new String[]{"string"}),
                    Arguments.arguments(new RouterArgument.Type("big_decimal", BigDecimal.class), new BigDecimal("1")),
                    Arguments.arguments(new RouterArgument.Type("big_decimal_array", BigDecimal[].class), new BigDecimal[]{new BigDecimal("1")}),
                    Arguments.arguments(compount_type,1),
                    Arguments.arguments(compount_type,true),
                    Arguments.arguments(compount_type,"string")
            );
        }
    }

    static class NameValueTestArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.CHAR, false, 'c'), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsChar),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.CHAR_ARRAY, false, new char[]{'c'}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsCharArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.CHAR, false, 'c'), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsCharacter),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.CHAR_ARRAY, false, new Character[]{'c'}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsCharacterArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.BOOLEAN, false, true), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsBoolean),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.BOOLEAN_ARRAY, false, new boolean[]{true}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsBooleanArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.BOOLEAN, false, true), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsBooleanObject),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.BOOLEAN_ARRAY, false, new Boolean[]{true}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsBooleanObjectArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.SHORT, false, (short) 1), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsShort),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.SHORT_ARRAY, false, new short[]{1}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsShortArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.SHORT, false, (short) 1), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsShortObject),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.SHORT_ARRAY, false, new Short[]{1}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsShortObjectArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.INT, false, 1), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsInt),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.INT_ARRAY, false, new int[]{1}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsIntArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.INT, false, 1), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsInteger),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.INT_ARRAY, false, new Integer[]{1}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsIntegerArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.LONG, false, (long) 1), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsLong),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.LONG_ARRAY, false, new long[]{1}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsLongArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.LONG, false, (long) 1), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsLongObject),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.LONG_ARRAY, false, new Long[]{1L}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsLongObjectArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.FLOAT, false, 1.0f), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsFloat),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.FLOAT_ARRAY, false, new float[]{1.0f}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsFloatArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.FLOAT, false, 1.0f), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsFloatObject),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.FLOAT_ARRAY, false, new Float[]{1.0f}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsFloatObjectArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.DOUBLE, false, (double) 1), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsDouble),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.DOUBLE_ARRAY, false, new double[]{1.0}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsDoubleArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.DOUBLE, false, (double) 1), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsDoubleObject),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.DOUBLE_ARRAY, false, new Double[]{1.0}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsDoubleObjectArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.STRING, false, "string"), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsString),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", RouterArgument.Type.STRING_ARRAY,false, new String[]{"string"}), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsStringArray),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", compount_type,false, 1), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsInt),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", compount_type,false, true), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsBoolean),
                    Arguments.arguments(new RouterArgument.NameValue("arg0", compount_type,false, "string"), (Consumer<RouterArgument.NameValue>) RouterArgument.NameValue::getAsString)

                    );
        }
    }

    static class AcceptTestArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.arguments(new RouterArgument.NameValue("arg", RouterArgument.Type.STRING,false,null),null),
                    Arguments.arguments(new RouterArgument.NameValue("arg", RouterArgument.Type.STRING,true,null),NullPointerException.class),
                    Arguments.arguments(new RouterArgument.NameValue("arg", RouterArgument.Type.STRING,true,1),IllegalArgumentException.class)
            );
        }
    }
}