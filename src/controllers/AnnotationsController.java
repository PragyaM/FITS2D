package controllers;

import java.io.File;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
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
	public void disableUndoButton() {
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
	
	@Override
	public EventHandler<ActionEvent> save() {
		return (final ActionEvent e) -> {
			File file = (File) ui.showSaveDialog("TXT");
			fitsCanvasController.getCanvas().writeAnnotationsToFile(file);
		};
	}
	
	@Override
	public EventHandler<ActionEvent> open() {
		return (final ActionEvent e) -> {
			File file = ui.openFile("Select an annotation file", "TXT");
			fitsCanvasController.getCanvas().addAnnotationsFromFile(file);
		};
	}

	@Override
	public void hideAll() {
		fitsCanvasController.getCanvas().clear();
		fitsCanvasController.getSelectionsController().drawAll();
	}
	
	@Override
	public void drawAll() {
		fitsCanvasController.getCanvas().getAnnotations().forEach((annotation) -> {
			annotation.draw();
		});
	}
	
	@Override
	public EventHandler<ActionEvent> toggleVisible(
			CheckBox hideAnnotationsButton) {
		return (final ActionEvent e) -> {
			try{
				if (hideAnnotationsButton.isSelected()){
					hideAll();
				}
				else {
					drawAll();
				}
			} catch (NullPointerException e1){
				/*fits canvas does not exist yet, so do nothing*/
			}
		};
	}

}
