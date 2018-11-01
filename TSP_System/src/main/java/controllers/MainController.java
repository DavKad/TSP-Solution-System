package controllers;

import dialogs.Dialog;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;
import utils.UtilsConnection;
import javafx.scene.control.ListView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jdk.nashorn.internal.objects.annotations.Function;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public WebView webView;

    @FXML
    private ListView<String> listOfInterest;

    //Variables to load map page
    private WebEngine webEngine;

    /*MenuBar methods*/
    @Function
    public void closeApplication() {
        Optional<ButtonType> confirmationResult = Dialog.closeApplicationConfirmation();
        if (confirmationResult.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    @Function
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

    @Function
    public void openAbout() {
        Dialog.informationAlert(UtilsConnection.getBundles().getString("textAbout.app")
                , UtilsConnection.getBundles().getString("headerAbout.app")
                , UtilsConnection.getBundles().getString("aboutTitle.app"));
    }

    @Function
    private void setFunctionHandlerInHTML(WebEngine engine){
        JSObject window = (JSObject) engine.executeScript("window");
        window.setMember("app", this);
    }

    @Function
    public void getLocation(String location){
        listOfInterest.getItems().add(location);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Loading map through HTML file
        try {
            URL url = this.getClass().getResource("/html/mainMap.html");
            webEngine = webView.getEngine();
            webEngine.setJavaScriptEnabled(true);
            webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue == Worker.State.SUCCEEDED){
                    setFunctionHandlerInHTML(webEngine);
                }
            });
            webEngine.load(url.toString());
        } catch (Exception e) {
            Dialog.error(UtilsConnection.getBundles().getString("error.map"));
        } finally {
            webEngine.reload();
        }
    }
}

