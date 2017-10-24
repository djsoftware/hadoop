package de.uni_leipzig.asv.toolbox.jLanI.commonTable;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * This class is to specifie an standardized JTable includet in an JScrollPane.
 * It comes with some usefull methods for the creation and modification of a Table.
 * The table itself is sortable by using a class named TableSorter (external). 
 * 
 * @author Daniel Zimmermann
 */
public class CommonTable extends JScrollPane
{
	private static final long serialVersionUID = 1L;
	
	private JTable commonTable;
	private TableSorter tableSorter;
	
//	private int columnWidth;
	private int columns;
	
	private String[] header;

	private TableCellRenderer cellRend;
	private DefaultCellEditor cellEdit;
	private boolean langTab;
	private int[] colWidth;
	
	/**
	 * Standart constructor for this class.
	 */
	public CommonTable()
	{
		
	}
	
	/**
	 * Standart constructor which creates a JScrollPane and a sortable JTable.
	 * 
	 * @param header A String[]-array for the column-headers.
	 * @param data A Vector wich contains String[]-arrays with the content of the table.
	 */
	public CommonTable( String[] header, int[] colWidth, Vector data, 
						TableCellRenderer cellRend, DefaultCellEditor cellEdit, boolean langTab)//, int columnWidth ) // der selbe Vektor wie aus commonDB
	{
		this.cellRend = cellRend;
		this.cellEdit = cellEdit;
		this.langTab = langTab;
		this.colWidth = colWidth;
		/*
		 * if there are data & a header available, fill the table both
		 */
		if ( (this.header != null || header != null) && data != null )
		{
			int actDatum = 0;		
			this.header = header;
			this.columns = this.header.length;					
			String rows[][] = new String[ data.size() ][ columns ];		
			Iterator it = data.iterator();		
//			this.columnWidth = columnWidth;
		
			while ( it.hasNext() )
			{
				String[] o = (String[]) it.next();
				int l = o.length;
			
				for ( int i = 0; i < l; i++ )
				{
					rows[ actDatum ][i] = o[i];
				}
			
				actDatum += 1;
			}
			
			DefaultTableModel model = new DefaultTableModel( rows , this.header );
			tableSorter = new TableSorter( model );
			commonTable = new JTable( tableSorter );
        
			tableSorter.setTableHeader( commonTable.getTableHeader() );
//        	commonTable.getColumnModel().getColumn( 0 ).setWidth( this.columnWidth );

			setCellRendEdit(commonTable, tableSorter);
			this.getViewport().add( commonTable, null );
		}
		/*
		 * if there is only a header available, create a table with a header, but w/o data
		 */
		else if ( data == null && (this.header != null || header != null))
		{
			if ( header != null ) {
				this.header = header;
			}
			
			this.columns = this.header.length;
			
			DefaultTableModel model = new DefaultTableModel( null , this.header );
			tableSorter = new TableSorter( model );
	        commonTable = new JTable( tableSorter );
	        
	        tableSorter.setTableHeader( commonTable.getTableHeader() );
			
			setCellRendEdit(commonTable, tableSorter);
	        this.getViewport().add( commonTable, null );
		}
	}

