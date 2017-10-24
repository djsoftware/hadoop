package de.uni_leipzig.asv.toolbox.jLanI.gui;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import de.uni_leipzig.asv.toolbox.jLanI.io.DBInput;
import de.uni_leipzig.asv.toolbox.jLanI.io.DBOutput;
import de.uni_leipzig.asv.toolbox.jLanI.io.FileInput;
import de.uni_leipzig.asv.toolbox.jLanI.io.FileOutput;
import de.uni_leipzig.asv.toolbox.jLanI.io.Input;
import de.uni_leipzig.asv.toolbox.jLanI.io.Output;
import de.uni_leipzig.asv.toolbox.jLanI.io.TextInput;
import de.uni_leipzig.asv.toolbox.jLanI.io.TextOutput;
import de.uni_leipzig.asv.toolbox.jLanI.kernel.LanIKernel;
import de.uni_leipzig.asv.toolbox.jLanI.kernel.LangManager;
import de.uni_leipzig.asv.toolbox.jLanI.kernel.Request;
import de.uni_leipzig.asv.toolbox.jLanI.kernel.Response;

/**
 * Die Sprache wird satzweise ermittelt und Vortschrittsbalken aktualisiert
 * 
 * @author Alexandr Uciteli
 * @version 1.0
 */
class CalculateThread extends Thread {
	private LanIKernel kern;

	private int progCount = 0;

	private int maxProgress;

	private int onePercent;

	private double mincov;

	private int mincount;

