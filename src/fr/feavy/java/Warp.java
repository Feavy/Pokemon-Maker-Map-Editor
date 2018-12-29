package fr.feavy.java;

public class Warp {

	private int mapID, x, y;
	
	public Warp(int mapID, int x, int y){
		this.mapID = mapID;
		this.x = x;
		this.y = y;
	}
	
	public int getMapID(){
		return mapID;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
}
