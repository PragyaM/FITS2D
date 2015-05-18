package services;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
		
		System.out.println("bunit: " + hdu.getBUnit());

		//use image tiler to retrieve data for the full image
		
		ImageTiler tiler = hdu.getTiler();
		float[] img = (float[]) tiler.getTile(new int[]{0, 0}, hdu.getAxes());

		//write image data

		BufferedImage im = new BufferedImage(hduWidth, hduHeight, BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster raster = im.getRaster();
		
		double max = hdu.getMaximumValue();
		double cutOff = max * 0.75;
		
		int colBandRange = 16777215;
		float segmentSize = (float) (cutOff/colBandRange);

		for (int i = 0; i < img.length; i++){
			float val = (float) (img[i]);
			int x = i % hduWidth;
			int y = i / hduWidth;
			if (val == Double.NaN || val <= 0) {
				raster.setSample(x, y, 0, 0);
				raster.setSample(x, y, 1, 0);
				raster.setSample(x, y, 2, 0);
			} 
			else if (val > cutOff){
				raster.setSample(x, y, 0, 255);
				raster.setSample(x, y, 1, 255);
				raster.setSample(x, y, 2, 255);
			}
			else {
				float colVal = val/segmentSize;
				Color c = getColorFromFloat(colVal);
				raster.setSample(x, y, 0, c.getRed()*256);
				raster.setSample(x, y, 1, c.getGreen()*256);
				raster.setSample(x, y, 2, c.getBlue()*256);
				
				//Old hacky code
//				if (val < 10){
//					raster.setSample(x, y, 0, colVal*20);
//					raster.setSample(x, y, 1, colVal*10);
//					raster.setSample(x, y, 2, colVal);
//				}
//				else if (val < 60){
//					raster.setSample(x, y, 0, colVal*85);
//					raster.setSample(x, y, 1, colVal*90);
//					raster.setSample(x, y, 2, colVal);
//				}
//				else if (val <= (int) hdu.getMaximumValue() + offset){
//					raster.setSample(x, y, 0, colVal*20);
//					raster.setSample(x, y, 1, colVal*2);
//					raster.setSample(x, y, 2, colVal*90);
//				}
//				else {
//					raster.setSample(x, y, 0, 0);
//					raster.setSample(x, y, 1, 0);
//					raster.setSample(x, y, 2, 0);
//				}
			}
		}

		//convert image to format that can be displayed
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(im, "png", out);
		out.flush();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

		FitsImage fitsImage = new FitsImage(in, fitsFile, hdu);
		//TODO: set properties of fitsImage

		return fitsImage;
	}
	
	public Color getColorFromFloat(float fVal){
		int red = 0;
		int green = 0;
		int blue = 0;
		
		blue = (int) Math.floor(fVal / 256 / 256 );
		green = (int) Math.floor((fVal - blue * 256 * 256) / 256);
		red = (int) Math.floor(fVal - blue * 256 * 256 - green * 256);

		Color c = Color.rgb(red, green, blue);
		
		return c;
	}
}
