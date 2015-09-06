package views.tools_area;

import controllers.ImageController;

public class HeaderToolBox extends BaseToolBox{
	
	private ImageController controller;
	
	public HeaderToolBox(ImageController controller){
		super("Header Information");
		this.controller = controller;
		
		controller.getHeaderInfo();
	}

}
