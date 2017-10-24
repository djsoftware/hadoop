package de.uni_leipzig.asv.toolbox.jLanI.gui;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.uni_leipzig.asv.toolbox.jLanI.io.DB;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class DBTab extends JPanel
{
	// Datenbank-Eingabefelder
	public static JTextField dbinputDriverTF;
	public static JTextField dbinputProtTF;
	public static JTextField dbinputHostTF;
	public static JTextField dbinputDbTF;
	public static JTextField dbinputUserTF;
	public static JPasswordField dbinputPwTF;
	
	public DBTab()
	{
	    init();
	}

	private void init()
	{
		DB db = DB.getInstance();
		
	    setLayout(null);

		JLabel dbinputDriverLA = new JLabel("Driver");
		dbinputDriverTF = new JTextField(db.getProperty("jlani.driverClass"),15);
		JLabel dbinputProtLA = new JLabel("Protokol");
		dbinputProtTF = new JTextField(db.getProperty("jlani.dbProtokol"),15);
		JLabel dbinputHostLA = new JLabel("Host");
		dbinputHostTF = new JTextField(db.getProperty("jlani.dbHost"),15);
		JLabel dbinputDbLA = new JLabel("Database");
		dbinputDbTF = new JTextField(db.getProperty("jlani.dbDatabase"),15);
		JLabel dbinputUserLA = new JLabel("User");
		dbinputUserTF = new JTextField(db.getProperty("jlani.userID"),15);
		JLabel dbinputPwLA = new JLabel("Password");
		dbinputPwTF = new JPasswordField(15);
		dbinputPwTF.setText(db.getProperty("jlani.passWd"));

		dbinputDriverLA.setBounds(280,100,90,20); add(dbinputDriverLA);
		dbinputDriverTF.setBounds(380,100,140,20); add(dbinputDriverTF);
		dbinputProtLA.setBounds(280,140,90,20); add(dbinputProtLA);
		dbinputProtTF.setBounds(380,140,140,20); add(dbinputProtTF);
		dbinputHostLA.setBounds(280,180,90,20); add(dbinputHostLA);
		dbinputHostTF.setBounds(380,180,140,20); add(dbinputHostTF);
		dbinputDbLA.setBounds(280,220,90,20); add(dbinputDbLA);
		dbinputDbTF.setBounds(380,220,140,20); add(dbinputDbTF);
		dbinputUserLA.setBounds(280,260,90,20); add(dbinputUserLA);
		dbinputUserTF.setBounds(380,260,140,20); add(dbinputUserTF);
		dbinputPwLA.setBounds(280,300,90,20); add(dbinputPwLA);
		dbinputPwTF.setBounds(380,300,140,20); add(dbinputPwTF);
	}

	/** liefert eingegebene Werte der Datenbankfelder im DBTab */
	public static String[] getValues()
	{
		return new String[]{dbinputDriverTF.getText(), dbinputProtTF.getText(), dbinputHostTF.getText(),
							dbinputDbTF.getText(), dbinputUserTF.getText(), dbinputPwTF.getText()};
	}
}
