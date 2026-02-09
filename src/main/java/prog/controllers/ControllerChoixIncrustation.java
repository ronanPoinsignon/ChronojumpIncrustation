package prog.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import prog.executor.ControllerExecutor;
import prog.utils.FxmlIncrustation;
import prog.utils.Panel;

import java.io.IOException;

public class ControllerChoixIncrustation extends AbstractController {

    private final ControllerExecutor controllerExecutor = ControllerExecutor.getExecutor();

    @FXML
    private void showBasicIncrustation(MouseEvent event) {
        controllerExecutor.close(this);
        controllerExecutor.show(FxmlIncrustation.BASIC, Panel.MAIN_PANEL);
    }

    @FXML
    private void showSHFIncrustation(MouseEvent event) {
        controllerExecutor.close(this);
        controllerExecutor.show(FxmlIncrustation.SHF, Panel.MAIN_PANEL);
    }

    @Override
    protected void addEvent(Stage stage) {

    }
}
