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

public class PostBoundaryRulesProcessor implements BoundaryProcessor {

	private final SegmentizerConfig config;
	private final Map<Pattern, Boolean> compiledPatterns = new HashMap<Pattern, Boolean>();

	public PostBoundaryRulesProcessor(final SegmentizerConfig config) {
		this.config = config;
	}

	public void initialize() {
		loadRules(config, compiledPatterns);
	}

	private void loadRules(final SegmentizerConfig segmentizerConfig, final Map<Pattern, Boolean> compiledPatterns) {
		try {
			final URL postBoundaryRulesFile = segmentizerConfig.getPostBoundaryRulesFile();
			if (!segmentizerConfig.isQuietMode()) {
				System.out.println((new Date()) + ": Loading post-boundary rules from: " + postBoundaryRulesFile
						+ " using encoding " + config.getEncoding());
			}
			BoundaryRuleHelper.loadPatterns(compiledPatterns, postBoundaryRulesFile, segmentizerConfig);
			if (!segmentizerConfig.isQuietMode()) {
				System.out.println((new Date()) + ": Done loading post-boundary rules from: " + postBoundaryRulesFile);
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isNoSentenceBoundary(final String textWindow, final BoundaryIndexAndLength boundaryCandidate) {
		final String postBoundaryToken = StringHelper.fetchNextTokenAfterPos(textWindow, boundaryCandidate, false,
				false, config);

		if (postBoundaryToken == null) {
			return false;
		}

		for (final Entry<Pattern, Boolean> entry : compiledPatterns.entrySet()) {
			final Matcher matcher = entry.getKey().matcher(postBoundaryToken);
			if (matcher.matches()) {
				return !entry.getValue();
			}
		}

		return false;
	}
}
