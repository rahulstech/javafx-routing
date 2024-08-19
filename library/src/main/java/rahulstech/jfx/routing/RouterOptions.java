package rahulstech.jfx.routing;

import rahulstech.jfx.routing.util.StringUtil;

import java.nio.charset.Charset;
import java.util.ResourceBundle;

@SuppressWarnings({"unused","UnusedReturnValue"})
public class RouterOptions {

    private String enterAnimation;

    private String exitAnimation;

    private String popEnterAnimation;

    private String popExitAnimation;
    
    private ResourceBundle bundle;
    
    private Charset charset;

    public RouterOptions() {}

    public RouterOptions(RouterOptions from) {
        apply(from);
    }

    public void apply(RouterOptions from) {
        enterAnimation = from.enterAnimation;
        exitAnimation = from.exitAnimation;
        popEnterAnimation = from.popEnterAnimation;
        popExitAnimation = from.popExitAnimation;
        bundle = from.bundle;
        charset = from.charset;
    }

    public String getEnterAnimation() {
        return enterAnimation;
    }

    public String getEnterAnimation(String defaultValue) {
        return StringUtil.isEmpty(enterAnimation) ? defaultValue : enterAnimation;
    }

    public RouterOptions setEnterAnimation(String enterAnimation) {
        this.enterAnimation = enterAnimation;
        return this;
    }

    public String getExitAnimation() {
        return exitAnimation;
    }

    public String getExitAnimation(String defaultValue) {
        return StringUtil.isEmpty(exitAnimation) ? defaultValue : exitAnimation;
    }

    public RouterOptions setExitAnimation(String exitAnimation) {
        this.exitAnimation = exitAnimation;
        return this;
    }

    public String getPopEnterAnimation() {
        return popEnterAnimation;
    }

    public String getPopEnterAnimation(String defaultValue) {
        return StringUtil.isEmpty(popEnterAnimation) ? defaultValue : popEnterAnimation;
    }

    public RouterOptions setPopEnterAnimation(String popEnterAnimation) {
        this.popEnterAnimation = popEnterAnimation;
        return this;
    }

    public String getPopExitAnimation() {
        return popExitAnimation;
    }

    public String getPopExitAnimation(String defaultValue) {
        return StringUtil.isEmpty(popExitAnimation) ? defaultValue : popExitAnimation;
    }

    public RouterOptions setPopExitAnimation(String popExitAnimation) {
        this.popExitAnimation = popExitAnimation;
        return this;
    }

    public RouterOptions setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        return this;
    }
    
    public ResourceBundle getBundle() {
        return bundle;
    }
    
    public RouterOptions setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public Charset getCharset() {
        return charset;
    }
}
