package prog.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import prog.utils.ResourceUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AbstractControllerIncrustation extends AbstractController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        try {
            openSecondaryPannel();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openSecondaryPannel() throws IOException {
        final Stage stage = new Stage();
        final FXMLLoader loader = new FXMLLoader(ResourceUtils.getResource("/fxml/panneau_incrustation.fxml"));
        final Parent sceneVideo = loader.load();
        final Scene scene = new Scene(sceneVideo);
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
}
