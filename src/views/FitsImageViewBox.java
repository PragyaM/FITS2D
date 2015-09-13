package views;

import java.io.IOException;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import services.BuildFitsImage;
import controllers.AnnotationsController;
import controllers.ImageController;

public class FitsImageViewBox extends ScrollPane{
	private Group g;
	private ImageView view;
	private AnnotationLayer annotationLayer;
	private FitsImage fitsImage;
	private ImageController controller;
	private int zoomLevel = 100;
	
	public FitsImageViewBox(ImageController controller){
		super();
		this.controller = controller;
		this.setPannable(true);
		setFitToWidth(true);
		setFitToHeight(true);
		autosize();
		disableScrollBars();
		this.setId("image-view-box");
		this.getStyleClass().add("image-view-box");
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
		Image image = fitsImage.getImage();
		
		//prepare the view which holds the image
		view = new ImageView(image);
		view.setId("view-pane");
		view.autosize();
		view.setFitWidth(this.getWidth());
		view.setFitHeight(this.getHeight());
		view.setPreserveRatio(true);
		view.setCache(true);
		
	    //add image view to the display pane
		g = new Group(view);
		this.setContent(g);
//		setUpTabs();
	}
	
	public ImageView getImageView(){
		return view;
	}
	
	public void setupAnnotationLayer(AnnotationsController annotationsController){
		annotationLayer = new AnnotationLayer(view.minWidth(view.getImage().getHeight()), view.minHeight(view.getImage().getWidth()), annotationsController);
		System.out.println("Canvas width = " + annotationLayer.getWidth() + "  image width = " + getFitsImage().getWidth());
		System.out.println("Canvas height = " + annotationLayer.getHeight() + "  image height = " + getFitsImage().getHeight());
		annotationLayer.turnAnnotatingOff();
		g.getChildren().add(annotationLayer);
	}
	
	public AnnotationLayer getAnnotationLayer(){
		return annotationLayer;
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
		view.setImage(fitsImage.getImage());
	}
}
