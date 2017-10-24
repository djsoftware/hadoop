package de.uni_leipzig.asv.toolbox.util.commonDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

/**
 * This class organizes the DB specific Functions.
 * You can establish a connection to a DB, execute queries and finaly close the connection.
 * 
 * @author Daniel Zimmermann, Torsten Thalheim, Volker Boehlke
 */
public class DBFunctions
{
	private JFrame parent;
	
	private Connection con = null;
        private static boolean loaded=false;
	// Objekt zum Ausfuehren von Queries
	private Statement stmt = null;
	
	private boolean isSwing = false; // look for the CommonDB to get some help;
	
	/**
	 * This default constructor establishes a connection to a DB.
	 * 
	 * @param driverClass the driver class needed to connect to the DB
	 * @param dbUrl the URL of the DB you want to connect with
	 * @param userid the user who have to connect to the DB
	 * @param passwd the users password - witout it - no connection
	 */
	public DBFunctions(String driverClass, String dbUrl, String userid, String passwd, boolean isSwing )
	{
		this.isSwing = isSwing;
		if (!loaded) {
                    loadDriver(driverClass);
                    loaded=true;
                }
		this.con = establishConnection(dbUrl, userid, passwd);
	}
	
	public void setIsSwing( boolean isSwing )
	{
		this.isSwing = isSwing;
	}
	
	public boolean getIsSwing()
	{
		return isSwing;
	}
	
	/**
	 * Loads the driver for the DB.
	 * 
	 * @param driverClass the driver to load
	 */
	private void loadDriver(String driverClass)
	{
		try 
		{			
			Class.forName(driverClass); //load db driver class		
		}
		catch (Exception e)
		{
			if ( this.isSwing )
			{
				ErrorDialog ed=new ErrorDialog(parent, "Couldn't load the jdbc-driver.\n  Please check your CLASSPATH.");
				ed.show(ed);
			} else {
				System.out.println( "Couldn't load the jdbc-driver.\n  Please check your CLASSPATH." );
			}
			
			//e.printStackTrace();
			//System.err.println("Couldn't load the jdbc-driver.\n" +
			//	"Please check your CLASSPATH.");
			//System.exit(1);
		}
	}

	/**
	 * This method finally establishes the connection to the database
	 * 
	 * @param dbUrl the URL to the DB
	 * @param userid the user its login
	 * @param passwd the password of the user
	 * @return the connection to a DB
	 */
	private Connection establishConnection(String dbUrl, String userid, String passwd)
	{
		// Verbindungsobjekt zur Datenbank
		Connection con = null;
				
		//  Verbindung herstellen
		try
		{
			con = DriverManager.getConnection(dbUrl, userid, passwd);
			// Query-Object fuer die aktuelle Verbindung erzeugen
			stmt = con.createStatement();
		}
		catch (Exception e)
		{
			if ( this.isSwing )
			{
				ErrorDialog ed=new ErrorDialog(parent, e.getMessage());
				ed.show(ed);
			} else {
				System.out.println( e.getMessage() );
			}
			
			//System.err.println(e.getMessage());
		}		
		return (con); 
	}
	
	public Connection getConnection()
	{
		return this.con;
	}
	
	/**
	 * This method is used for querys which returns a result, like the SELECT statements
	 * 
	 * @param query the SQL-query to execute on the DB 
	 * @return a ResultSet
	 */
	public ResultSet execStmt(String query)
	{
		// Enthaelt die Query-Ergebnisse
		ResultSet rs = null;
		try
		{
			// Query ausfuehren, Ergebnisse in rs
			rs = stmt.executeQuery(query);
		}
		catch(SQLException e)
		{
			if ( this.isSwing )
			{
				ErrorDialog ed=new ErrorDialog(parent, "An error occured while execute a SQL statement:  \n" + e.getMessage());
				ed.show(ed);
			} else {
				System.out.println( "An error occured while execute a SQL statement:  \n" + e.getMessage() );
			}
			
			//System.err.println("Fehler beim Ausfuehren einer Datenbankoperation aufgetreten:\n" + e.getMessage());
			//e.printStackTrace();
		}
		finally
		{
			try 
			{
				if (rs != null) {
					return(rs);
				}
			} 
			catch (Exception e) 
			{
				if ( this.isSwing )
				{
					ErrorDialog ed=new ErrorDialog(parent, e.getMessage());
					ed.show(ed);
				} else {
					System.out.println( e.getMessage() );
				//System.err.println(e.getMessage());
				}
			};
		}
		return(null);
	}

