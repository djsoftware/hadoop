package de.uni_leipzig.asv.toolbox.util.commonFileChooser;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author Daniel Zimmermann & Torsten Thalheim
 *
 * This class organizes all possible file extensions.
 * -> taken from the book "CoreJava Band 1" 
 */
public class ExtensionFileFilter extends FileFilter
{
	private String description = "";
	private ArrayList extensions = new ArrayList();
	
	/**
	 * adds an extension to search for, when you start a open or save dialog  
	 * 
	 * @param ext the extension you want to search for
	 */
	public void addExtension(String ext)
	{
		if (!ext.startsWith(".")) {
			ext = "." + ext;
		}
		extensions.add(ext.toLowerCase());			//an die Liste der möglichen Erweiterungen anhängen
	}
	
	/**
	 * removes all extensions and prepare to create new
	 */
	public void clearExtensions()
	{
		extensions = null;
		extensions = new ArrayList();
	}
	
	/**
	 * The description for your preselected file extensions.
	 * 
	 * @param desc the description
	 */
	public void setDescription(String desc)
	{
		description = desc;
	}
	
	/**
	 * Returns the actual description for your preselected file extensions.
	 * 
	 * @return the description
	 */
	@Override
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Checks if the file you have entered is valid to your file extensions
	 * 
	 * @param f the file to check
	 * @return true, if the file is valid
	 */
	@Override
	public boolean accept(File f)
	{
		if (f.isDirectory()) {
			return true;
		}
		
		String name = f.getName().toLowerCase();
		
		for (int i=0; i<extensions.size(); i++)
		{
			if (name.endsWith( (String) extensions.get(i) )) {
				return true;
			}
		}
		
		return false; //tritt ein, wenn die Datei mit keiner der Endungen aus der Liste übereinstimmt
	}
}
