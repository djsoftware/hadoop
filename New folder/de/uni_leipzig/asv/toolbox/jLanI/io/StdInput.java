package de.uni_leipzig.asv.toolbox.jLanI.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StdInput implements Input 
{
    private BufferedReader reader = null;

    public StdInput() 
	{
		reader = new BufferedReader( new InputStreamReader( System.in ) );
	}

    public String readSent() throws Exception
	{
	    return reader.readLine();
    }

	public String getId() {return "";}
	public int getMaxProgress() {return -1;}
	public int getIncProgress() {return -1;}
        public int getCurrentProgress() {return -1;}
 
}