	/*
	public ResultSet execNewStmt(String query)
	{
		// Enthaelt die Query-Ergebnisse
		ResultSet rs = null;
		try
		{
			// Query ausfuehren, Ergebnisse in rs
			stmt.close();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
		}
		catch(SQLException e)
		{
			System.err.println("Fehler beim Ausfuehren einer Datenbankoperation aufgetreten:\n" + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if (rs != null) return(rs);
			} 
			catch (Exception e) 
			{
				System.err.println(e.getMessage());
			};
		}
		return(null);
	}
	*/
	
	/**
	 * This method can perform queries which havn't a result, such as INSERT statements.
	 * 
	 * @param query the INSERT query
	 */
	public int insertStmt(String query)
	{
		//System.out.println(query);
		int rows=0;
		try
		{
			// Query ausfuehren, Ergebnisse in rs
			rows=stmt.executeUpdate(query);
		}
		catch(SQLException e)
		{
			if ( this.isSwing )
			{
				ErrorDialog ed=new ErrorDialog(parent, "An error occured while execute a SQL statement:  \n" + e.getMessage());
				ed.show(ed);
			} else {
				System.out.println( "An error occured while execute a SQL statement:  \n" + e.getMessage() );
			}
			
			//System.err.println("Fehler beim Ausfuehren einer Datenbankoperation aufgetreten:\n" + e.getMessage());
			//e.printStackTrace();
			rows=0;
		}
		return(rows);
	}
	
	/**
	 * This method is to commit statements, when you've swiched of the auto-commit.
	 *
	 */
	public void Commit()
	{
		try
		{
			con.commit();
		}
		catch(Exception e)
		{
			if ( this.isSwing )
			{
				ErrorDialog ed=new ErrorDialog(parent, e.getMessage());
				ed.show(ed);
			} else {
				System.out.println( e.getMessage() );
			//System.err.println(e.getMessage());
			}
		}
	}
	
	/**
	 * This method is to specifie whether or not you want to use auto-commit.
	 * 
	 * @param b true if you want to use auto-commit, else false
	 */
	public void setAutoCommit(boolean b)
	{
		try
		{
			con.setAutoCommit(b);
		}
		catch(Exception e)
		{
			if ( this.isSwing )
			{
				ErrorDialog ed=new ErrorDialog(parent, e.getMessage());
				ed.show(ed);
			} else {
				System.out.println( e.getMessage() );
			//System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * When auto-commit is switched off, you can do a rollback in the case of some errors...
	 *
	 */
	public void rollBack()
	{
		try
		{
			con.rollback();
		}
		catch(Exception e)
		{
			if ( this.isSwing )
			{
				ErrorDialog ed=new ErrorDialog(parent, e.getMessage());
				ed.show(ed);
			} else {
				System.out.println( e.getMessage() );
			//System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * This method finally closes the connection to a DB.
	 *
	 */
	public void closeCon()
	{
		try
		{
			if (stmt != null) {
				stmt.close();
			}
			if (con != null) {
				con.close();
			}
		}
		catch (Exception e)
		{
			if ( this.isSwing )
			{
				ErrorDialog ed=new ErrorDialog(parent, e.getMessage());
				ed.show(ed);
			} else {
				System.out.println( e.getMessage() );
			//System.err.println(e.getMessage());
			}
		}		
	}
	
	/**
	 * If you want to use prepared statements instead of ordinary statements you can create one. 
	 * 
	 * @param query the query you want to save as a prepared statement
	 * @return the prepared statement, ready to use
	 */
	public PreparedStatement getPreparedStmt(String query)
	{
		PreparedStatement pstmt = null;
                
                //System.out.println("Preparing query:"+query);
                
		if(con!=null && query!=null)
		{
			try
			{
                                pstmt=con.prepareStatement(query);
			}
			catch (Exception e)
			{
				pstmt=null;
				
				if ( this.isSwing )
				{
					ErrorDialog ed=new ErrorDialog(parent, e.getMessage());
					ed.show(ed);
				} else {
					System.out.println( e.getMessage() );
				//System.err.println(e.getMessage());
				}
			}		
		}
		return(pstmt);
	}
}