package bsa.controller;

import bsa.dao.EntityDAOImpl;
import bsa.model.*;
import bsa.utility.AttributeNames;
import bsa.utility.EntityClasses;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dbierek
 */
public class EntityTablePaneController {

   protected final FlowPane entityTablePane;

   @FXML
   public FlowPane buttonPane;

   @FXML
   public TableView table;

   @FXML
   public Button addButton;

   @FXML
   public Button editButton;

   @FXML
   public Button deleteButton;

   public Class clazz;

   private ObservableList<Entity> entities = FXCollections.emptyObservableList();
   private HashMap<String, HashMap<Integer, Entity>> propertiesEntitiesHashMaps = new HashMap<>();

   EntityTablePaneController(Class clazz) throws IOException {
      this(clazz, "/bsa/view/EntityTablePane.fxml");
   }

   EntityTablePaneController(Class clazz, String fxmlFileName) throws IOException {
      entityTablePane = loadPane(clazz, fxmlFileName);
   }

   private FlowPane loadPane(Class clazz, String fxmlFileName) throws IOException {
      FXMLLoader entityTablePaneLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
      entityTablePaneLoader.setController(this);
      FlowPane pane = entityTablePaneLoader.load();

      setClazz(clazz);
      initializeComponents();
      refreshItems();
      return pane;
   }

   public FlowPane getEntityTablePane() {
      return entityTablePane;
   }

   public Class getClazz() {
      return clazz;
   }

   private void setClazz(Class clazz) {
      this.clazz = clazz;
   }

