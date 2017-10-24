package de.uni_leipzig.asv.toolbox.util.commonDB;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JFrame;

/**
 * This class is to define a common quasi-standardized access on a DB, the user can
 * spezifie in a 'queryfile'.
 * 
 * @author Daniel Zimmermann
 */
public class CommonDB
{
	private JFrame parent;
	
	private String fileName;
	private Properties queryFile;
	private boolean showQueries;
        private static PreparedStatement pstmt;
	
	private static DBConnection dbCon;	
	private DBFunctions dbFunc;
        private Hashtable preparedQueries=new Hashtable();
	
	private boolean reEncode = false;	// Very important, when you have to re-encode a String
										// @see setReEncode & isReEncode
	private boolean isSwing = false;	// also very important for re-encoding, this value have to be set to know,
										// if you are running re-encing from console or Java.Swing 
										// update: Also important for The Error-Msg. - if swing, then DialogBox, else System.out
	private String reEncodeType;		// very important, never forget to specify the type from which you want to re-encode!
	/**
	 * A standard Constructor, which uses MySQL per default.<br>
	 * It establishes a connection to a Database and load the queryfile.
	 * 
	 * @param knownDBURL The URL to the DB (simple, w/o the additional Java-stuff).
	 * @param userId The user for the connection.
	 * @param passWd The users password.
	 * @param fileName The name of the queryfile - or better: the path!
	 * @param showQueries True, if you want to list the queries, the queryfile contains, to StandartOut
	 * @param dbName The database its name. 
	 * @param isSwing to specify wheter it is Swing based or not
	 */
	public CommonDB( String knownDBURL , String userId, String passWd, String fileName, boolean showQueries, String dbName, boolean isSwing )
	{
	   this.isSwing = isSwing;
           if (dbCon == null) {
		dbCon = new DBConnection();  //deleted DBConnection
		dbCon.setDriverClass("com.mysql.jdbc.Driver");
                
		if ( !knownDBURL.startsWith("//") )
		{
			dbCon.setDbURL("jdbc:mysql:" + "//" + knownDBURL );
			if ( !dbName.startsWith("/") ) {
				dbCon.setDbURL( dbCon.getDbURL() + "/" + dbName );
			} else {
				dbCon.setDbURL( dbCon.getDbURL() + dbName );
			}
		}
		else
		{
			dbCon.setDbURL("jdbc:mysql:" + knownDBURL );
			if ( !dbName.startsWith("/") ) {
				dbCon.setDbURL( dbCon.getDbURL() + "/" + dbName );
			} else {
				dbCon.setDbURL( dbCon.getDbURL() + dbName );
			}
		}
		dbCon.setUserID( userId );
		dbCon.setPassWd( passWd );
		
		this.establishDBConnection( dbCon, fileName, showQueries );
           } //fi null
	} // ------- end of the constructor -------
	
	/**
	 * A standard Constructor, which uses MySQL per default.<br>
	 * It establishes a connection to a Database and load the queryfile.
	 * 
	 * @param knownDBURL The URL to the DB (simple, w/o the additional Java-stuff).
	 * @param userId The user for the connection.
	 * @param passWd The users password.
	 * @param fileName The name of the queryfile - or better: the path!
	 * @param showQueries True, if you want to list the queries, the queryfile contains, to StandartOut
	 * @param isSwing to specify wheter it is Swing based or not 
	 */
	public CommonDB( String knownDBURL, String userId, String passWd, String fileName, boolean showQueries, boolean isSwing )
	{
	   this.isSwing = isSwing;
           if (dbCon==null) {
		dbCon = new DBConnection();
		dbCon.setDriverClass("com.mysql.jdbc.Driver");
		if ( !knownDBURL.startsWith("//") ) {
			dbCon.setDbURL("jdbc:mysql:" + "//" + knownDBURL );
		} else {
			dbCon.setDbURL("jdbc:mysql:" + knownDBURL );
		}
		dbCon.setUserID( userId );
		dbCon.setPassWd( passWd );
		
		this.establishDBConnection( dbCon, fileName, showQueries );
           }
	} // ------- end of the constructor -------
	
	/**
	 * This constructor establishes a connection to a Database and loads the queryfile.
	 * 
	 * @param driverClass The driverClass in Java to establish the connection to the DB.
	 * @param dbUrl The URL to this DB.
	 * @param userId The user for the connection.
	 * @param passWd The users password.
	 * @param fileName The name of the queryfile - or better: the path!
	 * @param showQueries True, if you want to list the queries, the queryfile contains, to StandartOut
	 * @param dbName The database its name.
	 * @param isSwing to specify wheter it is Swing based or not
	 */
	public CommonDB( String driverClass, String dbUrl, String userId, String passWd, String fileName, boolean showQueries, String dbName, boolean isSwing )
	{
	   this.isSwing = isSwing;
           if (dbCon == null) {
		dbCon = new DBConnection();
		if ( !dbName.startsWith("/") ) {
			dbCon.setDbURL( dbUrl + "/" + dbName );
		} else {
			dbCon.setDbURL( dbUrl + dbName );
		}
		dbCon.setDriverClass( driverClass );
		dbCon.setPassWd( passWd );
		dbCon.setUserID( userId );
		
		this.establishDBConnection( dbCon, fileName, showQueries );
           }
	} // ------- end of the constructor -------
	
