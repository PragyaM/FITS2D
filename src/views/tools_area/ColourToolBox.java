package views.tools_area;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import controllers.GUIController;

public class ColourToolBox extends BaseToolBox{

	public ColourToolBox(GUIController parent){
		super("Colour Tools");
		
		Slider redSlider = new Slider();
		Slider greenSlider = new Slider();
		Slider bluelider = new Slider();
		
		add(new Label("Red"), 0, 0);
		add(redSlider, 1, 0);
		add(new Label("Green"), 0, 1);
		add(greenSlider, 1, 1);
	    add(new Label("Blue"), 0, 2);
	    add(bluelider, 1, 2);
	    
//	    redSlider.setOnDragDetected(value);
	}
}
