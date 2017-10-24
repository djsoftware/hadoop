package de.uni_leipzig.asv.toolbox.jLanI.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;

import de.uni_leipzig.asv.toolbox.jLanI.io.DB;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class InputPane extends JPanel
{
	JRadioButton textinputRB;
	JTextArea textinputTA;
	JScrollPane textinputSP;

	JRadioButton fileinputRB;
	FileChooserPane fileinputFCP;
	
	JRadioButton dbinputRB;
	public JTextField dbinputTableTF;
	public JTextField dbinputSentTF;
	public JTextField dbinputIdTF;
	JLabel dbinputTableLA;
	JLabel dbinputSentLA;
	JLabel dbinputIdLA;
	SpinnerNumberModel dbinputIdMinSNM;
	SpinnerNumberModel dbinputIdMaxSNM;
    JSpinner dbinputIdMinSpin;
	JSpinner dbinputIdMaxSpin;
	JLabel dbinputIdMinLA;
	JLabel dbinputIdMaxLA;

	public InputPane()
	{
	    init();
		setTextInputEnabled(true);
		setFileInputEnabled(false);
		setDBInputEnabled(false);
		addComponents();
	}

	private void init()
	{
		textinputRB = new JRadioButton("Text input",true);
		textinputTA = new JTextArea(5,23);
		textinputTA.setLineWrap(true);
		textinputSP = new JScrollPane(textinputTA,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
															  ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		fileinputRB = new JRadioButton("File input",false);
		fileinputFCP = new FileChooserPane("select", "search", new String[]{"txt"}, "Text files");

		DB db = DB.getInstance();
		dbinputRB = new JRadioButton("DB input",false);
		dbinputTableLA = new JLabel("Table name");
		dbinputTableTF = new JTextField(db.getProperty("jlani.inputTable"),15);
		dbinputIdLA = new JLabel("ID");
		dbinputIdTF = new JTextField(db.getProperty("jlani.inputId"),15);
		dbinputSentLA = new JLabel("Sentence");
		dbinputSentTF = new JTextField(db.getProperty("jlani.inputSent"),15);
		
		dbinputIdMinSNM = new SpinnerNumberModel();
		dbinputIdMinSNM.setMinimum(new Integer(0));
		dbinputIdMaxSNM = new SpinnerNumberModel();
		dbinputIdMaxSNM.setMinimum(new Integer(0));
		dbinputIdMinSpin = new JSpinner(dbinputIdMinSNM);
		dbinputIdMaxSpin = new JSpinner(dbinputIdMaxSNM);
		dbinputIdMinLA = new JLabel("ID min");
		dbinputIdMaxLA = new JLabel("ID max");

		ButtonGroup bg = new ButtonGroup();
		bg.add(textinputRB);
		bg.add(fileinputRB);
		bg.add(dbinputRB);

		textinputRB.addActionListener(new RadioListener());
		fileinputRB.addActionListener(new RadioListener());
		dbinputRB.addActionListener(new RadioListener());
	}

	private void addComponents()
	{
	    setLayout(null);
		textinputRB.setBounds(10,10,90,20); add(textinputRB);
		textinputSP.setBounds(10,40,360,200); add(textinputSP);
		fileinputRB.setBounds(10,260,90,20); add(fileinputRB);
		fileinputFCP.setBounds(100,260,270,20); add(fileinputFCP);
		dbinputRB.setBounds(10,300,90,20); add(dbinputRB);
		dbinputTableTF.setBounds(100,300,170,20); add(dbinputTableTF);
		dbinputTableLA.setBounds(280,300,90,20); add(dbinputTableLA);
		dbinputIdTF.setBounds(100,330,170,20); add(dbinputIdTF);
		dbinputIdLA.setBounds(280,330,90,20); add(dbinputIdLA);
		dbinputSentTF.setBounds(100,360,170,20); add(dbinputSentTF);
		dbinputSentLA.setBounds(280,360,90,20); add(dbinputSentLA);
		dbinputIdMinSpin.setBounds(100,390,50,20); add(dbinputIdMinSpin);
		dbinputIdMinLA.setBounds(160,390,50,20); add(dbinputIdMinLA);
		dbinputIdMaxSpin.setBounds(220,390,50,20); add(dbinputIdMaxSpin);
		dbinputIdMaxLA.setBounds(280,390,50,20); add(dbinputIdMaxLA);
	}

	private void setTextInputEnabled(boolean en)
	{
		textinputTA.setEnabled(en); 
		if(en) {
			textinputTA.setBackground(Color.white);
		} else {
			textinputTA.setBackground(new Color(240,240,240));
		}
	}

	private void setFileInputEnabled(boolean en)
	{
		fileinputFCP.fileTF.setEditable(en); 
		fileinputFCP.fileTF.setEnabled(en);
		fileinputFCP.fileBA.setEnabled(en);
	}

	private void setDBInputEnabled(boolean en)
	{
		dbinputTableTF.setEditable(en); 
		dbinputTableTF.setEnabled(en);
		dbinputIdTF.setEditable(en); 
		dbinputIdTF.setEnabled(en);
		dbinputSentTF.setEditable(en); 
		dbinputSentTF.setEnabled(en);
		dbinputTableLA.setEnabled(en); 
		dbinputIdLA.setEnabled(en);
		dbinputSentLA.setEnabled(en);
		dbinputIdMinSpin.setEnabled(en);
		dbinputIdMaxSpin.setEnabled(en);
		dbinputIdMinLA.setEnabled(en);
		dbinputIdMaxLA.setEnabled(en);
	}

	private class RadioListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae) 
		{
			JRadioButton qrb = (JRadioButton)ae.getSource();
			if (qrb.equals(textinputRB))
			{
				setTextInputEnabled(true);
				setFileInputEnabled(false);
				setDBInputEnabled(false);
				LaniTab.outputPane.setDBInput(false);
			}
			else if (qrb.equals(fileinputRB))
			{
				setTextInputEnabled(false);
				setFileInputEnabled(true);
				setDBInputEnabled(false);
				LaniTab.outputPane.setDBInput(false);
			}
			else if (qrb.equals(dbinputRB))
			{
				setTextInputEnabled(false);
				setFileInputEnabled(false);
				setDBInputEnabled(true);
				LaniTab.outputPane.setDBInput(true);
			}
		}
	}
}