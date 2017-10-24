package de.uni_leipzig.asv.toolbox.jLanI.io;
import de.uni_leipzig.asv.toolbox.jLanI.kernel.LangResult;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public interface Output 
{
	public void writeSent(LangResult res, String id, String sent) throws Exception;
	public void close();
}
