package com.sport.sportinfractructureapi.views;

import com.sport.sportinfractructureapi.model.Key;
import com.sport.sportinfractructureapi.service.KeyService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("")
@CssImport("./styles/loginView.css")
public class LoginView extends VerticalLayout {

    private final KeyService keyService;

    @Autowired
    public LoginView(KeyService keyService) {
        this.keyService = keyService;
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Paragraph logo = new Paragraph("SportScape");
        logo.addClassName("logo");

        H1 title = new H1("Вхід в систему");
        title.addClassName("login-title");

        TextField username = new TextField("Логін");
        PasswordField password = new PasswordField("Пароль");
        Button loginButton = new Button("Увійти");

        loginButton.addClickListener(e -> {
            Key authenticatedUser = keyService.authenticate(username.getValue(), password.getValue());
            if (authenticatedUser != null) {
                VaadinSession.getCurrent().setAttribute(Key.class, authenticatedUser);
                UI.getCurrent().navigate(MainView.class);
            } else {
                Notification.show("Неправильний логін або пароль", 3000, Notification.Position.TOP_CENTER);
            }
        });

        Button forgotPasswordButton = new Button("Забули пароль?");
        forgotPasswordButton.addClickListener(e -> showForgotPasswordDialog());

        forgotPasswordButton.addClassName("forgot-password-link");

        VerticalLayout formLayout = new VerticalLayout(logo, title, username, password, loginButton, forgotPasswordButton);
        formLayout.addClassName("login-form");
        formLayout.setAlignItems(Alignment.CENTER);

        add(formLayout);
    }

    private void showForgotPasswordDialog() {
        Dialog forgotPasswordDialog = new Dialog();
        forgotPasswordDialog.setCloseOnEsc(true);
        forgotPasswordDialog.setCloseOnOutsideClick(true);

        TextField loginField = new TextField("Введіть ваш логін");
        Button submitButton = new Button("Отримати пароль");

        Div passwordDiv = new Div();

        submitButton.addClickListener(click -> {
            String userLogin = loginField.getValue();
            List<Key> keys = keyService.getPasswordByLogin(userLogin);

            if (keys != null && !keys.isEmpty()) {
                StringBuilder passwords = new StringBuilder();
                for (Key key : keys) {
                    passwords.append("Ваш пароль: ").append(key.getUserPassword()).append("\n");
                }
                passwordDiv.setText(passwords.toString());
            } else {
                passwordDiv.setText("Логін не знайдено");
            }
        });

        forgotPasswordDialog.add(loginField, submitButton, passwordDiv);
        forgotPasswordDialog.open();
    }
}