	@Override
	public void run() {
		boolean dbInput = false;
		Input in = null;

		NewLanguageTab nlt = NewLanguageTab.getInstance();
		mincov = nlt.langparamCov.getText().trim().equals("") ? 0.05 : Double
				.valueOf(nlt.langparamCov.getText());
		mincount = nlt.langparamWC.getText().trim().equals("") ? 1 : Integer
				.valueOf(nlt.langparamWC.getText());

		// mincov = new Double(nlt.langparamCov.getText())
		// .doubleValue();
		// mincount = new Integer(nlt.langparamWC
		// .getText()).intValue();

		if (LaniTab.inputPane.textinputRB.isSelected()) {
			String text = LaniTab.inputPane.textinputTA.getText();
			try {
				in = new TextInput(text);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Text Input Error!",
						"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		} else if (LaniTab.inputPane.fileinputRB.isSelected()) {
			String file = LaniTab.inputPane.fileinputFCP.fileTF.getText();
			try {
				in = new FileInput(file);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "File '" + file
						+ "' not found!", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		} else {
			try {
				dbInput = true;
				in = new DBInput(DBTab.getValues(),
						LaniTab.inputPane.dbinputTableTF.getText(),
						LaniTab.inputPane.dbinputIdTF.getText(),
						LaniTab.inputPane.dbinputSentTF.getText(),
						LaniTab.inputPane.dbinputIdMinSpin.getValue(),
						LaniTab.inputPane.dbinputIdMaxSpin.getValue());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "DB Input Error!", "Error",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}

		Output o1 = null, o2 = null, o3 = null;
		if (LaniTab.outputPane.textoutputCB.isSelected()) {
			LaniTab.outputPane.initOutputTable();
			o1 = new TextOutput();
		}
		if (LaniTab.outputPane.fileoutputCB.isSelected()) {
			String file = LaniTab.outputPane.fileoutputFCP.fileTF.getText();
			try {
				o2 = new FileOutput(file);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "File Output Error!",
						"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		if (LaniTab.outputPane.dboutputCB.isSelected()) {
			try {
				if (dbInput) {
					o3 = new DBOutput(LaniTab.inputPane.dbinputTableTF
							.getText(),
							LaniTab.inputPane.dbinputIdTF.getText(),
							LaniTab.outputPane.dboutputLang1TF.getText(),
							LaniTab.outputPane.dboutputProb1TF.getText(),
							LaniTab.outputPane.dboutputLang2TF.getText(),
							LaniTab.outputPane.dboutputProb2TF.getText());
				} else {
					o3 = new DBOutput(DBTab.getValues(),
							LaniTab.outputPane.dboutputTableTF.getText(),
							LaniTab.outputPane.dboutputIdTF.getText(),
							LaniTab.outputPane.dboutputSentTF.getText(),
							LaniTab.outputPane.dboutputLang1TF.getText(),
							LaniTab.outputPane.dboutputProb1TF.getText(),
							LaniTab.outputPane.dboutputLang2TF.getText(),
							LaniTab.outputPane.dboutputProb2TF.getText());
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "DB Output Error!",
						"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}

		if (in != null && (o1 != null || o2 != null || o3 != null)) {
			maxProgress = in.getMaxProgress();
			onePercent = maxProgress / 100 + 1;
			LaniTab.pb.setMaximum(maxProgress);
			LaniTab.progress(onePercent);
			setKern();
			calculate(in, o1, o2, o3);
		}
		LaniTab.setButtonsEnabled(true);
	}

	private void setKern() {
		try {
			kern = LanIKernel.getInstance();
			if (!LangManager.loaded) {
				Thread t = new Thread(new Runnable() {
					public void run() {
						NewLanguageTab.getInstance().loadAllLanguages();
					}
				});
				t.start();
			}
			kern.log.setLogMode(0);
			kern.log.setDebug(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void calculate(Input in, Output o1, Output o2, Output o3) {
		HashMap<String, Integer> resultHM = new HashMap<String, Integer>();
		int sentCount = 0;
		String sent = null;
		try {
			while ((sent = in.readSent()) != null && !isInterrupted()) {
				Request req = new Request();
				req.setLanguages(new HashSet());
				req.setSentence(sent);
				Response res = kern.evaluate(req);

				if (o1 != null) {
					try {
						o1.writeSent(res.getLangResult(mincov, mincount), in
								.getId(), sent);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"Error while writing text!", "Error",
								JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
						o1 = null;
					}
				}
				if (o2 != null) {
					try {
						o2.writeSent(res.getLangResult(mincov, mincount), in
								.getId(), sent);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"Error while writing file!", "Error",
								JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
						o2 = null;
					}
				}
				if (o3 != null) {
					try {
						o3.writeSent(res.getLangResult(mincov, mincount), in
								.getId(), sent);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"Error while writing DB!", "Error",
								JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
						o3 = null;
					}
				}

				String lang = res.getLangResult(mincov, mincount).getLang1();

				// Double coverage = res.getLanguageCoverage(lang);
				// int wordcount=res.getLanguageWordCount(lang);

				// System.out.println("Lang: "+lang+"
				// cov="+coverage+"wc="+wordcount);
				// System.out.println("Lang: "+lang);

				Integer num = resultHM.get(lang);
				if (num == null) {
					resultHM.put(lang, new Integer(1));
				} else {
					resultHM.put(lang, new Integer(++num));
				}

				progCount = in.getCurrentProgress();
				// System.out.println("Progcount:"+progCount);
				// if(progCount>onePercent) LaniTab.progress(progCount);
				LaniTab.progress(progCount);
				sentCount++;
			}
		} catch (Exception e) {
			if (sent == null) {
				JOptionPane.showMessageDialog(null,
						"Error while reading from input!", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			e.printStackTrace();
		} finally {
			LaniTab.progress(LaniTab.pb.getMaximum());
			if (o2 != null) {
				o2.close();
			}
			LaniTab.outputPane.textoutputResLA.setText(getResultString(
					resultHM, sentCount));
		}
	}

	private String getResultString(HashMap<String, Integer> result,
			int sentCount) {

		Integer unknownNum = result.remove("unknown");
		Set<String> langSet = result.keySet();
		String[] langAr = langSet.toArray(new String[] {});
		int langCount = langAr.length;
		Integer[] valueAr = new Integer[langCount];

		for (int i = 0; i < langCount; i++) {
			valueAr[i] = result.get(langAr[i]);
		}

		for (int i = 0; i < 3; i++) {
			for (int j = i + 1; j < langAr.length; j++) {
				if (valueAr[i].intValue() < valueAr[j].intValue()) {
					String temp = langAr[i];
					langAr[i] = langAr[j];
					langAr[j] = temp;

					Integer tmp = valueAr[i];
					valueAr[i] = valueAr[j];
					valueAr[j] = tmp;
				}
			}
		}

		String res = "sent:" + sentCount;
		if (langCount > 0) {

			res += " " + langAr[0] + ":" + valueAr[0];
		}
		if (langCount > 1) {
			res += " " + langAr[1] + ":" + valueAr[1];
		}
		if (unknownNum != null) {
			res += " unknown:" + unknownNum;
		}

		return res;
	}
}
