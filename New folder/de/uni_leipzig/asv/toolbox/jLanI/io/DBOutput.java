package de.uni_leipzig.asv.toolbox.jLanI.io;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import de.uni_leipzig.asv.toolbox.jLanI.kernel.LangResult;
import de.uni_leipzig.asv.toolbox.util.commonDB.CommonDB;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class DBOutput implements Output
{
	private static CommonDB cdb;
	private String tabName;
	private String sentCol;
	private String idCol;
	private String lang1Col;
	private String prob1Col;
	private String lang2Col;
	private String prob2Col;
	private boolean dbInput;
	private int rowCount=0;

	public DBOutput(String[] dbValues, String tabName, String idCol, String sentCol, 
					String lang1Col, String prob1Col, String lang2Col, String prob2Col) throws SQLException
	{
		cdb = DB.getInstance().getCommonDB(dbValues);
		
		this.tabName = tabName;
		this.sentCol = sentCol;
		this.idCol = idCol;
		this.lang1Col = lang1Col;
		this.prob1Col = prob1Col;
		this.lang2Col = lang2Col;
		this.prob2Col = prob2Col;
		dbInput = false;

		checkTable(); checkColumns();

		rowCount = cdb.getHighestPrimaryKey(tabName, idCol);
	}

	public DBOutput(String tabName, String idCol,
					String lang1Col, String prob1Col, String lang2Col, String prob2Col) throws SQLException
	{
		cdb = DB.getInstance().getCommonDB();
		this.tabName = tabName;
		this.idCol = idCol;
		this.lang1Col = lang1Col;
		this.prob1Col = prob1Col;
		this.lang2Col = lang2Col;
		this.prob2Col = prob2Col;
		dbInput = true;

		checkTable(); checkColumns();
	}

	public void writeSent(LangResult res, String id, String sent)  throws Exception
	{
                
		sent = sent.replaceAll("[']","\\\\'");
                
                
		String query = "";
                Vector params=new Vector();
		if (dbInput)                 
		{
                      
                      params.add(res.getLang1());
                                       
                      params.add(""+res.getProb1());
                      
                      params.add(res.getLang2());
                       
                      params.add(""+res.getProb2());
                      
                      params.add(id);
       
                      query = "update "+tabName+" set "+lang1Col+"=?, "+prob1Col+"=?,"+lang2Col+"=?, "+prob2Col+"=? where "+idCol+"=?";
											 
		}
		else
		{
                    rowCount++;

  
                      params.add(""+rowCount);
                      params.add(sent);
                      params.add(res.getLang1());                
                      params.add(""+res.getProb1());
                      params.add(res.getLang2()); 
                      params.add(""+res.getProb2());
                    
                      query = "insert into "+tabName+" ("+idCol+","+sentCol+","+lang1Col+","+prob1Col+","+lang2Col+","+prob2Col+") values (?,?,?,?,?,?)";
				
		}
//System.out.println(query);
		cdb.executeQueryWithoutResults(query, params);

	}

	public void close()
	{
	}


	private void checkTable() throws SQLException
	{
		ResultSet rs = cdb.executeQueryWithResults_ResultSet("show tables", null);
		while(rs.next())
		{
			if(tabName.equals(rs.getString(1))) {
				return;
			}
		}
		createTable();
	}

	private void checkColumns() throws SQLException
	{
		Vector columns = cdb.executeQueryWithResultsCatched("show columns from "+tabName, null);
		if(!dbInput && !existsColumn(sentCol,columns)) {
			cdb.executeQueryWithoutResultsCatched("ALTER TABLE "+tabName+" ADD "+sentCol+" TEXT", null);
		}	
		if(!existsColumn(lang1Col,columns)) {
			cdb.executeQueryWithoutResultsCatched("ALTER TABLE "+tabName+" ADD "+lang1Col+" varchar(10)", null);
		}	
		if(!existsColumn(prob1Col,columns)) {
			cdb.executeQueryWithoutResultsCatched("ALTER TABLE "+tabName+" ADD "+prob1Col+" smallint(3)", null);
		}
		if(!existsColumn(lang2Col,columns)) {
			cdb.executeQueryWithoutResultsCatched("ALTER TABLE "+tabName+" ADD "+lang2Col+" varchar(10)", null);
		}	
		if(!existsColumn(prob2Col,columns)) {
			cdb.executeQueryWithoutResultsCatched("ALTER TABLE "+tabName+" ADD "+prob2Col+" smallint(3)", null);
		}	
	}

	private boolean existsColumn(String column, Vector columns)  throws SQLException
	{
		for (int i=0; i<columns.size(); i++)
		{
			String[] col = (String[])columns.get(i);
			if(column.equals(col[0])) {
				return true;
			}
		}
		return false;

           
       }

	private void createTable() throws SQLException
	{
		String query = "CREATE TABLE "+tabName+" ("+
						idCol+" int(11) NOT NULL auto_increment, "+
						sentCol+"  text default NULL, "+
						lang1Col+" varchar(10) default NULL, "+
						prob1Col+" smallint(3) default '0', "+
						lang2Col+" varchar(10) default NULL, "+
						prob2Col+" smallint(3) default '0', "+
						"PRIMARY KEY ("+idCol+"))";
		cdb.executeQueryWithoutResults(query, null);
	}

}