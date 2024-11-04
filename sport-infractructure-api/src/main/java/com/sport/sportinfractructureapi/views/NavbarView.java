package com.sport.sportinfractructureapi.views;

import com.sport.sportinfractructureapi.model.Key;
import com.sport.sportinfractructureapi.service.KeyService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import java.util.HashMap;
import java.util.Map;

public class NavbarView {

    private final KeyService keyService;
    private Key authenticatedUser;
    private SessionManager sessionManager;

    public NavbarView(KeyService keyService) {
        this.keyService = keyService;
        this.authenticatedUser = keyService.getAuthenticatedUser();
    }

    public void setupMenu(MenuBar menuBar) {
        MenuItem tableMenu = menuBar.addItem("Таблиці");
        setupTableSubmenu(tableMenu);

        MenuItem aggregations = menuBar.addItem("Агрегації");
        aggregations.addClickListener(e -> UI.getCurrent().navigate("aggregations"));

        if (isAuthorized("Власник") || isAuthorized("Адміністратор")) {
            System.out.println("Authorized");
            MenuItem addUserMenu = menuBar.addItem("Додати користувача");
            addUserMenu.addClickListener(e -> handleAddUser());
        }

        MenuItem rightsMenu = menuBar.addItem("Права");
        rightsMenu.addClickListener(e -> showUserRights());

        MenuItem logoutMenu = menuBar.addItem("Вийти");
        logoutMenu.addClickListener(e -> {
            keyService.logout();
            this.authenticatedUser = null;
            UI.getCurrent().navigate("");
        });

        menuBar.getItems().forEach(item -> item.addClassName("menu-item"));
    }

    private boolean isAuthorized(String role) {
        if (authenticatedUser != null) {
            String userRole = authenticatedUser.getUserRole().trim();
            System.out.println("User role" + userRole);
                if (userRole.equalsIgnoreCase(role)) {
                    return true;
                }
        }
        return false;
    }

    private static void setupTableSubmenu(MenuItem tableMenu) {
        tableMenu.getSubMenu().addItem("Спортсмени", e -> navigateToMainWithTable("Athlete"));
        tableMenu.getSubMenu().addItem("Тренери", e -> navigateToMainWithTable("Coach"));
        tableMenu.getSubMenu().addItem("Нагородження", e -> navigateToMainWithTable("Award"));
        tableMenu.getSubMenu().addItem("Змагання", e -> navigateToMainWithTable("Competition"));
        tableMenu.getSubMenu().addItem("Учасники змагань", e -> navigateToMainWithTable("CompetitionParticipant"));
        tableMenu.getSubMenu().addItem("Організатори", e -> navigateToMainWithTable("Organizer"));
        tableMenu.getSubMenu().addItem("Спортивні клуби", e -> navigateToMainWithTable("SportClub"));
        tableMenu.getSubMenu().addItem("Спортивні споруди", e -> navigateToMainWithTable("SportFacility"));
        tableMenu.getSubMenu().addItem("Види спорту", e -> navigateToMainWithTable("SportType"));
        tableMenu.getSubMenu().addItem("Спортсмени-тренери", e -> navigateToMainWithTable("AthleteCoach"));
        tableMenu.getSubMenu().addItem("Спортсмени-види спорту", e -> navigateToMainWithTable("AthleteSportType"));
    }

    private static void navigateToMainWithTable(String tableName) {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("table", tableName);
        QueryParameters queryParameters = QueryParameters.simple(parameterMap);
        UI.getCurrent().navigate("main", queryParameters);
    }

    private static void showUserRights() {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        VerticalLayout layout = new VerticalLayout();

        Span ownerTitle = new Span("Власник");
        ownerTitle.getStyle().set("font-weight", "bold");
        Span ownerRights = new Span(" - повний доступ до таблиць.");

        Span adminTitle = new Span("Адміністратор");
        adminTitle.getStyle().set("font-weight", "bold");
        Span adminRights = new Span(" - модифікація даних таблиці Змагання.");

        Span operatorTitle = new Span("Оператор");
        operatorTitle.getStyle().set("font-weight", "bold");
        Span operatorRights = new Span(" - доступ до зміни даних в стовпці Нагорода таблиці Нагородження.");

        Span guestTitle = new Span("Гість");
        guestTitle.getStyle().set("font-weight", "bold");
        Span guestRights = new Span(" - перегляд даних у таблиці.");

        layout.add(
                new Div(ownerTitle, ownerRights),
                new Div(adminTitle, adminRights),
                new Div(operatorTitle, operatorRights),
                new Div(guestTitle, guestRights)
        );

        dialog.add(layout);
        dialog.open();
    }

    private void handleAddUser() {
        if (isAuthorized("Власник") || isAuthorized("Адміністратор")) {
            Dialog addUserDialog = new Dialog();
            addUserDialog.setCloseOnEsc(true);
            addUserDialog.setCloseOnOutsideClick(true);

            Paragraph title = new Paragraph("Створити нового користувача");
            TextField usernameField = new TextField("Логін");
            TextField passwordField = new TextField("Пароль");
            ComboBox<String> roleField = new ComboBox<>("Роль");

            if (authenticatedUser.getUserRole().equals("Власник")) {
                roleField.setItems("Адміністратор", "Оператор", "Гість");
            } else {
                roleField.setItems("Оператор", "Гість");
            }

            Button addButton = new Button("Додати");

            addButton.addClickListener(click -> {
                String username = usernameField.getValue();
                String password = passwordField.getValue();
                String role = roleField.getValue();

                if (username.isEmpty() || password.isEmpty() || role == null) {
                    Notification.show("Будь ласка, заповніть всі поля", 3000, Notification.Position.MIDDLE);
                } else {
                    if (keyService.userExists(username)) {
                        Notification.show("Користувач з таким логіном вже існує", 3000, Notification.Position.MIDDLE);
                    } else {
                        Key newKey = new Key();
                        newKey.setUserLogin(username);
                        newKey.setUserPassword(password);
                        newKey.setUserRole(role);

                        try {
                            keyService.saveKey(newKey);
                            Notification.show("Користувача успішно додано", 3000, Notification.Position.MIDDLE);
                            addUserDialog.close();
                        } catch (Exception ex) {
                            Notification.show("Помилка при додаванні користувача", 3000, Notification.Position.MIDDLE);
                            ex.printStackTrace();
                        }
                    }
                }
            });

            VerticalLayout layout = new VerticalLayout(title, usernameField, passwordField, roleField, addButton);
            addUserDialog.add(layout);
            addUserDialog.open();
        } else {
            Notification.show("У вас немає прав для додавання користувачів", 3000, Notification.Position.MIDDLE);
        }
    }
}
