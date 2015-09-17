package models;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import views.FitsCanvas;
import views.FitsCanvas.Mode;
import controllers.SelectionsController;

public class Selection extends Drawing{

	public Selection(FitsCanvas canvas, SelectionsController controller,
			Color color){
		super(canvas, controller, color);
	}
	
	@Override
	public void handle(MouseEvent event) {
		if (canvas.mode==Mode.SELECTION_DRAW){
			super.drawAction(event);
			((SelectionsController) controller).enableUndoButton();
		}
		else if (canvas.mode==Mode.SELECTION_FILL){
			super.floodFillAction(event);
			((SelectionsController) controller).enableUndoButton();
		}		
	}
	
	public String toString(){
		StringBuilder selectionString = new StringBuilder("\ns");
		//selectionString.append("\nColour: ");
		//selectionString.append(color.toString());
		for (PixelRegion region : regions){
			selectionString.append('\n');
			selectionString.append(region.toString());
		}
		return selectionString.toString();
	}
}
