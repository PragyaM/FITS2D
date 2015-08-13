package models;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;

import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;
import nom.tam.image.StandardImageTiler;
import nom.tam.util.ArrayFuncs;
import controllers.GUIController;
import controllers.ImageController;

public class FitsImage{
	private ImageHDU hdu;
	private Fits fitsFile;
	private StandardImageTiler tiler;
	private int width;
	private int height;

	private double[] imageFriendlyData;
	private Color nanColour;

	private double[][] processingFriendlyData;
	private Image image;
	private ImageController controller;

	//TODO handle uncaught exceptions
	public FitsImage(Fits fitsFile, ImageController controller) throws FitsException, IOException{
		this.controller = controller;
		this.fitsFile = fitsFile;
		this.hdu = (ImageHDU) fitsFile.getHDU(0);;
		width = hdu.getAxes()[1];
		height = hdu.getAxes()[0];
		this.tiler =  hdu.getTiler();
		setNanColour(controller.getNanColour());
		prepareData();
		writeImage();
		printFitsInfo();
	}

	private void prepareData(){

		double[] img = null;
		try {
			Object dataArray = tiler.getTile(new int[]{0, 0}, hdu.getAxes());
			img = (double[]) ArrayFuncs.convertArray(dataArray, double.class);
			processingFriendlyData =  (double[][]) ArrayFuncs.convertArray(hdu.getKernel(), double.class);
			imageFriendlyData = new double[width * height];
			for (int h = height - 1; h >= 0; h--){
				for (int w = 0; w < width; w++){
					imageFriendlyData[h * width + w] = (img[(height-1 - h)*width + w]);
				}
			}
		} catch (IOException | FitsException e) {
			e.printStackTrace();
		}

		System.out.println(processingFriendlyData.length + ", " + processingFriendlyData[0].length);
	}

	public ArrayList<Point> getDataPositions(Point p, int cWidth, int cHeight){
		HashSet<Point> points = new HashSet<Point>();
		int x0 = (p.x * width)/cWidth;
		int y0 = (p.y * height)/cHeight;

		int x1 = x0 + width/cWidth;
		int y1 = y0 + height/cHeight;

		for (int x = x0; x <= x1; x++){
			for (int y = y0; y <= y1; y++){
				Point pos = new Point(x, height - y);
				points.add(pos);
			}
		}
		return (new ArrayList<Point>(points));
	}

	public double getValueAt(Point p){
		if (p.x < width && p.y < height && p.x >= 0 && p.y >= 0){
			return processingFriendlyData[p.y][p.x];
		} else return processingFriendlyData[0][0];
	}

	public Fits getFitsFile(){
		return fitsFile;
	}

	public Image getImage(){
		return image;
	}

	public double[][] getData(){
		return processingFriendlyData;
	}

	public ImageHDU getHDU(){
		return hdu;
	}

	//SETUP METHODS:

	public void writeImage() throws IOException{
		//write image data
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		WritableRaster raster = im.getRaster();
		setImageColours(imageFriendlyData, raster);

		//convert image to a format that can be displayed
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(im, "png", out);
		out.flush();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

		image = new Image(in);
	}

	public void setNanColour(Color replacement) throws IOException{
		nanColour = replacement;
	}

	private void setImageColours(double[] imageData, WritableRaster raster){
		double max = hdu.getMaximumValue();
		double cutOff = max * 0.025;

		int colBandRange = 250;
		double segmentSize = (double) (cutOff/colBandRange);

		for (int i = 0; i < imageData.length; i++){
			double val = (double) (imageData[i]);
			int x = i % width;
			int y = (int) Math.ceil(i / width);
			if (isNaN(val) || val <= 0) {
				raster.setPixel(x, y, new double[]{nanColour.getRed()*250, nanColour.getGreen()*250, nanColour.getBlue()*250, nanColour.getOpacity()*250});
			} 
			else if (val > cutOff){
				raster.setPixel(x, y, new double[]{250, 250, 250,250});
			}
			else {
				double colVal = val/segmentSize;
				raster.setPixel(x, y, new double[]{colVal, colVal, colVal, 250});
			}
		}
	}

