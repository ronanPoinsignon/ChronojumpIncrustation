package prog.transmission;

import javafx.beans.property.ReadOnlyObjectProperty;
import prog.observableproperties.StringObsProperty;
import prog.observableproperties.json.JsonCheval;
import prog.observableproperties.json.JsonEpreuve;
import prog.transmission.tache.AbstractTacheReception;
import prog.transmission.tache.JsonTacheReception;
import prog.transmission.tache.RawTacheReception;

public class EventObserver {

    private static final int PORT_LIEU_EPREUVE = 8090;				// port incruste lieu + numéro épreuve
    private static final int PORT_CHEVAL_JSON = 8091;				// port incruste cheval
    private static final int PORT_PRENALITE = 8092;					// port incruste penalite
    private static final int PORT_CHRONO = 8093;					// port incruste chrono
    private static final int PORT_DOSSARD = 8094;					// port incruste dossard

    private static EventObserver eventObserver;

    public static EventObserver getInstance() {
        if(eventObserver == null) {
            eventObserver = new EventObserver();
        }

        return eventObserver;
    }

    private final RawTacheReception chrono;
    private final RawTacheReception dossard;
    private final RawTacheReception penalite;
    private final JsonEpreuve epreuve;
    private final JsonCheval cheval;

    private EventObserver() {
        JsonTacheReception<JsonEpreuve> tacheEpreuve = new JsonTacheReception<>(PORT_LIEU_EPREUVE, JsonEpreuve.class);
        JsonTacheReception<JsonCheval> tacheCheval = new JsonTacheReception<>(PORT_CHEVAL_JSON, JsonCheval.class);

        chrono =  new RawTacheReception(PORT_CHRONO);
        dossard = new RawTacheReception(PORT_DOSSARD);
        penalite = new RawTacheReception(PORT_PRENALITE);
        epreuve = tacheEpreuve.getObject();
        cheval = tacheCheval.getObject();

        this.init(chrono);
        this.init(dossard);
        this.init(penalite);
        this.init(tacheEpreuve);
        this.init(tacheCheval);
    }

    private void init(AbstractTacheReception<?> tache) {
        final Thread t = new Thread(tache);
        t.setDaemon(true);
        t.start();
    }

    public ReadOnlyObjectProperty<String> getChrono() {
        return chrono.valueProperty();
    }

    public ReadOnlyObjectProperty<String> getDossard() {
        return dossard.valueProperty();
    }

    public ReadOnlyObjectProperty<String> getPenalite() {
        return penalite.valueProperty();
    }

    public StringObsProperty getNumeroEpreuve() {
        return epreuve.getNumero();
    }

    public StringObsProperty getLieuEpreuve() {
        return epreuve.getLieu();
    }

    public StringObsProperty getChevalName() {
        return cheval.getChevalName();
    }

    public StringObsProperty getCavalier() {
        return cheval.getCavalier();
    }

    public StringObsProperty getChevalPere() {
        return cheval.getPere();
    }

    public StringObsProperty getChevalMere() {
        return cheval.getMere();
    }

    public StringObsProperty getChevalPereMere() {
        return cheval.getPereMere();
    }

    public StringObsProperty getChevalRace() {
        return cheval.getRace();
    }

}
