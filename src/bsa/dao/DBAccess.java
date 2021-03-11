package bsa.dao;

import bsa.model.Appointment;
import bsa.model.Entity;
import bsa.model.User;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dbierek
 */
public class DBAccess {

   public static int create(Entity entity) throws SQLException {
      String entityClassName = entity.getClass().getSimpleName().toLowerCase();

      Integer id;
      StringBuilder selectIdString = new StringBuilder();
      selectIdString.append("select \"").append(entityClassName).append("Id\" from ").append(entityClassName).append(" order by \"").append(entityClassName).append("Id\" desc limit 1;");
      ResultSet selectIdResults = getResults(selectIdString.toString());
      if (selectIdResults.next()) {
         id = selectIdResults.getInt(1) + 1;
      } else {
         id = 1;
      }
      entity.setId(id);

      StringBuilder sql = new StringBuilder().append("insert into ");
      sql.append(entityClassName).append(" (");

      try {
         boolean isFirst = true;
         ArrayList<Field> fields = new ArrayList(Arrays.asList(entity.getClass().getDeclaredFields()));
         fields.addAll(Arrays.asList(Entity.class.getDeclaredFields()));
         for (Field field : fields) {
            if (isFirst) {
               isFirst = false;
            } else {
               sql.append("\", ");
            }
            sql.append("\"");
            sql.append(field.getName());
         }
         sql.append("\") values (");
         isFirst = true;
         for (Field field : fields) {
            if (isFirst) {
               isFirst = false;
            } else {
               sql.append(", ");
            }
            sql.append("?");
         }
         sql.append(");");
         PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql.toString());
         int colNum = 1;
         for (Field field : fields) {
            Object fieldValue = new PropertyDescriptor(field.getName(), entity.getClass()).getReadMethod().invoke(entity);
            if (field.getType().equals(Integer.class)) {
               preparedStatement.setInt(colNum++, (Integer) fieldValue);
            }
            if (field.getType().equals(ZonedDateTime.class)) {
               ZonedDateTime utcZonedDateTime;
               if (!(fieldValue instanceof ZonedDateTime)) {
                  utcZonedDateTime = ZonedDateTime.now();
               } else {
                  utcZonedDateTime = (ZonedDateTime) fieldValue;
               }
               preparedStatement.setTimestamp(colNum++, Timestamp.valueOf(utcZonedDateTime.toLocalDateTime()));
            }
            if (field.getType().equals(String.class)) {
               preparedStatement.setString(colNum++, (String) fieldValue);
            }
         }
         preparedStatement.execute();
         return preparedStatement.getUpdateCount();
      } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
         Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, "Reflection error in generating SQL for create.", ex);
         return -1;
      }
   }

   public static int update(Entity entity) throws SQLException {
      StringBuilder sql = new StringBuilder();
      PreparedStatement preparedStatement;
      try {
         String entityClassName = entity.getClass().getSimpleName().toLowerCase();
         sql.append("update ").append(entityClassName).append(" set ");
         boolean isFirst = true;
         ArrayList<Field> fields = new ArrayList(Arrays.asList(entity.getClass().getDeclaredFields()));
         fields.addAll(Arrays.asList(Entity.class.getDeclaredFields()));
         int colNum = 1;
         // Build SQL String from field names
         for (Field field : fields) {
            if (isFirst) {
               isFirst = false;
            } else {
               sql.append(", ");
            }
            sql.append("\"");
            sql.append(field.getName()).append("\"=?");
         }
         sql.append(" where \"").append(entityClassName).append("Id\"=");
         sql.append(new PropertyDescriptor(entityClassName + "Id", entity.getClass()).getReadMethod().invoke(entity));
         sql.append(";");
         preparedStatement = DBConnection.getConnection().prepareStatement(sql.toString());
         // Set values on preparedStatement for each field
         for (Field field : fields) {
            Object fieldValue = new PropertyDescriptor(field.getName(), entity.getClass()).getReadMethod().invoke(entity);
            if (field.getType().equals(ZonedDateTime.class)) {
               ZonedDateTime utcZonedDateTime;
               if (!(fieldValue instanceof ZonedDateTime)) {
                  utcZonedDateTime = ZonedDateTime.now();
               } else {
                  utcZonedDateTime = (ZonedDateTime) fieldValue;
               }
               preparedStatement.setTimestamp(colNum++, Timestamp.valueOf(utcZonedDateTime.toLocalDateTime()));
            }
            if (field.getType().equals(Integer.class)) {
               preparedStatement.setInt(colNum++, (Integer) fieldValue);
            }
            if (field.getType().equals(String.class)) {
               preparedStatement.setString(colNum++, (String) fieldValue);
            }
         }
      } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
         Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, "Reflection error in generating SQL for update.", ex);
         return -1;
      }
      preparedStatement.execute();
      return preparedStatement.getUpdateCount();
   }

   public static int deleteEntitiesForTypeId(String classForItemsToDelete, String classForTypeId, Integer entityId) throws SQLException {
      try {
         Statement sqlStatement = DBConnection.getConnection().createStatement();
         StringBuilder sql = new StringBuilder();

         sql.append("delete from ").append(classForItemsToDelete).append(" where \"").append(classForTypeId).append("Id\"=");
         sql.append(entityId.toString());
         sql.append(";");
         if (!sqlStatement.execute(sql.toString())) {
            return sqlStatement.getUpdateCount();
         } else {
            System.err.println("Error in SQL delete.");
            return -1;
         }
      } catch (IllegalArgumentException ex) {
         Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, "Reflection error in generating SQL for delete.", ex);
         return -1;
      }
   }

   public static int delete(Entity entity) throws SQLException {
      try {
         Statement sqlStatement = DBConnection.getConnection().createStatement();
         StringBuilder sql = new StringBuilder();

         String entityClassName = entity.getClass().getSimpleName().toLowerCase();
         sql.append("delete from ").append(entityClassName).append(" where \"").append(entityClassName).append("Id\"=");
         sql.append(new PropertyDescriptor(entityClassName + "Id", entity.getClass()).getReadMethod().invoke(entity));
         sql.append(";");
         if (!sqlStatement.execute(sql.toString())) {
            return sqlStatement.getUpdateCount();
         } else {
            System.err.println("Error in SQL delete.");
            return -1;
         }
      } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
         Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, "Reflection error in generating SQL for delete.", ex);
         return -1;
      }
   }

   public static ArrayList get(String className) throws SQLException {
      return get(className, -1);
   }

   public static ArrayList get(String className, Integer id) throws SQLException {
      List<Integer> ids = new ArrayList<>();
      ids.add(id);
      return get(className, ids);
   }

   public static ArrayList getAppointments(Integer userId) throws SQLException {
      String className = Appointment.class.getSimpleName().toLowerCase();
      String whereClause = " where \"userId\"=" + userId;

      return get(className, whereClause);
   }
   
   public static ArrayList getAppointments(String appointmentType) throws SQLException {
      String className = Appointment.class.getSimpleName().toLowerCase();
      String whereClause = " where \"type\"='" + appointmentType + "'";

      return get(className, whereClause);
   }

   public static ArrayList getAppointments(Integer userId, ZonedDateTime start, ZonedDateTime end) throws SQLException {
      String className = Appointment.class.getSimpleName().toLowerCase();
      String whereClause = " where \"userId\"=" + userId + " and \"start\" > '" + Timestamp.valueOf(start.toLocalDateTime()) + "' and \"start\" < '" + Timestamp.valueOf(end.toLocalDateTime()) + "'";

      return get(className, whereClause);
   }

   public static ArrayList getAppointments(ZonedDateTime start, ZonedDateTime end) throws SQLException {
      String className = Appointment.class.getSimpleName().toLowerCase();
      String whereClause = " where \"start\" > '" + Timestamp.valueOf(start.toLocalDateTime()) + "' and \"start\" < '" + Timestamp.valueOf(end.toLocalDateTime()) + "'";

      return get(className, whereClause);
   }

   public static ArrayList getAppointments(Integer userId, String appointmentType) throws SQLException {
      String className = Appointment.class.getSimpleName().toLowerCase();
      String whereClause = " where \"userId\"=" + userId + " and \"type\"='" + appointmentType + "'";

      return get(className, whereClause);
   }

   public static ArrayList getAppointments(Integer userId, ZonedDateTime start, ZonedDateTime end, String appointmentType) throws SQLException {
      String className = Appointment.class.getSimpleName().toLowerCase();
      String whereClause = " where \"userId\"=" + userId + " and \"start\" > '" + Timestamp.valueOf(start.toLocalDateTime()) + "' and \"start\" < '" + Timestamp.valueOf(end.toLocalDateTime()) + "'" + " and \"type\"='" + appointmentType + "'";

      return get(className, whereClause);
   }

   public static ArrayList getAppointments(ZonedDateTime start, ZonedDateTime end, String appointmentType) throws SQLException {
      String className = Appointment.class.getSimpleName().toLowerCase();
      String whereClause = " where \"start\" > '" + Timestamp.valueOf(start.toLocalDateTime()) + "' and \"start\" < '" + Timestamp.valueOf(end.toLocalDateTime()) + "'" + " and \"type\"='" + appointmentType + "'";

      return get(className, whereClause);
   }

   public static ArrayList findOverlappingAppointments(Appointment appointment) throws SQLException {
      String className = Appointment.class.getSimpleName().toLowerCase();
      String whereClause = " where (\"userId\"=" + appointment.getUserId() + " or \"customerId\"=" + appointment.getCustomerId() + ")"
              + " and \"appointmentId\" != " + appointment.getId()
              + " and ((\"start\" >= '" + Timestamp.valueOf(appointment.getStart().toLocalDateTime()) + "'"
              + " and \"start\" < '" + Timestamp.valueOf(appointment.getEnd().toLocalDateTime()) + "')"
              + " or (\"end\" > '" + Timestamp.valueOf(appointment.getStart().toLocalDateTime()) + "'"
              + " and \"end\" <= '" + Timestamp.valueOf(appointment.getEnd().toLocalDateTime()) + "'));";

      return get(className, whereClause);
   }

   public static ArrayList get(String className, List<Integer> ids) throws SQLException {
      String whereClause = "";
      if (ids.size() > 0 && !ids.get(0).equals(-1)) {
         whereClause = " where \"" + className + "Id\" in (";
         boolean isFirst = true;
         for (Integer id : ids) {
            if (isFirst) {
               isFirst = false;
            } else {
               whereClause += ", ";
            }
            whereClause += id.toString();
         }
         whereClause += ")";
      }
      return get(className, whereClause);
   }

   public static ArrayList get(String className, String whereClause) throws SQLException {
      ArrayList<Entity> list = new ArrayList();

      ResultSet results = getResults("select * from " + className + whereClause);
      ResultSetMetaData metadata = results.getMetaData();
      try {
         Constructor constructor = Class.forName("bsa.model."
                 + className.substring(0, 1).toUpperCase() + className.substring(1)).getConstructor();
         while (results.next()) {
            Entity nextItem = (Entity) constructor.newInstance();
            for (int colNum = 1; colNum < metadata.getColumnCount() + 1; colNum++) {
               String colName = metadata.getColumnName(colNum);
               String colType = metadata.getColumnTypeName(colNum);
               Method setter = new PropertyDescriptor(colName, nextItem.getClass()).getWriteMethod();
               Object colValue = null;
               if (colType.equals("DATETIME") || colType.equals("TIMESTAMP")) {
                  Timestamp timestamp = results.getTimestamp(colNum);
                  colValue = ZonedDateTime.ofLocal(
                          timestamp.toLocalDateTime(),
                          ZoneId.of("GMT"),
                          ZoneOffset.UTC);
               }
               if (colType.equals("VARCHAR")) {
                  colValue = results.getString(colNum);
               }
               if (colType.equals("INTEGER") || colType.equals("TINYINT")) {
                  colValue = results.getInt(colNum);
               }
               setter.invoke(nextItem, colValue);
            }
            list.add(nextItem);
         }
      } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException ex) {
         Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, "Reflection error in generating SQL for select.", ex);
      }
      return list;
   }

   public static User login(String loginUserName, String loginPassword) {
      try {
         PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(
                 "select \"userId\", \"userName\", \"password\", \"active\" from user "
                 + "where \"userName\"=? and \"password\"=?;");
         preparedStatement.setString(1, loginUserName);
         preparedStatement.setString(2, loginPassword);
         ResultSet results = preparedStatement.executeQuery();
         while (results.next()) {
            Integer userId = results.getInt(1);
            String userName = results.getString(2);
            String password = results.getString(3);
            Integer active = results.getInt(4);
            if (loginUserName.equals(userName) && loginPassword.equals(password)) {
               return new User(userId, userName, password, active);
            }
         }
      } catch (SQLException ex) {
         Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, "Error Logging In", ex);
      }
      return null;
   }

   public static ResultSet getResults(String sql) throws SQLException {
      Statement sqlStatement = DBConnection.getConnection().createStatement();
      if (sqlStatement.execute(sql)) {
         return sqlStatement.getResultSet();
      } else {
         throw new SQLException("No results for query:" + sql);
      }
   }
}
