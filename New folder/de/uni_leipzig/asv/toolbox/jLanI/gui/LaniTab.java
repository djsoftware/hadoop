package de.uni_leipzig.asv.toolbox.jLanI.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;

import de.uni_leipzig.asv.toolbox.jLanI.io.DB;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class LaniTab extends JSplitPane
{
	static JProgressBar pb;
	public static InputPane inputPane = new InputPane();
	public static OutputPane outputPane = new OutputPane();
	private JSplitPane laniPaneTop;
	private JPanel laniPaneBot;
	static private JButton startBA;
	static private JButton cancelBA;
	Thread prog;

	public LaniTab()
	{
	    init();
		addComponents();
	}

	private void init()
	{
		laniPaneTop = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,inputPane,outputPane);

		laniPaneBot = new JPanel();
		pb = new JProgressBar();
		pb.setStringPainted(true);
		startBA = new JButton("start");
		cancelBA = new JButton("cancel");
		setButtonsEnabled(true);

		startBA.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {start();}
        });
		cancelBA.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {stop();}
        });
	}

	private void addComponents()
	{
		laniPaneTop.setResizeWeight(0.485);
		laniPaneTop.setDividerSize(1);
		laniPaneTop.setEnabled(false);
	    laniPaneBot.setLayout(null);
		pb.setBounds(50,10,500,20); laniPaneBot.add(pb);
		startBA.setBounds(560,10,90,20); laniPaneBot.add(startBA);
		cancelBA.setBounds(660,10,90,20); laniPaneBot.add(cancelBA);
	
		setOrientation(JSplitPane.VERTICAL_SPLIT);
		setTopComponent(laniPaneTop);
		setBottomComponent(laniPaneBot);
		setResizeWeight(0.93);
		setDividerSize(1);
		setEnabled(false);
	}

	/**
	 * Fortschrittsbalken wird akualisiert
	 * @param i - neuer Wert
	 */
    public static void progress(int i)
    {
	    pb.setValue(i);
    }

	private void start()
	{
		setButtonsEnabled(false);
		try
		{
			if(inputFieldsCompleted() && outputFieldsCompleted())
			{
				prog = new CalculateThread();
				prog.start();
				DB.getInstance().updateProperties();
			} else {
				setButtonsEnabled(true);
			}
		}catch (Exception e){e.printStackTrace();}
	}

	private void stop()
	{
		try
		{
			prog.interrupt();
		}catch (Exception e){e.printStackTrace();}
		finally {setButtonsEnabled(true);}
	}

	public static void setButtonsEnabled(boolean en)
	{
		startBA.setEnabled(en);
		cancelBA.setEnabled(!en);
	}

	private boolean inputFieldsCompleted()
	{
		if(inputPane.textinputRB.isSelected() &&
		   inputPane.textinputTA.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "The field 'Text input' must be completed!", "Warning", JOptionPane.WARNING_MESSAGE); 
			return false;
		}
		else if(inputPane.fileinputRB.isSelected() &&
		   inputPane.fileinputFCP.fileTF.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "The field 'File input' must be completed!", "Warning", JOptionPane.WARNING_MESSAGE); 
			return false;
		}
		else if(inputPane.dbinputRB.isSelected() &&
			    (inputPane.dbinputTableTF.getText().equals("") ||
			     inputPane.dbinputSentTF.getText().equals("") ||
			     inputPane.dbinputIdTF.getText().equals("")))
		{
			JOptionPane.showMessageDialog(null, "The fields 'Table name', 'ID' and 'Sentence' must be completed!",
												"Warning", JOptionPane.WARNING_MESSAGE); 
			return false;
		}
		return true;
	}

	private boolean outputFieldsCompleted()
	{
		boolean complete = true;
		if(outputPane.fileoutputCB.isSelected() &&
		   outputPane.fileoutputFCP.fileTF.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "The field 'File output' must be completed!", "Warning", JOptionPane.WARNING_MESSAGE); 
			complete = false;
		}
		if(outputPane.dboutputCB.isSelected() && !inputPane.dbinputRB.isSelected() &&
		   (outputPane.dboutputTableTF.getText().equals("") ||
			outputPane.dboutputSentTF.getText().equals("") ||
			outputPane.dboutputIdTF.getText().equals("") ||
			outputPane.dboutputLang1TF.getText().equals("") ||
			outputPane.dboutputProb1TF.getText().equals("") ||
			outputPane.dboutputLang2TF.getText().equals("") ||
			outputPane.dboutputProb2TF.getText().equals("")))
		{
			JOptionPane.showMessageDialog(null, "All DB output fields must be completed!",
												"Warning", JOptionPane.WARNING_MESSAGE); 
			complete = false;
		}
		if(outputPane.dboutputCB.isSelected() && inputPane.dbinputRB.isSelected() &&
		   (outputPane.dboutputLang1TF.getText().equals("") ||
			outputPane.dboutputProb1TF.getText().equals("") ||
			outputPane.dboutputLang2TF.getText().equals("") ||
			outputPane.dboutputProb2TF.getText().equals("")))
		{
			JOptionPane.showMessageDialog(null, "The fields 'Language1', 'Probability1', 'Language2', 'Probability2' must be completed!",
												"Warning", JOptionPane.WARNING_MESSAGE); 
			complete = false;
		}
		return complete;
	}
}
