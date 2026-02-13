package prog.executor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import prog.controllers.AbstractController;
import prog.transmission.EventObserver;
import prog.utils.FxmlIncrustation;
import prog.utils.Panel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ControllerExecutor {

    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 720;
    private static ControllerExecutor controllerExecutor;

    private final EventObserver eventObserver = EventObserver.getInstance();

    private final Map<AbstractController, Stage> controllerMap = new HashMap<>();
    private final Map<Panel, AbstractController> panelToControllerMap = new HashMap<>();
    private final Map<AbstractController, Panel> controllerToPanelMap = new HashMap<>();

    private ControllerExecutor() {
        gestionEvent();
    }

    private void gestionEvent() {
        eventObserver.getIncrustationAction().addListener((obs, oldEvent, newEvent) -> newEvent.execute());
    }

    public static ControllerExecutor getExecutor() {
        if(controllerExecutor == null) {
            controllerExecutor = new ControllerExecutor();
        }

        return controllerExecutor;
    }

    public void show(FxmlIncrustation fxml, Panel panel) {
        this.show(fxml, panel, stage -> {
            stage.setWidth(DEFAULT_WIDTH);
            stage.setHeight(DEFAULT_HEIGHT);
            stage.initStyle(StageStyle.UNDECORATED);
        });
    }

    public void show(FxmlIncrustation fxml, Panel panel, Consumer<Stage> stageConsumer) {
        final Stage stage = new Stage();
        final FXMLLoader loader = new FXMLLoader(fxml.getPathAsResource());
        final Parent sceneVideo;
        try {
            sceneVideo = loader.load();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        final Scene scene = new Scene(sceneVideo);
        stage.setScene(scene);
        stageConsumer.accept(stage);

        AbstractController controller = loader.getController();
        if(panel == null) {
            stage.show();
            controllerMap.put(controller, stage);
            return;
        }

        if(!panelToControllerMap.containsKey(panel)) {
            stage.show();
            controllerMap.put(controller, stage);
            panelToControllerMap.put(panel, controller);
            controllerToPanelMap.put(controller, panel);
        } else {
            AbstractController lastController = panelToControllerMap.get(panel);
            this.switchScene(lastController, fxml);
        }
    }

    public void close(AbstractController controller) {
        controller.onClose();
        controllerMap.remove(controller).close();
    }

    public void switchScene(Panel panel, FxmlIncrustation fxml) {
        AbstractController controller = panelToControllerMap.get(panel);
        if(controller == null) {
            return;
        }

        this.switchScene(controller, fxml);
    }

    public void switchScene(AbstractController controller, FxmlIncrustation fxml) {
        FXMLLoader loader = new FXMLLoader(fxml.getPathAsResource());
        Parent next;
        try {
            next = loader.load();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(next);
        Stage appStage = controller.getStage();
        AbstractController newController = loader.getController();
        newController.setFullScreen(controller.isFullScreen());
        controller.onClose();
        appStage.setScene(scene);

        controllerMap.remove(controller);
        controllerMap.put(newController, appStage);
        Panel panel = controllerToPanelMap.remove(controller);
        panelToControllerMap.put(panel, newController);
        controllerToPanelMap.put(newController, panel);
    }
}
