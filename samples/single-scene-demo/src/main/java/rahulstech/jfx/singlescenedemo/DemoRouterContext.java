package rahulstech.jfx.singlescenedemo;

import rahulstech.jfx.routing.BaseRouterContext;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.RouterExecutor;
import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.element.RouterCompoundAnimation;
import rahulstech.jfx.routing.element.animation.ScaleAnimation;
import rahulstech.jfx.routing.element.animation.SlideAnimation;
import rahulstech.jfx.routing.parser.AttributeSet;
import rahulstech.jfx.routing.transaction.SingleSceneTransaction;

import java.io.InputStream;
import java.net.URL;

public class DemoRouterContext extends BaseRouterContext {

    public static final String ANIMATION_SCALE_UP_XY_SLIDE_IN_BOTTOM = "scale_up_xy_slide_in_bottom";

    public static final String ANIMATION_SCALE_DOWN_XY_SLIDE_OUT_BOTTOM = "scale_down_xy_slide_out_bottom";

    public static final String DEMO_EXECUTOR = "rahulstech.jfx.singlescenedemo.DemoRouterExecutor";

    @Override
    protected RouterExecutor createRouterExecutor(String name, Router router) {
        if (DEMO_EXECUTOR.equals(name)) {
            return new DemoRouterExecutor(router,(SingleSceneTransaction) getTransaction(SingleSceneTransaction.class,router.getContentPane()));
        }
        return super.createRouterExecutor(name, router);
    }

    @Override
    protected RouterAnimation getAnimationByName(String name, AttributeSet attrs) {
        if (ANIMATION_SCALE_DOWN_XY_SLIDE_OUT_BOTTOM.equals(name)) {
            RouterAnimation animation = new RouterCompoundAnimation(ANIMATION_SCALE_DOWN_XY_SLIDE_OUT_BOTTOM,
                    ScaleAnimation.getScaleDownXY(), SlideAnimation.getSlideOutBottom());
            if (null!=attrs) {
                animation.initialize(attrs);
            }
            return animation;
        }
        else if (ANIMATION_SCALE_UP_XY_SLIDE_IN_BOTTOM.equals(name)) {
            RouterAnimation animation = new RouterCompoundAnimation(ANIMATION_SCALE_UP_XY_SLIDE_IN_BOTTOM,
                    ScaleAnimation.getScaleUpXY(),SlideAnimation.getSlideInBottom());
            if (null!=attrs) {
                animation.initialize(attrs);
            }
            return animation;
        }
        return super.getAnimationByName(name, attrs);
    }

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
