package de.uni_leipzig.asv.html2text.textextractor.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileHelper {

	public static List<String> loadText(final File file) throws IOException {
		final List<String> textLines = new LinkedList<String>();
		final BufferedReader inBuff = new BufferedReader(new FileReader(file));
		while (inBuff.ready()) {
			textLines.add(inBuff.readLine());
		}
		return textLines;
	}
}
