package prog.controllers;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import prog.element.Element;
import prog.element.ElementProperties;
import prog.observableproperties.DoubleObsProperty;
import prog.transmission.TacheReception;

public class ControllerVideo implements Initializable {

	@FXML
	BorderPane borderPane;

	@FXML
	BorderPane borderPaneBot;

	@FXML
	GridPane gridPaneDroit;

	@FXML
	GridPane gridPaneGauche;

	public static final int PORT_COUREUR = 8090;
	public static final int PORT_CHEVAL = 8091;
	public static final int PORT_PRENALITE = 8092;
	public static final int PORT_CHRONO = 8093;

	public static final String INIT_IP_ADRESSE = "192.168.1.36";

	//GridPane
	private static final double PERCENTAGE_HEIGHT_BORDERPANE_BOT = 0.13;
	private final DoubleObsProperty tailleYBorderPaneBot = new DoubleObsProperty();

	Element elementCoureur = new Element(ElementProperties.COUREUR_PROPERTIES);
	Element elementCheval = new Element(ElementProperties.CHEVAL_PROPERTIES);
	Element elementPenalite = new Element(ElementProperties.PENALITE_PROPERTIES);
	Element elementChrono = new Element(ElementProperties.CHRONO_PROPERTIES);

	private double coordX, coordY;
	private boolean isFullScreen = false;


	@Override
	public void initialize(final URL arg0, final ResourceBundle arg1) {
		elementCoureur.getTexte().setText("coureur");
		elementCheval.getTexte().setText("cheval");
		elementPenalite.getTexte().setText("10");
		elementChrono.getTexte().setText("100");
		borderPaneBot.prefHeightProperty().bind(tailleYBorderPaneBot.doubleProperty());

		GridPane.setHalignment(elementCoureur, HPos.LEFT);
		StackPane.setAlignment(elementCoureur.getBg(), Pos.BOTTOM_CENTER);
		StackPane.setAlignment(elementCoureur.getTexte(), Pos.BOTTOM_LEFT);

		GridPane.setHalignment(elementCheval, HPos.LEFT);
		StackPane.setAlignment(elementCheval.getBg(), Pos.TOP_CENTER);
		StackPane.setAlignment(elementCheval.getTexte(), Pos.TOP_LEFT);

		GridPane.setHalignment(elementPenalite, HPos.RIGHT);
		GridPane.setHalignment(elementChrono, HPos.RIGHT);

		borderPane.setBackground(new Background(new BackgroundFill(Color.LAWNGREEN, null, null)));

		StackPane.setAlignment(elementChrono, Pos.CENTER_RIGHT);

		elementCoureur.visibleProperty().bind(Bindings.when(
				elementCoureur.getTexte().textProperty().isEmpty().or(elementCoureur.getTexte().textProperty().isEqualTo("0")))
				.then(false)
				.otherwise(true));
		elementCoureur.getBg().setFill(Color.CORNFLOWERBLUE);
		elementCoureur.getBg().setOpacity(0.7);
		gridPaneGauche.add(elementCoureur, 0, 0);

		elementCheval.visibleProperty().bind(Bindings.when(
				elementCheval.getTexte().textProperty().isEmpty().or(elementCoureur.getTexte().textProperty().isEqualTo("0")))
				.then(false)
				.otherwise(true));
		elementCheval.getBg().setFill(Color.DARKBLUE);
		elementCheval.getTexte().setTextFill(Color.WHITE);
		elementCheval.getBg().setOpacity(0.7);
		gridPaneGauche.add(elementCheval, 0, 1);

		elementPenalite.visibleProperty().bind(Bindings.when(
				elementPenalite.getTexte().textProperty().isEmpty().or(elementPenalite.getTexte().textProperty().isEqualTo("0")))
				.then(false)
				.otherwise(true));
		elementPenalite.getBg().setFill(Color.CORNFLOWERBLUE);
		gridPaneDroit.add(elementPenalite, 0, 0);

		elementChrono.visibleProperty().bind(Bindings.when(
				elementChrono.getTexte().textProperty().isEmpty().or(elementChrono.getTexte().textProperty().isEqualTo("0").or(elementChrono.getTexte().textProperty().isEqualTo("0.0").or(elementChrono.getTexte().textProperty().isEqualTo("0.00")))))
				.then(false)
				.otherwise(true));
		elementChrono.getBg().setFill(Color.CORNFLOWERBLUE);
		gridPaneDroit.add(elementChrono, 0, 1);
		final Thread th = new Thread(() -> {
			TacheReception.setAdresse(ControllerVideo.INIT_IP_ADRESSE);
			final TacheReception tacheCoureur = new TacheReception(ControllerVideo.PORT_COUREUR);
			final TacheReception tacheCheval = new TacheReception(ControllerVideo.PORT_CHEVAL);
			final TacheReception tachePenalite = new TacheReception(ControllerVideo.PORT_PRENALITE);
			final TacheReception tacheChrono = new TacheReception(ControllerVideo.PORT_CHRONO);
			final List<TacheReception> taches = Arrays.asList(tacheCoureur, tacheCheval, tachePenalite, tacheChrono);
			elementCoureur.getTexte().textProperty().bind(tacheCoureur.valueProperty());
			elementCheval.getTexte().textProperty().bind(tacheCheval.valueProperty());
			elementPenalite.getTexte().textProperty().bind(tachePenalite.valueProperty());
			elementChrono.getTexte().textProperty().bind(tacheChrono.valueProperty());
			taches.forEach(tache -> {
				final Thread t = new Thread(tache);
				t.setDaemon(true);
				t.start();
			});
		});
		th.setDaemon(true);
		th.start();
	}

