package bsa.utility;

import bsa.model.*;
import java.util.HashMap;

/**
 *
 * @author dbierek
 */
public class EntityClasses {

   protected static HashMap<String, Class> classes;

   public static HashMap<String, Class> getClasses() {
      if (classes == null) {
         classes = new HashMap();
         classes.put("addressId", Address.class);
         classes.put("appointmentId", Appointment.class);
         classes.put("cityId", City.class);
         classes.put("countryId", Country.class);
         classes.put("customerId", Customer.class);
         classes.put("userId", User.class);
      }
      return classes;
   }
}
