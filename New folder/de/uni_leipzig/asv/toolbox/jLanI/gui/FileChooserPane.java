package de.uni_leipzig.asv.toolbox.jLanI.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.uni_leipzig.asv.toolbox.util.commonFileChooser.CommonFileChooser;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class FileChooserPane extends JPanel
{ 
	/** Dateieingabefeld */
	final JTextField fileTF = new JTextField(15);
	/** Search-Button */
	final JButton fileBA;
	/** Dateiauswahldialogname */
	final String label;
	/** Bezeichnung der anzuzeigenden Dateien */
	final String extDesc;
	/** Extensionen der anzuzeigenden Dateien */
	final String[] ext;
	
	/**
	 * @param label - Dateiauswahldialogname
	 * @param buttonName - Search-Button-Name
	 * @param ext - Extensionen der anzuzeigenden Dateien
	 * @param extDesc - Bezeichnung der anzuzeigenden Dateien
	 */
	public FileChooserPane(String label, String buttonName, String[] ext, String extDesc)
	{
		this.label = label;
		this.ext = ext;
		this.extDesc = extDesc;
		fileBA = new JButton(buttonName);
	    init();
	}

	private void init()
	{
	    setLayout(null);
		final CommonFileChooser fc = new CommonFileChooser(ext, extDesc);
		final JPanel parent = this;

		fileBA.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
				try
				{
					String fileName = fc.showDialogAndReturnFilename(parent, label);
					if(fileName != null) {
						fileTF.setText(fileName);
					}
				}catch (Exception e){e.printStackTrace();}
            }
        });

		fileTF.setBounds(0,0,170,20); add(fileTF);
		fileBA.setBounds(180,0,90,20); add(fileBA);
	}
}
