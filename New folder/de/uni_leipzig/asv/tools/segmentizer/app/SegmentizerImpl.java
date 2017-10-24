package de.uni_leipzig.asv.tools.segmentizer.app;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import de.uni_leipzig.asv.tools.segmentizer.Segmentizer;
import de.uni_leipzig.asv.tools.segmentizer.config.SegmentizerConfig;
import de.uni_leipzig.asv.tools.segmentizer.config.impl.SegmentizerConfigImpl;
import de.uni_leipzig.asv.tools.segmentizer.data.ContentAndMetaData;
import de.uni_leipzig.asv.tools.segmentizer.data.ContentMetaData;
import de.uni_leipzig.asv.tools.segmentizer.data.SegmentizerData;

public class SegmentizerImpl implements Segmentizer {

	private final SegmentizerConfig segmentizerConfig;

	public SegmentizerImpl() throws MalformedURLException {
		this(new File("boundariesFile.txt").toURI().toURL(), new File(
				"postList.txt").toURI().toURL(), new File("postRules.txt")
				.toURI().toURL(), new File("preList.txt").toURI().toURL(),
				new File("preRules.txt").toURI().toURL());
	}

	public SegmentizerImpl(final URL sentenceBoundariesFile,
			final URL postBoundaryListFile, final URL postBoundaryRulesFile,
			final URL preBoundaryListFile, final URL preBoundaryRulesFile) {
		final SegmentizerConfigImpl config = new SegmentizerConfigImpl();
		config.setSentenceBoundariesFile(sentenceBoundariesFile);
		config.setPostBoundaryListFile(postBoundaryListFile);
		config.setPostBoundaryRulesFile(postBoundaryRulesFile);
		config.setPreBoundaryListFile(preBoundaryListFile);
		config.setPreBoundaryRulesFile(preBoundaryRulesFile);
		config.setQuietMode(true);
		segmentizerConfig = config;
	}

	public SegmentizerConfig getConfig() {
		return segmentizerConfig;
	}

	private class SegmentationRunner implements Runnable {

		private final AsvSegmentizerImpl segmentizer;

		public SegmentationRunner(final SegmentizerData inputData,
				final SegmentizerData outputData,
				final SegmentizerConfig segmentizerConfig) {
			segmentizer = new AsvSegmentizerImpl(inputData, outputData,
					segmentizerConfig);
		}

		public void run() {
			segmentizer.doSegmentation();
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uni_leipzig.asv.tools.segmentizer.Segmentizer#segmentizeStr(java.lang
	 * .String)
	 */
	public List<String> segmentize(final String inputStr) {

		final SegmentizerData inputData = new SegmentizerData();
		final SegmentizerData outputData = new SegmentizerData();

		final ContentMetaData contentMetaData = new ContentMetaData(
				SegmentizerImpl.class.getName());
		inputData.addData(inputStr, contentMetaData);
		inputData.setFinished(true);

		final Thread segmentizerThread = new Thread(new SegmentationRunner(
				inputData, outputData, segmentizerConfig));
		segmentizerThread.start();

		final List<String> sentences = new LinkedList<String>();
		while (!outputData.isFinished()) {
			while (outputData.dataAvailable()) {
				final ContentAndMetaData contentAndMetaData = outputData
						.fetchData();
				final String trimmedText = contentAndMetaData.getContent()
						.trim();
				if (trimmedText.length() == 0) {
					continue;
				}
				sentences.add(trimmedText);
			}
		}
		return sentences;
	}

	public static void main(final String[] args) throws MalformedURLException {
		final Segmentizer segmentizer = new SegmentizerImpl();

		final String input = "Dies ist ein Test. Dies ist noch ein Test. Hier dann noch ein Satz. Und vielleicht noch einer danach. Und nun der letzte Satz zum Schluss.";

		final List<String> sentences = segmentizer.segmentize(input);
		System.out.println(sentences.size() + " Sätze gefunden:");
		for (final String sentence : sentences) {
			System.out.println(sentence.trim());
		}
	}
}
