package rahulstech.jfx.historydemo;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rahulstech.jfx.routing.element.RouterArgument;

public class ProfileController extends VBoxScreenController {

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        getLabel().setText("Welcome ");

        Button back = new Button("Back");
        back.setOnAction(e->handleBackButtonClick());

        VBox root = getRoot();
        root.getChildren().addAll(back);
        root.setBackground(Utils.colorFillBackground(Color.GOLDENROD));
    }

    @Override
    public void onLifecycleInitialize() {
        super.onLifecycleInitialize();
        RouterArgument data = getRouter().getCurrentData();
        String name = data.getValue("name");
        String usernmae = data.getValue("username");
        String password = data.getValue("password");
        String email = data.getValue("email");

        getLabel().setText("Hello, "+name+"\nHere is your login details:\nusername:"+usernmae+"\npassword:"+password+"\n" +
                "We will connect you via: "+(Utils.isEmpty(email) ? "email not provided" : email));
    }

    private void handleBackButtonClick() {
        getRouter().popBackStack();
    }
}
