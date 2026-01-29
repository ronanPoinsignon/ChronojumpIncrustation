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

		bind(idLieu, eventObserver.getLieuEpreuve());
		bind(idNumeroEpreuve, eventObserver.getNumeroEpreuve());

		this.bind(idNomCheval, eventObserver.getChevalName());
		this.bind(idRaceCheval, eventObserver.getChevalRace());
		this.bind(idPereCheval, eventObserver.getChevalPere(), value -> "Père : " + value);
		this.bind(idMereCheval, eventObserver.getChevalMere(), value -> "Mère : " + value);
		this.bind(idPereMereCheval, eventObserver.getChevalPereMere(), value -> "Père de mère : " + value.replace("(Mme\\.)|(M\\.)", "").trim());
		this.bind(idNomCavalier, eventObserver.getCavalier(), value -> Utils.getNom(value).toUpperCase());
		this.bind(idPrenomCavalier, eventObserver.getCavalier(), value -> Utils.capitalizeFirstOnly(Utils.getPrenom(value)));

		// gestion visibilité pane principaux

		idGridpaneChrono.visibleProperty().bind(idChrono.visibleProperty());
		idChrono.visibleProperty().addListener(addFadeTransition(idGridpaneChrono));

		BooleanBinding allLabelsVisible = Bindings.or(Bindings.createBooleanBinding(() -> false), idNomCheval.visibleProperty())
				.or(idRaceCheval.visibleProperty())
				.or(idPereCheval.visibleProperty())
				.or(idMereCheval.visibleProperty())
				.or(idPereMereCheval.visibleProperty())
				.or(idNomCavalier.visibleProperty())
				.or(idDossard.visibleProperty());

		idGridpaneInfo.visibleProperty().bind(allLabelsVisible);
		allLabelsVisible.addListener(addFadeTransition(idGridpaneInfo));

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

		setImageSize(idImageChrono, scene, 35);
		setImageSize(idImageCavalier, scene, 64);

		this.bindLabelSize(idLieu, 16, scene);
		this.bindLabelSize(idNumeroEpreuve, 16, scene);
		this.bindLabelSize(idPereCheval, 16, scene);
		this.bindLabelSize(idMereCheval, 16, scene);
		this.bindLabelSize(idPereMereCheval, 16, scene);
		this.bindLabelSize(idPrenomCavalier, 16, scene);
		this.bindLabelSize(idNomCavalier, 16, scene);
		this.bindLabelSize(idPenalite, 23, scene);
		this.bindLabelSize(idNomCheval, 23, scene);
		this.bindLabelSize(idRaceCheval, 23, scene);
		this.bindLabelSize(idDossard, 23, scene);
		this.bindLabelSize(idChrono, 23, scene);
	}
}
