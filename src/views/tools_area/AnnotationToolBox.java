package views.tools_area;

import controllers.GUIController;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class AnnotationToolBox extends GridPane{
	
	public AnnotationToolBox(GUIController parent){
		ToggleButton viewMode = new ToggleButton("viewing");
        ToggleButton annotateMode = new ToggleButton("annotating");
        ToggleGroup group = new ToggleGroup();
        viewMode.setToggleGroup(group);
        annotateMode.setToggleGroup(group);
        this.add(viewMode, 1, 1);
        this.add(annotateMode, 2, 1);
        
        viewMode.setOnMouseClicked(parent.setImageViewMode());
        annotateMode.setOnMouseClicked(parent.setImageAnnotateMode());
	}

}
