package views.tools_area;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import controllers.GUIController;

public class ColourToolBox extends BaseToolBox{
	
	private ColorPicker nanColourPicker;
	private GUIController parent;

	public ColourToolBox(GUIController parent, ToggleGroup group){
		super("Colour Tools");
		this.parent = parent;
		
		nanColourPicker = new ColorPicker();
		nanColourPicker.setPromptText("NaN value display colour");
		
		add(new Label("Undefined value colour: "), 0, 1);
		add(nanColourPicker, 1, 1);
		
		nanColourPicker.setOnAction(parent.changeNanColour());
		nanColourPicker.setValue(Color.BLACK);
	}
	
	public Color getNanColourPickerColour(){
		return nanColourPicker.getValue();
	}
	
}
