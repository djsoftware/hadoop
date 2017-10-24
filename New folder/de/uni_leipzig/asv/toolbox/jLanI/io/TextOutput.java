package de.uni_leipzig.asv.toolbox.jLanI.io;
import de.uni_leipzig.asv.toolbox.jLanI.gui.LaniTab;
import de.uni_leipzig.asv.toolbox.jLanI.kernel.LangResult;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class TextOutput implements Output
{
	public void writeSent(LangResult res, String id, String sent) throws Exception
	{
		LaniTab.outputPane.textoutputSP.addRow(new String[]{res.toTextOutputString(), sent});
	}

	public void close()
	{
	}
}