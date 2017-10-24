package de.uni_leipzig.asv.toolbox.jLanI.io;
import de.uni_leipzig.asv.toolbox.jLanI.kernel.LangResult;

public class StdOutput implements Output 
{
	public void writeSent(LangResult res, String id, String sent) throws Exception
	{
        System.out.println(res+"\t"+sent);
    }

    public void close() {}
}
