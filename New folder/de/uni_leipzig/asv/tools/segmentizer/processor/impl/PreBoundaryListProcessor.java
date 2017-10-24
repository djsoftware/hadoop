package de.uni_leipzig.asv.tools.segmentizer.processor.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.uni_leipzig.asv.tools.segmentizer.config.SegmentizerConfig;
import de.uni_leipzig.asv.tools.segmentizer.processor.BoundaryProcessor;
import de.uni_leipzig.asv.tools.segmentizer.processor.impl.util.BoundaryIndexAndLength;
import de.uni_leipzig.asv.tools.segmentizer.processor.impl.util.StringHelper;

public class PreBoundaryListProcessor implements BoundaryProcessor {

	private final Set<String> reversedAbbrevations = new HashSet<String>();
	private int longestAbbrevation = 0;
	private final SegmentizerConfig config;

	public PreBoundaryListProcessor(final SegmentizerConfig config) {
		this.config = config;
	}

	public void initialize() {
		loadAbbrevations(config, reversedAbbrevations);
	}

	private void loadAbbrevations(final SegmentizerConfig segmentizerConfig, final Set<String> abbrevations2) {
		try {
			final URL preBoundaryListFile = segmentizerConfig.getPreBoundaryListFile();
			if (!segmentizerConfig.isQuietMode()) {
				System.out.println((new Date()) + ": Loading pre-boundary list from: " + preBoundaryListFile
						+ " using encoding " + config.getEncoding());
			}
			final InputStreamReader fileReader = new InputStreamReader(preBoundaryListFile.openStream());
			final BufferedReader bufferedReader = new BufferedReader(fileReader);
			while (bufferedReader.ready()) {
				final String line = new String(bufferedReader.readLine().getBytes(), config.getEncoding());
				if (line.length() > longestAbbrevation) {
					longestAbbrevation = line.length();
				}
				reversedAbbrevations.add(StringHelper.reverse(line));
			}
			if (!segmentizerConfig.isQuietMode()) {
				System.out.println((new Date()) + ": Done loading pre-boundary list from: " + preBoundaryListFile);
			}
			
			// LSW
			fileReader.close();
			bufferedReader.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	
	}

	public boolean isNoSentenceBoundary(final String textWindow, final BoundaryIndexAndLength boundaryCandidate) {
		final int firstPos = boundaryCandidate.getIndex() - longestAbbrevation > 0 ? boundaryCandidate.getIndex()
				- longestAbbrevation : 0;
		final String reversedString = StringHelper.reverse(textWindow.substring(firstPos, boundaryCandidate.getIndex()
				+ boundaryCandidate.getLength()));
		final int pos = reversedString.indexOf(" ", boundaryCandidate.getLength());
		final String reversedAbbrevCandidate = pos != -1 ? reversedString.substring(0, pos) : reversedString;

		if (reversedAbbrevations.contains(reversedAbbrevCandidate.trim())) {
			return true;
		}

		return false;
	}
}
