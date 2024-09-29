package rahulstech.jfx.routing.demo.backstackcallback;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.RouterOptions;
import rahulstech.jfx.routing.backstack.Backstack;
import rahulstech.jfx.routing.backstack.SimpleBackstackCallback;
import rahulstech.jfx.routing.element.animation.SlideAnimation;
import rahulstech.jfx.routing.layout.RouterStackPane;

import java.util.Arrays;

public class DemoApplication extends Application {

    ToggleButton tab1, tab2, tab3;

    ToggleButton selected = null;

    RouterStackPane pages;

    private String[] page_order = new String[]{"page1","page2","page3"};

    ToggleButton createTab(String text, EventHandler<ActionEvent> handler) {
        ToggleButton button = new ToggleButton(text);
        button.setOnAction(handler);
        return button;
    }

    boolean selectTab(ToggleButton tab) {
        if (tab == selected) {
            tab.setSelected(true);
            return false;
        }
        ToggleButton old = selected;
        selected = tab;
        if (old != null) {
            old.setSelected(false);
        }
        if (null != tab) {
            tab.setSelected(true);
        }
        return true;
    }

    private void handleTabClick(ToggleButton tab) {
        Router router = pages.getRouter();
        if (null == router) {
            return;
        }

        if (!selectTab(tab)) {
            return;
        }

        if (tab == tab1) {
            moveto("page1");
        }
        else if (tab == tab2) {
            moveto("page2");
        }
        else if (tab == tab3) {
            moveto("page3");
        }
    }

    public void moveto(String id) {
        Router router = pages.getRouter();
        if (null == router) {
            return;
        }

        String current = router.getCurrentDestination().getId();

        int iOld = getPageIndex(current);
        int iNew = getPageIndex(id);

        RouterOptions options = new RouterOptions();
        if (iNew < iOld) {
            options.setEnterAnimation(SlideAnimation.SLIDE_IN_RIGHT);
            options.setExitAnimation(SlideAnimation.SLIDE_OUT_LEFT);
        }
        else {
            options.setEnterAnimation(SlideAnimation.SLIDE_IN_LEFT);
            options.setExitAnimation(SlideAnimation.SLIDE_OUT_RIGHT);
        }

        pages.getRouter().moveto(id,null,options);
    }

    private int getPageIndex(String id) {
        return Arrays.binarySearch(page_order,id);
    }

    @Override
    public void start(Stage stage) throws Exception {

        EventHandler<ActionEvent> onClickTab = e -> {
            ToggleButton source = (ToggleButton) e.getSource();
            handleTabClick(source);
        };

        tab1 = createTab("Page1",onClickTab);
        tab2 = createTab("Page2",onClickTab);
        tab3 = createTab("Page3",onClickTab);

        HBox tabs = new HBox(tab1,tab2,tab3);
        tabs.setSpacing(16);

        pages = new RouterStackPane();
        pages.setContext(new DemoRouterContext(this));
        pages.setRouterConfig("router.xml");
        pages.routerProperty().addListener((ov,oldRouter,newRouter) -> {
            if (null != oldRouter) {
                oldRouter.getBackstack().unregisterBackstackCallback(backstackCallback);
            }
            if (null != newRouter) {
                newRouter.getBackstack().registerBackstackCallback(backstackCallback);
            }
        });

        BorderPane root = new BorderPane();

        root.setTop(tabs);
        root.setCenter(pages);

        Scene scene = new Scene(root,800,600);
        scene.getStylesheets().add(DemoApplication.class.getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    SimpleBackstackCallback<Router.RouterBackstackEntry> backstackCallback = new SimpleBackstackCallback<>() {
        @Override
        public void onBackstackTopChanged(Backstack<Router.RouterBackstackEntry> backstack, Router.RouterBackstackEntry entry) {
            String id = entry.getDestination().getId();
            switch (id) {
                case "page1": {
                    selectTab(tab1);
                }
                break;
                case "page2": {
                    selectTab(tab2);
                }
                break;
                case "page3": {
                    selectTab(tab3);
                }
            }
        }
    };

    public static void main(String[] args) {
        launch(args);
    }
}
