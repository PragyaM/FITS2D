package models;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import views.FitsCanvas;
import views.FitsCanvas.Mode;
import controllers.AnnotationsController;

public class Annotation extends Drawing{

	public Annotation(FitsCanvas canvas, AnnotationsController controller,
			Color color) {
		super(canvas, controller, color);
	}

	@Override
	public void handle(MouseEvent event) {
		if (canvas.mode==Mode.ANNOTATION_DRAW){
			super.drawAction(event);
			((AnnotationsController) controller).enableUndoButton();
		}
		else if (canvas.mode==Mode.ANNOTATION_FILL){
			super.floodFillAction(event);
			((AnnotationsController) controller).enableUndoButton();
		}
	}

	public String toString(){
		StringBuilder annotationString = new StringBuilder("\na"); //"Colour: " + color.toString();
		//annotationString.append("\nColour: ");
		//annotationString.append(color.toString());
		for (PixelRegion region : regions){
			annotationString.append('\n');
			annotationString.append(region.toString());
		}
		return annotationString.toString();
	}
}
