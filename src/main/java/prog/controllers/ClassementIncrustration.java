package prog.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import prog.observableproperties.json.ClassementCavalier;
import prog.transmission.EventObserver;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClassementIncrustration extends AbstractController {

    private static final double PERCENTAGE_COLUMN_CLASSEMENT = 0.05;
    private static final double PERCENTAGE_COLUMN_CAVALIER = 0.95;
    public static final int NB_SHOWN_CAVALIERS = 8;

    private final EventObserver eventObserver = EventObserver.getInstance();

    @FXML
    private TableView<ClassementCavalier> idTableClassement;
    @FXML
    private TableColumn<ClassementCavalier, Integer> idColumnClassement;
    @FXML
    private TableColumn<ClassementCavalier, String> idColumnCavalier;

    // style table view
    private final StringProperty fontFamily = new SimpleStringProperty(Font.getDefault().getFamily());
    private final ObjectProperty<FontWeight> fontWeight = new SimpleObjectProperty<>(FontWeight.NORMAL);
    private final IntegerProperty fontSize = new SimpleIntegerProperty(35);

    private final StringProperty style = new SimpleStringProperty();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        idTableClassement.itemsProperty().bind(eventObserver.getClassementCavalierList());
        idColumnClassement.setCellValueFactory(param -> {
            ObjectProperty<Integer> property = new SimpleObjectProperty<>();
            Integer classement = param.getValue().getClassement();
            property.set(classement);
            return property;
        });
        idColumnCavalier.setCellValueFactory(param -> {
            StringProperty property = new SimpleStringProperty();
            property.set(param.getValue().getCavalier());
            return property;
        });
        idColumnClassement.minWidthProperty().bind(idTableClassement.widthProperty().multiply(PERCENTAGE_COLUMN_CLASSEMENT));
        idColumnClassement.prefWidthProperty().bind(idTableClassement.widthProperty().multiply(PERCENTAGE_COLUMN_CLASSEMENT));
        idColumnClassement.maxWidthProperty().bind(idTableClassement.widthProperty().multiply(PERCENTAGE_COLUMN_CLASSEMENT));
        idColumnCavalier.minWidthProperty().bind(idTableClassement.widthProperty().multiply(PERCENTAGE_COLUMN_CAVALIER));
        idColumnCavalier.prefWidthProperty().bind(idTableClassement.widthProperty().multiply(PERCENTAGE_COLUMN_CAVALIER));
        idColumnCavalier.maxWidthProperty().bind(idTableClassement.widthProperty().multiply(PERCENTAGE_COLUMN_CAVALIER));

        // gestion style table view
        style.bind(Bindings.createStringBinding(() -> String.format(
                        "-fx-font-family: %s;\n"
                                + "-fx-font-weight: %d;\n"
                                + "-fx-font-size: %d;\n",
                        fontFamily.get(),
                        fontWeight.get().getWeight(),
                        fontSize.get()
                ),
                fontFamily,
                fontWeight,
                fontSize
        ));
        idTableClassement.fixedCellSizeProperty().bind(idTableClassement.heightProperty().divide(NB_SHOWN_CAVALIERS));
        idTableClassement.setRowFactory(tv -> {
            TableRow<ClassementCavalier> row = new TableRow<>();
            row.styleProperty().bind(style);
            return row ;
        });
        idColumnClassement.setCellFactory(tc -> {
            TableCell<ClassementCavalier, Integer> cell = new TableCell<ClassementCavalier, Integer>() {
                @Override
                public void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(getString());
                    setGraphic(null);
                }

                private String getString() {
                    return getItem() == null ? "" : getItem().toString() + ".";
                }
            };

            cell.setStyle("-fx-alignment: CENTER;");
            return cell;
        });
        idColumnCavalier.setCellFactory(tc -> {
            TableCell<ClassementCavalier, String> cell = new TableCell<ClassementCavalier, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(getString());
                    setGraphic(null);
                }

                private String getString() {
                    return getItem() == null ? "" : getItem();
                }
            };

            cell.setStyle("-fx-alignment: CENTER-LEFT;");
            return cell;
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

    @Override
    protected void onSceneUpdate(Scene scene) {
        super.onSceneUpdate(scene);
        fontSize.bind(getWidthRatio(scene.widthProperty()).multiply(fontSize.get()));
    }
}
