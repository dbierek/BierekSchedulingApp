package bsa.controller;

import bsa.dao.EntityDAOImpl;
import bsa.model.*;
import bsa.utility.AppointmentTypeNames;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dbierek
 */
public class AppointmentTablePaneController extends EntityTablePaneController {

   @FXML
   public ComboBox timeComboBox;

   @FXML
   public Button previousButton;

   @FXML
   public Button nextButton;

   public EntityComboBox userComboBox;

   public ComboBox<String> appointmentTypeComboBox;

   @FXML
   public FlowPane userFlowPane;

   @FXML
   public FlowPane appointmentTypeFlowPane;

   @FXML
   public FlowPane timeNavButtonPane;

   @FXML
   public Text datesText;

   @FXML
   public Text numAppointmentsText;

   private ZonedDateTime start = ZonedDateTime.now();

   private ZonedDateTime end = start.plusWeeks(1);

   private final String ALL_USERS = "All Users";
   private final String ALL_TYPES = "All Types";
   private final String ALL_TIME = "All Time", BY_WEEK = "By Week", BY_MONTH = "By Month";

   AppointmentTablePaneController() throws IOException {
      super(Appointment.class, "/bsa/view/AppointmentTablePane.fxml");
   }

   @Override
   public void initializeComponents() {
      super.initializeComponents();
      userComboBox = new EntityComboBox<>(User.class);
      userComboBox.getItems().add(0, new User(-1, ALL_USERS, "", 0));
      userComboBox.getSelectionModel().selectFirst();
      userComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
         refreshItems();
      });
      userFlowPane.getChildren().add(userComboBox);
      appointmentTypeComboBox = new ComboBox<>();
      appointmentTypeComboBox.setItems(FXCollections.observableArrayList(AppointmentTypeNames.appointmentTypeNames));
      appointmentTypeComboBox.getItems().add(0, ALL_TYPES);
      appointmentTypeComboBox.getSelectionModel().selectFirst();
      appointmentTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
         refreshItems();
      });
      appointmentTypeComboBox.setPrefWidth(100);
      appointmentTypeComboBox.setPrefHeight(30);
      appointmentTypeFlowPane.getChildren().add(appointmentTypeComboBox);
      timeNavButtonPane.setVisible(false);
      ObservableList<String> timeOptions = FXCollections.observableArrayList();
      timeOptions.addAll(ALL_TIME, BY_WEEK, BY_MONTH);
      timeComboBox.setItems(timeOptions);
      timeComboBox.getSelectionModel().selectFirst();
      timeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue.equals(ALL_TIME)) {
            timeNavButtonPane.setVisible(false);
         } else if (newValue.equals(BY_WEEK)) {
            timeNavButtonPane.setVisible(true);
            start = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS);
            end = start.plusWeeks(1);
            DateTimeFormatter weekFormat = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");
            datesText.setText("Week of " + weekFormat.format(start));
         } else if (newValue.equals(BY_MONTH)) {
            timeNavButtonPane.setVisible(true);
            start = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
            end = start.plusMonths(1);
            DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MMM yyyy");
            datesText.setText(monthFormat.format(start));
         }
         refreshItems();
      });
      previousButton.setOnAction((event) -> {
         if (timeComboBox.getSelectionModel().getSelectedItem().equals(BY_WEEK)) {
            start = start.minusWeeks(1);
            end = end.minusWeeks(1);
            DateTimeFormatter weekFormat = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");
            datesText.setText("Week of " + weekFormat.format(start));
         }
         if (timeComboBox.getSelectionModel().getSelectedItem().equals(BY_MONTH)) {
            end = end.minusMonths(1);
            start = start.minusMonths(1);
            DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MMM yyyy");
            datesText.setText(monthFormat.format(start));
         }
         refreshItems();
      });
      nextButton.setOnAction((event) -> {
         if (timeComboBox.getSelectionModel().getSelectedItem().equals(BY_WEEK)) {
            start = start.plusWeeks(1);
            end = end.plusWeeks(1);
            DateTimeFormatter weekFormat = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");
            datesText.setText("Week of " + weekFormat.format(start));
         }
         if (timeComboBox.getSelectionModel().getSelectedItem().equals(BY_MONTH)) {
            start = start.plusMonths(1);
            end = end.plusMonths(1);
            DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MMM yyyy");
            datesText.setText(monthFormat.format(start));
         }
         refreshItems();
      });
      addButton.setOnAction((e) -> { // Lambda expression used to easily create an EventHandler
         AppointmentAddEditScreenController addScreenController = new AppointmentAddEditScreenController((Stage) addButton.getScene().getWindow(), clazz);
         addScreenController.flowPane.addEventHandler(EntityEvent.ADD_EVENT_TYPE, (ev)
                 -> { // Lambda expression used to easily create an EventHandler
            refreshItems();
         });
      });

      editButton.setOnAction((e) -> { // Lambda expression used to easily create an EventHandler
         AppointmentAddEditScreenController editScreenController = new AppointmentAddEditScreenController((Stage) editButton.getScene().getWindow(), clazz, (Entity) table.getSelectionModel().getSelectedItem());
         editScreenController.flowPane.addEventHandler(EntityEvent.EDIT_EVENT_TYPE, (ev)
                 -> { // Lambda expression used to easily create an EventHandler
            refreshItems();
         });
      });

   }

   @Override
   public void refreshItems() {
      if (userComboBox == null || timeComboBox == null || appointmentTypeComboBox == null) {
         setEntities(EntityDAOImpl.getEntities(clazz));
         return;
      }
      User selectedUser = (User) userComboBox.getSelectionModel().getSelectedItem();
      String selectedAppointmentType = appointmentTypeComboBox.getSelectionModel().getSelectedItem();
      if (selectedUser.getId().equals(-1)) {
         if (timeComboBox.getSelectionModel().getSelectedItem().equals(ALL_TIME)) {
            if (selectedAppointmentType.equals(ALL_TYPES)) {
               setEntities(EntityDAOImpl.getEntities(clazz));
            } else {
               setEntities(EntityDAOImpl.getAppointments(selectedAppointmentType));
            }
         } else {
            if (selectedAppointmentType.equals(ALL_TYPES)) {
               setEntities(EntityDAOImpl.getAppointments(start, end));
            } else {
               setEntities(EntityDAOImpl.getAppointments(start, end, selectedAppointmentType));
            }
         }
      } else {
         if (timeComboBox.getSelectionModel().getSelectedItem().equals(ALL_TIME)) {
            if (selectedAppointmentType.equals(ALL_TYPES)) {
               setEntities(EntityDAOImpl.getAppointments(selectedUser.getId()));
            } else {
               setEntities(EntityDAOImpl.getAppointments(selectedUser.getId(), selectedAppointmentType));
            }
         } else {
            if (selectedAppointmentType.equals(ALL_TYPES)) {
               setEntities(EntityDAOImpl.getAppointments(selectedUser.getId(), start, end));
            } else {
               setEntities(EntityDAOImpl.getAppointments(selectedUser.getId(), start, end, selectedAppointmentType));
            }
         }
      }
      int numAppointments = table.getItems().size();
      numAppointmentsText.setText(numAppointments + " Appointment" + (numAppointments != 1 ? "s" : ""));
   }
}
