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
import javafx.scene.transform.Scale;
import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import views.FitsImageViewBox;
import views.MainWindow;

public class ImageController {
	
	private MainWindow ui;
	protected FitsImageViewBox imageViewBox;
	
	public ImageController(MainWindow mainWindow){
		this.ui = mainWindow;
		
		imageViewBox = new FitsImageViewBox(this);
        
		ui.addImageViewBox(imageViewBox);
		imageViewBox.setOnZoom(this.zoomImage());
	}
	
	public void addImageFromFile(File file){
		try {
			Fits fitsFile = new Fits(file);
			addImage(fitsFile);
			
		} catch (FitsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addImage(Fits fitsFile){
		try {
			imageViewBox.addImage(fitsFile);
			imageViewBox.setVisible(true);
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
		try {
			imageViewBox.getFitsImage().writeImage();
		} catch (IOException e) {
			e.printStackTrace(); //TODO: handle
		}
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
	
	public EventHandler<? super ZoomEvent> zoomImage() {
		return (final ZoomEvent e) -> {
			double zoomFactor = e.getZoomFactor();
			double pivotX = e.getX();
			double pivotY = e.getY();
			Scale scale = new Scale(zoomFactor, zoomFactor, pivotX, pivotY); //FIXME setting a pivot point appears to have no effect
			imageViewBox.getImageView().getTransforms().add(scale);
			imageViewBox.getAnnotationLayer().getTransforms().add(scale);
			imageViewBox.getAnnotationLayer().getGraphicsContext2D().scale(zoomFactor, zoomFactor);
		};
	}
}
