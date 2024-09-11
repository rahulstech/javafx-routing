package rahulstech.jfx.historydemo;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rahulstech.jfx.routing.element.RouterArgument;

public class StepOneController extends VBoxScreenController {

    private TextField username;

    private PasswordField password;

    private Label error;

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        getLabel().setText("New User LogIn Details");

        username = new TextField();
        username.setPromptText("Username");

        password = new PasswordField();
        password.setPromptText("Password");

        error = new Label();

        Button back = new Button("Back");
        back.setOnAction(e->handleBackButtonClick());

        Button next = new Button("Go To Step 2");
        next.setOnAction(e->handleNextButtonClick());

        VBox root = getRoot();
        root.getChildren().addAll(username,password,error,back,next);
        root.setBackground(Utils.colorFillBackground(Color.AQUA));
    }

    private void handleBackButtonClick() {
        getRouter().popBackStack();
    }

    private void handleNextButtonClick() {
        String username = this.username.getText();
        String password = this.password.getText();
        if (Utils.isEmpty(username)) {
            error.setText("username required");
            return;
        }
        else if (Utils.isEmpty(password)) {
            error.setText("password required");
            return;
        }
        error.setText(null);
        RouterArgument data = new RouterArgument();
        data.addArgument("username",username);
        data.addArgument("password",password);
        getRouter().moveto("signup_step2",data);
    }
}
