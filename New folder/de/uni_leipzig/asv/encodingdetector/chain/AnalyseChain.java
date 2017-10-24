/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uni_leipzig.asv.encodingdetector.chain;

import de.uni_leipzig.asv.encodingdetector.basic.DetectResult;
import de.uni_leipzig.asv.encodingdetector.basic.Detector;
import java.util.ArrayList;


/**
 *
 * @author ccui
 */
public interface  AnalyseChain {

    public void addDetector(Detector dt);

    public void startParser();

    public DetectResult getBestResult();
    
    public ArrayList<DetectResult> getAllResult();

}
