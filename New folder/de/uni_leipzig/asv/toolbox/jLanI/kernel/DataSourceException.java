/*
 * $Header: /usr/cvs/jlani2/src/de/uni_leipzig/asv/toolbox/jLanI/kernel/DataSourceException.java,v 1.1 2006-11-07 13:27:33 ksveds Exp $
 * 
 * Created on 15.04.2005, 10:42:16 by knorke
 * for project jLanI
 */
package de.uni_leipzig.asv.toolbox.jLanI.kernel;

/**
 * @author knorke
 */
public class DataSourceException extends Exception {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 664991027778548483L;

    private int errorcode;

    public DataSourceException( String string ) {
        super( string );
        this.errorcode = -1;
        
    }
    
    public DataSourceException( String string, int errorcode ) {
        super( string );
        this.errorcode = errorcode;
        
    }
    
    
    /**
     * @return Returns the errorcode.
     */
    public int getErrorcode() {
        return errorcode;
    }
}
