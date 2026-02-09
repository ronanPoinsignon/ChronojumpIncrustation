package prog.utils;

import java.net.URL;

public enum FxmlIncrustation {

    CHOIX("/fxml/menu_choix_incrustation.fxml"),
    BASIC("/fxml/basic_incrustation.fxml"),
    SHF("/fxml/SHF_incrustation.fxml"),
    PANNEAU("/fxml/panneau_incrustation.fxml"),
    CLASSEMENT("/fxml/classement_incrustation.fxml");

    private final String path;

    FxmlIncrustation(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public URL getPathAsResource() {
        return ResourceUtils.getResource(getPath());
    }

}
