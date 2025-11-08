package prog.transmission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import prog.observableproperties.json.AbstractJsonObject;

public class JsonTacheReception<T extends AbstractJsonObject> extends AbstractTacheReception<T> {

    private final ObjectMapper mapper = new ObjectMapper();;
    private final Class<T> clazz;
    private T object;

    public JsonTacheReception(int port, Class<T> clazz) {
        super(port);
        this.clazz = clazz;
        try {
            object = clazz.newInstance();
        } catch(InstantiationException | IllegalAccessException e) {
           e.printStackTrace();
        }
    }

    @Override
    protected T convert(String value) {
        if(value == null) {
            return null;
        }

        try {
            return mapper.readValue(value, clazz);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void updateValue(T value) {
        Platform.runLater(() -> object.from(value));
    }

    public T getObject() {
        return object;
    }

}
