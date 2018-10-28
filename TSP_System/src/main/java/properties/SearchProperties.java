package properties;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jdk.nashorn.internal.objects.annotations.Getter;

public class SearchProperties {

    //From TextField properties
    private StringProperty fromProperties = new SimpleStringProperty();

    //End TextField properties
    private StringProperty endProperties = new SimpleStringProperty();

    @Getter
    public StringProperty getFromProperties() {
        return fromProperties;
    }
    @Getter
    public StringProperty getEndProperties() {
        return endProperties;
    }




}
