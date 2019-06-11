package com.tabjin.permission;

public class Permission {
    private boolean granted,showReason;

    private String name;

    public Permission(boolean granted, boolean showReason, String name) {
        this.granted = granted;
        this.showReason = showReason;
        this.name = name;
    }


}
