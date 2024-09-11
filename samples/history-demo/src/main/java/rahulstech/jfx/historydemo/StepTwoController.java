package rahulstech.jfx.historydemo;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rahulstech.jfx.routing.element.RouterArgument;

public class StepTwoController extends VBoxScreenController {

    private TextField name;

    private TextField email;

    private Label error;

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        getLabel().setText("New User Personal Details");

        name = new TextField();
        name.setPromptText("Full Name");

        email = new TextField();
        email.setPromptText("Email");

        error = new Label();

        Button back = new Button("Back");
        back.setOnAction(e->handleBackButtonClick());

        Button next = new Button("Sign Up and GoTo My Profile");
        next.setOnAction(e->handleNextButtonClick());

        VBox root = getRoot();
        root.setBackground(Utils.colorFillBackground(Color.BURLYWOOD));
        root.getChildren().addAll(name,email,error,back,next);
    }

    private void handleBackButtonClick() {
        getRouter().popBackStack();
    }

    private void handleNextButtonClick() {
        String fullName = name.getText();
        String emailAddress = email.getText();
        if (Utils.isEmpty(fullName)) {
            error.setText("name required");
            return;
        }
        error.setText(null);
        RouterArgument data = new RouterArgument();
        data.merge(getRouter().getCurrentData());
        data.addArgument("name",fullName);
        data.addArgument("email",emailAddress);
        getRouter().movePoppingUpto("profile","signup_step1",true,data);
    }
}
