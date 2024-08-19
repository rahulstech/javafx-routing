package rahulstech.jfx.singlescenedemo;

import rahulstech.jfx.routing.BaseRouterContext;

import java.io.InputStream;
import java.net.URL;

public class DemoRouterContext extends BaseRouterContext {

    @Override
    public URL getResource(String name, String type) {
        if ("fxml".equals(type)) {
            return ResourceLoader.getLayoutResourceURL(name);
        }
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String name, String type) {
        if ("xml".equals(type)) {
            return ResourceLoader.getRouterConfigInputStream(name);
        }
        return null;
    }
}
