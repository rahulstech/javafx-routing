package rahulstech.jfx.singlescenedemo;

import java.io.InputStream;
import java.net.URL;

public class ResourceLoader {

    private static final String RESOURCE_ROOT = "/";

    public static URL getLayoutResourceURL(String name) {
        String resource_name = RESOURCE_ROOT+name;
        return ResourceLoader.class.getResource(resource_name);
    }

    public static InputStream getRouterConfigInputStream(String name) {
        String resource_name = "/"+name;
        return ResourceLoader.class.getResourceAsStream(resource_name);
    }
}
