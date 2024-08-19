package rahulstech.jfx.routing.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AttributeValueConverter {

    public abstract Object convert(Attribute attr);

    public boolean isEmptyString(String text) {
        return null==text || text.trim().isEmpty();
    }

    public abstract boolean check(String value);

    public static Matcher match(String regex, String test) {
        return match(regex,test,0);
    }

    public static Matcher match(String regex, String test, int flag) {
        Pattern pattern = 0==flag ? Pattern.compile(regex) : Pattern.compile(regex,flag);
        return pattern.matcher(test);
    }
}
