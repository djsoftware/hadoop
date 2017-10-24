package de.uni_leipzig.asv.toolbox.util.commonDB;

/**
 * This class is a placeholder for the DB-connectivity data.
 * 
 * @author Daniel Zimmermann 
 */
public class DBConnection
{
	private String driverClass;
	private String dbURL;
	private String userID;
	private String PassWd;
	
	/*
	 * I think, there is no need to explain getter- and setter-methods... 
	 */
	
	public String getDbURL() {
		return dbURL;
	}
	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getPassWd() {
		return PassWd;
	}
	public void setPassWd(String passWd) {
		PassWd = passWd;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}	
}