	public void setDimension(final Stage stage) {
		stage.setHeight(400);
		tailleYBorderPaneBot.setDouble(stage.getHeight() * ControllerVideo.PERCENTAGE_HEIGHT_BORDERPANE_BOT);

		ElementProperties.setDimensions(stage.getWidth(), stage.getHeight());
		elementCoureur.getTexte().fontProperty().set(new Font(elementCoureur.getProps().getTailleY().getDouble() / 1.3));
		elementCheval.getTexte().fontProperty().set(new Font(elementCheval.getProps().getTailleY().getDouble() / 1.3));
		elementPenalite.getTexte().fontProperty().set(new Font(elementPenalite.getProps().getTailleY().getDouble() / 1.3));
		elementChrono.getTexte().fontProperty().set(new Font(elementChrono.getProps().getTailleY().getDouble() / 1.3));

		tailleYBorderPaneBot.setDouble(stage.getHeight() * ControllerVideo.PERCENTAGE_HEIGHT_BORDERPANE_BOT);

		GridPane.setMargin(elementCoureur, new Insets(0, 0, 0, stage.getWidth() / 100));
		GridPane.setMargin(elementCheval, new Insets(0, 0, 0, stage.getWidth() / 25));
		GridPane.setMargin(elementPenalite, new Insets(0, stage.getWidth() / 100, 0, 0));
		GridPane.setMargin(elementChrono, new Insets(0, stage.getWidth() / 100, 0, 0));
	}

	public void addevent(final Stage stage) {
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.getScene().setOnKeyPressed(evt -> {
			if(evt.getCode() == KeyCode.F12 || isFullScreen && evt.getCode() == KeyCode.ESCAPE) {
				setFullScreen(stage);
			}
		});
		borderPane.setOnMousePressed(evt -> {
			coordX = evt.getX();
			coordY = evt.getY();
			if(evt.getClickCount()%2 == 0) {
				setFullScreen(stage);
			}
		});
		borderPane.setOnMouseDragged(evt -> {
			if(isFullScreen) {
				evt.consume();
				return;
			}
			stage.setX(evt.getScreenX() - coordX);
			stage.setY(evt.getScreenY() - coordY);
		});
		elementChrono.getBg().widthProperty().unbind();
		elementChrono.getTexte().textProperty().addListener((obs, oldV, newV) -> {
			final Text text = new Text(newV);
			text.fontProperty().set(new Font(elementChrono.getProps().getTailleY().getDouble()));
			elementChrono.getBg().setWidth(text.getLayoutBounds().getWidth() + 5);
		});
		stage.widthProperty().addListener((obs, oldV, newV) -> {
			ElementProperties.setWidths(newV.doubleValue());
			GridPane.setMargin(elementCoureur, new Insets(0, 0, 0, newV.doubleValue() / 100));
			GridPane.setMargin(elementCheval, new Insets(0, 0, 0, newV.doubleValue() / 25));
			GridPane.setMargin(elementPenalite, new Insets(0, newV.doubleValue() / 100, 0, 0));
			GridPane.setMargin(elementChrono, new Insets(0, newV.doubleValue() / 100, 0, 0));
		});
		stage.heightProperty().addListener((obs, oldV, newV) -> {
			ElementProperties.setHeights(newV.doubleValue());
			elementChrono.getTexte().fontProperty().set(new Font(elementChrono.getProps().getTailleY().getDouble() / 1.3));
			elementCoureur.getTexte().fontProperty().set(new Font(elementCoureur.getProps().getTailleY().getDouble() / 1.3));
			elementCheval.getTexte().fontProperty().set(new Font(elementCheval.getProps().getTailleY().getDouble() / 1.3));
			elementPenalite.getTexte().fontProperty().set(new Font(elementPenalite.getProps().getTailleY().getDouble() / 1.3));
			tailleYBorderPaneBot.setDouble(newV.doubleValue() * ControllerVideo.PERCENTAGE_HEIGHT_BORDERPANE_BOT);
		});
		stage.setOnCloseRequest(evt -> {
			System.out.println("setOnClosed");
			//evt.consume();
		});
	}

	public void setFullScreen(final Stage stage) {
		stage.setFullScreen(isFullScreen = !isFullScreen);
	}
}
