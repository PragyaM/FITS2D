package services;

import java.io.File;
import java.io.IOException;

import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;
import controllers.ImageController;

public class BuildFitsImage {
	private Fits fitsFile;
	private ImageHDU hdu;
	private ImageController controller;

	//TODO handle uncaught exceptions
	public BuildFitsImage(Fits fitsFile, ImageController controller) throws FitsException, IOException{
		this.controller = controller;
		this.fitsFile = fitsFile;
	}

	//TODO handle uncaught exceptions
	public FitsImage call() throws FitsException, IOException{		
		FitsImage fitsImage = new FitsImage(fitsFile, controller);
		return fitsImage;
	}
}
