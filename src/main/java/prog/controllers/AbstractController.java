package prog.controllers;

import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import prog.observableproperties.StringObsProperty;
import prog.transmission.tache.AbstractTacheReception;
import prog.transmission.tache.RawTacheReception;
import prog.utils.ResourceUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public abstract class AbstractController implements Initializable {

    private static final int FADE_DURATION = 200;

//    private static final String INIT_IP_ADRESSE = "192.168.1.20";
	private static final String INIT_IP_ADRESSE = "169.254.122.66";

    protected static final int BASE_WIDTH = 1280;

    private boolean isFullScreen = false;
    private double coordX, coordY;
    private final AtomicReference<Scene> atomicScene = new AtomicReference<>();

    @FXML
    private AnchorPane idAnchorBase;

    public AbstractController() {
        AbstractTacheReception.setAdresse(INIT_IP_ADRESSE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idAnchorBase.sceneProperty().addListener((obsScene, oldVScene, newScene) -> {
            if(newScene == null) {
                return;
            }

            onSceneUpdate(newScene);
        });
    }

    protected void bindLabelSize(Label label, int defaultSize, Scene scene) {
        ChangeListener<? super Number> listener = (obs, oldV, newV) -> {
            if(scene == null) {
                return;
            }

            double width = scene.getWidth();
            if(width == 0) {
                return;
            }

            setLabelTextSize(label, defaultSize, scene.getWidth());
        };

        scene.widthProperty().addListener(listener);
    }

    protected void setLabelTextSize(final Label label, final int defaultSize, double sceneWidth) {
        final double widthRatio = sceneWidth/ BASE_WIDTH;
        double textSize = widthRatio * defaultSize;
        label.setFont(Font.font(label.getFont().getFamily(), FontWeight.findByName(label.getFont().getStyle()), textSize));
    }

    protected void setImageSize(ImageView imageView, Scene scene, int defaultSize) {
        imageView.fitWidthProperty().bind(scene.widthProperty().multiply(defaultSize).divide(AbstractController.BASE_WIDTH));
        imageView.fitHeightProperty().bind(scene.widthProperty().multiply(defaultSize).divide(AbstractController.BASE_WIDTH));
    }

    protected ChangeListener<? super Boolean> addFadeTransition(final Node node) {
        return (obs, oldV, newV) -> {
            final FadeTransition transition = new FadeTransition(Duration.millis(FADE_DURATION), node);
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

    protected void bind(final Label label, final RawTacheReception tacheReception) {
        this.bind(label, tacheReception.valueProperty());
    }

    protected void bind(final Label label, final ReadOnlyObjectProperty<String> property) {
        label.textProperty().bind(property);
        setVisibilityEvent(label);
    }

    protected void bind(Label label, StringObsProperty stringObsProperty, Function<String, String> supplier) {
        label.textProperty().bind(Bindings.createStringBinding(() -> {
            StringProperty property = stringObsProperty.getValue();
            if(property == null) {
                return "";
            }

            String value = property.getValue();
            if(value == null) {
                return "";
            }

            String trimValue = property.getValue().trim();
            if(trimValue.isEmpty()) {
                return "";
            }

            return supplier.apply(trimValue);
        }, stringObsProperty.getValue()));
        this.setVisibilityEvent(label);
    }

    protected void bind(Label label, StringObsProperty stringProperty) {
        bind(label, stringProperty, String::trim);
    }

    protected void setVisibilityEvent(Label label)  {
        label.visibleProperty().bind(Bindings.when(
                        label.textProperty().isEmpty().or(label.textProperty().isEqualTo("0").or(label.textProperty().isEqualTo("0.0").or(label.textProperty().isEqualTo("0.00")))))
                .then(false)
                .otherwise(true));
        label.visibleProperty().addListener((obs, oldV, newV) -> {
            final FadeTransition transition = new FadeTransition(Duration.millis(FADE_DURATION), label);
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

    protected void addEvent(final Stage stage) {
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.getScene().setOnKeyPressed(evt -> {
            if(evt.getCode() == KeyCode.F12 || isFullScreen && evt.getCode() == KeyCode.ESCAPE) {
                switchFullScreen(stage);
            }
        });
        idAnchorBase.setOnMousePressed(evt -> anchorBaseOnMousePressed(evt, stage));
        idAnchorBase.setOnMouseDragged(evt -> anchorBaseOnMouseDragged(evt, stage));
    }

    protected void anchorBaseOnMousePressed(MouseEvent evt, Stage stage) {
        if(!MouseButton.PRIMARY.equals(evt.getButton())) {
            return;
        }

        coordX = evt.getX();
        coordY = evt.getY();
        if(evt.getClickCount()%2 == 0) {
            switchFullScreen(stage);
        }
    }

    protected void anchorBaseOnMouseDragged(MouseEvent evt, Stage stage) {
        if(isFullScreen) {
            evt.consume();
            return;
        }
        stage.setX(evt.getScreenX() - coordX);
        stage.setY(evt.getScreenY() - coordY);
    }

    protected void switchFullScreen(final Stage stage) {
        stage.setMaximized(isFullScreen = !isFullScreen);
    }

    protected AtomicReference<Scene> getAtomicScene() {
        return atomicScene;
    }

    protected void onSceneUpdate(Scene scene) {
        atomicScene.set(scene);
        scene.windowProperty().addListener((obsWindow, oldWindow, newWindow) -> {
            if(newWindow == null) {
                return;
            }

            addEvent((Stage) newWindow);
        });
    }

    protected void switchScene(String fxml) throws IOException {
        Parent next = FXMLLoader.load(ResourceUtils.getResource(fxml));
        Scene scene = new Scene(next);
        Stage appStage = (Stage) this.getAtomicScene().get().getWindow();
        appStage.setScene(scene);
    }

}