	private void setCellRendEdit(JTable tab, TableSorter tableSorter) 
	{
		if (langTab)
		{
			tableSorter.langTab = langTab;
			TableColumn tc = tab.getColumn(header[1]);
			tc.setCellRenderer(cellRend);
			tc.setCellEditor(cellEdit); 
		}
		else
		{
			tab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableColumn tc = tab.getColumn(header[0]);
			tc.setPreferredWidth(colWidth[0]);
			tc = tab.getColumn(header[1]);
			tc.setPreferredWidth(colWidth[1]);
		}
	}

	
	/**
	 * A method that creates a JScrollPane and a sortable JTable, if not done via the constructor.
	 * 
	 * @param header A String[]-array for the column-headers.
	 * @param data A Vector wich contains String[]-arrays with the content of the table.
	 */
	public void createTable( String[] header, Vector data )
	{
		if ( this.header == null && header == null) {
			return;
		} else if ( data == null )	// this is to set the tables header, if available
		{
			if ( header != null ) {
				this.header = header;
			}
			
			this.columns = this.header.length;
			
			DefaultTableModel model = new DefaultTableModel( null , this.header );
			tableSorter = new TableSorter( model );
	        commonTable = new JTable( tableSorter );
	        
	        tableSorter.setTableHeader( commonTable.getTableHeader() );
	        
			setCellRendEdit(commonTable, tableSorter);
			this.getViewport().add( commonTable, null );
			
			return;
		}
		
		/*
		 * if there are any data available, then fill the table
		 */
		if ( header != null ) {
			this.header = header;
		}
		
		int actDatum = 0;		
		this.columns = this.header.length;
		String rows[][] = new String[ data.size() ][ columns ];		
		Iterator it = data.iterator();
		
		while ( it.hasNext() )
		{
			String[] o = (String[]) it.next();
			int l = o.length;
			
			for ( int i = 0; i < l; i++ )
			{
				rows[ actDatum ][i] = o[i];
			}
			
			actDatum += 1;
		}
			
		DefaultTableModel model = new DefaultTableModel( rows , this.header );
		tableSorter = new TableSorter( model );
        commonTable = new JTable( tableSorter );
			
        tableSorter.setTableHeader( commonTable.getTableHeader() );
//      commonTable.getColumnModel().getColumn( 0 ).setWidth( this.columnWidth );

		setCellRendEdit(commonTable, tableSorter);
        this.getViewport().add( commonTable, null );
	}
	
//	public void setColumnWidth( int columnWidth )
//	{
//		this.columnWidth = columnWidth;
//	}
	
	/**
	 * This method sets the header.<br>
	 * (only used, if you want to create a new table or you will add data, soon)
	 * 
	 * @param header A String[]-array for the column-headers.
	 */
	public void setHeader( String[] header )
	{
		this.header = header;
		this.columns = this.header.length;
	}
	
	/**
	 * Adds a Vector with data (String[]-array) to the table - used when the table was modified.<br>
	 * Use createTable before you addData. 
	 * 
	 * @param data 
	 */
	public void addData( Vector data )
	{
		if ( data == null ) {
			return;
		}
		
		int actDatum = 0;		
		String rows[][] = new String[ data.size() ][ columns ];		
		Iterator it = data.iterator();
		
		while ( it.hasNext() )
		{
			String[] o = (String[]) it.next();			
			int l = o.length;
			
			for ( int i = 0; i < l; i++ )
			{
				rows[ actDatum ][i] = o[i];
			}
			
			actDatum += 1;
		}
			
		DefaultTableModel model = new DefaultTableModel( rows , this.header );
		tableSorter = new TableSorter( model );
        commonTable = new JTable( tableSorter );
        
        tableSorter.setTableHeader( commonTable.getTableHeader() );
//        commonTable.getColumnModel().getColumn( 0 ).setWidth( this.columnWidth );
        
		setCellRendEdit(commonTable, tableSorter);
		this.getViewport().add( commonTable, null );
	}
	
	/**
	 * This method cann add a single row to the table.
	 * 
	 * @param row The row you want to add to the table.
	 */
	public void addRow( String[] row )
	{
		if ( row == null ) {
			return;
		}
		
		Vector v = this.getTableData();				// get the tables content
		if ( v == null ) {
			v = new Vector();
		}
		
		v.add( row );								// add the new row to the Vector
		
		this.addData( v );							// add this 'new' Vector to the table
		
		this.getViewport().add( commonTable, null );

		this.setVisible(true);
		this.repaint();
	}
	
