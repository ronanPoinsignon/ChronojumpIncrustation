package prog.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import prog.transmission.EventObserver;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PanneauController extends AbstractController {

    EventObserver eventObserver = EventObserver.getInstance();

    @FXML
    private Label idChrono;
    @FXML
    private Label idDossard;
    @FXML
    private Label idCheval;
    @FXML
    private Label idCavalier;
    @FXML
    private Label idPereCheval;
    @FXML
    private Label idMereCheval;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        this.bind(idCavalier, eventObserver.getCavalier(), value -> value.replace("(Mme\\.)|(M\\.)", "").trim());
        this.bind(idCheval, eventObserver.getChevalName());
        this.bind(idPereCheval, eventObserver.getChevalPere());
        this.bind(idDossard, eventObserver.getDossard());
        this.bind(idMereCheval, eventObserver.getChevalMere());
        this.bind(idChrono, eventObserver.getChrono());
    }

    @Override
    protected void onSceneUpdate(Scene scene) {
        super.onSceneUpdate(scene);

        this.bindLabelSize(idCavalier, 45, scene);
        this.bindLabelSize(idCheval, 45, scene);
        this.bindLabelSize(idPereCheval, 35, scene);
        this.bindLabelSize(idDossard, 35, scene);
        this.bindLabelSize(idMereCheval, 35, scene);
        this.bindLabelSize(idChrono, 50, scene);
    }
}
