package controllers;

import dialogs.Dialog;
import javafx.scene.web.WebView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.UtilsConnection;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public WebView webView;

    /*MenuBar methods*/
    public void closeApplication() {
        Optional<ButtonType> confirmationResult = Dialog.closeApplicationConfirmation();
        if (confirmationResult.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    public void openReport() {
        try {
            AnchorPane anchorPane = UtilsConnection.getFXML("/FXML/ReportWindow.fxml");
            Scene scene = new Scene(anchorPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(UtilsConnection.getBundles().getString("title.rep"));
            stage.getIcons().add(new Image("images/logo.png"));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openAbout() {
        Dialog.aboutApplication();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            URL url = this.getClass().getResource("/html/map.html");
            webView.getEngine().load(url.toString());
        } catch (Exception e) {
            Dialog.error(e.getMessage());
        }
    }
}

