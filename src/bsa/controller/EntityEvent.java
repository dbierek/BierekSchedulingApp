package bsa.controller;

import javafx.event.Event;
import javafx.event.EventType;
/**
 *
 * @author dbierek
 */
public class EntityEvent extends Event {
   public static EventType<EntityEvent> ADD_EVENT_TYPE =
           new EventType("ADD_EVENT");
   public static EventType<EntityEvent> EDIT_EVENT_TYPE =
           new EventType("EDIT_EVENT");
   public static EventType<EntityEvent> VALIDATE_EVENT_TYPE =
           new EventType("VALIDATE_EVENT");
   EntityEvent(EventType eventType) {
      super(eventType);
   }
}
