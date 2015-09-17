package views.tools_area;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;
import views.ImageToggleButton;
import controllers.FitsCanvasController;

public class AnnotationToolBox extends BaseToolBox{
	
//	private ListView<String> annotationsList = new ListView<String>();
//    private ObservableList<String> items = FXCollections.observableArrayList();
	
	private Button undoButton;
	
	public AnnotationToolBox(FitsCanvasController controller, ToggleGroup group){
		super("Annotation");
		
		ImageToggleButton drawToolButton = new ImageToggleButton("/resources/pencil112.png");

        ImageToggleButton fillToolButton = new ImageToggleButton("/resources/paint3.png");

        drawToolButton.setToggleGroup(group);
        fillToolButton.setToggleGroup(group);
        
        undoButton = new Button("Undo");
        
        this.add(drawToolButton, 0, 1);
        this.add(fillToolButton, 0, 2);
        this.add(undoButton, 1, 2);
        
        drawToolButton.setOnAction(controller.getAnnotationsController().toggleDrawMode(drawToolButton));
        fillToolButton.setOnAction(controller.getAnnotationsController().toggleFillMode(fillToolButton));
        undoButton.setOnAction(controller.getAnnotationsController().undo());
        
        CheckBox hideAnnotationsButton = new CheckBox("Hide all");
        add(hideAnnotationsButton, 1, 1);
        hideAnnotationsButton.setOnAction(controller.toggleAnnotationsVisible(hideAnnotationsButton));
        
//        addAnnotationItem("Test");
//        annotationsList.autosize();
//        annotationsList.setPrefHeight(100);
//        this.add(annotationsList, 1,  1);
	}
	
	public void setUndoButtonDisabled(boolean disable){
		undoButton.setDisable(disable);
	}
	
//	public void addAnnotationItem(String annotationName){
//		items.add(annotationName);
//		annotationsList.setItems(items);
//	}
}
