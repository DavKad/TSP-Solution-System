package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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


    private WebEngine engine;

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
            //table.getItems().removeAll(table.getSelectionModel().getSelectedItem());
        });
        showButton.setOnAction(event -> {
            engine = this.getEngine();
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
            engine.executeScript("map.setView([51.125736, 17.080392], 11);");
        });
    }
}
