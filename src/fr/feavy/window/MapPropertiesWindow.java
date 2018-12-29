package fr.feavy.window;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import fr.feavy.java.Map;
import fr.feavy.java.Project;
import fr.feavy.java.utils.IntegerUtils;
import fr.feavy.java.utils.WindowUtils;

public class MapPropertiesWindow extends JFrame {

	private JPanel contentPane;
	private Map c_map;
	private Project project;
	
	public MapPropertiesWindow(Map map, Project project) {
		
		this.project = project;
		Main.instance.setEnabled(false);
		
		if(map == null){
			c_map = new Map(project.getMapCount(), "Undefined", 100, 100, -1, -1, -1, -1, 0);
			c_map.setTiles(new int[][][]{{{0},{0}},{{0},{0}}});
		}else{
			c_map = map;
		}
		
		setResizable(false);
		setTitle("Map Properties");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Map name : ");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(95, 13, 93, 14);
		contentPane.add(lblNewLabel);

		JTextField textField = new JTextField(c_map.getName());
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setBounds(198, 11, 111, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Map ID : " + c_map.getID());
		lblNewLabel_1.setBounds(10, 386, 80, 14);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Map dimensions :");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_2.setBounds(164, 55, 116, 14);
		contentPane.add(lblNewLabel_2);

		JTextField textWidth = new JTextField(c_map.width() + "");
		textWidth.setHorizontalAlignment(SwingConstants.CENTER);
		textWidth.setBounds(117, 91, 86, 20);
		contentPane.add(textWidth);
		textWidth.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Width :");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_3.setBounds(61, 94, 46, 14);
		contentPane.add(lblNewLabel_3);

		JLabel lblHeight = new JLabel("Height :");
		lblHeight.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHeight.setBounds(249, 94, 46, 14);
		contentPane.add(lblHeight);

		JTextField textHeight = new JTextField(c_map.height() + "");
		textHeight.setHorizontalAlignment(SwingConstants.CENTER);
		textHeight.setColumns(10);
		textHeight.setBounds(305, 91, 86, 20);
		contentPane.add(textHeight);

		JTextField textTopMap = new JTextField();
		textTopMap.setHorizontalAlignment(SwingConstants.CENTER);
		textTopMap.setBounds(179, 156, 86, 20);
		contentPane.add(textTopMap);
		textTopMap.setColumns(10);

		JTextField textRightMap = new JTextField();
		textRightMap.setHorizontalAlignment(SwingConstants.CENTER);
		textRightMap.setColumns(10);
		textRightMap.setBounds(305, 226, 86, 20);
		contentPane.add(textRightMap);

		JTextField textBottomMap = new JTextField();
		textBottomMap.setHorizontalAlignment(SwingConstants.CENTER);
		textBottomMap.setColumns(10);
		textBottomMap.setBounds(179, 297, 86, 20);
		contentPane.add(textBottomMap);

		JTextField textLeftMap = new JTextField();
		textLeftMap.setHorizontalAlignment(SwingConstants.CENTER);
		textLeftMap.setColumns(10);
		textLeftMap.setBounds(61, 226, 86, 20);
		contentPane.add(textLeftMap);

		int[] sideMapsID = c_map.getSideMapsIDs();
		textTopMap.setText((sideMapsID[Map.UP] == -1) ? "?" : sideMapsID[Map.UP] + "");
		textRightMap.setText((sideMapsID[Map.RIGHT] == -1) ? "?" : sideMapsID[Map.RIGHT] + "");
		textBottomMap.setText((sideMapsID[Map.DOWN] == -1) ? "?" : sideMapsID[Map.DOWN] + "");
		textLeftMap.setText((sideMapsID[Map.LEFT] == -1) ? "?" : sideMapsID[Map.LEFT] + "");

		JLabel lblNewLabel_4 = new JLabel("Top map ID :");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setBounds(185, 142, 74, 14);
		contentPane.add(lblNewLabel_4);

		JLabel lblLeftMapId = new JLabel("Left map ID :");
		lblLeftMapId.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeftMapId.setBounds(67, 212, 74, 14);
		contentPane.add(lblLeftMapId);

		JLabel lblRightMapId = new JLabel("Right map ID :");
		lblRightMapId.setHorizontalAlignment(SwingConstants.CENTER);
		lblRightMapId.setBounds(311, 212, 74, 14);
		contentPane.add(lblRightMapId);

		JLabel lblBottomMapId = new JLabel("Bottom map ID :");
		lblBottomMapId.setHorizontalAlignment(SwingConstants.CENTER);
		lblBottomMapId.setBounds(177, 282, 90, 14);
		contentPane.add(lblBottomMapId);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(147, 352, 149, 29);
		btnSave.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				int topMapID = -1;
				int rightMapID = -1;
				int bottomMapID = -1;
				int leftMapID = -1;

				if (IntegerUtils.isNumeric(textTopMap.getText()))
					topMapID = Integer.parseInt(textTopMap.getText());
				if (IntegerUtils.isNumeric(textRightMap.getText()))
					rightMapID = Integer.parseInt(textRightMap.getText());
				if (IntegerUtils.isNumeric(textBottomMap.getText()))
					bottomMapID = Integer.parseInt(textBottomMap.getText());
				if (IntegerUtils.isNumeric(textLeftMap.getText()))
					leftMapID = Integer.parseInt(textLeftMap.getText());
				
				try{
					int width = Integer.parseInt(textWidth.getText());
					int height = Integer.parseInt(textHeight.getText());
					if(width > 0 && height >0)
						if(textField.getText().length() >= 1 && !textField.getText().contains(";"))
							save(c_map, textField.getText(), height, width, topMapID, rightMapID, bottomMapID, leftMapID);
						else
							WindowUtils.showError("Map name is invalid"+((textField.getText().contains(";")) ? " : ';' is an invalid character.": "."));
					else
						WindowUtils.showError("Height and width of the map can't be null.");
				}catch(Exception e){
					e.printStackTrace();
					WindowUtils.showError("Dimensions and ids of the sides map are not numeric.");
				}
			}
		});
		contentPane.add(btnSave);
		setLocationRelativeTo(Main.instance);
	}

	public void save(Map m, String mapName, int height, int width, int topMapID, int rightMapID, int bottomMapID, int leftMapID) {

		m.setName(mapName);
		m.setHeight(height);
		m.setWidth(width);
		
		if(topMapID >= 0)
			if(project.isMapSet(topMapID))
				m.setTopMapID(topMapID);	
		if(rightMapID >= 0)
			if(project.isMapSet(rightMapID))
				m.setRightMapID(rightMapID);
		if(bottomMapID >= 0)
			if(project.isMapSet(bottomMapID))
				m.setBottomMapID(bottomMapID);
		if(leftMapID >= 0)
			if(project.isMapSet(leftMapID))
				m.setLeftMapID(leftMapID);
				
		if(m.getTiles().length != m.width() || m.getTiles()[0].length != m.height()){
			int[][][] tiles = new int[2][m.width()][m.height()];
			
			int width1 = m.getTiles().length;
			int height1 = m.getTiles()[0].length;
			
			for(int i = 0; i < m.width(); i++)
				for(int j = 0; j < m.height(); j++){
					tiles[0][i][j] = (i >= width1 || j >= height1) ? 0 : m.getTiles()[i][j];
					tiles[1][i][j] = (i >= width1 || j >= height1) ? 0 : m.getMovements()[i][j];
				}
			m.setTiles(tiles);
		}
		
		Main.instance.changeMap(m);
		dispose();
		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		Main.instance.setEnabled(true);
		Main.instance.toFront();
	}
}
