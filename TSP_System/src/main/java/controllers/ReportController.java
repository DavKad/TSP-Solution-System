package controllers;

import dialogs.Dialog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReportController {


    public void openAbout() {
        Dialog.aboutApplication();
    }

    public void saveReport() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Report.txt")));
            //TODO Get a data from Node.
            writer.close();
            Dialog.savedFile();
        } catch (IOException e) {
            Dialog.error(e.getMessage());
        }
    }
}
