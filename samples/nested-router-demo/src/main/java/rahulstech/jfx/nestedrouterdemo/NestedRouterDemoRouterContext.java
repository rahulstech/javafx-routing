package rahulstech.jfx.nestedrouterdemo;

import rahulstech.jfx.routing.BaseRouterContext;
import rahulstech.jfx.routing.Router;

import java.io.InputStream;
import java.net.URL;

public class NestedRouterDemoRouterContext extends BaseRouterContext {

    // store the reference of the parent router in the context
    // set the parent router when configuring router for sub screens
    // check the NestedRouterController
    private Router parentRouter;

    public NestedRouterDemoRouterContext() {}

    public void setParentRouter(Router parentRouter) {
        this.parentRouter = parentRouter;
    }

    public Router getParentRouter() {
        return parentRouter;
    }

    @Override
    public URL getResource(String name, String type) {
        return getClass().getResource("/"+name);
    }

    @Override
    public InputStream getResourceAsStream(String name, String type) {
        return getClass().getResourceAsStream("/"+name);
    }
}
