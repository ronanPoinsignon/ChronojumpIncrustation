package prog.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import prog.transmission.EventObserver;

import java.net.URL;
import java.util.ResourceBundle;

public class BasicControllerIncrustation extends AbstractController {

    private final EventObserver eventObserver = EventObserver.getInstance();

    @FXML
    private Label idChrono;
    @FXML
    private Label idPenalite;
    @FXML
    private Label idDossard;
    @FXML
    private Label idNomCavalier;
    @FXML
    private Label idNomCheval;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        // Gestion des events de récéption des informations

        this.bind(idChrono, eventObserver.getChrono());
        this.bind(idDossard, eventObserver.getDossard());
        this.bind(idPenalite, eventObserver.getPenalite());
        this.bind(idNomCheval, eventObserver.getChevalName());
        this.bind(idNomCavalier, eventObserver.getCavalier(), value -> value.replace("(Mme\\.)|(M\\.)", "").trim());
    }

    @Override
    protected void onSceneUpdate(Scene scene) {
        super.onSceneUpdate(scene);

        this.bindLabelSize(idNomCavalier, 20, scene);
        this.bindLabelSize(idPenalite, 20, scene);
        this.bindLabelSize(idNomCheval, 20, scene);
        this.bindLabelSize(idDossard, 20, scene);
        this.bindLabelSize(idChrono, 20, scene);
    }
}
