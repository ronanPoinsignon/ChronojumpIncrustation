package prog.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerChoixIncrustation extends AbstractController {

    @FXML
    private void showBasicIncrustation(MouseEvent event) {
        try {
            switchScene("/fxml/basic_incrustation.fxml");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void showSHFIncrustation(MouseEvent event) {
        try {
            switchScene("/fxml/SHF_incrustation.fxml");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void addEvent(Stage stage) {

    }
}
