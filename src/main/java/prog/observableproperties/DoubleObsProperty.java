package prog.observableproperties;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class DoubleObsProperty {

	private final DoubleProperty doubleProps;

	public DoubleObsProperty() {
		this.doubleProps = new SimpleDoubleProperty(0);
	}
	
    public DoubleObsProperty(double doubleProps){
        this.doubleProps = new SimpleDoubleProperty(doubleProps);
    }

    public double getDouble(){
        return doubleProps.get();
    }

    public void setDouble(double newDouble){
        this.doubleProps.setValue(newDouble);
    }

    public DoubleProperty doubleProperty() {
        return this.doubleProps;
    }
}
