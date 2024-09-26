package rahulstech.jfx.nestedrouterdemo;

import rahulstech.jfx.routing.BaseRouterContext;

import java.io.InputStream;
import java.net.URL;

public class NestedRouterDemoRouterContext extends BaseRouterContext {

    public NestedRouterDemoRouterContext() {}

    @Override
    public URL getResource(String name, String type) {
        return getClass().getResource("/"+name);
    }

    @Override
    public InputStream getResourceAsStream(String name, String type) {
        return getClass().getResourceAsStream("/"+name);
    }
}
