package controllers;

import dialogs.Dialog;
import javafx.fxml.FXML;
import jdk.nashorn.internal.objects.annotations.Function;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import utils.UtilsConnection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReportController {

    @FXML
    public TextFlow dataContainer;

    @FXML
    public AnchorPane toClose;

    @Function
    public String timeFormat(long totalSecs){
        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        long seconds = totalSecs % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Function
    public void setText(long time, Double distance, ArrayList<RouteProperties> list) {
        DecimalFormat value = new DecimalFormat("#.00");
        long setTimeValue;
        double setDistanceValue;
        if (list.size() < 1) {
            Text text = new Text();
            text.setText("No routes determined.");
            dataContainer.getChildren().add(text);
        } else {
            if (list.size() == 1) {
                String propDistance = value.format(list.get(0).getDistance());
                Text text = new Text();
                text.setText("Total time and distance for the determined route:\n" +
                        "   -> " + timeFormat(list.get(0).getTime()) + " \n" +
                        "   -> " + propDistance + " km." +
                        "\n");
                dataContainer.getChildren().add(text);
            }

            if (list.size() > 1) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        String propDistance = value.format(list.get(i).getDistance());
                        Text text = new Text();
                        text.setText("Time and distance required from: " + list.get(i).getNodeA() + " to " + list.get(i).getNodeB() + ": \n" +
                                "   -> " + timeFormat(list.get(i).getTime()) + "\n" +
                                "   -> " + propDistance + " km." +
                                "\n");
                        dataContainer.getChildren().add(text);
                    }
                    if (i >= 1) {
                        setTimeValue = list.get(i).getTime() - list.get(i - 1).getTime();
                        setDistanceValue = list.get(i).getDistance() - list.get(i - 1).getDistance();
                        String propDistance = value.format(setDistanceValue);
                        Text text = new Text();
                        text.setText("Time and distance required from: " + list.get(i).getNodeA() + " to " + list.get(i).getNodeB() + ": \n" +
                                "  -> " + timeFormat(setTimeValue) + " \n" +
                                "  ->  " + propDistance + " km." +
                                "\n");
                        dataContainer.getChildren().add(text);
                    }
                }
                String wellDistance = value.format(distance);
                Text totalData = new Text();
                totalData.setText("Total time and distance for the determined route:\n" +
                        "   -> " + timeFormat(time) +" \n" +
                        "   -> " + wellDistance + " km." +
                        "\n");
                dataContainer.getChildren().add(totalData);
            }
        }
    }

    @Function
    public void openAbout() {
        Dialog.informationAlert(UtilsConnection.getBundles().getString("textAbout.app")
                , UtilsConnection.getBundles().getString("headerAbout.app")
                , UtilsConnection.getBundles().getString("aboutTitle.app"));
    }

    @Function
    public void saveReport() {
        StringBuilder content = new StringBuilder();
        for (Node x : dataContainer.getChildren()) {
            if (x instanceof Text) {
                content.append(((Text) x).getText());
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Report.txt")));
            writer.write(content.toString());
            writer.close();
            Dialog.savedFile();
        } catch (IOException e) {
            Dialog.error(e.getMessage());
        }
    }
}
