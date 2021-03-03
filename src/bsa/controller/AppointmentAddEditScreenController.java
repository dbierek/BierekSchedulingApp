/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsa.controller;

import bsa.dao.EntityDAOImpl;
import bsa.model.Appointment;
import bsa.model.Entity;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 *
 * @author dbierek
 */
public class AppointmentAddEditScreenController extends EntityAddEditScreenController {

   public <T extends Entity> AppointmentAddEditScreenController(Stage owner, Class clazz, T entity) {
      super(owner, clazz, entity);
      setActionHandler();
   }

   AppointmentAddEditScreenController(Stage owner, Class clazz) {
      super(owner, clazz);
      setActionHandler();
   }

   private void setActionHandler() {
      actButton.setOnAction((ActionEvent e) -> { // Lambda expression used to easily create an EventHandler
         Appointment appointment = (Appointment) entityPropertiesPaneController.getEntity();
         List<Appointment> appointments = EntityDAOImpl.findOverlappingAppointments(appointment);
         if (appointments.isEmpty()) {
            if (addEdit == EntityAddEditScreenController.AddEdit.ADD) {
               EntityDAOImpl.createEntity(appointment);
            } else {
               EntityDAOImpl.updateEntity(appointment);
            }
            flowPane.fireEvent(new EntityEvent(addEdit.entityEventType));
         } else {
            PopupScreen appointmentOverlapsScreen
                    = new PopupScreen(
                            (Stage) entityPropertiesPaneController.flowPane.getScene().getWindow(),
                            "Appointment Not " + (addEdit == EntityAddEditScreenController.AddEdit.ADD ? "Added" : "Updated"),
                            "Appointment overlaps with other appointments for this User or Customer");
         }
      });
   }
}
