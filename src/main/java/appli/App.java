package appli;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import prog.controllers.NewControllerVideo;

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
		final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/new_sceneVideo.fxml"));
		final Parent sceneVideo = loader.load();
		final Scene scene = new Scene(sceneVideo);
		//		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.show();
		final NewControllerVideo controller = loader.getController();
		controller.addevent(stage);
		//		controller.setDimension(stage);
	}
}
