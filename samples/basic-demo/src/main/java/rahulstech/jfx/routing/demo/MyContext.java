package rahulstech.jfx.routing.demo;

import rahulstech.jfx.routing.BaseRouterContext;

import java.io.InputStream;
import java.net.URL;

public class MyContext extends BaseRouterContext {

    public MyContext() {}

    @Override
    public URL getResource(String name, String type) {
        String resource_name = "/"+name;
        return MyApplication.class.getResource(resource_name);
    }

    @Override
    public InputStream getResourceAsStream(String name, String type) {
        String resource_name = "/"+name;
        return MyApplication.class.getResourceAsStream(resource_name);
    }
}