	private boolean isNaN(double d){
		return d != d;
	}

	/**
	 * @param wavelength - expected to be within the 380 to 780 range (where visible colors can be resolved).
	 * @return a Color containing calculated RGB values
	 */
	public Color getColorFromWavelength(double wavelength){
		double red = 0;
		double green = 0;
		double blue = 0;
		double SSS;

		if (wavelength < 380){
			red = 1.0;
			green = 1.0;
			blue = 1.0;
		}
		if (wavelength >= 380 && wavelength < 440){
			red = -(wavelength - 440) / (440 - 350);
			green = 0.1; //0.0;
			blue = 0.9; //1.0;
		}
		else if (wavelength >= 440 && wavelength < 490){
			red = 0.1; //0.0;
			green = (wavelength - 440) / (490 - 440);
			blue = 0.8; //1.0;
		}
		else if (wavelength >= 490 && wavelength < 510){
			red = 0.1; //0.0;
			green = 0.9; //1.0;
			blue = -(wavelength - 510) / (510 - 490);
		}
		else if (wavelength >= 510 && wavelength < 580){
			red = (wavelength - 510) / (580 - 510);
			green = 0.8; //1.0;
			blue = 0.2;
		}
		else if (wavelength >= 580 && wavelength < 645){
			red = 1.0; //1.0;
			green = -(wavelength - 645) / (645 - 580);
			blue = 0.3; //0.0;
		}
		else if (wavelength >= 645 && wavelength <= 780){
			red = 1.0; //1.0;
			green = 0.6;  //0.0;
			blue = 0.4; //0.0;
		}
		else{
			red = 0.0;
			green = 0.0;
			blue = 0.0;
		}

		// intensity correction
		if (wavelength >= 380 && wavelength < 420){
			SSS = 0.3 + 0.7*(wavelength - 350) / (420 - 350);
		}
		else if (wavelength >= 420 && wavelength <= 700){
			SSS = 1.0;
		} 
		else if (wavelength > 700 && wavelength <= 780){
			SSS = 0.3 + 0.7*(780 - wavelength) / (780 - 700);
		} 
		else{
			SSS = 0.0;
		}
		SSS *= 255;

		Color c = Color.rgb((int)(SSS*red), (int)(SSS*green), (int)(SSS*blue));

		return c;
	}

	//TODO add methods for manipulating FITS image data

	public void printFitsInfo(){
		System.out.println("Number of HDUs: " + fitsFile.getNumberOfHDUs());
		System.out.println("Author: " + hdu.getAuthor());
		//CRVAL1 and CRVAL2 describe the coordinates for the center of the image
		System.out.println("NAXIS1: " + hdu.getHeader().getDoubleValue("NAXIS1"));
		System.out.println("NAXIS2: " + hdu.getHeader().getDoubleValue("NAXIS2"));
		
		System.out.println("CRVAL1: " + hdu.getHeader().getDoubleValue("CRVAL1"));
		System.out.println("CRVAL2: " + hdu.getHeader().getDoubleValue("CRVAL2"));
		
		System.out.println("CRPIX1: " + hdu.getHeader().getDoubleValue("CRPIX1"));
		System.out.println("CRPIX2: " + hdu.getHeader().getDoubleValue("CRPIX2"));
		
		System.out.println("CDELT1: " + hdu.getHeader().getDoubleValue("CDELT1"));
		System.out.println("CDELT2: " + hdu.getHeader().getDoubleValue("CDELT2"));
		
		System.out.println("CTYPE1: " + hdu.getHeader().getStringValue("CTYPE1"));
		System.out.println("CTYPE2: " + hdu.getHeader().getStringValue("CTYPE2"));
		
		try {
			System.out.println("BitPix" + hdu.getBitPix());
		} catch (FitsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Color getNanColour() {
		return nanColour;
	}
	
	public double getCRVAL1(){
		return hdu.getHeader().getDoubleValue("CRVAL1");
	}
	
	public double getCRVAL2(){
		return hdu.getHeader().getDoubleValue("CRVAL2");
	}
}
