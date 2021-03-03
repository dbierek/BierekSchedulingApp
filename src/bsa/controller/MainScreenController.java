/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsa.controller;

import bsa.model.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author dbierek
 */
public class MainScreenController implements Initializable {

   @FXML
   public TabPane tabPane;

   private FlowPane addressPane;

   private FlowPane appointmentPane;

   private FlowPane cityPane;

   private FlowPane countryPane;

   private FlowPane customerPane;

   private FlowPane userPane;

   @Override
   public void initialize(URL url, ResourceBundle rb) {
   }

   public void initializeComponents(AnchorPane mainScreen) {
      final HashMap<Node, EntityTablePaneController> controllers = new HashMap<>();
      try {
         EntityTablePaneController customerPaneController = new CustomerTablePaneController();
         customerPane = customerPaneController.getEntityTablePane();
         controllers.put(customerPane, customerPaneController);
         
         EntityTablePaneController appointmentTablePaneController = new AppointmentTablePaneController();
         appointmentPane = appointmentTablePaneController.getEntityTablePane();
         controllers.put(appointmentPane, appointmentTablePaneController);
         
         EntityTablePaneController addressPaneController = new EntityTablePaneController(Address.class);
         addressPane = addressPaneController.getEntityTablePane();
         controllers.put(addressPane, addressPaneController);
         
         EntityTablePaneController cityPaneController = new EntityTablePaneController(City.class);
         cityPane = cityPaneController.getEntityTablePane();
         controllers.put(cityPane, cityPaneController);
         
         EntityTablePaneController countryPaneController = new EntityTablePaneController(Country.class);
         countryPane = countryPaneController.getEntityTablePane();
         controllers.put(countryPane, countryPaneController);
         
         EntityTablePaneController userPaneController = new EntityTablePaneController(User.class);
         userPane = userPaneController.getEntityTablePane();
         controllers.put(userPane, userPaneController);
         
         tabPane.getTabs().add(new Tab("Appointments", appointmentPane));
         tabPane.getTabs().add(new Tab("Customers", customerPane));
         tabPane.getTabs().add(new Tab("Addresses", addressPane));
         tabPane.getTabs().add(new Tab("Cities", cityPane));
         tabPane.getTabs().add(new Tab("Countries", countryPane));
         tabPane.getTabs().add(new Tab("Users", userPane));
         tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            EntityTablePaneController newTabController = controllers.get(newValue.getContent());
            newTabController.refreshItems();
         });

      } catch (IOException ex) {
         Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
