/*
 * $Header: /usr/cvs/jlani2/src/de/uni_leipzig/asv/toolbox/jLanI/kernel/LanIKernelInterface.java,v 1.1 2009-04-07 10:42:25 steresniak Exp $
 * 
 * Created on Apr 13, 2005
 * by knorke
 * 
 * package jLanI
 * for jLanI project
 *
 */
package de.uni_leipzig.asv.toolbox.jLanI.kernel;

import java.util.Set;



/**
 * @author knorke
 * @date Apr 13, 2005 2:20:43 PM
 */
public interface LanIKernelInterface {
	
	/**
	 * identify the language of a given sentence
	 * encapsulated in a request object
	 * @param request
	 * @return
	 * @throws RequestException with error message
	 * @throws DataSourceException
	 */
	public Response evaluate(Request request) throws RequestException, DataSourceException;
	
	
	/**
	 * keep the server running, unload unneeded datasources
	 * YOU DON'T NEED THIS FOR NOW!
	 * @return
	 */
	public boolean upkeep();
	
	
	
	/**
	 * returns the set of available datasources
	 * @return
	 */
	public Set getAvailableDatasources();
	

}
