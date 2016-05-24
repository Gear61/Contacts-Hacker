package com.randomappsinc.contactshacker.Models;

/**
 * Created by alexanderchiou on 5/23/16.
 */
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
