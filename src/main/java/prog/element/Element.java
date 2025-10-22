package prog.element;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Element extends StackPane {

	private final Rectangle bg = new Rectangle();
	private final Label texte = new Label();
	private ElementProperties props;
	
	{
		this.getChildren().add(bg);
		this.getChildren().add(texte);
	}
	
	public Element() {

	}
	
	public Element(ElementProperties props) {
		this.props = props;
		this.getBg().widthProperty().bind(props.getTailleX().doubleProperty());
		this.getBg().heightProperty().bind(props.getTailleY().doubleProperty());
		//this.getTexte().fontProperty().set(new Font(props.getTailleY().getDouble()));
	}

	public Label getTexte() {
		return texte;
	}

	public Rectangle getBg() {
		return bg;
	}
	
	public ElementProperties getProps() {
		return this.props;
	}
	
}
