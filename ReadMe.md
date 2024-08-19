## JavaFx Routing ##

---

The sample project structure will look like the below
``` requirements
project/
├── src/
│   └── main/
│      ├── java/
│      │   └── rahulstech.jfx.routing.demo
│      │       └── MyApplication.java
│      │       └── MyContext.java
│      │       └── DashboardController.java
│      │       └── ScreenOneController.java
│      │       └── ScreenTwoController.java
│      └── resources/
│          └── router.xml
│          └── dashboard.fxml
│          └── screen1.fxml
├── build.gradle
└── README.md
```

Define the router configuration file **router.xml**
```xml
<?xml version="1.0" encoding="UTF-8" ?>

<!--
 adding animation is not mandatory; but animations make the transition look
 more elegant, so you are recomanded to use some animation for screen transition.
 you can set home destination enter animation seperately. to set home destination
 enter transition use homeEnterAnimation attribute.
 -->
<router xmlns="https://github.com/rahulstech/javafx-routing"
        home="dashboard"
        enterAnimation="scale_up_xy"
        exitAnimation="scale_down_xy"
        popEnterAnimation="scale_up_xy"
        popExitAnimation="scale_down_xy">

    <!-- define some destinations -->
    <destination id="dashboard"
                 fxml="dashboard.fxml"/>

    <!--
        if your fxml does not set fxml:controller then you must set controller atribute.
        if fxml:controller is set then this controller attribute value will be ignoed.
        if fxml attribute not provided then you set controller attribute otherwise it will
        throw error.

        NOTE: If you are not using fxml then you are resposible to creat the layout file inside
              this controller class. see the ScreenTwoController.java
     -->

    <destination id="screen1"
                 fxml="screen1.fxml"
                 controllerClass="rahulstech.jfx.routing.demo.ScreenOneController"/>

    <destination id="screen2"
                 controllerClass="rahulstech.jfx.routing.demo.ScreenTwoController"/>

</router>
```

Create **MyApplication.java**

```java
package rahulstech.jfx.routing.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.RouterContext;
import rahulstech.jfx.routing.layout.RouterStackPane;

public class MyApplication extends Application {

    private Router router;

    @Override
    public void start(Stage primaryStage) {

        RouterContext context = new MyContext();

        RouterStackPane root = new RouterStackPane();
        root.setContext(context);
        root.setRouterConfig("router.xml");

        Scene scene = new Scene(root,800,600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Routing Example");
        primaryStage.setOnShown(e-> router = root.getRouter());
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (null!=router) {
            router.dispose();
        }
        super.stop();
    }

    public static void main(String... args) {
        launch(args);
    }
}
```

Implement [RouterContext](/library/src/main/java/rahulstech/jfx/routing/RouterContext.java) as **MyContext.java**

```java
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
```

Create screen1 fxml **screen1.fxml** and controller **ScreenOneController.java**

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.VBox?>

<VBox xmlns="http://javafx.com/javafx"
      xmlns:fx="http://javafx.com/fxml"
      style="-fx-background-color: CADETBLUE;"
      spacing="10">

    <Label VBox.vgrow="NEVER" fx:id="label"/>

    <Button text="Back" onAction="#handleBackButtonClick"/>

    <Button text="Go To" onAction="#handleGoToButtonClick"/>

</VBox>

```

```java
package rahulstech.jfx.routing.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;
import rahulstech.jfx.routing.Router;

public class ScreenOneController extends SimpleLifecycleAwareController {

    @FXML
    private Label label;

    private int counter;

    @Override
    public void onLifecycleInitialize() {
        RouterArgument args = getRouter().getCurrentData();
        counter = (int) args.getValue("counter");
        label.setText("Screen 1 (counter "+counter+")");
    }

    @FXML
    private void handleBackButtonClick() {
        getRouter().popBackStack();
    }

    @FXML
    private void handleGoToButtonClick() {
        Router router = getRouter();
        String targetId = "screen2";
        RouterArgument args = new RouterArgument();
        args.addArgument("counter",++counter);
        router.moveto(targetId,args);
    }
}
```

Similarly create screen2 controller **ScreenTwoController.java**

```java
package rahulstech.jfx.routing.demo;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

/**
 * if you are going to create the layout inside controller class then the
 * controller class must implement {@link rahulstech.jfx.routing.lifecycle.LifecycleAwareController LifecycleAwareController}
 * also you have to create your layout inside {@link rahulstech.jfx.routing.lifecycle.LifecycleAwareController#onLifecycleCreate() onLifecycleCreate()}
 * method and return root node from {@link rahulstech.jfx.routing.lifecycle.LifecycleAwareController#getRoot() getRoot()}. you can extend
 * {@link rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController SimpleLifecycleAwareController}  for simple usages. otherwise use
 * the interface {@link rahulstech.jfx.routing.lifecycle.LifecycleAwareController LifecycleAwareController}.
 */
public class ScreenTwoController extends SimpleLifecycleAwareController {

    private Node root;

    private Label label;

    int counter;

    @Override
    public void onLifecycleCreate() {
        String id = getRouter().getCurrentDestination().getId();

        Button button = new Button("Screen 1");
        button.setOnAction(e->handleButtonOneClick());

        Button backButton = new Button("Back");
        backButton.setOnAction(e->handleBackButtonClick());

        Label label = new Label(id);

        VBox root = new VBox(label,backButton,button);
        root.setSpacing(10);
        root.setBackground(Background.fill(Color.BURLYWOOD));

        this.root = root;
        this.label = label;
    }

    @Override
    public void onLifecycleInitialize() {
        RouterArgument data = getRouter().getCurrentData();
        counter = (int) data.getValue("counter");
        label.setText("Screen 2 (Counter "+counter+")");
    }

    @Override
    public Node getRoot() {
        return root;
    }

    private void handleBackButtonClick() {
        getRouter().popBackStack();
    }

    private void handleButtonOneClick() {
        RouterArgument data = new RouterArgument();
        data.addArgument("counter",++counter);
        getRouter().moveto("screen1",data);
    }
}
```

Create dashboard fxml **dashboard.fxml** and controller **DashboardController.java**

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.VBox?>

<VBox xmlns="http://javafx.com/javafx"
      xmlns:fx="http://javafx.com/fxml"
      fx:controller="rahulstech.jfx.routing.demo.DashboardController"
      style="-fx-background-color: pink;"
      spacing="10">

    <Label VBox.vgrow="NEVER" text="Dashboard"/>

    <Button text="Screen 1" onAction="#handleButtonOneClick"/>

    <Button text="Screen 2" onAction="#handleButtonTwoClick"/>

</VBox>
```

```java
package rahulstech.jfx.routing.demo;

import javafx.fxml.FXML;
import rahulstech.jfx.routing.element.RouterArgument;
import rahulstech.jfx.routing.lifecycle.SimpleLifecycleAwareController;

public class DashboardController extends SimpleLifecycleAwareController {

    @FXML
    private void handleButtonOneClick() {
        handleButtonClick(1);
    }

    @FXML
    private void handleButtonTwoClick() {
        handleButtonClick(2);
    }

    private void handleButtonClick(int which) {
        String targetId = "screen"+which;
        RouterArgument data = new RouterArgument();
        data.addArgument("counter",0);
        getRouter().moveto(targetId,data);
    }
}
```