package de.uni_leipzig.asv.toolbox.jLanI.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import de.uni_leipzig.asv.toolbox.jLanI.commonTable.CommonTable;
import de.uni_leipzig.asv.toolbox.jLanI.io.DB;
import de.uni_leipzig.asv.toolbox.jLanI.kernel.LangManager;
import de.uni_leipzig.asv.toolbox.util.commonFileChooser.CommonFileChooser;

/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class NewLanguageTab extends JPanel {
	CommonTable langSP;

	JTable langTA;

	public int checkedRow = -1;

	JButton loadAllLangBA;

	JButton addFromFileBA;

	JButton activBA;

	JButton delBA;

	JButton addFromDbBA;

	public JTextField dbinputTableTF;

	public JTextField dbinputIdTF;

	public JTextField dbinputWordTF;

	public JTextField dbinputFreqTF;

	public JTextField dbinputLangTF;

	public JTextField langparamWC;

	public JTextField langparamCov;

	JLabel langparamWCLA;

	JLabel langparamCovLA;

	JLabel dbinputTableLA;

	JLabel dbinputIdLA;

	JLabel dbinputWordLA;

	JLabel dbinputFreqLA;

	JLabel dbinputLangLA;

	SpinnerNumberModel dbinputIdMinSNM;

	JSpinner dbinputIdMinSpin;

	JLabel dbinputIdMinLA;

	SpinnerNumberModel dbinputNumSNM;

	JSpinner dbinputNumSpin;

	JLabel dbinputNumLA;

	private static JProgressBar pb = new JProgressBar();

	private JButton cancelBA;

	private static NewLanguageTab langTab = null;

	private Thread t;

	private NewLanguageTab() {
		init();
		addComponents();
	}

	public synchronized static NewLanguageTab getInstance() {
		if (langTab == null) {
			langTab = new NewLanguageTab();
		}
		return langTab;
	}

	private void setButtonsEnabled(boolean en) {
		addFromFileBA.setEnabled(en);
		delBA.setEnabled(en);
		addFromDbBA.setEnabled(en);
	}

	private void init() {
		initLangTable(null);

		loadAllLangBA = new JButton("load all languages");
		addFromFileBA = new JButton("add from file");
		delBA = new JButton("delete");
		addFromDbBA = new JButton("add from DB");
		setButtonsEnabled(false);

		DB db = DB.getInstance();
		dbinputLangLA = new JLabel("Language");
		dbinputLangTF = new JTextField(db.getProperty("jlani.langLang"), 2);
		dbinputTableLA = new JLabel("Table name");
		dbinputTableTF = new JTextField(db.getProperty("jlani.langTable"), 15);
		dbinputIdLA = new JLabel("ID");
		dbinputIdTF = new JTextField(db.getProperty("jlani.langId"), 15);
		dbinputWordLA = new JLabel("Words");
		dbinputWordTF = new JTextField(db.getProperty("jlani.langWord"), 15);
		dbinputFreqLA = new JLabel("Frequency");
		dbinputFreqTF = new JTextField(db.getProperty("jlani.langFreq"), 15);
		langparamWCLA = new JLabel("Min word count");
		langparamWC = new JTextField(db.getProperty("jlani.minWordCount"), 5);
		langparamCovLA = new JLabel("% Min coverage");
		langparamCov = new JTextField(db.getProperty("jlani.minCoverage"), 5);

		dbinputIdMinSNM = new SpinnerNumberModel();
		dbinputIdMinSNM.setMinimum(new Integer(0));
		dbinputIdMinSNM.setValue(new Integer(101));
		dbinputIdMinSpin = new JSpinner(dbinputIdMinSNM);
		dbinputIdMinLA = new JLabel("ID min");
		dbinputNumSNM = new SpinnerNumberModel();
		dbinputNumSNM.setMinimum(new Integer(0));
		dbinputNumSNM.setValue(new Integer(2000));
		dbinputNumSpin = new JSpinner(dbinputNumSNM);
		dbinputNumLA = new JLabel("Words number");

		pb.setStringPainted(true);
		cancelBA = new JButton("cancel");
		cancelBA.setEnabled(false);
		cancelBA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				stop();
			}
		});

		loadAllLangBA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				t = new Thread(new Runnable() {
					public void run() {
						loadAllLanguages();
					}
				});
				t.start();
			}
		});

		addFromFileBA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				t = new Thread(new Runnable() {
					public void run() {
						setButtonsEnabled(false);
						try {
							addFromFile();
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							setButtonsEnabled(true);
							cancelBA.setEnabled(false);
						}
					}
				});
				t.start();
			}
		});

		delBA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				t = new Thread(new Runnable() {
					public void run() {
						setButtonsEnabled(false);
						try {
							delete();
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							setButtonsEnabled(true);
						}
					}
				});
				t.start();
			}
		});

		addFromDbBA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				t = new Thread(new Runnable() {
					public void run() {
						setButtonsEnabled(false);
						try {
							addFromDb();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "DB Error!",
									"Error", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						} finally {
							setButtonsEnabled(true);
							cancelBA.setEnabled(false);
						}
					}
				});
				t.start();
			}
		});
	}

	private void initLangTable(Vector data) {
		if (isAncestorOf(langSP)) {
			remove(langSP);
		}

		JCheckBox aktivCB = new JCheckBox();
		aktivCB.addActionListener(new CheckListener());
		langSP = new CommonTable(new String[] { "languages", "active",
				"# words" }, new int[] { 0, 0 }, data, new LangCellRenderer(),
				new DefaultCellEditor(aktivCB), true);

		langSP.setBounds(170, 20, 180, 440);
		add(langSP);
	}

	/** Die Tabelle im Language Tab wird mit vorhandenen Sprachen gefÃÂÃÂllt */
	public void loadAllLanguages() {
		loadAllLangBA.setEnabled(false);
		try {
			HashMap allLanguages = LangManager.getAllLanguages();
			pb.setMaximum(allLanguages.size());
			Set allLangNames = allLanguages.keySet();
			Iterator it = allLangNames.iterator();
			int i = 0;
			while (it.hasNext()) {
				String lang = (String) it.next();
				langSP.addRow(new String[] { lang, "true",
						allLanguages.get(lang).toString() });
				progress(++i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setButtonsEnabled(true);
		}
	}

	private void addFromFile() throws Exception {
		CommonFileChooser fc = new CommonFileChooser(new String[] { "txt" },
				"Word files");
		String filename = fc.showDialogAndReturnFilename(this, "Add language");
		if (filename == null) {
			return;
		}

		cancelBA.setEnabled(true);

		File file = new File(filename);
		String lang = file.getName().replaceAll(".txt", "");
		System.out.println("addFromFile: " + filename + ":" + lang);

		if (LangManager.containsLanguage(lang)) {
			if (toOverwrite(lang)) {
				int size = LangManager.addNewLanguage(lang, filename);
				Vector data = langSP.getTableData();
				for (int i = 0; i < data.size(); i++) {
					String[] row = (String[]) data.get(i);
					if (row[0].equals(lang)) {
						data.remove(i);
						row[2] = "" + size;
						data.add(row);
						initLangTable(data);
						progress(pb.getMaximum());
						return;
					}
				}
			}
		} else {
			int size = LangManager.addNewLanguage(lang, filename);
			langSP.addRow(new String[] { lang, "true", "" + size });
		}
		progress(pb.getMaximum());
	}

	private void addFromDb() throws Exception {
		if (inputFieldsCompleted()) {
			cancelBA.setEnabled(true);
			String lang = dbinputLangTF.getText().substring(0, 2);
			HashMap wordlist;
			System.out.println("addFromDb: " + lang);
			if (LangManager.containsLanguage(lang)) {
				if (toOverwrite(lang)) {
					wordlist = DB.getInstance().getWordList(DBTab.getValues(),
							dbinputTableTF.getText(), dbinputIdTF.getText(),
							dbinputWordTF.getText(), dbinputFreqTF.getText(),
							dbinputIdMinSpin.getValue(),
							dbinputNumSpin.getValue());

					int size = LangManager.addNewLanguageFromDb(lang, wordlist);
					Vector data = langSP.getTableData();
					for (int i = 0; i < data.size(); i++) {
						String[] row = (String[]) data.get(i);
						if (row[0].equals(lang)) {
							data.remove(i);
							row[2] = "" + size;
							data.add(row);
							initLangTable(data);
							progress(pb.getMaximum());
							return;
						}
					}
				}
			} else {
				wordlist = DB.getInstance().getWordList(DBTab.getValues(),
						dbinputTableTF.getText(), dbinputIdTF.getText(),
						dbinputWordTF.getText(), dbinputFreqTF.getText(),
						dbinputIdMinSpin.getValue(), dbinputNumSpin.getValue());
				int size = LangManager.addNewLanguageFromDb(lang, wordlist);
				langSP.addRow(new String[] { lang, "true", "" + size });
			}
			DB.getInstance().updateProperties();
			progress(pb.getMaximum());
		}
	}

	private boolean inputFieldsCompleted() {
		if (dbinputTableTF.getText().equals("")
				|| dbinputIdTF.getText().equals("")
				|| dbinputWordTF.getText().equals("")
				|| dbinputFreqTF.getText().equals("")
				|| dbinputLangTF.getText().equals("")) {
			JOptionPane.showMessageDialog(null,
					"All fields must be completed!", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private boolean toOverwrite(String lang) {
		return JOptionPane.showOptionDialog(null, "The Language '" + lang
				+ "' exists already. Do you want to overwrite it?", "Warning",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				new String[] { "Yes", "No" }, "Yes") == JOptionPane.YES_OPTION;
	}

	private void delete() throws Exception {
		langTA = langSP.getTable();
		int selRow = langTA.getSelectedRow();
		if (toDelete(selRow)) {
			String lang = (String) langTA.getValueAt(selRow, 0);
			LangManager.delete(lang);
			langSP.deleteSelected();
			System.out.println("DEL: " + selRow + ":" + lang);
		}
	}

	private boolean toDelete(int selRow) {
		if (selRow == -1) {
			JOptionPane.showMessageDialog(null,
					"The Language must be selected!", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		} else {
			String lang = (String) langTA.getValueAt(selRow, 0);
			int n = JOptionPane.showOptionDialog(null,
					"Do you realy want to delete '" + lang + "'?", "Warning",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, new String[] { "Yes", "No" }, "Yes");
			return n == JOptionPane.YES_OPTION;
		}
	}

	private void addComponents() {
		setLayout(null);

		// langSP.setBounds(170,20,180,440); add(langSP);

		loadAllLangBA.setBounds(400, 20, 140, 20);
		add(loadAllLangBA);
		delBA.setBounds(400, 80, 140, 20);
		add(delBA);
		addFromFileBA.setBounds(400, 120, 140, 20);
		add(addFromFileBA);
		addFromDbBA.setBounds(400, 160, 140, 20);
		add(addFromDbBA);
		dbinputLangLA.setBounds(550, 200, 90, 20);
		add(dbinputLangLA);
		dbinputLangTF.setBounds(400, 200, 140, 20);
		add(dbinputLangTF);
		dbinputTableLA.setBounds(550, 240, 90, 20);
		add(dbinputTableLA);
		dbinputTableTF.setBounds(400, 240, 140, 20);
		add(dbinputTableTF);
		dbinputIdLA.setBounds(550, 280, 90, 20);
		add(dbinputIdLA);
		dbinputIdTF.setBounds(400, 280, 140, 20);
		add(dbinputIdTF);
		dbinputWordLA.setBounds(550, 320, 90, 20);
		add(dbinputWordLA);
		dbinputWordTF.setBounds(400, 320, 140, 20);
		add(dbinputWordTF);
		dbinputFreqLA.setBounds(550, 360, 90, 20);
		add(dbinputFreqLA);
		dbinputFreqTF.setBounds(400, 360, 140, 20);
		add(dbinputFreqTF);
		dbinputIdMinLA.setBounds(550, 400, 90, 20);
		add(dbinputIdMinLA);
		dbinputIdMinSpin.setBounds(400, 400, 140, 20);
		add(dbinputIdMinSpin);
		dbinputNumLA.setBounds(550, 440, 90, 20);
		add(dbinputNumLA);
		dbinputNumSpin.setBounds(400, 440, 140, 20);
		add(dbinputNumSpin);

		langparamWCLA.setBounds(65, 100, 90, 20);
		add(langparamWCLA);
		langparamWC.setBounds(30, 100, 30, 20);
		add(langparamWC);
		langparamCovLA.setBounds(65, 150, 90, 20);
		add(langparamCovLA);
		langparamCov.setBounds(30, 150, 30, 20);
		add(langparamCov);

		pb.setBounds(170, 500, 370, 20);
		add(pb);
		cancelBA.setBounds(550, 500, 90, 20);
		add(cancelBA);
	}

	public static void progress(final int i) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				pb.setValue(i);
			}
		});
		t.start();
	}

	public static void setMaxProgress(int i) {
		pb.setMaximum(i);
	}

	private void stop() {
		try {
			t.stop();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setButtonsEnabled(true);
			progress(pb.getMaximum());
			cancelBA.setEnabled(false);
		}
	}

	private class CheckListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			langTA = langSP.getTable();
			int selRow = langTA.getSelectedRow();
			if (selRow == -1) {
				selRow = checkedRow;
				checkedRow = -1;
			}
			// System.out.println("SELROW: "+selRow);
			String lang = (String) langTA.getValueAt(selRow, 0);
			Object val = langTA.getValueAt(selRow, 1);
			boolean activ;
			if (val instanceof String) {
				activ = (new Boolean((String) val)).booleanValue();
			} else {
				activ = ((Boolean) val).booleanValue();
			}
			LangManager.activ(lang, activ);
			// System.out.println("\n!!! "+lang + ":" + activ+" !!!\n");
		}
	}
}