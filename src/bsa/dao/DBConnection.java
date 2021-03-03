package bsa.dao;

import bsa.controller.PopupScreen;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.h2.tools.RunScript;
/**
 *
 * @author dbierek
 */
public class DBConnection {

   private static Connection conn;

   public static Connection getConnection() throws SQLException {
      if (conn == null) {
         conn = DriverManager.getConnection(
                 "jdbc:h2:./bsaDatabase", "", "");
      }
      return conn;
   }
   
   public static boolean initDB() {
      
      try {
         PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(
                 "select \"intialized\" from initialized");
         ResultSet results = preparedStatement.executeQuery();
         return false;
      } catch (SQLException ex) {
         Logger.getLogger(DBAccess.class.getName()).log(Level.INFO, "Database not initialized. Initializing with default data.");}
      try {
         PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(
                 "create table intialized (initialized integer)");
         preparedStatement.execute();
         preparedStatement = DBConnection.getConnection().prepareStatement(
                 "insert into intialized values (1)");
         preparedStatement.execute();
         // Getting resource(File) from class loader
         InputStream in = DBConnection.class.getResourceAsStream("/initDB.sql"); 
         BufferedReader reader = new BufferedReader(new InputStreamReader(in));
         RunScript.execute(conn, reader);
         return true;
      } catch (SQLException ex) {
         Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, "Database initializization failed.", ex);
      } 
      return false;
   }
}
