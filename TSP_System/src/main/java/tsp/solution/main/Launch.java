package tsp.solution.main;

import dialogs.Dialog;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jdk.nashorn.internal.objects.annotations.Function;
import utils.UtilsConnection;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

public class Launch extends Application {
    @Function
    public static void main(String[] args) { Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Locale.setDefault(new Locale("en"));
        /*Resources*/
        AnchorPane anchorPane = UtilsConnection.getFXML("/FXML/MainWindow.fxml");
        Scene mainScene = new Scene(anchorPane);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle(UtilsConnection.getBundles().getString("title.app"));
        primaryStage.getIcons().add(new Image("images/logo.png"));
        primaryStage.setResizable(false);
        primaryStage.show();

        /*Confirmed exit from application*/
        primaryStage.setOnCloseRequest(s -> {
            Optional<ButtonType> confirmExit = Dialog.closeApplicationConfirmation();
            if (confirmExit.get() == ButtonType.OK) {
                Platform.exit();
            } else {
                s.consume();
            }
        });
    }
}