	/**
	 * This constructor establishes a connection to a Database and loads the queryfile.
	 * 
	 * @param driverClass The driverClass in Java to establish the connection to the DB.
	 * @param dbUrl The URL to this DB.
	 * @param userId The user for the connection.
	 * @param passWd The users password.
	 * @param fileName The name of the queryfile - or better: the path!
	 * @param showQueries True, if you want to list the queries, the queryfile contains, to StandartOut
	 * @param isSwing to specify wheter it is Swing based or not
	 */
	public CommonDB( String driverClass, String dbUrl, String userId, String passWd, String fileName, boolean showQueries, boolean isSwing )
	{
	   this.isSwing = isSwing;
           if (dbCon == null) {
		dbCon = new DBConnection();
		dbCon.setDbURL( dbUrl );
		dbCon.setDriverClass( driverClass );
		dbCon.setPassWd( passWd );
		dbCon.setUserID( userId );
		
		this.establishDBConnection( dbCon, fileName, showQueries );
           }
	} // ------- end of the constructor -------
	
	/**
	 * This constructor establishes a connection to a Database and loads the queryfile.
	 * 
	 * @param dbCon The DBConnection-Object, that holds the connectivity data for the DB
	 * @param fileName The name of the queryfile - or better: the path!
	 * @param showQueries True, if you want to list the queries, the queryfile contains, to StandartOut
	 * @param isSwing to specify wheter it is Swing based or not
	 */
	public CommonDB( DBConnection dbCon, String fileName, boolean showQueries, boolean isSwing )
	{
		this.isSwing = isSwing;
		
		this.establishDBConnection( dbCon, fileName, showQueries );
		
	} // ------- end of the constructor -------
	
	/**
	 * This constructor establishes a connection to a Database and loads the queryfile.
	 * 
	 * @param dbCon The DBConnection-Object, that holds the connectivity data for the DB
	 * @param fileName The name of the queryfile - or better: the path!
	 * @param showQueries True, if you want to list the queries, the queryfile contains, to StandartOut
	 */
	public CommonDB( DBConnection dbCon, String fileName, boolean showQueries )
	{		
		this.establishDBConnection( dbCon, fileName, showQueries );		
	} // ------- end of the constructor -------

	/**
	 * This method is only internal to establish the DB-Connection.
	 * 
	 * @param dbCon The DBConnection-Object, that holds the connectivity data for the DB
	 * @param fileName The name of the queryfile - or better: the path!
	 * @param showQueries True, if you want to list the queries, the queryfile contains, to StandartOut
	 */
	private void establishDBConnection( DBConnection dbCon, String fileName, boolean showQueries )
	{
		// let me know, if you want to show the queries or not.
		this.showQueries = showQueries;
		
		// only if the filename for the queries ends with .query, try to open it
		if ( fileName != null && fileName.endsWith(".query") )
		{
			this.fileName = fileName;
			
			// create new property-object
			this.queryFile = new Properties();
			try
			{
				// load the data of the file to the property-object
				FileInputStream fis = new FileInputStream( fileName );
				this.queryFile.load( fis );
				
				// now the properties, contained by the queryfile, might be listed out to StandartOut
				if ( showQueries ) {
					this.queryFile.list( System.out );
				}
			}
			catch ( FileNotFoundException e )
			{
				if ( this.isSwing )
				{
					ErrorDialog ed=new ErrorDialog( parent, "Couldn't find the file you've specified - please check the position!" );
					ed.show( ed );
				} else {
					System.out.println( "Couldn't find the file you've specified - please check the position!" );
				}				
			}
			catch ( IOException e )
			{
				if ( this.isSwing )
				{
					ErrorDialog ed=new ErrorDialog( parent, "An error occured, while loading the file - maybe the file you've specified, is corrupt." );
					ed.show( ed );
				} else {
					System.out.println( "An error occured, while loading the file - maybe the file you've specified, is corrupt." );
				}				
			}
				
		}
		else
		{
			if ( this.isSwing )
			{
				ErrorDialog ed=new ErrorDialog( parent, "Please check, that your query file ends with \".query\"." );
				ed.show( ed );
			} else {
				System.out.println( "Please check, that your query file ends with \".query\"." );
			}
		}
		
		// create an DBFuntion-object and try to establish a DB connection
		this.dbCon = dbCon;
		this.dbFunc = new DBFunctions( this.dbCon.getDriverClass(),
				this.dbCon.getDbURL(),
				this.dbCon.getUserID(),
				this.dbCon.getPassWd(),
				this.isSwing );		
		
	}
	
	/**
	 * This one is very important, when you know, that your Strings in a DB are stored via ISO-LATIN-1,
	 * but you know, that tey are containing UTF-8 data.<br>
	 * In this case you <I>need</I> to set reEncode to 'true'!<br>
	 * If not, it's per default set to false, but maybe then you have to reset it to false again.<br>
	 * Never forget to use the setReEncodeType method, too! 
	 * 
	 * @param reEncode True, if the Strings from the DB need to be re-encodes.
	 */
        
        /*
        public void finalize() {
            System.out.println("finalize");
          
            
        }
        */
        
	public void setReEncode( boolean reEncode )
	{
		this.reEncode = reEncode;
	}
	
	/**
	 * This one is very important, when you know, that your Strings in a DB are stored via ISO-LATIN-1,
	 * but you know, that tey are containing UTF-8 data.<br>
	 * In this case you <I>need</I> to set reEncode to 'true'!<br>
	 * If not, it's per default set to false, but maybe then you have to reset it to false again.<br>
	 * In this method you can specify the type of reencoding by your own. 
	 * 
	 * @param reEncode
	 * @param type
	 */
	public void setReEncode( boolean reEncode, String type )
	{
		this.setReEncode( reEncode );
		this.setReEncodeType( type );
	}
	
