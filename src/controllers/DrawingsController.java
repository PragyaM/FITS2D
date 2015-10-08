package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import models.Drawing;
import views.FitsCanvas;
import views.FitsImageViewBox;
import views.MainWindow;

public abstract class DrawingsController {
	
	protected FitsCanvasController fitsCanvasController;
	protected Drawing currentDrawing;
	protected MainWindow ui;
	protected FitsCanvas canvas;
	protected FitsImageViewBox imageViewBox;
	protected boolean allHidden;
	
	public DrawingsController(FitsCanvasController fitsCanvasController){
		this.fitsCanvasController = fitsCanvasController;
		this.ui = fitsCanvasController.getMainUi();
		this.canvas = fitsCanvasController.getCanvas();
		this.imageViewBox = fitsCanvasController.getImageViewBox();
	}
	
	public void setCurrentDrawing(Drawing drawing){
		this.currentDrawing = drawing;
	}
	
	public FitsImageViewBox getImageViewBox(){
		return fitsCanvasController.getImageViewBox();
	}
	
	public FitsCanvasController getFitsCanvasController(){
		return fitsCanvasController;
	}

	public void disableAnnotationUndoButton() {
		ui.getToolsAreaBox().getAnnotationToolBox().setUndoButtonDisabled(true);
	}
	
	public void disableSelectionUndoButton() {
		ui.getToolsAreaBox().getRegionExtractionToolBox().setUndoButtonDisabled(true);
	}
	
	abstract void hideAll();
	
	public EventHandler<ActionEvent> toggleDrawMode(ToggleButton toggle){
		return (final ActionEvent e) -> {
			try {
				if (toggle.isSelected()){ //enable drawing mode
					setDrawMode(true);
				}
				else if (!toggle.isSelected()){ //disable drawing mode
					setDrawMode(false);
				}
			} catch (NullPointerException e1){
				/*There is no image yet*/
			}
			e.consume();
		};
	}
	
	public EventHandler<ActionEvent> toggleFillMode(ToggleButton toggle) {
		return (final ActionEvent e) -> {
			try {
				if (toggle.isSelected()){ //enable fill mode
					setFillMode(true);
				}
				else if (!toggle.isSelected()){ //disable fill mode
					setFillMode(false);
				}
			} catch (NullPointerException e1){
				/*There is no image yet*/
			}
			e.consume();
		};
	}
	
	abstract EventHandler<ActionEvent> undo();
	
	abstract void setDrawMode(boolean enabled);
	
	abstract void setFillMode(boolean enabled);
	
	abstract void disableUndoButton();
	
	abstract void enableUndoButton();
	
	abstract EventHandler<ActionEvent> save();
	
	abstract EventHandler<ActionEvent> open();
	
	abstract void drawAll();

	abstract EventHandler<ActionEvent> toggleVisible(
			CheckBox hideAnnotationsButton);

	public boolean hasHiddenAll(){
		return allHidden;
	}
	
}
