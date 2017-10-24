package de.uni_leipzig.asv.toolbox.jLanI.commonTable;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

public class CommonTableTest extends JFrame {

	private JTable commonTable;
	private TableSorter tableSorter;
	
	private int columnWidth;
	private int columns;
	
	private String[] header;
	
	private JPanel jContentPane = null;

	private JMenuBar jJMenuBar = null;

	private JMenu fileMenu = null;

	private JMenu editMenu = null;

	private JMenu helpMenu = null;

	private JMenuItem exitMenuItem = null;

	private JMenuItem aboutMenuItem = null;

	private JMenuItem cutMenuItem = null;

	private JMenuItem copyMenuItem = null;

	private JMenuItem pasteMenuItem = null;

	private JMenuItem saveMenuItem = null;
	
	private JMenuItem deleteSelectedItem = null;

	private JScrollPane jScrollPane = null;

	private JTable jTable = null;

	public void createTable( String[] header, Vector data )
	{
		int actDatum = 0;
		
		this.columns = header.length;
		this.header = header;
		
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
        commonTable.getColumnModel().getColumn( 0 ).setWidth( this.columnWidth );
        
        jScrollPane.getViewport().add( commonTable, null );
	}
	
	public Vector getTableData()
	{
		Vector result = new Vector();
		
		int rowCount = commonTable.getRowCount();
		int columnCount = commonTable.getColumnCount();
		
		for ( int i = 0; i < rowCount; i++ )
		{
			String[] value = new String[ columnCount ];
			
			for ( int j = 0; j < columnCount; j++ )
			{
				value[j] = new String();
				value[j] = (String) commonTable.getValueAt( i, j );
			}
			
			result.add( value );
		}
		
		return result;
	}
	
	public void deleteSelected()
	{
		String[] delItem;
		Vector v = this.getTableData();
		
		int rowCount = commonTable.getSelectedRowCount();
		int[] selectedRows = commonTable.getSelectedRows();		
		int columnCount = commonTable.getColumnCount();
		
		for ( int i = 0; i < rowCount; i++ )
		{
			delItem = new String[ columnCount ];
			
			for ( int j = 0; j < columnCount; j++ )
			{
				delItem[j] = new String();
				delItem[j] = (String) commonTable.getValueAt( selectedRows[i], j );
			}
			
			if ( v.contains( delItem ) ) {
				v.remove( delItem );
			}
		}
		
		this.addData( v );
		
		jScrollPane.add( commonTable, null );

		jScrollPane.setVisible(true);
		jScrollPane.repaint();
	}
	
	public void addData( Vector data )
	{
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
        commonTable.getColumnModel().getColumn( 0 ).setWidth( this.columnWidth );
        
        jScrollPane.add( commonTable, null );
	}
	
	public void setColumnWidth( int columnWidth )
	{
		this.columnWidth = columnWidth;
	}
	
	public void setHeader( String[] header )
	{
		this.header = header;
	}
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable();
		}
		return jTable;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommonTableTest application = new CommonTableTest();
		application.show();
	}

	/**
	 * This is the default constructor
	 */
	public CommonTableTest() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setJMenuBar(getJJMenuBar());
		this.setSize(439, 391);
		this.setContentPane(getJContentPane());
		this.setTitle("Application");
		
		this.setColumnWidth(50);
		
		Vector v = new Vector();
		
		String[] s1 = new String[2];
		s1[0] = new String();	s1[1] = new String();		
		s1[0] = "test1"; s1[1] = "1";
		v.add(s1);
		
		String[] s2 = new String[2];
		s2[0] = new String();	s2[1] = new String();
		s2[0] = "test2"; s2[1] = "2";
		v.add(s2);
		
		String[] s3 = new String[2];
		s3[0] = new String();	s3[1] = new String();
		s3[0] = "test3"; s3[1] = "3";
		v.add(s3);
		
		String[] s = new String[2];
		s[0] = new String();	s[1] = new String();
		s[0] = "header1"; s[1] = "header2";
		
		this.createTable( s, v);
		
		Vector test = this.getTableData();
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
		
		System.out.println("-------------------------------------------------------");
		
		commonTable.selectAll();
		this.deleteSelected();
		
		Vector test2 = this.getTableData();
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
		
		System.out.println("-------------------------------------------------------");
		
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getEditMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getSaveMenuItem());
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.add(getDeleteSelectedItem());
			editMenu.add(getCutMenuItem());
			editMenu.add(getCopyMenuItem());
			editMenu.add(getPasteMenuItem());
		}
		return editMenu;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new JDialog(CommonTableTest.this, "About", true).show();
				}
			});
		}
		return aboutMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getCutMenuItem() {
		if (cutMenuItem == null) {
			cutMenuItem = new JMenuItem();
			cutMenuItem.setText("Cut");
			cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					Event.CTRL_MASK, true));
		}
		return cutMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getCopyMenuItem() {
		if (copyMenuItem == null) {
			copyMenuItem = new JMenuItem();
			copyMenuItem.setText("Copy");
			copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					Event.CTRL_MASK, true));
		}
		return copyMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getPasteMenuItem() {
		if (pasteMenuItem == null) {
			pasteMenuItem = new JMenuItem();
			pasteMenuItem.setText("Paste");
			pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
					Event.CTRL_MASK, true));
		}
		return pasteMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getSaveMenuItem() {
		if (saveMenuItem == null) {
			saveMenuItem = new JMenuItem();
			saveMenuItem.setText("Save");
			saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					Event.CTRL_MASK, true));
		}
		return saveMenuItem;
	}
	
	private JMenuItem getDeleteSelectedItem() {
		if (deleteSelectedItem == null) {
			deleteSelectedItem = new JMenuItem();
			deleteSelectedItem.setText("Delete selected");
			//deleteSelectedItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
			//		Event.CTRL_MASK, true));
			
			final CommonTableTest thisPanel = this;
			deleteSelectedItem.addActionListener( new ActionListener()
					{
						public void actionPerformed( ActionEvent e )
						{
							thisPanel.deleteSelected();
							
							Vector test = thisPanel.getTableData();
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
						}
					}
					);
		}
		return saveMenuItem;
	}

}  //  @jve:decl-index=0:visual-constraint="189,22"
