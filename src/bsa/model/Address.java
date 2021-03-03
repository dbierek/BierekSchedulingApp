package bsa.model;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dbierek
 */
public class Address extends Entity {

   private Integer addressId;
   private String address;
   private String address2;
   private Integer cityId;
   private String postalCode;
   private String phone;

   public Address() {
   }

   public Address(Integer addressId, String address, String address2, Integer cityId, String postalCode, String phone) {
      super();
      this.addressId = addressId;
      this.address = address;
      this.address2 = address2;
      this.cityId = cityId;
      this.postalCode = postalCode;
      this.phone = phone;
   }

   @Override
   public String toString() {
      return address;
   }

   @Override
   public Integer getId() {
      return addressId;
   }

   @Override
   public void setId(Integer id) {
      addressId = id;
   }
   
   public static Field getNameField() {
      try {
         return Address.class.getDeclaredField("address");
      } catch (NoSuchFieldException | SecurityException ex) {
         Logger.getLogger(Address.class.getName()).log(Level.SEVERE, "Error getting name field", ex);
         return null;
      }
   }   
   public Integer getAddressId() {
      return addressId;
   }

   public void setAddressId(Integer addressId) {
      this.addressId = addressId;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getAddress2() {
      return address2;
   }

   public void setAddress2(String address2) {
      this.address2 = address2;
   }

   public Integer getCityId() {
      return cityId;
   }

   public void setCityId(Integer cityId) {
      this.cityId = cityId;
   }

   public String getPostalCode() {
      return postalCode;
   }

   public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }
}
