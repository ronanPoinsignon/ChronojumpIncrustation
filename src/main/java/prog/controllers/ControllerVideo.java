package prog.controllers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import prog.observableproperties.StringObsProperty;
import prog.observableproperties.json.JsonCheval;
import prog.transmission.AbstractTacheReception;
import prog.transmission.JsonTacheReception;
import prog.transmission.RawTacheReception;

public class ControllerVideo implements Initializable {

//	private static final String INIT_IP_ADRESSE = "192.168.1.31";
	private static final String INIT_IP_ADRESSE = "169.254.122.66";

	private static final int PORT_CHEVAL_JSON = 8091;				// port incruste cheval
	private static final int PORT_PRENALITE = 8092;					// port incruste penalite
	private static final int PORT_CHRONO = 8093;					// port incruste chrono
	private static final int PORT_DOSSARD = 8094;					// port incruste dossard

	private static final int PORT_LIEU = 8095; 						// port incruste lieu
	private static final int PORT_NUMERO_EPREUVE = 8101;			// port incruste lieu

	private static final int FADE_DURATION = 200;

	// 20% de la hauteur de l'écran
	private static final double PANEL_INFO_COUREUR_HEIGHT_PERCENTAGE = 0.134;
	private static final double PANEL_INFO_EPREUVE_HEIGHT_PERCENTAGE = 0.134;
	private static final int BASE_WIDTH = 1280;
	private static final int BASE_HEIGHT = 720;

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
		AbstractTacheReception.setAdresse(INIT_IP_ADRESSE);

		// Gestion des events de récéption des informations

		final Map<Label, RawTacheReception> labelTacheMap = new HashMap<>();
		labelTacheMap.put(idPenalite, new RawTacheReception(ControllerVideo.PORT_PRENALITE));
		labelTacheMap.put(idChrono, new RawTacheReception(ControllerVideo.PORT_CHRONO));
		labelTacheMap.put(idLieu, new RawTacheReception(ControllerVideo.PORT_LIEU));
		labelTacheMap.put(idDossard, new RawTacheReception(ControllerVideo.PORT_DOSSARD));
		labelTacheMap.put(idNumeroEpreuve, new RawTacheReception(ControllerVideo.PORT_NUMERO_EPREUVE));
		labelTacheMap.forEach(this::bind);

		JsonTacheReception<JsonCheval> tacheCheval = new JsonTacheReception<>(PORT_CHEVAL_JSON, JsonCheval.class);
		JsonCheval json  = tacheCheval.getObject();

		final Thread chavalThread = new Thread(tacheCheval);
		chavalThread.setDaemon(true);
		chavalThread.start();

		this.bind(idNomCheval, json.getChevalName());
		this.bind(idRaceCheval, json.getRace());
		this.bind(idPereCheval, json.getPere(), value -> "Père : " + value);
		this.bind(idMereCheval, json.getMere(), value -> "Mère : " + value);
		this.bind(idPereMereCheval, json.getPereMere(), value -> "Père de mère : " + value);
		this.bind(idNomCavalier, json.getCavalier(), (value) -> {
			String[] parts = value.split(" ", -1);
			String nom = parts.length == 2 ? parts[1].split("_")[0] : "";
			return nom.toUpperCase();
		});
		this.bind(idPrenomCavalier, json.getCavalier(), (value) -> {
			String[] parts = value.split("_");
			String prenom = parts.length > 1 ? parts[parts.length - 1] : value;
			return capitalizeFirstOnly(prenom);
		});

		// gestion visibilité pane principaux

		idGridpaneChrono.visibleProperty().bind(idChrono.visibleProperty());
		idChrono.visibleProperty().addListener(addFadeTransition(idGridpaneChrono));

		BooleanBinding allLabelsVisible = Bindings.createBooleanBinding(() -> false);
		for (final Label label : labelTacheMap.keySet()) {
			allLabelsVisible = Bindings.or(allLabelsVisible, label.visibleProperty());
		}
		allLabelsVisible = Bindings.or(allLabelsVisible, idNomCheval.visibleProperty())
				.or(idRaceCheval.visibleProperty())
				.or(idPereCheval.visibleProperty())
				.or(idMereCheval.visibleProperty())
				.or(idPereMereCheval.visibleProperty())
				.or(idNomCavalier.visibleProperty());
		idGridpaneInfo.visibleProperty().bind(allLabelsVisible);
		allLabelsVisible.addListener(addFadeTransition(idGridpaneInfo));

		//		Gestion responsive
		idGridpaneInfo.prefHeightProperty().bind(idAnchorBase.heightProperty().multiply(ControllerVideo.PANEL_INFO_COUREUR_HEIGHT_PERCENTAGE));
		idGridpaneEpreuve.prefHeightProperty().bind(idAnchorBase.heightProperty().multiply(ControllerVideo.PANEL_INFO_EPREUVE_HEIGHT_PERCENTAGE));

