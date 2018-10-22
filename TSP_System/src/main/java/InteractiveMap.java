import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;


public class InteractiveMap extends AnchorPane {

    public InteractiveMap() {
        WebView webView = new WebView();
        getChildren().add(webView);


    }
}
