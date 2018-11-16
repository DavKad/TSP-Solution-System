package controllers;

import dialogs.Dialog;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import jdk.nashorn.internal.objects.annotations.Getter;
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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public WebView webView;

    @FXML
    public Button clearButton;

    @FXML
    private ListView<String> listOfInterest;

    //Variables to load map page
    private WebEngine webEngine;
    private ArrayList<RouteProperties> listOfData = new ArrayList<>();
    private Double time;
    private Double distance;

    @Getter
    private Double getTime() {
        return time;
    }

    @Getter
    private Double getDistance() {
        return distance;
    }

    @Function
    public void setRouteProperties(String time, Double distance, String nodeA, String nodeB) {
        this.time = Double.parseDouble(time);
        this.distance = distance / 1000;
        listOfData.add(new RouteProperties(this.getTime(), this.getDistance(), nodeA, nodeB));
    }

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
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/FXML/ReportWindow.fxml"));
            loader.setResources(ResourceBundle.getBundle("bundles/messages"));
            AnchorPane anchorPane = loader.load();
            ReportController passData = loader.getController();
            passData.setText(this.getTime(), this.getDistance(), listOfData);
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

    //Access to JavaScript code inside HTML.
    @Function
    private void setFunctionHandlerInHTML(WebEngine engine, String name, Object object) {
        JSObject window = (JSObject) engine.executeScript("window");
        window.setMember(name, object);
    }

    @Function
    public void getLocation(String location) {
        listOfInterest.getItems().add(location);
    }

    @Function
    public void getAddress(String address) {
        listOfInterest.getItems().add(address);
    }

    @Function
    public void removeLocation(String location) {
        listOfInterest.getItems().remove(location);
        listOfData.removeIf(s -> s.getNodeA().equals(location) | s.getNodeB().equals(location));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Loading map through HTML file
        try {
            URL url = this.getClass().getResource("/html/map.html");
            webEngine = webView.getEngine();
            webEngine.setJavaScriptEnabled(true);
            webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    setFunctionHandlerInHTML(webEngine, "app", this);
                }
            });
            webEngine.load(url.toString());
        } catch (Exception e) {
            Dialog.error(UtilsConnection.getBundles().getString("error.map"));
        } finally {
            listOfInterest.getItems().clear();
            webEngine.reload();
        }
        clearButton.setOnAction(event -> {
            webEngine.executeScript("control.spliceWaypoints(0, control.getWaypoints().length);" +
                    "routingArray.splice(0, routingArray.length);" +
                    "for(var i = 0; i < markers.length; i++){" +
                    "map.removeLayer(markers[i]);" +
                    "}" +
                    "markers.splice(0, markers.length);"
            );
            listOfInterest.getItems().clear();
            listOfData.clear();
        });
    }
}

