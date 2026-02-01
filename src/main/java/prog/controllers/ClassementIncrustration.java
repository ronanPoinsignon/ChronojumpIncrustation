package prog.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import prog.observableproperties.json.ClassementCavalier;
import prog.transmission.EventObserver;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClassementIncrustration extends AbstractController {

    EventObserver eventObserver = EventObserver.getInstance();

    @FXML
    private ListView<ClassementCavalier> idTableClassement;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        idTableClassement.itemsProperty().bind(eventObserver.getClassementCavalierList());
        idTableClassement.setCellFactory(param -> new ListCell<ClassementCavalier>() {
            @Override
            protected void updateItem(ClassementCavalier item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getCavalier() == null) {
                    setText(null);
                } else {
                    setText(item.getCavalier());
                }
            }
        });

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> {
                try {
                    switchScene("/fxml/panneau_incrustation.fxml");
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }).start();
    }

}
