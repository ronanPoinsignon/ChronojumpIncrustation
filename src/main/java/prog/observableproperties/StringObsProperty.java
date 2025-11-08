package prog.observableproperties;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StringObsProperty extends ObsProperty<String> {

	private final StringProperty stringProps;

	public StringObsProperty() {
		this.stringProps = new SimpleStringProperty("");
	}
	
    public StringObsProperty(String someString){
        this.stringProps = new SimpleStringProperty(someString);
    }

    public String getString(){
        return stringProps.get();
    }

    public void setValue(String newString){
        this.stringProps.setValue(newString);
    }

    public StringProperty getValue() {
        return this.stringProps;
    }
}
