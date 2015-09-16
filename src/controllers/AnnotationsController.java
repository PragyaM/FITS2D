package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;

public class AnnotationsController extends DrawingsController{

	public AnnotationsController(FitsCanvasController fitsCanvasController) {
		super(fitsCanvasController);
		
	}
	
	public EventHandler<ActionEvent> toggleDrawMode(ToggleButton toggle){
		return (final ActionEvent e) -> {
			try {
				if (toggle.isSelected()){ //enable currentDrawing mode
					fitsCanvasController.getCanvas().setDrawMode(true);
					fitsCanvasController.getImageViewBox().setPannable(false);
				}
				else if (!toggle.isSelected()){ //disable currentDrawing mode
					fitsCanvasController.getCanvas().setDrawMode(false);
					fitsCanvasController.getImageViewBox().setPannable(true);
				}
			} catch (NullPointerException e1){
				/*There is no image yet*/
			}
		};
	}
	
	public EventHandler<ActionEvent> toggleFillMode(ToggleButton toggle) {
		return (final ActionEvent e) -> {
			try {
				if (toggle.isSelected()){ //enable fill mode
					fitsCanvasController.getCanvas().setFillMode(true);
					fitsCanvasController.getImageViewBox().setPannable(false);
				}
				else if (!toggle.isSelected()){ //disable fill mode
					fitsCanvasController.getCanvas().setFillMode(false);
					fitsCanvasController.getImageViewBox().setPannable(true);
				}
			} catch (NullPointerException e1){
				/*There is no image yet*/
			}
		};
	}

}
