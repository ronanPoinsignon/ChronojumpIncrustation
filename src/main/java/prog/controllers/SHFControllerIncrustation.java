package prog.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import prog.transmission.EventObserver;
import prog.utils.FxUtils;
import prog.utils.Utils;

public class SHFControllerIncrustation extends AbstractControllerIncrustation {

	private final EventObserver eventObserver = EventObserver.getInstance();

	@FXML
	private Label idNumeroEpreuve;
	@FXML
	private Label idLieu;
	@FXML
	private Label idChrono;
	@FXML
	private Label idPenalite;
	@FXML
	private Label idDossard;
	@FXML
	private Label idNomCheval;
	@FXML
	private Label idRaceCheval;
	@FXML
	private Label idPereCheval;
	@FXML
	private Label idMereCheval;
	@FXML
	private Label idPereMereCheval;
	@FXML
	private Label idPrenomCavalier;
	@FXML
	private Label idNomCavalier;
	@FXML
	private ImageView idImageCavalier;
	@FXML
	private Pane idPaneCavalier;
	@FXML
	private ImageView idLogo;
	@FXML
	private Pane idPaneLogo;
	@FXML
	private ImageView idImageChrono;
	@FXML
	private Pane idPaneChrono;
	@FXML
	private GridPane idGridpaneChrono;
	@FXML
	private GridPane idGridpaneInfo;

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		super.initialize(location, resources);

		// Gestion des events de récéption des informations

		this.bind(idChrono, eventObserver.getChrono());
		this.bind(idDossard, eventObserver.getDossard());
		this.bind(idPenalite, eventObserver.getPenalite());
		this.bind(idLieu, eventObserver.getLieuEpreuve());
		this.bind(idNumeroEpreuve, eventObserver.getNumeroEpreuve());
		this.bind(idNomCheval, eventObserver.getChevalName());
		this.bind(idRaceCheval, eventObserver.getChevalRace());
		this.bind(idPereCheval, eventObserver.getChevalPere(), value -> "Père : " + value);
		this.bind(idMereCheval, eventObserver.getChevalMere(), value -> "Mère : " + value);
		this.bind(idPereMereCheval, eventObserver.getChevalPereMere());
		this.bind(idNomCavalier, eventObserver.getCavalier(), value -> Utils.getNom(value).toUpperCase());
		this.bind(idPrenomCavalier, eventObserver.getCavalier(), value -> Utils.capitalizeFirstOnly(Utils.getPrenom(value)));

		// gestion visibilité pane principaux

		idGridpaneChrono.visibleProperty().bind(idChrono.visibleProperty());
		addListener(idChrono.visibleProperty(), FxUtils.addFadeTransition(idGridpaneChrono, AbstractController.FADE_DURATION));

		BooleanBinding allLabelsVisible = Bindings.or(Bindings.createBooleanBinding(() -> false), idNomCheval.visibleProperty())
				.or(idRaceCheval.visibleProperty())
				.or(idPereCheval.visibleProperty())
				.or(idMereCheval.visibleProperty())
				.or(idPereMereCheval.visibleProperty())
				.or(idNomCavalier.visibleProperty())
				.or(idDossard.visibleProperty());

		idGridpaneInfo.visibleProperty().bind(allLabelsVisible);
		addListener(allLabelsVisible, FxUtils.addFadeTransition(idGridpaneInfo, AbstractController.FADE_DURATION));

		//		Gestion responsive
		idLogo.fitWidthProperty().bind(idPaneLogo.widthProperty().multiply(0.8));
		idLogo.layoutXProperty().bind(idPaneLogo.widthProperty().multiply(0.5).subtract(idLogo.fitWidthProperty().divide(2)));

		idImageChrono.layoutXProperty().bind(idPaneChrono.widthProperty().multiply(0.5).subtract(idImageChrono.fitWidthProperty().divide(2)));
		idImageChrono.layoutYProperty().bind(idPaneChrono.heightProperty().multiply(0.5).subtract(idImageChrono.fitHeightProperty().divide(2)));
		idImageCavalier.layoutXProperty().bind(idPaneCavalier.widthProperty().multiply(0.5).subtract(idImageCavalier.fitWidthProperty().divide(2)));
		idImageCavalier.layoutYProperty().bind(idPaneCavalier.heightProperty().multiply(0.5).subtract(idImageCavalier.fitHeightProperty().divide(2)));
	}

	@Override
	protected void onSceneUpdate(Scene scene) {
		super.onSceneUpdate(scene);

		FxUtils.setImageSize(idImageChrono, scene, 35, AbstractController.BASE_WIDTH);
		FxUtils.setImageSize(idImageCavalier, scene, 64, AbstractController.BASE_WIDTH);

		this.bindLabelSize(idLieu, scene);
		this.bindLabelSize(idNumeroEpreuve, scene);
		this.bindLabelSize(idPereCheval, scene);
		this.bindLabelSize(idMereCheval, scene);
		this.bindLabelSize(idPereMereCheval, scene);
		this.bindLabelSize(idPrenomCavalier, scene);
		this.bindLabelSize(idNomCavalier, scene);
		this.bindLabelSize(idPenalite, scene);
		this.bindLabelSize(idNomCheval, scene);
		this.bindLabelSize(idRaceCheval, scene);
		this.bindLabelSize(idDossard, scene);
		this.bindLabelSize(idChrono, scene);
	}
}
