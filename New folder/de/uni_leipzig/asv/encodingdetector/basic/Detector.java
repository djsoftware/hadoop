/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uni_leipzig.asv.encodingdetector.basic;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;


/**
 *
 * @author ccui
 */
public interface  Detector {
    /**
     * buffer size
     * CPD Detector need more Buffer for detecting.
     */
    public final static int BUFFER_SIZE = 40960;

    /**
     * set a url of document/html for encoding detector.
     * @param url
     */
    public void setDocument(URL url);

       /**
     * set the document for encoding detector.
     * @param buffer
     */
    public void setDocument(byte[] buffer);


     /**
     * set a file for encoding detector.
     * @param file
     */
    public void setDocument(File file);


    /**
     * get the best Encoding for this document.
     * @return String of encoding
     */
    public DetectResult getTopOneEncoding();

    /**
     *  get all encodings with confidence for this document
     * @return a list of encodings
     */
    public ArrayList<DetectResult> getAllEncoding();

    /**
     * get unicode data of this byte[]
     * @param i 
     * @return unicode data
     */
    public String getUnicodeData(int i);

}
