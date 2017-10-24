/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uni_leipzig.asv.html2text;

import de.uni_leipzig.asv.html2text.impl.SimpleHTML2Text;
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
    public static void main(final String[] args) {
        SimpleHTML2Text h2t;
        try {
            h2t = new SimpleHTML2Text(new String(args[0]));
            System.out.println(h2t.getUTF8Text());
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
