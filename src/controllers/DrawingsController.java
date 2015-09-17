package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	
//	public EventHandler<ActionEvent> undoStroke() {
//		return (final ActionEvent e) -> {
//			currentDrawing.undo();
//			fitsCanvasController.drawAll();
//		};
//	}

	public void disableAnnotationUndoButton() {
		ui.getToolsAreaBox().getAnnotationToolBox().setUndoButtonDisabled(true);
	}
	
	public void disableSelectionUndoButton() {
		ui.getToolsAreaBox().getRegionExtractionToolBox().setUndoButtonDisabled(true);
	}
	
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
		};
	}
	
	abstract EventHandler<ActionEvent> undo();
	
	abstract void setDrawMode(boolean enabled);
	
	abstract void setFillMode(boolean enabled);
	
	abstract void disableUndoButton();
	
	abstract void enableUndoButton();
	
}
