/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni_leipzig.asv.encodingdetector.chain;

import de.uni_leipzig.asv.encodingdetector.basic.DetectResult;
import de.uni_leipzig.asv.encodingdetector.basic.Detector;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * 
 * @author ccui
 */
public class BasicChains implements AnalyseChain {

    ArrayList<Detector> detectorList = new ArrayList<Detector>();
    ArrayList<DetectResult> resultList = new ArrayList<DetectResult>();

    public void addDetector(Detector dt) {
        detectorList.add(dt);
    }

    public void setSource(URL obj) {
        for (int i = 0; i < detectorList.size(); i++) {
            detectorList.get(i).setDocument(obj);
        }
    }

    public void setSource(byte[] obj) {
        for (int i = 0; i < detectorList.size(); i++) {
            detectorList.get(i).setDocument(obj);
        }
    }

    public void setSource(File obj) {
        for (int i = 0; i < detectorList.size(); i++) {
            detectorList.get(i).setDocument(obj);
        }
    }

    public void startParser() {
        Detector currentDetector;
        for (int i = 0; i < detectorList.size(); i++) {
            currentDetector = detectorList.get(i);
            resultList.add(currentDetector.getTopOneEncoding());
        }
    }

    public DetectResult getBestResult() {
        DetectResult bestResult = new DetectResult();
        bestResult.confidence = 0;
        for (int i = 0; i < resultList.size(); i++) {
            if (bestResult.confidence < resultList.get(i).confidence) {
                bestResult = resultList.get(i);
            }
        }
        return bestResult;
    }

    public ArrayList<DetectResult> getAllResult() {
        return resultList;
    }

}
