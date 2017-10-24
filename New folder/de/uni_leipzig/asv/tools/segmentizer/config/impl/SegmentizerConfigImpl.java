package de.uni_leipzig.asv.tools.segmentizer.config.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.uni_leipzig.asv.tools.segmentizer.config.SegmentizerConfig;
import de.uni_leipzig.asv.tools.segmentizer.processor.BoundaryProcessor;
import de.uni_leipzig.asv.tools.segmentizer.processor.impl.PostBoundaryListProcessor;
import de.uni_leipzig.asv.tools.segmentizer.processor.impl.PostBoundaryRulesProcessor;
import de.uni_leipzig.asv.tools.segmentizer.processor.impl.PreBoundaryListProcessor;
import de.uni_leipzig.asv.tools.segmentizer.processor.impl.PreBoundaryRulesProcessor;

public class SegmentizerConfigImpl implements SegmentizerConfig {

	private static final String CR = "\n";

	public SegmentizerConfigImpl() {
		initBoundaryProcessors();
	}

	private final int MIN_WINDOW_SIZE = 64;

	public int getMinWindowSize() {
		return MIN_WINDOW_SIZE;
	}

	private final int MIN_POST_BOUNDARY_WINDOW_SIZE = 20;

	public int getMinPostBoundaryWindowSize() {
		return MIN_POST_BOUNDARY_WINDOW_SIZE;
	}

	private final int fileThreadBuffSize = 128;

	public int getFileThreadBuffSize() {
		return fileThreadBuffSize;
	}

	private final int fileThreadMaxLines = 1024;

	public int getFileThreadMaxLines() {
		return fileThreadMaxLines;
	}

	private Collection<String> sentenceBoundaries = new LinkedList<String>();

	private void initSentenceBoundaries() {
		if (sentenceBoundaries.isEmpty()) {
			sentenceBoundaries.addAll(tryToLoadSentenceBoundariesFromFile());
			if (sentenceBoundaries.isEmpty()) {
				sentenceBoundaries.add(".");
				sentenceBoundaries.add("!");
				sentenceBoundaries.add("?");
				if (!isQuietMode()) {
					System.out.println("No/Empty boundary candidates file found. Using defaults.");
				}
			}
			sentenceBoundaries.addAll(createNewLineBoundaries(sentenceBoundaries));
		}
	}

	private Collection<String> createNewLineBoundaries(final Collection<String> boundaries) {
		final Collection<String> newLineBoundaries = new LinkedList<String>();
		if (!boundaries.contains(CR)) {
			newLineBoundaries.add(CR);
		}
		return newLineBoundaries;
	}

