package de.uni_leipzig.asv.toolbox.util.commonDB;

import java.util.Iterator;
import java.util.Vector;

public class CommonDBTest
{
	
	public static void main(String[] args)
	{
		DBConnection dbcon = new DBConnection();
		
		dbcon.setDriverClass("com.mysql.jdbc.Driver");
		dbcon.setDbURL("jdbc:mysql://localhost:3306/db_test");
		dbcon.setUserID("root");
		dbcon.setPassWd("");
		
		// Tests w/o a queryfile - all passed
		CommonDB cdb = new CommonDB( dbcon, null, false );
		
		// insert-test - for of the queries, that will not have a result - passed
//		cdb.excecuteQueryWithoutResults( "insert into test values ('666','Max Mutzke',6660)", null );
		
		// get the whole table... - passed
		Vector test = cdb.executeQueryWithResultsCatched( "select * from test", null );		
		Iterator it = test.iterator();
		while ( it.hasNext() )
		{
			String[] array = (String[])it.next();
			int l = array.length;
			for ( int i = 0; i < l; i++ ) {
				System.out.println( "value["+i+"] = " + array[i] + " " );
			}
			System.out.println( "\n" );
		}
		
		// get the whole table, but this time using the method, that could also just give a little part as a result - passed
		String primkey = cdb.getPrimaryKeyNameCatched( "test" );
		System.out.println( "primkey = " + primkey +"\n" );
		// To get all, including the very last enty of the table, you have to add 1 to the endPosition!
		// But not during the normal run, only at the very last one!
		Vector test2 = cdb.executePartOfAQueryWithResultsCatched( "select * from test", null, cdb.getLowestPrimaryKeyCatched( "test", primkey ), cdb.getHighestPrimaryKeyCatched( "test", primkey ) + 1, primkey );
		Iterator it2 = test2.iterator();
		while ( it2.hasNext() )
		{
			String[] array = (String[])it2.next();
			int l = array.length;
			for ( int i = 0; i < l; i++ ) {
				System.out.println( "value["+i+"] = " + array[i] + " " );
			}
			System.out.println( "\n" );
		}
		
		// Tests with a queryfile - all passed
		cdb = new CommonDB( dbcon, "./test.query", true );
		
		// passed - but returns an error, when entry is allready there!
//		String id = "665";
//		String name = "The neighbour of the beast";
//		String no = "6650";
//		Vector v = new Vector();
//		v.add( id ); v.add( name ); v.add( no );
//		cdb.excecuteQueryWithoutResults( cdb.getQuery("test.query2"), v );
		
		// passed
		String id3 = "666";
		Vector v3 = new Vector();
		v3.add( id3 );
		Vector test3 = cdb.executeQueryWithResultsCatched( cdb.getQuery("test.query1"), v3 );
		Iterator it3 = test3.iterator();
		while ( it3.hasNext() )
		{
			String[] array = (String[])it3.next();
			int l = array.length;
			for ( int i = 0; i < l; i++ ) {
				System.out.println( "value["+i+"] = " + array[i] + " " );
			}
			System.out.println( "\n" );
		}
		
		// changing queryfile - passed
		System.out.println( cdb.getQueryFileName() );
		cdb.changeQueryFileCatched( "./test2.query" );
		System.out.println( cdb.getQueryFileName() );
		
		// ... and test - passed
		Vector test4 = cdb.executeQueryWithResultsCatched( cdb.getQuery("test2.query"), null );		
		Iterator it4 = test4.iterator();
		while ( it4.hasNext() )
		{
			String[] array = (String[])it4.next();
			int l = array.length;
			for ( int i = 0; i < l; i++ ) {
				System.out.println( "value["+i+"] = " + array[i] + " " );
			}
			System.out.println( "\n" );
		}
	}

}
