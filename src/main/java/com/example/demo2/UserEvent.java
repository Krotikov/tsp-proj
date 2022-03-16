package com.example.demo2;

import javafx.event.Event;
import javafx.event.EventType;

public class UserEvent extends Event {

    public static final EventType<UserEvent> ANY = new EventType<>(Event.ANY,"ANY");
    public static final EventType<UserEvent> LOGIN_SUCCEEDED = new EventType<>(ANY,"LOGIN_SUCCEEDED");
    public static final EventType<UserEvent> LOGIN_FILED = new EventType<>(ANY,"LOGIN_FILED");

    public UserEvent(EventType<? extends Event> eventType){
        super(eventType);
    }
}
