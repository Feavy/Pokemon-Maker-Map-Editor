package fr.feavy.java.utils;

import javax.swing.JOptionPane;

public class WindowUtils {

	public static void showError(String message){
		JOptionPane.showConfirmDialog(null, message, "Error", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
	}
	
	public static boolean showYesNo(String message){
		return (JOptionPane.showConfirmDialog(null, message, "Information", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
	}
	
}
