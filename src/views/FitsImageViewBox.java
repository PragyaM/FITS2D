package views;

import java.io.IOException;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import services.BuildFitsImage;

public class FitsImageViewBox extends Label{
	
	public FitsImageViewBox(){
		super();
	}
	
	public void addImage(Fits fitsFile) throws FitsException, IOException{
		//retrieve image from FITS file
		FitsImage fitsImage = new BuildFitsImage(fitsFile).call();
		Image image = fitsImage;
		
		//prepare the view which holds the image
		ImageView view = new ImageView(image);
		view.autosize();
		view.setFitWidth(getParent().getScene().getWidth());
		view.setPreserveRatio(true);
		view.setCache(true);
		
	    //add image view to the label which will be displayed
		setGraphic(view);
	}
}
