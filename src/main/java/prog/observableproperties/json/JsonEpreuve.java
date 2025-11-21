package prog.observableproperties.json;

import prog.observableproperties.StringObsProperty;

public class JsonEpreuve extends AbstractJsonObject {

    private final StringObsProperty numero = new StringObsProperty();
    private final StringObsProperty lieu = new StringObsProperty();

    public StringObsProperty getNumero() {
        return numero;
    }

    public StringObsProperty getLieu() {
        return lieu;
    }

    @Override
    public void from(AbstractJsonObject from) {
        if(!(from instanceof JsonEpreuve)) {
            return;
        }

        JsonEpreuve fromEpreuve = (JsonEpreuve) from;

        numero.setValue(fromEpreuve.numero.getString());
        lieu.setValue(fromEpreuve.lieu.getString());
    }
}
