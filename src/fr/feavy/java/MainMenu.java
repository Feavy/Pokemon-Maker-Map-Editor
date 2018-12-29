package fr.feavy.java;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import fr.feavy.java.utils.WindowUtils;
import fr.feavy.window.Main;
import fr.feavy.window.MapPropertiesWindow;
import fr.feavy.window.chooser.MapChooserWindow;

public class MainMenu extends JMenuBar {

	public MainMenu(Main main, Project project) {

		JMenu menu = new JMenu("File");
		add(menu);

		addItem(menu, new AbstractAction("Load...") {

			public void actionPerformed(ActionEvent arg0) {
				File f = showFileBrower("open");
				try {
					if (f != null)
						project.load(f);
				} catch (Exception e) {
					JOptionPane.showConfirmDialog(null, "The file could not be loaded.\nIt is probably corrupted.",
							"Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		}, 'L');

		addItem(menu, new AbstractAction("Save as...") {

			public void actionPerformed(ActionEvent arg0) {
				File f = showFileBrower("save");
				if (f != null)
					project.save(f);
			}
		}, 'U');

		addItem(menu, new AbstractAction("Save") {

			public void actionPerformed(ActionEvent arg0) {
				saveProject(project);
			}
		}, 'S');

		menu = new JMenu("Map");
		add(menu);

		addItem(menu, new AbstractAction("New") {

			public void actionPerformed(ActionEvent arg0) {

				MapPropertiesWindow window = new MapPropertiesWindow(null, project);

				if (project.isSaved()) {
					window.setVisible(true);
					return;
				}

				int reply = JOptionPane.showConfirmDialog(null,
						"You have to save your project before modifying another map.\nDo you want to save your project ?",
						"Information", JOptionPane.YES_NO_OPTION);

				if (reply == JOptionPane.YES_OPTION) {
					saveProject(project);
					window.setVisible(true);
				} else if (reply == JOptionPane.NO_OPTION) {
					if (project.isLoaded())
						window.setVisible(true);
					else
						JOptionPane.showConfirmDialog(null,
								"You have to save the current map at least once before creating a new map.", "Error",
								JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
				} else
					return;
			}
		}, 'N');

		addItem(menu, new AbstractAction("List") {

			public void actionPerformed(ActionEvent arg0) {

				MapChooserWindow window = new MapChooserWindow(Main.instance);

				if (project.isSaved()) {
					window.setVisible(true);
					return;
				}

				if (WindowUtils.showYesNo("You have to save your project before modifying another map.\nDo you want to save your project ?")) {
					saveProject(project);
					window.setVisible(true);
				}else 
					if (project.isLoaded())
						window.setVisible(true);
					else
						WindowUtils.showError("You have to save your project at least once before creating a new map.");
			}
		}, 'M');
		
		menu = new JMenu("Window");
		add(menu);
		addItem(menu, new AbstractAction("Zoom in") {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				main.getPanel().zoomIn();
				
			}
		}, 'I');
		addItem(menu, new AbstractAction("Zoom out") {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				main.getPanel().zoomOut();
				
			}
		}, 'O');

	}

	public static void saveProject(Project project){
		if (project.isLoaded())
			project.save(null);
		else {
			File f = showFileBrower("save");
			if(f != null)
				project.save(f);
		}
	}
	
	private static File showFileBrower(String type) {

		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Game (.pkrg)", new String[] { "pkrg" });
		chooser.setFileFilter(filter);
		int response;
		File f;
		if (type.equalsIgnoreCase("save")) {
			response = chooser.showSaveDialog(null);
			f = chooser.getSelectedFile().getAbsolutePath().contains(".pkrg") ? chooser.getSelectedFile()
					: new File(chooser.getSelectedFile() + ".pkrg");
		} else {
			response = chooser.showOpenDialog(null);
			f = chooser.getSelectedFile();
		}
		if (response == JFileChooser.APPROVE_OPTION)
			return f;
		return null;

	}

	private void addItem(JMenu menu, AbstractAction e, char shortcut) {

		JMenuItem item = new JMenuItem(e);
		if (shortcut != 0x00)
			item.setAccelerator(KeyStroke.getKeyStroke(shortcut, Event.CTRL_MASK));
		menu.add(item);

	}

}
