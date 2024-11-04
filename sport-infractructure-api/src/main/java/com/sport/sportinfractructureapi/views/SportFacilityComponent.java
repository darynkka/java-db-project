package com.sport.sportinfractructureapi.views;
import com.sport.sportinfractructureapi.model.SportFacility;
import com.sport.sportinfractructureapi.service.SportFacilityService;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;
@CssImport("./styles/aggregationsView.css")
public class SportFacilityComponent extends VerticalLayout {
    private final SportFacilityService sportFacilityService;
    private Div resultDiv;
    private VerticalLayout accordionContent;

    public SportFacilityComponent(SportFacilityService sportFacilityService) {
        this.sportFacilityService = sportFacilityService;
        initializeComponent();
    }

    private void initializeComponent() {
        Accordion querySportFacilitySpecific = new Accordion();
        querySportFacilitySpecific.addClassName("query-accordion-specific");

        MenuBar typeMenu = new MenuBar();
        typeMenu.addClassName("menu-bar");
        MenuItem typeItem = typeMenu.addItem("Sport facility type");
        typeItem.getSubMenu().addItem("Стадіон", e -> handleOption("Option 1"));
        typeItem.getSubMenu().addItem("Корт", e -> handleOption("Option 2"));
        typeItem.getSubMenu().addItem("Спортзал", e -> handleOption("Option 3"));
        typeItem.getSubMenu().addItem("Манеж", e -> handleOption("Option 4"));

        VerticalLayout inputLayout = new VerticalLayout(typeMenu);
        inputLayout.setAlignItems(Alignment.BASELINE);

        accordionContent = new VerticalLayout();
        accordionContent.setVisible(false);

        querySportFacilitySpecific.add("Одержати перелік спортивних споруд вказаного типу які задовільняють певні умови", new VerticalLayout(inputLayout, accordionContent));

        querySportFacilitySpecific.close();
        add(querySportFacilitySpecific);
    }

    private void handleOption(String option) {
        accordionContent.removeAll();
        accordionContent.setVisible(true);

        switch (option) {
            case "Option 1":
                handleStadiumOption();
                break;
            case "Option 2":
                handleCourtOption();
                break;
            case "Option 3":
                handleGymOption();
                break;
            case "Option 4":
                handlePlaypenOption();
                break;
            default:
                Notification.show("Невідома опція", 3000, Notification.Position.TOP_CENTER);
                break;
        }
    }

    private void handleStadiumOption() {
        TextField stadiumCapacity = new TextField("Stadium capacity");
        Button fetchStadiumButton = new Button("Do");

        fetchStadiumButton.addClickListener(e -> {
            if (stadiumCapacity.isEmpty()) {
                Notification.show("Поле не повинно бути порожнім", 3000, Notification.Position.TOP_CENTER);
            } else {
                try {
                    int capacity = Integer.parseInt(stadiumCapacity.getValue());
                    List<SportFacility> results = sportFacilityService.getStadiumsByCapacity(capacity);
                    displayResults(results);
                } catch (NumberFormatException ex) {
                    Notification.show("Введіть дійсний числовий обсяг", 3000, Notification.Position.TOP_CENTER);
                }
            }
        });

        HorizontalLayout stadiumLayout = new HorizontalLayout(stadiumCapacity, fetchStadiumButton);
        stadiumLayout.setAlignItems(Alignment.BASELINE);

        resultDiv = new Div();
        resultDiv.addClassName("result-div");

        accordionContent.add(stadiumLayout, resultDiv);
    }

    private void handleCourtOption() {
        TextField cortCoating = new TextField("Court coating");
        Button fetchCourtButton = new Button("Do");

        fetchCourtButton.addClickListener(e -> {
            if (cortCoating.isEmpty()) {
                Notification.show("Поле не повинно бути порожнім", 3000, Notification.Position.TOP_CENTER);
            } else {
                String coating = cortCoating.getValue();
                List<SportFacility> results = sportFacilityService.getCortsByCoating(coating);
                displayResults(results);
            }
        });

        HorizontalLayout courtLayout = new HorizontalLayout(cortCoating, fetchCourtButton);
        courtLayout.setAlignItems(Alignment.BASELINE);

        resultDiv = new Div();
        resultDiv.addClassName("result-div");

        accordionContent.add(courtLayout, resultDiv);
    }

    private void handleGymOption() {
        TextField gymEquipment = new TextField("Gym equipment");
        Button fetchGymButton = new Button("Do");

        fetchGymButton.addClickListener(e -> {
            if(gymEquipment.isEmpty()) {
                Notification.show("Поле не повинно бути порожнім", 3000, Notification.Position.TOP_CENTER);
            } else {
            String equipment = gymEquipment.getValue();
            List<SportFacility> results = sportFacilityService.getGymsByEquipment(equipment);
            displayResults(results);
            }
        });

        HorizontalLayout gymLayout = new HorizontalLayout(gymEquipment, fetchGymButton);
        gymLayout.setAlignItems(Alignment.BASELINE);

        resultDiv = new Div();
        resultDiv.addClassName("result-div");

        accordionContent.add(gymLayout, resultDiv);
    }

    private void handlePlaypenOption() {
        TextField playpenCCS = new TextField("Playpen CCS");
        Button fetchPlaypenButton = new Button("Do");

        fetchPlaypenButton.addClickListener(e -> {
            if (playpenCCS.isEmpty()) {
                Notification.show("Поле не повинно бути порожнім", 3000, Notification.Position.TOP_CENTER);
            } else {
            String ccs = playpenCCS.getValue();
            List<SportFacility> results = sportFacilityService.getPlaypensByCCS(ccs);
            displayResults(results);
            }
        });

        HorizontalLayout playpenLayout = new HorizontalLayout(playpenCCS, fetchPlaypenButton);
        playpenLayout.setAlignItems(Alignment.BASELINE);

        resultDiv = new Div();
        resultDiv.addClassName("result-div");

        accordionContent.add(playpenLayout, resultDiv);
    }

    private void displayResults(List<SportFacility> results) {
        if (results.isEmpty()) {
            Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
        } else {
            displaySportFacilities(results, resultDiv);
        }
    }

    private void displaySportFacilities(List<SportFacility> results, Div resultDiv) {
        resultDiv.removeAll();
        for (SportFacility sportFacility : results) {
            String competitionInfo = results.toString();
            resultDiv.add(new Paragraph(competitionInfo));
        }
    }
}