package de.uni_leipzig.asv.tools.segmentizer.config;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import de.uni_leipzig.asv.tools.segmentizer.processor.BoundaryProcessor;

public interface SegmentizerConfig {

	int getMinWindowSize();

	int getMinPostBoundaryWindowSize();

	URL getInputFile();

	File getOutputFile();

	URL getPreBoundaryListFile();

	URL getPreBoundaryRulesFile();

	URL getPostBoundaryListFile();

	URL getPostBoundaryRulesFile();

	Collection<String> getSentenceBoundaries();

	void setSentenceBoundaries(final Collection<String> boundaries);

	Collection<BoundaryProcessor> getBoundaryProcessors();

	int getFileThreadBuffSize();

	int getFileThreadMaxLines();

	String getEncoding();

	boolean isQuietMode();

	boolean isNameAndSourceMode();

	boolean isNewSourceMode();

	boolean isIdAndHashMode();

	boolean isTrimMode();

	boolean isUseCarriageReturnAsBoundary();

	boolean isUseEmptyLineAsBoundary();

}
