package fr.feavy.window.chooser;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.feavy.java.Map;
import fr.feavy.java.Project;
import fr.feavy.java.utils.ImageUtils;
import fr.feavy.window.Main;
import sun.net.www.content.image.jpeg;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import java.awt.Font;
import javax.swing.SwingConstants;

public class MapChooserWindow extends JFrame {

	private JPanel contentPane;
	boolean change = true;
	
	private MapPreview panel;
	
	private Main main;

	public MapChooserWindow(Main main) {
		
		this.main = main;
		
		main.setEnabled(false);
		Project project = main.getProject();
		
		setResizable(false);
		setTitle("Choose a map");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 689, 483);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ListModel<String> model = new DefaultListModel<String>();
		
		JLabel lblDims = new JLabel("Width : ? | Height: ?");
		lblDims.setHorizontalAlignment(SwingConstants.CENTER);
		lblDims.setBounds(384, 370, 158, 14);
		contentPane.add(lblDims);
		
		JList list = new JList(model);
		list.setLocation(249, 0);
		
		String[] names;
		try {
			names = project.getMapsName();
			for(int i = 0; i < project.getMapCount(); i++){
				if(names[i].length() >= 1)
					((DefaultListModel)list.getModel()).addElement(i+" : "+names[i]);
			}
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(change){
					int id = Integer.parseInt(list.getSelectedValue().toString().split(":")[0].trim());
					try {
						Map m = project.getMap(id);
						panel.setSelectedMap(m);
						panel.repaint();
						lblDims.setText("Width : "+m.width()+" | Height : "+m.height());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				change = !change;
				
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(new Rectangle(10, 34, 219, 350));
		
		contentPane.add(scrollPane);
		
		JLabel lblNewLabel = new JLabel("Select a map :");
		lblNewLabel.setBounds(10, 9, 115, 14);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Load");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(panel.getSelectedMap() != null){
					main.changeMap(panel.getSelectedMap());
					dispose();
				}
				
			}
		});
		btnNewButton.setBounds(273, 420, 89, 23);
		contentPane.add(btnNewButton);
		
		panel = new MapPreview();
		panel.setBounds(296, 34, 334, 334);
		contentPane.add(panel);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnDelete.setBounds(573, 420, 89, 23);
		contentPane.add(btnDelete);
		
		setLocationRelativeTo(main);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		main.setEnabled(true);
		main.toFront();
	}
	
	class MapPreview extends JPanel{
		
		private Map m;
		
		private int stepX;
		private int stepY;
		
		protected Map getSelectedMap(){
			return m;
		}
		
		protected void setSelectedMap(Map m){
			
			double maxY = (double)m.height()/(double)m.width()*334;
			double maxX = (double)m.width()/(double)m.height()*334;
			
			stepX = (int) maxX/m.width();
			stepY = (int) maxY/m.height();
			this.m = m;

		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if(m != null){
				
				int width = m.width();
				int height = m.height();
				
				for(int i  = 0; i < width; i++)
					for(int j = 0; j < height; j++){
						g.drawImage(ImageUtils.tileIDtoImage(m.getTiles()[i][j]), stepX*i, stepY*j, stepX, stepY, null);

					}
				}
			
		}
		
	}
}
