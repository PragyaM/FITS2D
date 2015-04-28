package models;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class FitsFileChooser extends JFileChooser{
	
	public FitsFileChooser(){
		super(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("FITS FILES", "FITS", "fits");
		setFileFilter(filter);
	}

}
