package bsa;

import bsa.controller.AppointmentReminderScreenController;
import bsa.controller.LoginScreenController;
import bsa.controller.LoginEvent;
import bsa.controller.MainScreenController;
import bsa.model.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * This is a scheduling application written by Daniel Bierek. Version 3 - 3/18/2020
 *
 * @author dbierek
 */
public class BierekSchedulingApp extends Application {

   public static void main(String[] args) {
      launch(args);
   }

   public static int START_WIDTH = 1200;
   public static int START_HEIGHT = 600;
   protected Stage mainStage;

   protected User loggedInUser;

   private final boolean skipLogin = false;

   @Override
   public void start(Stage primaryStage) throws IOException {
      mainStage = primaryStage;
      if (skipLogin) {
         loggedInUser = new User(1, "test", "test", 1);
         loginSuccessful();
      } else {
         openLoginDialog();
      }
   }

   private void loginSuccessful() throws IOException {
      FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/bsa/view/MainScreen.fxml"));
      AnchorPane mainScreen = mainLoader.load();
      MainScreenController controller = mainLoader.getController();
      controller.initializeComponents(mainScreen);
      Scene scene = new Scene(mainScreen, START_WIDTH, START_HEIGHT);
      mainStage.setScene(scene);
      mainStage.setTitle("Bierek Scheduling Application");
      mainStage.show();
      checkForAppointment();
   }

   private void openLoginDialog() throws IOException {
      FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/bsa/view/Login.fxml"));
      Parent loginDialog = loginLoader.load();
      LoginScreenController loginController = loginLoader.getController();
      Stage loginStage = new Stage();
      loginStage.setScene(new Scene(loginDialog, 420, 390));
      loginController.setStage(loginStage);
      loginStage.addEventHandler(LoginEvent.LOGIN_EVENT_TYPE, (loginEvent) -> {
         try {
            loggedInUser = loginEvent.getUser();
            loginSuccessful();
         } catch (IOException ex) {
            Logger.getLogger(BierekSchedulingApp.class.getName()).log(Level.SEVERE, "Error loading main screen", ex);
         }
         loginStage.close();
         try {
            File loginLog = new File("loginlog.txt");
            if (!loginLog.exists()) {
               loginLog.createNewFile();
            }
            ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("GMT"));
            Files.write(Paths.get("loginlog.txt"), Arrays.asList(loginEvent.getUser().getUserName() + ", " + zonedDateTime), StandardOpenOption.APPEND);
         } catch (IOException ex) {
            Logger.getLogger(BierekSchedulingApp.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
   }

   private void checkForAppointment() {
      new AppointmentReminderScreenController(loggedInUser).showAppointmentReminderPopup(mainStage);
   }
}
