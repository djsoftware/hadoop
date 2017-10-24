/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni_leipzig.asv.encodingdetector.impl;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
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

/**
 * ICU4J impmentation of encoding detector
 * http://site.icu-project.org/
 *
 * @author ccui
 */
public class ICUDetectorImpl implements Detector {

    private byte[] buffer = new byte[BUFFER_SIZE];
    private CharsetMatch[] matches;
    //The CharsetDetector class also implements a crude input filter that can strip out html and xml style tags
    private boolean isInputFilter = true;
    private CharsetDetector detector;

    /**
     * intial object with tag filter, ie html meta tag for encoding
     * @param isInputFilter
     */
    public ICUDetectorImpl(boolean isInputFilter) {
	this.isInputFilter = isInputFilter;
    }

    public ICUDetectorImpl() {
    }

    public void setDocument(byte[] buffer) {

	this.buffer = buffer;

	detector = new CharsetDetector();

	detector.setText(buffer);

	detector.enableInputFilter(isInputFilter);

	matches = detector.detectAll();
    }

    public void setDocument(URL url) {
	try {
	    BufferedInputStream br = new BufferedInputStream(url.openStream());
	    br.read(buffer);
	    setDocument(buffer);
	} catch (IOException ex) {
	    Logger.getLogger(ICUDetectorImpl.class.getName()).log(Level.SEVERE, null, ex);
	}
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
                    throw new Exception("File can not be read with byte[" + BUFFER_SIZE + "]");
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

	result.encoding = matches[0].getName();
	result.confidence = matches[0].getConfidence();

	return result;
    }

    public ArrayList<DetectResult> getAllEncoding() {
	DetectResult dr;
	ArrayList<DetectResult> result = new ArrayList<DetectResult>();
	for (int i = 0; i < matches.length; i++) {
	    dr = new DetectResult();
	    dr.encoding = matches[i].getName();
            Logger.getLogger(ICUDetectorImpl.class.getName()).log(Level.FINE, null,"Language Detected:\t"+matches[i].getLanguage());
	    dr.confidence = matches[i].getConfidence();
	    result.add(dr);
	}
	return result;
    }

    public String getUnicodeData(int i) {
	try {
	    return matches[i].getString();
	} catch (IOException ex) {
	    Logger.getLogger(ICUDetectorImpl.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }

    public static void main(String[] args) {
	ICUDetectorImpl icuIm = new ICUDetectorImpl();
	icuIm.setDocument(new File("/disk/1.txt"));
	ArrayList<DetectResult> drlist = icuIm.getAllEncoding();
	for (int i = 0; i < drlist.size(); i++) {
	    DetectResult dr = drlist.get(i);
	    //System.out.println(icuIm.getUnicodeData(i));
	    System.out.println("Encoding: " + dr.encoding);
	    System.out.println("Confidence: " + dr.confidence);
	}
    }
}
