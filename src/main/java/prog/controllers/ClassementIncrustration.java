package prog.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import prog.executor.ControllerExecutor;
import prog.observableproperties.json.ClassementCavalier;
import prog.transmission.EventObserver;
import prog.utils.FxUtils;
import prog.utils.FxmlIncrustation;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class ClassementIncrustration extends AbstractController {

    private static final double PERCENTAGE_COLUMN_CLASSEMENT = 0.05;
    private static final double PERCENTAGE_COLUMN_CAVALIER = 0.95;
    private static final int NB_SHOWN_CAVALIERS = 8;

    private final EventObserver eventObserver = EventObserver.getInstance();

    private final ControllerExecutor controllerExecutor = ControllerExecutor.getExecutor();

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
        idColumnClassement.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getClassement()));
        idColumnCavalier.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCavalier()));
        FxUtils.bindTableColumnWidth(idTableClassement, Arrays.asList(idColumnClassement, idColumnCavalier), Arrays.asList(PERCENTAGE_COLUMN_CLASSEMENT, PERCENTAGE_COLUMN_CAVALIER));

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
        idColumnClassement.setCellFactory(tc -> FxUtils.createTableCell(value -> value + ".", Pos.CENTER));
        idColumnCavalier.setCellFactory(tc -> FxUtils.createTableCell(String::toString, Pos.CENTER_LEFT));
    }

    @Override
    protected void onSceneUpdate(Scene scene) {
        super.onSceneUpdate(scene);
        fontSize.bind(getWidthRatio().multiply(fontSize.get()));
    }
}
