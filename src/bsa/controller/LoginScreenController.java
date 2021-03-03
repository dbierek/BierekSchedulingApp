package bsa.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import bsa.dao.DBAccess;
import bsa.dao.DBConnection;
import bsa.model.User;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.Cursor;

/**
 *
 * @author dbierek
 */
public class LoginScreenController {

   public Stage loginStage;
   @FXML
   public TextField passwordTextField;

   @FXML
   public TextField userNameTextField;

   @FXML
   public Button loginButton;

   @FXML
   public Button cancelButton;

   @FXML
   public Text helpTextLabel;

   @FXML
   public Label userNameLabel;

   @FXML
   public Label passwordLabel;

   @FXML
   public Text screenLabel;

   @FXML
   void cancelButtonPressed(ActionEvent event) {
      loginStage.fireEvent(new WindowEvent(loginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
   }

   public void setStage(Stage loginStage) {

      this.loginStage = loginStage;
      ResourceBundle text = ResourceBundle.getBundle("bsa.utility.text", Locale.getDefault());
      loginStage.setTitle((String) text.getObject("loginTitle"));
      helpTextLabel.setText((String) text.getObject("loginHelpText"));
      userNameLabel.setText((String) text.getObject("loginUserName"));
      passwordLabel.setText((String) text.getObject("loginPassword"));
      screenLabel.setText((String) text.getObject("loginScreen"));
      loginButton.setText((String) text.getObject("login"));
      loginButton.addEventHandler(ActionEvent.ACTION, (event) -> {
         loginStage.getScene().setCursor(Cursor.WAIT);
         new Thread(() -> {
            User user = attemptLogin();
            Platform.runLater(() -> {
               loginStage.getScene().setCursor(Cursor.DEFAULT);
               if (user == null) {
                  PopupScreen failedLoginScreen = new PopupScreen(loginStage, (String) text.getObject("loginFailed"), (String) text.getObject("loginIncorrect"), 200, 100);
                  loginStage.fireEvent(new LoginEvent(LoginEvent.LOGIN_FAILED_EVENT_TYPE, user));
               } else {
                  loginStage.fireEvent(new LoginEvent(LoginEvent.LOGIN_EVENT_TYPE, user));
               }
            });
         }).start();
      });
      cancelButton.setText(
              (String) text.getObject("cancel"));
      loginStage.setResizable(
              false);
      loginStage.initModality(Modality.WINDOW_MODAL);

      loginStage.show();

      userNameTextField.requestFocus();
      if (DBConnection.initDB()) {
         PopupScreen databaseInitialized = new PopupScreen(loginStage, "Database Initialized", "Initialized Scheduling Application Database. Login with user name: admin, password: admin", 200, 100);
      }

   }

   private User attemptLogin() { // Lambda expression used to easily create an EventHandler
      User user = makeAttempt(getUserName(), getPassword());
      return user;
   }

   private User makeAttempt(String loginUserName, String loginPassword) {
      ResourceBundle text = ResourceBundle.getBundle("bsa.utility.text", Locale.getDefault());
      return DBAccess.login(loginUserName, loginPassword);
   }

   public String getUserName() {
      return userNameTextField.getText();
   }

   public String getPassword() {
      return passwordTextField.getText();
   }
}
