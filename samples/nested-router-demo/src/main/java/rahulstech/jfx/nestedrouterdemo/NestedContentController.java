package rahulstech.jfx.nestedrouterdemo;

import rahulstech.jfx.routing.layout.RouterStackPane;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

// this controller is responsible to instanciating
// and disposing of the router for sub screens
public class NestedContentController extends SimpleLifecycleAwareController {

    private NestedRouterDemoRouterContext context;

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        RouterStackPane root = new RouterStackPane();
        root.setRouterConfig("sub_router.xml");
        context = new NestedRouterDemoRouterContext();
        root.setContext(context);

        // parent Router is directly added to the child Router
        root.routerProperty().addListener((ov,oldValue,newValue) -> {
            if (null != oldValue) {
                oldValue.setParentRouter(null);
            }
            if (null != newValue) {
                newValue.setParentRouter(getRouter());
            }
        });

        setRoot(root);
    }

    @Override
    public void onLifecycleDestroy() {
        super.onLifecycleDestroy();
        // dispose the router of the sub screens
        ((RouterStackPane) getRoot()).getRouter().dispose();
    }
}
