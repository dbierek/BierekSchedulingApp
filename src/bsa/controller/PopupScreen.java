package bsa.controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author dbierek
 */
public class PopupScreen {

   public PopupScreen(Stage owner, String titleLabel, String textLabel) {
      this(owner, titleLabel, textLabel, 200, 200);
   }
   public PopupScreen(Stage owner, String titleLabel, String textLabel, double width, double height) {
      AnchorPane popupDialog = new AnchorPane();
      Stage popupStage = new Stage();
      popupDialog.setMinWidth(width);
      popupDialog.setMinHeight(height);

      GridPane popupGrid = new GridPane();
      popupGrid.setVgap(30);
      Text popupText = new Text(textLabel);
      popupText.setTextAlignment(TextAlignment.CENTER);
      popupText.setWrappingWidth(width - 40);
      popupGrid.setPadding(new Insets(20, 0, 20, 20));
      Button okButton = new Button("OK");
      okButton.setTranslateX(width / 2 - 30);
      okButton.setPrefHeight(30);
      okButton.setDefaultButton(true);
      popupGrid.add(popupText, 0, 0);
      popupGrid.add(okButton, 0, 1);
      popupDialog.getChildren().add(popupGrid);

      okButton.setOnAction((okEvent) -> { // Lambda expression used to easily create an EventHandler
         popupStage.close();
      });
      popupStage.initOwner(owner);
      popupStage.setTitle(titleLabel);
      popupStage.setResizable(false);
      popupStage.initModality(Modality.WINDOW_MODAL);
      Scene popupScene = new Scene(popupDialog, width, height);
      popupStage.setScene(popupScene);
      popupStage.show();
   }
}
