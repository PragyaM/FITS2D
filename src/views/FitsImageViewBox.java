package views;

import java.io.IOException;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import services.BuildFitsImage;

public class FitsImageViewBox extends ScrollPane{
	private Group g;
	private ImageView view;
	
	public FitsImageViewBox(){
		super();
		this.setPannable(true);
		setFitToWidth(true);
		setFitToHeight(true);
		autosize();
		disableScrollBars();
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
	}
	
	public ImageView getImageView(){
		return view;
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
