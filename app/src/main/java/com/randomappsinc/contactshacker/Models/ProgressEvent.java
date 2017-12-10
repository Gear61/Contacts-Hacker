package com.randomappsinc.contactshacker.Models;

public class ProgressEvent {

    public static final String SET_MAX = "setMax";
    public static final String INCREMENT = "increment";

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

    public int getTotal() {
        return total;
    }
}
