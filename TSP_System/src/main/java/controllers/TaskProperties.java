package controllers;

import jdk.nashorn.internal.objects.annotations.Getter;

public class TaskProperties {

    private String localization;
    private String description;

    TaskProperties(String localization, String description) {
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
