package prog.utils;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Function;

public class FxUtils {

    public static void setImageSize(ImageView imageView, Scene scene, int defaultSize, double baseWidth) {
        imageView.fitWidthProperty().bind(scene.widthProperty().multiply(defaultSize).divide(baseWidth));
        imageView.fitHeightProperty().bind(scene.widthProperty().multiply(defaultSize).divide(baseWidth));
    }

    public static void bindTableColumnWidth(TableView<?> table, List<TableColumn<?, ?>> columnList, List<Double> percentageList) {
        if(columnList.size() != percentageList.size()) {
            throw new ArrayIndexOutOfBoundsException("Arrays are not the same size.");
        }

        int size = columnList.size();
        for(int i = 0; i < size; i++) {
            TableColumn<?, ?> column = columnList.get(i);
            double percentage = percentageList.get(i);
            column.minWidthProperty().bind(table.widthProperty().multiply(percentage));
            column.prefWidthProperty().bind(table.widthProperty().multiply(percentage));
            column.maxWidthProperty().bind(table.widthProperty().multiply(percentage));
        }
    }

    public static ChangeListener<Boolean> addFadeTransition(final Node node, int fadeDuration) {
        return (obs, oldV, newV) -> {
            final FadeTransition transition = new FadeTransition(Duration.millis(fadeDuration), node);
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

    public static <S, T> TableCell<S, T> createTableCell(Function<T, String> displayFunction, Pos alignment) {
        TableCell<S, T> cell = new TableCell<S, T>() {
            @Override
            public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(getString());
            }

            private String getString() {
                T item = getItem();
                return item == null ? "" : displayFunction.apply(item);
            }
        };
        cell.setAlignment(alignment);

        return cell;
    }

}
