package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.ZoomEvent;
import javafx.scene.paint.Color;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import views.FitsImageViewBox;
import views.MainWindow;

public class ImageController {

	private MainWindow ui;
	protected FitsImageViewBox imageViewBox;
	private double MINIMUM_ZOOM = 50.0;

	public ImageController(MainWindow mainWindow){
		this.ui = mainWindow;

		imageViewBox = new FitsImageViewBox(this);

		ui.addImageViewBox(imageViewBox);
		imageViewBox.setOnZoom(this.touchZoomImage());
	}

	public void addImageFromFile(File file, FitsCanvasController annotationsController){
		try {
			Fits fitsFile = new Fits(file);
			addImage(fitsFile, annotationsController);

		} catch (FitsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addImage(Fits fitsFile, FitsCanvasController annotationsController){
		try {
			imageViewBox.addImage(fitsFile);
			imageViewBox.setVisible(true);
			imageViewBox.setupFitsCanvas(annotationsController);
		} catch (FitsException | IOException e) {
			e.printStackTrace();
		}

	}

	public EventHandler<javafx.event.ActionEvent> toggleImageScrollbars(CheckMenuItem toggle){
		return (final ActionEvent e) -> {
			if (toggle.isSelected()) {
				imageViewBox.enableScrollBars();
			}
			else  imageViewBox.disableScrollBars();
		};
	}

	public EventHandler<ActionEvent> changeNanColour() {
		return (final ActionEvent e) -> {
			Color nanColour = ((ColorPicker) e.getSource()).getValue();
			try {
				imageViewBox.getFitsImage().setNanColour(nanColour);
				refreshImage();
			} catch (IOException e1) {
				// TODO handle
				e1.printStackTrace();
			}
		};
	}

	public void refreshImage(){
		imageViewBox.getFitsImage().writeImage();
		imageViewBox.refreshImage();
	}

	public Color getNanColour() {
		return ui.getToolsAreaBox().getColourToolBox().getNanColourPickerColour();
	}

	public ArrayList<String> getHeaderInfo(){
		ArrayList<String> headerInfo = new ArrayList<String>();
		headerInfo.add("CRVAL1: " + imageViewBox.getFitsImage().getCRVAL1());
		headerInfo.add("CRVAL2: " + imageViewBox.getFitsImage().getCRVAL2());
		return headerInfo;
	}

	public EventHandler<? super ZoomEvent> touchZoomImage() {
		return (final ZoomEvent e) -> {
			double zoomFactor = e.getZoomFactor();

			if ((imageViewBox.getZoomLevel() * zoomFactor) < MINIMUM_ZOOM){ //zoom level cannot become less than minimum zoom
				zoomFactor = MINIMUM_ZOOM / imageViewBox.getZoomLevel();
				setZoomOutButtonDisabled(true);
			}
			if (imageViewBox.getZoomLevel() <= MINIMUM_ZOOM && zoomFactor < 1){
				setZoomOutButtonDisabled(true);
				e.consume();
			} 
			else {
				setZoomOutButtonDisabled(false);
				updateScale(zoomFactor);
				imageViewBox.setZoomLevel((int)(imageViewBox.getImageView().getScaleY() * 100.0) );
				setZoomLabel((int)(imageViewBox.getImageView().getScaleY() * 100.0) + "%");
				e.consume();
			}
			//			imageViewBox.getAnnotationLayer().getGraphicsContext2D().scale(zoomFactor, zoomFactor);
		};
	}

	public EventHandler<ActionEvent> zoomOut() {
		return (final ActionEvent e) -> {
			double zoomFactor = 0.9;

			if ((imageViewBox.getZoomLevel() * zoomFactor) < MINIMUM_ZOOM){ //zoom level cannot become less than minimum zoom
				zoomFactor = MINIMUM_ZOOM / imageViewBox.getZoomLevel();
				setZoomOutButtonDisabled(true);
			}
			if (imageViewBox.getZoomLevel() <= MINIMUM_ZOOM){
				e.consume();
			} 
			else {
				updateScale(zoomFactor);
				imageViewBox.setZoomLevel((int) (imageViewBox.getZoomLevel() * zoomFactor));
				setZoomLabel(imageViewBox.getZoomLevel() + "%");
				e.consume();
			}
		};
	}

	public EventHandler<ActionEvent> zoomIn() {
		return (final ActionEvent e) -> {
			double zoomFactor = 1.1;
			updateScale(zoomFactor);
			imageViewBox.setZoomLevel((int) (imageViewBox.getZoomLevel() * zoomFactor));
			setZoomLabel(imageViewBox.getZoomLevel() + "%");
			if (imageViewBox.getZoomLevel() > MINIMUM_ZOOM){
				setZoomOutButtonDisabled(false);
			}
			e.consume();
		};
	}

	public void updateScale(double zoomFactor){
		imageViewBox.getImageView().setScaleX(imageViewBox.getImageView().getScaleX() * zoomFactor);
		imageViewBox.getImageView().setScaleY(imageViewBox.getImageView().getScaleY() * zoomFactor);
		imageViewBox.getFitsCanvas().setScaleX(imageViewBox.getFitsCanvas().getScaleX() * zoomFactor);
		imageViewBox.getFitsCanvas().setScaleY(imageViewBox.getFitsCanvas().getScaleY() * zoomFactor);
	}

	public void setZoomLabel(String zoomText){
		ui.getToolsAreaBox().getImageToolBox().setZoomLabel(zoomText);
	}

	public void setZoomOutButtonDisabled(boolean disable){
		ui.getToolsAreaBox().getImageToolBox().setZoomOutButtonDisabled(disable);
	}
	
	public void resetZoom(){
		imageViewBox.getImageView().setScaleX(1);
		imageViewBox.getImageView().setScaleY(1);
		imageViewBox.getFitsCanvas().setScaleX(1);
		imageViewBox.getFitsCanvas().setScaleY(1);
		imageViewBox.setZoomLevel(100);
		setZoomLabel("100%");
	}
	
	public FitsImageViewBox getImageViewBox(){
		return imageViewBox;
	}
}
