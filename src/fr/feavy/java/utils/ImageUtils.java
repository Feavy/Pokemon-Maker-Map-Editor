package fr.feavy.java.utils;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import fr.feavy.window.Main;

public class ImageUtils {

	private static HashMap<Integer, Image> loadedTilesImage = new HashMap<Integer, Image>();
	private static HashMap<Integer, Image> loadedMovementsImage = new HashMap<Integer, Image>();
	
	public static Image WARP_IMAGE;
	
	public static void initialize(Main main){
		
		for (int i = 0; i < 4; i++) {
			try {
				loadedMovementsImage.put(i, ImageIO.read(main.getClass().getClassLoader().getResource("fr/feavy/ressources/movements/" + i + ".png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for(int j = 0; j < 1444; j++){
			try{
				loadedTilesImage.put(j, ImageIO.read(main.getClass().getClassLoader().getResource("fr/feavy/ressources/tiles/"+ ((j == -1) ? "-1" : Integer.toHexString(j)) + ".png")));
			}catch(Exception e){
			}
		}
		try {
			WARP_IMAGE = ImageIO.read(main.getClass().getClassLoader().getResource("fr/feavy/ressources/events/warp.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	public static boolean exists(int tileID){
		return loadedTilesImage.get(tileID) != null;
	}
	
	public static Image tileIDtoImage(int tileID) {
		return loadedTilesImage.get(tileID);
	}

	public static Image movementIDtoImage(int i) {
		return loadedMovementsImage.get(i);
	}
	
}
