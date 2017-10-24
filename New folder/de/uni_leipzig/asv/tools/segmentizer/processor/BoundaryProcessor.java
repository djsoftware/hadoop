package de.uni_leipzig.asv.tools.segmentizer.processor;

import de.uni_leipzig.asv.tools.segmentizer.processor.impl.util.BoundaryIndexAndLength;

public interface BoundaryProcessor {

	void initialize();

	boolean isNoSentenceBoundary(String textWindow, BoundaryIndexAndLength boundaryCandidate);
}
