package prog.controllers;

import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public abstract class AbstractController implements Initializable {

    private static final int FADE_DURATION = 200;

    private static final String INIT_IP_ADRESSE = "192.168.1.31";
//	private static final String INIT_IP_ADRESSE = "169.254.122.66";

    protected static final int BASE_WIDTH = 1280;

    private final Map<ObservableValue, List<ChangeListener<?>>> LISTENER_MAP = new HashMap<>();

    private boolean isFullScreen = false;
    private double coordX, coordY;
    private final AtomicReference<Scene> ATOMIC_SCENE = new AtomicReference<>();

    @FXML
    private AnchorPane idAnchorBase;

    public AbstractController() {
        AbstractTacheReception.setAdresse(INIT_IP_ADRESSE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListener(idAnchorBase.sceneProperty(), (obsScene, oldVScene, newScene) -> {
            if(newScene == null) {
                return;
            }

            onSceneUpdate(newScene);
        });
    }

    protected void bindLabelSize(Label label, int defaultSize, Scene scene) {
        addListener(scene.widthProperty(), (obs, oldV, newV) -> {
            double width = scene.getWidth();
            if(width == 0) {
                return;
            }

            setLabelTextSize(label, defaultSize, scene.getWidth());
        });
    }

    protected DoubleBinding getWidthRatio(ReadOnlyDoubleProperty defaultProperty) {
        return defaultProperty.divide((double) BASE_WIDTH);
    }

    protected void setLabelTextSize(final Label label, final int defaultSize, double sceneWidth) {
        double textSize = getMultiplicateur(sceneWidth, defaultSize);
        label.setFont(Font.font(label.getFont().getFamily(), FontWeight.findByName(label.getFont().getStyle()), textSize));
    }

    protected double getWidthRatio(double sceneWidth) {
        return sceneWidth / BASE_WIDTH;
    }

    protected double getMultiplicateur(double sceneWidth, double defaultSize) {
        final double widthRatio = getWidthRatio(sceneWidth);
        return widthRatio * defaultSize;
    }

    protected void setImageSize(ImageView imageView, Scene scene, int defaultSize) {
        imageView.fitWidthProperty().bind(scene.widthProperty().multiply(defaultSize).divide(AbstractController.BASE_WIDTH));
        imageView.fitHeightProperty().bind(scene.widthProperty().multiply(defaultSize).divide(AbstractController.BASE_WIDTH));
    }

    protected ChangeListener<Boolean> addFadeTransition(final Node node) {
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
        addListener(label.visibleProperty(), (obs, oldV, newV) -> {
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
        idAnchorBase.setOnMouseDragged(evt -> {
            if(isFullScreen) {
                return;
            }

            anchorBaseOnMouseDragged(evt, stage);
        });
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
        return ATOMIC_SCENE;
    }

    protected void onSceneUpdate(Scene scene) {
        ATOMIC_SCENE.set(scene);
        addListener(scene.windowProperty(), (obsWindow, oldWindow, newWindow) -> {
            if(newWindow == null) {
                return;
            }

            addEvent((Stage) newWindow);
        });
    }

    protected void switchScene(String fxml) throws IOException {
        removeAllListener();
        FXMLLoader loader = new FXMLLoader(ResourceUtils.getResource(fxml));
        Parent next = loader.load();
        Scene scene = new Scene(next);
        Stage appStage = (Stage) this.getAtomicScene().get().getWindow();
        AbstractController controller = loader.getController();
        controller.isFullScreen = this.isFullScreen;
        appStage.setScene(scene);
    }

    protected <T> void addListener(ObservableValue<T> property, ChangeListener<T> listener) {
        property.addListener(listener);
        if(LISTENER_MAP.containsKey(property)) {
            LISTENER_MAP.get(property).add(listener);
        } else {
            List<ChangeListener<?>> liste = new ArrayList<>();
            liste.add(listener);
            LISTENER_MAP.put(property, liste);
        }
    }

    private void removeAllListener() {
        for(Map.Entry<ObservableValue, List<ChangeListener<?>>> entry : LISTENER_MAP.entrySet()) {
            ObservableValue property = entry.getKey();
            for(ChangeListener<?> listener : entry.getValue()) {
                property.removeListener(listener);
            }
        }
    }

}
