package models;

import java.io.InputStream;

import javafx.scene.image.Image;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;

public class FitsImage extends Image{
	private InputStream is;
	private ImageHDU hdu;
	private Fits fitsFile;
	
	//TODO handle uncaught exceptions
	public FitsImage(InputStream is, Fits fitsFile, ImageHDU hdu){
		super(is);
		this.is = is;
		this.fitsFile = fitsFile;
		this.hdu = hdu;
	}
	
	//TODO add methods for manipulating FITS image data
	
	public void printFitsInfo(){
		float[][] data = (float[][]) hdu.getKernel();
		for (int i = 0; i < data.length; i++){
			for (int j = 0; j < data[i].length; j++){
				System.out.print(data[i][j] + ", ");
			}
			System.out.println();
		}
		
		System.out.println("Number of HDUs: " + fitsFile.getNumberOfHDUs());
		System.out.println("Author: " + hdu.getAuthor());
		try {
			System.out.println("BitPix" + hdu.getBitPix());
		} catch (FitsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
