package models;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.Header;
import nom.tam.fits.HeaderCard;
import nom.tam.fits.ImageHDU;
import nom.tam.image.StandardImageTiler;
import nom.tam.util.ArrayFuncs;
import services.RenderImageTask;
import controllers.ImageController;

public class FitsImage{
	private ImageHDU hdu;
	private Fits fitsFile;
	private StandardImageTiler tiler;
	private int width;
	private int height;

	private double[] imageFriendlyData;
	private Color nanColour;
	private float[] data;
	private double[][] processingFriendlyData;
	private Image image;
	private ImageController controller;

	protected double maxValue;
	protected double minValue;
	private Histogram histogram;

	//TODO handle uncaught exceptions
	public FitsImage(Fits fitsFile, ImageController controller) 
			throws FitsException, IOException{
		this.controller = controller;
		this.fitsFile = fitsFile;
		this.hdu = (ImageHDU) fitsFile.getHDU(0);;
		width = hdu.getAxes()[1];
		height = hdu.getAxes()[0];
		this.tiler =  hdu.getTiler();
		setNanColour(controller.getNanColour());
		prepareData();
		createHistogram();
		minValue = histogram.getMinValue();
		maxValue = histogram.getMaxValue();
		writeImage();
	}

	private void createHistogram(){
		histogram = new Histogram(imageFriendlyData, width, height);
	}

	private void prepareData(){

		double[] img = null;
		try {
			Object dataArray = tiler.getTile(new int[]{0, 0}, hdu.getAxes());
			img = (double[]) ArrayFuncs.convertArray(dataArray, double.class);
			data = (float[]) ArrayFuncs.convertArray(img, float.class);
			processingFriendlyData =  (double[][]) ArrayFuncs.convertArray(
					hdu.getKernel(), double.class);
			imageFriendlyData = new double[width * height];
			for (int h = height - 1; h >= 0; h--){
				for (int w = 0; w < width; w++){
					imageFriendlyData[h * width + w] = (img[(height-1 - h)*width + w]);
				}
			}
		} catch (IOException | FitsException e) {
			e.printStackTrace();
		}

		System.out.println(processingFriendlyData.length + ", " + 
				processingFriendlyData[0].length);
	}

	public float[] getRawData(){
		return data;
	}

	public double getValueAt(Point p){
		if (p.x < width && p.y < height && p.x >= 0 && p.y >= 0){
			return processingFriendlyData[p.y][p.x];
		} else return processingFriendlyData[0][0];
	}

	public int getHeight(){
		return height;
	}

