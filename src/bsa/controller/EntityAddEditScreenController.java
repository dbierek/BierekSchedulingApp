/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsa.controller;

import bsa.dao.DBAccess;
import bsa.model.Entity;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author dbierek
 */
public class EntityAddEditScreenController {

   @FXML
   public FlowPane flowPane;

   @FXML
   public Text screenLabel;

   @FXML
   public Button actButton;

   protected final Class clazz;

   protected EntityPropertiesPaneController entityPropertiesPaneController;
   protected EventHandler<ActionEvent> actButtonActionHandler;
   protected final AddEdit addEdit;

   <T extends Entity> EntityAddEditScreenController(Stage owner, Class clazz, T entity) {
      this.clazz = clazz;
      this.addEdit = AddEdit.EDIT;

      setActionHandler();
      createScreen(owner, addEdit, entity);
   }

   EntityAddEditScreenController(Stage owner, Class clazz) {
      this.clazz = clazz;
      this.addEdit = AddEdit.ADD;
      setActionHandler();
      createScreen(owner, addEdit, null);
   }

   private void setActionHandler() {
      this.actButtonActionHandler = (ActionEvent e) -> { // Lambda expression used to easily create an EventHandler
         Entity newEntity = entityPropertiesPaneController.getEntity();
         try {
            if (addEdit == AddEdit.ADD) {
               DBAccess.create(newEntity);
            } else {
               DBAccess.update(newEntity);
            }
         } catch (SQLException ex) {
            Logger.getLogger(EntityAddEditScreenController.class.getName()).log(Level.SEVERE, "Error adding or editing entity.", ex);
         }
         flowPane.fireEvent(new EntityEvent(addEdit.entityEventType));
      };
   }

   private void createScreen(Stage owner, AddEdit addEdit, Entity entity) {
      FXMLLoader addEditScreenLoader = new FXMLLoader(getClass().getResource("/bsa/view/AddEditScreen.fxml"));
      addEditScreenLoader.setController(this);
      Stage screenStage = new Stage();
      screenStage.initOwner(owner);
      screenStage.setTitle(addEdit.type + " " + clazz.getSimpleName());
      screenStage.setResizable(false);
      screenStage.initModality(Modality.WINDOW_MODAL);
      screenStage.show();
      try {
         FlowPane addDialog = addEditScreenLoader.load();
         screenStage.setScene(new Scene(addDialog, 400, 470));
      } catch (IOException ex) {
         Logger.getLogger(EntityAddEditScreenController.class.getName()).log(Level.SEVERE, null, ex);
      }
      flowPane.setPrefWrapLength(500);
      flowPane.addEventHandler(EntityEvent.ADD_EVENT_TYPE,
              (e) -> { // Lambda expression used to easily create an EventHandler
                 screenStage.close();
              });
      flowPane.addEventHandler(EntityEvent.EDIT_EVENT_TYPE,
              (e) -> { // Lambda expression used to easily create an EventHandler
                 screenStage.close();
              });
      screenLabel.setText(addEdit.type + " " + clazz.getSimpleName());
      actButton.setText(addEdit.type);
      actButton.setOnAction(actButtonActionHandler);
      if (addEdit.equals(AddEdit.EDIT)) {
         entityPropertiesPaneController = new EntityPropertiesPaneController(clazz, entity, flowPane);
      } else if (addEdit.equals(AddEdit.ADD)) {
         entityPropertiesPaneController = new EntityPropertiesPaneController(clazz, flowPane);
      }
      flowPane.addEventHandler(EntityEvent.VALIDATE_EVENT_TYPE, (event) -> {
         actButton.setDisable(!entityPropertiesPaneController.isValid());
      });
      actButton.setDisable(!entityPropertiesPaneController.isValid());
   }

   protected enum AddEdit {
      ADD("Add", EntityEvent.ADD_EVENT_TYPE),
      EDIT("Edit", EntityEvent.EDIT_EVENT_TYPE);
      String type;
      EventType<EntityEvent> entityEventType;

      AddEdit(String type, EventType<EntityEvent> entityEventType) {
         this.type = type;
         this.entityEventType = entityEventType;
      }
   }
}
