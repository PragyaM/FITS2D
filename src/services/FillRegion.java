package services;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FillRegion {
	
	private static int width;
	private static int height;
	private static WritableImage wImg;
	private static PixelReader pRead;
	private static PixelWriter pWrite;
	private static GraphicsContext gc;
	private static Canvas canvas;
	
	public static ArrayList<Point> fill(Canvas c, Point origin){
		ArrayList<Point> points = new ArrayList<Point>();
		
		//setup required information:
		canvas = c;
		width = (int) canvas.getWidth();
		height = (int) canvas.getHeight();
		gc = canvas.getGraphicsContext2D();
		wImg = new WritableImage(width, height);
		pWrite = gc.getPixelWriter();
		updatePixelReader();
		
		Color target = pRead.getColor(origin.x, origin.y);
		points = doFill(origin, target, Color.RED, points);
		
		return points;
	}
	
	private static ArrayList<Point> doFill(Point node, Color target, Color replacement, ArrayList<Point> points){
		if (target.equals(replacement)){
			return points;
		}
		else if (!(pRead.getColor(node.x, node.y).equals(target))){
			return points;
		}
		else if (node.x < 0 || node.y < 0 || node.x > width || node.y > height){
			return points;
		}
		pWrite.setColor(node.x, node.y, replacement);
		points.add(node);
		updatePixelReader();
		
		doFill(new Point(node.x-1, node.y), target, replacement, points);
		doFill(new Point(node.x+1, node.y), target, replacement, points);
		doFill(new Point(node.x, node.y-1), target, replacement, points);
		doFill(new Point(node.x, node.y+1), target, replacement, points);
		return points;
	}
	
	private static void updatePixelReader(){
		SnapshotParameters sp = new SnapshotParameters();
	    sp.setFill(Color.TRANSPARENT);
		canvas.snapshot(sp, wImg);
		pRead = wImg.getPixelReader();
	}
}