	public int getWidth(){
		return width;
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

	public String getWcsHeaderCardsString(){
		Header header = hdu.getHeader();
		Header filteredHeader = new Header();

		//now adjust WCS values:
		ArrayList<HeaderCard> cards = new ArrayList<HeaderCard>();
		cards.add(header.findCard("CTYPE1"));
		cards.add(header.findCard("CTYPE2"));	
		cards.add(header.findCard("RADESYS"));
		cards.add(header.findCard("RADECSYS"));
		cards.add(header.findCard("CRVAL1"));
		cards.add(header.findCard("CRVAL2"));
		cards.add(header.findCard("CDELT1"));
		cards.add(header.findCard("CDELT2"));
		cards.add(header.findCard("EQUINOX"));
		cards.add(header.findCard("BUNIT"));
		cards.add(header.findCard("EPOCH"));
		cards.add(header.findCard("CD1_1"));
		cards.add(header.findCard("CD1_2"));
		cards.add(header.findCard("CD2_1"));
		cards.add(header.findCard("CD2_2"));
		cards.add(header.findCard("CRPIX1"));
		cards.add(header.findCard("CRPIX2"));
		cards.add(header.findCard("END"));

		cards.forEach((card) -> {
			if (card != null){
				try {
					filteredHeader.addLine(card);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		filteredHeader.dumpHeader(ps);
		String wcsHdrString =  baos.toString();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ps.close();

		return wcsHdrString;
	}

	public String getHeaderString(){
		Header hdr = hdu.getHeader();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		hdr.dumpHeader(ps);
		String hdrString =  baos.toString();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ps.close();

		return hdrString;
	}

	public ImageHDU getHDU(){
		return hdu;
	}

	//SETUP METHODS:

	public void writeImage(){
		RenderImageTask renderImage = new RenderImageTask(nanColour, 
				imageFriendlyData, hdu, height, width, histogram);
		//		ProgressBar bar = new ProgressBar();
		//		bar.progressProperty().bind(renderImage.progressProperty());

		ChangeListener<Number> IMAGE_CREATED_LISTENER = new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (renderImage.getWorkDone() == 1) {
					image = renderImage.getImage();
					renderImage.workDoneProperty().removeListener(this);
					controller.adjustViewForImage();
				}
			}
		};

		renderImage.workDoneProperty().addListener(IMAGE_CREATED_LISTENER);
		
		ExecutorService es = controller.createExecutor("RenderImageExecutor");
		es.submit(renderImage);

		image = renderImage.getImage();
	}

	public void setImage(Image img){
		image = img;
	}

	public void setNanColour(Color replacement) throws IOException{
		nanColour = replacement;
	}

	//	/**
	//	 * @param wavelength - expected to be within the 380 to 780 range 
	//	 * (where visible colors can be resolved).
	//	 * @return a Color containing calculated RGB values
	//	 */
	//	public Color getColorFromWavelength(double wavelength){
	//		double red = 0;
	//		double green = 0;
	//		double blue = 0;
	//		double SSS;
	//
	//		if (wavelength < 380){
	//			red = 1.0;
	//			green = 1.0;
	//			blue = 1.0;
	//		}
	//		if (wavelength >= 380 && wavelength < 440){
	//			red = -(wavelength - 440) / (440 - 350);
	//			green = 0.1; //0.0;
	//			blue = 0.9; //1.0;
	//		}
	//		else if (wavelength >= 440 && wavelength < 490){
	//			red = 0.1; //0.0;
	//			green = (wavelength - 440) / (490 - 440);
	//			blue = 0.8; //1.0;
	//		}
	//		else if (wavelength >= 490 && wavelength < 510){
	//			red = 0.1; //0.0;
	//			green = 0.9; //1.0;
	//			blue = -(wavelength - 510) / (510 - 490);
	//		}
	//		else if (wavelength >= 510 && wavelength < 580){
	//			red = (wavelength - 510) / (580 - 510);
	//			green = 0.8; //1.0;
	//			blue = 0.2;
	//		}
	//		else if (wavelength >= 580 && wavelength < 645){
	//			red = 1.0; //1.0;
	//			green = -(wavelength - 645) / (645 - 580);
	//			blue = 0.3; //0.0;
	//		}
	//		else if (wavelength >= 645 && wavelength <= 780){
	//			red = 1.0; //1.0;
	//			green = 0.6;  //0.0;
	//			blue = 0.4; //0.0;
	//		}
	//		else{
	//			red = 0.0;
	//			green = 0.0;
	//			blue = 0.0;
	//		}
	//
	//		// intensity correction
	//		if (wavelength >= 380 && wavelength < 420){
	//			SSS = 0.3 + 0.7*(wavelength - 350) / (420 - 350);
	//		}
	//		else if (wavelength >= 420 && wavelength <= 700){
	//			SSS = 1.0;
	//		} 
	//		else if (wavelength > 700 && wavelength <= 780){
	//			SSS = 0.3 + 0.7*(780 - wavelength) / (780 - 700);
	//		} 
	//		else{
	//			SSS = 0.0;
	//		}
	//		SSS *= 255;
	//
	//		Color c = Color.rgb((int)(SSS*red), (int)(SSS*green), (int)(SSS*blue));
	//
	//		return c;
	//	}

	public void printFitsInfo(){

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

	public Histogram getHistogram() {
		return histogram;
	}

	public void setHistogram(Histogram histogram) {
		this.histogram = histogram;
	}

}