	/**
	 * This method shows you the actual state of the reEncode variable.<br>
	 * True: The Strings have to be re-encoded.<br>
	 * False: There's no need for re-encoding.
	 * 
	 * @return The bool value of reEncode.
	 */
	public boolean isReEncode()
	{
		return this.reEncode;
	}
	
	/**
	 * This method is very important for re-encoding, because for that you <I>need</I> to know,
	 * if you're running re-encoding from console or from Java.Swing.<br>
	 * Also you will need this, if you want to show DialogBoxes for the Errors or not.
	 * 
	 * @param isSwing True, if you're runnging from Java.Swing.
	 */
	public void setIsSwing( boolean isSwing )
	{
		this.isSwing = isSwing;
	}
	
	/**
	 * This method shows you the bool value of the isSwing variable.
	 * 
	 * @return The bool value of isSwing.
	 */
	public boolean isSwing()
	{
		return this.isSwing;
	}
	
	/**
	 * This method is to set the encoding type, from which you want to re-encode.<br>
	 * Never forgett to specify, when you want to use rencoding! 
	 * 
	 * @param type The type from which you want to re-encode. (ISO-8859-1 for example)
	 */
	public void setReEncodeType( String type )
	{
		if ( type != null ) {
			this.reEncodeType = type;
		} else {
			this.reEncodeType = "ISO-8859-1";	// standardized Type from which you can re-encode
		}
	}
	
	/**
	 * This methods returns the actual re-encoding Type.
	 * 
	 * @return the actual Type from which to re-encode.
	 */
	public String getReEncodeType()
	{
		return this.reEncodeType;
	}
	
	/**
	 * This methods loads a new queryfile, if neccesary.
	 * 
	 * @param newQueryFileName The name, or better the path, of the new queryfile
	 */
	public void changeQueryFileCatched( String newQueryFileName )
	{
		try
		{
			changeQueryFile( newQueryFileName );
		}
		catch ( FileNotFoundException fnfe )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "Couldn't find the file you've specified - please check the position!" );
				ed.show( ed );
			} else {
				System.out.println( "Couldn't find the file you've specified - please check the position!" );
			}
		}
		catch ( IOException ioe )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "An error occured, while loading the file - maybe the file you've specified, is corrupt." );
				ed.show( ed );
			} else {
				System.out.println( "An error occured, while loading the file - maybe the file you've specified, is corrupt." );
			}
		}
	}
	
	/**
	 * This methods loads a new queryfile, if neccesary.
	 * 
	 * @param newQueryFileName The name, or better the path, of the new queryfile
	 */
	public void changeQueryFile( String newQueryFileName )
	throws FileNotFoundException, IOException
	{
		queryFile = null;
		
		// see above!
		if ( fileName.endsWith(".query") )
		{
			this.fileName = newQueryFileName;
			this.queryFile = new Properties();
//			try
//			{
				FileInputStream fis = new FileInputStream( newQueryFileName );
				this.queryFile.load( fis );
				
				if ( showQueries ) {
					this.queryFile.list( System.out );
//			}
//			catch ( FileNotFoundException e )
//			{
//				ErrorDialog ed = new ErrorDialog( parent, "Couldn't find the file you've specified - please check the position!" );
//				ed.show( ed );
//			}
//			catch ( IOException e )
//			{
//				ErrorDialog ed = new ErrorDialog( parent, "An error occured, while loading the file - maybe the file you've specified, is corrupt." );
//				ed.show( ed );
//			}
				}
				
		}
		else
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "Please check, that your query file ends with \".query\"." );
				ed.show( ed );
			} else {
				System.out.println( "Please check, that your query file ends with \".query\"." );
			}
			
		}		
	} // ------- end of changeQueryFile -------
	
	/**
	 * This method returns the name/path of the queryfile.
	 * 
	 * @return the path/name of the queryfile 
	 */
	public String getQueryFileName()
	{
		return this.fileName;
	} // ------- end of getQueryFileName -------
	
	/**
	 * This method is to set, if you want to list the queries or not (if you change the queryfile...)
	 * 
	 * @param showQueries
	 */
	public void setShowQueries( boolean showQueries )
	{
		this.showQueries = showQueries;
	} // ------- end of setShowQueries -------

	/**
	 * Returns the status of the attribute, that allows to show the queries, when the queryfile is changed... 
	 * 
	 * @return true, if the queries will be listed out.
	 */
	public boolean isShowQueries()
	{
		return showQueries;
	} // ------- end of isShowQueries -------	

// ------------------- these methods might not be used to often - reasons below --------------------------	

	/**
	 * Adds a query to the queryfile
	 * 
	 * @param keyName The name of the key to add to the file
	 * @param query The query that relates on the key
	 */
	public void addQuery( String keyName, String query )
	{
		// a new query will be added to the queryfile - this is a simple method, but be aware:
		// adding a new key and value to the property file, will mix up the whole file, when stored.
		// Unfortunately I don't know how to solve this...
		// Sorry!
		// -------------------------------------------------------------------------------------------
		// Update - I know, there is a simple wai, but I haven't implemented it yet:
		// You can read in the file like any other textfile else and add the new line with <keyName>=<query>...
		
		queryFile.setProperty( keyName, query );
	} // ------- end of addQuery -------
	
