package map;

import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.*;
import net.java.html.leaflet.event.MouseEvent;


public class InteractiveMap extends AnchorPane {
    private WebView webView;
    private Map map;

    public WebView getWebView() {
        return webView;
    }

    public Map getMap() {
        return map;
    }

    public InteractiveMap() {
        webView = new WebView();
        getChildren().add(webView);

        FXBrowsers.load(webView, InteractiveMap.class.getResource("html/index.html"), () -> {
            map = new Map("map");
            map.addLayer(new TileLayer("https://tile.thunderforest.com/outdoors/{z}/{x}/{y}.png?apikey=1efed7dc4d8340d6a5529a4e9a9068c3", new TileLayerOptions().setMaxZoom(15)));
            map.setView(new LatLng(51.04,17.01));
        });
        Circle PWRCircle = new Circle(new LatLng(51.107319,17.062096),200);
        PWRCircle.addMouseListener(MouseEvent.Type.CLICK, s -> {
           PopupOptions popupOptions = new PopupOptions().setMaxWidth(400);
           Popup popup =new Popup(popupOptions);
           popup.setLatLng(s.getLatLng());
           popup.setContent("PWR!!");
           popup.openOn(map);
        });
    }

}
