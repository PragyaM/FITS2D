package views.tools_area;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import controllers.GUIController;

public class MaskToolBox extends BaseToolBox{
	
	public MaskToolBox(GUIController controller, ToggleGroup group){
		super("Region Cutting Tools");
		
		ToggleButton drawMaskToolButton = new ToggleButton("Region Draw Tool");
		ToggleButton fillMaskToolButton = new ToggleButton("Region Bucket Tool");
		Button createMaskButton = new Button("Cut Region From Selection");
		
		drawMaskToolButton.setToggleGroup(group);
		fillMaskToolButton.setToggleGroup(group);
		
		this.add(drawMaskToolButton, 0, 1);
		this.add(fillMaskToolButton, 1, 1);
		this.add(createMaskButton, 2, 1);
		
		drawMaskToolButton.setOnAction(controller.getAnnotationsController().toggleMaskDrawMode(drawMaskToolButton));
		fillMaskToolButton.setOnAction(controller.getAnnotationsController().toggleMaskFillMode(fillMaskToolButton));
		createMaskButton.setOnAction(controller.getAnnotationsController().createMaskFromSelection());
	}
}
