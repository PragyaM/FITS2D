package services;

import java.awt.Point;
import java.util.ArrayList;

import javafx.concurrent.Task;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class DoFill extends Task<ArrayList<Point>>{
	
	private final int width;
	private final int height;
	private final WritableImage wImg;
	private final Canvas canvas;
	private final Point origin;
	private final int target = 0;
	private final ArrayList<Point> points;
	private final int[][] canvasState;
	private final int replacement;
	private final Color replacementColor;
	private final Color targetColor;
	private final PixelReader pRead;
	
	public DoFill(Canvas c, Point orig, Color replaceCol){
		origin = orig;
		points = new ArrayList<Point>();
		replacementColor = replaceCol;
		
		//setup required information:
		canvas = c;
		width = (int) canvas.getWidth();
		height = (int) canvas.getHeight();
		wImg = new WritableImage(width, height); //FIXME: doesn't take zoom into account
		SnapshotParameters sp = new SnapshotParameters();
	    sp.setFill(Color.TRANSPARENT);
		canvas.snapshot(sp, wImg);
		pRead = wImg.getPixelReader();
		targetColor = pRead.getColor(origin.x, origin.y);
		if (targetColor.equals(replacementColor)){
			replacement = target;
		} else replacement = 1;
		
		canvasState = getCanvasState();
	}
	
	private ArrayList<Point> doFill(Point node, ArrayList<Point> points){
		if (target == replacement){
			return points;
		}
		else if (node.x < 0 || node.y < 0 || node.x > width || node.y > height){
			return points;
		}
		else if (!(canvasState[node.x][node.y] == target)){
			return points;
		}
		canvasState[node.x][node.y] = replacement;
		points.add(node);
		
		doFill(new Point(node.x-1, node.y), points);
		doFill(new Point(node.x+1, node.y), points);
		doFill(new Point(node.x, node.y-1), points);
		doFill(new Point(node.x, node.y+1), points);
		return points;
	}
	
	private int[][] getCanvasState(){
		
		int[][] cState = new int[width][height];
		for (int x = 0; x < width; x++){
			for (int y = 0; y<height; y++){
				if (pRead.getColor(x, y).equals(replacementColor)){
					cState[x][y] = replacement;
				}
				else cState[x][y] = target;
			}
		}
		return cState;
	}
	
	public ArrayList<Point> getPoints(){
		return points;
	}

	@Override
	protected ArrayList<Point> call() throws Exception {
		return doFill(origin, points);
	}

}
