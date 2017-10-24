package de.uni_leipzig.asv.toolbox.jLanI.main;

import java.util.HashSet;
import java.util.Set;

import de.uni_leipzig.asv.toolbox.jLanI.io.FileInput;
import de.uni_leipzig.asv.toolbox.jLanI.io.FileOutput;
import de.uni_leipzig.asv.toolbox.jLanI.io.Input;
import de.uni_leipzig.asv.toolbox.jLanI.io.Output;
import de.uni_leipzig.asv.toolbox.jLanI.io.StdInput;
import de.uni_leipzig.asv.toolbox.jLanI.io.StdOutput;
import de.uni_leipzig.asv.toolbox.jLanI.kernel.LanIKernel;
import de.uni_leipzig.asv.toolbox.jLanI.kernel.Request;
import de.uni_leipzig.asv.toolbox.jLanI.kernel.Response;
import de.uni_leipzig.asv.toolbox.jLanI.kernel.LanIKernelInterface;
import de.uni_leipzig.asv.toolbox.jLanI.tools.Log;
/**
 * 
 */
public class CLIMain 
{
	private static Log log = Log.getInstance();

    public static void main( String args[] ) 
	{
        if ( args.length < 2 ) {System.out.println( "usage: CLIMain <inFile> <outFile>" ); return;}

		Set languages = new HashSet();
        if ( args.length > 2 ) {
			for (int i=2; i<args.length; i++) {
				languages.add(args[i]);
			}
		}

        start(getInput(args[0]),getOutput(args[1]), languages);
    }
	
	private static Input getInput(String inFile)
	{
		Input input = null;
		try 
		{
			if(inFile.equals("-")) {
				input = new StdInput();
			} else {
				input = new FileInput(inFile);
			}
		} catch (Exception e){e.printStackTrace();}
		return input;
	}

	private static Output getOutput(String outFile)
	{
		Output output = null;
		try 
		{
			if(outFile.equals("-")) {
				output = new StdOutput();
			} else {
				output = new FileOutput(outFile);
			}
		} catch (Exception e){e.printStackTrace();}
		return output;
	}

	private static void start(Input in, Output out, Set languages)
	{
		String sent;

		try 
		{
			log.log("StandaloneClientCLI: loading kernel!");
			LanIKernelInterface kern = LanIKernel.getInstance();
			log.log("StandaloneClientCLI: loading kernel done!");
			while ((sent = in.readSent()) != null) 
			{
				Request req = new Request();
				req.setLanguages(languages);
				req.setSentence(sent);
				Response res = kern.evaluate(req);
				
				out.writeSent(res.getLangResult(0.15, 2),"",sent);
			}
		} catch (Exception e){e.printStackTrace();} finally{out.close();}
		System.out.println("Finished");
	}	
}