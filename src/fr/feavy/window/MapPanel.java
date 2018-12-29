package fr.feavy.window;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;

import com.sun.glass.events.KeyEvent;

import fr.feavy.java.Coordonate;
import fr.feavy.java.MainMenu;
import fr.feavy.java.Map;
import fr.feavy.java.Project;
import fr.feavy.java.Warp;
import fr.feavy.java.utils.ImageUtils;
import fr.feavy.java.utils.WindowUtils;
import javafx.scene.input.KeyCode;

public class MapPanel extends JPanel implements Scrollable {

	private boolean viewMovements, viewEvents;
	private JViewport parrent;

	private Map map;

	private Coordonate warpCoos;
	
	private int xChange, yChange;
	
	private int sideWidth = 32;
	
	public MapPanel(Map map, Main main) {

		this.map = map;
		
		int width = map.width();
		int height = map.height();

		int[][] tiles = map.getTiles();
		int[][] movements = map.getMovements();

		addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {

			}

			public void mousePressed(MouseEvent e) {

				int x = (int) parrent.getViewRect().getMinX();
				int y = (int) parrent.getViewRect().getMinY();

				while (x % sideWidth != 0)
					x++;
				while (y % sideWidth != 0)
					y++;

				parrent.setViewPosition(new Point(x, y));

				if (e.getButton() == MouseEvent.BUTTON1) {
					Main.instance.getCurrentMap().setSaved(false);
					if (viewMovements)
						movements[e.getX() / sideWidth + xChange][e.getY() / sideWidth + yChange] = main.getSelectedMovementID();
					else if(viewEvents){
						if(main.isKeyPressed(KeyEvent.VK_CONTROL) && map.isWarpSet(new Coordonate(e.getX()/sideWidth, e.getY()/sideWidth))){
							try{
							Warp w = map.getWarp(e.getX()/sideWidth, e.getY()/sideWidth);
							if (main.getProject().isSaved()) {
								main.changeMap(main.getProject().getMap(w.getMapID()));
								return;
							}

							if (WindowUtils.showYesNo("You have to save your project before modifying another map.\nDo you want to save your project ?")) {
								MainMenu.saveProject(main.getProject());
							}else 
								if (main.getProject().isLoaded())
									main.changeMap(main.getProject().getMap(w.getMapID()));
								else
									WindowUtils.showError("You have to save your project at least once before creating a new map.");
							}catch(Exception ex){
								ex.printStackTrace();
							}
						}
						warpCoos = new Coordonate(e.getX()/sideWidth, e.getY()/sideWidth);
					}else
						tiles[e.getX() / sideWidth + xChange][e.getY() / sideWidth + yChange] = main.getSelectedTileID();
				} else if (viewMovements)
					main.setSelectedMovementID(movements[e.getX() / sideWidth + xChange][e.getY() / sideWidth + yChange]);
				else if (viewEvents){
					if(map.getWarpCount(e.getX()/sideWidth, e.getY()/sideWidth) > 1){
						WindowUtils.showError("Error, there are differents warps at the same place.");
						return;
					}
					Warp w = map.getWarp(e.getX()/sideWidth, e.getY()/sideWidth);
					if(w == null) return;
					WarpProperties wp = new WarpProperties(map);
					wp.setWarp(w, e.getX()/sideWidth, e.getY()/sideWidth);
					wp.setVisible(true);
				}else
					main.setSelectedTile(tiles[e.getX() / sideWidth + xChange][e.getY() / sideWidth + yChange], true);
				repaint();
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

		addMouseMotionListener(new MouseMotionListener() {

			public void mouseMoved(MouseEvent e) {

				Main.instance.updateCursorText(e.getX()/sideWidth, e.getY()/sideWidth);
				
			}

			public void mouseDragged(MouseEvent e) {
				if (e.getX() / sideWidth + xChange >= 0 && e.getY() / sideWidth + yChange >= 0 && e.getX() / sideWidth + xChange < width && e.getY() / sideWidth + yChange < height)
					if (viewMovements)
						movements[e.getX() / sideWidth + xChange][e.getY() / sideWidth + yChange] = main.getSelectedMovementID();
					else if (viewEvents){
						if(!map.isWarpSet(warpCoos))return;
						map.moveWarp(warpCoos, e.getX()/sideWidth, e.getY()/sideWidth);
						warpCoos = new Coordonate(e.getX()/sideWidth, e.getY()/sideWidth);
					}
					else 
						tiles[e.getX() / sideWidth + xChange][e.getY() / sideWidth + yChange] = main.getSelectedTileID();
				repaint();

			}
		});

	}
	public void setMovementsVisible(boolean visible) {
		viewMovements = visible;
		repaint();
	}

	public void setEventsVisible(boolean visible){
		viewEvents = visible;
		repaint();
	}
	
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return sideWidth;
	}

	public boolean getScrollableTracksViewportHeight() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return sideWidth;
	}

	public void zoomIn(){
		sideWidth*=2;
		repaint();
	}
	
	public void zoomOut(){
		sideWidth/=2;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		double startX = 0;
		double endX = 0;
		double startY = 0;
		double endY = 0;

		parrent = (JViewport) getParent();
		if (parrent != null) {
			Rectangle viewRect = parrent.getViewRect();
			startX = viewRect.getMinX();
			endX = viewRect.getMaxX();
			startY = viewRect.getMinY();
			endY = viewRect.getMaxY();
			
			if(getPreferredSize().getHeight() == endY)
				yChange = -1;
			else
				yChange = 0;
			if(getPreferredSize().getWidth() == endX)
				xChange = -1;
			else
				xChange = 0;
		}
		
		if (endX > map.width())
			endX = map.width() * sideWidth;
		if (endY > map.height())
			endY = map.height() * sideWidth;

		Graphics2D g2d = null;

		if (viewMovements) {
			g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		}

		for (int i = (int) startX; i < endX; i += sideWidth)
			for (int j = (int) startY; j < endY; j += sideWidth) {
				g.drawImage(ImageUtils.tileIDtoImage(map.getTiles()[i / sideWidth][j / sideWidth]), i, j, sideWidth, sideWidth, null);
				if (viewMovements)
					g.drawImage(ImageUtils.movementIDtoImage(map.getMovements()[i / sideWidth][j / sideWidth]), i, j, sideWidth, sideWidth, null);
				else if(viewEvents)
					if(map.getWarp(i/sideWidth, j/sideWidth) != null)
						g.drawImage(ImageUtils.WARP_IMAGE, i, j, sideWidth, sideWidth, null);
			}

	}

}
