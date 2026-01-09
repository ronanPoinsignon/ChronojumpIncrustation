package appli;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ronan
 *
 */
public class App extends Application {

	public static void main(final String[] args) throws IOException {
		Application.launch(args);
	}

	@Override
	public void start(final Stage pStage) throws Exception {
		final Stage stage = new Stage();
		final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/SHF_incrustation.fxml"));
		final Parent sceneVideo = loader.load();
		final Scene scene = new Scene(sceneVideo);
		stage.setMinWidth(640);
		stage.setMinHeight(360);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.show();
	}
}
