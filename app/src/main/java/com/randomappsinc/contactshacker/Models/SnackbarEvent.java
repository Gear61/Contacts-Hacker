package com.randomappsinc.contactshacker.Models;

public class SnackbarEvent {

    private String screen;
    private String message;

    public SnackbarEvent(String screen, String message) {
        this.screen = screen;
        this.message = message;
    }

    public String getScreen() {
        return screen;
    }

    public String getMessage() {
        return message;
    }
}
