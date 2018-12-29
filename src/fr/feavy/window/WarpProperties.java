package fr.feavy.window;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fr.feavy.java.Coordonate;
import fr.feavy.java.Map;
import fr.feavy.java.Warp;
import fr.feavy.java.utils.IntegerUtils;
import fr.feavy.java.utils.WindowUtils;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WarpProperties extends JFrame {

	private JPanel contentPane;
	private JTextField mapIDText;
	private JTextField xText;
	private JTextField yText;

	private JButton saveButton;
	
	private Warp w = null;
	
	private int x,y;
	
	public WarpProperties(Map m) {
		
		setTitle("Warp creation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 355, 230);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Move player to :");
		lblNewLabel.setBounds(10, 11, 128, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Map ID :");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setBounds(92, 36, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblX = new JLabel("X :");
		lblX.setHorizontalAlignment(SwingConstants.RIGHT);
		lblX.setBounds(92, 72, 46, 14);
		contentPane.add(lblX);
		
		JLabel lblY = new JLabel("Y :");
		lblY.setHorizontalAlignment(SwingConstants.RIGHT);
		lblY.setBounds(92, 97, 46, 14);
		contentPane.add(lblY);
		
		mapIDText = new JTextField();
		mapIDText.setBounds(148, 33, 86, 20);
		contentPane.add(mapIDText);
		mapIDText.setColumns(10);
		
		xText = new JTextField();
		xText.setColumns(10);
		xText.setBounds(170, 69, 46, 20);
		contentPane.add(xText);
		
		yText = new JTextField();
		yText.setColumns(10);
		yText.setBounds(170, 94, 46, 20);
		contentPane.add(yText);
		
		saveButton = new JButton("Save warp");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(IntegerUtils.isNumeric(mapIDText.getText()) && IntegerUtils.isNumeric(xText.getText()) && IntegerUtils.isNumeric(yText.getText()))
					save(Integer.parseInt(mapIDText.getText()), Integer.parseInt(xText.getText()), Integer.parseInt(yText.getText()));
				else
					WindowUtils.showError("The values you have entered are invalids.");
				
				
			}
		});
		saveButton.setBounds(115, 153, 109, 23);
		contentPane.add(saveButton);
		
		setLocationRelativeTo(null);
	}
	
	public void setWarp(Warp w, int x, int y){
		this.w = w;
		JButton deleteButton = new JButton("Delete");
		deleteButton.setBounds(216, 153, 89, 23);
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(WindowUtils.showYesNo("Do you really want to delete this warp ?")){
					Main.instance.getCurrentMap().removeWarp(new Coordonate(x, y));
					Main.instance.getPanel().repaint();
					dispose();
				}
			}
		});
		saveButton.setBounds(25, 153, 109, 23);
		contentPane.add(deleteButton);
		mapIDText.setText(w.getMapID()+"");
		xText.setText(w.getX()+"");
		yText.setText(w.getY()+"");
		setTitle("Warp editing");
		this.x = x;
		this.y = y;
	}
	
	public void save(int mapID, int x, int y){
		if(w == null)
			Main.instance.getCurrentMap().addWarp(5, 5, new Warp(mapID, x, y));
		else
			Main.instance.getCurrentMap().replaceWarp(this.x, this.y, new Warp(mapID, x, y));
		Main.instance.getPanel().repaint();
		dispose();
	}
}
