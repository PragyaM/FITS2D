package controllers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import nom.tam.fits.FitsException;
import views.MainUI;

public class MainController {
	public static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static void main(String[] args) throws FitsException, IOException{
		MainUI ui = new MainUI();
	}
}
