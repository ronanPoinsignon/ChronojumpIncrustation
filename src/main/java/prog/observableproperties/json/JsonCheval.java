package prog.observableproperties.json;

import prog.observableproperties.StringObsProperty;

public class JsonCheval extends AbstractJsonObject {

    private final StringObsProperty chevalName = new StringObsProperty();
    private final StringObsProperty cavalier = new StringObsProperty();
    private final StringObsProperty pere = new StringObsProperty();
    private final StringObsProperty mere = new StringObsProperty();
    private final StringObsProperty pereMere = new StringObsProperty();
    private final StringObsProperty race = new StringObsProperty();

    public StringObsProperty getChevalName() {
        return chevalName;
    }

    public StringObsProperty getCavalier() {
        return cavalier;
    }

    public StringObsProperty getPere() {
        return pere;
    }

    public StringObsProperty getMere() {
        return mere;
    }

    public StringObsProperty getPereMere() {
        return pereMere;
    }

    public StringObsProperty getRace() {
        return race;
    }

    @Override
    public void from(AbstractJsonObject from) {
        if(!(from instanceof JsonCheval)) {
            return;
        }

        JsonCheval fromCheval = (JsonCheval) from;

        chevalName.setValue(fromCheval.chevalName.getString());
        cavalier.setValue(fromCheval.cavalier.getString());
        pere.setValue(fromCheval.pere.getString());
        mere.setValue(fromCheval.mere.getString());
        pereMere.setValue(fromCheval.pereMere.getString());
        race.setValue(fromCheval.race.getString());
    }
}
