package appli;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import prog.executor.ControllerExecutor;
import prog.utils.FxmlIncrustation;
import prog.utils.Panel;

/**
 *
 * @author ronan
 *
 */
public class App extends Application {

	private final ControllerExecutor controllerExecutor = ControllerExecutor.getExecutor();

	public static void main(final String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(final Stage pStage) {
		controllerExecutor.show(FxmlIncrustation.CHOIX, null, stage -> {
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setMinWidth(640);
			stage.setMinHeight(360);
		});
	}
}
