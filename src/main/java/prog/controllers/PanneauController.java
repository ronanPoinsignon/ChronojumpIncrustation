package prog.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import prog.executor.ControllerExecutor;
import prog.transmission.EventObserver;
import prog.utils.FxmlIncrustation;
import prog.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PanneauController extends AbstractController {

    private final ControllerExecutor controllerExecutor = ControllerExecutor.getExecutor();
    private final EventObserver eventObserver = EventObserver.getInstance();

    @FXML
    private AnchorPane idAnchorBase;
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
    private Label idPenalite;
    @FXML
    private Label idClassementPlace;
    @FXML
    private Label idClassementCavalier;
    @FXML
    private Label idClassementPenalite;
    @FXML
    private Label idClassementChrono;
    @FXML
    private SVGPath idSVGCavalier;
    @FXML
    private SVGPath idSVGCheval;
    @FXML
    private SVGPath idSVGDossard;
    @FXML
    private SVGPath idSVGChrono;
    @FXML
    private SVGPath idSVGPenalite;
    @FXML
    private SVGPath idSvgSeparation;
    @FXML
    private GridPane idGridpaneCavalier;
    @FXML
    private GridPane idGridpaneCheval;
    @FXML
    private GridPane idGridpaneDossard;
    @FXML
    private GridPane idGridpaneChrono;
    @FXML
    private StackPane idStackpaneCavalier;
    @FXML
    private StackPane idStackpaneCheval;
    @FXML
    private StackPane idStackpaneDossard;
    @FXML
    private StackPane idStackpaneChrono;
    @FXML
    private StackPane idStackpanePenalite;
    @FXML
    private GridPane idGridpaneChronoMain;
    @FXML
    private GridPane idGridpanePenalite;
    @FXML
    private GridPane idGridpanePenaliteHidden;
    @FXML
    private Pane idPaneSeparation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        this.bind(idCavalier, eventObserver.getCavalier(), value -> Utils.getNom(value).toUpperCase() + " " + Utils.capitalizeFirstOnly(Utils.getPrenom(value)));
        this.bind(idCheval, eventObserver.getChevalName());
        this.bind(idPereCheval, eventObserver.getChevalPere());
        this.bind(idDossard, eventObserver.getDossard());
        this.bind(idMereCheval, eventObserver.getChevalMere());
        this.bind(idPenalite, eventObserver.getPenalite());
        this.bind(idChrono, eventObserver.getChrono());

        // gestion responive

        idPaneSeparation.maxHeightProperty().bind(idClassementChrono.heightProperty());

        // gestion parallélogrammes

        bindParallelogramme(idGridpaneCavalier, idSVGCavalier);
        bindParallelogramme(idGridpaneCheval, idSVGCheval);
        bindParallelogramme(idGridpaneDossard, idSVGDossard);
        bindParallelogramme(idGridpaneChrono, idSVGChrono);
        bindParallelogramme(idGridpanePenalite, idSVGPenalite);

        // gestion visibilité

        idStackpaneCavalier.visibleProperty().bind(idCavalier.visibleProperty());
        idStackpaneCheval.visibleProperty().bind(idCheval.visibleProperty());
        idStackpaneDossard.visibleProperty().bind(idDossard.visibleProperty());
        idStackpaneChrono.visibleProperty().bind(idChrono.visibleProperty());
        idStackpanePenalite.visibleProperty().bind(idPenalite.visibleProperty());
        idGridpaneChronoMain.visibleProperty().bind(Bindings.or(idStackpanePenalite.visibleProperty(), idStackpaneChrono.visibleProperty()));
        idGridpanePenaliteHidden.visibleProperty().bind(idStackpaneChrono.visibleProperty());
        idPaneSeparation.visibleProperty().bind(idClassementPenalite.visibleProperty());
    }

    private void bindParallelogramme(Region region, SVGPath svgPath) {
        double angle = 10;

        ChangeListener<Number> listener = (obs, oldV, newV) -> svgPath.setContent(computeSVGContent(region, angle));
        addListener(region.widthProperty(), listener);
        addListener(region.heightProperty(), listener);
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
        this.bindLabelSize(idPenalite, 40, scene);
        this.bindLabelSize(idClassementPenalite, 35, scene);
        this.bindLabelSize(idClassementChrono, 35, scene);
        this.bindLabelSize(idClassementPlace, 35, scene);
        this.bindLabelSize(idClassementCavalier, 35, scene);
    }

    @Override
    protected void anchorBaseOnMousePressed(MouseEvent evt, Stage stage) {
        super.anchorBaseOnMousePressed(evt, stage);
        if(MouseButton.SECONDARY == evt.getButton()) {
            controllerExecutor.switchScene(this, FxmlIncrustation.CLASSEMENT);
        }
    }
}
