package com.sport.sportinfractructureapi.views;

import com.sport.sportinfractructureapi.model.*;
import com.sport.sportinfractructureapi.service.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.sport.sportinfractructureapi.service.SportTypeService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;

@Route("aggregations")
@CssImport("./styles/aggregationsView.css")
public class AggregationsView extends VerticalLayout {

    private final CompetitionService competitionService;
    private final SportClubService sportClubService;
    private final SportFacilityService sportFacilityService;
    private final AwardService awardService;
    private final AthleteService athleteService;
    private final CoachService coachService;
    private final KeyService keyService;

    public AggregationsView(CompetitionService competitionService, SportClubService sportClubService,
                            SportFacilityService sportFacilityService, AwardService awardService,
                            AthleteService athleteService, CoachService coachService,
                            KeyService keyService, AthleteCoachService athleteCoachService,
                            AthleteSportTypeService athleteSportTypeService) {
        super();
        this.competitionService = competitionService;
        this.sportClubService = sportClubService;
        this.sportFacilityService = sportFacilityService;
        this.awardService = awardService;
        this.athleteService = athleteService;
        this.coachService = coachService;
        this.keyService = keyService;

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


        // First accordion for querying competitions by date
        Accordion queryCompetitions = new Accordion();
        queryCompetitions.addClassName("query-accordion");

        DatePicker startDatePicker1 = new DatePicker("Початкова дата");
        DatePicker endDatePicker1 = new DatePicker("Кінцева дата");
        Button fetchButton1 = new Button("Виконати");

        VerticalLayout accordionContent1 = new VerticalLayout();
        HorizontalLayout inputLayout1 = new HorizontalLayout(startDatePicker1, endDatePicker1, fetchButton1);
        inputLayout1.setAlignItems(Alignment.BASELINE);
        accordionContent1.add(inputLayout1);

        Div resultDiv1 = new Div();
        resultDiv1.addClassName("result-div");
        accordionContent1.add(resultDiv1);

        AccordionPanel competitionsPanel1 = queryCompetitions.add("Одержати перелік змагань проведених протягом вказаного періоду", accordionContent1);
        queryCompetitions.close();

        fetchButton1.addClickListener(e -> {
            LocalDate startDate = startDatePicker1.getValue();
            LocalDate endDate = endDatePicker1.getValue();

            if (startDate != null && endDate != null) {
                List<Competition> competitions = competitionService.getCompetitionsByDate(startDate, endDate);
                if (competitions.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv1.removeAll();
                } else {
                    displayResults(competitions, resultDiv1);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            startDatePicker1.clear();
            endDatePicker1.clear();
        });

        // Second accordion for querying competitions by date and organizer
        Accordion queryCompetitionsOrganizer = new Accordion();
        queryCompetitionsOrganizer.addClassName("query-accordion");

        DatePicker startDatePicker2 = new DatePicker("Початкова дата");
        DatePicker endDatePicker2 = new DatePicker("Кінцева дата");
        TextField organizerField = new TextField("Організатор");
        Button fetchButton2 = new Button("Виконати");

        VerticalLayout accordionContent2 = new VerticalLayout();
        HorizontalLayout inputLayout2 = new HorizontalLayout(startDatePicker2, endDatePicker2, organizerField, fetchButton2);
        inputLayout2.setAlignItems(Alignment.BASELINE);
        accordionContent2.add(inputLayout2);

        Div resultDiv2 = new Div();
        resultDiv2.addClassName("result-div");
        accordionContent2.add(resultDiv2);

        AccordionPanel competitionsPanel2 = queryCompetitionsOrganizer.add("Одержати перелік змагань проведених протягом вказаного періоду та вказаним організатором", accordionContent2);
        queryCompetitionsOrganizer.close();

        fetchButton2.addClickListener(e -> {
            LocalDate startDate = startDatePicker2.getValue();
            LocalDate endDate = endDatePicker2.getValue();
            String organizerName = organizerField.getValue();

            if (startDate != null && endDate != null && !organizerName.isEmpty()) {
                List<Competition> competitions = competitionService.findCompetitionByOrganizerAndDate(organizerName, startDate, endDate);
                if (competitions.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv2.removeAll();
                } else {
                    displayResults(competitions, resultDiv2);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            startDatePicker2.clear();
            endDatePicker2.clear();
            organizerField.clear();
        });

        // Third accordion for querying facilities
        Accordion queryFacility = new Accordion();
        queryFacility.addClassName("query-accordion");

        TextField facility = new TextField("Назва споруди");
        Button fetchButton3 = new Button("Виконати");

        VerticalLayout accordionContent3 = new VerticalLayout();
        HorizontalLayout inputLayout3 = new HorizontalLayout(facility, fetchButton3);
        inputLayout3.setAlignItems(Alignment.BASELINE);
        accordionContent3.add(inputLayout3);

        Div resultDiv3 = new Div();
        resultDiv3.addClassName("result-div");
        accordionContent3.add(resultDiv3);

        AccordionPanel competitionsPanel3 = queryFacility.add("Одержати перелік змагань проведених у вказаній спортивній споруді", accordionContent3);
        queryFacility.close();

        fetchButton3.addClickListener(e -> {

            String facilityName = facility.getValue();
            if (!facilityName.isEmpty()) {
                List<Competition> competitions = competitionService.findCompetitionsByFacility(facilityName);
                if (competitions.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv3.removeAll();
                    facility.clear();
                } else {
                    displayResults(competitions, resultDiv3);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            facility.clear();
        });


        // Fourth accordion for querying facilities + sport type
        Accordion queryFacilitySportType = new Accordion();
        queryFacilitySportType.addClassName("query-accordion");

        TextField facilityName = new TextField("Назва споруди");
        TextField sportType = new TextField("Вид спорту");
        Button fetchButton4 = new Button("Виконати");

        VerticalLayout accordionContent4 = new VerticalLayout();
        HorizontalLayout inputLayout4 = new HorizontalLayout(facilityName, sportType, fetchButton4);
        inputLayout4.setAlignItems(Alignment.BASELINE);
        accordionContent4.add(inputLayout4);

        Div resultDiv4 = new Div();
        resultDiv4.addClassName("result-div");
        accordionContent4.add(resultDiv4);

        AccordionPanel competitionsPanel4 = queryFacilitySportType.add("Одержати перелік змагань проведених у вказаній спортивній споруді та з певного виду спорту", accordionContent4);
        queryFacilitySportType.close();

        fetchButton4.addClickListener(e -> {

            String facilityNameValue = facilityName.getValue();
            String sportTypeName = sportType.getValue();
            if (!facilityNameValue.isEmpty() && !sportTypeName.isEmpty()) {
                List<Competition> competitions = competitionService.findCompetitionsByFacilityAndSportType(facilityNameValue, sportTypeName);
                if (competitions.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv4.removeAll();
                    facilityName.clear();
                    sportType.clear();
                } else {
                    displayResults(competitions, resultDiv4);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            facilityName.clear();
            sportType.clear();
        });

        // Fifth accordion for querying sport club
        Accordion querySportClub = new Accordion();
        querySportClub.addClassName("query-accordion");

        DatePicker startDatePicker5 = new DatePicker("Початкова дата");
        DatePicker endDatePicker5 = new DatePicker("Кінцева дата");
        Button fetchButton5 = new Button("Виконати");

        VerticalLayout accordionContent5 = new VerticalLayout();
        HorizontalLayout inputLayout5 = new HorizontalLayout(startDatePicker5, endDatePicker5, fetchButton5);
        inputLayout5.setAlignItems(Alignment.BASELINE);
        accordionContent5.add(inputLayout5);

        Div resultDiv5 = new Div();
        resultDiv5.addClassName("result-div");
        accordionContent5.add(resultDiv5);

        AccordionPanel competitionsPanel5 = querySportClub.add("Одержати перелік спортивних клубів та кількість спортсменів цих клубів, які брали\n" +
                "участь у спортивних змаганнях протягом вказаного періоду", accordionContent5);
        querySportClub.close();

        fetchButton5.addClickListener(e -> {

            LocalDate startDate = startDatePicker5.getValue();
            LocalDate endDate = endDatePicker5.getValue();
            if (startDate != null & endDate != null) {
                List<Object[]> results = sportClubService.getSportClubsAndAthleteCountByCompetitionsDate(startDate, endDate);
                if (results.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv5.removeAll();
                    startDatePicker5.clear();
                    endDatePicker5.clear();
                } else {
                    displayResults(results, resultDiv5);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            startDatePicker5.clear();
            endDatePicker5.clear();
        });

        // Sixth accordion for querying sport club
        Accordion querySportFacility = new Accordion();
        querySportFacility.addClassName("query-accordion");

        TextField sportFacilityType = new TextField("Тип споруди");
        Button fetchButton6 = new Button("Виконати");

        VerticalLayout accordionContent6 = new VerticalLayout();
        HorizontalLayout inputLayout6 = new HorizontalLayout(sportFacilityType, fetchButton6);
        inputLayout6.setAlignItems(Alignment.BASELINE);
        accordionContent6.add(inputLayout6);

        Div resultDiv6 = new Div();
        resultDiv6.addClassName("result-div");
        accordionContent6.add(resultDiv6);

        AccordionPanel competitionsPanel6 = querySportFacility.add("Одержати перелік спортивних споруд вказаного типу", accordionContent6);
        querySportFacility.close();

        fetchButton6.addClickListener(e -> {

            String sportFacility = sportFacilityType.getValue();
            if (!sportFacility.isEmpty()) {
                List<SportFacility> facilities = sportFacilityService.findFacilitiesByType(sportFacility);
                if (facilities.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv6.removeAll();
                    sportFacilityType.clear();
                } else {
                    displayResults(facilities, resultDiv6);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            sportFacilityType.clear();
        });

        // Seventh accordion for querying sport club + theirs spesific types
        SportFacilityComponent sportFacilityComponent = new SportFacilityComponent(sportFacilityService);

        // Eigth accordion for querying sport facilities
        Accordion querySportFacilityAndDate = new Accordion();
        querySportFacilityAndDate.addClassName("query-accordion");

        DatePicker startDate8 = new DatePicker("Початкова дата");
        DatePicker endDate8 = new DatePicker("Кінцева дата");
        Button fetchButton8 = new Button("Виконати");

        VerticalLayout accordionContent8 = new VerticalLayout();
        HorizontalLayout inputLayout8 = new HorizontalLayout(startDate8, endDate8, fetchButton8);
        inputLayout8.setAlignItems(Alignment.BASELINE);
        accordionContent8.add(inputLayout8);

        Div resultDiv8 = new Div();
        resultDiv8.addClassName("result-div");
        accordionContent8.add(resultDiv8);

        AccordionPanel competitionsPanel8 = querySportFacilityAndDate.add("Одержати перелік спортивних споруд та дати проведення на них змагань протягом певного періоду", accordionContent8);
        querySportFacilityAndDate.close();

        fetchButton8.addClickListener(e -> {

            LocalDate startDate = startDate8.getValue();
            LocalDate endDate = endDate8.getValue();
            if (startDate != null && endDate != null) {
                List<Object[]> facilities = sportFacilityService.getFacilitiesAndCompetitionDatesByDateRange(startDate,endDate);
                if (facilities.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv8.removeAll();
                    startDate8.clear();
                    endDate8.clear();
                } else {
                    displayResults(facilities, resultDiv8);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            startDate8.clear();
            endDate8.clear();
        });

        //Ninth accordion for querying organizers + competitions
        Accordion queryOrganizerCount = new Accordion();
        queryOrganizerCount.addClassName("query-accordion");

        DatePicker startDate9 = new DatePicker("Початкова дата");
        DatePicker endDate9 = new DatePicker("Кінцева дата");
        Button fetchButton9 = new Button("Виконати");

        VerticalLayout accordionContent9 = new VerticalLayout();
        HorizontalLayout inputLayout9 = new HorizontalLayout(startDate9, endDate9, fetchButton9);
        inputLayout9.setAlignItems(Alignment.BASELINE);
        accordionContent9.add(inputLayout9);

        Div resultDiv9 = new Div();
        resultDiv9.addClassName("result-div");
        accordionContent9.add(resultDiv9);

        AccordionPanel competitionsPanel9 = queryOrganizerCount.add("Одержати список організаторів змагань та кількість змагань, які були ними\n" +
                "проведені протягом вказаного періоду", accordionContent9);
        queryOrganizerCount.close();

        fetchButton9.addClickListener(e -> {

            LocalDate start = startDate9.getValue();
            LocalDate end = endDate9.getValue();
            if (start != null && end != null) {
                List<Object[]> organizers = competitionService.getOrganizersAndCompetitionCountByDateRange(start, end);
                if (organizers.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv9.removeAll();
                    startDate9.clear();
                    endDate9.clear();
                } else {
                    displayResults(organizers, resultDiv9);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            startDate9.clear();
            endDate9.clear();
        });

        //10th accordion for querying awardings
        Accordion queryAwarding = new Accordion();
        queryAwarding.addClassName("query-accordion");

        TextField competitionName = new TextField("Назва змагання");
        Button fetchButton10 = new Button("Виконати");

        VerticalLayout accordionContent10 = new VerticalLayout();
        HorizontalLayout inputLayout10 = new HorizontalLayout(competitionName, fetchButton10);
        inputLayout10.setAlignItems(Alignment.BASELINE);
        accordionContent10.add(inputLayout10);

        Div resultDiv10 = new Div();
        resultDiv10.addClassName("result-div");
        accordionContent10.add(resultDiv10);

        AccordionPanel competitionsPanel10 = queryAwarding.add("Одержати список призерів вказаного змагання", accordionContent10);
        queryAwarding.close();

        fetchButton10.addClickListener(e -> {

            String competition = competitionName.getValue();
            if (!competition.isEmpty()) {
                List<Award> awards = awardService.getAwardsByCompetition(competition);
                if (awards.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv10.removeAll();
                    competitionName.clear();
                } else {
                    displayResults(awards, resultDiv10);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            competitionName.clear();
        });


        //11th accordion for querying athletes
        Accordion queryAthlete = new Accordion();
        queryAthlete.addClassName("query-accordion");

        TextField sportName = new TextField("Вид спорту");
        Button fetchButton11 = new Button("Виконати");

        VerticalLayout accordionContent11 = new VerticalLayout();
        HorizontalLayout inputLayout11 = new HorizontalLayout(sportName, fetchButton11);
        inputLayout11.setAlignItems(Alignment.BASELINE);
        accordionContent11.add(inputLayout11);

        Div resultDiv11 = new Div();
        resultDiv11.addClassName("result-div");
        accordionContent11.add(resultDiv11);

        AccordionPanel competitionsPanel11 = queryAthlete.add("Одержати список спортсменів, які займаються певним видом спорту", accordionContent11);
        queryAthlete.close();

        fetchButton11.addClickListener(e -> {

            String sport = sportName.getValue();
            if (!sport.isEmpty()) {
                List<Athlete> athletes = athleteService.findAthletesBySportTypeName(sport);
                if (athletes.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv11.removeAll();
                    sportName.clear();
                } else {
                    displayResults(athletes, resultDiv11);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            sportName.clear();
        });

        //12th for querying athletes with rank
        Accordion queryAthleteRank = new Accordion();
        queryAthleteRank.addClassName("query-accordion");

        TextField sportNameInfo = new TextField("Вид спорту");
        TextField rank = new TextField("Розряд спортсмена");
        Button fetchButton12 = new Button("Виконати");

        VerticalLayout accordionContent12 = new VerticalLayout();
        HorizontalLayout inputLayout12 = new HorizontalLayout(sportNameInfo, rank, fetchButton12);
        inputLayout12.setAlignItems(Alignment.BASELINE);
        accordionContent12.add(inputLayout12);

        Div resultDiv12 = new Div();
        resultDiv12.addClassName("result-div");
        accordionContent12.add(resultDiv12);

        AccordionPanel competitionsPanel12 = queryAthleteRank.add("Одержати список спортсменів, які займаються певним видом спорту та не нижче певного розряду", accordionContent12);
        queryAthleteRank.close();

        fetchButton12.addClickListener(e -> {

            String sportInfo = sportNameInfo.getValue();
            String rankInfo = rank.getValue();
            if (!sportInfo.isEmpty() && !rankInfo.isEmpty()) {
                List<Athlete> athletes = athleteService.findAthletesBySportTypeNameAndRank(sportInfo, rankInfo);
                if (athletes.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv12.removeAll();
                    sportNameInfo.clear();
                    rank.clear();
                } else {
                    displayResults(athletes, resultDiv12);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            sportNameInfo.clear();
            rank.clear();
        });


        //13th for querying athletes with multiple sport types
        Accordion queryAthleteMultipleSportType = new Accordion();
        queryAthleteMultipleSportType.addClassName("query-accordion");

        TextField sportTypes = new TextField("Види спорту(розділені комою)");
        Button fetchButton13 = new Button("Виконати");

        VerticalLayout accordionContent13 = new VerticalLayout();
        HorizontalLayout inputLayout13 = new HorizontalLayout(sportTypes, fetchButton13);
        inputLayout13.setAlignItems(Alignment.BASELINE);
        accordionContent13.add(inputLayout13);

        Div resultDiv13 = new Div();
        resultDiv13.addClassName("result-div");
        accordionContent13.add(resultDiv13);

        AccordionPanel competitionsPanel13 = queryAthleteMultipleSportType.add("Одержати список спортсменів, які займаються кількома видами спорту (вказавши їх)", accordionContent13);
        queryAthleteMultipleSportType.close();

        fetchButton13.addClickListener(e -> {

            String sportTypeInfo = sportTypes.getValue();
            if (!sportTypeInfo.isEmpty()) {
                List<Athlete> athletes = athleteService.findAthletesByMultipleSportTypes(sportTypeInfo);
                if (athletes.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv13.removeAll();
                    sportTypes.clear();
                } else {
                    displayResults(athletes, resultDiv13);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            sportTypes.clear();
        });

        //14th accordion for querying athletes that not in competitions
        Accordion queryAthletesCompetition = new Accordion();
        queryAthletesCompetition.addClassName("query-accordion");

        DatePicker startDate14 = new DatePicker("Початкова дата");
        DatePicker endDate14 = new DatePicker("Кінцева дата");
        Button fetchButton14 = new Button("Виконати");

        VerticalLayout accordionContent14 = new VerticalLayout();
        HorizontalLayout inputLayout14 = new HorizontalLayout(startDate14, endDate14, fetchButton14);
        inputLayout14.setAlignItems(Alignment.BASELINE);
        accordionContent14.add(inputLayout14);

        Div resultDiv14 = new Div();
        resultDiv14.addClassName("result-div");
        accordionContent14.add(resultDiv14);

        AccordionPanel competitionsPanel14 = queryAthletesCompetition.add("Одержати список спортсменів, які не брали участь у змаганнях\n" +
                "протягом вказаного періоду", accordionContent14);
        queryAthletesCompetition.close();

        fetchButton14.addClickListener(e -> {

            LocalDate start = startDate14.getValue();
            LocalDate end = endDate14.getValue();
            if (start != null && end != null) {
                List<Athlete> athletes = athleteService.findAthletesNotInCompetitionsByDate(start, end);
                if (athletes.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv14.removeAll();
                    startDate14.clear();
                    endDate14.clear();
                } else {
                    displayResults(athletes, resultDiv14);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            startDate14.clear();
            endDate14.clear();
        });

        //15th for querying athletes by coach
        Accordion queryAthleteCoach = new Accordion();
        queryAthleteCoach.addClassName("query-accordion");

        TextField coachName = new TextField("Ім'я тренера");
        Button fetchButton15 = new Button("Виконати");

        VerticalLayout accordionContent15 = new VerticalLayout();
        HorizontalLayout inputLayout15 = new HorizontalLayout(coachName, fetchButton15);
        inputLayout15.setAlignItems(Alignment.BASELINE);
        accordionContent15.add(inputLayout15);

        Div resultDiv15 = new Div();
        resultDiv15.addClassName("result-div");
        accordionContent15.add(resultDiv15);

        AccordionPanel competitionsPanel15 = queryAthleteCoach.add("Одержати список спортсменів, які займаються у вказаного тренера", accordionContent15);
        queryAthleteCoach.close();

        fetchButton15.addClickListener(e -> {

            String coachInfo = coachName.getValue();
            if (!coachInfo.isEmpty()) {
                List<Athlete> athletes = coachService.findAthletesByCoachName(coachInfo);
                if (athletes.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv15.removeAll();
                    coachName.clear();
                } else {
                    displayResults(athletes, resultDiv15);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            coachName.clear();
        });

        //16th for querying athletes by coach and rank
        Accordion queryAthleteCoachRank = new Accordion();
        queryAthleteCoachRank.addClassName("query-accordion");

        TextField coach = new TextField("Ім'я тренера");
        TextField athleteRank = new TextField("Розряд спортсмена");
        Button fetchButton16 = new Button("Виконати");

        VerticalLayout accordionContent16 = new VerticalLayout();
        HorizontalLayout inputLayout16 = new HorizontalLayout(coach, athleteRank, fetchButton16);
        inputLayout16.setAlignItems(Alignment.BASELINE);
        accordionContent16.add(inputLayout16);

        Div resultDiv16 = new Div();
        resultDiv16.addClassName("result-div");
        accordionContent16.add(resultDiv16);

        AccordionPanel competitionsPanel16 = queryAthleteCoachRank.add("Одержати список спортсменів, які займаються у вказаного тренера та певного розряду", accordionContent16);
        queryAthleteCoachRank.close();

        fetchButton16.addClickListener(e -> {

            String coachInfo = coach.getValue();
            String rankInfo = athleteRank.getValue();
            if (!coachInfo.isEmpty() && !rankInfo.isEmpty()) {
                List<Athlete> athletes = coachService.findAthletesByCoachNameAndRank(coachInfo, rankInfo);
                if (athletes.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv16.removeAll();
                    coach.clear();
                    rank.clear();
                } else {
                    displayResults(athletes, resultDiv16);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            coach.clear();
            athleteRank.clear();
        });

        //17th accordion for querying coarches with sport types
        Accordion queryAthleteCoachSportType = new Accordion();
        queryAthleteCoachSportType.addClassName("query-accordion");

        TextField athleteInfo = new TextField("Ім'я спортсмена");
        TextField sportInfo = new TextField("Вид спорту");
        Button fetchButton17 = new Button("Виконати");

        VerticalLayout accordionContent17 = new VerticalLayout();
        HorizontalLayout inputLayout17 = new HorizontalLayout(athleteInfo, sportInfo, fetchButton17);
        inputLayout17.setAlignItems(Alignment.BASELINE);
        accordionContent17.add(inputLayout17);

        Div resultDiv17 = new Div();
        resultDiv17.addClassName("result-div");
        accordionContent17.add(resultDiv17);

        AccordionPanel competitionsPanel17 = queryAthleteCoachSportType.add("Одержати список тренерів вказаного спортсмена з вказаного виду спорту", accordionContent17);
        queryAthleteCoachSportType.close();

        fetchButton17.addClickListener(e -> {

            String athlete = athleteInfo.getValue();
            String sport = sportInfo.getValue();
            if (!athlete.isEmpty() && !sport.isEmpty()) {
                List<Coach> coaches = coachService.findCoachesByAthleteAndSportType(athlete,sport);
                if (coaches.isEmpty()) {
                    Notification.show("Нема такої інформації", 3000, Notification.Position.TOP_CENTER);
                    resultDiv17.removeAll();
                    athleteInfo.clear();
                    sportInfo.clear();
                } else {
                    displayResults(coaches, resultDiv17);
                }
            } else {
                Notification.show("Заповніть поля", 3000, Notification.Position.TOP_CENTER);
            }
            athleteInfo.clear();
            sportInfo.clear();
        });

        // Adding components to the view
        add(queryCompetitions, queryCompetitionsOrganizer, queryFacility,
                queryFacilitySportType, queryFacilitySportType, querySportClub,
                querySportFacility, sportFacilityComponent, querySportFacilityAndDate,
                queryOrganizerCount, queryAwarding, queryAthlete, queryAthleteRank,
                queryAthleteMultipleSportType, queryAthletesCompetition,
                queryAthleteCoach, queryAthleteCoachRank, queryAthleteCoachSportType);
    }

    private void setupMenu(MenuBar menuBar) {
        MenuItem tableMenu = menuBar.addItem("Таблиці");
        tableMenu.getSubMenu().addItem("Спортсмени", e -> handleOptionSelection("Option 1"));
        tableMenu.getSubMenu().addItem("Тренери", e -> handleOptionSelection("Option 2"));
        tableMenu.addClassName("menu-item");
        MenuItem aggregations = menuBar.addItem("Агрегації");
        aggregations.addClassName("menu-item");
        aggregations.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("aggregations")));
        tableMenu.addClassName("menu-item");
        MenuItem addUserMenu = menuBar.addItem("Додати користувача");
        addUserMenu.addClickListener(e -> handleAddUser());
        addUserMenu.addClassName("menu-item");
        MenuItem logoutMenu = menuBar.addItem("Вийти");
        logoutMenu.addClassName("menu-item");
        logoutMenu.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("")));
    }

    private void handleOptionSelection(String option) {
        System.out.println("Selected: " + option);
    }

    private void handleAddUser() {
        Dialog addUserDialog = new Dialog();
        addUserDialog.setCloseOnEsc(true);
        addUserDialog.setCloseOnOutsideClick(true);

        Paragraph title = new Paragraph("Створити нового користувача");
        TextField usernameField = new TextField("Логін");
        TextField passwordField = new TextField("Пароль");
        ComboBox<String> roleField = new ComboBox<>("Роль");
        roleField.setItems("Адміністратор", "Оператор", "Гість");
        Button addButton = new Button("Додати");

        addButton.addClickListener(click -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            String role = roleField.getValue();

            if (username.isEmpty() || password.isEmpty() || role == null) {
                Notification.show("Будь ласка, заповніть всі поля", 3000, Notification.Position.TOP_CENTER);
            } else {
                if (keyService.userExists(username)) {
                    Notification.show("Користувач з таким логіном вже існує", 3000, Notification.Position.TOP_CENTER);
                } else {
                    Key newKey = new Key();
                    newKey.setUserLogin(username);
                    newKey.setUserPassword(password);
                    newKey.setUserRole(role);

                    try {
                        keyService.saveKey(newKey);
                        Notification.show("Користувача успішно додано", 3000, Notification.Position.TOP_CENTER);
                        addUserDialog.close();
                    } catch (Exception ex) {
                        Notification.show("Помилка при додаванні користувача", 3000, Notification.Position.TOP_CENTER);
                        ex.printStackTrace();
                    }
                }
            }
        });

        VerticalLayout layout = new VerticalLayout(title, usernameField, passwordField, roleField, addButton);
        addUserDialog.add(layout);
        addUserDialog.open();
    }

    private <T> void displayResults(List<T> results, Div resultDiv) {
        resultDiv.removeAll();
        for (T result : results) {
            String resultInfo = result.toString();
            resultDiv.add(new Paragraph(resultInfo));
        }
    }

}

