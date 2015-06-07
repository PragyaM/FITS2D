package services;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

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
	
	public static ArrayList<Point> fill(Canvas c, Point orig){
		ArrayList<Point> points = new ArrayList<Point>();
		
		//setup required information:
		canvas = c;
		width = (int) canvas.getWidth();
		height = (int) canvas.getHeight();
		gc = canvas.getGraphicsContext2D();
		wImg = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
		pWrite = gc.getPixelWriter();
		updatePixelReader();
		
		Color target = pRead.getColor(orig.x, orig.y);
		Color replacement = Color.RED;
		Point node = orig;

		int width = (int) canvas.getWidth();
		int height = (int) canvas.getHeight();
		Deque<Point> queue = new LinkedList<Point>();
		do {
			int x = node.x;
			int y = node.y;
			while (x > 0 && pRead.getColor(x - 1, y).equals(target)) {
				x--;
			}
			boolean spanUp = false;
			boolean spanDown = false;
			while (x < width && pRead.getColor(x, y).equals(target)) {
				pWrite.setColor(x, y, replacement);
				updatePixelReader();
				points.add(new Point(x, y));  //add to points
				if (!spanUp && y > 0 && pRead.getColor(x, y - 1).equals(target)) {
					queue.add(new Point(x, y - 1));
					spanUp = true;
				} else if (spanUp && y > 0 && !(pRead.getColor(x, y - 1).equals(target))) {
					spanUp = false;
				}
				if (!spanDown && y < height - 1 && pRead.getColor(x, y + 1).equals(target)) {
					queue.add(new Point(x, y + 1));
					spanDown = true;
				} else if (spanDown && y < height - 1 && !(pRead.getColor(x, y + 1).equals(target))) {
					spanDown = false;
				}
				x++;
			}
		} while ((node = queue.pollFirst()) != null);

		return points;

	}
	
	private static void updatePixelReader(){
		SnapshotParameters sp = new SnapshotParameters();
	    sp.setFill(Color.TRANSPARENT);
		canvas.snapshot(sp, wImg);
		pRead = wImg.getPixelReader();
	}
}
