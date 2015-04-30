package services;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;
import nom.tam.image.ImageTiler;

public class BuildFitsImage {
	private Fits fitsFile;
	private ImageHDU hdu;
	private int hduWidth;
	private int hduHeight;
	
	//TODO handle uncaught exceptions
	public BuildFitsImage(Fits fitsFile) throws FitsException, IOException{
		this.fitsFile = fitsFile;
	}
	
	//TODO handle uncaught exceptions
	public FitsImage call() throws FitsException, IOException{
		//grab image data from FITS file
		hdu = (ImageHDU) fitsFile.getHDU(0);
		hduWidth = hdu.getAxes()[1];
		hduHeight = hdu.getAxes()[0];
		
		//use image tiler to retrieve data for the full image
		ImageTiler tiler = hdu.getTiler();
	    float[] img = (float[]) tiler.getTile(new int[]{0, 0}, hdu.getAxes());
	    
	    //write image data
	    BufferedImage im = new BufferedImage(hduWidth, hduHeight, BufferedImage.TYPE_BYTE_BINARY);
		WritableRaster raster = im.getRaster();
		raster.setPixels(0, 0, hduWidth, hduHeight, img);
		
		//convert image to format that can be displayed
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(im, "png", out);
		out.flush();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		
		FitsImage fitsImage = new FitsImage(in);
		//TODO: set properties of fitsImage
		
		return fitsImage;
	}
}
