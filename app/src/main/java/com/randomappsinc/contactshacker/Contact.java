package com.randomappsinc.contactshacker;

/**
 * Created by alexanderchiou on 5/22/16.
 */
public class Contact {
    private String id;
    private String displayName;

    public Contact(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
