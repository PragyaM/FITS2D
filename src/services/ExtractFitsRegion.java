package services;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.shape.Rectangle;
import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.Header;
import nom.tam.fits.HeaderCard;
import nom.tam.util.ArrayFuncs;

public class ExtractFitsRegion {

	private static Rectangle boundingBox;
	private static double cWidth;
	private static double cHeight;

	public static Fits mapToFits(ArrayList<Point> imagePixels, FitsImage fitsImage, 
			double cw, double ch) throws FitsException, IOException {
		cWidth = cw;
		cHeight = ch;

		/* Get selected data region from FITS data */
		Fits maskFits = new Fits();

		boundingBox = getBoundingBox(fitsImage, imagePixels, cWidth, cHeight);

		double[][] newData = new double[(int) boundingBox.getHeight()][(int) boundingBox.getWidth()];
		newData = setAll(newData, Double.NaN);
		newData = setMaskValues(newData, imagePixels, fitsImage, cWidth, cHeight);

		/* get base type of original data */
		Class type = ArrayFuncs.getBaseClass(fitsImage.getHDU().getKernel());

		Object data = newData;
		ArrayFuncs.convertArray(data, type);
		maskFits.addHDU(Fits.makeHDU(data));

		/* set WCS reference coordinate values */
		setWCSRefs(maskFits, fitsImage);

		return maskFits;
	}

	private static void setWCSRefs(Fits newFits, FitsImage oldFits){
		Header oldHeader = oldFits.getHDU().getHeader();
		
		//now adjust WCS values:
		try {
			ArrayList<HeaderCard> cards = new ArrayList<HeaderCard>();
			cards.add(oldHeader.findCard("CTYPE1"));
			cards.add(oldHeader.findCard("CTYPE2"));	
			cards.add(oldHeader.findCard("RADESYS"));
			cards.add(oldHeader.findCard("RADECSYS"));
			cards.add(oldHeader.findCard("CRVAL1"));
			cards.add(oldHeader.findCard("CRVAL2"));
			cards.add(oldHeader.findCard("CDELT1"));
			cards.add(oldHeader.findCard("CDELT2"));
			cards.add(oldHeader.findCard("EQUINOX"));
			cards.add(oldHeader.findCard("BUNIT"));
			cards.add(oldHeader.findCard("EPOCH"));
			cards.add(oldHeader.findCard("CD1_1"));
			cards.add(oldHeader.findCard("CD1_2"));
			cards.add(oldHeader.findCard("CD2_1"));
			cards.add(oldHeader.findCard("CD2_2"));

			cards.forEach((card) -> {
				if (card != null){
					try {
						newFits.getHDU(0).getHeader().addLine(card);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			newFits.getHDU(0).addValue("CRPIX1", 
					(oldHeader.getDoubleValue("CRPIX1") - boundingBox.getX()),
					"X pixel of tangent point");
			newFits.getHDU(0).addValue("CRPIX2", 
					(oldHeader.getDoubleValue("CRPIX2") - boundingBox.getY()),
					"Y pixel of tangent point");
		} catch (FitsException | IOException e) {
			e.printStackTrace();
		}

	}

	private static Rectangle getBoundingBox(FitsImage fitsImage, 
			ArrayList<Point> imagePixels, double width, double height){
		int minX = Integer.MAX_VALUE;
		int maxX = 0;
		int minY = Integer.MAX_VALUE;
		int maxY = 0;

		for (Point p : imagePixels){
			if (p.x < minX) minX = p.x;
			if (p.x > maxX) maxX = p.x;
			if (p.y < minY) minY = p.y;
			if (p.y > maxY) maxY = p.y;
		}
		
		return new Rectangle(minX, minY, maxX-minX + 1, maxY-minY + 1);
	}

	private static double[][] setAll(double[][] data, double value){
		for (int i=0; i<data.length; i++){
			for (int j=0; j<data[i].length; j++){
				data[i][j] = value;
			}
		}

		return data;
	}

	private static double[][] setMaskValues(double[][] data, ArrayList<Point> imagePixels, 
			FitsImage fitsImage, double cw, double ch){
		for (Point dataPos : imagePixels){
			try {
				data[dataPos.y - (int) boundingBox.getY()][dataPos.x - (int) boundingBox.getX()] 
						= fitsImage.getValueAt(dataPos);
			} catch (Exception e) {
				System.out.println(e.getMessage() + " out of bounds in (" 
						+ (dataPos.y - (int) boundingBox.getY()) + ", " 
						+ (dataPos.x - (int) boundingBox.getX()) + ")");
			}
		}

		return data;
	}

}
