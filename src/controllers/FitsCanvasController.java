package controllers;

import views.FitsCanvas;
import views.FitsImageViewBox;
import views.MainWindow;

public class FitsCanvasController {

	private FitsCanvas fitsCanvas;
	private FitsImageViewBox imageViewBox;
	private ImageController imageController;
	private AnnotationsController annotationsController;
	private SelectionsController selectionsController;

	private MainWindow ui;

	public FitsCanvasController(MainWindow mainWindow, ImageController imageController){
		this.ui = mainWindow;
		this.imageController = imageController;
		initialise(imageController.getImageViewBox());
		this.annotationsController = new AnnotationsController(this);
		this.selectionsController = new SelectionsController(this);
	}

	public void initialise(FitsImageViewBox imageViewBox){
		this.imageViewBox = imageViewBox;
		this.fitsCanvas = imageViewBox.getFitsCanvas();
	}
	
	public FitsImageViewBox getImageViewBox(){
		return imageViewBox;
	}
	
	public void resetZoom(){
		imageController.resetZoom();
	}

	public void drawAll() {
		fitsCanvas.getGraphicsContext2D().clearRect(0, 0, fitsCanvas.getWidth(), fitsCanvas.getHeight());
		selectionsController.drawAll();
		annotationsController.drawAll();
	}

	public MainWindow getMainUi() {
		return ui;
	}

	public AnnotationsController getAnnotationsController() {
		return annotationsController;
	}
	
	public SelectionsController getSelectionsController() {
		return selectionsController;
	}

	public FitsCanvas getCanvas() {
		return fitsCanvas;
	}

}
