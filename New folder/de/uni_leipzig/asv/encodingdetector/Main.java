/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni_leipzig.asv.encodingdetector;

import de.uni_leipzig.asv.encodingdetector.utils.EncodingDetector;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ccui
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("java -jar encodingdetector.jar URL");
            System.out.println("Example: java -jar encodingdetector.jar http://www.asv.informatik.uni-leipzig.de");
            System.out.println("Example: java -jar encodingdetector.jar file:/tmp/some.html");
        } else {
            try {
                EncodingDetector edt = new EncodingDetector(new URL(args[0]));
                System.out.println(edt.getBestEncoding());
            } catch (MalformedURLException ex) {
                Logger.getLogger(EncodingDetector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
