package de.uni_leipzig.asv.toolbox.jLanI.io;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Vector;

import de.uni_leipzig.asv.toolbox.jLanI.kernel.LanIKernel;
import de.uni_leipzig.asv.toolbox.util.commonDB.CommonDB;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class DBInput implements Input
{
	private CommonDB cdb;
	private Iterator sentences;
	private String idCol;
	private String sentCol;
	private String query;
	private String id;
	private int nextId;
	private int bufSize;
	private int idMin;
	private int idMax;
	private int size;
	private boolean part;
	
	public DBInput(String[] dbValues, String tabName, String idCol, String sentCol, Object imi, Object ima) throws Exception
	{
		this.idCol = idCol;	this.sentCol = sentCol;
		cdb = DB.getInstance().getCommonDB(dbValues);
		idMin = ((Integer)imi).intValue();
		idMax = ((Integer)ima).intValue();

		bufSize = Integer.parseInt(LanIKernel.getInstance().prefs.getProperty("BufferSize"));
	
		String sizeQuery = "select max("+idCol+") from "+tabName;
		query = "select "+idCol+","+sentCol+" from "+tabName;
             
		
		if(idMax>0 && idMin<=idMax)	
		{
			part = true;
			ResultSet sizeRS = cdb.executePartOfAQueryWithResults_ResultSet(sizeQuery, null, idMin, idMax+1, idCol);
			sizeRS.next(); 
			size = sizeRS.getInt(1);

			nextId = (bufSize < size) ? idMin+bufSize+1 : idMax+1;
			sentences = cdb.executePartOfAQueryWithResults(query, null, idMin, nextId, idCol).iterator();
		}
		else
		{
			part = false;
			ResultSet sizeRS = cdb.executeQueryWithResults_ResultSet(sizeQuery, null);
			sizeRS.next(); 
			size = sizeRS.getInt(1);

			nextId = idMin+bufSize+1;
                        Vector params=new Vector();
                        params.add(""+idMin);
                        params.add(nextId+"");
			sentences = cdb.executePartOfAQueryWithResults(query, params, -1, -1, idCol).iterator();
		}
	}
	
	public String readSent() throws Exception
	{
		String sent = null;
		if(!sentences.hasNext())
		{
                        
                        while((!sentences.hasNext())&&(nextId<size)) {
                             Vector params=new Vector();
                          params.add(nextId+"");
                          params.add(getNextId()+"");
			  sentences = cdb.executePartOfAQueryWithResults(query, params, -1, -1, idCol).iterator();
			  nextId += bufSize;
			  System.out.println("\nprocessing sentences until:"+nextId+"\n");
                        }
                        
		}
		if(sentences.hasNext())
		{
			String[] row = (String[])sentences.next();
			id = row[0]; sent = row[1];
		}
		return sent;
	}

	public String getId(){ return ""+id; }

	private int getNextId()
	{
		if(part) {
			return (nextId+bufSize > idMax+1) ? idMax+1 : nextId+bufSize;
		} else {
			return nextId+bufSize;
		}
	}

	public int getMaxProgress()
	{
		System.out.println("\n\nSIZE:"+size+"\n\n");
		return 	size;
	}

	public int getIncProgress(){ return 1; }
        
        public int getCurrentProgress() {
           return nextId;
        }
        
}
