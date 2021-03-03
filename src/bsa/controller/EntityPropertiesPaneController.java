package bsa.controller;

import bsa.model.*;
import bsa.utility.AttributeNames;
import bsa.utility.EntityClasses;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author dbierek
 * @param <E> extends Entity
 */
public class EntityPropertiesPaneController<E extends Entity> {

   public FlowPane flowPane;

   private final Class clazz;

   private final HashMap<String, TextInputControl> textInputs = new HashMap<>();

   private final HashMap<String, DatePicker> pickers = new HashMap<>();

   private final HashMap<String, EntityComboBox<E>> entityComboBoxes = new HashMap<>();

   private final HashMap<String, ComboBox<LocalTime>> timeComboBoxes = new HashMap<>();

   private Integer entityId;

   /**
    * Workday start time. Hard coded to be 9 AM Local Time.
    */
   private final LocalTime WORKDAY_START = LocalTime.of(9, 0);

   /**
    * Workday end time. Hard coded to be 5 PM Local Time.
    */
   private final LocalTime WORKDAY_END = LocalTime.of(17, 0);

   /**
    * Minimum time interval. Hard coded to be 30 minutes.
    */
   private final int INTERVAL_MINUTES = 30;
   protected TextField nameTextField;
   protected ComboBox<LocalTime> startTimeComboBox;
   protected DatePicker startPicker;
   protected ComboBox<LocalTime> endTimeComboBox;
   protected DatePicker endPicker;
   private Label nameErrorLabel;
   private Label dateTimeErrorLabel;

   EntityPropertiesPaneController(Class clazz, E entity, FlowPane flowPane) {
      this(clazz, flowPane);
      if (entity != null) {
         setEntity(entity);
      }
   }

   EntityPropertiesPaneController(Class clazz, FlowPane flowPane, Integer entityId) {
      this(clazz, flowPane);
      this.entityId = entityId;
   }

   EntityPropertiesPaneController(Class clazz, FlowPane flowPane) {
      this.clazz = clazz;
      this.flowPane = flowPane;
      entityId = 0;
      generatePaneElements();
   }

