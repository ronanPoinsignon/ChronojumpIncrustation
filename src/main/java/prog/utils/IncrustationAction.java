package prog.utils;

import prog.executor.ControllerExecutor;
import prog.transmission.EventObserver;

public enum IncrustationAction {

    SHOW_PANNEAU(() -> ControllerExecutor.getExecutor().switchScene(Panel.SECONDARY_PANEL, FxmlIncrustation.PANNEAU)),
    SHOW_CLASSEMENT(() -> ControllerExecutor.getExecutor().switchScene(Panel.SECONDARY_PANEL, FxmlIncrustation.CLASSEMENT)),
    RESET_ALL(() -> EventObserver.getInstance().resetValues());

    private final Runnable runnable;

    IncrustationAction(Runnable runnable) {
        this.runnable = runnable;
    }

    public void execute() {
        runnable.run();
    }

}
