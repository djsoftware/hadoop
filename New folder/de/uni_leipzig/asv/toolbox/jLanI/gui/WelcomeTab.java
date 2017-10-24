package de.uni_leipzig.asv.toolbox.jLanI.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class WelcomeTab extends JPanel
{
    private String welcomemsg = "<html><center><br><br><font size = +1>JLanI</font><br><br>languageidentifier written in Java<br>" +
    		"<br>by Sven Teresniak & Alexandr Uciteli" +
    		"</center></html>";
    
    
	public WelcomeTab()
	{
	    init();
	}

	private void init()
	{
	    add(new JLabel(welcomemsg));
	}
}
