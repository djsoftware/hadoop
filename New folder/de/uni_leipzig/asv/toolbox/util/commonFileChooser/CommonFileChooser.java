package de.uni_leipzig.asv.toolbox.util.commonFileChooser;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * This class is to define a quasi standardized JFileChooser, so the look-and-feel will be
 * always the same.
 * 
 * @author Daniel Zimmermann
 */
public class CommonFileChooser {

	private JFileChooser chooser;
	private final ExtensionFileFilter eff;
	
	private String currentDirectory;
	
	/**
	 * This constructor creates the JFileChooser and the Extension-Filter. 
	 * 
	 * @param extensions The extensions that have to be accepted by the program
	 * @param description The description for these extensions
	 */
	public CommonFileChooser( String[] extensions, String description )
	{
		chooser = new JFileChooser();
		eff = new ExtensionFileFilter();
		
		currentDirectory = ".";
		
		if ( extensions != null )
		{
			int l = extensions.length;
			for ( int i = 0; i < l; i++ )
			{	
				addExtension( extensions[i] );
			}
		}
		
		if ( description != null )
		{
			setDescription( description );
		}
		
		if ( extensions != null ) {
			chooser.setFileFilter( eff );
		}
	}
	
	/**
	 * Adds an extension to the extension-filter.
	 * 
	 * @param extension The extension to add 
	 */
	public void addExtension( String extension )
	{
		eff.addExtension( extension );
		
		chooser.setFileFilter( eff );
	}
	
	/**
	 * Deletes all extensions from the extension-filter.
	 */
	public void clearExtensions()
	{
		eff.clearExtensions();
				
		chooser.setFileFilter( chooser.getAcceptAllFileFilter() );
		//chooser.setFileFilter( eff );
	}
	
	/**
	 * Sets the description for the extensions.
	 * 
	 * @param description The description the user want so set up
	 */
	public void setDescription( String description )
	{
		eff.setDescription( description );
		
		chooser.setFileFilter( eff );
	}
	
	/**
	 * This method shows the JFileChooser for a parent JPanel and returns the path of the 
	 * selected file.
	 * 
	 * @param parent The parent JPanel
	 * @param label This parameter is to define the label of the dialog
	 * @return The name/path of the selected file
	 */
	public String showDialogAndReturnFilename( JPanel parent, String label )
	{
		String filename = new String();
		
		chooser.setCurrentDirectory( new File( currentDirectory ) );
		
		int result = chooser.showDialog( parent, label );
		
		if (result == JFileChooser.APPROVE_OPTION)
		{	
			//if selected file approved the filter, store its path to an attribute
			filename =  chooser.getSelectedFile().getPath();
			
			currentDirectory = chooser.getSelectedFile().getAbsolutePath();
			if (isMSWindowsPlatform())
			{
				currentDirectory = currentDirectory.substring( 0, currentDirectory.lastIndexOf("\\") );
			}
			else
			{
				currentDirectory = currentDirectory.substring( 0, currentDirectory.lastIndexOf("/") );
			}
			//System.out.println(currentDirectory);
		} else {
			return null;
		}
		
		return filename;
	}
	
	/**
	 * This method checks, whether the Operating System is Windows or not.
	 * 
	 * @return true, if the OS is Windows, else false
	 */
	private boolean isMSWindowsPlatform()
    {
    	String os = System.getProperty("os.name");
        if ( os != null && os.startsWith("Windows")) {
			return true;
		} else {
			return false;
		}
    }

}
