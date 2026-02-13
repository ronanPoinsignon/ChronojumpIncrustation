package prog.transmission.tache;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import prog.observableproperties.json.AbstractJsonObject;

public class JsonTacheReception<T extends AbstractJsonObject> extends AbstractJsonTacheReception<T, T, T> {

    public JsonTacheReception(int port, Class<T> clazz) {
        super(port, clazz);
    }

    @Override
    protected T createObject() {
        try {
            return getClazz().newInstance();
        } catch(InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected JavaType getTypeBase(ObjectMapper mapper, Class<T> clazz) {
        return mapper.getTypeFactory().constructType(clazz);
    }

    @Override
    protected void updateValue(T value) {
        Platform.runLater(() -> getObject().from(value));
    }

    @Override
    public void reset() {
        updateValue(createObject());
    }
}