// XXX write a remove-procedure for the queryfile... - canceled - the user have to remove by his/her own...
//	public void removeQuery( String keyName )
//	{
//		// Same problem here - after storing the file, nothing will be like before :-)
//		
//	} // ------- end of removeQuery -------
	
	/**
	 * This will store the queryfile with possible new queries.<br>
	 * But: be carefull, this might change the order of the queryfile so that it will not any longer be easy to read this file.
	 * 
	 * @param comment If you want to add a comment to beginn of the queryfile...
	 */
	public void storeQueryFileCatched( String comment )
	{
		try
		{
			storeQueryFile( comment );
		}
		catch ( IOException e )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "An error occured, while writing to file "+fileName+"!" );
				ed.show( ed );
			} else {
				System.out.println( "An error occured, while writing to file "+fileName+"!" );
			}
		}
	}
	
	/**
	 * This will store the queryfile with possible new queries.<br>
	 * But: be carefull, this might change the order of the queryfile so that it will not any longer be easy to read this file.
	 * 
	 * @param comment If you want to add a comment to beginn of the queryfile...
	 */
	public void storeQueryFile( String comment )
	throws IOException
	{
		// So. You are sure, that you want to do this??? - Ok!
		// PS: comment might be null...
//		try
//		{
			FileOutputStream fos = new FileOutputStream( fileName );
			queryFile.store(fos, comment);
//		}
//		catch (IOException e)
//		{
//			ErrorDialog ed = new ErrorDialog( parent, "An error occured, while writing to file "+fileName+"!" );
//			ed.show( ed );
//		}
	} // ------- end of storeQueryFile -------

