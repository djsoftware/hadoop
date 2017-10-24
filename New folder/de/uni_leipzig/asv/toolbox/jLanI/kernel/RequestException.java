/*
 * $Header: /usr/cvs/jlani2/src/de/uni_leipzig/asv/toolbox/jLanI/kernel/RequestException.java,v 1.1 2006-11-07 13:27:33 ksveds Exp $
 * 
 * Created on Apr 20, 2005
 * by knorke
 * 
 * package jLanI
 * for jLanI project
 *
 */
package de.uni_leipzig.asv.toolbox.jLanI.kernel;

/**
 * @author knorke
 */
public class RequestException extends Exception {

 

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3762253049980335159L;

	public RequestException (String string) {
		super(string);
	}
}
