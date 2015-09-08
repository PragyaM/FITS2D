package views.tools_area;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import controllers.ImageController;

public class ColourToolBox extends BaseToolBox{
	
	private ColorPicker nanColourPicker;
	private ImageController parent;

	public ColourToolBox(ImageController controller, ToggleGroup group){
		super("Colour");
		this.parent = controller;
		
		nanColourPicker = new ColorPicker();
		nanColourPicker.setPromptText("NaN value display colour");
		
		add(new Label("Undefined values: "), 0, 1);
		add(nanColourPicker, 1, 1);
		
		nanColourPicker.setOnAction(controller.changeNanColour());
		nanColourPicker.setValue(Color.BLACK);
	}
	
	public Color getNanColourPickerColour(){
		return nanColourPicker.getValue();
	}
	
}
