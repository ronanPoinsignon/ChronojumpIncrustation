package prog.transmission.tache;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class JsonListTacheReception<T> extends AbstractJsonTacheReception<List<T>, T, ObservableList<T>> {

    public JsonListTacheReception(int port, Class<T> clazz) {
        super(port, clazz);
    }

    @Override
    protected ObservableList<T> createObject() {
        return FXCollections.observableList(new ArrayList<>());
    }

    @Override
    protected JavaType getTypeBase(ObjectMapper mapper, Class<T> clazz) {
        return mapper.getTypeFactory().constructCollectionType(List.class, clazz);
    }

    @Override
    protected void updateValue(List<T> value) {
        Platform.runLater(() -> {
            getObject().clear();
            getObject().addAll(value);
        });
    }
}
