package de.uni_leipzig.asv.toolbox.jLanI.gui;

import javax.swing.JTabbedPane;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class MainPane extends JTabbedPane
{
	public MainPane()
	{
	    init();
	}

	private void init()
	{
	    addTab("welcome", new WelcomeTab());
	    addTab("jLanI", new LaniTab());
	    addTab("add new language", NewLanguageTab.getInstance());
	    addTab("db connection", new DBTab());
	}
}
