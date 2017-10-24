package de.uni_leipzig.asv.tools.segmentizer.processor.impl.util;

import de.uni_leipzig.asv.clarin.common.tuple.Pair;

public class BoundaryIndexAndLength extends Pair<Integer, Integer>{

	public BoundaryIndexAndLength(final int index, final int length) {
		super(index, length);
	}

	public int getIndex() {
		return first();
	}

	public int getLength() {
		return second();
	}
}
