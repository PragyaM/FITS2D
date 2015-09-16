package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import models.Drawing;
import views.FitsImageViewBox;
import views.MainWindow;

public abstract class DrawingsController {
	
	protected FitsCanvasController fitsCanvasController;
	protected Drawing currentDrawing;
	protected MainWindow ui;
	
	public DrawingsController(FitsCanvasController fitsCanvasController){
		this.fitsCanvasController = fitsCanvasController;
		this.ui = fitsCanvasController.getMainUi();
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
	
	public EventHandler<ActionEvent> undoStroke() {
		return (final ActionEvent e) -> {
			currentDrawing.undo();
			fitsCanvasController.drawAll();
		};
	}

	public void disableAnnotationUndoButton() {
		ui.getToolsAreaBox().getAnnotationToolBox().setUndoButtonDisabled(true);
	}
	
	public void disableSelectionUndoButton() {
		ui.getToolsAreaBox().getRegionExtractionToolBox().setUndoButtonDisabled(true);
	}
}
