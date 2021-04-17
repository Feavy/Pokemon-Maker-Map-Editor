package fr.feavy.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.InflaterInputStream;

import fr.feavy.window.Main;
import fr.feavy.window.MapPanel;

public class Project {

	private HashMap<Integer, Map> savedMaps = new HashMap<Integer, Map>();
	private MapPanel panel;
	private FileGameIO fgIO;

	private boolean loaded = false;

	private Main main;
	private File projectFile;

	public Project(Main main) {

		this.main = main;

	}

	public void setMapPanel(MapPanel panel) {
		this.panel = panel;
	}

	public void load(File f) throws Exception {

		this.projectFile = f;

		fgIO = getFileGameIO(f);
		
		loaded = true;
		main.onProjectLoaded();

	}

	private FileGameIO getFileGameIO(File f) throws Exception{

		StringBuilder builder = new StringBuilder();

		FileInputStream fis = new FileInputStream(f);
		InflaterInputStream iis = new InflaterInputStream(fis);

		byte[] b = new byte[2048];
		int length = 0;

		while ((length = iis.read(b)) >= 0)
			builder.append(new String(b, 0, length));

		iis.close();

		return new FileGameIO(builder.toString());
		
	}
	
	private FileGameIO createFileGameIO() throws Exception{
		
		File tempFile = new File(getClass().getResource("/fr/feavy/ressources/decompiledGame.pkrg").getFile());
		
		StringBuilder data = new StringBuilder();
		data.append("Maps:");
		data.append(System.getProperty("line.separator"));
		data.append("Scrips:");
		data.append(System.getProperty("line.separator"));
		data.append("Trainers:");
		data.append(System.getProperty("line.separator"));
		
		return new FileGameIO(data.toString());
		
	}
	
	public int getMapCount() {
		return (loaded) ? fgIO.getMapCount() : 1;
	}

	public String[] getMapsName() throws Exception{
		String[] mapName = new String[fgIO.getMapCount()];
		for(int i = 0; i < fgIO.getMapCount(); i++){
			mapName[i] = fgIO.getMap(i).getName();
		}
		return mapName;
	}
	
	public boolean isMapSet(int id){
		if(id < fgIO.getMapCount()){
			try {
				return (getMap(id) != null);
			} catch (Exception e) {
				return false;
			}
		}else{
			return false;
		}
	}
	
	public Map getMap(int id) throws Exception {
		return fgIO.getMap(id);
	}

	public void saveMap(Map map) throws Exception {
		System.out.println("SaveMap: "+map.getID());
		fgIO.setMap(map.getID(), map);
		main.getCurrentMap().setSaved(true);
	}
	
	public void deleteMap(int id) {

	}
	
	public void save(File f){
		
		try{
		
		if(f != null){
			if(f != projectFile)
				loaded = false;
			else if(!f.exists())
				f.createNewFile();
		}else{
			f = projectFile;
		}
		
		if(!loaded){
			fgIO = createFileGameIO();
			projectFile = f;
			loaded = true;
		}
		
		System.out.println("width: "+main.getCurrentMap().width());
		System.out.println("height: "+main.getCurrentMap().height());
		
		saveMap(main.getCurrentMap());
			
		fgIO.save(f);
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public boolean isSaved(){
		return main.getCurrentMap().isSaved();
	}
	
	public boolean isLoaded() {

		return loaded;

	}

}