   protected void initializeComponents() {
      ArrayList<Field> fields = new ArrayList(Arrays.asList(clazz.getDeclaredFields()));
      table.itemsProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
         propertiesEntitiesHashMaps = new HashMap<>();
         fields.stream().forEach((field) -> { // Iterate through the Fields and for each one that is a foreign key, get the ids for the referenced entities from each item in the table (using Stream and map with a lambda expression
            if (field.getName().contains("Id") && !field.getName().contains(clazz.getSimpleName().toLowerCase())) {
               HashSet<Integer> fieldIds = new HashSet<>();
               table.getItems().stream().forEach((entity) -> {
                  Entity ent = (Entity) entity;
                  Method getter = AttributeNames.getterFromField(clazz, field);
                  try {
                     fieldIds.add((Integer) getter.invoke(ent));
                  } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                     Logger.getLogger(EntityTablePaneController.class.getName()).log(Level.SEVERE, null, ex);
                  }
               });
               ObservableList<Entity> propertyEntities = EntityDAOImpl.getEntities(EntityClasses.getClasses().get(field.getName()), new ArrayList<>(fieldIds));

               HashMap<Integer, Entity> propertyEntitiesHashMap = new HashMap<>();
               propertyEntities.stream().forEach((entity) -> {
                  Entity ent = (Entity) entity;
                  propertyEntitiesHashMap.put(ent.getId(), ent);
               });
               propertiesEntitiesHashMaps.put(field.getName(), propertyEntitiesHashMap);
            }
         });
      });
      fields.stream().map((field) -> { // Stream and map used with a lambda expression to easily iterate through the Fields and for each one, create a TableColumn with the correct text
         Class propertyEntityClass = EntityClasses.getClasses().get(field.getName());
         if (propertyEntityClass != null && propertyEntityClass.equals(clazz)) {
            return null;
         }
         TableColumn<List<String>, String> col = new TableColumn<>((String) AttributeNames.getColNames().get(field.getName()));
         if (field.getName().contains("Id")) {
            col.setCellValueFactory((cellValue) -> {
               Integer propertyEntityId = null;
               Entity rowEntity = (Entity) cellValue.getValue();
               Method getter = AttributeNames.getterFromField(clazz, field);
               try {
                  propertyEntityId = (Integer) getter.invoke(rowEntity);
               } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                  Logger.getLogger(EntityPropertiesPaneController.class.getName()).log(Level.SEVERE, null, ex);
               }
               return new ReadOnlyStringWrapper(propertiesEntitiesHashMaps.get(field.getName()).get(propertyEntityId).toString());
            });
         } else if (field.getType().equals(ZonedDateTime.class)) {
            col.setComparator((o1, o2) -> {
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a").withZone(ZoneId.systemDefault());
               return LocalDateTime.parse(o1, formatter).atZone(ZoneId.systemDefault()).
                       compareTo(LocalDateTime.parse(o2, formatter).atZone(ZoneId.systemDefault()));
            });
            col.setCellValueFactory((cellValue) -> {
               ZonedDateTime propertyEntityZonedDateTime = null;
               Entity rowEntity = (Entity) cellValue.getValue();
               Method getter = AttributeNames.getterFromField(clazz, field);
               try {
                  if (getter != null) {
                     propertyEntityZonedDateTime = (ZonedDateTime) getter.invoke(rowEntity);
                  } else {
                     Logger.getLogger(EntityPropertiesPaneController.class.getName()).log(Level.SEVERE, "Getter not found for date");
                     return new ReadOnlyStringWrapper("");
                  }
               } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                  Logger.getLogger(EntityPropertiesPaneController.class.getName()).log(Level.SEVERE, "Error getting date", ex);
               }
               if (propertyEntityZonedDateTime == null) {
                  return new ReadOnlyStringWrapper("");
               }

               ZonedDateTime zonedDateTime = propertyEntityZonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
               DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
               return new ReadOnlyStringWrapper(dateFormat.format(zonedDateTime));
            });
         } else {
            col.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
         }
         return col;
      }).forEachOrdered((col) -> { // Lambda expression used to quickly add each of the new columns to the TableView
         if (col != null) {
            table.getColumns().add(col);
         }
      });
      editButton.setDisable(true);
      deleteButton.setDisable(true);
      table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> { // Lambda expression used to easily create a ChangeListener
         editButton.setDisable(newSelection == null);
         deleteButton.setDisable(newSelection == null);
      });

      addButton.setOnAction((e) -> { // Lambda expression used to easily create an EventHandler
         EntityAddEditScreenController addScreenController = new EntityAddEditScreenController((Stage) addButton.getScene().getWindow(), clazz);
         addScreenController.flowPane.addEventHandler(EntityEvent.ADD_EVENT_TYPE, (ev)
                 -> { // Lambda expression used to easily create an EventHandler
            refreshItems();
         });
      });

      editButton.setOnAction((e) -> { // Lambda expression used to easily create an EventHandler
         EntityAddEditScreenController editScreenController = new EntityAddEditScreenController((Stage) editButton.getScene().getWindow(), clazz, (Entity) table.getSelectionModel().getSelectedItem());
         editScreenController.flowPane.addEventHandler(EntityEvent.EDIT_EVENT_TYPE, (ev)
                 -> { // Lambda expression used to easily create an EventHandler
            refreshItems();
         });
      });

      deleteButton.setOnAction(deleteButtonActionHandler());
   }

   private EventHandler<ActionEvent> deleteButtonActionHandler() {
      return (e) -> { // Lambda expression used to easily create an EventHandler
         Entity toDelete = (Entity) table.getSelectionModel().getSelectedItem();
         EntityDAOImpl.deleteEntity(toDelete);
         refreshItems();
      };
   }

   public ObservableList<Entity> getEntities() {
      return entities;
   }

   public void setEntities(ObservableList<Entity> entities) {
      this.entities = entities;
      table.setItems(entities);
      Optional<TableColumn> firstCol = table.getColumns().stream().filter((o) -> {
         TableColumn col = (TableColumn) o;
         return col.getText().equals(AttributeNames.getColNames().get("start"));
      }).findFirst();
      if (firstCol.isPresent()) {
         table.getSortOrder().add(firstCol.get());
      }
   }

   public void refreshItems() {
      setEntities(EntityDAOImpl.getEntities(clazz));
   }
}
