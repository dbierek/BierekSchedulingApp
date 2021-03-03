package bsa.controller;

import bsa.model.User;
import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author dbierek
 */
public class LoginEvent extends Event {
   public static EventType<LoginEvent> ATTEMPT_LOGIN_EVENT_TYPE =
           new EventType<>("ATTEMPT_LOGIN_EVENT");
   public static EventType<LoginEvent> LOGIN_EVENT_TYPE =
           new EventType<>("LOGIN_EVENT");
   public static EventType<LoginEvent> LOGIN_FAILED_EVENT_TYPE =
           new EventType<>("LOGIN_FAILED_EVENT");
   private final User user;
   

   LoginEvent(EventType e, User user) {
      super(e);
      this.user = user;
   }
   LoginEvent(EventType e, String userName, String password) {
      super(e);
      user = new User();
      user.setUserName(userName);
      user.setPassword(password);
   }
   public User getUser() {
      return user;
   }
}
