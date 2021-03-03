package bsa.utility;

import bsa.controller.EntityPropertiesPaneController;
import bsa.model.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dbierek
 */
public class AttributeNames {

   protected static HashMap<String, String> colNames;

   public static Method getterFromEntityType(Class clazz) {
      Method getter = null;
      try {
         getter = clazz.getDeclaredMethod("toString");
      } catch (NoSuchMethodException | SecurityException ex) {
         Logger.getLogger(EntityPropertiesPaneController.class.getName()).log(Level.SEVERE, "Could not find toString method", ex);
      }
      return getter;
   }

   public static Method getterFromField(Class clazz, Field field) {
      Method getter = null;
      try {
         getter = clazz.getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
      } catch (NoSuchMethodException | SecurityException ex) {
         try {
            getter = Entity.class.getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
         } catch (NoSuchMethodException | SecurityException ex1) {
            Logger.getLogger(EntityPropertiesPaneController.class.getName()).log(Level.SEVERE, null, ex1);
         }
      }
      return getter;
   }

   public static HashMap<String, String> getColNames() {
      Locale locale = Locale.getDefault();

      ResourceBundle labels = ResourceBundle.getBundle("bsa.utility.text", locale);

      if (colNames == null) {
         colNames = new HashMap();
         colNames.put("active", labels.getString("active"));
         colNames.put("address", labels.getString("address"));
         colNames.put("address2", labels.getString("address2"));
         colNames.put("addressId", labels.getString("addressId"));
         colNames.put("appointmentId", labels.getString("appointmentId"));
         colNames.put("city", labels.getString("city"));
         colNames.put("cityId", labels.getString("cityId"));
         colNames.put("contact", labels.getString("contact"));
         colNames.put("country", labels.getString("country"));
         colNames.put("countryId", labels.getString("countryId"));
         colNames.put("createDate", labels.getString("createDate"));
         colNames.put("createdBy", labels.getString("createdBy"));
         colNames.put("customerId", labels.getString("customerId"));
         colNames.put("customerName", labels.getString("customerName"));
         colNames.put("description", labels.getString("description"));
         colNames.put("end", labels.getString("end"));
         colNames.put("lastUpdate", labels.getString("lastUpdate"));
         colNames.put("lastUpdateBy", labels.getString("lastUpdateBy"));
         colNames.put("location", labels.getString("location"));
         colNames.put("password", labels.getString("password"));
         colNames.put("phone", labels.getString("phone"));
         colNames.put("postalCode", labels.getString("postalCode"));
         colNames.put("start", labels.getString("start"));
         colNames.put("title", labels.getString("title"));
         colNames.put("type", labels.getString("type"));
         colNames.put("url", labels.getString("url"));
         colNames.put("userId", labels.getString("userId"));
         colNames.put("userName", labels.getString("userName"));
      }
      return colNames;
   }
}