		idAnchorBase.heightProperty().addListener((obs, oldV, newV) -> {
			final double heightRatio = newV.doubleValue()/ ControllerVideo.BASE_HEIGHT;
			AnchorPane.setTopAnchor(idGridpaneEpreuve, 33 * heightRatio);
		});
		idGridpaneEpreuve.heightProperty().addListener((obs, oldV, newV) -> {
			idLogo.setFitHeight(newV.doubleValue());
			idLogo.setFitWidth(newV.doubleValue() * ((double) 73/88));
		});
		GridPane.setFillWidth(idImageChrono, false);
		GridPane.setFillHeight(idImageChrono, false);

		AtomicReference<Scene> atomicScene = new AtomicReference<>();
		idAnchorBase.sceneProperty().addListener((obsScene, oldVScene, newScene) -> {
			atomicScene.set(newScene);
			newScene.windowProperty().addListener((obsWindow, oldWindow, newWindow) -> {
				Stage stage = (Stage) newWindow;
				stage.setOnShown(event -> {
					newScene.getRoot().requestLayout();
				});
			});
			newScene.widthProperty().addListener((obs, oldV, newV) -> {
				idImageChrono.setFitWidth(35 * newV.doubleValue() / BASE_WIDTH);
				idImageCavalier.setFitWidth(35 * newV.doubleValue() / BASE_WIDTH);
			});
			newScene.heightProperty().addListener((obs, oldV, newV) -> {
				idImageChrono.setFitWidth(35 * newScene.getWidth() / BASE_WIDTH);
				idImageCavalier.setFitWidth(35 * newScene.getWidth() / BASE_WIDTH);
			});
		});
		this.bindLabelSize(idLieu, 16,atomicScene);
		this.bindLabelSize(idNumeroEpreuve, 16,atomicScene);
		this.bindLabelSize(idPereCheval, 16,atomicScene);
		this.bindLabelSize(idMereCheval, 16,atomicScene);
		this.bindLabelSize(idPereMereCheval, 16,atomicScene);
		this.bindLabelSize(idPrenomCavalier, 16,atomicScene);
		this.bindLabelSize(idNomCavalier, 16,atomicScene);
		this.bindLabelSize(idPenalite, 23,atomicScene);
		this.bindLabelSize(idNomCheval, 23,atomicScene);
		this.bindLabelSize(idRaceCheval, 23,atomicScene);
		this.bindLabelSize(idDossard, 23,atomicScene);
		this.bindLabelSize(idChrono, 23,atomicScene);

		// Lancement des process de récéption
		labelTacheMap.values().forEach(tache -> {
			final Thread t = new Thread(tache);
			t.setDaemon(true);
			t.start();
		});
	}

	private void bindLabelSize(Label label, int defaultSize, AtomicReference<Scene> atomicScene) {
		ChangeListener<? super Number> listener = (obs, oldV, newV) -> {
			if(atomicScene.get() == null) {
				return;
			}

			double width = atomicScene.get().getWidth();
			if(width == 0) {
				return;
			}

			setLabelTextSize(label, defaultSize, atomicScene.get().getWidth());
		};

		label.widthProperty().addListener(listener);
		label.heightProperty().addListener(listener);
	}

	private void setLabelTextSize(final Label label, final int defaultSize, double sceneWidth) {
		final double height = label.getHeight();
        final double widthRatio = sceneWidth/ControllerVideo.BASE_WIDTH;
		final double textWidth = Math.min(height, widthRatio * defaultSize);
		label.setFont(Font.font(label.getFont().getFamily(), FontWeight.findByName(label.getFont().getStyle()), widthRatio * defaultSize));
	}

	private ChangeListener<? super Boolean> addFadeTransition(final Node node) {
		return (obs, oldV, newV) -> {
			final FadeTransition transition = new FadeTransition(Duration.millis(ControllerVideo.FADE_DURATION), node);
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

	private String capitalizeFirstOnly(String value) {
		if(value == null) {
			return null;
		}
		if(value.trim().isEmpty()) {
			return "";
		}

		return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
	}

	private void bind(final Label label, final RawTacheReception tacheReception) {
		label.textProperty().bind(tacheReception.valueProperty());
		setVisibilityEvent(label);
	}

	private void bind(Label label, StringObsProperty stringObsProperty, Function<String, String> supplier) {
		label.textProperty().bind(Bindings.createStringBinding(() -> {
			StringProperty property = stringObsProperty.getValue();
			if(property == null) {
				return "";
			}

			String value = property.getValue();
			if(value == null || value.trim().isEmpty()) {
				return "";
			}

			return supplier.apply(property.get());
		}, stringObsProperty.getValue()));
		this.setVisibilityEvent(label);
	}

	private void bind(Label label, StringObsProperty stringProperty) {
		label.textProperty().bind(stringProperty.getValue());
		setVisibilityEvent(label);
	}

	private void setVisibilityEvent(Label label)  {
		label.visibleProperty().bind(Bindings.when(
						label.textProperty().isEmpty().or(label.textProperty().isEqualTo("0").or(label.textProperty().isEqualTo("0.0").or(label.textProperty().isEqualTo("0.00")))))
				.then(false)
				.otherwise(true));
		label.visibleProperty().addListener((obs, oldV, newV) -> {
			final FadeTransition transition = new FadeTransition(Duration.millis(ControllerVideo.FADE_DURATION), label);
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
