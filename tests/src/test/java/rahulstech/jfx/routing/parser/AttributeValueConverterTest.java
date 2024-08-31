package rahulstech.jfx.routing.parser;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import rahulstech.jfx.routing.parser.converter.BooleanConverter;
import rahulstech.jfx.routing.parser.converter.DurationConverter;
import rahulstech.jfx.routing.parser.converter.NumberConverter;
import rahulstech.jfx.routing.parser.converter.SizeConverter;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AttributeValueConverterTest {

    @ParameterizedTest
    @ArgumentsSource(CheckTestArgumentProvider.class)
    public void checkTest(AttributeValueConverter converter, String value, boolean expected) {
        assertEquals(expected,converter.check(value),"converter="+converter.getClass()+" value="+value+" expected="+expected);
    }

    static class CheckTestArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.arguments(new BooleanConverter(),"true",true),
                    Arguments.arguments(new BooleanConverter(),"FaLsE",true),
                    Arguments.arguments(new DurationConverter(),"250ms",true),
                    Arguments.arguments(new DurationConverter(),"250Ms",true),
                    Arguments.arguments(new DurationConverter(),".2s",true),
                    Arguments.arguments(new DurationConverter(),"0.2S",true),
                    Arguments.arguments(new DurationConverter(),"2s",true),
                    Arguments.arguments(new DurationConverter(),"duration_long",true),
                    Arguments.arguments(new DurationConverter(),"DUraTIoN_SHOrt",true),
                    Arguments.arguments(new SizeConverter(),"5%",true),
                    Arguments.arguments(new SizeConverter(),".5%",true),
                    Arguments.arguments(new SizeConverter(),"0.5%",true),
                    Arguments.arguments(new SizeConverter(),"5%p",true),
                    Arguments.arguments(new SizeConverter(),".5%p",true),
                    Arguments.arguments(new SizeConverter(),"0.5%p",true),
                    Arguments.arguments(new SizeConverter(),"5px",true),
                    Arguments.arguments(new SizeConverter(),".5px",true),
                    Arguments.arguments(new SizeConverter(),"0.5px",true),
                    Arguments.arguments(new SizeConverter(),"5deg",true),
                    Arguments.arguments(new SizeConverter(),".5deg",true),
                    Arguments.arguments(new SizeConverter(),"0.5deg",true),
                    Arguments.arguments(new SizeConverter(),"5rad",true),
                    Arguments.arguments(new SizeConverter(),".5rad",true),
                    Arguments.arguments(new SizeConverter(),"0.5rad",true),
                    Arguments.arguments(new NumberConverter(),"5",true),
                    Arguments.arguments(new NumberConverter(),".5",true),
                    Arguments.arguments(new NumberConverter(),"0.5",true),
                    Arguments.arguments(new NumberConverter(),"-5",true),
                    Arguments.arguments(new NumberConverter(),"-.5",true),
                    Arguments.arguments(new NumberConverter(),"-0.5",true),

                    Arguments.arguments(new BooleanConverter(),"unknown",false), // illegal boolean
                    Arguments.arguments(new DurationConverter(),".25ms",false), // fractional millisecond
                    Arguments.arguments(new DurationConverter(),"250 ms",false), // space between number and unit for duration
                    Arguments.arguments(new DurationConverter(),"duration_medium",false), // unknown duration constant
                    Arguments.arguments(new SizeConverter(),"5dp",false), // unknown unit
                    Arguments.arguments(new SizeConverter(),"5 px",false), // space between number and unit for size
                    Arguments.arguments(new NumberConverter(),"+5",false) // illegal character '+'
            );
        }
    }
}