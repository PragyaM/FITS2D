package models;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;

import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;
import nom.tam.image.ImageTiler;

@SuppressWarnings("serial")
public class FitsImage{
	private Fits fitsFile;
	private Image image;
	private ImageHDU hdu;
	private int hduWidth;
	private int hduHeight;
	
	//TODO handle uncaught exceptions
	public FitsImage(Fits fitsFile) throws FitsException, IOException{
		this.fitsFile = fitsFile;
		setupImage();
	}
	
	//TODO handle uncaught exceptions
	private void setupImage() throws FitsException, IOException{
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
		this.image = im;
	}
	
	public Image getImage(){
		return this.image;
	}
	
	//TODO handle uncaught exceptions
	public Image getScaledImage(Dimension maxSize) throws FitsException{
		if (maxSize.getHeight()/maxSize.getWidth() == hduHeight/hduWidth){
			return image.getScaledInstance((int) maxSize.getWidth(), (int) maxSize.getHeight(), 0);
		} 
		else{
			double ratio = (maxSize.getWidth()/hduWidth);
			int imgWidth = (int) maxSize.getWidth();
			int imgHeight = (int) (hduHeight*ratio);
			return image.getScaledInstance(imgWidth, imgHeight, 0);
		}
	}

}
