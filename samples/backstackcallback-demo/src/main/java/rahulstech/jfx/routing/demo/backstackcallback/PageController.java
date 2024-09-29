package rahulstech.jfx.routing.demo.backstackcallback;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

public class PageController extends SimpleLifecycleAwareController {

    Label header;

    Button nav1, nav2, nav3;

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        header = new Label();
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: 500;");

        nav1 = createNavButtons("Page1",e -> gotoPage("page1"));
        nav2 = createNavButtons("Page2",e -> gotoPage("page2"));
        nav3 = createNavButtons("Page3", e -> gotoPage("page3"));

        HBox navs = new HBox(nav1,nav2,nav3);
        navs.setSpacing(16);

        BorderPane root = new BorderPane();

        root.setTop(header);
        root.setCenter(navs);

        BorderPane.setAlignment(header, Pos.CENTER);

        setRoot(root);
    }

    Button createNavButtons(String name, EventHandler<ActionEvent> handler) {
        Button nav = new Button(name);
        nav.setOnAction(handler);
        return nav;
    }

    @Override
    public BorderPane getRoot() {
        return (BorderPane) super.getRoot();
    }


    @Override
    public void onLifecycleInitialize() {
        super.onLifecycleInitialize();
        String id = getRouter().getCurrentDestination().getId();
        String title = getRouter().getCurrentDestination().getTitle();
        BorderPane root = getRoot();
        header.setText(title);
        Color color = Color.WHEAT;
        if ("page1".equals(id)) {
            nav1.setDisable(true);
            color = Color.LIMEGREEN;
        }
        else if ("page2".equals(id)) {
            nav2.setDisable(true);
            color = Color.LIGHTCYAN;
        }
        else if ("page3".equals(id)) {
            nav3.setDisable(true);
            color = Color.LIGHTPINK;
        }
        root.setBackground(new Background(new BackgroundFill(color,null,null)));
    }

    private void gotoPage(String id) {
        ((DemoRouterContext) getRouter().getContext()).getApp().moveto(id);
    }
}
