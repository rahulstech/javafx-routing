package rahulstech.jfx.historydemo;

import rahulstech.jfx.routing.BaseRouterContext;

import java.io.InputStream;
import java.net.URL;

public class HistoryDemoRouterContext extends BaseRouterContext {


    @Override
    public URL getResource(String name, String type) {
        return HistoryDemoApplication.class.getResource("/"+name);
    }

    @Override
    public InputStream getResourceAsStream(String name, String type) {
        return HistoryDemoApplication.class.getResourceAsStream("/"+name);
    }
}
