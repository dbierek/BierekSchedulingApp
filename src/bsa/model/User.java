package bsa.model;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dbierek
 */
public class User extends Entity {

   private Integer userId;
   private String userName;
   private String password;
   private Integer active;

   public User() {
   }

   public User(Integer userId, String userName, String password, Integer active) {
      this.userId = userId;
      this.userName = userName;
      this.password = password;
      this.active = active;
   }

   @Override
   public String toString() {
      return userName;
   }

   @Override
   public Integer getId() {
      return userId;
   }

   @Override
   public void setId(Integer id) {
      userId = id;
   }
   
   public static Field getNameField() {
      try {
         return User.class.getDeclaredField("userName");
      } catch (NoSuchFieldException | SecurityException ex) {
         Logger.getLogger(Address.class.getName()).log(Level.SEVERE, "Error getting name field", ex);
         return null;
      }
   }


   public Integer getUserId() {
      return userId;
   }

   public void setUserId(Integer userId) {
      this.userId = userId;
   }

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Integer getActive() {
      return active;
   }

   public void setActive(Integer active) {
      this.active = active;
   }

}
