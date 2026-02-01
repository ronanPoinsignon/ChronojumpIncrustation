package prog.transmission.tache;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Convertit un string en objet JSON à partir de la librairie Jackson.
 * @param <CONVERTED_TYPE> Le retour de la conversion jackson.
 * @param <T> Le type de la classe à convertir.
 * @param <RESULT_TYPE> L'objet final après conversion. Cet objet est modifié par les éléments désérialisés par Jackson.
 */
public abstract class AbstractJsonTacheReception<CONVERTED_TYPE, T, RESULT_TYPE> extends AbstractTacheReception<CONVERTED_TYPE> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Class<T> clazz;
    private final RESULT_TYPE object;

    protected AbstractJsonTacheReception(int port, Class<T> clazz) {
        super(port);
        this.clazz = clazz;
        this.object = createObject();
    }

    protected Class<T> getClazz() {
        return clazz;
    }

    protected abstract RESULT_TYPE createObject();

    public RESULT_TYPE getObject() {
        return object;
    }

    @Override
    protected CONVERTED_TYPE convert(String value) {
        if(value == null) {
            return null;
        }

        try {
            return mapper.readValue(value, getTypeBase(mapper, clazz));
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected abstract JavaType getTypeBase(ObjectMapper mapper, Class<T> clazz);
}
