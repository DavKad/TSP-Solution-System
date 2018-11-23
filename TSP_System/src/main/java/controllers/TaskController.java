package controllers;

import dialogs.Dialog;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import jdk.nashorn.internal.objects.annotations.Function;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Property;
import utils.UtilsConnection;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskController implements Initializable {

    @FXML
    public TextArea taskText;
    @FXML
    public Button cancelButton;
    @FXML
    public Button saveButton;
    @FXML
    public Label localization;

    String getLocalization() {
        return localization.getText();
    }

    /*Callback to Main Controller*/
    @Property
    private ReadOnlyObjectWrapper<String> taskStatus = new ReadOnlyObjectWrapper<>();

    @Getter
    String getTask() {
        return taskStatus.get();
    }

    @Function
    ReadOnlyObjectProperty<String> getTaskStatusProperty() {
        return taskStatus.getReadOnlyProperty();
    }

    @Function
    public void setText(String location) {
        localization.setText(location);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskText.setWrapText(true);
        saveButton.setOnAction(s -> {
            if (taskText.getText() == null || taskText.getText().trim().isEmpty()) {
                Dialog.error(UtilsConnection.getBundles().getString("emptyTask.task"));
            } else {
                if (taskText.getText().length() < 80) {
                    taskStatus.set(taskText.getText());
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();
                } else {
                    Dialog.error(UtilsConnection.getBundles().getString("toMany.task"));
                }
            }

        });

        cancelButton.setOnAction(s -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }
}
