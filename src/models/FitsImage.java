package models;

import java.io.InputStream;

import javafx.scene.image.Image;

public class FitsImage extends Image{
	private InputStream is;
	
	//TODO handle uncaught exceptions
	public FitsImage(InputStream is){
		super(is);
		this.is = is;
	}
	
	//TODO add methods for manipulating FITS image data
}
