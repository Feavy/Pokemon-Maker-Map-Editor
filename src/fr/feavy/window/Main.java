package fr.feavy.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import fr.feavy.java.MainMenu;
import fr.feavy.java.Map;
import fr.feavy.java.Project;
import fr.feavy.java.utils.ImageUtils;
import fr.feavy.window.chooser.ChooserPanel;

import java.awt.Checkbox;

public class Main extends JFrame {

	private JPanel contentPane;

	private int selectedTileID;
	private int selectedMovementID;

	private Map currentMap;
	private Project project;
	
	private JScrollPane scrollPane;
	private JLabel lblMapInfo;	
	private MapPanel mapPanel;
	private BorderTileChooser borderPanel;
	
	private ChooserPanel movementsPanel;
	private ChooserPanel tilesPanel;
	
	private int keyPressed = -1;
	
	JRadioButton[] movementButtons = new JRadioButton[4];
	
	private List<Integer[]> absolutePosition = new ArrayList<Integer[]>();
	
	public static Main instance;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		
		ImageUtils.initialize(this);
		
		instance = this;
		project = new Project(this);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		currentMap = new Map(0, "Undefined", 100, 100, -1, -1, -1, -1, 0);
		
		setTitle("Pokémon Map Creator Gen. 1");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1562, 940);

		MainMenu menuBar = new MainMenu(this, project);
		setJMenuBar(menuBar);

		contentPane = new JPanel(){
			@Override
			public Component add(Component comp) {
				absolutePosition.add(new Integer[]{comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight()});
				
				return super.add(comp);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblMapInfo = new JLabel();
		lblMapInfo.setBounds(10, 866, 591, 14);
		contentPane.add(lblMapInfo);
		
		int[][][] tiles = new int[2][currentMap.width()][currentMap.height()];

		for (int i = 0; i < currentMap.width(); i++)
			for (int j = 0; j < currentMap.height(); j++) {
				tiles[0][i][j] = 1;
				tiles[1][i][j] = 0;
			}

		
		currentMap.setTiles(tiles);
		
		setMapInformations(currentMap);

		ChooserPanel tilesPanel = new ChooserPanel(38,38);
		tilesPanel.setTiles();
		tilesPanel.setBackground(new Color(230, 238, 255));
		tilesPanel.setLayout(null);
		tilesPanel.setPreferredSize(new Dimension(608, 592));

		JScrollPane tilesScroll = new JScrollPane(tilesPanel);
		tilesScroll.setBounds(1200, 11, 320, 320);
		contentPane.add(tilesScroll);
		
		setSelectedTile(0, false);
		
		borderPanel = new BorderTileChooser(this);
		borderPanel.setBackground(Color.WHITE);
		borderPanel.setBounds(1165, 28, 32, 32);
		contentPane.add(borderPanel);
		
		scrollPane = new JScrollPane();
		scrollPane.setToolTipText("");
		scrollPane.setBounds(10, 11, getWidth()-410, 864);
		scrollPane.getHorizontalScrollBar().setBlockIncrement(32);
		scrollPane.getHorizontalScrollBar().setBlockIncrement(32);
		contentPane.add(scrollPane);
		
		mapPanel = updateMap();
		
		JButton propertiesButton = new JButton("Map properties");
		propertiesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				MapPropertiesWindow propertiesWindow = new MapPropertiesWindow(currentMap, project);
				propertiesWindow.setVisible(true);

			}
		});
		propertiesButton.setBounds(1277, 846, 168, 23);
		contentPane.add(propertiesButton);
		
		JLabel borderLabel = new JLabel("Border");
		borderLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		borderLabel.setBounds(1165, 11, 46, 14);
		contentPane.add(borderLabel);
		
		movementsPanel = new ChooserPanel(4,1);
		movementsPanel.setLayout(null);
		movementsPanel.setMovements();
		movementsPanel.setBackground(Color.WHITE);
		movementsPanel.setBounds(1200, 376, 320, 32);
		contentPane.add(movementsPanel);
		
		Checkbox eventsEditionChkbox = new Checkbox("Events Editing");
		
		Checkbox mvtEditingCheckBox = new Checkbox("Movement Editing");
		mvtEditingCheckBox.setBounds(1200, 348, 109, 22);
		mvtEditingCheckBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				mapPanel.setMovementsVisible(e.getStateChange() == ItemEvent.SELECTED);
				eventsEditionChkbox.setState(false);
				mapPanel.setEventsVisible(false);
			}
		});
		contentPane.add(mvtEditingCheckBox);
		
		JButton warpButton = new JButton("Add warp");
		warpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				new WarpProperties(currentMap).setVisible(true);
				
			}
		});
		warpButton.setBounds(1200, 487, 89, 23);
		contentPane.add(warpButton);
		
		JButton scriptButton = new JButton("Add script");
		scriptButton.setBounds(1431, 487, 89, 23);
		contentPane.add(scriptButton);
		
		JButton npcButton = new JButton("Add NPC");
		npcButton.setBounds(1317, 487, 89, 23);
		contentPane.add(npcButton);
		
		eventsEditionChkbox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				mvtEditingCheckBox.setState(false);
				mapPanel.setMovementsVisible(false);
				mapPanel.setEventsVisible(e.getStateChange() == ItemEvent.SELECTED);				
			}
		});
		eventsEditionChkbox.setBounds(1200, 447, 109, 22);
		contentPane.add(eventsEditionChkbox);
		
		setResizable(true);
		setLocationRelativeTo(null);

		System.out.println("End");

		contentPane.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				scrollPane.setBounds(10, 11, getWidth()-410, getHeight()-86);
				propertiesButton.setBounds(getWidth()-285, getHeight()-94, 168, 23);
				lblMapInfo.setBounds(10, getHeight()-74, 591, 14);
				
				Rectangle b;
				
				int i = 0;
				
				for(Component c : contentPane.getComponents()){
					if(c.equals(scrollPane) || c.equals(propertiesButton) || c.equals(lblMapInfo)){
						i++;
						continue;
					}
					b = c.getBounds();
					c.setBounds(getWidth()-(1562-absolutePosition.get(i)[0]), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight());
					i++;
				}
				
				System.out.println("width: "+getWidth());
				System.out.println("height: "+getHeight());
				revalidate();
				
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		contentPane.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				keyPressed = -1;
			}
			
			public void keyPressed(KeyEvent e) {
				System.out.println("keypressed: "+e.getKeyCode());
				keyPressed = e.getKeyCode();
			}
		});
		
	}
	
	public boolean isKeyPressed(int keyCode){
		return (keyCode == keyPressed);
	}
	
	public void dispose(){
		
		if(!currentMap.isSaved()){
			int reply = JOptionPane.showConfirmDialog(null, "You didn't save your project. Do you want to save it now ?", "Information", JOptionPane.YES_NO_OPTION);
			if(reply == JOptionPane.YES_OPTION)
				MainMenu.saveProject(project);	
		}
		
		System.exit(0);
	}
	
	public void onProjectLoaded(){
		
		try {
			currentMap = project.getMap(0);
			updateMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Project loaded");
		
	}
	
	public void updateCursorText(int x, int y){
		String[] text = lblMapInfo.getText().split("\\|");
		lblMapInfo.setText(text[0]+"|"+text[1]+"|"+text[2]+"|"+text[3]+"| Cursor: ("+x+", "+y+")");
	}
	
	private void setMapInformations(Map map){
		lblMapInfo.setText("Map name : "+map.getName()+" | Map ID : "+map.getID()+" | Width : "+map.width()+" | Height : "+map.height() + " | Cursor: (?, ?)");
	}
	
	public MapPanel getPanel(){
		return mapPanel;
	}
	
	private void setCurrentMap(Map m){
		currentMap = m;
	}
	
	public void changeMap(Map newMap){
		setCurrentMap(newMap);
		updateMap();
	}
	
	public MapPanel updateMap(){
		
		setMapInformations(currentMap);
		
		mapPanel = new MapPanel(currentMap, this);
		mapPanel.setPreferredSize(new Dimension(currentMap.width() * 32+32, currentMap.height() * 32+32));
		mapPanel.setLayout(null);
		mapPanel.setBackground(Color.white);

		borderPanel.setBorderTile(currentMap.getBorderTileID());
		
		project.setMapPanel(mapPanel);
		
		scrollPane.setViewportView(mapPanel);
		scrollPane.revalidate();
		scrollPane.repaint();

		
		return mapPanel;
		
	}
	
	public Project getProject(){
		return project;
	}
	
	public Map getCurrentMap() {
		return currentMap;
	}
	
	public void setSelectedTile(int tileID, boolean pickUp) {
		System.out.println("Tile chosen: "+tileID);
		selectedTileID = tileID;
		if(pickUp)
			tilesPanel.setSelectedTile(tileID);
	}

	public void setSelectedMovementID(int id){
		selectedMovementID = id;
		movementsPanel.setSelectedMovement(id);
	}
	
	public int getSelectedMovementID(){
		return selectedMovementID;
	}
	
	public int getSelectedTileID() {
		return selectedTileID;
	}
}
