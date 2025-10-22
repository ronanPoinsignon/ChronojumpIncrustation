package prog.controllers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import prog.transmission.TacheReception;

public class NewControllerVideo implements Initializable {

	private static final int PORT_CAVALIER_NOM = 8090;
	private static final int PORT_CHEVAL_NOM = 8091;
	private static final int PORT_PRENALITE = 8092;
	private static final int PORT_CHRONO = 8093;
	private static final int PORT_LIEU = 8094;
	private static final int PORT_DOSSARD = 8095;
	private static final int PORT_CHEVAL_RACE = 8096;
	private static final int PORT_CHEVAL_PERE = 8097;
	private static final int PORT_CHEVAL_MERE = 8098;
	private static final int PORT_CHEVAL_PERE_MERE = 8099;
	private static final int PORT_CAVALIER_PRENOM = 8100;

	private static final int FADE_DURATION = 200;

	// 20% de la hauteur de l'écran
	private static final double PANEL_INFO_COUREUR_HEIGHT_PERCENTAGE = 0.134;
	private static final double PANEL_INFO_EPREUVE_HEIGHT_PERCENTAGE = 0.134;
	private static final int BASE_WIDTH = 1280;
	private static final int BASE_HEIGHT = 720;

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
	private ImageView idLogo;
	@FXML
	private ImageView idImageChrono;
	@FXML
	private GridPane idGridpaneChrono;
	@FXML
	private GridPane idGridpaneCheval;
	@FXML
	private GridPane idGridpaneParents;
	@FXML
	private GridPane idGridpaneCavalier;
	@FXML
	private GridPane idGridpaneInfo;
	@FXML
	private GridPane idGridpaneEpreuve;
	@FXML
	private AnchorPane idAnchorBase;

	private boolean isFullScreen = false;
	private double coordX, coordY;

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		TacheReception.setAdresse(ControllerVideo.INIT_IP_ADRESSE);

		// Gestion des events de récéption des informations
		final Map<Label, TacheReception> labelTacheMap = new HashMap<>();
		labelTacheMap.put(idNomCavalier, new TacheReception(NewControllerVideo.PORT_CAVALIER_NOM));
		labelTacheMap.put(idNomCheval, new TacheReception(NewControllerVideo.PORT_CHEVAL_NOM));
		labelTacheMap.put(idPenalite, new TacheReception(NewControllerVideo.PORT_PRENALITE));
		labelTacheMap.put(idChrono, new TacheReception(NewControllerVideo.PORT_CHRONO));
		labelTacheMap.put(idLieu, new TacheReception(NewControllerVideo.PORT_LIEU));
		labelTacheMap.put(idDossard, new TacheReception(NewControllerVideo.PORT_DOSSARD));
		labelTacheMap.put(idRaceCheval, new TacheReception(NewControllerVideo.PORT_CHEVAL_RACE));
		labelTacheMap.put(idPereCheval, new TacheReception(NewControllerVideo.PORT_CHEVAL_PERE));
		labelTacheMap.put(idMereCheval, new TacheReception(NewControllerVideo.PORT_CHEVAL_MERE));
		labelTacheMap.put(idPereMereCheval, new TacheReception(NewControllerVideo.PORT_CHEVAL_PERE_MERE));
		labelTacheMap.put(idPrenomCavalier, new TacheReception(NewControllerVideo.PORT_CAVALIER_PRENOM));
//				labelTacheMap.forEach(this::bind);

		idGridpaneChrono.visibleProperty().bind(idChrono.visibleProperty());
		idChrono.visibleProperty().addListener(addFadeTransition(idGridpaneChrono));

		BooleanBinding allLabelsVisible = Bindings.createBooleanBinding(() -> false);
		for (final Label label : labelTacheMap.keySet()) {
			allLabelsVisible = Bindings.or(allLabelsVisible, label.visibleProperty());
		}
		idGridpaneInfo.visibleProperty().bind(allLabelsVisible);
		allLabelsVisible.addListener(addFadeTransition(idGridpaneInfo));

		//		Gestion responsive
		idGridpaneInfo.prefHeightProperty().bind(idAnchorBase.heightProperty().multiply(NewControllerVideo.PANEL_INFO_COUREUR_HEIGHT_PERCENTAGE));
		idGridpaneEpreuve.prefHeightProperty().bind(idAnchorBase.heightProperty().multiply(NewControllerVideo.PANEL_INFO_EPREUVE_HEIGHT_PERCENTAGE));

