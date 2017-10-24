/**
 * $Id: DB.java,v 1.3 2008-05-20 12:23:28 steresniak Exp $
 */

package de.uni_leipzig.asv.toolbox.jLanI.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import de.uni_leipzig.asv.toolbox.jLanI.gui.DBTab;
import de.uni_leipzig.asv.toolbox.jLanI.gui.LaniTab;
import de.uni_leipzig.asv.toolbox.jLanI.gui.NewLanguageTab;
import de.uni_leipzig.asv.toolbox.util.commonDB.CommonDB;
import de.uni_leipzig.asv.toolbox.util.commonDB.DBConnection;

/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class DB {
	private static DB db;

	private Properties prop;

	private String propFileName;

	private CommonDB cdb;

	private String[] dbValues;

	private DB() {
		String fs = System.getProperty("file.separator");
		prop = new Properties();
		try {
			propFileName = "." + fs + "lanidb.ini";
			// propFileName = "."+fs+"config"+fs+"jlani"+fs+"lanidb.ini";
			prop.load(new FileInputStream(propFileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DB getInstance() {
		if (db == null) {
			db = new DB();
		}
		return db;
	}

	public CommonDB getCommonDB() {
		return cdb;
	}

	/**
	 * @param values -
	 *            eingegebene DB-Werte im DB Tab
	 */
	public CommonDB getCommonDB(String[] values) {
		if (newDBConnection(values)) {
			DBConnection dbcon = new DBConnection();
			dbcon.setDriverClass(values[0]);
			dbcon.setDbURL("jdbc:" + values[1] + "://" + values[2] + "/"
					+ values[3]);
			dbcon.setUserID(values[4]);
			dbcon.setPassWd(values[5]);
			cdb = new CommonDB(dbcon, "lanidb.query", false);
			dbValues = values;
		}
		return cdb;
	}

	private boolean newDBConnection(String[] values) {
		if (dbValues == null) {
			return true;
		}
		for (int i = 0; i < dbValues.length; i++) {
			if (!dbValues[i].equals(values)) {
				return true;
			}
		}
		return false;
	}

	public String getProperty(String key) {
		return prop.getProperty(key);
	}

	/** obrains wordlist from database */
	public HashMap getWordList(String[] values, String tabName, String idCol,
			String wordCol, String freqCol, Object idm, Object wordsNum)
			throws SQLException {
		int idMin = ((Integer) idm).intValue();
		int num = ((Integer) wordsNum).intValue();
		HashMap wl = new HashMap();
		CommonDB db = getCommonDB(values);
		String q1 = "select " + wordCol + "," + freqCol + " from " + tabName
				+ " where " + idCol + " >=?"; // +" order by "+freqCol+" desc"
		if (num > 0) {
			q1 += " limit 0,?";
		}
		Vector params = new Vector();
		params.add("" + idMin);
		params.add("" + num);

		// try
		// {
		ResultSet wordCountRs = db.executeQueryWithResults_ResultSet(
				"select count(*) from " + tabName, null);
		wordCountRs.next();
		int wordCount = wordCountRs.getInt(1);
		NewLanguageTab.setMaxProgress(wordCount + 1);
		int count = 0;

		ResultSet wordsRs = db.executeQueryWithResults_ResultSet(q1, params);
		ResultSet tokensRs = db.executeQueryWithResults_ResultSet("select SUM("
				+ freqCol + ") from " + tabName, null);
		tokensRs.next();
		double tokens = tokensRs.getDouble(1);
		while (wordsRs.next()) {
			String word = wordsRs.getString(1);
			double freq = wordsRs.getDouble(2);
			Double relFreq = new Double(freq / tokens);
			wl.put(word, relFreq);
			// System.out.println("\n\nWORD:"+word+" - FREQ:"+relFreq+"\n\n");
			NewLanguageTab.progress(++count);
		}
		// }catch (Exception e){e.printStackTrace();}

		return wl;
	}

	public void updateProperties() {

		prop.setProperty("jlani.driverClass", DBTab.dbinputDriverTF.getText());
		prop.setProperty("jlani.dbProtokol", DBTab.dbinputProtTF.getText());
		prop.setProperty("jlani.dbHost", DBTab.dbinputHostTF.getText());
		prop.setProperty("jlani.dbDatabase", DBTab.dbinputDbTF.getText());
		prop.setProperty("jlani.userID", DBTab.dbinputUserTF.getText());
		prop.setProperty("jlani.passWd", DBTab.dbinputPwTF.getText());

		prop.setProperty("jlani.inputTable", LaniTab.inputPane.dbinputTableTF
				.getText());
		prop.setProperty("jlani.inputId", LaniTab.inputPane.dbinputIdTF
				.getText());
		prop.setProperty("jlani.inputSent", LaniTab.inputPane.dbinputSentTF
				.getText());

		prop.setProperty("jlani.outputTable",
				LaniTab.outputPane.dboutputTableTF.getText());
		prop.setProperty("jlani.outputId", LaniTab.outputPane.dboutputIdTF
				.getText());
		prop.setProperty("jlani.outputSent", LaniTab.outputPane.dboutputSentTF
				.getText());
		prop.setProperty("jlani.outputLang1",
				LaniTab.outputPane.dboutputLang1TF.getText());
		prop.setProperty("jlani.outputLang2",
				LaniTab.outputPane.dboutputLang2TF.getText());
		prop.setProperty("jlani.outputProb1",
				LaniTab.outputPane.dboutputProb1TF.getText());
		prop.setProperty("jlani.outputProb2",
				LaniTab.outputPane.dboutputProb2TF.getText());

		NewLanguageTab langTab = NewLanguageTab.getInstance();
		prop.setProperty("jlani.langTable", langTab.dbinputTableTF.getText());
		prop.setProperty("jlani.langWord", langTab.dbinputWordTF.getText());
		prop.setProperty("jlani.langFreq", langTab.dbinputFreqTF.getText());
		prop.setProperty("jlani.langLang", langTab.dbinputLangTF.getText());
		prop.setProperty("jlani.langId", langTab.dbinputIdTF.getText());
		prop.setProperty("jlani.minWordCount", langTab.langparamWC.getText());
		prop.setProperty("jlani.minCoverage", langTab.langparamCov.getText());

		try {
			prop.store(new FileOutputStream(propFileName), "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
