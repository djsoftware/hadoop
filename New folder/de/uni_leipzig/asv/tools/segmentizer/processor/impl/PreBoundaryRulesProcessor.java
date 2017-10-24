package de.uni_leipzig.asv.tools.segmentizer.processor.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_leipzig.asv.tools.segmentizer.config.SegmentizerConfig;
import de.uni_leipzig.asv.tools.segmentizer.processor.BoundaryProcessor;
import de.uni_leipzig.asv.tools.segmentizer.processor.impl.util.BoundaryIndexAndLength;
import de.uni_leipzig.asv.tools.segmentizer.processor.impl.util.BoundaryRuleHelper;
import de.uni_leipzig.asv.tools.segmentizer.processor.impl.util.StringHelper;

public class PreBoundaryRulesProcessor implements BoundaryProcessor {

	private final SegmentizerConfig config;
	private final Map<Pattern, Boolean> compiledPatterns = new HashMap<Pattern, Boolean>();

	public PreBoundaryRulesProcessor(final SegmentizerConfig config) {
		this.config = config;
	}

	public void initialize() {
		loadRules(config, compiledPatterns);
	}

	private void loadRules(final SegmentizerConfig segmentizerConfig, final Map<Pattern, Boolean> compiledPatterns) {
		try {
			final URL preBoundaryRulesFile = segmentizerConfig.getPreBoundaryRulesFile();
			if (!segmentizerConfig.isQuietMode()) {
				System.out.println((new Date()) + ": Loading pre-boundary rules from: " + preBoundaryRulesFile
						+ " using encoding " + config.getEncoding());
			}
			BoundaryRuleHelper.loadPatterns(compiledPatterns, preBoundaryRulesFile, segmentizerConfig);
			if (!segmentizerConfig.isQuietMode()) {
				System.out.println((new Date()) + ": Done loading pre-boundary rules from: " + preBoundaryRulesFile);
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isNoSentenceBoundary(final String textWindow, final BoundaryIndexAndLength boundaryCandidate) {
		final String reversedString = StringHelper.reverse(textWindow.substring(0, boundaryCandidate.getIndex()
				+ boundaryCandidate.getLength()));
		final int pos = reversedString.indexOf(" ", boundaryCandidate.getLength());
		final String reversedToken = pos != -1 && pos - boundaryCandidate.getLength() > 0 ? reversedString.substring(
				boundaryCandidate.getLength(), pos) : reversedString.substring(boundaryCandidate.getLength(),
				reversedString.length());
		final String token = StringHelper.reverse(reversedToken.trim());

		for (final Entry<Pattern, Boolean> entry : compiledPatterns.entrySet()) {
			final Matcher matcher = entry.getKey().matcher(token);
			if (matcher.matches()) {
				return !entry.getValue();
			}
		}

		return false;
	}
}
