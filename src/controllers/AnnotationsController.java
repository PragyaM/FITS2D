package controllers;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import models.Annotation;
import views.FitsCanvas.Mode;

public class AnnotationsController extends DrawingsController{

	public AnnotationsController(FitsCanvasController fitsCanvasController) {
		super(fitsCanvasController);
	}
	
	@Override
	public void setDrawMode(boolean enabled){
		if (enabled){
			//TODO: turn cursor into pencil
			fitsCanvasController.getCanvas().turnSelectingOff();
			if (fitsCanvasController.getCanvas().getCurrentAnnotation() == null){
				fitsCanvasController.getCanvas().makeNewAnnotation();
			} else {
				fitsCanvasController.getCanvas().addEventHandler(MouseEvent.ANY, 
						fitsCanvasController.getCanvas().getCurrentAnnotation());
			}
			fitsCanvasController.getCanvas().setMode(Mode.ANNOTATION_DRAW);
			fitsCanvasController.getImageViewBox().setPannable(false);
		} else {
			fitsCanvasController.getCanvas().turnAnnotatingOff();
			fitsCanvasController.getCanvas().setMode(Mode.NONE);
			fitsCanvasController.getImageViewBox().setPannable(true);
		}
	}
	
	@Override
	public void setFillMode(boolean enabled){
		if (enabled){
			//TODO: turn cursor into bucket
			fitsCanvasController.getCanvas().turnSelectingOff();
			if (fitsCanvasController.getCanvas().getCurrentAnnotation() == null){
				fitsCanvasController.getCanvas().makeNewAnnotation();
			} else {
				fitsCanvasController.getCanvas().addEventHandler(MouseEvent.ANY, 
						fitsCanvasController.getCanvas().getCurrentAnnotation());
			}
			fitsCanvasController.getCanvas().setMode(Mode.ANNOTATION_FILL);
			fitsCanvasController.getImageViewBox().setPannable(false);
		} else {
			fitsCanvasController.getCanvas().turnAnnotatingOff();
			fitsCanvasController.getCanvas().setMode(Mode.NONE);
			fitsCanvasController.getImageViewBox().setPannable(true);
		}
	}

	@Override
	void disableUndoButton() {
		ui.getToolsAreaBox().getAnnotationToolBox().setUndoButtonDisabled(true);
	}
	
	@Override
	public void enableUndoButton() {
		ui.getToolsAreaBox().getAnnotationToolBox().setUndoButtonDisabled(false);
	}

	@Override
	public EventHandler<ActionEvent> undo() {
		return (final ActionEvent e) -> {
			ArrayList<Annotation> annotations = fitsCanvasController.getCanvas().getAnnotations();
			annotations.get(annotations.size() - 1 ).undo();
			fitsCanvasController.getCanvas().clear();
			fitsCanvasController.getCanvas().drawAll();
			
			if (annotations.size() < 1){
				disableUndoButton();
			}
		};
	}

}
