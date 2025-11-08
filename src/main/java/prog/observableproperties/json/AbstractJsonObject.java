package prog.observableproperties.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import prog.transmission.deserializer.ObsPropertyDeserializer;

@JsonDeserialize(using = ObsPropertyDeserializer.class)
public abstract class AbstractJsonObject {

    public abstract void from(AbstractJsonObject from);
}
