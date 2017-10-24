package de.uni_leipzig.asv.tools.segmentizer;

import java.util.List;

import de.uni_leipzig.asv.tools.segmentizer.config.SegmentizerConfig;

public interface Segmentizer {

	List<String> segmentize(final String text);

	SegmentizerConfig getConfig();

}