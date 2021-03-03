package bsa.model;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dbierek
 */
public class Customer extends Entity {

   private Integer customerId;
   private String customerName;
   private Integer addressId;
   private Integer active;

   public Customer() {
   }

   public Customer(Integer customerId, String customerName, Integer addressId, Integer active) {
      this.customerId = customerId;
      this.customerName = customerName;
      this.addressId = addressId;
      this.active = active;
   }

   @Override
   public String toString() {
      return customerName;
   }

   @Override
   public Integer getId() {
      return customerId;
   }

   @Override
   public void setId(Integer id) {
      customerId = id;
   }
   
   public static Field getNameField() {
      try {
         return Customer.class.getDeclaredField("customerName");
      } catch (NoSuchFieldException | SecurityException ex) {
         Logger.getLogger(Address.class.getName()).log(Level.SEVERE, "Error getting name field", ex);
         return null;
      }
   }


   public Integer getCustomerId() {
      return customerId;
   }

   public void setCustomerId(Integer customerId) {
      this.customerId = customerId;
   }

   public String getCustomerName() {
      return customerName;
   }

   public void setCustomerName(String customerName) {
      this.customerName = customerName;
   }

   public Integer getAddressId() {
      return addressId;
   }

   public void setAddressId(Integer addressId) {
      this.addressId = addressId;
   }

   public Integer getActive() {
      return active;
   }

   public void setActive(Integer active) {
      this.active = active;
   }

}
