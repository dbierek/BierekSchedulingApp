package bsa.model;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dbierek
 */
public class Country extends Entity {

   private Integer countryId;
   private String country;

   public Country() {
   }

   public Country(Integer countryId, String country) {
      this.countryId = countryId;
      this.country = country;
   }

   @Override
   public String toString() {
      return country;
   }

   @Override
   public Integer getId() {
      return countryId;
   }

   @Override
   public void setId(Integer id) {
      countryId = id;
   }
   
   public static Field getNameField() {
      try {
         return Country.class.getDeclaredField("country");
      } catch (NoSuchFieldException | SecurityException ex) {
         Logger.getLogger(Address.class.getName()).log(Level.SEVERE, "Error getting name field", ex);
         return null;
      }
   }


   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public Integer getCountryId() {
      return countryId;
   }

   public void setCountryId(Integer countryId) {
      this.countryId = countryId;
   }

}
