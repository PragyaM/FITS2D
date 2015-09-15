package test.services;

import java.awt.Point;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;

import services.ConvertPixels;

public class ConvertPixelsTest extends TestCase{

	public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();
	protected ArrayList<Point> imagePixels;
	protected ArrayList<Point> canvasPixels;
	protected ConvertPixels pixelConverter;
	
	protected void setUp(){
		
		canvasPixels = new ArrayList<Point>();
		canvasPixels.add(new Point(0, 0));
		canvasPixels.add(new Point(999, 0));
		canvasPixels.add(new Point(0, 299));
		canvasPixels.add(new Point(999, 299));
		canvasPixels.add(new Point(25, 50));
		
		imagePixels = new ArrayList<Point>();
		imagePixels.add(new Point(0, 0));
		imagePixels.add(new Point(1, 0));
		imagePixels.add(new Point(0, 1));
		imagePixels.add(new Point(1, 1));
		imagePixels.add(new Point(1999, 0));
		imagePixels.add(new Point(1999, 1));
		imagePixels.add(new Point(1998, 0));
		imagePixels.add(new Point(1998, 1));
		imagePixels.add(new Point(0, 599));
		imagePixels.add(new Point(1, 599));
		imagePixels.add(new Point(0, 598));
		imagePixels.add(new Point(1, 598));
		imagePixels.add(new Point(1999, 599));
		imagePixels.add(new Point(1999, 598));
		imagePixels.add(new Point(1998, 599));
		imagePixels.add(new Point(1998, 598));
		imagePixels.add(new Point(50, 100));
		imagePixels.add(new Point(51, 100));
		imagePixels.add(new Point(50, 101));
		imagePixels.add(new Point(51, 101));
	}

	@Test
	public void testSmallImageToLargeCanvas(){
		pixelConverter = new ConvertPixels(600, 300);

		ArrayList<Point> outputCanvasPixels = pixelConverter.imageToCanvas(imagePixels);
		ArrayList<Point> inputImagePixels = pixelConverter.canvasToImage(outputCanvasPixels);
		
		assertTrue(inputImagePixels.containsAll(imagePixels));
	}
	
	@Test
	public void testLargeImageToSmallCanvas(){
		pixelConverter = new ConvertPixels(300, 600);

		ArrayList<Point> outputCanvasPixels = pixelConverter.imageToCanvas(imagePixels);
		ArrayList<Point> inputImagePixels = pixelConverter.canvasToImage(outputCanvasPixels);
		
		assertTrue(inputImagePixels.containsAll(imagePixels));
	}

	@Test
	public void testSmallCanvasToLargeImage(){
		pixelConverter = new ConvertPixels(300, 600);

		ArrayList<Point> outputImagePixels = pixelConverter.canvasToImage(canvasPixels);
		ArrayList<Point> inputCanvasPixels = pixelConverter.imageToCanvas(outputImagePixels);
		
		assertTrue(inputCanvasPixels.containsAll(canvasPixels));
	}
	
	@Test
	public void testLargeCanvasToSmallImage(){
		pixelConverter = new ConvertPixels(600, 300);

		ArrayList<Point> outputImagePixels = pixelConverter.canvasToImage(canvasPixels);
		ArrayList<Point> inputCanvasPixels = pixelConverter.imageToCanvas(outputImagePixels);
		
		assertTrue(inputCanvasPixels.containsAll(canvasPixels));
	}

}
