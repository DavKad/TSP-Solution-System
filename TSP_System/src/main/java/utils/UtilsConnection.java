package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ResourceBundle;

public class UtilsConnection {

    public static AnchorPane getFXML(String FXMLPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(UtilsConnection.class.getResource(FXMLPath));
        loader.setResources(getBundles());
        return loader.load();
    }

    public static ResourceBundle getBundles() {
        return ResourceBundle.getBundle("bundles/messages");
    }

}
