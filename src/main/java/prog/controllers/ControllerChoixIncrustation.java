package prog.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ControllerChoixIncrustation extends AbstractController {

    @FXML
    private void showBasicIncrustation(MouseEvent event) {
        try {
            switchScene("basic_incrustation.fxml");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void showSHFIncrustation(MouseEvent event) {
        try {
            switchScene("SHF_incrustation.fxml");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void switchScene(String fxmlName) throws IOException {
        Stage actualStage = (Stage) getAtomicScene().get().getWindow();
        actualStage.close();

        final Stage stage = new Stage();
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/" + fxmlName));
        final Parent sceneVideo = loader.load();
        final Scene scene = new Scene(sceneVideo);
        stage.setMinWidth(640);
        stage.setMinHeight(360);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
}