// -------------------------------------------------------------------------------------------------------

	/**
	 * Returns the query to a given key.
	 * 
	 * @return The query.
	 */
	public String getQuery( String key )
	{
		return queryFile.getProperty( key );
	}
	
	/**
	 * Returns the Primary Key to a given table on the DB, that this object has a connection to.
	 * 
	 * @param tableName The tablename, where you want to get the primary's key name. 
	 * @return
	 */
	public String getPrimaryKeyNameCatched( String tableName )
	{
		try
		{
			String result = getPrimaryKeyName( tableName );
			return result;
		}
		catch ( SQLException e )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "Error... Couldn't retrieve the DBs Metadata..." );
				ed.show( ed );
			} else {
				System.out.println( "Error... Couldn't retrieve the DBs Metadata..." );
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the Primary Key to a given table on the DB, that this object has a connection to.
	 * 
	 * @param tableName The tablename, where you want to get the primary's key name. 
	 * @return
	 */
	public String getPrimaryKeyName( String tableName )
	throws SQLException
	{
		String primkey = "";
		
		// a DB-MetaData-Object - contains many interessting data of a DB!
		DatabaseMetaData dbmd;
//		try
//		{
			dbmd = dbFunc.getConnection().getMetaData();
			
			// get the primary key
			ResultSet dbmdrs = dbmd.getPrimaryKeys( null, null, tableName );
			
			while ( dbmdrs.next() )
			{
				
				if ( primkey.equals("") ) {
					primkey = primkey + dbmdrs.getString("COLUMN_NAME");
				} else {
					primkey = primkey + ", " + dbmdrs.getString("COLUMN_NAME");				
					//System.out.println( dbmdrs.getString( "KEY_SEQ" ) );
					//System.out.println( dbmdrs.getString( "PK_NAME" ) );
				}
			}			
			//System.out.println(primkey);
			
			// close the resultset
			dbmdrs.close();
//		}
//		catch (SQLException e)
//		{
//			ErrorDialog ed = new ErrorDialog( parent, "Error... Couldn't retrieve the DBs Metadata..." );
//			ed.show( ed );
//		}
		
		// here it is...
		return primkey;
	} // ------- end of getPrimaryKeyName -------
	
	/**
	 * Returns the highest number of an primary key - you can see, that this method needs an integer or so as a primary key.
	 * 
	 * @param tableName The name of the table where to look on. 
	 * @param primkey The name of the primary key (use the getPrimaryKeyName-method)
	 * @return The value, that is the maximum in this table 
	 */
	public int getHighestPrimaryKeyCatched( String tableName, String primkey )
	{
		try
		{
			int i = getHighestPrimaryKey( tableName, primkey );
			return i;
		}
		catch ( SQLException e )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "Error... Couldn't retrieve the DBs Metadata..." );
				ed.show( ed );
			} else {
				System.out.println( "Error... Couldn't retrieve the DBs Metadata..." );
			}
		}
		
		return 0;		
	}
	
	/**
	 * Returns the highest number of an primary key - you can see, that this method needs an integer or so as a primary key.
	 * 
	 * @param tableName The name of the table where to look on. 
	 * @param primkey The name of the primary key (use the getPrimaryKeyName-method)
	 * @return The value, that is the maximum in this table 
	 */
	public int getHighestPrimaryKey( String tableName, String primkey )
	throws SQLException
	{
		int columnCount = 0;
		
//		try
//		{			
			ResultSet rs = dbFunc.execStmt( "SELECT max("+ primkey +") FROM " + tableName );
			while ( rs.next() )
			{
				columnCount = rs.getInt( 1 );
			}				
			//System.out.println(columnCount);
			rs.close();
//		}
//		catch (SQLException e)
//		{
//			ErrorDialog ed = new ErrorDialog( parent, "Error... Couldn't retrieve the DBs Metadata..." );
//			ed.show( ed );
//		}			
		
		return columnCount;
	} // ------- end of getMaxCountOfRowsInTableByPrimaryKey -------
	
	/**
	 * Very similiar to the getMaxCountOfRowsInTableByPrimaryKey-method.<br>
	 * But: This method returns the lowest value.
	 * 
	 * @param tableName The name of the table where to look on. 
	 * @param primkey The name of the primary key (use the getPrimaryKeyName-method)
	 * @return The value, that is the minimum in this table 
	 */
	public int getLowestPrimaryKeyCatched( String tableName, String primkey )
	{
		try
		{
			int i = getLowestPrimaryKey( tableName, primkey );
			return i;
		}
		catch ( SQLException e )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "Error... Couldn't retrieve the DBs Metadata..." );
				ed.show( ed );
			} else {
				System.out.println( "Error... Couldn't retrieve the DBs Metadata..." );
			}
		}
		
		return 0;
	}
	
	/**
	 * Very similiar to the getMaxCountOfRowsInTableByPrimaryKey-method.<br>
	 * But: This method returns the lowest value.
	 * 
	 * @param tableName The name of the table where to look on. 
	 * @param primkey The name of the primary key (use the getPrimaryKeyName-method)
	 * @return The value, that is the minimum in this table 
	 */
	public int getLowestPrimaryKey( String tableName, String primkey )
	throws SQLException
	{
		int minPK = 0;

//		try
//		{
			ResultSet rs = dbFunc.execStmt( "SELECT min("+ primkey +") FROM " + tableName );
			while ( rs.next() )
			{
				minPK = rs.getInt( 1 );
			}				
			//System.out.println(columnCount);
			rs.close();
//		}
//		catch (SQLException e)
//		{
//			ErrorDialog ed = new ErrorDialog( parent, "Error... Couldn't retrieve the DBs Metadata..." );
//			ed.show( ed );
//		}			
		
		return minPK;
	} // ------- end of getLowestPrimaryKey -------
	
	/**
	 * Executes a query on a DB, that will not return any results (such as INSERTS, and so on).
	 * 
	 * @param query The query, that have to be executed on the DB
	 * @param questionMarks A Vector-object, that contains the data, that have to be entered for prepared statements<br>Null, if there is no information to enter<br>Add Strings to this Vector.  
	 */
	public void executeQueryWithoutResultsCatched( String query, Vector questionMarks )
	{
		try
		{
			executeQueryWithoutResults( query, questionMarks );
		}
		catch ( SQLException e )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "An error occured while processing a PreparedStatement\n"															
						+ "or an error occured while excecuting a PreparedSatement" );
				ed.show( ed );
			} else {
				System.out.println( "An error occured while processing a PreparedStatement\n"															
						+ "or an error occured while excecuting a PreparedSatement" );
			}
		}
	}
	
	/**
	 * Executes a query on a DB, that will not return any results (such as INSERTS, and so on).
	 * 
	 * @param query The query, that have to be executed on the DB
	 * @param questionMarks A Vector-object, that contains the data, that have to be entered for prepared statements<br>Null, if there is no information to enter<br>Add Strings to this Vector.  
	 */
	public void executeQueryWithoutResults( String query, Vector questionMarks )
	throws SQLException //SQLException: this method
	{
		Iterator it;
		
		int i = 1;
		
		// 'prepare' a PreparedStatement
               // System.out.println("Preparing "+query);
                                
                if (preparedQueries.containsKey(query)) {
                    pstmt=(PreparedStatement)preparedQueries.get(query);
                } else {
                    pstmt =  dbFunc.getPreparedStmt( query );
                    preparedQueries.put(query, pstmt);
                }
                //if (!(questionMarks == null)) System.out.println("With params "+questionMarks.toString()) ;
                
		// add the question marks to this prepared statement
		if ( questionMarks != null && !questionMarks.isEmpty() )
		{
			it = questionMarks.iterator();
			
			while ( it.hasNext() )
			{
				String o = (String) it.next(); // I guess this is the easyiest way, but will it work? I dunno! - seems to work fine
				
				try
				{
					pstmt.setString( i, o );
					i += 1;
				}
				catch (SQLException e)
				{
					ErrorDialog ed = new ErrorDialog( parent, "An error occured while processing a PreparedStatement" );
					ed.show( ed );
				}
			}
		}
                
                
		
//		try
//		{
			if ( pstmt.executeUpdate() != 1 ) // hm... might be tested very well!
			{
				if ( this.isSwing )
				{
					ErrorDialog ed = new ErrorDialog( parent, "A PreparedStatement was not executed for unknown reasons." );
					ed.show( ed );
				} else {
					System.out.println( "A PreparedStatement was not executed for unknown reasons." );
				}
			}
//		}
//		catch (SQLException e)
//		{
//			ErrorDialog ed = new ErrorDialog( parent, "An error occured while executing a PreparedStatement" );
//			ed.show( ed );
//		}
        
	} // ------- end of excecuteQueryWithoutResults -------
	
	/**
	 * Executes a query on a DB, that will return a result (SELECTs).
	 * 
	 * @param query The query, that have to be executed on the DB
	 * @param questionMarks A Vector-object, that contains the data, that have to be entered for prepared statements<br>Null, if there is no information to enter<br>Add Strings to this Vector.
	 * @return A Vector that contains String[]-arrays with the results  
	 */
	public Vector executeQueryWithResultsCatched( String query, Vector questionMarks )
	{
		try
		{
			Vector result = executeQueryWithResults( query, questionMarks );
			return result;
		}
		catch ( SQLException sqle )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "An error occured while processing a PreparedStatement\n"															
						+ "or an error occured while excecuting a PreparedSatement" );
				ed.show( ed );
			} else {
				System.out.println( "An error occured while processing a PreparedStatement\n"															
						+ "or an error occured while excecuting a PreparedSatement" );
			}
		}
		catch ( Exception e )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "An error occured while re-encoding a string:" + e.getMessage() );
				ed.show( ed );
			} else {
				System.out.println( "An error occured while re-encoding a string:" + e.getMessage() );
			}
		}
		
		return null;
	}
	
	/**
	 * Executes a query on a DB, that will return a result (SELECTs).
	 * 
	 * @param query The query, that have to be executed on the DB
	 * @param questionMarks A Vector-object, that contains the data, that have to be entered for prepared statements<br>Null, if there is no information to enter<br>Add Strings to this Vector.
	 * @return A Vector that contains String[]-arrays with the results  
	 */
	public Vector executeQueryWithResults( String query, Vector questionMarks )
	throws SQLException, Exception	//SQLException: this method; Exception: getStandardizedString
	{
		Iterator it;
		
		ResultSet rs;
		
		Vector outputVector = new Vector();
		
		int i = 1;
		
		PreparedStatement pstmt =  dbFunc.getPreparedStmt( query );
		
		if ( questionMarks != null && !questionMarks.isEmpty() )
		{
			it = questionMarks.iterator();
			
			while ( it.hasNext() )
			{
				String o = (String) it.next();
				
//				try
//				{
					pstmt.setString( i, o );
					i += 1;
//				}
//				catch (SQLException e)
//				{
//					ErrorDialog ed = new ErrorDialog( parent, "An error occured while processing a PreparedStatement" );
//					ed.show( ed );
//				}
			}
		}
		
//		try
//		{
			// executes the query on the DB
			rs = pstmt.executeQuery();
			
			// returns the ResultSets metadata
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int columnCount = rsmd.getColumnCount();
			String[] results = null;
			
			while ( rs.next() )
			{
				// creates the String[]-array
				results = new String[columnCount];
				
				for ( int j = 1; j <= columnCount; j++ )
				{
					// ... fills it ...
					if ( this.reEncode ) {
						results[j-1] = this.getStandardizedString( this.getReEncodeType(), rs.getString( j ), this.isSwing );
					} else {
						results[j-1] = rs.getString( j );
					}
				}
				
				// ... and finally adds it to the output-Vector
				outputVector.add( results );
			}
			
			if ( rs == null )
			{
				if ( this.isSwing )
				{
					ErrorDialog ed = new ErrorDialog( parent, "A PreparedStatement was not executed for unknown reasons." );
					ed.show( ed );
				} else {
					System.out.println( "A PreparedStatement was not executed for unknown reasons." );
				}
			}
//		}
//		catch (SQLException e)
//		{
//			System.out.println(e);
//			ErrorDialog ed = new ErrorDialog( parent, "An error occured while executing a PreparedStatement" );
//			ed.show( ed );
//		}
		
		if ( outputVector != null ) {
			return outputVector;
		} else {
			return null;
		}
	} // ------- end of executeQueryWithResults -------
	
	/**
	 * Executes a query on a DB, that will return a result (SELECTs).
	 * 
	 * @param query The query, that have to be executed on the DB
	 * @param questionMarks A Vector-object, that contains the data, that have to be entered for prepared statements<br>Null, if there is no information to enter<br>Add Strings to this Vector.
	 * @return TheResultSet with the results  
	 */
	public ResultSet executeQueryWithResults_ResultSetCatched( String query, Vector questionMarks ) // ResultSet-Variante
	{
		try
		{
			ResultSet result = executeQueryWithResults_ResultSet( query, questionMarks );
			return result;
		}
		catch ( SQLException e )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "An error occured while processing a PreparedStatement\n"															
						+ "or an error occured while excecuting a PreparedSatement" );
				ed.show( ed );
			} else {
				System.out.println( "An error occured while processing a PreparedStatement\n"															
						+ "or an error occured while excecuting a PreparedSatement" );
			}
		}
		
		return null;
	}
	
	/**
	 * Executes a query on a DB, that will return a result (SELECTs).
	 * 
	 * @param query The query, that have to be executed on the DB
	 * @param questionMarks A Vector-object, that contains the data, that have to be entered for prepared statements<br>Null, if there is no information to enter<br>Add Strings to this Vector.
	 * @return TheResultSet with the results  
	 */
	public ResultSet executeQueryWithResults_ResultSet( String query, Vector questionMarks ) // ResultSet-Variante
	throws SQLException	//SQLException: this method
	{
		Iterator it;
		
		ResultSet rs = null;
		
		int i = 1;
		
		PreparedStatement pstmt =  dbFunc.getPreparedStmt( query );
		
		if ( questionMarks != null && !questionMarks.isEmpty() )
		{
			it = questionMarks.iterator();
			
			while ( it.hasNext() )
			{
				String o = (String) it.next();
				
//				try
//				{
					pstmt.setString( i, o );
					i += 1;
//				}
//				catch (SQLException e)
//				{
//					ErrorDialog ed = new ErrorDialog( parent, "An error occured while processing a PreparedStatement" );
//					ed.show( ed );
//				}
			}
		}
		
//		try
//		{
			rs = pstmt.executeQuery();
			
			if ( rs == null )
			{
				if ( this.isSwing )
				{
					ErrorDialog ed = new ErrorDialog( parent, "A PreparedStatement was not executed for unknown reasons." );
					ed.show( ed );
				} else {
					System.out.println( "A PreparedStatement was not executed for unknown reasons." );
				}
			}
//		}
//		catch (SQLException e)
//		{
//			ErrorDialog ed = new ErrorDialog( parent, "An error occured while executing a PreparedStatement" );
//			ed.show( ed );
//		}
		
		if ( rs != null ) {
			return rs;
		} else {
			return null;
		}
	} // ------- end of executeQueryWithResults - ResultSet -------
	
	/**
	 * Executes a query on a DB, that will return a result (SELECTs).<br>
	 * Very similar to the executeQueryWithResults-method, but:<br>
	 * Returns only a part of the data (useful for guge DBs)<br><br>
	 * Attention: To get all, including the very last entry of the table, you have to add 1 to the endPosition!<br>
	 * But not during the normal run, only at the very last one!
	 * 
	 * @param query The query, that have to be executed on the DB
	 * @param questionMarks A Vector-object, that contains the data, that have to be entered for prepared statements<br>Null, if there is no information to enter<br>Add Strings to this Vector.
	 * @return A Vector that contains String[]-arrays with the results  
	 */	
	public Vector executePartOfAQueryWithResultsCatched(
			String query,
			Vector questionMarks,
			int startPosition,
			int endPosition,
			String primkey )
	{
		try
		{
			Vector result = executePartOfAQueryWithResults( query, questionMarks, startPosition, endPosition, primkey );
			return result;
		}
		catch ( SQLException sqle )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "An error occured while processing a PreparedStatement\n"															
						+ "or an error occured while excecuting a PreparedSatement" );
				ed.show( ed );
			} else {
				System.out.println( "An error occured while processing a PreparedStatement\n"															
						+ "or an error occured while excecuting a PreparedSatement" );
			}
		}
		catch ( Exception e )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "An error occured while re-encoding a string:" + e.getMessage() );
				ed.show( ed );
			} else {
				System.out.println( "An error occured while re-encoding a string:" + e.getMessage() );
			}
		}
		
		return null;
	}
	
	/**
	 * Executes a query on a DB, that will return a result (SELECTs).<br>
	 * Very similar to the executeQueryWithResults-method, but:<br>
	 * Returns only a part of the data (useful for guge DBs)<br><br>
	 * Attention: To get all, including the very last entry of the table, you have to add 1 to the endPosition!<br>
	 * But not during the normal run, only at the very last one!
	 * 
	 * @param query The query, that have to be executed on the DB
	 * @param questionMarks A Vector-object, that contains the data, that have to be entered for prepared statements<br>Null, if there is no information to enter<br>Add Strings to this Vector.
	 * @return A Vector that contains String[]-arrays with the results  
	 */
    public Vector<String[]> executePartOfAQueryWithResults(String query,
            Vector questionMarks, int startPosition, int endPosition,
            String primkey) throws SQLException, Exception // SQLException:
                                                            // this method;
                                                            // Exception:
                                                            // getStandardizedString
    {
        Iterator it;
        // '->\'->\\\'->\\\\\\\'
        // query.replaceAll("\"", "\\\"");
        // query.replaceAll("\'", "\\\'");
        // PreparedStatement pstmt;

        ResultSet rs;

        Vector<String[]> outputVector = new Vector<String[]>();

        int i = 1;

        // adds the needet information to the query, to execute only a part of
        // it.
        if (query.length() > 0 && query.toLowerCase().indexOf("where") != -1) {
            // pstmt = dbFunc.getPreparedStmt(
            query = query + " and " + primkey + ">=?" + " and " + primkey
                    + "<?";// );
        } else {
            // pstmt = dbFunc.getPreparedStmt(
            query = query + " where " + primkey + ">=?" + " and " + primkey
                    + "<?";// );
        }

        if (preparedQueries.containsKey(query)) {
            // System.out.println("prepared statement exists already");
            pstmt = (PreparedStatement) preparedQueries.get(query);
        } else {
            // System.out.println("prepared Statemaent not found, create one
            // new");
            pstmt = dbFunc.getPreparedStmt(query);
            if(pstmt!=null)preparedQueries.put(query, pstmt);
        }

        if (questionMarks != null && !questionMarks.isEmpty()) {
            it = questionMarks.iterator();

            while (it.hasNext()) {
                String o = (String) it.next();
                // System.out.println("i in Vector="+i+" in Vector is "+o);
                // try
                // {
                pstmt.setString(i, o);
                i += 1;
                // }
                // catch (SQLException e)
                // {
                // ErrorDialog ed = new ErrorDialog( parent, "An error occured
                // while processing a PreparedStatement" );
                // ed.show( ed );
                // }
            }
        }
        // System.out.println("i="+i);
        pstmt.setString(i, "" + startPosition);
        i += 1;
        pstmt.setString(i, "" + endPosition);
        i += 1;
        // try
        // {
        rs = pstmt.executeQuery();

        ResultSetMetaData rsmd = rs.getMetaData();

        int columnCount = rsmd.getColumnCount();
        String[] results = null;

        while (rs.next()) {
            results = new String[columnCount];

            for (int j = 1; j <= columnCount; j++) {
                if (this.reEncode)
                    results[j - 1] = this.getStandardizedString(this
                            .getReEncodeType(), rs.getString(j), this.isSwing);
                else
                    results[j - 1] = rs.getString(j);
            }

            outputVector.add(results);
        }

        if (rs == null) {
            if (this.isSwing) {
                ErrorDialog ed = new ErrorDialog(parent,
                        "A PreparedStatement was not executed for unknown reasons.");
                ed.show(ed);
            } else
                System.out
                        .println("A PreparedStatement was not executed for unknown reasons.");
        }
        // }
        // catch (SQLException e)
        // {
        // ErrorDialog ed = new ErrorDialog( parent, "An error occured while
        // executing a PreparedStatement" );
        // ed.show( ed );
        // }

        if (outputVector != null)
            return outputVector;
        else
            return null;
    } // ------- end of executePartOfAQueryWithResults -------
		
	/**
	 * Executes a query on a DB, that will return a result (SELECTs).<br>
	 * Very similar to the executeQueryWithResults-method, but:<br>
	 * Returns only a part of the data (useful for guge DBs)<br><br>
	 * Attention: To get all, including the very last entry of the table, you have to add 1 to the endPosition!<br>
	 * But not during the normal run, only at the very last one!
	 * 
	 * @param query The query, that have to be executed on the DB
	 * @param questionMarks A Vector-object, that contains the data, that have to be entered for prepared statements<br>Null, if there is no information to enter<br>Add Strings to this Vector.
	 * @return The ResultSet with the results  
	 */
	public ResultSet executePartOfAQueryWithResults_ResultSetCatched(
			String query,
			Vector questionMarks,
			int startPosition,
			int endPosition,
			String primkey )
	{
		try
		{
			ResultSet result = executePartOfAQueryWithResults_ResultSet( query, questionMarks, startPosition, endPosition, primkey );
			return result;
		}
		catch ( SQLException e )
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "An error occured while processing a PreparedStatement\n"															
						+ "or an error occured while excecuting a PreparedSatement" );
				ed.show( ed );
			} else {
				System.out.println( "An error occured while processing a PreparedStatement\n"															
						+ "or an error occured while excecuting a PreparedSatement" );
			}
		}		
		
		return null;
	}
	
	/**
	 * Executes a query on a DB, that will return a result (SELECTs).<br>
	 * Very similar to the executeQueryWithResults-method, but:<br>
	 * Returns only a part of the data (useful for guge DBs)<br><br>
	 * Attention: To get all, including the very last entry of the table, you have to add 1 to the endPosition!<br>
	 * But not during the normal run, only at the very last one!
	 * 
	 * @param query The query, that have to be executed on the DB
	 * @param questionMarks A Vector-object, that contains the data, that have to be entered for prepared statements<br>Null, if there is no information to enter<br>Add Strings to this Vector.
	 * @return The ResultSet with the results  
	 */
	public ResultSet executePartOfAQueryWithResults_ResultSet(
			String query,
			Vector questionMarks,
			int startPosition,
			int endPosition,
			String primkey )
	throws SQLException	//SQLException: this method
	{
		Iterator it;
		
		PreparedStatement pstmt;
		
		ResultSet rs = null;
		
		int i = 0;
		
		if ( query.length() > 0 && query.toLowerCase().indexOf( "where" ) != -1 )
		{
			pstmt =  dbFunc.getPreparedStmt(
					query
					+" and "+primkey+">="+startPosition
					+" and "+primkey+"<"+endPosition );
		}
		else
		{
			pstmt =  dbFunc.getPreparedStmt(
					query
					+" where "+primkey+">="+startPosition
					+" and "+primkey+"<"+endPosition );
		}
		
		if ( questionMarks != null && !questionMarks.isEmpty() )
		{
			it = questionMarks.iterator();
			
			while ( it.hasNext() )
			{
				String o = (String) it.next();
				
//				try
//				{
					pstmt.setString( i, o );
					i += 1;
//				}
//				catch (SQLException e)
//				{
//					ErrorDialog ed = new ErrorDialog( parent, "An error occured while processing a PreparedStatement" );
//					ed.show( ed );
//				}
			}
		}
		
//		try
//		{
			rs = pstmt.executeQuery();
			
			if ( rs == null )
			{
				if ( this.isSwing )
				{
					ErrorDialog ed = new ErrorDialog( parent, "A PreparedStatement was not executed for unknown reasons." );
					ed.show( ed );
				} else {
					System.out.println( "A PreparedStatement was not executed for unknown reasons." );
				}
			}
//		}
//		catch (SQLException e)
//		{
//			ErrorDialog ed = new ErrorDialog( parent, "An error occured while executing a PreparedStatement" );
//			ed.show( ed );
//		}
		
		if ( rs != null ) {
			return rs;
		} else {
			return null;
		}
	} // ------- end of executePartOfAQueryWithResults - ResultSet -------
	
	/**
	 * Use this method if you have UTF-8 encoded Strings which a stored in a DB via
	 * ISO-LATIN-1 (ISO-8859-1).<br>
	 * That means, this method will give you the UTF-8 String back, which was automatically
	 * ISO encoded by the DB.
	 * 
	 * @param stringType The type from which to reencode. (ISO-8859-1 for example)
	 * @param toEncode The String to re-encode to UTF-8.
	 * @param isSwing True, if you run this method from Java.Swing, false if not.
	 * @return the re-encoded String
	 */	
	public String getStandardizedStringCatched( String stringType, String toEncode, boolean isSwing )
	{		
		try
		{
			String result = getStandardizedString( stringType, toEncode, isSwing );
			return result;
		}
		catch(Exception e)
		{
			if ( this.isSwing )
			{
				ErrorDialog ed = new ErrorDialog( parent, "An error occured while re-encoding a string:" + e.getMessage() );
				ed.show( ed );
			} else {
				System.out.println( "An error occured while re-encoding a string:" + e.getMessage() );
			}
		}
		
		return null;
	}
	
	public String getStandardizedString( String stringType, String toEncode, boolean isSwing )
		throws Exception	//Exception: this method
	{
		String result = null;
		
		// This part of the method re-encodes from console...
		if ( !isSwing )
		{
			result = new String( toEncode.getBytes( stringType ) );			
		}
		// ...and this part from Swing.
		// I don't know why, but the getBytes-method for Swing need to be a little more 'complex'.
		else if ( isSwing )
		{
			result = new String( toEncode.getBytes( stringType ),"UTF-8" );		    
		}
		
		return result;
	} // ------- end of getStandardizedString -------
}
