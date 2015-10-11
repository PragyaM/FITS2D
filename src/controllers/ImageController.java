package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.ZoomEvent;
import javafx.scene.paint.Color;
import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import views.FitsImageViewBox;
import views.MainWindow;

public class ImageController {

	private MainWindow ui;
	protected FitsImageViewBox imageViewBox;
	private double MINIMUM_ZOOM = 50.0;
	private FitsCanvasController fitsCanvasController;

	public ImageController(MainWindow mainWindow){
		this.ui = mainWindow;

		imageViewBox = new FitsImageViewBox(this);
		imageViewBox.prepareView();
		imageViewBox.setVisible(true);

		ui.addImageViewBox(imageViewBox);
		imageViewBox.setOnZoom(this.touchZoomImage());
	}

	public void addImageFromFile(File file, FitsCanvasController fitsCanvasController){
		try {
			Fits fitsFile = new Fits(file);
			addImage(fitsFile, fitsCanvasController);

		} catch (FitsException e) {
			e.printStackTrace();
		}

	}

	public void addImage(Fits fitsFile, FitsCanvasController fitsCanvasController){
		this.fitsCanvasController = fitsCanvasController;
		try {
			imageViewBox.showImageLoadingProgressBar();
			imageViewBox.addImage(fitsFile);
		} catch (FitsException | IOException e) {
			e.printStackTrace();
		}

	}
	
	public void adjustViewForImage(){
		imageViewBox.setImage();
		fitsCanvasController.adjustForImage(imageViewBox);
		ui.getToolsAreaBox().getHistogramToolBox().setUpHistogram(imageViewBox.getFitsImage().getHistogram());
	}

	public EventHandler<javafx.event.ActionEvent> toggleImageScrollbars(CheckMenuItem toggle){
		return (final ActionEvent e) -> {
			if (toggle.isSelected()) {
				imageViewBox.enableScrollBars();
			}
			else  imageViewBox.disableScrollBars();
			e.consume();
		};
	}

	public EventHandler<ActionEvent> changeNanColour() {
		return (final ActionEvent e) -> {
			Color nanColour = ((ColorPicker) e.getSource()).getValue();
			try {
				imageViewBox.showImageLoadingProgressBar();
				getFitsImage().setNanColour(nanColour);
				imageViewBox.getFitsImage().writeImage();
			} catch (IOException e1) {
				// TODO handle
				e1.printStackTrace();
			}
			e.consume();
		};
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
	
	public FitsImage getFitsImage(){
		return imageViewBox.getFitsImage();
	}

	public EventHandler<ActionEvent> toggleHistogramVisible(Button hideButton) {
		return (final ActionEvent e) -> {
			boolean hidden = false;
			if (hideButton.getText().equals("Show Distribution Graph")) hidden = true;
			
			if (hidden) {
				ui.getToolsAreaBox().getHistogramToolBox().show();
			} else {
				ui.getToolsAreaBox().getHistogramToolBox().hide();
			}
			e.consume();
		};
	}
	
	public EventHandler<ActionEvent> updateVisibleRange(TextField visibleRangeMinInput,
			TextField visibleRangeMaxInput) {
		return (final ActionEvent e) -> {
			double newMin = getFitsImage().getHistogram().getVisibleRangeMin();
			double newMax = getFitsImage().getHistogram().getVisibleRangeMax();
			try {
				newMin = Double.parseDouble(visibleRangeMinInput.getText());
			} catch(NullPointerException e1){
				/* do nothing - previous min value will be used */
			}
			
			try {
				newMax = Double.parseDouble(visibleRangeMaxInput.getText());
			} catch(NullPointerException e2){
				/* do nothing - previous max value will be used */
			}
			if (newMin <= getFitsImage().getHistogram().getMaxValue()
					&& newMin >= getFitsImage().getHistogram().getMinValue()
					&& newMax <= getFitsImage().getHistogram().getMaxValue()
					&& newMax >= getFitsImage().getHistogram().getMinValue()){
				getFitsImage().getHistogram().setVisibleRangeMin(newMin);
				getFitsImage().getHistogram().setVisibleRangeMax(newMax);
				imageViewBox.showImageLoadingProgressBar();
				imageViewBox.getFitsImage().writeImage();
				getFitsImage().getHistogram().updateChart();
				ui.getToolsAreaBox().getHistogramToolBox().updateHistogram(getFitsImage().getHistogram());
			}
			else ui.displayMessage("Visible range must be within data range");
			e.consume();
		};
	}

	public EventHandler<ActionEvent> toggleHistogramLogScale(
			boolean enableLogScale) {
		return (final ActionEvent e) -> {
			getFitsImage().getHistogram().createChart(enableLogScale);
			ui.getToolsAreaBox().getHistogramToolBox().updateHistogram(getFitsImage().getHistogram());
			e.consume();
		};
	}
	
	public ExecutorService createExecutor(final String name) {       
	    ThreadFactory factory = new ThreadFactory() {
	      @Override public Thread newThread(Runnable r) {
	        Thread t = new Thread(r);
	        t.setName(name);
	        t.setDaemon(true);
	        return t;
	      }
	    };
	    
	    return Executors.newSingleThreadExecutor(factory);
	  }  
}
