package bsa.model;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dbierek
 */
public class City extends Entity {

   private Integer cityId;
   private String city;
   private Integer countryId;

   public City() {
   }

   public City(Integer cityId, String city, Integer countryId) {
      this.cityId = cityId;
      this.city = city;
      this.countryId = countryId;
   }

   @Override
   public String toString() {
      return city;
   }

   @Override
   public Integer getId() {
      return cityId;
   }

   @Override
   public void setId(Integer id) {
      cityId = id;
   }
   
   public static Field getNameField() {
      try {
         return City.class.getDeclaredField("city");
      } catch (NoSuchFieldException | SecurityException ex) {
         Logger.getLogger(Address.class.getName()).log(Level.SEVERE, "Error getting name field", ex);
         return null;
      }
   }


   public Integer getCityId() {
      return cityId;
   }

   public void setCityId(Integer cityId) {
      this.cityId = cityId;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public Integer getCountryId() {
      return countryId;
   }

   public void setCountryId(Integer countryId) {
      this.countryId = countryId;
   }

}
