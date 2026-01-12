package prog.controllers;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import prog.transmission.EventObserver;

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
    @FXML
    private SVGPath idSVGCavalier;
    @FXML
    private SVGPath idSVGCheval;
    @FXML
    private SVGPath idSVGPenalite;
    @FXML
    private SVGPath idSVGChrono;
    @FXML
    private GridPane idGridpaneCavalier;
    @FXML
    private GridPane idGridpaneCheval;
    @FXML
    private GridPane idGridpanePenalite;
    @FXML
    private GridPane idGridpaneChrono;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        this.bind(idCavalier, eventObserver.getCavalier(), value -> value.replace("(Mme\\.)|(M\\.)", "").trim());
        this.bind(idCheval, eventObserver.getChevalName());
        this.bind(idPereCheval, eventObserver.getChevalPere());
        this.bind(idDossard, eventObserver.getDossard());
        this.bind(idMereCheval, eventObserver.getChevalMere());
        this.bind(idChrono, eventObserver.getChrono());

        // gestion parallelogramme

        bindParallelogramme(idGridpaneCavalier, idSVGCavalier);
        bindParallelogramme(idGridpaneCheval, idSVGCheval);
        bindParallelogramme(idGridpanePenalite, idSVGPenalite);
        bindParallelogramme(idGridpaneChrono, idSVGChrono);
    }

    private void bindParallelogramme(Region region, SVGPath svgPath) {
        double angle = 10;

        ChangeListener<Number> listener = (obs, oldV, newV) -> svgPath.setContent(computeSVGContent(region, angle));
        region.widthProperty().addListener(listener);
        region.heightProperty().addListener(listener);
    }

    private String computeSVGContent(Region region, double angle) {
        double x1 = computeX(region.getWidth(), region.getHeight(), angle);
        double length = region.getWidth() - x1;
        return "M " + length + ",0 L " + region.getWidth() + ",0 L " + x1 + "," + region.getHeight() + " L 0," + region.getHeight() + " Z";
    }

    private double computeX(double width, double height, double angle) {
        double radiansAngle = Math.toRadians(angle);
        double lengthSide = height * Math.cos(radiansAngle);
        return width - Math.sin(radiansAngle) * lengthSide;
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
