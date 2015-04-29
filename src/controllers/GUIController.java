package controllers;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import models.FitsFileChooser;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import views.MainWindow;

public class GUIController implements ActionListener{
	private MainWindow ui;

	public GUIController(){
		//this is where previously customized configurations should be applied
	}
	
	//TODO handle uncaught exceptions
	public void start() throws FitsException, IOException{
		ui = new MainWindow();
		ui.addImageViewPanel();
		ui.addTopBarMenu(this);
		ui.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FitsFileChooser fileChooser = new FitsFileChooser();
        int returnVal = fileChooser.showOpenDialog((Component) e.getSource());

        if (returnVal == FitsFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //This is where a real application would open the file.
            System.out.println("Opening: " + file.getName() + ".");
            Fits fitsFile;
			try {
				fitsFile = new Fits(file);
				ui.getImageViewPanel().addImage(fitsFile);
			} catch (FitsException e2) {
				// TODO Notify user that the selected file is not a FITS file with image data
				e2.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } else {
            System.out.println("Open command cancelled by user.");
        }
		
	}
}
