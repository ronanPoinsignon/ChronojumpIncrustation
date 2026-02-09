package prog.transmission.tache;

import prog.utils.IncrustationAction;

public class ActionEnumTacheReception extends AbstractTacheReception<IncrustationAction> {

    public ActionEnumTacheReception(int port) {
        super(port);
    }

    @Override
    protected IncrustationAction convert(String value) {
        try {
            return IncrustationAction.valueOf(value);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
