package prog.observableproperties;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StringObsProperty {

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

    public void setString(String newString){
        this.stringProps.setValue(newString);
    }

    public StringProperty stringProperty() {
        return this.stringProps;
    }
}
