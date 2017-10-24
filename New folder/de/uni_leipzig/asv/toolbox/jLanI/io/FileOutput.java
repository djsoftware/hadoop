package de.uni_leipzig.asv.toolbox.jLanI.io;
import java.io.BufferedWriter;
import java.io.FileWriter;

import de.uni_leipzig.asv.toolbox.jLanI.kernel.LangResult;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class FileOutput implements Output
{
	private BufferedWriter out;
	private String fileName;
	
	public FileOutput(String fileName) throws Exception
	{
		this.fileName = fileName;
		out = new BufferedWriter(new FileWriter(fileName, true));
System.out.println(fileName);
		out.write("language1\tprobability1\tlanguage2\tprobability2\tsentences"+System.getProperty("line.separator")); 	
	}
	
	public void writeSent(LangResult res, String id, String sent) throws Exception
	{
		out.write(res.toString()+"\t"+sent+System.getProperty("line.separator")); 	
	}

	public void close()
	{
		try
		{
			out.close();
		}catch (Exception e){e.printStackTrace();}
	}
}
