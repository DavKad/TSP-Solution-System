package controllers;

import dialogs.Dialog;
import jdk.nashorn.internal.objects.annotations.Function;
import utils.UtilsConnection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReportController {

    @Function
    public void openAbout() {
        Dialog.informationAlert(UtilsConnection.getBundles().getString("textAbout.app")
                ,UtilsConnection.getBundles().getString("headerAbout.app")
                ,UtilsConnection.getBundles().getString("aboutTitle.app"));
    }

    @Function
    public void saveReport() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Report.pdf")));
            //TODO Get a data from Node.
            writer.close();
            Dialog.savedFile();
        } catch (IOException e) {
            Dialog.error(e.getMessage());
        }
    }
}
