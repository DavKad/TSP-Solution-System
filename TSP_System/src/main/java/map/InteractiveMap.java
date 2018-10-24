package map;

import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.*;


public class InteractiveMap extends AnchorPane {
    private final WebView webView;
    private Map map;
    private MapOptions mapOptions;

    public WebView getWebView() {
        return webView;
    }

    public Map getMap() {
        return map;
    }

    public InteractiveMap() {
        webView = new WebView();
        getChildren().add(webView);

        FXBrowsers.load(webView, InteractiveMap.class.getResource("/html/index.html"), () -> {
            mapOptions = new MapOptions().setCenter(new LatLng(52.125736,19.080392)).setZoom(6);
            map = new Map("map", mapOptions);
            map.addLayer(new TileLayer("https://tile.thunderforest.com/transport/{z}/{x}/{y}.png?apikey=1efed7dc4d8340d6a5529a4e9a9068c3",
                     new TileLayerOptions()
                    .setAttribution("Map data &copy; Transport" + " Imagery © <a href='http://www.thunderforest.com/'>Thunderforest</a>")
                    .setMaxZoom(18)
                    .setMinZoom(3)));
        });
    }

}
