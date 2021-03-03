package bsa.controller;

import bsa.dao.EntityDAOImpl;
import bsa.model.*;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dbierek
 */
public class CustomerTablePaneController extends EntityTablePaneController {

   CustomerTablePaneController() throws IOException {
      super(Customer.class);
      deleteButton.setOnAction(deleteButtonActionHandler());
   }

   private EventHandler<ActionEvent> deleteButtonActionHandler() {
      return (e) -> { // Lambda expression used to easily create an EventHandler
         Customer toDelete = (Customer) table.getSelectionModel().getSelectedItem();
         int deletedAppointments = EntityDAOImpl.deleteEntitiesForTypeId(Appointment.class, Customer.class, toDelete.getId());
         if (deletedAppointments > 0) {
            PopupScreen appointmentsDeletedPopup = new PopupScreen((Stage) entityTablePane.getScene().getWindow(),
                    "Appointments Deleted",
                    deletedAppointments + " appointment"
                    + (deletedAppointments > 1 ? "s" : "")
                    + " for " + toDelete.toString() + " deleted.", 300, 120);
         }
         EntityDAOImpl.deleteEntity(toDelete);
         refreshItems();
      };
   }

}
