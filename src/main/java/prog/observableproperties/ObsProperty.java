package prog.observableproperties;

import javafx.beans.property.Property;

public abstract class ObsProperty<T> {

    public abstract void setValue(T value);
    public abstract Property<T> getValue();
}