	private List<String> tryToLoadSentenceBoundariesFromFile() {
		final List<String> list = new LinkedList<String>();
		final URL boundariesFile = getSentenceBoundariesFile();
		if (boundariesFile != null) {
			try {
				if (!isQuietMode()) {
					System.out.println((new Date()) + ": Loading boundary candiates from: " + boundariesFile
							+ " using encoding " + getEncoding());
				}
				final InputStreamReader fileReader = new InputStreamReader(boundariesFile.openStream());
				final BufferedReader bufferedReader = new BufferedReader(fileReader);
				while (bufferedReader.ready()) {
					final String line = new String(bufferedReader.readLine().getBytes(), getEncoding());
					list.add(line);
				}
				if (!isQuietMode()) {
					System.out.println((new Date()) + ": Done loading boundary candidates from: " + boundariesFile);
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
		return list;
	}

	public void setSentenceBoundaries(final Collection<String> boundaries) {
		sentenceBoundaries = boundaries;
	}

	public Collection<String> getSentenceBoundaries() {
		initSentenceBoundaries();
		return sentenceBoundaries;
	}

	private URL sentenceBoundariesFile = null;

	public void setSentenceBoundariesFile(final URL sentenceBoundariesFile) {
		this.sentenceBoundariesFile = sentenceBoundariesFile;
	}

	private URL getSentenceBoundariesFile() {
		return sentenceBoundariesFile;
	}

	private final Collection<BoundaryProcessor> boundaryProcessors = new LinkedList<BoundaryProcessor>();

	private void initBoundaryProcessors() {
		if (boundaryProcessors.isEmpty()) {
			boundaryProcessors.add(new PreBoundaryListProcessor(this));
			boundaryProcessors.add(new PostBoundaryListProcessor(this));
			boundaryProcessors.add(new PreBoundaryRulesProcessor(this));
			boundaryProcessors.add(new PostBoundaryRulesProcessor(this));
		}
	}

	public Collection<BoundaryProcessor> getBoundaryProcessors() {
		return boundaryProcessors;
	}

	private URL inputFile = null;

	public URL getInputFile() {
		return inputFile;
	}

	public void setInputFile(final URL inputFile) {
		this.inputFile = inputFile;
	}

	private File outputFile = null;

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(final File outputFile) {
		this.outputFile = outputFile;
	}

	private URL preBoundaryListFile = null;

	public URL getPreBoundaryListFile() {
		return preBoundaryListFile;
	}

	public void setPreBoundaryListFile(final URL preBoundaryListFile) {
		this.preBoundaryListFile = preBoundaryListFile;
	}

	private URL preBoundaryRulesFile = null;

	public void setPreBoundaryRulesFile(final URL preBoundaryRulesFile) {
		this.preBoundaryRulesFile = preBoundaryRulesFile;
	}

	public URL getPreBoundaryRulesFile() {
		return preBoundaryRulesFile;
	}

	private URL postBoundaryListFile = null;

	public URL getPostBoundaryListFile() {
		return postBoundaryListFile;
	}

	public void setPostBoundaryListFile(final URL postBoundaryListFile) {
		this.postBoundaryListFile = postBoundaryListFile;
	}

	private URL postBoundaryRulesFile = null;

	public void setPostBoundaryRulesFile(final URL postBoundaryRulesFile) {
		this.postBoundaryRulesFile = postBoundaryRulesFile;
	}

	public URL getPostBoundaryRulesFile() {
		return postBoundaryRulesFile;
	}

	private String encoding = "UTF-8";

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(final String encoding) {
		this.encoding = encoding;
	}

	private boolean isQuietMode = false;

	public boolean isQuietMode() {
		return isQuietMode;
	}

	public void setQuietMode(final boolean isQuietMode) {
		this.isQuietMode = isQuietMode;
	}

	private boolean isNameAndSourceMode = false;

	public boolean isNameAndSourceMode() {
		return isNameAndSourceMode;
	}

	public void setNameAndSourceMode(final boolean isNameAndSourceMode) {
		this.isNameAndSourceMode = isNameAndSourceMode;
	}

	private boolean isNewSourceMode = false;

	public boolean isNewSourceMode() {
		return isNewSourceMode;
	}

	public void setNewSourceMode(final boolean isNewSourceMode) {
		this.isNewSourceMode = isNewSourceMode;
	}

	private boolean isIdAndHashMode = false;

	public boolean isIdAndHashMode() {
		return isIdAndHashMode;
	}

	public void setIdAndHashMode(final boolean isIdAndHashMode) {
		this.isIdAndHashMode = isIdAndHashMode;
	}

	private boolean isTrimMode = true;

	public boolean isTrimMode() {
		return isTrimMode;
	}

	public void setTrimMode(final boolean isTrimMode) {
		this.isTrimMode = isTrimMode;
	}

	private boolean useCarriageReturnAsBoundary = true;

	public void setUseCarriageReturnAsBoundary(final boolean useCarriageReturnAsBoundary) {
		this.useCarriageReturnAsBoundary = useCarriageReturnAsBoundary;
	}

	public boolean isUseCarriageReturnAsBoundary() {
		return useCarriageReturnAsBoundary;
	}

	private boolean useEmptyLineAsBoundary = true;

	public void setUseEmptyLineAsBoundary(final boolean useEmptyLineAsBoundary) {
		this.useEmptyLineAsBoundary = useEmptyLineAsBoundary;
	}

	public boolean isUseEmptyLineAsBoundary() {
		return useEmptyLineAsBoundary;
	}

}
