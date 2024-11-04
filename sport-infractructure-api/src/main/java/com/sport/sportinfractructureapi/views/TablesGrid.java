package com.sport.sportinfractructureapi.views;

import com.sport.sportinfractructureapi.model.*;
import com.sport.sportinfractructureapi.service.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class TablesGrid<T> extends VerticalLayout {

    private final Grid<T> grid;
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
    private TextField nameFilter;
    private TextField rankFilter;
    private final Class<T> entityClass;
    private final Key authenticatedUser;
    private final String userRole;

    public TablesGrid(AthleteService athleteService, SportClubService sportClubService,
                      CoachService coachService, AwardService awardService,
                      CompetitionService competitionService, OrganizerService organizerService,
                      SportFacilityService sportFacilityService, SportTypeService sportTypeService,
                      CompetitionParticipantService competitionParticipantService,
                      AthleteCoachService athleteCoachService,
                      AthleteSportTypeService athleteSportTypeService,
                      KeyService keyService,
                      Class<T> entityClass) {
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
        this.entityClass = entityClass;
        this.grid = new Grid<>(entityClass, false);

        this.authenticatedUser = keyService.getAuthenticatedUser();
        this.userRole = authenticatedUser != null ? authenticatedUser.getUserRole() : "Гість";

        configureGrid();
        configureFilters();
        refreshItems();

        if (userRole.equalsIgnoreCase("Власник") ||
                (userRole.equalsIgnoreCase("Адміністратор") && entityClass.equals(Competition.class))) {
            addAddButton();
        }
        add(grid);
        setSizeFull();
    }

    private void configureFilters() {
        nameFilter = new TextField();
        nameFilter.setPlaceholder("Пошук по імені");

        Button searchButton = new Button(VaadinIcon.SEARCH.create());
        searchButton.addClickListener(e -> search());

        Button clearButton = new Button(VaadinIcon.CLOSE_CIRCLE.create());
        clearButton.addClickListener(e -> clearFilters());


        HorizontalLayout filterLayout = new HorizontalLayout(nameFilter, searchButton, clearButton);
        filterLayout.setWidthFull();
        filterLayout.setAlignItems(Alignment.CENTER);

        HorizontalLayout topLayout = new HorizontalLayout(filterLayout);
        topLayout.setWidthFull();
        topLayout.setAlignItems(Alignment.CENTER);
        topLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        add(topLayout);
    }

    private void search() {
        String name = nameFilter.getValue().trim();

        if (entityClass.equals(Athlete.class)) {
            List<Athlete> filteredAthletes = athleteService.getAllAthletes().stream()
                    .filter(athlete -> athlete.getAthleteName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
            grid.setItems((List<T>) filteredAthletes);
        } else if (entityClass.equals(Coach.class)) {
            List<Coach> filteredCoaches = coachService.getAllCoaches().stream()
                    .filter(coach -> coach.getCoachName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
            grid.setItems((List<T>) filteredCoaches);
        } else if (entityClass.equals(Award.class)) {
            List<Award> filteredAwards = awardService.getAllAwards().stream()
                    .filter(award -> {
                        boolean matchesAthlete = award.getAthlete() != null &&
                                award.getAthlete().getAthleteName().toLowerCase().contains(name.toLowerCase());
                        boolean matchesAwardType = award.getAwardType().toLowerCase().contains(name.toLowerCase());
                        return matchesAthlete || matchesAwardType;
                    })
                    .toList();
            grid.setItems((List<T>) filteredAwards);
        } else if (entityClass.equals(Organizer.class)) {
            List<Organizer> filteredOrganizers = organizerService.getAllOrganizers().stream()
                    .filter(organizer -> organizer.getOrganizerName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
            grid.setItems((List<T>) filteredOrganizers);
        } else if (entityClass.equals(SportClub.class)) {
            List<SportClub> filteredSportClubs = sportClubService.getAllSportClubs().stream()
                    .filter(sportClub -> sportClub.getSportClubName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
            grid.setItems((List<T>) filteredSportClubs);
        } else if (entityClass.equals(SportType.class)) {
            List<SportType> filteredSportTypes = sportTypeService.getAllSportTypes().stream()
                    .filter(sportType -> sportType.getSportName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
            grid.setItems((List<T>) filteredSportTypes);
        } else if (entityClass.equals(Competition.class)) {
            List<Competition> filteredCompetitions = competitionService.getAllCompetitions().stream()
                    .filter(competition -> competition.getCompetitionName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
            grid.setItems((List<T>) filteredCompetitions);
        }  else if (entityClass.equals(CompetitionParticipant.class)) {
            List<CompetitionParticipant> filteredCompetitionParticipants = competitionParticipantService.getAllCompetitionParticipants().stream()
                    .filter(competitionParticipant -> competitionParticipant.getAthlete().getAthleteName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
            grid.setItems((List<T>) filteredCompetitionParticipants);
        }  else if (entityClass.equals(SportFacility.class)) {
            List<SportFacility> filteredSportFacilities = sportFacilityService.getAllSportFacilities().stream()
                    .filter(sportFacility -> sportFacility.getFacilityName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
            grid.setItems((List<T>) filteredSportFacilities);
        }  else if (entityClass.equals(AthleteCoach.class)) {
            List<AthleteCoach> filteredAthleteCoaches = athleteCoachService.getAllAthletesCoaches().stream()
                    .filter(athleteCoach -> athleteCoach.getCoach().getCoachName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
            grid.setItems((List<T>) filteredAthleteCoaches);
        }  else if (entityClass.equals(AthleteSportType.class)) {
            List<AthleteSportType> filteredAthleteSportType = athleteSportTypeService.getAllAthletesSportTypes().stream()
                    .filter(athleteSportType -> athleteSportType.getAthlete().getAthleteName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
            grid.setItems((List<T>) filteredAthleteSportType);
        }

    }

    private void addAddButton() {
        Button addButton = new Button("Додати");
        addButton.addClickListener(e -> openAddDialog());
        add(addButton);
        getChildren().filter(component -> component instanceof HorizontalLayout)
                .findFirst()
                .ifPresent(layout -> ((HorizontalLayout) layout).add(addButton));
    }

    private void clearFilters() {
        nameFilter.clear();
        refreshItems();
    }

    private void configureGrid() {
        grid.addClassName("dynamic-grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        if (entityClass.equals(Athlete.class)) {
            configureAthleteGrid();
        } else if (entityClass.equals(Coach.class)) {
            configureCoachGrid();
        } else if (entityClass.equals(Award.class)) {
            configureAwardGrid();
        } else if (entityClass.equals(Organizer.class)) {
            configureOrganizerGrid();
        }  else if (entityClass.equals(SportClub.class)) {
            configureSportClubGrid();
        } else if (entityClass.equals(SportType.class)) {
            configureSportTypeGrid();
        } else if (entityClass.equals(Competition.class)) {
            configureCompetitionGrid();
        }  else if (entityClass.equals(CompetitionParticipant.class)) {
            configureCompetitionParticipantsGrid();
        }  else if (entityClass.equals(SportFacility.class)) {
            configureSportFacilityGrid();
        }  else if (entityClass.equals(AthleteCoach.class)) {
            configureAthleteCoachGrid();
        }  else if (entityClass.equals(AthleteSportType.class)) {
            configureAthleteSportTypesGrid();
        }
    }

    private void configureAthleteGrid() {
        Grid<Athlete> athleteGrid = (Grid<Athlete>) grid;

        athleteGrid.addColumn(Athlete::getAthleteId).setHeader("ID").setSortable(true);
        athleteGrid.addColumn(Athlete::getAthleteName).setHeader("Ім'я").setSortable(true);
        athleteGrid.addColumn(Athlete::getAthleteRank).setHeader("Розряд").setSortable(true);
        athleteGrid.addColumn(athlete -> athlete.getSportClub() != null ? athlete.getSportClub().getSportClubName() : "Не визначено").setHeader("Спортивний клуб").setSortable(true);

        if (userRole.equalsIgnoreCase("Власник")) {
            athleteGrid.addComponentColumn(athlete -> {
                Button deleteButton = new Button(VaadinIcon.MINUS.create());
                deleteButton.addClickListener(e -> openDeleteDialog(athlete.getAthleteId(), athlete.getAthleteName()));

                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addClickListener(e -> openEditDialog(athlete));

                return new HorizontalLayout(editButton, deleteButton);
            }).setHeader("Дії");
        }
    }

    private void configureCoachGrid() {
        Grid<Coach> coachGrid = (Grid<Coach>) grid;

        coachGrid.addColumn(Coach::getCoachId).setHeader("ID").setSortable(true);
        coachGrid.addColumn(Coach::getCoachName).setHeader("Ім'я").setSortable(true);

        if (userRole.equalsIgnoreCase("Власник"))
        {
            coachGrid.addComponentColumn(coach -> {
                Button deleteButton = new Button(VaadinIcon.MINUS.create());
                deleteButton.addClickListener(e -> openDeleteDialog(coach.getCoachId(), coach.getCoachName()));

                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addClickListener(e -> openEditDialog(coach));

                return new HorizontalLayout(editButton, deleteButton);
            }).setHeader("Дії");
        }

    }

    private void configureAwardGrid() {
        Grid<Award> awardGrid = (Grid<Award>) grid;

        awardGrid.addColumn(Award::getAwardId).setHeader("ID").setSortable(true);
        awardGrid.addColumn(Award::getAthletePlace).setHeader("Місце").setSortable(true);
        awardGrid.addColumn(Award::getAwardType).setHeader("Тип нагороди").setSortable(true);

        awardGrid.addColumn(award -> award.getAthlete() != null ? award.getAthlete().getAthleteName() : "")
                .setHeader("Спортсмен")
                .setSortable(true);

        awardGrid.addColumn(award -> award.getCompetition() != null ? award.getCompetition().getCompetitionName() : "")
                .setHeader("Змагання")
                .setSortable(true);

        if (userRole.equalsIgnoreCase("Власник")) {
            awardGrid.addComponentColumn(award -> {
                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addClickListener(e -> openEditDialog(award));

                if (userRole.equalsIgnoreCase("Власник")) {
                    Button deleteButton = new Button(VaadinIcon.MINUS.create());
                    deleteButton.addClickListener(e -> openDeleteDialog(award.getAwardId(), award.getAwardType()));
                    return new HorizontalLayout(editButton, deleteButton);
                }

                return new HorizontalLayout(editButton);
            }).setHeader("Дії");
        } else if(userRole.equalsIgnoreCase("Оператор")) {
            awardGrid.addComponentColumn(award -> {
                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addClickListener(e -> openEditDialog(award));
                return new HorizontalLayout(editButton);
            }).setHeader("Дії");
        }
    }


    private void configureOrganizerGrid() {
        Grid<Organizer> organizerGrid = (Grid<Organizer>) grid;

        organizerGrid.addColumn(Organizer::getOrganizerId).setHeader("ID").setSortable(true);
        organizerGrid.addColumn(Organizer::getOrganizerName).setHeader("Ім'я").setSortable(true);

        if (userRole.equalsIgnoreCase("Власник"))
        {
            organizerGrid.addComponentColumn(organizer -> {
                Button deleteButton = new Button(VaadinIcon.MINUS.create());
                deleteButton.addClickListener(e -> openDeleteDialog(organizer.getOrganizerId(), organizer.getOrganizerName()));

                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addClickListener(e -> openEditDialog(organizer));

                return new HorizontalLayout(editButton, deleteButton);
            }).setHeader("Дії");
        }

    }

    private void configureSportClubGrid() {
        Grid<SportClub> sportClubGrid = (Grid<SportClub>) grid;

        sportClubGrid.addColumn(SportClub::getSportClubId).setHeader("ID").setSortable(true);
        sportClubGrid.addColumn(SportClub::getSportClubName).setHeader("Назва").setSortable(true);

        if (userRole.equalsIgnoreCase("Власник")) {
            sportClubGrid.addComponentColumn(sportClub -> {
                Button deleteButton = new Button(VaadinIcon.MINUS.create());
                deleteButton.addClickListener(e -> openDeleteDialog(sportClub.getSportClubId(), sportClub.getSportClubName()));

                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addClickListener(e -> openEditDialog(sportClub));

                return new HorizontalLayout(editButton, deleteButton);
            }).setHeader("Дії");
        }
    }

    private void configureSportTypeGrid() {
        Grid<SportType> sportTypeGrid = (Grid<SportType>) grid;

        sportTypeGrid.addColumn(SportType::getSportTypeId).setHeader("ID").setSortable(true);
        sportTypeGrid.addColumn(SportType::getSportName).setHeader("Назва").setSortable(true);

        if (userRole.equalsIgnoreCase("Власник")) {
            sportTypeGrid.addComponentColumn(sportType -> {
                Button deleteButton = new Button(VaadinIcon.MINUS.create());
                deleteButton.addClickListener(e -> openDeleteDialog(sportType.getSportTypeId(), sportType.getSportName()));

                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addClickListener(e -> openEditDialog(sportType));

                return new HorizontalLayout(editButton, deleteButton);
            }).setHeader("Дії");
        }
    }

    private void configureCompetitionGrid() {
        Grid<Competition> competitionGrid = (Grid<Competition>) grid;

        competitionGrid.addColumn(Competition::getCompetitionId).setHeader("ID").setSortable(true);
        competitionGrid.addColumn(Competition::getCompetitionDate).setHeader("Дата").setSortable(true);
        competitionGrid.addColumn(Competition::getCompetitionName).setHeader("Ім'я").setSortable(true);
        competitionGrid.addColumn(competition -> competition.getOrganizer() != null ? competition.getOrganizer().getOrganizerName() : "").setHeader("Організатор").setSortable(true);
        competitionGrid.addColumn(competition -> competition.getSportFacility() != null ? competition.getSportFacility().getFacilityName() : "").setHeader("Спортивна споруда").setSortable(true);
        competitionGrid.addColumn(competition -> competition.getSportType() != null ? competition.getSportType().getSportName() : "").setHeader("Тип спорту").setSortable(true);

        if (userRole.equalsIgnoreCase("Адміністратор") ||
                userRole.equalsIgnoreCase("Власник"))
        {
            competitionGrid.addComponentColumn(competition -> {
                Button deleteButton = new Button(VaadinIcon.MINUS.create());
                deleteButton.addClickListener(e -> openDeleteDialog(competition.getCompetitionId(), competition.getCompetitionName()));

                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addClickListener(e -> openEditDialog(competition));

                return new HorizontalLayout(editButton, deleteButton);
            }).setHeader("Дії");
        }
    }

    private void configureCompetitionParticipantsGrid() {
        Grid<CompetitionParticipant> participantsGrid = (Grid<CompetitionParticipant>) grid;

        participantsGrid.addColumn(participant -> participant.getAthlete().getAthleteId()).setHeader("ID спортсмена").setSortable(true);
        participantsGrid.addColumn(participant -> participant.getAthlete().getAthleteName()).setHeader("Ім'я спортсмена").setSortable(true);
        participantsGrid.addColumn(participant -> participant.getCompetition().getCompetitionId()).setHeader("ID змагання").setSortable(true);
        participantsGrid.addColumn(participant -> participant.getCompetition().getCompetitionName()).setHeader("Назва змагання").setSortable(true);

        if (userRole.equalsIgnoreCase("Власник")) {
            participantsGrid.addComponentColumn(participant -> {
                Button deleteButton = new Button(VaadinIcon.MINUS.create());
                deleteButton.addClickListener(e -> openDeleteDialog(
                        participant.getAthlete().getAthleteId(),
                        participant.getCompetition().getCompetitionId().toString()
                ));

                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addClickListener(e -> openEditDialog(participant));

                return new HorizontalLayout(editButton, deleteButton);
            }).setHeader("Дії");
        }
    }

    private void configureSportFacilityGrid() {
        Grid<SportFacility> facilitiesGrid = (Grid<SportFacility>) grid;

        facilitiesGrid.addColumn(SportFacility::getFacilityId).setHeader("ID").setSortable(true);
        facilitiesGrid.addColumn(SportFacility::getFacilityName).setHeader("Назва").setSortable(true);
        facilitiesGrid.addColumn(SportFacility::getFacilityAddress).setHeader("Адреса").setSortable(true);
        facilitiesGrid.addColumn(SportFacility::getFacilityType).setHeader("Тип").setSortable(true);
        facilitiesGrid.addColumn(SportFacility::getFacilityDimensions).setHeader("Розміри").setSortable(true);

        facilitiesGrid.addColumn(facility -> {
            if ("Стадіон".equalsIgnoreCase(facility.getFacilityType())) {
                return facility.getStadiumCapacity() != null ? facility.getStadiumCapacity() : "N/A";
            } else if ("Корт".equalsIgnoreCase(facility.getFacilityType())) {
                return facility.getCortCoating() != null ? facility.getCortCoating() : "N/A";
            } else if ("Зал".equalsIgnoreCase(facility.getFacilityType())) {
                return facility.getGymEquipment() != null ? facility.getGymEquipment() : "N/A";
            } else if ("Манеж".equalsIgnoreCase(facility.getFacilityType())) {
                return facility.getPlaypenCcs() != null ? facility.getPlaypenCcs() : "N/A";
            }
            return "N/A";
        }).setHeader("Додаткова інформація");

        if (userRole.equalsIgnoreCase("Власник")) {
            facilitiesGrid.addComponentColumn(facility -> {
                Button deleteButton = new Button(VaadinIcon.MINUS.create());
                deleteButton.addClickListener(e -> openDeleteDialog(facility.getFacilityId(), facility.getFacilityName()));

                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addClickListener(e -> openEditDialog(facility));

                return new HorizontalLayout(editButton, deleteButton);
            }).setHeader("Дії");
        }
    }

    private void configureAthleteCoachGrid() {
        Grid<AthleteCoach> athleteCoachGrid = (Grid<AthleteCoach>) grid;

        athleteCoachGrid.addColumn(athleteCoach -> athleteCoach.getAthlete().getAthleteId()).setHeader("ID спортсмена").setSortable(true);
        athleteCoachGrid.addColumn(athleteCoach -> athleteCoach.getAthlete().getAthleteName()).setHeader("Ім'я спортсмена").setSortable(true);
        athleteCoachGrid.addColumn(athleteCoach -> athleteCoach.getCoach().getCoachId()).setHeader("ID тренера").setSortable(true);
        athleteCoachGrid.addColumn(athleteCoach -> athleteCoach.getCoach().getCoachName()).setHeader("Ім'я тренера").setSortable(true);
        athleteCoachGrid.addColumn(athleteCoach -> athleteCoach.getSportType().getSportTypeId()).setHeader("ID виду спорту").setSortable(true);
        athleteCoachGrid.addColumn(athleteCoach -> athleteCoach.getSportType().getSportName()).setHeader("Назва виду спорту").setSortable(true);

        if (userRole.equalsIgnoreCase("Власник")) {
            athleteCoachGrid.addComponentColumn(athleteCoach -> {
                Button deleteButton = new Button(VaadinIcon.MINUS.create());
                deleteButton.addClickListener(e -> openDeleteDialog(athleteCoach.getAthlete().getAthleteId(),
                        athleteCoach.getCoach().getCoachId() + "," + athleteCoach.getSportType().getSportTypeId()));

                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addClickListener(e -> openEditDialog(athleteCoach));

                return new HorizontalLayout(editButton, deleteButton);
            }).setHeader("Дії");
        }

    }

    private void configureAthleteSportTypesGrid() {
        Grid<AthleteSportType> athleteSportTypeGrid = (Grid<AthleteSportType>) grid;

       athleteSportTypeGrid.addColumn(athleteSportType -> athleteSportType.getAthlete().getAthleteId()).setHeader("ID спортсмена").setSortable(true);
       athleteSportTypeGrid.addColumn(athleteSportType -> athleteSportType.getAthlete().getAthleteName()).setHeader("Ім'я спортсмена").setSortable(true);
       athleteSportTypeGrid.addColumn(athleteSportType -> athleteSportType.getSportType().getSportTypeId()).setHeader("ID виду спорту").setSortable(true);
       athleteSportTypeGrid.addColumn(athleteSportType -> athleteSportType.getSportType().getSportName()).setHeader("Назва виду спорту").setSortable(true);

        if (userRole.equalsIgnoreCase("Власник")) {
            athleteSportTypeGrid.addComponentColumn(athleteSportType -> {
                Button deleteButton = new Button(VaadinIcon.MINUS.create());
                deleteButton.addClickListener(e -> openDeleteDialog(
                        athleteSportType.getAthlete().getAthleteId(),
                        athleteSportType.getSportType().getSportTypeId().toString()
                ));

                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addClickListener(e -> openEditDialog(athleteSportType));

                return new HorizontalLayout(editButton, deleteButton);
            }).setHeader("Дії");
        }

    }

    private void openDeleteDialog(Long entityId, String entityName) {
        Dialog confirmDialog = new Dialog();
        Paragraph confirmText;

        if (entityClass.equals(Award.class)) {
            Optional<Award> optionalAward = awardService.getAwardById(entityId);

            if (optionalAward.isPresent()) {
                Award award = optionalAward.get();
                String athleteName = award.getAthlete() != null ? award.getAthlete().getAthleteName() : "Unknown Athlete";
                confirmText = new Paragraph("Ви дійсно хочете видалити нагороду атлета " + athleteName + "?");

                Button confirmButton = new Button("Так", event -> {
                    awardService.deleteAward(entityId);
                    refreshItems();
                    confirmDialog.close();
                });

                Button cancelButton = new Button("Ні", event -> confirmDialog.close());

                confirmDialog.add(new VerticalLayout(confirmText, new HorizontalLayout(confirmButton, cancelButton)));
                confirmDialog.open();
            } else {
                Paragraph notFoundText = new Paragraph("Нагороду не знайдено.");
                Button closeButton = new Button("Закрити", event -> confirmDialog.close());
                confirmDialog.add(new VerticalLayout(notFoundText, closeButton));
                confirmDialog.open();
            }
        } else if (entityClass.equals(CompetitionParticipant.class)) {
            Long athleteId = entityId;
            Long competitionId = Long.parseLong(entityName);

            confirmText = new Paragraph("Ви дійсно хочете видалити цього учасника з змагання?");

            Button confirmButton = new Button("Так", event -> {
                competitionParticipantService.deleteCompetitionParticipant(athleteId, competitionId);
                refreshItems();
                confirmDialog.close();
            });

            Button cancelButton = new Button("Ні", event -> confirmDialog.close());

            confirmDialog.add(new VerticalLayout(confirmText, new HorizontalLayout(confirmButton, cancelButton)));
            confirmDialog.open();
        } else if (entityClass.equals(AthleteCoach.class)) {
            Long athleteId = entityId;
            Long coachId = Long.parseLong(entityName.split(",")[0]);
            Long sportTypeId = Long.parseLong(entityName.split(",")[1]);

            confirmText = new Paragraph("Ви дійсно хочете видалити цей зв'язок атлета з тренером?");

            Button confirmButton = new Button("Так", event -> {
                athleteCoachService.deleteAthleteCoach(athleteId, coachId, sportTypeId);
                refreshItems();
                confirmDialog.close();
            });

            Button cancelButton = new Button("Ні", event -> confirmDialog.close());

            confirmDialog.add(new VerticalLayout(confirmText, new HorizontalLayout(confirmButton, cancelButton)));
            confirmDialog.open();
        } else if (entityClass.equals(AthleteSportType.class)) {
            Long athleteId = entityId;
            Long sportTypeId = Long.parseLong(entityName);

            confirmText = new Paragraph("Ви дійсно хочете видалити цей зв'язок атлета з видом спорту?");

            Button confirmButton = new Button("Так", event -> {
                athleteSportTypeService.deleteAthleteSportType(athleteId,sportTypeId);
                refreshItems();
                confirmDialog.close();
            });

            Button cancelButton = new Button("Ні", event -> confirmDialog.close());

            confirmDialog.add(new VerticalLayout(confirmText, new HorizontalLayout(confirmButton, cancelButton)));
            confirmDialog.open();
        } else {
            confirmText = new Paragraph("Ви дійсно хочете видалити " + entityName + "?");
            Button confirmButton = new Button("Так", event -> {
                if (entityClass.equals(Athlete.class)) {
                    athleteService.deleteAthlete(entityId);
                } else if (entityClass.equals(Coach.class)) {
                    coachService.deleteCoach(entityId);
                } else if (entityClass.equals(Organizer.class)) {
                    organizerService.deleteOrganizer(entityId);
                }  else if (entityClass.equals(SportClub.class)) {
                    sportClubService.deleteSportClub(entityId);
                } else if (entityClass.equals(SportType.class)) {
                    sportTypeService.deleteSportType(entityId);
                } else if (entityClass.equals(Competition.class)) {
                    competitionService.deleteCompetition(entityId);
                } else if (entityClass.equals(SportFacility.class)) {
                    sportFacilityService.deleteSportFacility(entityId);
                }
                refreshItems();
                confirmDialog.close();
            });

            Button cancelButton = new Button("Ні", event -> confirmDialog.close());

            confirmDialog.add(new VerticalLayout(confirmText, new HorizontalLayout(confirmButton, cancelButton)));
            confirmDialog.open();
        }
    }


    private void openEditDialog(Object entity) {
        Dialog editDialog = new Dialog();

        TextField nameField = new TextField("Ім'я");

        if (entity instanceof Athlete athlete) {
            nameField.setValue(athlete.getAthleteName());
            TextField rankField = new TextField("Розряд");
            rankField.setValue(athlete.getAthleteRank());

            ComboBox<SportClub> sportClubComboBox = new ComboBox<>("Спортивний клуб");
            sportClubComboBox.setItems(sportClubService.getAllSportClubs());
            sportClubComboBox.setItemLabelGenerator(SportClub::getSportClubName);
            sportClubComboBox.setValue(athlete.getSportClub());

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {

                if(nameField.isEmpty() || rankField.isEmpty() || sportClubComboBox.isEmpty()) {
                    Notification.show("Заповніть всі поля", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                athlete.setAthleteName(nameField.getValue());
                athlete.setAthleteRank(rankField.getValue());
                athlete.setSportClub(sportClubComboBox.getValue());
                athleteService.saveAthlete(athlete);
                refreshItems();
                editDialog.close();
            });

            Button cancelButton = new Button(VaadinIcon.CLOSE_CIRCLE.create(), e -> editDialog.close());

            HorizontalLayout editAthlete = new HorizontalLayout(nameField, rankField, sportClubComboBox, saveButton, cancelButton);
            editAthlete.setAlignItems(Alignment.BASELINE);
            editDialog.add(editAthlete);

        } else if (entity instanceof Coach coach) {
            nameField.setValue(coach.getCoachName());

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {

                if(nameField.isEmpty()) {
                    Notification.show("Заповніть поле", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                coach.setCoachName(nameField.getValue());
                coachService.saveCoach(coach);
                refreshItems();
                editDialog.close();
            });
            Button cancelButton = new Button(VaadinIcon.CLOSE_CIRCLE.create(), e -> editDialog.close());

            HorizontalLayout editCoach = new HorizontalLayout(nameField, saveButton,cancelButton);
            editCoach.setAlignItems(Alignment.BASELINE);
            editDialog.add(editCoach);
        } else if (entity instanceof Award award) {
            TextField athletePlaceField = new TextField("Місце");
            athletePlaceField.setValue(String.valueOf(award.getAthletePlace()));

            TextField awardTypeField = new TextField("Тип нагороди");
            awardTypeField.setValue(award.getAwardType());

            ComboBox<Athlete> athleteComboBox = new ComboBox<>("Спортсмен");
            athleteComboBox.setItems(athleteService.getAllAthletes());
            athleteComboBox.setItemLabelGenerator(Athlete::getAthleteName);
            athleteComboBox.setValue(award.getAthlete());

            ComboBox<Competition> competitionComboBox = new ComboBox<>("Змагання");
            competitionComboBox.setItems(competitionService.getAllCompetitions());
            competitionComboBox.setItemLabelGenerator(Competition::getCompetitionName);
            competitionComboBox.setValue(award.getCompetition());

            if (userRole.equalsIgnoreCase("Оператор")) {
                awardTypeField.setEnabled(true);
                athleteComboBox.setEnabled(false);
                competitionComboBox.setEnabled(false);
            }

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {

                if(athletePlaceField.isEmpty() || awardTypeField.isEmpty() || athleteComboBox.isEmpty() || competitionComboBox.isEmpty()) {
                    Notification.show("Заповніть всі поля", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                award.setAthletePlace(Integer.parseInt(athletePlaceField.getValue()));
                if (!userRole.equalsIgnoreCase("Оператор")) {
                    award.setAwardType(awardTypeField.getValue());
                    award.setAthlete(athleteComboBox.getValue());
                    award.setCompetition(competitionComboBox.getValue());
                }

                awardService.saveAward(award);
                refreshItems();
                editDialog.close();
            });

            Button cancelButton = new Button(VaadinIcon.CLOSE_CIRCLE.create(), e -> editDialog.close());

            HorizontalLayout buttons = new HorizontalLayout(cancelButton, saveButton);

            HorizontalLayout editAwardLayout = new HorizontalLayout(
                    athletePlaceField, awardTypeField, athleteComboBox, competitionComboBox, buttons
            );
            editAwardLayout.setAlignItems(Alignment.BASELINE);

            editDialog.add(editAwardLayout);
        } else if (entity instanceof Organizer organizer) {
            nameField.setValue(organizer.getOrganizerName());

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {

                if(nameField.isEmpty()) {
                    Notification.show("Заповніть поле", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                organizer.setOrganizerName(nameField.getValue());
                organizerService.saveOrganizer(organizer);
                refreshItems();
                editDialog.close();
            });
            Button cancelButton = new Button(VaadinIcon.CLOSE_CIRCLE.create(), e -> editDialog.close());

            HorizontalLayout editOrganizer = new HorizontalLayout(nameField, saveButton, cancelButton);
            editOrganizer.setAlignItems(Alignment.BASELINE);
            editDialog.add(editOrganizer);
        } else if (entity instanceof SportClub sportClub) {
            nameField.setValue(sportClub.getSportClubName());

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {

                if(nameField.isEmpty()) {
                    Notification.show("Заповніть поле", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                sportClub.setSportClubName(nameField.getValue());
                sportClubService.saveSportClub(sportClub);
                refreshItems();
                editDialog.close();
            });
            Button cancelButton = new Button(VaadinIcon.CLOSE_CIRCLE.create(), e -> editDialog.close());

            HorizontalLayout editOrganizer = new HorizontalLayout(nameField, saveButton, cancelButton);
            editOrganizer.setAlignItems(Alignment.BASELINE);
            editDialog.add(editOrganizer);
        } else if (entity instanceof SportType sportType) {
            nameField.setValue(sportType.getSportName());

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {

                if(nameField.isEmpty()) {
                    Notification.show("Заповніть поле", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                sportType.setSportName(nameField.getValue());
                sportTypeService.saveSportType(sportType);
                refreshItems();
                editDialog.close();
            });
            Button cancelButton = new Button(VaadinIcon.CLOSE_CIRCLE.create(), e -> editDialog.close());

            HorizontalLayout editSportType = new HorizontalLayout(nameField, saveButton, cancelButton);
            editSportType.setAlignItems(Alignment.BASELINE);
            editDialog.add(editSportType);
        } if (entity instanceof Competition competition) {

            nameField.setValue(competition.getCompetitionName());
            DatePicker datePicker = new DatePicker("Дата");
            datePicker.setValue(competition.getCompetitionDate().toLocalDate());

            ComboBox<Organizer> organizerComboBox = new ComboBox<>("Організатор");
            organizerComboBox.setItems(organizerService.getAllOrganizers());
            organizerComboBox.setItemLabelGenerator(Organizer::getOrganizerName);
            organizerComboBox.setValue(competition.getOrganizer());

            ComboBox<SportFacility> sportFacilityComboBox = new ComboBox<>("Спортивна споруда");
            sportFacilityComboBox.setItems(sportFacilityService.getAllSportFacilities());
            sportFacilityComboBox.setItemLabelGenerator(SportFacility::getFacilityName);
            sportFacilityComboBox.setValue(competition.getSportFacility());

            ComboBox<SportType> sportTypeComboBox = new ComboBox<>("Вид спорту");
            sportTypeComboBox.setItems(sportTypeService.getAllSportTypes());
            sportTypeComboBox.setItemLabelGenerator(SportType::getSportName);
            sportTypeComboBox.setValue(competition.getSportType());

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {

                if(nameField.isEmpty() || datePicker.isEmpty() || organizerComboBox.isEmpty() || sportFacilityComboBox.isEmpty() || sportTypeComboBox.isEmpty()) {
                    Notification.show("Заповніть всі поля", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                competition.setCompetitionName(nameField.getValue());
                competition.setCompetitionDate(Date.valueOf(datePicker.getValue()));
                competition.setOrganizer(organizerComboBox.getValue());
                competition.setSportFacility(sportFacilityComboBox.getValue());
                competition.setSportType(sportTypeComboBox.getValue());

                competitionService.saveCompetition(competition);
                refreshItems();
                editDialog.close();
            });

            Button cancelButton = new Button(VaadinIcon.CLOSE_CIRCLE.create(), e -> editDialog.close());

            HorizontalLayout editCompetition = new HorizontalLayout(nameField, datePicker, organizerComboBox, sportFacilityComboBox, sportTypeComboBox, saveButton, cancelButton);
            editCompetition.setAlignItems(Alignment.BASELINE);
            editDialog.add(editCompetition);


        } if (entity instanceof CompetitionParticipant participant) {
            ComboBox<Athlete> athleteComboBox = new ComboBox<>("Атлет");
            athleteComboBox.setItems(athleteService.getAllAthletes());
            athleteComboBox.setItemLabelGenerator(Athlete::getAthleteName);
            athleteComboBox.setValue(participant.getAthlete());

            ComboBox<Competition> competitionComboBox = new ComboBox<>("Змагання");
            competitionComboBox.setItems(competitionService.getAllCompetitions());
            competitionComboBox.setItemLabelGenerator(Competition::getCompetitionName);
            competitionComboBox.setValue(participant.getCompetition());

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {

                if(athleteComboBox.isEmpty() || competitionComboBox.isEmpty()) {
                    Notification.show("Заповніть всі поля", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                CompetitionParticipant oldParticipant = new CompetitionParticipant(participant.getCompetition(), participant.getAthlete());
                participant.setAthlete(athleteComboBox.getValue());
                participant.setCompetition(competitionComboBox.getValue());

                if (competitionParticipantService.exists(participant)) {
                    Notification notification = new Notification(
                            "Даний атлет уже змагається в цьому змаганні", 1000, Notification.Position.TOP_CENTER);
                    notification.open();
                    return;
                }

                competitionParticipantService.editCompetitionParticipant(oldParticipant, participant);
                refreshItems();
                editDialog.close();
            });
            Button cancelButton = new Button(VaadinIcon.CLOSE_CIRCLE.create(), e -> editDialog.close());

            HorizontalLayout editParticipant = new HorizontalLayout(athleteComboBox, competitionComboBox, saveButton, cancelButton);
            editParticipant.setAlignItems(Alignment.BASELINE);
            editDialog.add(editParticipant);
        } if (entity instanceof SportFacility facility) {
            nameField.setValue(facility.getFacilityName());

            TextField addressField = new TextField("Адреса");
            addressField.setValue(facility.getFacilityAddress());

            TextField dimensionsField = new TextField("Розміри");
            dimensionsField.setValue(facility.getFacilityDimensions());

            ComboBox<String> typeComboBox = new ComboBox<>("Тип споруди");
            typeComboBox.setItems("Стадіон", "Корт", "Зал", "Манеж");
            typeComboBox.setValue(facility.getFacilityType());

            TextField stadiumCapacityField = new TextField("Місткість стадіону");
            stadiumCapacityField.setVisible(false);

            TextField cortCoatingField = new TextField("Покриття корту");
            cortCoatingField.setVisible(false);

            TextField gymEquipmentField = new TextField("Обладнання залу");
            gymEquipmentField.setVisible(false);

            TextField playpenCcsField = new TextField("Клімат контроль манежу");
            playpenCcsField.setVisible(false);

            typeComboBox.addValueChangeListener(event -> {
                String selectedType = event.getValue();
                stadiumCapacityField.setVisible("Стадіон".equals(selectedType));
                cortCoatingField.setVisible("Корт".equals(selectedType));
                gymEquipmentField.setVisible("Зал".equals(selectedType));
                playpenCcsField.setVisible("Манеж".equals(selectedType));
            });

            if ("Стадіон".equals(facility.getFacilityType())) {
                stadiumCapacityField.setVisible(true);
                stadiumCapacityField.setValue(facility.getStadiumCapacity() != null ? facility.getStadiumCapacity().toString() : "");
            } else if ("Корт".equals(facility.getFacilityType())) {
                cortCoatingField.setVisible(true);
                cortCoatingField.setValue(facility.getCortCoating() != null ? facility.getCortCoating() : "");
            } else if ("Зал".equals(facility.getFacilityType())) {
                gymEquipmentField.setVisible(true);
                gymEquipmentField.setValue(facility.getGymEquipment() != null ? facility.getGymEquipment() : "");
            } else if ("Манеж".equals(facility.getFacilityType())) {
                playpenCcsField.setVisible(true);
                playpenCcsField.setValue(facility.getPlaypenCcs() != null ? facility.getPlaypenCcs() : "");
            }

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {

                if (nameField.isEmpty() || addressField.isEmpty() || dimensionsField.isEmpty() || typeComboBox.isEmpty() ||
                        ("Стадіон".equals(typeComboBox.getValue()) && stadiumCapacityField.isEmpty()) ||
                        ("Корт".equals(typeComboBox.getValue()) && cortCoatingField.isEmpty()) ||
                        ("Зал".equals(typeComboBox.getValue()) && gymEquipmentField.isEmpty()) ||
                        ("Манеж".equals(typeComboBox.getValue()) && playpenCcsField.isEmpty())) {

                    Notification.show("Заповніть всі поля", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                facility.setFacilityName(nameField.getValue());
                facility.setFacilityAddress(addressField.getValue());
                facility.setFacilityDimensions(dimensionsField.getValue());
                facility.setFacilityType(typeComboBox.getValue());

                facility.setStadiumCapacity(null);
                facility.setCortCoating(null);
                facility.setGymEquipment(null);
                facility.setPlaypenCcs(null);

                if ("Стадіон".equals(typeComboBox.getValue())) {
                    facility.setStadiumCapacity(Integer.parseInt(stadiumCapacityField.getValue()));
                } else if ("Корт".equals(typeComboBox.getValue())) {
                    facility.setCortCoating(cortCoatingField.getValue());
                } else if ("Зал".equals(typeComboBox.getValue())) {
                    facility.setGymEquipment(gymEquipmentField.getValue());
                } else if ("Манеж".equals(typeComboBox.getValue())) {
                    facility.setPlaypenCcs(playpenCcsField.getValue());
                }

                sportFacilityService.saveSportFacility(facility);
                refreshItems();
                editDialog.close();
            });
            Button cancelButton = new Button(VaadinIcon.CLOSE_CIRCLE.create(), e -> editDialog.close());

            HorizontalLayout editLayout = new HorizontalLayout(
                    nameField, addressField, dimensionsField, typeComboBox,
                    stadiumCapacityField, cortCoatingField, gymEquipmentField,
                    playpenCcsField, saveButton, cancelButton);
            editLayout.setAlignItems(Alignment.BASELINE);

            editDialog.add(editLayout);


        } if (entity instanceof AthleteCoach athleteCoach) {
            ComboBox<Athlete> athleteComboBox = new ComboBox<>("Атлет");
            athleteComboBox.setItems(athleteService.getAllAthletes());
            athleteComboBox.setItemLabelGenerator(Athlete::getAthleteName);
            athleteComboBox.setValue(athleteCoach.getAthlete());

            ComboBox<Coach> coachComboBox = new ComboBox<>("Тренер");
            coachComboBox.setItems(coachService.getAllCoaches());
            coachComboBox.setItemLabelGenerator(Coach::getCoachName);
            coachComboBox.setValue(athleteCoach.getCoach());

            ComboBox<SportType> sportTypeComboBox = new ComboBox<>("Вид спорту");
            sportTypeComboBox.setItems(sportTypeService.getAllSportTypes());
            sportTypeComboBox.setItemLabelGenerator(SportType::getSportName);
            sportTypeComboBox.setValue(athleteCoach.getSportType());

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {

                if(athleteComboBox.isEmpty() || coachComboBox.isEmpty() || sportTypeComboBox.isEmpty()) {
                    Notification.show("Заповніть всі поля", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                AthleteCoach oldAthleteCoach = new AthleteCoach(athleteCoach.getAthlete(), athleteCoach.getCoach(), athleteCoach.getSportType());
                athleteCoach.setAthlete(athleteComboBox.getValue());
                athleteCoach.setCoach(coachComboBox.getValue());
                athleteCoach.setSportType(sportTypeComboBox.getValue());

                if (athleteCoachService.exists(athleteCoach)) {
                    Notification notification = new Notification(
                            "Даний атлет уже займається у цього тренера", 1000, Notification.Position.TOP_CENTER);
                    notification.open();
                    return;
                }

                athleteCoachService.editAthleteCoach(oldAthleteCoach, athleteCoach);
                refreshItems();
                editDialog.close();
            });

            Button cancelButton = new Button(VaadinIcon.CLOSE_CIRCLE.create(), e -> editDialog.close());

            HorizontalLayout editAthleteCoach = new HorizontalLayout(athleteComboBox, coachComboBox, sportTypeComboBox, saveButton, cancelButton);
            editAthleteCoach.setAlignItems(Alignment.BASELINE);
            editDialog.add(editAthleteCoach);
        } if (entity instanceof AthleteSportType athleteSportType) {
            ComboBox<Athlete> athleteComboBox = new ComboBox<>("Атлет");
            athleteComboBox.setItems(athleteService.getAllAthletes());
            athleteComboBox.setItemLabelGenerator(Athlete::getAthleteName);
            athleteComboBox.setValue(athleteSportType.getAthlete());

            ComboBox<SportType> sportTypeComboBox = new ComboBox<>("Тип спорту");
            sportTypeComboBox.setItems(sportTypeService.getAllSportTypes());
            sportTypeComboBox.setItemLabelGenerator(SportType::getSportName);
            sportTypeComboBox.setValue(athleteSportType.getSportType());

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {

                if(athleteComboBox.isEmpty() || sportTypeComboBox.isEmpty()) {
                    Notification.show("Заповніть всі поля", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                AthleteSportType oldAthleteSportType = new AthleteSportType(athleteSportType.getAthlete(), athleteSportType.getSportType());
                athleteSportType.setAthlete(athleteComboBox.getValue());
                athleteSportType.setSportType(sportTypeComboBox.getValue());

                if (athleteSportTypeService.exists(athleteSportType)) {
                    Notification notification = new Notification(
                            "Даний атлет уже займається даним видом спорту", 1000, Notification.Position.TOP_CENTER);
                    notification.open();
                    return;
                }

                athleteSportTypeService.editAthleteSportType(oldAthleteSportType, athleteSportType);
                refreshItems();
                editDialog.close();
            });
            Button cancelButton = new Button(VaadinIcon.CLOSE_CIRCLE.create(), e -> editDialog.close());

            HorizontalLayout editAthleteSportType = new HorizontalLayout(athleteComboBox, sportTypeComboBox, saveButton, cancelButton);
            editAthleteSportType.setAlignItems(Alignment.BASELINE);
            editDialog.add(editAthleteSportType);
        }
        editDialog.open();
    }

    private void refreshItems() {
        if (entityClass.equals(Athlete.class)) {
            List<Athlete> athletes = athleteService.getAllAthletes();
            grid.setItems((List<T>) athletes);
        } else if (entityClass.equals(Coach.class)) {
            List<Coach> coaches = coachService.getAllCoaches();
            grid.setItems((List<T>) coaches);
        } else if (entityClass.equals(Award.class)) {
            List<Award> awards = awardService.getAllAwards();
            grid.setItems((List<T>) awards);
        } else if (entityClass.equals(Organizer.class)) {
            List<Organizer> organizers = organizerService.getAllOrganizers();
            grid.setItems((List<T>) organizers);
        } else if (entityClass.equals(SportClub.class)) {
            List<SportClub> sportClubs = sportClubService.getAllSportClubs();
            grid.setItems((List<T>) sportClubs);
        } else if (entityClass.equals(SportType.class)) {
            List<SportType> sportTypes = sportTypeService.getAllSportTypes();
            grid.setItems((List<T>) sportTypes);
        } else if (entityClass.equals(Competition.class)) {
            List<Competition> competitions = competitionService.getAllCompetitions();
            grid.setItems((List<T>) competitions);
        } else if (entityClass.equals(CompetitionParticipant.class)) {
            List<CompetitionParticipant> participants = competitionParticipantService.getAllCompetitionParticipants();
            grid.setItems((List<T>) participants);
        } else if (entityClass.equals(SportFacility.class)) {
            List<SportFacility> facilities = sportFacilityService.getAllSportFacilities();
            grid.setItems((List<T>) facilities);
        } else if (entityClass.equals(AthleteCoach.class)) {
            List<AthleteCoach> athleteCoaches = athleteCoachService.getAllAthletesCoaches();
            grid.setItems((List<T>) athleteCoaches);
        } else if (entityClass.equals(AthleteSportType.class)) {
            List<AthleteSportType> athleteSportType = athleteSportTypeService.getAllAthletesSportTypes();
            grid.setItems((List<T>) athleteSportType);
        }
    }


    private void openAddDialog() {
        Dialog addDialog = new Dialog();

        TextField nameField = new TextField("Ім'я");

        if (entityClass.equals(Athlete.class)) {
            TextField rankField = new TextField("Розряд");

            ComboBox<SportClub> sportClubComboBox = new ComboBox<>("Спортивний клуб");
            sportClubComboBox.setItems(sportClubService.getAllSportClubs());
            sportClubComboBox.setItemLabelGenerator(SportClub::getSportClubName);

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
                if (nameField.isEmpty()) {
                    Notification.show("Введіть ім'я спортсмена", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (rankField.isEmpty()) {
                    Notification.show("Введіть розряд спортсмена", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (sportClubComboBox.isEmpty()) {
                    Notification.show("Виберіть спортивний клуб", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                Athlete newAthlete = new Athlete();
                newAthlete.setAthleteName(nameField.getValue());
                newAthlete.setAthleteRank(rankField.getValue());
                newAthlete.setSportClub(sportClubComboBox.getValue());

                athleteService.saveAthlete(newAthlete);
                refreshItems();
                addDialog.close();
            });

            HorizontalLayout addAthlete = new HorizontalLayout(nameField, rankField, sportClubComboBox, saveButton);
            addAthlete.setAlignItems(Alignment.BASELINE);
            addDialog.add(addAthlete);

        } else if (entityClass.equals(Coach.class)) {
            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {

                if (nameField.isEmpty()) {
                    Notification.show("Введіть ім'я тренера", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                Coach newCoach = new Coach();
                newCoach.setCoachName(nameField.getValue());

                coachService.saveCoach(newCoach);
                refreshItems();
                addDialog.close();
            });

            HorizontalLayout addCoach = new HorizontalLayout(nameField, saveButton);
            addCoach.setAlignItems(Alignment.BASELINE);
            addDialog.add(addCoach);
        } else if (entityClass.equals(Award.class)) {
            TextField athletePlaceField = new TextField("Місце");
            TextField awardTypeField = new TextField("Тип нагороди");

            ComboBox<Athlete> athleteComboBox = new ComboBox<>("Спортсмен");
            athleteComboBox.setItems(athleteService.getAllAthletes());
            athleteComboBox.setItemLabelGenerator(Athlete::getAthleteName);

            ComboBox<Competition> competitionComboBox = new ComboBox<>("Змагання");
            competitionComboBox.setItems(competitionService.getAllCompetitions());
            competitionComboBox.setItemLabelGenerator(Competition::getCompetitionName);

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
                if (athletePlaceField.isEmpty()) {
                    Notification.show("Введіть місце", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                try {
                    int place = Integer.parseInt(athletePlaceField.getValue().trim());
                    if (place <= 0) {
                        Notification.show("Місце має бути додатнім числом", 3000, Notification.Position.TOP_CENTER);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    Notification.show("Місце має бути числом", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (awardTypeField.isEmpty()) {
                    Notification.show("Введіть тип нагороди", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (athleteComboBox.isEmpty()) {
                    Notification.show("Виберіть спортсмена", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (competitionComboBox.isEmpty()) {
                    Notification.show("Виберіть змагання", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                Award newAward = new Award();
                newAward.setAthletePlace(Integer.parseInt(athletePlaceField.getValue()));
                newAward.setAwardType(awardTypeField.getValue());
                newAward.setAthlete(athleteComboBox.getValue());
                newAward.setCompetition(competitionComboBox.getValue());

                awardService.saveAward(newAward);
                refreshItems();
                addDialog.close();
            });

            HorizontalLayout addAward = new HorizontalLayout(athletePlaceField, awardTypeField, athleteComboBox, competitionComboBox, saveButton);
            addAward.setAlignItems(Alignment.BASELINE);
            addDialog.add(addAward);
        } else if (entityClass.equals(Organizer.class)) {
            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
                if (nameField.isEmpty()) {
                    Notification.show("Введіть назву організатора", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                Organizer newOrganizer = new Organizer();
                newOrganizer.setOrganizerName(nameField.getValue());

                organizerService.saveOrganizer(newOrganizer);
                refreshItems();
                addDialog.close();
            });

            HorizontalLayout addOrganizer = new HorizontalLayout(nameField, saveButton);
            addOrganizer.setAlignItems(Alignment.BASELINE);
            addDialog.add(addOrganizer);
        } else if (entityClass.equals(SportClub.class)) {
            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
                if (nameField.isEmpty()) {
                    Notification.show("Введіть назву спортивного клубу", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                SportClub newSportClub = new SportClub();
                newSportClub.setSportClubName(nameField.getValue());

                sportClubService.saveSportClub(newSportClub);
                refreshItems();
                addDialog.close();
            });

            HorizontalLayout addSportClub = new HorizontalLayout(nameField, saveButton);
            addSportClub.setAlignItems(Alignment.BASELINE);
            addDialog.add(addSportClub);

        } else if (entityClass.equals(SportType.class)) {
            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
                if (nameField.isEmpty()) {
                    Notification.show("Введіть назву виду спорту", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                SportType newSportType = new SportType();
                newSportType.setSportName(nameField.getValue());

                sportTypeService.saveSportType(newSportType);
                refreshItems();
                addDialog.close();
            });

            HorizontalLayout addSportType = new HorizontalLayout(nameField, saveButton);
            addSportType.setAlignItems(Alignment.BASELINE);
            addDialog.add(addSportType);

        }  else if (entityClass.equals(Competition.class)) {
            DatePicker datePicker = new DatePicker("Дата");
            ComboBox<Organizer> organizerComboBox = new ComboBox<>("Організатор");
            organizerComboBox.setItems(organizerService.getAllOrganizers());
            organizerComboBox.setItemLabelGenerator(Organizer::getOrganizerName);
            ComboBox<SportFacility> sportFacilityComboBox = new ComboBox<>("Спортивна споруда");
            sportFacilityComboBox.setItems(sportFacilityService.getAllSportFacilities());
            sportFacilityComboBox.setItemLabelGenerator(SportFacility::getFacilityName);

            ComboBox<SportType> sportTypeComboBox = new ComboBox<>("Вид спорту");
            sportTypeComboBox.setItems(sportTypeService.getAllSportTypes());
            sportTypeComboBox.setItemLabelGenerator(SportType::getSportName);

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
                if (nameField.isEmpty()) {
                    Notification.show("Введіть назву змагання", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (datePicker.isEmpty()) {
                    Notification.show("Виберіть дату змагання", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (organizerComboBox.isEmpty()) {
                    Notification.show("Виберіть організатора", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (sportFacilityComboBox.isEmpty()) {
                    Notification.show("Виберіть спортивну споруду", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (sportTypeComboBox.isEmpty()) {
                    Notification.show("Виберіть вид спорту", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                Competition newCompetition = new Competition();
                newCompetition.setCompetitionName(nameField.getValue());
                newCompetition.setCompetitionDate(Date.valueOf(datePicker.getValue()));
                newCompetition.setOrganizer(organizerComboBox.getValue());
                newCompetition.setSportFacility(sportFacilityComboBox.getValue());
                newCompetition.setSportType(sportTypeComboBox.getValue());

                competitionService.saveCompetition(newCompetition);
                refreshItems();
                addDialog.close();
            });

            HorizontalLayout addCompetition = new HorizontalLayout(nameField, datePicker, organizerComboBox, sportFacilityComboBox, sportTypeComboBox, saveButton);
            addCompetition.setAlignItems(Alignment.BASELINE);
            addDialog.add(addCompetition);
        } else if (entityClass.equals(CompetitionParticipant.class)) {
            ComboBox<Athlete> athleteComboBox = new ComboBox<>("Спортсмен");
            athleteComboBox.setItems(athleteService.getAllAthletes());
            athleteComboBox.setItemLabelGenerator(Athlete::getAthleteName);

            ComboBox<Competition> competitionComboBox = new ComboBox<>("Змагання");
            competitionComboBox.setItems(competitionService.getAllCompetitions());
            competitionComboBox.setItemLabelGenerator(Competition::getCompetitionName);

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
                if (athleteComboBox.isEmpty()) {
                    Notification.show("Виберіть спортсмена", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (competitionComboBox.isEmpty()) {
                    Notification.show("Виберіть змагання", 3000, Notification.Position.TOP_CENTER);
                    return;
                }


                CompetitionParticipant newParticipant = new CompetitionParticipant();
                newParticipant.setAthlete(athleteComboBox.getValue());
                newParticipant.setCompetition(competitionComboBox.getValue());

                competitionParticipantService.saveCompetitionParticipant(newParticipant);
                refreshItems();
                addDialog.close();
            });

            HorizontalLayout addParticipant = new HorizontalLayout(athleteComboBox, competitionComboBox, saveButton);
            addParticipant.setAlignItems(Alignment.BASELINE);
            addDialog.add(addParticipant);
        } else if (entityClass.equals(SportFacility.class)) {
            TextField addressField = new TextField("Адреса");
            addressField.setPlaceholder("вул. Українська 14");
            TextField dimensionsField = new TextField("Розміри");
            dimensionsField.setPlaceholder("160x100");

            ComboBox<String> typeComboBox = new ComboBox<>("Тип споруди");
            typeComboBox.setItems("Стадіон", "Корт", "Зал", "Манеж");

            TextField stadiumCapacityField = new TextField("Місткість стадіону");
            stadiumCapacityField.setVisible(false);


            TextField cortCoatingField = new TextField("Покриття корту");
            cortCoatingField.setVisible(false);

            TextField gymEquipmentField = new TextField("Обладнання залу");
            gymEquipmentField.setVisible(false);

            TextField playpenCcsField = new TextField("Клімат контроль манежу");
            playpenCcsField.setVisible(false);

            typeComboBox.addValueChangeListener(event -> {
                String selectedType = event.getValue();
                stadiumCapacityField.setVisible("Стадіон".equals(selectedType));
                cortCoatingField.setVisible("Корт".equals(selectedType));
                gymEquipmentField.setVisible("Зал".equals(selectedType));
                playpenCcsField.setVisible("Манеж".equals(selectedType));
            });

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
                if (nameField.isEmpty()) {
                    Notification.show("Введіть назву спортивної споруди", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (addressField.isEmpty()) {
                    Notification.show("Введіть адресу", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (dimensionsField.isEmpty()) {
                    Notification.show("Введіть розміри", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (typeComboBox.isEmpty()) {
                    Notification.show("Виберіть тип споруди", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                String selectedType = typeComboBox.getValue();
                if ("Стадіон".equals(selectedType) && stadiumCapacityField.isEmpty()) {
                    Notification.show("Введіть місткість стадіону", 3000, Notification.Position.TOP_CENTER);
                    return;
                } else if ("Стадіон".equals(selectedType)) {
                    try {
                        int capacity = Integer.parseInt(stadiumCapacityField.getValue().trim());
                        if (capacity <= 0) {
                            Notification.show("Місткість стадіону має бути додатнім числом", 3000, Notification.Position.TOP_CENTER);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        Notification.show("Місткість стадіону має бути числом", 3000, Notification.Position.TOP_CENTER);
                        return;
                    }
                } else if ("Корт".equals(selectedType) && cortCoatingField.isEmpty()) {
                    Notification.show("Введіть тип покриття корту", 3000, Notification.Position.TOP_CENTER);
                    return;
                } else if ("Зал".equals(selectedType) && gymEquipmentField.isEmpty()) {
                    Notification.show("Введіть обладнання залу", 3000, Notification.Position.TOP_CENTER);
                    return;
                } else if ("Манеж".equals(selectedType) && playpenCcsField.isEmpty()) {
                    Notification.show("Введіть інформацію про клімат контроль", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                SportFacility newFacility = new SportFacility();
                newFacility.setFacilityName(nameField.getValue());
                newFacility.setFacilityAddress(addressField.getValue());
                newFacility.setFacilityDimensions(dimensionsField.getValue());
                newFacility.setFacilityType(typeComboBox.getValue());

                if ("Стадіон".equals(typeComboBox.getValue())) {
                    newFacility.setStadiumCapacity(Integer.parseInt(stadiumCapacityField.getValue()));
                } else if ("Корт".equals(typeComboBox.getValue())) {
                    newFacility.setCortCoating(cortCoatingField.getValue());
                } else if ("Зал".equals(typeComboBox.getValue())) {
                    newFacility.setGymEquipment(gymEquipmentField.getValue());
                } else if ("Манеж".equals(typeComboBox.getValue())) {
                    newFacility.setPlaypenCcs(playpenCcsField.getValue());
                }

                sportFacilityService.saveSportFacility(newFacility);
                refreshItems();
                addDialog.close();
            });

            HorizontalLayout addLayout = new HorizontalLayout(
                    nameField, addressField, dimensionsField, typeComboBox,
                    stadiumCapacityField, cortCoatingField, gymEquipmentField, playpenCcsField, saveButton
            );
            addLayout.setAlignItems(Alignment.BASELINE);

            addDialog.add(addLayout);
        } else if (entityClass.equals(AthleteCoach.class)) {
            ComboBox<Athlete> athleteComboBox = new ComboBox<>("Атлет");
            athleteComboBox.setItems(athleteService.getAllAthletes());
            athleteComboBox.setItemLabelGenerator(Athlete::getAthleteName);

            ComboBox<Coach> coachComboBox = new ComboBox<>("Тренер");
            coachComboBox.setItems(coachService.getAllCoaches());
            coachComboBox.setItemLabelGenerator(Coach::getCoachName);

            ComboBox<SportType> sportTypeComboBox = new ComboBox<>("Вид спорту");
            sportTypeComboBox.setItems(sportTypeService.getAllSportTypes());
            sportTypeComboBox.setItemLabelGenerator(SportType::getSportName);

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
                if (athleteComboBox.isEmpty()) {
                    Notification.show("Виберіть атлета", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (coachComboBox.isEmpty()) {
                    Notification.show("Виберіть тренера", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (sportTypeComboBox.isEmpty()) {
                    Notification.show("Виберіть вид спорту", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                AthleteCoach newAthleteCoach = new AthleteCoach();
                newAthleteCoach.setAthlete(athleteComboBox.getValue());
                newAthleteCoach.setCoach(coachComboBox.getValue());
                newAthleteCoach.setSportType(sportTypeComboBox.getValue());

                athleteCoachService.saveAthleteCoach(newAthleteCoach);
                refreshItems();
                addDialog.close();
            });

            HorizontalLayout addAthleteCoach = new HorizontalLayout(athleteComboBox, coachComboBox, sportTypeComboBox, saveButton);
            addAthleteCoach.setAlignItems(Alignment.BASELINE);
            addDialog.add(addAthleteCoach);
        }  else if (entityClass.equals(AthleteSportType.class)) {
            ComboBox<Athlete> athleteComboBox = new ComboBox<>("Спортсмен");
            athleteComboBox.setItems(athleteService.getAllAthletes());
            athleteComboBox.setItemLabelGenerator(Athlete::getAthleteName);

            ComboBox<SportType> sportTypeComboBox = new ComboBox<>("Вид спорту");
            sportTypeComboBox.setItems(sportTypeService.getAllSportTypes());
            sportTypeComboBox.setItemLabelGenerator(SportType::getSportName);

            Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
                if (athleteComboBox.isEmpty()) {
                    Notification.show("Виберіть спортсмена", 3000, Notification.Position.TOP_CENTER);
                    return;
                }
                if (sportTypeComboBox.isEmpty()) {
                    Notification.show("Виберіть вид спорту", 3000, Notification.Position.TOP_CENTER);
                    return;
                }

                AthleteSportType newAthleteSportType = new AthleteSportType();
                newAthleteSportType.setAthlete(athleteComboBox.getValue());
                newAthleteSportType.setSportType(sportTypeComboBox.getValue());

                athleteSportTypeService.saveAthleteSportType(newAthleteSportType);
                refreshItems();
                addDialog.close();
            });

            HorizontalLayout addAthleteSportType = new HorizontalLayout(athleteComboBox, sportTypeComboBox, saveButton);
            addAthleteSportType.setAlignItems(Alignment.BASELINE);
            addDialog.add(addAthleteSportType);

        }

        addDialog.open();
    }
}