	/**
	 * This method remove the selected rows in the JTable.
	 */
	public void deleteSelected()
	{
		String[] delItem;									// temporary Item to delete
		Vector v = this.getTableData();						// the content of the table
		Iterator it = v.iterator(); 						// the iterator for the content
		int rowCount = commonTable.getSelectedRowCount();	// how many rows are selected?
		int[] selectedRows = commonTable.getSelectedRows();	// which rows are the selected?
		int columnCount = commonTable.getColumnCount();		// how many columns do the table have?
		
		int tmp = 0;				// to delete from the Vector via Index-No.
		boolean deleteThis = false;	// used to remember, if the last 'line' have to be deleted
		
		if ( v == null ) {
			return;	// would be senseless to go on, with no data...
		}
		
		/*
		 * watch the seleted rows
		 */
		for ( int i = 0; i < rowCount; i++ )
		{
			delItem = new String[ columnCount ];
			
			/*
			 * get the marked row now...
			 */
			for ( int j = 0; j < columnCount; j++ )
			{
				delItem[j] = new String();
				Object val = commonTable.getValueAt( selectedRows[i], j );
				if(val instanceof String) {
					delItem[j] = (String) val;
				} else {
					delItem[j] = val.toString();
//				System.out.println(selectedRows[i]);
				}
			}
			
//			System.out.println(v.indexOf(delItem));
			
//			v.remove( delItem );	// it is not so easy - what a pitty!
			
			// reset the tmp-value
			tmp = 0;			
			
			/*
			 * ...find out which one is the right one...
			 */
			while ( it.hasNext() )
			{
				String[] test = (String[]) it.next();
				
				for ( int k = 0; k < columnCount; k++ )
				{
					if ( test[k].equals(delItem[k]) ) {
						deleteThis = true;	// if one datum to delete equals an entry, mark this entry as to remove
					} else
					{
						deleteThis = false;	// if the datum to delete does not equals an entry in one partikular value, it schould not be removed!
						
						break;				// to secure that it will not be removed and because it is not neccessary anymore to check the rest of the entries
											// (if one not equals, it is not the right entry), we can break this loop now					
					}
//					System.out.println(deleteThis);
				}
				
				/*
				 * ...and remove it!
				 */
				if ( deleteThis == true )
				{
//					System.out.println("columnCount="+columnCount+" | deleteThis="+deleteThis);
					v.remove(tmp);
					tmp -= 1;			// lower index by 1 - elsewise you would get an indexOutOfBounds...
										// you have to do this, because you habe changed the size of the Vector!
					it = v.iterator();	// get the iterator on the 'new' Vector - I'm not sure if this is neccessary, but - just for security
					deleteThis = false;	// reset this value
				
					break;				// if the item to delete was already found, it would be a waste of the CPUs capacity to go on! 
				}
				
				
				deleteThis = false;
				tmp += 1;				// next index
			}
		}
		
		this.addData( v );				// add this 'new' Vector to the table (overwrite-process)
		
		setCellRendEdit(commonTable, tableSorter);
		this.getViewport().add( commonTable, null );

		this.setVisible(true);
		this.repaint();
	}
	
	/**
	 * This methods returns the data of the JTable.
	 * 
	 * @return the Vector that contains all the data from the JTable (String[]-arrays)
	 */
	public Vector getTableData()
	{
		if ( commonTable == null ) {
			return null;
		}
		
		Vector result = new Vector();		
		int rowCount = commonTable.getRowCount();
		int columnCount = commonTable.getColumnCount();
		
		for ( int i = 0; i < rowCount; i++ )
		{
			String[] value = new String[ columnCount ];
			
			for ( int j = 0; j < columnCount; j++ )
			{
				value[j] = new String();
				Object val = commonTable.getValueAt( i, j );
				if(val instanceof String) {
					value[j] = (String) val;
				} else {
					value[j] = ((Boolean)val).toString();
				}
			}
			
			result.add( value );
		}
		
		return result;
	}

	public JTable getTable() {return commonTable;}
}
