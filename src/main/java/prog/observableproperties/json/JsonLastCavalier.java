package prog.observableproperties.json;

import prog.observableproperties.StringObsProperty;

public class JsonLastCavalier extends AbstractJsonObject {

    private final StringObsProperty classement = new StringObsProperty();
    private final StringObsProperty cavalier = new StringObsProperty();
    private final StringObsProperty chrono = new StringObsProperty();
    private final StringObsProperty penalite = new StringObsProperty();

    public StringObsProperty getClassement() {
        return classement;
    }

    public StringObsProperty getCavalier() {
        return cavalier;
    }

    public StringObsProperty getChrono() {
        return chrono;
    }

    public StringObsProperty getPenalite() {
        return penalite;
    }

    @Override
    public void from(AbstractJsonObject from) {
        if(!(from instanceof JsonLastCavalier)) {
            return;
        }

        JsonLastCavalier fromCavalier = (JsonLastCavalier) from;

        classement.setValue(fromCavalier.classement.getString());
        cavalier.setValue(fromCavalier.cavalier.getString());
        chrono.setValue(fromCavalier.chrono.getString());
        penalite.setValue(fromCavalier.penalite.getString());
    }
}
