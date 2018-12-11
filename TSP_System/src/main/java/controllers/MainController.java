package controllers;

import dialogs.Dialog;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import jdk.nashorn.internal.objects.annotations.Getter;
import netscape.javascript.JSObject;
import utils.UtilsConnection;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jdk.nashorn.internal.objects.annotations.Function;
import javafx.fxml.FXML;

import java.io.File;
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
    public BorderPane borderPane;

    @FXML
    private TableView<TaskProperties> table;

    @FXML
    public TableColumn<TaskProperties, String> localization = new TableColumn<>();

    @FXML
    public TableColumn<TaskProperties, String> description = new TableColumn<>();
    private ObservableList<TaskProperties> taskList = FXCollections.observableArrayList();

    @FXML
    private ListView<String> listOfInterest;

    //Variables to load map page
    private WebEngine webEngine;
    private ArrayList<RouteProperties> getRouteData = new ArrayList<>();
    private long time = 0;
    private Double distance = 0.0;
    private String NodeA;
    private String NodeB;
    private String nameOfPlace;

    @Getter
    private String getNameOfPlace() {
        return nameOfPlace;
    }

    @Getter
    private String getNodeA() {
        return NodeA;
    }

    @Getter
    private String getNodeB() {
        return NodeB;
    }

    @Getter
    private long getTime() {
        return time;
    }

    @Getter
    private Double getDistance() {
        return distance;
    }

    /*Extract data from JS*/
    @Function
    public void printFoo(String loc){
        this.nameOfPlace = loc;
    }

    @Function
    public void removeRouteSegment(String localization) {
        getRouteData.removeIf(s -> s.getNodeA().equals(localization) || s.getNodeB().equals(localization));
    }

    @Function
    public void setRouteProperties(String time, Double distance, String nodeA, String nodeB) {
        Double dTime = Double.parseDouble(time);
        this.time = dTime.longValue();
        this.distance = distance / 1000;
        this.NodeA = nodeA;
        this.NodeB = nodeB;
        getRouteData.add(new RouteProperties(this.getTime(), this.getDistance(), this.getNodeA(), this.getNodeB()));
    }

    @Function
    public void openTask(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/FXML/TaskWindow.fxml"));
            loader.setResources(UtilsConnection.getBundles());
            SplitPane splitPane = loader.load();
            TaskController passData = loader.getController();
            passData.setText(this.getNameOfPlace());
            passData.getTaskStatusProperty().addListener(s -> {
                taskList.add(new TaskProperties(passData.getLocalization(), passData.getTask()));
                table.setItems(taskList);
            });
            Scene scene = new Scene(splitPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.getIcons().add(new Image("images/logo.png"));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            Dialog.error(UtilsConnection.getBundles().getString("error.task") + e.getMessage());
        }
    }

    @Function
    public void getLocation(String location) {
        listOfInterest.getItems().add(location);
    }

    @Function
    public void removeLocation(String location) {
        listOfInterest.getItems().remove(location);
        if(!(table.getItems() ==null)){
            for(int i = 0; i <table.getItems().size(); i++){
                TaskProperties x = table.getItems().get(i);
                if(x.getLocalization().equals(location)){
                    table.getItems().remove(x);
                }
            }
        }
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
            loader.setResources(UtilsConnection.getBundles());
            AnchorPane anchorPane = loader.load();
            ReportController passData = loader.getController();
            passData.setText(this.getTime(), this.getDistance(), getRouteData);
            Scene scene = new Scene(anchorPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(UtilsConnection.getBundles().getString("title.rep"));
            stage.getIcons().add(new Image("images/logo.png"));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            Dialog.error(UtilsConnection.getBundles().getString("reportError.rep") + e.getMessage());
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Loading map through HTML file
        try {
            webEngine = webView.getEngine();
            webEngine.setJavaScriptEnabled(true);
            webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    setFunctionHandlerInHTML(webEngine, "app", this);
                    borderPane.setDisable(false);
                }
            });
            webEngine.load(MainController.class.getResource("/html/map.html").toString());
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
                    "markers.splice(0, markers.length);" +
                    "removedMarker = undefined;"
            );
            listOfInterest.getItems().clear();
            table.getItems().clear();
            getRouteData.clear();
        });

        localization.setCellValueFactory(new PropertyValueFactory<>("Localization"));
        description.setCellValueFactory(new PropertyValueFactory<>("Description"));

        listOfInterest.getSelectionModel().getSelectedItems().addListener((InvalidationListener) event -> {
            String value = listOfInterest.getSelectionModel().getSelectedItem();
            webEngine.executeScript(" for(var i = 0; i<markers.length; i++){\n" +
                    "        if(markers[i].options.id === "+"'"+ value + "'" +" ){\n" +
                    "            map.setView([markers[i].getLatLng().lat, markers[i].getLatLng().lng], 9);\n" +
                    "        }\n" +
                    "    }"
            );
        });

        table.getSelectionModel().getSelectedCells().addListener((InvalidationListener) select -> {
            ObservableList<TaskProperties> getTaskProperties;
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/FXML/TaskViewWindow.fxml"));
                loader.setResources(UtilsConnection.getBundles());
                VBox pane = loader.load();
                TaskViewController passData = loader.getController();
                getTaskProperties = table.getSelectionModel().getSelectedItems();
                if (!(getTaskProperties == null)) {
                    passData.setContent(getTaskProperties.get(0).getLocalization(), getTaskProperties.get(0).getDescription());
                    passData.setEngine(webEngine);
                } else {
                    Dialog.error(UtilsConnection.getBundles().getString("selectErr.taskView"));
                }
                passData.toRemoveProperty().addListener(event -> {
                    table.getItems().removeIf(p -> p.getLocalization().equals(passData.getToRemove()));
                });
                Scene scene = new Scene(pane);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.getIcons().add(new Image("images/logo.png"));
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setResizable(false);
                stage.show();
                if (!stage.isFocused()) {
                    stage.close();
                }
            } catch (IOException e) {
                Dialog.error(UtilsConnection.getBundles().getString("newWindow.taskView"));
            }
        });
    }
}