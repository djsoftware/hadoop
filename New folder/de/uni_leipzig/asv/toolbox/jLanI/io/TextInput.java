package de.uni_leipzig.asv.toolbox.jLanI.io;

import java.io.BufferedReader;
import java.io.StringReader;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class TextInput implements Input
{
	private BufferedReader in;
	private String text;
	private int id = 0;
	private int sentLength;
        private int curProg=0;

	public TextInput(String text) throws Exception
	{
		this.text = text;
		in = new BufferedReader(new StringReader(text));
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

	public int getMaxProgress(){ return (text.length()); }

	public int getIncProgress(){ return sentLength+2; }
        
        public int getCurrentProgress() {
            curProg+=getIncProgress();
            return curProg;
        }
}
