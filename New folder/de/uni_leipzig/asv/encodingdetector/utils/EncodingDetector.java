/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni_leipzig.asv.encodingdetector.utils;

import de.uni_leipzig.asv.encodingdetector.basic.DetectResult;
import de.uni_leipzig.asv.encodingdetector.chain.AnalyseChain;
import de.uni_leipzig.asv.encodingdetector.chain.BasicChains;
import de.uni_leipzig.asv.encodingdetector.impl.ICUDetectorImpl;
import de.uni_leipzig.asv.encodingdetector.impl.SimpleDetectorImpl;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ccui
 */
public class EncodingDetector {

    private BasicChains chain;

    public EncodingDetector(URL obj) {
        initialDetector();
        chain.setSource(obj);
        chain.startParser();

    }

    public EncodingDetector(byte[] obj) {
        initialDetector();
        chain.setSource(obj);
        chain.startParser();
    }

    public EncodingDetector(File obj) {
        initialDetector();
        chain.setSource(obj);
        chain.startParser();
    }

    private void initialDetector() {
        chain = new BasicChains();

        // icu analyse initialed
        ICUDetectorImpl icu = new ICUDetectorImpl();

        //SimpleDetectorImpl is a html tag detector
        SimpleDetectorImpl sdi = new SimpleDetectorImpl();

        chain.addDetector(icu);
        chain.addDetector(sdi);
    }

    public String getBestEncoding() {
        return chain.getBestResult().encoding;
    }

    public ArrayList<DetectResult> getAllEncoding() {
        return chain.getAllResult();
    }

    public static void main(String[] args) {
            EncodingDetector edt = new EncodingDetector(new File("/home/ccui/ohne_index.html"));
            System.out.println(edt.getBestEncoding());
 
    }
}
