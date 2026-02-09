package prog.utils;

import prog.executor.ControllerExecutor;

import java.util.function.Consumer;

public enum IncrustationAction {

    SHOW_PANNEAU(executor -> executor.switchScene(Panel.SECONDARY_PANEL, FxmlIncrustation.PANNEAU)),
    SHOW_CLASSEMENT(executor -> executor.switchScene(Panel.SECONDARY_PANEL, FxmlIncrustation.CLASSEMENT));

    private final Consumer<ControllerExecutor> controllerConsumer;

    IncrustationAction(Consumer<ControllerExecutor> controllerConsumer) {
        this.controllerConsumer = controllerConsumer;
    }

    public void execute(ControllerExecutor controllerExecutor) {
        controllerConsumer.accept(controllerExecutor);
    }

}
