package views;

import java.io.IOException;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import services.BuildFitsImage;
import controllers.ImageController;

public class FitsImageViewBox extends ScrollPane{
	private Group g;
	private ImageView view;
	private FitsCanvas fitsCanvas;
	private FitsImage fitsImage;
	private ImageController controller;
	private int zoomLevel = 100;
	private ImageLoadingProgressBar pBar;

	public FitsImageViewBox(ImageController controller){
		super();
		this.controller = controller;
		this.setPannable(true);
		setFitToWidth(true);
		setFitToHeight(true);
		autosize();
		disableScrollBars();
		
		this.setStyle("-fx-background: space-gray ; "
				+ "-fx-border-color: space-gray ; "
				+ "-fx-border-width: 2px; "
				+ "-fx-text-alignment: center;");
	}
	
	public void showImageLoadingProgressBar(){
		pBar = new ImageLoadingProgressBar(this.getViewportBounds().getWidth(), this.getViewportBounds().getHeight());
		this.getChildren().add(pBar);
	}

	public void setUpTabs(){
		TabPane imageLayerControl = new TabPane();
		Tab tab = new Tab();
		tab.setText("new tab");
		imageLayerControl.getTabs().add(tab);
		g.getChildren().add(imageLayerControl);
	}

	public void addImage(Fits fitsFile) throws FitsException, IOException{
		//retrieve image from FITS file
		fitsImage = new BuildFitsImage(fitsFile, controller).call();
	}
	
	public void addCanvas(FitsCanvas fitsCanvas){
		this.fitsCanvas = fitsCanvas;
		g.getChildren().add(fitsCanvas);
		this.setContent(g);
	}

	public void prepareView(){
		//prepare the view which holds the image
		view = new ImageView();
		view.setId("view-pane");

		//add view to the display pane
		g = new Group(view);
		this.setContent(g);
		//TODO: setup tabs here
		
		g.setStyle("-fx-text-alignment: center;");
	}
	
	public void setImage(){
		removeImageLoadingProgressBar();
		view.setImage(fitsImage.getImage());
		view.autosize();
		view.setFitWidth(this.getWidth());
		view.setFitHeight(this.getHeight());
		view.setPreserveRatio(true);
		view.setCache(true);
	}

	public ImageView getImageView(){
		return view;
	}
	
	public void resizeCanvas(){
		fitsCanvas.setWidth(view.minWidth(view.getImage().getHeight()));
		fitsCanvas.setHeight(view.minHeight(view.getImage().getWidth()));
		System.out.println("Canvas width = " + fitsCanvas.getWidth() + "  image width = " + getFitsImage().getWidth());
		System.out.println("Canvas height = " + fitsCanvas.getHeight() + "  image height = " + getFitsImage().getHeight());
		fitsCanvas.turnAnnotatingOff();
	}

	public FitsCanvas getFitsCanvas(){
		return fitsCanvas;
	}

	public void disableScrollBars(){
		setHbarPolicy(ScrollBarPolicy.NEVER);
		setVbarPolicy(ScrollBarPolicy.NEVER);
	}

	public void enableScrollBars(){
		setHbarPolicy(ScrollBarPolicy.ALWAYS);
		setVbarPolicy(ScrollBarPolicy.ALWAYS);
	}

	public FitsImage getFitsImage(){
		return fitsImage;
	}

	public int getZoomLevel(){
		return zoomLevel;
	}

	public void setZoomLevel(int zoom){
		this.zoomLevel = zoom;
	}

	public void refreshImage() {
		setImage();
	}

	public void removeImageLoadingProgressBar() {
		this.getChildren().remove(pBar);
	}
}
