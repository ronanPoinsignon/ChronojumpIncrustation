package prog.element;

import prog.observableproperties.DoubleObsProperty;

public enum ElementProperties {

	COUREUR_PROPERTIES(0.6, 0.08),
	CHEVAL_PROPERTIES(0.6, 0.08),
	PENALITE_PROPERTIES(0.08, 0.08),
	CHRONO_PROPERTIES(0.08, 0.08),
	NULL_PROPERTIES(0, 0);
	
	private double widthPercentage;
	private double heightPercentage;
	private DoubleObsProperty tailleX = new DoubleObsProperty();
	private DoubleObsProperty tailleY = new DoubleObsProperty();
	private DoubleObsProperty positionXChrono = new DoubleObsProperty();
	private DoubleObsProperty positionYChrono = new DoubleObsProperty();
	
	private ElementProperties(double widthPercentage, double heightPercentage) {
		this.widthPercentage = widthPercentage;
		this.heightPercentage = heightPercentage;
	}

	public double getWidthPercentage() {
		return widthPercentage;
	}

	public double getHeightPercentage() {
		return heightPercentage;
	}

	public DoubleObsProperty getTailleX() {
		return tailleX;
	}

	public DoubleObsProperty getTailleY() {
		return tailleY;
	}

	public DoubleObsProperty getPositionXChrono() {
		return positionXChrono;
	}

	public DoubleObsProperty getPositionYChrono() {
		return positionYChrono;
	}
	
	public void setDimension(double width, double height) {
		this.tailleX.setDouble(width * widthPercentage);
		this.tailleY.setDouble(height * heightPercentage);
	}
	
	public void setWidth(double width) {
		this.tailleX.setDouble(width * widthPercentage);
	}
	
	public void setHeight(double height) {
		this.tailleY.setDouble(height * heightPercentage);
	}
	
	public static void setDimensions(double width, double height) {
		for(ElementProperties props : ElementProperties.values()){
			props.tailleX.setDouble(width * props.widthPercentage);
			props.tailleY.setDouble(height * props.heightPercentage);
		}
	}
	
	public static void setWidths(double width) {
		for(ElementProperties props : ElementProperties.values()){
			props.tailleX.setDouble(width * props.widthPercentage);
		}
	}
	
	public static void setHeights(double height) {
		for(ElementProperties props : ElementProperties.values()){
			props.tailleY.setDouble(height * props.heightPercentage);
		}
	}
	
}
