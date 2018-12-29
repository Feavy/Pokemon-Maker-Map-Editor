package fr.feavy.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.jndi.url.corbaname.corbanameURLContextFactory;

public class Map {

	public static final int LEFT = 0;
	public static final int UP = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;
	
	private int width;
	private int height;
	private String name;
	private int id;
	
	private int[] sideMapsID;
	private int[][][] tiles;
	
	private HashMap<Coordonate, List<Warp>> warps = new HashMap<Coordonate, List<Warp>>();
	
	private int borderTileID;
	
	private boolean isSaved = true;
	
	public Map(int id, String name, int width, int height, int leftMapID, int topMapID, int rightMapID, int bottomMapID, int borderTileID){
		
		this.id = id;
		this.name = name;
		this.width = width;
		this.height = height;
		this.borderTileID = borderTileID;
		
		this.sideMapsID = new int[]{leftMapID, topMapID, rightMapID, bottomMapID};
		
	}
	
	public HashMap<Coordonate, List<Warp>> getWarps(){
		return warps;
	}
	
	public int getWarpCount(int x, int y){
		return (!warps.containsKey(new Coordonate(x, y)) ? 0 : warps.get(new Coordonate(x, y)).size());
	}
	
	public void addWarp(int x, int y, Warp w){
		List<Warp> l = warps.get(new Coordonate(x, y));
		if(l != null){
			l.add(w);
			warps.replace(new Coordonate(x, y), l);
		}else{
			l = new ArrayList<Warp>();
			l.add(w);
			warps.put(new Coordonate(x, y), l);
		}
	}
	
	public void removeWarp(Coordonate c){
		if(warps.get(c).size() >= 2)
			warps.get(c).remove(warps.get(c).size()-1);
		else
			warps.remove(c);
	}
	
	public void replaceWarp(int x, int y, Warp newWarp){
		removeWarp(new Coordonate(x, y));
		addWarp(x, y, newWarp);
	}
	
	public void moveWarp(Coordonate last, int newX, int newY){
		Warp l = warps.get(last).get(0);
		removeWarp(last);
		addWarp(newX, newY, l);
	}
	
	public boolean isWarpSet(Coordonate c){
		return warps.get(c) != null;
	}
	
	public Warp getWarp(int x, int y){
		if(warps.get(new Coordonate(x, y)) == null)
			return null;
		else
			return warps.get(new Coordonate(x, y)).get(0);
	}
	
	public void setBorderTile(int tileID){
		borderTileID = tileID;
		isSaved = false;
	}
	
	public int getBorderTileID(){
		return borderTileID;
	}
	
	public void setTiles(int[][][] tiles){
		this.tiles = tiles;
	}
	
	public int[][] getTiles(){
		return tiles[0];
	}
	
	public int[][] getMovements(){
		return tiles[1];
	}
	
	public int getID(){
		return id;
	}
	
	public int[] getSideMapsIDs(){
		return sideMapsID;
	}
	
	public String getName(){
		return name;
	}
	
	public int width(){
		return width;
	}
	
	public int height(){
		return height;
	}
	
	public void setTopMapID(int id){
		sideMapsID[UP] = id;
		isSaved = false;
	}
	
	public void setRightMapID(int id){
		sideMapsID[RIGHT] = id;
		isSaved = false;
	}
	
	public void setLeftMapID(int id){
		sideMapsID[LEFT] = id;
		isSaved = false;
	}
	
	public void setBottomMapID(int id){
		sideMapsID[DOWN] = id;
		isSaved = false;
	}
	
	public void setWidth(int width){
		this.width = width;
		isSaved = false;
	}
	
	public void setHeight(int height){
		this.height = height;
		isSaved = false;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public boolean isSaved(){
		return isSaved;
	}
	
	public void setSaved(boolean saved){
		isSaved = saved;
	}
	
}
