package fr.feavy.window.chooser;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;

import fr.feavy.java.Movement;
import fr.feavy.java.Tile;
import fr.feavy.java.utils.ImageUtils;
import fr.feavy.window.Main;

public class ChooserPanel extends JPanel implements Scrollable {

	private int width, height;
	
	private Tile selectedTile;
	private Movement selectedMovement;
	
	private HashMap<Integer, Tile> tiles = new HashMap<Integer, Tile>();
	
	public ChooserPanel(int width, int height) {

		this.height = height;
		this.width = width;
		
	}

	public void setTiles(){
		int k = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				if (ImageUtils.exists(k)) {
					try {
						final Tile label = new Tile(k);
						label.setBounds(16 * i, 16 * j, 16, 16);
						label.setImage(ImageUtils.tileIDtoImage(k));
						label.addMouseListener(new MouseListener() {

							public void mouseReleased(MouseEvent arg0) {
								// TODO Auto-generated method stub

							}

							public void mousePressed(MouseEvent arg0) {
								selectedTile.setSelected(false);
								selectedTile = label;
								label.setSelected(true);
								Main.instance.setSelectedTile(label.getID(), false);
							}

							public void mouseExited(MouseEvent arg0) {
								// TODO Auto-generated method stub

							}

							public void mouseEntered(MouseEvent arg0) {
								// TODO Auto-generated method stub

							}

							public void mouseClicked(MouseEvent arg0) {

							}
						});
						if (k == 0) {
							selectedTile = label;
							label.setSelected(true);
						}
						tiles.put(k, label);
						super.add(label);
					} catch (Exception ex) {
						System.out.println("Reading error, tileID = " + i);
					}
				}
				k++;
			}
		}
	}
	
	public void setMovements(){
		int k = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				if (ImageUtils.exists(k)) {
					try {
						final Movement mvt = new Movement(k);
						mvt.setBounds(32 * i, 32 * j, 32, 32);
						mvt.setImage(ImageUtils.movementIDtoImage(k));
						mvt.addMouseListener(new MouseListener() {

							public void mouseReleased(MouseEvent arg0) {
								// TODO Auto-generated method stub

							}

							public void mousePressed(MouseEvent arg0) {
								selectedMovement.setSelected(false);
								selectedMovement = mvt;
								mvt.setSelected(true);
								Main.instance.setSelectedMovementID(mvt.getID());
							}

							public void mouseExited(MouseEvent arg0) {
								// TODO Auto-generated method stub

							}

							public void mouseEntered(MouseEvent arg0) {
								// TODO Auto-generated method stub

							}

							public void mouseClicked(MouseEvent arg0) {

							}
						});
						if (k == 0) {
							selectedMovement = mvt;
							mvt.setSelected(true);
						}

						super.add(mvt);
					} catch (Exception ex) {
						System.out.println("Reading error, tileID = " + i);
					}
				}
				k++;
			}
		}
	}
	
	public void setSelectedMovement(int selectedMovement){
		this.selectedMovement.setSelected(false);
		Movement m = (Movement)getComponent(selectedMovement);
		m.setSelected(true);
		this.selectedMovement = m;
	}
	
	public void setSelectedTile(int selectedTile){
		this.selectedTile.setSelected(false);
		Tile t = tiles.get(selectedTile);
		t.setSelected(true);
		this.selectedTile = t;
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(50, 50);
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		// TODO Auto-generated method stub
		return 0;
	}

}
