package com.sport.sportinfractructureapi.views;

import com.sport.sportinfractructureapi.model.*;
import com.sport.sportinfractructureapi.service.*;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.component.dependency.CssImport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Route("main")
@CssImport("./styles/mainView.css")
public class MainView extends VerticalLayout implements HasUrlParameter<String> {

    private final KeyService keyService;
    private TablesGrid<?> currentGrid;
    private final AthleteService athleteService;
    private final SportClubService sportClubService;
    private final CoachService coachService;
    private final AwardService awardService;
    private final CompetitionService competitionService;
    private final OrganizerService organizerService;
    private final SportFacilityService sportFacilityService;
    private final SportTypeService sportTypeService;
    private final CompetitionParticipantService competitionParticipantService;
    private final AthleteCoachService athleteCoachService;
    private final AthleteSportTypeService athleteSportTypeService;
    private final SessionManager sessionManager;


    @Autowired
    public MainView(KeyService keyService, AthleteService athleteService,
                    SportClubService sportClubService, CoachService coachService,
                    AwardService awardService, CompetitionService competitionService,
                    OrganizerService organizerService, SportFacilityService sportFacilityService,
                    SportTypeService sportTypeService,
                    CompetitionParticipantService competitionParticipantService,
                    AthleteCoachService athleteCoachService,
                    AthleteSportTypeService athleteSportTypeService, SessionManager sessionManager) {

        this.keyService = keyService;
        this.athleteService = athleteService;
        this.sportClubService = sportClubService;
        this.coachService = coachService;
        this.awardService = awardService;
        this.competitionService = competitionService;
        this.organizerService = organizerService;
        this.sportFacilityService = sportFacilityService;
        this.sportTypeService = sportTypeService;
        this.competitionParticipantService = competitionParticipantService;
        this.athleteCoachService = athleteCoachService;
        this.athleteSportTypeService = athleteSportTypeService;
        this.sessionManager = sessionManager;

        setSizeFull();
        addClassName("main-view");

        HorizontalLayout navbar = new HorizontalLayout();
        navbar.addClassName("navbar");

        H1 logo = new H1("SportScape");
        logo.addClassName("logo");

        MenuBar menuBar = new MenuBar();
        menuBar.addClassName("menu-bar");

        NavbarView navbarView = new NavbarView(keyService);
        navbarView.setupMenu(menuBar);

        navbar.setWidthFull();
        navbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        navbar.add(logo, menuBar);

        add(navbar);

        if (!sessionManager.isGreetingShown()) {
            displayGreeting();
            sessionManager.setGreetingShown(true);
        }
        showTable(Athlete.class);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Map<String, List<String>> queryParameters = event.getLocation().getQueryParameters().getParameters();
        String tableName = queryParameters.getOrDefault("table", Collections.singletonList("Athlete")).get(0);
        Class<?> entityClass = getClassForName(tableName);
        showTable(entityClass);
    }

    private <T> void showTable(Class<T> entityClass) {
        if (currentGrid != null) {
            remove(currentGrid);
        }
        currentGrid = new TablesGrid<>(athleteService, sportClubService, coachService,
                awardService, competitionService, organizerService,
                sportFacilityService, sportTypeService, competitionParticipantService,
                athleteCoachService, athleteSportTypeService, keyService, entityClass);
        currentGrid.setClassName("dynamic-grid");
        currentGrid.setHeight("100%");
        add(currentGrid);

        setFlexGrow(1, currentGrid);
    }

    private Class<?> getClassForName(String className) {
        switch (className) {
            case "Athlete": return Athlete.class;
            case "Coach": return Coach.class;
            case "Award": return Award.class;
            case "Competition": return Competition.class;
            case "CompetitionParticipant": return CompetitionParticipant.class;
            case "Organizer": return Organizer.class;
            case "SportClub": return SportClub.class;
            case "SportFacility": return SportFacility.class;
            case "SportType": return SportType.class;
            case "AthleteCoach": return AthleteCoach.class;
            case "AthleteSportType": return AthleteSportType.class;
            default: return Athlete.class;
        }
    }

    private void displayGreeting() {
        Dialog greeting = new Dialog();
        greeting.add(new H4("З поверненням, " + keyService.getAuthenticatedUser().getUserLogin() + "!"));
        greeting.addClassName("top-left-dialog");
        greeting.setCloseOnEsc(true);
        greeting.open();
    }


}
