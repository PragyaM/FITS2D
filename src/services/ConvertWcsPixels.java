package services;

import java.awt.Point;
import java.util.ArrayList;

import uk.ac.starlink.ast.FitsChan;
import uk.ac.starlink.ast.FrameSet;


public class ConvertWcsPixels {
	
	private FrameSet fsConvert;
	
	public ConvertWcsPixels(FitsChan chanFrom, FitsChan chanTo){
		/* SETUP */
		
		/* Read in a FrameSet from chanFrom holding FITS-WCS Headers. */
		chanFrom.clear("Card");
		FrameSet fsFrom = (FrameSet) chanFrom.read();

		/* Read in a FrameSet from chanTo holding FITS-WCS Headers. */
		chanTo.clear("Card");
		FrameSet fsTo = (FrameSet) chanTo.read();

		/* We want to align the two FrameSets in world coordinates, so invert them
	   before using the Convert method. */
		fsFrom.invert();
		fsTo.invert();
		fsConvert = fsFrom.convert( fsTo, "SKY" );

		/* The "fs3" FrameSet describes the transformation from pixel coords in
	   header1 to pixel coords in header2. */
	}

	
	/**
	 * 
	 * @param points The points defining pixel locations to be mapped to pixel coordinates in another WCS
	 * @return an ArrayList of points defining converted pixel coordinates
	 */
	public ArrayList<Point> convertPixels(ArrayList<Point> points){
		double[] xin = new double[points.size()];
		double[] yin = new double[points.size()];
		
		for (int i = 0; i < points.size(); i++){
			xin[i] = points.get(i).x;
			yin[i] = points.get(i).y;
		}
		
		double[][] result = fsConvert.tran2( points.size(), xin, yin, true );
		double[] xout = result[0];
		double[] yout = result[1];
		
		ArrayList<Point> convertedPoints = new ArrayList<Point>();
		
		for (int i = 0; i < points.size(); i++){
			Point p = new Point();
			p.setLocation(xout[i], yout[i]);
			convertedPoints.add(p);
		}
		
		return convertedPoints;
	}
}
