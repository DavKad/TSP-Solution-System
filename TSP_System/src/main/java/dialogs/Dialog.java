package dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.UtilsConnection;

import java.util.Optional;
import java.util.ResourceBundle;

public class Dialog {
    private static ResourceBundle bundles = UtilsConnection.getBundles();

    public static Optional<ButtonType> closeApplicationConfirmation() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(bundles.getString("exitTitle.dialogs"));
        confirmationAlert.setHeaderText(bundles.getString("exitMessage.dialogs"));
        Stage dialogStage = (Stage) confirmationAlert.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(Dialog.class.getResource("/images/information.png").toString()));
        return confirmationAlert.showAndWait();
    }

    public static void aboutApplication() {
        Alert information = new Alert(Alert.AlertType.INFORMATION);
        information.setTitle(bundles.getString("aboutTitle.app"));
        information.setHeaderText(bundles.getString("headerAbout.app"));
        information.setContentText(bundles.getString("textAbout.app"));
        Stage stage = (Stage) information.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Dialog.class.getResource("/images/information.png").toString()));
        information.showAndWait();
    }

    public static void savedFile(){
        Alert saved = new Alert(Alert.AlertType.INFORMATION);
        saved.setTitle(bundles.getString("savedTitle.rep"));
        saved.setHeaderText(bundles.getString("saveHeader.rep"));
        Stage stage = (Stage) saved.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Dialog.class.getResource("/images/information.png").toString()));
        saved.showAndWait();
    }

    public static void error(String message){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(bundles.getString("errorTitle.rep"));
        error.setHeaderText(bundles.getString("errorHeader.rep"));
        TextField textField = new TextField(message);
        error.getDialogPane().setContent(textField);
        error.showAndWait();
    }


}
