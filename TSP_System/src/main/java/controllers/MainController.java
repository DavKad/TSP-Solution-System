package controllers;

import dialogs.Dialog;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jdk.nashorn.internal.objects.annotations.Function;
import properties.SearchProperties;
import utils.UtilsConnection;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public WebView webView;

    @FXML
    public TextField endField;

    @FXML
    public TextField fromField;

    @FXML
    public VBox addFieldContainer;

    //Variables to load map page and properties management
    private SearchProperties searchProperties = new SearchProperties();
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
                ,UtilsConnection.getBundles().getString("headerAbout.app")
                ,UtilsConnection.getBundles().getString("aboutTitle.app"));
    }

    @Function
    public void setLocation() {
        String location = fromField.getText();
        //Running JS function in very DIRTY WAY!!!!
        try {
            webEngine.executeScript(
                    "geocoder.query('"+location+"', showMap);\n" +
                            "function showMap(err, data) {\n" +
                            "    if (data.lbounds) {\n" +
                            "        map.fitBounds(data.lbounds);\n" +
                            "    } else if (data.latlng) {\n" +
                            "        map.setView([data.latlng[0], data.latlng[1]], 13);\n" +
                            "        L.marker([data.latlng[0], data.latlng[1]]).addTo(map)" +
                            "    }\n" +
                            "}"
            );
        } catch (Exception e) {
            Dialog.error(UtilsConnection.getBundles().getString("error.map"));
        }
    }

    @Function
    private void addNewWayPoint(TextField nextField) {
            nextField.setOnKeyReleased(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    //Add new field and image
                    HBox addPointContainer = new HBox();
                    ImageView plus = new ImageView("/images/plus.png");
                    TextField addPoint = new TextField();
                    addPoint.setPromptText(UtilsConnection.getBundles().getString("addPoint.search"));
                    addPointContainer.setAlignment(Pos.CENTER);
                    addPointContainer.setSpacing(5);
                    addPointContainer.getChildren().add(plus);
                    addPointContainer.getChildren().add(addPoint);
                    addFieldContainer.setSpacing(5);
                    addFieldContainer.getChildren().add(addPointContainer);

                    String location = nextField.getText();
                    //Running JS function in very DIRTY WAY!!!!
                    try {
                        webEngine.executeScript(
                                        "geocoder.query('"+location+"', showMap);\n" +
                                                "function showMap(err, data) {\n" +
                                                "    if (data.lbounds) {\n" +
                                                "        map.fitBounds(data.lbounds);\n" +
                                                "    } else if (data.latlng) {\n" +
                                                "        map.setView([data.latlng[0], data.latlng[1]]).setZoom(18);\n" +
                                                "        L.marker([data.latlng[0], data.latlng[1]]).addTo(map)" +
                                                "    }\n" +
                                                "}"
                        );
                    } catch (Exception e) {
                        Dialog.error(UtilsConnection.getBundles().getString("error.map"));
                    }
                    //TODO Run routing algorithm
                    addNewWayPoint(addPoint);
                }
            });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Loading map through HTML file
        try {
            URL url = this.getClass().getResource("/html/anotherMap.html");
            webEngine = webView.getEngine();
            webEngine.setJavaScriptEnabled(true);
            webEngine.load(url.toString());
        } catch (Exception e) {
            Dialog.error(UtilsConnection.getBundles().getString("error.map"));
        } finally {
            webEngine.reload();
        }

        //Binding
        endField.textProperty().bindBidirectional(searchProperties.getEndProperties());
        fromField.textProperty().bindBidirectional(searchProperties.getFromProperties());
        addNewWayPoint(endField);
    }


}

