package prog.observableproperties;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class IntegerObsProperty {
	
	private final IntegerProperty intProps;

	public IntegerObsProperty() {
		this.intProps = new SimpleIntegerProperty(0);
	}
	
    public IntegerObsProperty(int intProps){
        this.intProps = new SimpleIntegerProperty(intProps);
    }

    public int getint(){
        return intProps.get();
    }

    public void setInt(int newInt){
        this.intProps.setValue(newInt);
    }

    public IntegerProperty intProperty() {
        return this.intProps;
    }
}
