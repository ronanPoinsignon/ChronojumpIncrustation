package prog.controllers;

import prog.executor.ControllerExecutor;
import prog.utils.FxmlIncrustation;
import prog.utils.Panel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AbstractControllerIncrustation extends AbstractController {

    private final ControllerExecutor controllerExecutor = ControllerExecutor.getExecutor();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        try {
            openSecondaryPanel();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openSecondaryPanel() throws IOException {
        controllerExecutor.show(FxmlIncrustation.PANNEAU, Panel.SECONDARY_PANEL);
    }
}
