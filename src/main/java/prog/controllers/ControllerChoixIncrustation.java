package prog.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import prog.utils.ResourceUtils;

import java.io.IOException;

public class ControllerChoixIncrustation extends AbstractController {

    @FXML
    private AnchorPane idAnchorBase;

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
}
