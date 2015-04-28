package controllers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import nom.tam.fits.FitsException;
import views.MainWindow;

public class Main {
	
	public static void main(String[] args) throws FitsException, IOException{
		GUIController guiController = new GUIController();
		guiController.start();
	}
}
