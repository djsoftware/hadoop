package de.uni_leipzig.asv.toolbox.jLanI.io;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public interface Input 
{
	public String readSent() throws Exception;
	public String getId();
	public int getMaxProgress();
	public int getIncProgress();
        public int getCurrentProgress(); 
}