   private void generatePaneElements() {
      double height = 0;
      ArrayList<Field> fields = new ArrayList(Arrays.asList(clazz.getDeclaredFields()));
      fields.stream().map((Field field) -> { // Stream and map used with a lambda expression to easily iterate through the Fields and for each one, create a Label with the correct text and an editor for the property
         GridPane attributeGridPane = new GridPane();
         attributeGridPane.getColumnConstraints().add(new ColumnConstraints(100));
         attributeGridPane.getColumnConstraints().add(new ColumnConstraints(200));
         attributeGridPane.getColumnConstraints().add(new ColumnConstraints(60));
         attributeGridPane.setMinWidth(800);
         attributeGridPane.setHgap(5);
         Class propertyEntityClass = EntityClasses.getClasses().get(field.getName());
         if (propertyEntityClass != null && propertyEntityClass.equals(clazz)) {
         } else {
            Label label = new Label(AttributeNames.getColNames().get(field.getName()));
            label.setMaxWidth(100);
            label.setPrefHeight(30);
            attributeGridPane.add(label, 0, 0);
            if (field.getName().contains("Id")) {
               EntityComboBox<E> entityComboBox = new EntityComboBox<>(EntityClasses.getClasses().get(field.getName()));
               attributeGridPane.add(entityComboBox, 1, 0);
               entityComboBoxes.put(field.getName(), entityComboBox);
            } else if (field.getType() == Integer.class || field.getType() == String.class) {
               TextArea textArea = new TextArea();

               TextField textField = new TextField();
               textField.setPromptText(label.getText());
               textField.setMaxWidth(200);
               textField.setPrefHeight(30);
               attributeGridPane.add(textField, 1, 0);
               textInputs.put(field.getName(), textField);
               Method getNameFieldMethod = null;
               try {
                  getNameFieldMethod = clazz.getMethod("getNameField");
               } catch (NoSuchMethodException | SecurityException ex) {
                  Logger.getLogger(EntityPropertiesPaneController.class.getName()).log(Level.SEVERE, null, ex);
               }
               Field clazzField = null;
               try {
                  if (getNameFieldMethod != null) {
                     clazzField = (Field) getNameFieldMethod.invoke(null);
                  }
               } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                  Logger.getLogger(EntityPropertiesPaneController.class.getName()).log(Level.SEVERE, null, ex);
               }
               if (field.equals(clazzField)) {
                  nameTextField = textField;
                  nameErrorLabel = new Label("Required");
                  nameErrorLabel.setStyle("-fx-text-fill: red;");
                  nameErrorLabel.setMaxWidth(60);
                  nameErrorLabel.setPrefHeight(30);
                  nameErrorLabel.setVisible(false);
                  attributeGridPane.add(nameErrorLabel, 2, 0);
                  textField.textProperty().addListener((observable, oldValue, newValue) -> {
                     Event.fireEvent(flowPane, new EntityEvent(EntityEvent.VALIDATE_EVENT_TYPE));
                  });
               }
            } else if (field.getType() == ZonedDateTime.class) {
               ObservableList<LocalTime> times = FXCollections.observableArrayList();
               LocalTime currentTime, endTime;
               ComboBox<LocalTime> timeComboBox = new ComboBox<>();
               DatePicker picker = new DatePicker();
               if (field.getName().contains("start")) {
                  startTimeComboBox = timeComboBox;
                  startPicker = picker;
                  currentTime = LocalTime.from(WORKDAY_START);
                  endTime = WORKDAY_END.minusMinutes(INTERVAL_MINUTES);
               } else {
                  endTimeComboBox = timeComboBox;
                  endPicker = picker;
                  currentTime = WORKDAY_START.plusMinutes(INTERVAL_MINUTES);
                  endTime = LocalTime.from(WORKDAY_END);
                  dateTimeErrorLabel = new Label("End must be after start");
                  dateTimeErrorLabel.setWrapText(true);
                  dateTimeErrorLabel.setStyle("-fx-text-fill: red;");
                  dateTimeErrorLabel.setMaxWidth(60);
                  dateTimeErrorLabel.setPrefHeight(60);
                  dateTimeErrorLabel.setVisible(false);
                  attributeGridPane.add(dateTimeErrorLabel, 2, 0, 1, 2);
               }
               DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a").withZone(ZoneId.systemDefault());
               while (currentTime.compareTo(endTime) <= 0) {
                  times.add(currentTime);
                  currentTime = currentTime.plus(INTERVAL_MINUTES, ChronoUnit.MINUTES);
               }
               timeComboBox.setItems(times);
               timeComboBox.getSelectionModel().selectFirst();
               attributeGridPane.add(timeComboBox, 1, 0);
               timeComboBox.setPrefWidth(200);
               timeComboBox.setPrefHeight(30);
               timeComboBox.setOnAction((event) -> {
                  Event.fireEvent(flowPane, new EntityEvent(EntityEvent.VALIDATE_EVENT_TYPE));
               });
               timeComboBoxes.put(field.getName(), timeComboBox);
               ListCell<LocalTime> listCell = new ListCell<LocalTime>() {
                  @Override
                  public void updateItem(LocalTime localTime, boolean empty) {
                     super.updateItem(localTime, empty);
                     if (localTime != null) {
                        setText(timeFormatter.format(localTime));
                     } else {
                        setText("");
                     }
                  }
               };
               timeComboBox.setButtonCell(listCell);
               timeComboBox.setCellFactory(cellView -> new ListCell<LocalTime>() {
                  @Override
                  public void updateItem(LocalTime localTime, boolean empty) {
                     super.updateItem(localTime, empty);
                     if (localTime != null) {
                        setText(timeFormatter.format(localTime));
                     } else {
                        setText("");
                     }
                  }
               });
               picker.setValue(LocalDate.now());
               picker.setPrefWidth(200);
               picker.setPrefHeight(30);
               attributeGridPane.add(picker, 1, 1);
               picker.setOnAction((event) -> {
                  Event.fireEvent(flowPane, new EntityEvent(EntityEvent.VALIDATE_EVENT_TYPE));
               });
               pickers.put(field.getName(), picker);
            }
         }
         return attributeGridPane;
      }).forEachOrdered((attributeGridPane) -> { // Lambda expression used to easily add the attribute's GridPane to the screen's attributes FlowPlane
         flowPane.getChildren().add(attributeGridPane);
      });
      Event.fireEvent(flowPane, new EntityEvent(EntityEvent.VALIDATE_EVENT_TYPE));
   }

   /**
    * @return A new <tt>Entity</tt> of the type defined by <tt>clazz</tt>.
    */
   protected E getEntity() {
      try {
         E entity = (E) clazz.cast(clazz.getDeclaredConstructor().newInstance());
         ArrayList<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
         for (Field field : fields) {
            Object value = null;
            Class propertyEntityClass = EntityClasses.getClasses().get(field.getName());
            if (propertyEntityClass != null && propertyEntityClass.equals(clazz)) {
               value = entityId;
            } else if (field.getName().contains("Id")) {
               EntityComboBox<E> propertyEntityComboBox = entityComboBoxes.get(field.getName());
               E selectedEntity = (E) propertyEntityComboBox.getSelectionModel().getSelectedItem();
               value = selectedEntity.getId();

            } else if (field.getType() == Integer.class) {
               try {
                  value = Integer.valueOf(textInputs.get(field.getName()).getText());
               } catch (NumberFormatException e) {
                  value = -1;

               }
            } else if (field.getType() == String.class) {
               value = textInputs.get(field.getName()).getText();

            } else if (field.getType() == ZonedDateTime.class) {
               LocalDateTime localDateTime = pickers.get(field.getName()).getValue().
                       atTime(timeComboBoxes.get(field.getName()).getSelectionModel().getSelectedItem());
               value = ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("GMT"));
            }
            String setterMethodName = "set"
                    + field.getName().substring(0, 1).toUpperCase()
                    + field.getName().substring(1);
            Optional<Method> optional = Arrays.asList(clazz.getDeclaredMethods()).stream()
                    .filter((Method filterMethod) -> {
                       return filterMethod.getName().equals(setterMethodName);
                    }).findFirst();
            if (!optional.isPresent()) {
               throw new NoSuchMethodException("Setter \"" + setterMethodName
                       + "\" not found for " + clazz + "." + field.getType() + field.getName());
            }
            Method method = optional.get();
            method.invoke(entity, value);
         }
         return entity;

      } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
         Logger.getLogger(EntityPropertiesPaneController.class
                 .getName()).log(Level.SEVERE, ex.getMessage(), ex);
      }
      return null;
   }

   /**
    * Fills the editors values using the entity's properties.
    *
    * @param <T> The type of the Entity
    * @param entity The entity to get the values from to fill in the editors.
    */
   private <T extends Entity> void setEntity(T entity) {
      entityId = entity.getId();
      ArrayList<Field> fields = new ArrayList(Arrays.asList(clazz.getDeclaredFields()));
      fields.forEach((field) -> { // Lambda expression used to easily iterate through the fields
         TextInputControl textField = textInputs.get(field.getName());
         ComboBox<LocalTime> timeComboBox = timeComboBoxes.get(field.getName());
         ComboBox<Entity> entityComboBox = entityComboBoxes.get(field.getName());
         DatePicker picker = pickers.get(field.getName());
         Object value;
         Method method = null;
         try {
            method = clazz.getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));

         } catch (NoSuchMethodException | SecurityException ex) {

            Logger.getLogger(EntityPropertiesPaneController.class
                    .getName()).log(Level.SEVERE, null, ex);
         }
         try {
            if (method != null) {
               value = method.invoke(entity);
            } else {
               value = null;

            }
         } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(EntityPropertiesPaneController.class
                    .getName()).log(Level.SEVERE, null, ex);
            return;
         }
         Class propertyEntityClass = EntityClasses.getClasses().get(field.getName());
         if (propertyEntityClass != null && propertyEntityClass.equals(clazz)) {
         } else if (field.getName().contains("Id")) {
            Integer propertyEntityId = (Integer) value;
            entityComboBox.getItems().forEach((propertyEntity) -> {
               try {
                  if (AttributeNames.getterFromField(clazz, field).invoke(entity).equals(propertyEntity.getId())) {
                     entityComboBox.getSelectionModel().select(propertyEntity);

                  }
               } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                  Logger.getLogger(EntityPropertiesPaneController.class
                          .getName()).log(Level.SEVERE, null, ex);
               }
            });

         } else if (field.getType() == Integer.class) {
            Integer integ = (Integer) value;
            textField.setText(Integer.toString(integ));

         } else if (field.getType() == String.class) {
            textField.setText((String) value);

         } else if (field.getType() == ZonedDateTime.class) {
            ZonedDateTime utcZonedDateTime = (ZonedDateTime) value;
            if (utcZonedDateTime == null) {
               return;
            }
            ZonedDateTime localZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
            picker.setValue(localZonedDateTime.toLocalDate());

            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a");
            Optional<LocalTime> optional = timeComboBox.getItems().stream().filter((LocalTime localTime) -> {
               return localTime.equals(localZonedDateTime.toLocalTime());
            }).findFirst();
            if (optional.isPresent()) {
               timeComboBox.getSelectionModel().select(optional.get());
            } else {
               timeComboBox.getSelectionModel().selectFirst();
            }
         }
      });
   }

   public boolean isValid() {
      boolean valid = true;
      if (startPicker != null) {
         LocalDateTime startLocalDateTime = startPicker.getValue().
                 atTime(startTimeComboBox.getSelectionModel().getSelectedItem());
         LocalDateTime endLocalDateTime = endPicker.getValue().
                 atTime(endTimeComboBox.getSelectionModel().getSelectedItem());
         boolean dateTimesValid = startLocalDateTime.compareTo(endLocalDateTime) < 0;
         dateTimeErrorLabel.setVisible(!dateTimesValid);
         valid &= dateTimesValid;
      }
      boolean nameValid = nameTextField.getText().length() > 0;
      nameErrorLabel.setVisible(!nameValid);
      valid &= nameValid;
      return valid;
   }
}
