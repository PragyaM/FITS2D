package controllers;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import models.Selection;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.util.BufferedFile;
import services.ExtractFitsRegion;
import views.FitsCanvas.Mode;

public class SelectionsController extends DrawingsController{

	public SelectionsController(FitsCanvasController fitsCanvasController) {
		super(fitsCanvasController);
	}

	@Override
	public void setDrawMode(boolean enabled){
		if (enabled){
			//TODO: turn cursor into pencil
			fitsCanvasController.getCanvas().turnAnnotatingOff();
			fitsCanvasController.getCanvas().makeNewSelection();
			fitsCanvasController.getCanvas().addEventHandler(MouseEvent.ANY, 
					fitsCanvasController.getCanvas().getCurrentSelection());
			fitsCanvasController.getCanvas().setMode(Mode.SELECTION_DRAW);
			fitsCanvasController.setPannable(false);
		} else {
			fitsCanvasController.getCanvas().turnSelectingOff();
			fitsCanvasController.getCanvas().setMode(Mode.NONE);
			fitsCanvasController.setPannable(true);
		}
	}

	@Override
	public void setFillMode(boolean enabled){
		if (enabled){
			//TODO: turn cursor into bucket
			fitsCanvasController.getCanvas().turnAnnotatingOff();
			fitsCanvasController.getCanvas().makeNewSelection();
			fitsCanvasController.getCanvas().addEventHandler(MouseEvent.ANY, 
					fitsCanvasController.getCanvas().getCurrentSelection());
			fitsCanvasController.getCanvas().setMode(Mode.SELECTION_FILL);
			fitsCanvasController.setPannable(false);
		} else {
			fitsCanvasController.getCanvas().turnSelectingOff();
			fitsCanvasController.getCanvas().setMode(Mode.NONE);
			fitsCanvasController.setPannable(true);
		}
	}

	public EventHandler<ActionEvent> extractFitsFromSelection() {
		return (final ActionEvent e) -> {
			ArrayList<Point> fullSelection = fitsCanvasController.getCanvas()
					.getSelectedArea();
			try {
				Fits extraction = ExtractFitsRegion.mapToFits(fullSelection,
						fitsCanvasController.getImageViewBox().getFitsImage(), 
						(int) fitsCanvasController.getCanvas().getWidth(), 
						(int) fitsCanvasController.getCanvas().getHeight());

				BufferedFile bf = new BufferedFile(ui.showSaveDialog("FITS"), "rw");
				extraction.write(bf);
				bf.close();
			} catch (FitsException e1) {
				System.out.println(e1.getMessage());
			} catch (IOException e1) {
				System.out.println("cancelled");
			} catch (NegativeArraySizeException e1){
				ui.displayMessage("Failed to convert selection to image pixels");
			}
			e.consume();
		};
	}

	@Override
	public void disableUndoButton(){
		ui.getToolsAreaBox().getRegionExtractionToolBox().setUndoButtonDisabled(true);
	}

	@Override
	public void enableUndoButton() {
		ui.getToolsAreaBox().getRegionExtractionToolBox().setUndoButtonDisabled(false);
	}

	@Override
	public EventHandler<ActionEvent> undo() {
		return (final ActionEvent e) -> {
			ArrayList<Selection> selections = fitsCanvasController.getCanvas()
					.getSelections();
			if (!selections.isEmpty()) selections.get(selections.size() - 1 ).undo();
			if (selections.size() > 1 &&
					selections.get(selections.size() - 1 ).getRegions().size() == 0){
				selections.remove(selections.size() - 1);
			}
			fitsCanvasController.getCanvas().clear();
			fitsCanvasController.getCanvas().drawAll();

			if (selections.size() < 1) {
				disableUndoButton();
			}
			e.consume();
		};
	}

	@Override
	public EventHandler<ActionEvent> save() {
		return (final ActionEvent e) -> {
			File file = (File) ui.showSaveDialog("TXT");
			fitsCanvasController.getCanvas().writeSelectionsToFile(file);
		};
	}

	@Override
	public EventHandler<ActionEvent> open() {
		return (final ActionEvent e) -> {
			File file = ui.openFile("Select an annotation file", "TXT");
			fitsCanvasController.getCanvas().addSelectionsFromFile(file);
			e.consume();
		};
	}

	@Override
	public void hideAll() {
		fitsCanvasController.getCanvas().clear();
		if (! fitsCanvasController.getAnnotationsController().hasHiddenAll()){
			fitsCanvasController.getAnnotationsController().drawAll();
		}
		allHidden = true;
	}

	@Override
	public void drawAll() {
		fitsCanvasController.getCanvas().getSelections().forEach((selection) -> {
			selection.draw();
		});
		allHidden = false;
	}

	@Override
	public EventHandler<ActionEvent> toggleVisible(
			CheckBox hideSelectionsButton) {
		return (final ActionEvent e) -> {
			try{
				if (hideSelectionsButton.isSelected()){
					hideAll();
				}
				else {
					drawAll();
				}
			} catch (NullPointerException e1){
				/*fits canvas does not exist yet, so do nothing*/
			}
			e.consume();
		};
	}
}
