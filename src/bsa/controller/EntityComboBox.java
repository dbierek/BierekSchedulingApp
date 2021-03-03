package bsa.controller;

import bsa.dao.EntityDAOImpl;
import bsa.model.Entity;
import bsa.utility.AttributeNames;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

/**
 *
 * @author dbierek
 * @param <E> extends Entity
 */
public class EntityComboBox<E extends Entity> extends ComboBox {

   private final Class clazz;
   
   private final Method method;

   EntityComboBox(Class clazz, E toSelect) {
      this(clazz);
      getSelectionModel().select(toSelect);
   }

   EntityComboBox(Class clazz) {
      this(clazz, AttributeNames.getterFromEntityType(clazz));
   }

   EntityComboBox(Class clazz, Method method) {
      this.clazz = clazz;
      this.method = method;
      
//      ListCell<Entity> listCell = new EntityCell<>();
//      setCellFactory(cellView -> new EntityCell<>());
      setItems(EntityDAOImpl.<E>getEntities(clazz));
      getSelectionModel().selectFirst();
      setPrefWidth(100);
      setPrefHeight(30);
   }

   class EntityCell<Entity> extends ListCell<Entity> {

      @Override
      public void updateItem(Entity entity, boolean empty) {
         try {
            Object value = method.invoke(entity);
            super.updateItem(entity, empty);
            if (value != null) {
               setText(value.toString());
            } else {
               setText("");
            }
         } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(EntityComboBox.class.getName()).log(Level.SEVERE, "Error updating EntityComboBox cell value", ex);
         }
      }
   };
}
