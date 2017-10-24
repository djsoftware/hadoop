package de.uni_leipzig.asv.tools.segmentizer.processor.impl.util;

import de.uni_leipzig.asv.tools.segmentizer.config.SegmentizerConfig;

public class StringHelper {

	public static final char POINT_CHAR = ".".charAt(0);

	public static String reverse(final String str) {
		final StringBuilder reverser = new StringBuilder(str);
		return reverser.reverse().toString();
	}

	public static String fetchNextTokenAfterPos(final String textWindow,
			final BoundaryIndexAndLength boundaryCandidate, final boolean includeBoundary,
			final boolean removeSentenceBoundariesAtEnd, final SegmentizerConfig config) {
		int afterBoundaryPos = boundaryCandidate.getIndex() + boundaryCandidate.getLength();
		int pos = textWindow.indexOf(" ", afterBoundaryPos);
		if (pos == afterBoundaryPos) {
			afterBoundaryPos++;
			pos = textWindow.indexOf(" ", afterBoundaryPos);
		}

		if (pos == afterBoundaryPos || pos == -1) {
			return null;
		}

		final int start = includeBoundary ? boundaryCandidate.getIndex() : afterBoundaryPos;
		final String postBoundaryToken = textWindow.substring(start, pos);

		for (final String boundary : config.getSentenceBoundaries()) {
			if (postBoundaryToken.endsWith(boundary)) {
				return postBoundaryToken.substring(0, postBoundaryToken.length() - boundary.length());
			}
		}

		return postBoundaryToken;
	}
}
