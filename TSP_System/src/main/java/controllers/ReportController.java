package controllers;

import dialogs.Dialog;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jdk.nashorn.internal.objects.annotations.Function;
import utils.UtilsConnection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class ReportController{

    @FXML
    public TextFlow dataContainer;

    @FXML
    public AnchorPane toClose;

    @Function
    public void setText(Double time, Double distance, ArrayList<RouteProperties> list) {
        if(list.size() >= 1) {
            DecimalFormat value = new DecimalFormat("#.00");
            for (RouteProperties x : list) {
                if (time >= 3600) {
                    String propTime = value.format(x.getTime() / 3600);
                    String propDistance = value.format(x.getDistance());
                    Text text = new Text();
                    text.setText("Time requaired from: " + x.getNodeA() + " to " + x.getNodeB() + ": \n" +
                            "   " + propTime + " hours. \n" +
                            "\n" +
                            "Distance requaired from: " + x.getNodeA() + " to " + x.getNodeB() + ": \n" +
                            "   " + propDistance + " km. \n" +
                            "\n");
                    dataContainer.getChildren().add(text);
                } else {
                    String propTime = value.format(x.getTime() / 60);
                    String propDistance = value.format(x.getDistance());
                    Text text = new Text();
                    text.setText("Time requaired from: " + x.getNodeA() + " to " + x.getNodeB() + ": \n" +
                            "   " + propTime + " minutes. \n" +
                            "\n" +
                            "Distance requaired from: " + x.getNodeA() + " to " + x.getNodeB() + ": \n" +
                            "   " + propDistance + " km. \n" +
                            "\n");
                    dataContainer.getChildren().add(text);
                }
            }
            if (time >= 3600) {
                Text text = new Text();
                String propTotalTime = value.format(time / 3600);
                String propTotalDistance = value.format(distance);
                text.setText("Total time for determined route: \n" +
                        "   " + propTotalTime + " hours. \n" +
                        "\n" +
                        "Total distance for determined route: \n" +
                        "   " + propTotalDistance + " km. \n" +
                        "\n");
                dataContainer.getChildren().add(text);
            } else {
                Text text = new Text();
                String propTotalTime = value.format(time / 60);
                String propTotalDistance = value.format(distance);
                text.setText("Total time for determined route: \n" +
                        "   " + propTotalTime + " minutes. \n" +
                        "\n" +
                        "Total distance for determined route: \n" +
                        "   " + propTotalDistance + " km. \n" +
                        "\n");
                dataContainer.getChildren().add(text);
            }
        }else{
            Text text = new Text("No routes determinated");
            dataContainer.getChildren().add(text);
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
