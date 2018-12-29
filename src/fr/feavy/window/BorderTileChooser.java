package fr.feavy.window;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import fr.feavy.java.utils.ImageUtils;

public class BorderTileChooser extends JPanel{

	private int tileID = 0;
	
	public BorderTileChooser(Main main){
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				tileID = main.getSelectedTileID();
				main.getCurrentMap().setBorderTile(tileID);
				repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void setBorderTile(int tileID){
		this.tileID = tileID;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(ImageUtils.tileIDtoImage(tileID), 0, 0, 32, 32, null);
	}
	
}
