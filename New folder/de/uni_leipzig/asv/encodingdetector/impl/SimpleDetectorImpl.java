/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uni_leipzig.asv.encodingdetector.impl;

import de.uni_leipzig.asv.encodingdetector.basic.DetectResult;
import de.uni_leipzig.asv.encodingdetector.basic.Detector;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.LoggerProvider;
import net.htmlparser.jericho.Source;

/**
 * simple meta tag detector for html file.
 * @author ccui
 */
public class SimpleDetectorImpl implements Detector  {
    private byte[] buffer = new byte[BUFFER_SIZE];
    Source source;

    public SimpleDetectorImpl() {
       //disable log
       Config.LoggerProvider = LoggerProvider.DISABLED;
    }


    public void setDocument(URL url) {
        try {
            //TODO get url, save it into file and use setDocument(File file)
            BufferedInputStream br = new BufferedInputStream(url.openStream());
            br.read(buffer);
            setDocument(buffer);
        } catch (IOException ex) {
            Logger.getLogger(ICUDetectorImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setDocument(byte[] buffer) {
         source = new Source(new String(buffer));
    }

    public void setDocument(File file) {
        FileInputStream fis = null;
	try {
	    fis = new FileInputStream(file);
	    int read = fis.read(buffer);
	    if (read > 0) {
		setDocument(buffer);
	    } else {
		try {
		    throw new Exception("File can not be read with byte[4096]");
		} catch (Exception ex) {
		    Logger.getLogger(ICUDetectorImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	} catch (IOException ex) {
	    Logger.getLogger(ICUDetectorImpl.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    try {
		fis.close();
	    } catch (IOException ex) {
		Logger.getLogger(ICUDetectorImpl.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    public DetectResult getTopOneEncoding() {
       DetectResult result = new DetectResult();

       
       String encoding = source.getEncoding();
       if(encoding != null){
           result.encoding = encoding.toUpperCase();
           // default encodingdetector.
           result.confidence = 90;
       }
       return result;
    }

    public ArrayList<DetectResult> getAllEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getUnicodeData(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
