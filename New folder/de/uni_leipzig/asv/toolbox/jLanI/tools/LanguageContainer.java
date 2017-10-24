/*
 * $Header: /usr/cvs/jlani2/src/de/uni_leipzig/asv/toolbox/jLanI/tools/LanguageContainer.java,v 1.1 2006-11-07 13:27:33 ksveds Exp $
 * Created on 16.08.2005
 *
 */
package de.uni_leipzig.asv.toolbox.jLanI.tools;

/**
 * @author Michael Welt
 * 
 */
public class LanguageContainer {
    private static LanguageContainer singleton = null;

    private String[] languages = null;

    private String[] defaults = { "de", "en", "fr" };


    private LanguageContainer() {
        languages = defaults;
    }


    public static LanguageContainer getInstance() {
        if ( singleton == null ) {
			singleton = new LanguageContainer();
		}
        return singleton;
    }


    public void setLanguages( String[] lans ) {
        if ( lans != null && lans.length > 0 ) {
			this.languages = lans;
		}
    }


    public String[] getLanguages() {
        return this.languages;
    }
}
