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

public class FitsImageViewBox extends ScrollPane{
	private Group g;
	private ImageView view;
	private AnnotationLayer annotationLayer;
	
	public FitsImageViewBox(){
		super();
		this.setPannable(true);
		setFitToWidth(true);
		setFitToHeight(true);
		autosize();
		disableScrollBars();
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
		FitsImage fitsImage = new BuildFitsImage(fitsFile).call();
		Image image = fitsImage;
		
		//prepare the view which holds the image
		view = new ImageView(image);
		view.autosize();
		view.setFitWidth(this.getWidth());
		view.setFitHeight(this.getHeight());
		view.setPreserveRatio(true);
		view.setCache(true);
		
	    //add image view to the display pane
		g = new Group(view);
		this.setContent(g);
//		setUpTabs();
		setupAnnotationLayer();
	}
	
	public ImageView getImageView(){
		return view;
	}
	
	public void setupAnnotationLayer(){
		annotationLayer = new AnnotationLayer(view.getFitWidth(), view.getFitHeight());
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
}
