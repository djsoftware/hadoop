package de.uni_leipzig.asv.toolbox.jLanI.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import de.uni_leipzig.asv.toolbox.jLanI.commonTable.CommonTable;
import de.uni_leipzig.asv.toolbox.jLanI.io.DB;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class OutputPane extends JPanel
{
	JCheckBox textoutputCB;
	public JLabel textoutputResLA;

	public CommonTable textoutputSP;
	JTable textoutputTA;

	JCheckBox fileoutputCB;
	FileChooserPane fileoutputFCP;
	
	JCheckBox dboutputCB;
	public JTextField dboutputTableTF;
	public JTextField dboutputSentTF;
	public JTextField dboutputIdTF;
	public JTextField dboutputLang1TF;
	public JTextField dboutputLang2TF;
	public JTextField dboutputProb1TF;
	public JTextField dboutputProb2TF;
	JLabel dboutputTableLA;
	JLabel dboutputSentLA;
	JLabel dboutputIdLA;
	JLabel dboutputLang1LA;
	JLabel dboutputLang2LA;
	JLabel dboutputProb1LA;
	JLabel dboutputProb2LA;

	private boolean dbInput = false;
	private boolean dbOutput = false;

	public OutputPane()
	{
	    init();
		setTextOutputEnabled(true);
		setFileOutputEnabled(false);
		setDBOutputEnabled(false);
		addComponents();
	}

	private void init()
	{
		textoutputCB = new JCheckBox("Text output",true);
		textoutputResLA = new JLabel();
		initOutputTable();

		fileoutputCB = new JCheckBox("File output",false);
		fileoutputFCP = new FileChooserPane("select", "search", new String[]{"txt"}, "Text files");

		DB db = DB.getInstance();
		dboutputCB = new JCheckBox("DB output",false);
		dboutputTableLA = new JLabel("Table name");
		dboutputTableTF = new JTextField(db.getProperty("jlani.outputTable"),15);
		dboutputIdLA = new JLabel("ID");
		dboutputIdTF = new JTextField(db.getProperty("jlani.outputId"),15);
		dboutputSentLA = new JLabel("Sentence");
		dboutputSentTF = new JTextField(db.getProperty("jlani.outputSent"),15);
		dboutputLang1LA = new JLabel("Language1");
		dboutputLang1TF = new JTextField(db.getProperty("jlani.outputLang1"),15);
		dboutputLang2LA = new JLabel("Language2");
		dboutputLang2TF = new JTextField(db.getProperty("jlani.outputLang2"),15);
		dboutputProb1LA = new JLabel("Probability1");
		dboutputProb1TF = new JTextField(db.getProperty("jlani.outputProb1"),15);
		dboutputProb2LA = new JLabel("Probability2");
		dboutputProb2TF = new JTextField(db.getProperty("jlani.outputProb2"),15);

		textoutputCB.addActionListener(new CheckListener());
		fileoutputCB.addActionListener(new CheckListener());
		dboutputCB.addActionListener(new CheckListener());
	}

	/** Beim Starten der Spracherkennung und ausgewähltem text output wird die output-Tabelle geleert */
	public void initOutputTable()
	{
		if(isAncestorOf(textoutputSP)) {
			remove(textoutputSP);
		}
		textoutputSP = new CommonTable(new String[]{"language","sentence"}, new int[]{90,400}, null, 
								 null, null, false);
		textoutputSP.setBounds(10,40,385,200); add(textoutputSP);
	}

	private void addComponents()
	{
	    setLayout(null);
		textoutputCB.setBounds(10,10,90,20); add(textoutputCB);
		textoutputResLA.setBounds(125,10,270,20); add(textoutputResLA);
		//textoutputSP.setBounds(10,40,385,200); add(textoutputSP);
		fileoutputCB.setBounds(10,260,90,20); add(fileoutputCB);
		fileoutputFCP.setBounds(125,260,270,20); add(fileoutputFCP);
		dboutputCB.setBounds(10,300,90,20); add(dboutputCB);
		dboutputTableTF.setBounds(125,300,170,20); add(dboutputTableTF);
		dboutputTableLA.setBounds(305,300,90,20); add(dboutputTableLA);
		dboutputIdTF.setBounds(125,330,170,20); add(dboutputIdTF);
		dboutputIdLA.setBounds(305,330,90,20); add(dboutputIdLA);
		dboutputSentTF.setBounds(125,360,170,20); add(dboutputSentTF);
		dboutputSentLA.setBounds(305,360,90,20); add(dboutputSentLA);
		dboutputLang1TF.setBounds(125,390,170,20); add(dboutputLang1TF);
		dboutputLang1LA.setBounds(305,390,90,20); add(dboutputLang1LA);
		dboutputProb1TF.setBounds(125,420,170,20); add(dboutputProb1TF);
		dboutputProb1LA.setBounds(305,420,90,20); add(dboutputProb1LA);
		dboutputLang2TF.setBounds(125,450,170,20); add(dboutputLang2TF);
		dboutputLang2LA.setBounds(305,450,90,20); add(dboutputLang2LA);
		dboutputProb2TF.setBounds(125,480,170,20); add(dboutputProb2TF);
		dboutputProb2LA.setBounds(305,480,90,20); add(dboutputProb2LA);
	}

	private void setTextOutputEnabled(boolean en)
	{
		textoutputTA = textoutputSP.getTable();
		textoutputTA.setEnabled(en); 
		if(en) {
			textoutputTA.setBackground(Color.white);
		} else {
			textoutputTA.setBackground(new Color(240,240,240));
		}
	}

	private void setFileOutputEnabled(boolean en)
	{
		fileoutputFCP.fileTF.setEditable(en); 
		fileoutputFCP.fileTF.setEnabled(en);
		fileoutputFCP.fileBA.setEnabled(en);
	}

	private void setDBOutputEnabled(boolean en)
	{
		dbOutput = en;
		if (!dbInput)
		{
			dboutputTableTF.setEditable(en); 
			dboutputTableTF.setEnabled(en);
			dboutputIdTF.setEditable(en); 
			dboutputIdTF.setEnabled(en);
			dboutputSentTF.setEditable(en); 
			dboutputSentTF.setEnabled(en);
			dboutputTableLA.setEnabled(en); 
			dboutputIdLA.setEnabled(en);
			dboutputSentLA.setEnabled(en);
		}
		dboutputLang1TF.setEditable(en); 
		dboutputLang1TF.setEnabled(en);
		dboutputLang2TF.setEditable(en); 
		dboutputLang2TF.setEnabled(en);
		dboutputProb1TF.setEditable(en); 
		dboutputProb1TF.setEnabled(en);
		dboutputProb2TF.setEditable(en); 
		dboutputProb2TF.setEnabled(en);
		dboutputLang1LA.setEnabled(en);
		dboutputLang2LA.setEnabled(en);
		dboutputProb1LA.setEnabled(en);
		dboutputProb2LA.setEnabled(en);
	}

	/** Bei ausgewähltem db input werden entsprechende db output Felder ausgeblendet */
	public void setDBInput(boolean dbin)
	{
		dbInput = dbin;
		if (dbOutput)
		{
			dboutputTableTF.setEditable(!dbin); 
			dboutputTableTF.setEnabled(!dbin);
			dboutputIdTF.setEditable(!dbin); 
			dboutputIdTF.setEnabled(!dbin);
			dboutputSentTF.setEditable(!dbin); 
			dboutputSentTF.setEnabled(!dbin);
			dboutputTableLA.setEnabled(!dbin); 
			dboutputIdLA.setEnabled(!dbin);
			dboutputSentLA.setEnabled(!dbin);
		}
	}

	private class CheckListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae) 
		{
			JCheckBox qcb = (JCheckBox)ae.getSource();
			if (qcb.equals(textoutputCB))
			{
				if(qcb.isSelected()) {
					setTextOutputEnabled(true);
				} else {
					setTextOutputEnabled(false);
				}
			}
			else if (qcb.equals(fileoutputCB))
			{
				if(qcb.isSelected()) {
					setFileOutputEnabled(true);
				} else {
					setFileOutputEnabled(false);
				}
			}
			else if (qcb.equals(dboutputCB))
			{
				if(qcb.isSelected()) {
					setDBOutputEnabled(true);
				} else {
					setDBOutputEnabled(false);
				}
			}
		}
	}
}