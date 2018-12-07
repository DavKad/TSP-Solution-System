package controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import jdk.nashorn.internal.objects.annotations.Function;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskViewController implements Initializable {

    @FXML
    public Button showButton;

    @FXML
    public Button deleteButton;

    @FXML
    private TextFlow local;

    @FXML
    private TextFlow description;

    @FXML
    private Button closeButton;

    /*Data to execute in main window*/
    private WebEngine engine;
    private ReadOnlyObjectWrapper<String> toRemove = new ReadOnlyObjectWrapper<>();

    @Setter
    Object getToRemove() {
        return toRemove.get();
    }

    @Setter
    ReadOnlyObjectWrapper toRemoveProperty() {
        return toRemove;
    }

    @Setter
    void setEngine(WebEngine engine) {
        this.engine = engine;
    }

    @Getter
    private WebEngine getEngine() {
        return engine;
    }

    @Function
    public void setContent(String localization, String desc) {
        Text place = new Text(localization);
        Text toDescription = new Text(desc);
        local.getChildren().add(place);
        description.getChildren().add(toDescription);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeButton.setOnAction(close -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
        deleteButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
            StringBuilder value = new StringBuilder();
            for(Node x : local.getChildren()){
                value.append(((Text) x).getText());
            }
            toRemove.set(value.toString());
        });
        showButton.setOnAction(event -> {
            engine = this.getEngine();
            StringBuilder value = new StringBuilder();
            for (Node x : local.getChildren()) {
                if (x instanceof Text) {
                    value.append(((Text) x).getText());
                }
            }
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
            engine.executeScript(" for(var i = 0; i<markers.length; i++){\n" +
                    "        if(markers[i].options.id === "+"'"+ value + "'" +" ){\n" +
                    "            map.setView([markers[i].getLatLng().lat, markers[i].getLatLng().lng], 11);\n" +
                    "        }\n" +
                    "    }"
            );
        });
    }
}
