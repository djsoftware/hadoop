/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni_leipzig.asv.encodingdetector.basic;

/**
 * result of detector
 * @author ccui
 */
public class DetectResult {

    /** encoding name of document
     *  default iso-8859-1
     */
    public String encoding="";

    /**
     *  set confidence with 10%
     */
    public int confidence = 0;
}
