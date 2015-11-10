package services;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;

import models.Histogram;
import nom.tam.fits.ImageHDU;

public class RenderImageTask extends Task<Image>{

	protected final Color nanColour;
	protected final double[] imageData;
	protected final ImageHDU hdu;
	protected final int height;
	protected final int width;
	protected Image image;
	protected Histogram histogram;

	public RenderImageTask(Color nanC, double[] data, 
			ImageHDU imgHdu, int h, int w, Histogram hist){
		nanColour = nanC;
		imageData = data;
		hdu = imgHdu;
		height = h;
		width = w;
		histogram = hist;
	}

	@Override
	protected Image call() throws Exception {
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		WritableRaster raster = im.getRaster();
		setImageColours(imageData, raster);

		//convert image to a format that can be displayed
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(im, "png", out);
		out.flush();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

		image = new Image(in);
		out.close();
		in.close();

		this.updateProgress(1, 1);
		
		return image;
	}

	private void setImageColours(double[] imageData, WritableRaster raster){
		/* needs: lowerBound, upperBound and colBandRange */
		int colBandRange = 250;
		double lowerBound = histogram.getVisibleRangeMin();
		double upperBound = histogram.getVisibleRangeMax();
		
		double segmentSize = (double) ((upperBound - lowerBound)/colBandRange);
		double[] black = new double[]{0, 0, 0,250};
		double[] nan = new double[]{nanColour.getRed()*250, 
				nanColour.getGreen()*250, nanColour.getBlue()*250, nanColour.getOpacity()*250};
		double[] white = new double[]{250, 250, 250,250};

		for (int i = 0; i < imageData.length; i++){
			double val = (double) (imageData[i]);
			int x = i % width;
			int y = (int) Math.ceil(i / width);
			if (Double.isNaN(val)) {
				raster.setPixel(x, y, nan);
			}
			else if (val < lowerBound) {
				raster.setPixel(x, y, black);
			}
			else if (val > upperBound) {
				raster.setPixel(x, y, white);
			}
			else {
				double colVal = (val - lowerBound)/segmentSize;
				raster.setPixel(x, y, new double[]{colVal, colVal, colVal, 250});
			}
		}
	}
	
	public Image getImage(){
		return image;
	}

}
