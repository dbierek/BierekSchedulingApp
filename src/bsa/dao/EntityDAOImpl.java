package bsa.dao;

import bsa.model.*;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author dbierek
 */
public class EntityDAOImpl {

   public static <E extends Entity> int deleteEntity(E entity) {
      try {
         int deleteCount = DBAccess.delete(entity);
         return deleteCount;
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Error deleting entity.", ex);
         return -1;
      }
   }

   public static <E extends Entity> int createEntity(E entity) {
      try {
         int createCount = DBAccess.create(entity);
         return createCount;
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Error creating entity.", ex);
         return -1;
      }
   }

   public static <E extends Entity> int updateEntity(E entity) {
      try {
         int updateCount = DBAccess.update(entity);
         return updateCount;
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Error updating entity.", ex);
         return -1;
      }
   }

   public static <E extends Entity> E getEntity(Class clazz, Integer id) {
      try {
         return (E) DBAccess.get(clazz.getSimpleName().toLowerCase(), id).get(0);
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Error getting entity.", ex);
         return null;
      } catch (IndexOutOfBoundsException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, clazz.getSimpleName() + " with id: " + id.toString() + " not found.", ex);
         return null;
      }
   }

   public static <E extends Entity> ObservableList<E> getEntities(Class clazz) {
      try {
         return FXCollections.observableList(DBAccess.get(clazz.getSimpleName().toLowerCase()));
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Error getting entities.");
         return null;
      }
   }

   public static <E extends Entity> ObservableList<E> getEntities(Class clazz, List<Integer> ids) {
      try {
         return FXCollections.observableList(DBAccess.get(clazz.getSimpleName().toLowerCase(), ids));
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Error getting entities.", ex);
         return null;
      }
   }

   public static <E extends Entity> ObservableList<E> getAppointments(Integer userId) {
      try {
         return FXCollections.observableList(DBAccess.getAppointments(userId));
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Error getting entities.");
         return null;
      }
   }

   public static <E extends Entity> ObservableList<E> getAppointments(Integer userId, String appointmentType) {
      try {
         return FXCollections.observableList(DBAccess.getAppointments(userId, appointmentType));
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Error getting entities.");
         return null;
      }
   }

   public static <E extends Entity> ObservableList<E> getAppointments(String appointmentType) {
      try {
         return FXCollections.observableList(DBAccess.getAppointments(appointmentType));
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Error getting entities.");
         return null;
      }
   }

   public static <E extends Entity> ObservableList<E> getAppointments(Integer userId, ZonedDateTime start, ZonedDateTime end) {
      try {
         return FXCollections.observableList(DBAccess.getAppointments(userId, start, end));
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Error getting entities.");
         return null;
      }
   }

   public static <E extends Entity> ObservableList<E> getAppointments(Integer userId, ZonedDateTime start, ZonedDateTime end, String appointmentType) {
      try {
         return FXCollections.observableList(DBAccess.getAppointments(userId, start, end, appointmentType));
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Error getting entities.");
         return null;
      }
   }

   public static <E extends Entity> ObservableList<E> getAppointments(ZonedDateTime start, ZonedDateTime end) {
      try {
         return FXCollections.observableList(DBAccess.getAppointments(start, end));
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Error getting entities.");
         return null;
      }
   }

   public static <E extends Entity> ObservableList<E> getAppointments(ZonedDateTime start, ZonedDateTime end, String appointmentType) {
      try {
         return FXCollections.observableList(DBAccess.getAppointments(start, end, appointmentType));
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Error getting entities.");
         return null;
      }
   }

   public static <E extends Entity> ObservableList<E> findOverlappingAppointments(Appointment appointment) {
      try {
         return FXCollections.observableList(DBAccess.findOverlappingAppointments(appointment));
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Error getting entities.");
         return null;
      }
   }

   public static int deleteEntitiesForTypeId(Class classForItemsToDelete, Class classForTypeId, Integer id) {
      int deleteCount = 0;
      try {
         deleteCount = DBAccess.deleteEntitiesForTypeId(classForItemsToDelete.getSimpleName().toLowerCase(),
                 classForTypeId.getSimpleName().toLowerCase(), id);
      } catch (SQLException ex) {
         Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Error Deleting all " +
                 classForItemsToDelete.getSimpleName() + "s for " +
                 classForTypeId.getSimpleName().toLowerCase() + "Id: " + id, ex);
      }
      return deleteCount;
   }
}
