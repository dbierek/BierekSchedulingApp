package bsa.controller;

import bsa.dao.EntityDAOImpl;
import bsa.model.Appointment;
import bsa.model.Customer;
import bsa.model.User;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

/**
 *
 * @author dbierek
 */
public class AppointmentReminderScreenController {

   private final User loggedInUser;
   private final ObservableList<Appointment> upcomingAppointments;

   public AppointmentReminderScreenController(User loggedInUser) {
      this.loggedInUser = loggedInUser;
      this.upcomingAppointments = getUpcomingAppointments();
   }

   private ObservableList<Appointment> getUpcomingAppointments() {
      return EntityDAOImpl.getAppointments(loggedInUser.getUserId(),
              ZonedDateTime.now().withZoneSameInstant(ZoneId.of("GMT")),
              ZonedDateTime.now().plusMinutes(15).withZoneSameInstant(ZoneId.of("GMT")));
   }

   public void showAppointmentReminderPopup(Stage mainStage) {
      if (upcomingAppointments.size() > 0) {
         Appointment appointment = upcomingAppointments.get(0);
         Customer customer = EntityDAOImpl.getEntity(Customer.class, upcomingAppointments.get(0).getCustomerId());
         DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("h:mm a");

         String appointmentText = appointment.getType() + " Appointment\n"
                 + "with " + customer.getCustomerName() + "\n"
                 + "from " + dateFormat.format(appointment.getStart().withZoneSameInstant(ZoneId.systemDefault()))
                 + " to " + dateFormat.format(appointment.getEnd().withZoneSameInstant(ZoneId.systemDefault())) + "\n\n"
                 + appointment.getTitle() + ": \n"
                 + appointment.getDescription() + "\n\n"
                 + "Location: " + appointment.getLocation() + "\n"
                 + "Contact: " + appointment.getContact() + "\n"
                 + "URL: " + appointment.getUrl();
         PopupScreen appointmentReminderPopup = new PopupScreen(mainStage, "Upcoming Appointment", appointmentText, 350, 300);
      }
   }
}
