package views.tools_area;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import controllers.GUIController;

public class MaskToolBox extends BaseToolBox{
	
	public MaskToolBox(GUIController controller, ToggleGroup group){
		super("Masking Tools");
		
		ToggleButton drawMaskToolButton = new ToggleButton("Mask Draw Tool");
		ToggleButton fillMaskToolButton = new ToggleButton("Mask Bucket Tool");
		Button createMaskButton = new Button("Create Mask From Selection");
		
		drawMaskToolButton.setToggleGroup(group);
		fillMaskToolButton.setToggleGroup(group);
		
		this.add(drawMaskToolButton, 4, 1);
		this.add(fillMaskToolButton, 5, 1);
		this.add(createMaskButton, 6, 1);
		
		drawMaskToolButton.setOnAction(controller.toggleMaskDrawMode(drawMaskToolButton));
		fillMaskToolButton.setOnAction(controller.toggleMaskFillMode(fillMaskToolButton));
		createMaskButton.setOnAction(controller.createMaskFromSelection());
	}
}
