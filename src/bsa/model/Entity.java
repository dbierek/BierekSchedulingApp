package bsa.model;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;

/**
 *
 * @author dbierek
 */
public abstract class Entity {
   public ZonedDateTime createDate;
   public String createdBy;
   public ZonedDateTime lastUpdate;
   public String lastUpdateBy;

   public Entity() {
      this.createDate = ZonedDateTime.now();
      this.createdBy = "Not used";
      this.lastUpdate = ZonedDateTime.now();
      this.lastUpdateBy = "Not used";
   }

   public ZonedDateTime getCreateDate() {
      return createDate;
   }

   public void setCreateDate(ZonedDateTime createDate) {
      this.createDate = createDate;
   }

   public String getCreatedBy() {
      return createdBy;
   }

   public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
   }

   public ZonedDateTime getLastUpdate() {
      return lastUpdate;
   }

   public void setLastUpdate(ZonedDateTime lastUpdate) {
      this.lastUpdate = lastUpdate;
   }

   public String getLastUpdateBy() {
      return lastUpdateBy;
   }

   public void setLastUpdateBy(String lastUpdateBy) {
      this.lastUpdateBy = lastUpdateBy;
   }
   
   @Override
   public String toString() {
      return "";
   }
   
   public Integer getId() {
      return null;
   }
   
   public void setId(Integer id) {
   }
   
   public static Field getNameField() {
      return null;
   }
}
