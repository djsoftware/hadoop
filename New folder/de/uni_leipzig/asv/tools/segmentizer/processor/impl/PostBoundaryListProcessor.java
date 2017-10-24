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

public class PostBoundaryListProcessor implements BoundaryProcessor {

	private final Set<String> postBoundaries = new HashSet<String>();
	private int longestPostBoundary = 0;
	private final SegmentizerConfig config;

	public PostBoundaryListProcessor(final SegmentizerConfig config) {
		this.config = config;
	}

	public void initialize() {
		loadPostBoundaries(config, postBoundaries);
	}

	private void loadPostBoundaries(final SegmentizerConfig segmentizerConfig, final Set<String> abbrevations2) {
		try {
			final URL postBoundaryListFile = segmentizerConfig.getPostBoundaryListFile();
			if (!segmentizerConfig.isQuietMode()) {
				System.out.println((new Date()) + ": Loading post-boundary list from: " + postBoundaryListFile
						+ " using encoding " + config.getEncoding());
			}
			final InputStreamReader fileReader = new InputStreamReader(postBoundaryListFile.openStream());
			final BufferedReader bufferedReader = new BufferedReader(fileReader);
			while (bufferedReader.ready()) {
				final String line = new String(bufferedReader.readLine().getBytes(), config.getEncoding());
				if (line.length() > longestPostBoundary) {
					longestPostBoundary = line.length();
				}
				postBoundaries.add(line);
			}
			if (!segmentizerConfig.isQuietMode()) {
				System.out.println((new Date()) + ": Done loading post-boundary list from: " + postBoundaryListFile);
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
		final String postBoundaryToken = StringHelper.fetchNextTokenAfterPos(textWindow, boundaryCandidate, false,
				true, config);

		if (postBoundaryToken == null) {
			return false;
		}

		if (postBoundaries.contains(postBoundaryToken)) {
			return true;
		}

		return false;
	}

}
