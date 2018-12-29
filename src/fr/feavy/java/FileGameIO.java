package fr.feavy.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.zip.DeflaterOutputStream;

public class FileGameIO {

	private File project;
	private int scriptsIndex = -1;
	private int mapsIndex = -1;
	private int trainersIndex = -1;
	
	private int mapCount = 0;
	private int scriptCount = 0;
	private int trainerCount = 0;
	
	protected FileGameIO(File decompiledProject) throws Exception{
		
		String currentStep = "";
		
		this.project = decompiledProject;
		String line = null;
		int lineNb = 0;
		BufferedReader reader = new BufferedReader(new FileReader(decompiledProject));
		
		while((line = reader.readLine()) != null){
			
			if(line.equals("Maps:")){
				mapsIndex = lineNb+1;
				currentStep = "maps";
			}else if(line.equals("Scripts:")){
				scriptsIndex = lineNb+1;
				currentStep = "scripts";
			}else if(line.equals("Trainers:")){
				trainersIndex = lineNb+1;
				currentStep = "trainers";
			}else{
				if(currentStep.equals("maps"))
					mapCount++;
				else if(currentStep.equals("scripts"))
					scriptCount++;
				else if(currentStep.equals("trainers"))
					trainerCount++;
			}
			lineNb++;
		}
		
		reader.close();
		
	}
	
	protected int getMapCount(){
		return mapCount;
	}
	
	private String readLine(int line) throws IOException{
		
		String response = "";
		int lineNb = 0;
		BufferedReader reader = new BufferedReader(new FileReader(project));
		
		while(lineNb < line){
			reader.readLine();
			lineNb++;
		}
		
		response = reader.readLine();
		reader.close();
		return response;
		
	}
	
	private void setLine(int line, String data, boolean add) throws Exception{
		
		int lineNb = 0;
		String str = "";
		
		BufferedReader reader = new BufferedReader(new FileReader(project));
		StringBuilder builder = new StringBuilder();
		
		while((str = reader.readLine()) != null){
			if(lineNb == line){
				builder.append(data+"\n");
				if(add)
					builder.append(str+"\n");
			}else
				builder.append(str+"\n");
			lineNb++;
		}
		
		if(lineNb < line)
			builder.append(data+"\n");
		
		reader.close();
		
		PrintWriter writer = new PrintWriter(project);
		
		writer.write(builder.toString());
		writer.flush();
		writer.close();
		
	}
	
	protected void setMap(int id, Map map) throws Exception{
		
		StringBuilder builder = new StringBuilder();

		int[] sideMapsID = map.getSideMapsIDs();
		
		//SET MAP PROPERTIES
		
		builder.append(map.getName() + ";" + map.width() + ";" + map.height() + ";" + String.format("%X", map.getBorderTileID()) + ";"+ sideMapsID[0] + ";" + sideMapsID[1]+ ";" + sideMapsID[2]+ ";" + sideMapsID[3] + ";");

		int[][] tiles = map.getTiles();
		int[][] movements = map.getMovements();

		//SET MAP TILES
		
		for (int i = 0; i < map.width(); i++)
			for (int j = 0; j < map.height(); j++)
				builder.append((String.format("%03X", tiles[i][j]) + "" + movements[i][j]));
		
		builder.append(";");
		
		HashMap<Coordonate, List<Warp>> warps = map.getWarps();
		
		//SET MAP WARPS
		
		if(warps.size() > 0){
		
			for(Coordonate c : warps.keySet()){
				for(Warp w : warps.get(c))
					builder.append(c.getX()+","+c.getY()+","+w.getMapID()+","+w.getX()+","+w.getY()+":");
			}
			
			builder.delete(builder.length()-1, builder.length()-1);
		
		}else{
			builder.append(" ");
		}
		
		System.out.println("--= Setmap =--");
		
		System.out.println("id: "+id);
		System.out.println("mapCount: "+mapCount);
		
		setLine(mapsIndex+id, builder.toString(), (id >= mapCount));
		if(id == mapCount)
			mapCount++;
	}
	
	protected Map getMap(int id) throws IOException{
		
		String stringMap = readLine(mapsIndex+id);
		
		System.out.println(stringMap);

		if(stringMap.length() < 5)
			return null;
		
		//GET MAP PROPERTIES
		
		String[] mapData = stringMap.split(";");
		int width = Integer.parseInt(mapData[1]);
		int height = Integer.parseInt(mapData[2]);
		Map m = new Map(id, mapData[0], width, height, Integer.parseInt(mapData[4]), Integer.parseInt(mapData[5]), Integer.parseInt(mapData[6]), Integer.parseInt(mapData[7]), Integer.parseInt(mapData[3], 16));

		//GET MAP TILES
		
		String cases = mapData[8];

		int[][][] tiles = new int[2][width][height];

		int k = 0;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[0][i][j] = Integer.parseInt(cases.substring(4 * k, 4 * k + 3), 16);
				tiles[1][i][j] = Integer.parseInt(cases.substring(4 * k + 3, 4 * k + 4));
				k++;
			}
		}

		m.setTiles(tiles);
		
		//GET MAP WARPS
		
		HashMap<Coordonate, List<Warp>> warps = new HashMap<Coordonate, List<Warp>>();
		String warpsStr = mapData[9];
		
		if(warpsStr.length() >= 5){
			
			String[] warp = warpsStr.split(":");
			
			for(String w : warp){
				String[] d = w.split(",");
				m.addWarp(Integer.parseInt(d[0]), Integer.parseInt(d[1]), new Warp(Integer.parseInt(d[2]), Integer.parseInt(d[3]), Integer.parseInt(d[4])));
			}
			
		}
		
		return m;
		
	}
	
	protected void save(File f) throws IOException{
		
		BufferedReader reader = new BufferedReader(new FileReader(project));
		
		StringBuilder builder = new StringBuilder();
		
		String str = "";
		
		System.out.println("Saving...");
		
		while((str = reader.readLine()) != null){
			System.out.println(str);
			builder.append(str+"\n");
		}
		
		reader.close();
		
		DeflaterOutputStream dos = new DeflaterOutputStream(new FileOutputStream(f));
		dos.write(builder.toString().getBytes());
		dos.flush();
		dos.close();
			
	}
	
}
