package dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jdk.nashorn.internal.objects.annotations.Function;
import utils.UtilsConnection;

import java.util.Optional;
import java.util.ResourceBundle;

public class Dialog {

    private static ResourceBundle bundles = UtilsConnection.getBundles();

    @Function
    public static Optional<ButtonType> closeApplicationConfirmation() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(bundles.getString("exitTitle.dialogs"));
        confirmationAlert.setHeaderText(bundles.getString("exitMessage.dialogs"));
        Stage dialogStage = (Stage) confirmationAlert.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(Dialog.class.getResource("/images/information.png").toString()));
        return confirmationAlert.showAndWait();
    }

    @Function
    public static void informationAlert(String message, String header, String title) {
        Alert information = new Alert(Alert.AlertType.INFORMATION);
        information.setTitle(title);
        information.setHeaderText(header);
        information.setContentText(message);
        Stage stage = (Stage) information.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Dialog.class.getResource("/images/information.png").toString()));
        information.showAndWait();
    }

    @Function
    public static void savedFile(){
        Alert saved = new Alert(Alert.AlertType.INFORMATION);
        saved.setTitle(bundles.getString("savedTitle.rep"));
        saved.setHeaderText(bundles.getString("saveHeader.rep"));
        Stage stage = (Stage) saved.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Dialog.class.getResource("/images/information.png").toString()));
        saved.showAndWait();
    }

    @Function
    public static void error(String message){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(bundles.getString("errorTitle.rep"));
        error.setHeaderText(bundles.getString("errorHeader.rep"));
        TextField textField = new TextField(message);
        error.getDialogPane().setContent(textField);
        error.showAndWait();
    }

}
