package services;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;

import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;
import nom.tam.image.ImageTiler;

public class BuildFitsImage {
	private Fits fitsFile;
	private ImageHDU hdu;

	//TODO handle uncaught exceptions
	public BuildFitsImage(Fits fitsFile) throws FitsException, IOException{
		this.fitsFile = fitsFile;
	}

	//TODO handle uncaught exceptions
	public FitsImage call() throws FitsException, IOException{
		
		//grab image data from FITS file
//		hdu = (ImageHDU) fitsFile.getHDU(0);
//		hduWidth = hdu.getAxes()[1];
//		hduHeight = hdu.getAxes()[0];
		
//		System.out.println("bunit: " + hdu.getBUnit());

		//use image tiler to retrieve data for the full image
//		ImageTiler tiler = hdu.getTiler();
//		float[] img = (float[]) tiler.getTile(new int[]{0, 0}, hdu.getAxes());
		
//		ArrayList<Float> mirrorImg = new ArrayList<Float>();
//		float[] mirrorImg = new float[hduWidth * hduHeight];
//		for (int h = hduHeight - 1; h >= 0; h--){
//			for (int w = 0; w < hduWidth; w++){
//				mirrorImg[h * hduWidth + w] = (img[(hduHeight-1 - h)*hduWidth + w]);
//			}
//		}
		
		
		//write image data
//		BufferedImage im = new BufferedImage(hduWidth, hduHeight, BufferedImage.TYPE_3BYTE_BGR);
//		
//		WritableRaster raster = im.getRaster();
//		writeColourImage(mirrorImg, raster);
//		
//		//convert image to a format that can be displayed
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		ImageIO.write(im, "png", out);
//		out.flush();
//		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
//
//		Image image = new Image(in);
		
		//TODO: set properties of fitsImage
		
		FitsImage fitsImage = new FitsImage(fitsFile);
		return fitsImage;
	}
	
//	public void writeColourImage(float[] imageData, WritableRaster raster){
//		double max = hdu.getMaximumValue();
//		double cutOff = max * 0.01;
//		
//		int colBandRange = 780 - 380;  //16777215;
//		float segmentSize = (float) (cutOff/colBandRange);
//
//		for (int i = 0; i < imageData.length; i++){
//			float val = (float) (imageData[i]);
//			int x = i % hduWidth;
//			int y = (int) Math.ceil(i / hduWidth);
//			if (val == Double.NaN || val <= 0) {
//				raster.setSample(x, y, 0, 0);
//				raster.setSample(x, y, 1, 0);
//				raster.setSample(x, y, 2, 0);
//			} 
//			else if (val > cutOff){
//				raster.setSample(x, y, 0, 255);
//				raster.setSample(x, y, 1, 255);
//				raster.setSample(x, y, 2, 255);
//			}
//			else {
//				float colVal = val/segmentSize + 380;
//				Color c = getColorFromWavelength(colVal);
//				raster.setSample(x, y, 0, c.getRed()*255);
//				raster.setSample(x, y, 1, c.getGreen()*255);
//				raster.setSample(x, y, 2, c.getBlue()*255);
//			}
//		}
//
//	}
//	
//	/**
//	 * @param wavelength - expected to be within the 380 to 780 range (where visible colors can be resolved).
//	 * @return a Color containing calculated RGB values
//	 */
//	public Color getColorFromWavelength(float wavelength){
//		double red = 0;
//		double green = 0;
//		double blue = 0;
//		double SSS;
//		
//		if (wavelength >= 380 && wavelength < 440){
//			red = -(wavelength - 440) / (440 - 350);
//	        green = 0.0;
//	        blue = 1.0;
//		}
//		else if (wavelength >= 440 && wavelength < 490){
//			red = 0.0;
//	        green = (wavelength - 440) / (490 - 440);
//	        blue = 1.0;
//		}
//		else if (wavelength >= 490 && wavelength < 510){
//			red = 0.0;
//	        green = 1.0;
//	        blue = -(wavelength - 510) / (510 - 490);
//		}
//		else if (wavelength >= 510 && wavelength < 580){
//			red = (wavelength - 510) / (580 - 510);
//	        green = 1.0;
//	        blue = 0.0;
//		}
//		else if (wavelength >= 580 && wavelength < 645){
//			red = 1.0;
//	        green = -(wavelength - 645) / (645 - 580);
//	        blue = 0.0;
//		}
//		else if (wavelength >= 645 && wavelength <= 780){
//			red = 1.0;
//	        green = 0.0;
//	        blue = 0.0;
//		}
//	    else{
//	    	red = 0.0;
//	        green = 0.0;
//	        blue = 0.0;
//	    }
//
//	    // intensity correction
//	    if (wavelength >= 380 && wavelength < 420){
//	    	SSS = 0.3 + 0.7*(wavelength - 350) / (420 - 350);
//	    }
//	    else if (wavelength >= 420 && wavelength <= 700){
//	    	SSS = 1.0;
//	    } 
//	    else if (wavelength > 700 && wavelength <= 780){
//	    	SSS = 0.3 + 0.7*(780 - wavelength) / (780 - 700);
//	    } 
//	    else{
//	    	SSS = 0.0;
//	    }
//	    SSS *= 255;
//
//	    Color c = Color.rgb((int)(SSS*red), (int)(SSS*green), (int)(SSS*blue));
//		
//		return c;
//	}
}
