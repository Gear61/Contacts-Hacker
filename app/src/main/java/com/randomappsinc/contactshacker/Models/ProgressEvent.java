package com.randomappsinc.contactshacker.Models;

/**
 * Created by alexanderchiou on 5/23/16.
 */
public class ProgressEvent {
    public static final String SET_MAX = "setMax";
    public static final String INCREMENT = "increment";
    public static final String FINISHED = "finished";

    private String screen;
    private String eventType;
    private int total;

    public ProgressEvent(String screen, String eventType, int total) {
        this.screen = screen;
        this.eventType = eventType;
        this.total = total;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
