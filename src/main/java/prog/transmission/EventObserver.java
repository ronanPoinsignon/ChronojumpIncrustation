package prog.transmission;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import prog.observableproperties.StringObsProperty;
import prog.observableproperties.json.JsonCheval;
import prog.observableproperties.json.ClassementCavalier;
import prog.observableproperties.json.JsonEpreuve;
import prog.observableproperties.json.JsonLastCavalier;
import prog.transmission.tache.*;
import prog.utils.IncrustationAction;

public class EventObserver {

    private static final int PORT_LIEU_EPREUVE = 8090;				// port incruste lieu + numéro épreuve
    private static final int PORT_CHEVAL_JSON = 8091;				// port incruste cheval
    private static final int PORT_PRENALITE = 8092;					// port incruste penalite
    private static final int PORT_CHRONO = 8093;					// port incruste chrono
    private static final int PORT_DOSSARD = 8094;					// port incruste dossard
    private static final int PORT_LAST_CAVALIER = 8095;				// port incruste classement dernier cavalier
    private static final int PORT_CLASSEMENT = 8096;				// port incruste classement 8 derniers cavaliers
    private static final int PORT_EVENEMENT = 8099;				    // port gestion événement

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
    private final JsonLastCavalier lastCavalier;
    private final ReadOnlyListProperty<ClassementCavalier> classementCavalierList;
    private final ActionEnumTacheReception incrustationAction;

    private EventObserver() {
        JsonTacheReception<JsonEpreuve> tacheEpreuve = new JsonTacheReception<>(PORT_LIEU_EPREUVE, JsonEpreuve.class);
        JsonTacheReception<JsonCheval> tacheCheval = new JsonTacheReception<>(PORT_CHEVAL_JSON, JsonCheval.class);
        JsonTacheReception<JsonLastCavalier> tacheLastCavalier = new JsonTacheReception<>(PORT_LAST_CAVALIER, JsonLastCavalier.class);
        JsonListTacheReception<ClassementCavalier> tacheClassement = new JsonListTacheReception<>(PORT_CLASSEMENT, ClassementCavalier.class);

        chrono =  new RawTacheReception(PORT_CHRONO);
        dossard = new RawTacheReception(PORT_DOSSARD);
        penalite = new RawTacheReception(PORT_PRENALITE);
        epreuve = tacheEpreuve.getObject();
        cheval = tacheCheval.getObject();
        lastCavalier = tacheLastCavalier.getObject();
        classementCavalierList = new ReadOnlyListWrapper<>(tacheClassement.getObject());
        incrustationAction = new ActionEnumTacheReception(PORT_EVENEMENT);

        this.init(chrono);
        this.init(dossard);
        this.init(penalite);
        this.init(tacheEpreuve);
        this.init(tacheCheval);
        this.init(tacheLastCavalier);
        this.init(tacheClassement);
        this.init(incrustationAction);
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

    public StringObsProperty getLastClassement() {
        return lastCavalier.getClassement();
    }

    public StringObsProperty getLastCavalier() {
        return lastCavalier.getCavalier();
    }

    public StringObsProperty getLastChrono() {
        return lastCavalier.getChrono();
    }

    public StringObsProperty getLastPenalite() {
        return lastCavalier.getPenalite();
    }

    public ReadOnlyListProperty<ClassementCavalier> getClassementCavalierList() {
        return classementCavalierList;
    }

    public ReadOnlyObjectProperty<IncrustationAction> getIncrustationAction() {
        return incrustationAction.valueProperty();
    }

}
