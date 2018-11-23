package controllers;

import jdk.nashorn.internal.objects.annotations.Getter;

public class TaskProperty {

    private String localization;
    private String description;

    TaskProperty(String localization, String description) {
        this.localization = localization;
        this.description = description;
    }

    @Getter
    public String getLocalization() {
        return localization;
    }

    @Getter
    public String getDescription() {
        return description;
    }
}
