package rahulstech.jfx.routing.demo.backstackcallback;

import rahulstech.jfx.routing.BaseRouterContext;

import java.io.InputStream;
import java.net.URL;

public class DemoRouterContext extends BaseRouterContext {

    DemoApplication app;

    public DemoRouterContext(DemoApplication app) {
        this.app = app;
    }

    public DemoApplication getApp() {
        return app;
    }

    @Override
    public URL getResource(String name, String type) {
        return DemoApplication.class.getResource("/"+name);
    }

    @Override
    public InputStream getResourceAsStream(String name, String type) {
        return DemoApplication.class.getResourceAsStream("/"+name);
    }
}
