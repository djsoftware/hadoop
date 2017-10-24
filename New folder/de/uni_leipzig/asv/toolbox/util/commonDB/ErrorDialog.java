package de.uni_leipzig.asv.toolbox.util.commonDB;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * This class organizes the Errors and displayes them on screen.
 *  
 * @author Daniel Zimmermann, Torsten Thalheim
 */
public class ErrorDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JFrame parent;
	
	/**
	 * constructor with parent Frame and one line
	 * 
	 * @param parent The parent frame, needed for the ErrorDialog
	 * @param string The error message
	 * */
	public ErrorDialog(JFrame parent,String string) {
		super(parent, "Error", true);
		this.parent=parent;
		getContentPane().add(new JLabel(string));
	}
	
	/**
	 * constructor with parent Frame and two lines
	 * 
	 * @param parent The parent frame, needed for the ErrorDialog
	 * @param string1 The error messages first line
	 * @param string2 The error messages second line
	 * */
	public ErrorDialog(JFrame parent,String string1,String string2) {
		super(parent, "Fehler", true);
		this.parent=parent;
		getContentPane().add(BorderLayout.NORTH,new JLabel(string1));
		getContentPane().add(BorderLayout.SOUTH,new JLabel(string2));
	}

	/**
	 * constructor with two lines
	 * 
	 * @param string1 The error messages first line
	 * @param string2 The error messages second line
	 * */
	public ErrorDialog(String string1,String string2) {
		//super(getContentPane(), "Fehler", true);
		getContentPane().add(BorderLayout.NORTH,new JLabel(string1));
		getContentPane().add(BorderLayout.SOUTH,new JLabel(string2));
	}
	
	/**
	 * This method shows the error dialog
	 * 
	 * @param ed the error dialog to show
	 */
	public void show(ErrorDialog ed) {
		ed.setLocationRelativeTo(parent);
		ed.pack();
		ed.setVisible(true);
	}
	
	/**
	 * another method to show the error dialog using another parent panel
	 * 
	 * @param ed the error dialog to show
	 * @param panel the calling panel	
	 * */
	public void show(ErrorDialog ed, JFrame panel) {
		ed.setLocationRelativeTo(panel);
		ed.pack();
		ed.setVisible(true);
	}
}