		idAnchorBase.heightProperty().addListener((obs, oldV, newV) -> {
			final double heightRatio = newV.doubleValue()/NewControllerVideo.BASE_HEIGHT;
			AnchorPane.setTopAnchor(idGridpaneEpreuve, 33 * heightRatio);
		});
		idGridpaneEpreuve.heightProperty().addListener((obs, oldV, newV) -> {
			idLogo.setFitHeight(newV.doubleValue());
			idLogo.setFitWidth(newV.doubleValue() * ((double) 73/88));
		});
		GridPane.setFillWidth(idImageChrono, false);
		GridPane.setFillHeight(idImageChrono, false);
		idGridpaneChrono.widthProperty().addListener((obs, oldV, newV) -> {
			final ColumnConstraints colConstraints = idGridpaneChrono.getColumnConstraints().get(0);
			final RowConstraints rowConstraints = idGridpaneChrono.getRowConstraints().get(0);

			final double cellWidth = colConstraints.getPrefWidth() == Region.USE_COMPUTED_SIZE ?
					idGridpaneChrono.getWidth() * (colConstraints.getPercentWidth() / 100) :
						colConstraints.getPrefWidth();

			final double cellHeight = rowConstraints.getPrefHeight() == Region.USE_COMPUTED_SIZE ?
					idGridpaneChrono.getHeight() * (rowConstraints.getPercentHeight() / 100) :
						rowConstraints.getPrefHeight();

			final double size = Math.min(cellWidth, cellHeight);

			idImageChrono.setFitWidth(size);
		});
		idGridpaneChrono.heightProperty().addListener((obs, oldV, newV) -> {
			final ColumnConstraints colConstraints = idGridpaneChrono.getColumnConstraints().get(0);
			final RowConstraints rowConstraints = idGridpaneChrono.getRowConstraints().get(0);

			final double cellWidth = colConstraints.getPrefWidth() == Region.USE_COMPUTED_SIZE ?
					idGridpaneChrono.getWidth() * (colConstraints.getPercentWidth() / 100) :
						colConstraints.getPrefWidth();

			final double cellHeight = rowConstraints.getPrefHeight() == Region.USE_COMPUTED_SIZE ?
					idGridpaneChrono.getHeight() * (rowConstraints.getPercentHeight() / 100) :
						rowConstraints.getPrefHeight();

			final double size = Math.min(cellWidth, cellHeight);

			idImageChrono.setFitHeight(size);
		});

		// gestion de la taille du texte des labels
		idLieu.widthProperty().addListener((obs, oldV, newV) -> {
			setLabelTextSize(idLieu, 16);
		});

		// Lancement des process de récéption
		labelTacheMap.values().forEach(tache -> {
			final Thread t = new Thread(tache);
			t.setDaemon(true);
			t.start();
		});
	}

	private void setLabelTextSize(final Label label, final int defaultSize) {
		final double height = label.getHeight();
		final double width = label.getWidth();
		final double heightRatio = height/NewControllerVideo.BASE_HEIGHT;
		final double widthRatio = width/NewControllerVideo.BASE_WIDTH;

		final double textWidth = Math.max(height, widthRatio * defaultSize);

		//		label.setFont(Font.font(label.getFont().getFamily(), textWidth));
		//		System.out.println("height ratio : " + heightRatio );
		//		System.out.println("text width : " + textWidth);
		System.out.println("result : " + NewControllerVideo.calculateMaxFontSize(label.getText(), label.getFont().getFamily(), label.getWidth(), 16));
	}

	public static double calculateMaxFontSize(final String text, final String fontFamily, final double maxWidth, final double baseFontSize) {
		final Text helperText = new Text(text);
		helperText.setFont(Font.font(fontFamily, baseFontSize));

		// Mesure la largeur actuelle du texte
		final double textWidth = helperText.getLayoutBounds().getWidth();
		System.out.println("textWidth : " + textWidth);
		System.out.println("baseFontSize : " + baseFontSize);
		// Si le texte tient déjà, retourne la taille de base
		if (textWidth <= maxWidth) {
			return baseFontSize;
		}

		// Sinon, calcule la taille maximale proportionnelle

		return maxWidth / textWidth * baseFontSize;
	}

	private ChangeListener<? super Boolean> addFadeTransition(final Node node) {
		return (obs, oldV, newV) -> {
			final FadeTransition transition = new FadeTransition(Duration.millis(NewControllerVideo.FADE_DURATION), node);
			if(newV) {
				transition.setFromValue(0);
				transition.setToValue(1);
			} else {
				transition.setFromValue(1);
				transition.setToValue(0);
			}
			transition.play();
		};
	}

	private void bind(final Label label, final TacheReception tacheReception) {
		label.textProperty().bind(tacheReception.valueProperty());
		label.visibleProperty().bind(Bindings.when(
				label.textProperty().isEmpty().or(label.textProperty().isEqualTo("0").or(label.textProperty().isEqualTo("0.0").or(label.textProperty().isEqualTo("0.00")))))
				.then(false)
				.otherwise(true));
		label.visibleProperty().addListener((obs, oldV, newV) -> {
			final FadeTransition transition = new FadeTransition(Duration.millis(NewControllerVideo.FADE_DURATION), label);
			if(newV) {
				transition.setFromValue(0);
				transition.setToValue(1.0);
			} else {
				transition.setFromValue(1);
				transition.setToValue(0);
			}
			transition.play();
		});
	}

	public void addevent(final Stage stage) {
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.getScene().setOnKeyPressed(evt -> {
			if(evt.getCode() == KeyCode.F12 || isFullScreen && evt.getCode() == KeyCode.ESCAPE) {
				setFullScreen(stage);
			}
		});
		idAnchorBase.setOnMousePressed(evt -> {
			coordX = evt.getX();
			coordY = evt.getY();
			if(evt.getClickCount()%2 == 0) {
				setFullScreen(stage);
			}
		});
		idAnchorBase.setOnMouseDragged(evt -> {
			if(isFullScreen) {
				evt.consume();
				return;
			}
			stage.setX(evt.getScreenX() - coordX);
			stage.setY(evt.getScreenY() - coordY);
		});
	}

	public void setFullScreen(final Stage stage) {
		stage.setFullScreen(isFullScreen = !isFullScreen);
	}

}
