package de.uni_leipzig.asv.toolbox.jLanI.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class FileInput implements Input
{
	private BufferedReader in;
	private File file;
	private String fileName;
	private int id = 0;
        private int curProg=0;
	private int sentLength;

	public FileInput(String fileName) throws FileNotFoundException
	{
		this.fileName = fileName;
		file = new File(fileName); 
		in = new BufferedReader(new FileReader(file));
	}
	
	public String readSent() throws Exception
	{
		String sent = in.readLine();	
		id++;
		if(sent == null) {
			in.close();
		} else {
			sentLength = sent.length();
		}
		return sent;
	}

	public String getId(){ return ""+id; }
	
	public int getMaxProgress(){ return (int)(file.length()); }

	public int getIncProgress(){ return sentLength+2; }
        
        public int getCurrentProgress() {
            curProg+=getIncProgress();
            return curProg;
        }
}
