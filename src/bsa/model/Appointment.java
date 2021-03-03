package bsa.model;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dbierek
 */
public class Appointment extends Entity {

   private Integer appointmentId;
   private Integer customerId;
   private Integer userId;
   private String title;
   private String description;
   private String location;
   private String contact;
   private String type;
   private String url;
   private ZonedDateTime start;
   private ZonedDateTime end;

   public Appointment() {
   }

   public Appointment(Integer appointmentId, Integer customerId, Integer userId, String title, String description, String location, String contact, String type, String url, ZonedDateTime start, ZonedDateTime end) {
      this.appointmentId = appointmentId;
      this.customerId = customerId;
      this.userId = userId;
      this.title = title;
      this.description = description;
      this.location = location;
      this.contact = contact;
      this.type = type;
      this.url = url;
      this.start = start;
      this.end = end;
   }

   @Override
   public String toString() {
      return title;
   }

   @Override
   public Integer getId() {
      return appointmentId;
   }

   @Override
   public void setId(Integer id) {
      appointmentId = id;
   }
   
   public static Field getNameField() {
      try {
         return Appointment.class.getDeclaredField("title");
      } catch (NoSuchFieldException | SecurityException ex) {
         Logger.getLogger(Address.class.getName()).log(Level.SEVERE, "Error getting name field", ex);
         return null;
      }
   }

   public static Field getTypeField() {
      try {
         return Appointment.class.getDeclaredField("type");
      } catch (NoSuchFieldException | SecurityException ex) {
         Logger.getLogger(Address.class.getName()).log(Level.SEVERE, "Error getting name field", ex);
         return null;
      }
   }

   public Integer getAppointmentId() {
      return appointmentId;
   }

   public void setAppointmentId(Integer appointmentId) {
      this.appointmentId = appointmentId;
   }

   public Integer getCustomerId() {
      return customerId;
   }

   public void setCustomerId(Integer customerId) {
      this.customerId = customerId;
   }

   public Integer getUserId() {
      return userId;
   }

   public void setUserId(Integer userId) {
      this.userId = userId;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getLocation() {
      return location;
   }

   public void setLocation(String location) {
      this.location = location;
   }

   public String getContact() {
      return contact;
   }

   public void setContact(String contact) {
      this.contact = contact;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public ZonedDateTime getStart() {
      return start;
   }

   public void setStart(ZonedDateTime start) {
      this.start = start;
   }

   public ZonedDateTime getEnd() {
      return end;
   }

   public void setEnd(ZonedDateTime end) {
      this.end = end;
   }

}
