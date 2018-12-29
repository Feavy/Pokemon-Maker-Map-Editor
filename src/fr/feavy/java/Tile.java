package fr.feavy.java;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class Tile extends JPanel{

	private Image image;
	
	private int tileID;
	
	private boolean selected = false;
	
	public Tile(final int tileID){
		this.tileID = tileID;
		setBackground(Color.white);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		if(selected){
			g.setColor(Color.RED);
			g.drawLine(0, 0, getWidth(), 0);
			g.drawLine(0, 0, 0, getHeight());
			g.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());
			g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
		}

	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
		repaint();
	}
	
	public int getID(){
		return tileID;
	}
	
	public void setImage(Image i){
		this.image = i;
		repaint();
	}
	
	public Image getImage(){
		return image;
	}
	
}
