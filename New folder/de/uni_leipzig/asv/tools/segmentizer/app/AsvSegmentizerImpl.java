package de.uni_leipzig.asv.tools.segmentizer.app;

import java.util.Date;

import de.uni_leipzig.asv.tools.segmentizer.config.SegmentizerConfig;
import de.uni_leipzig.asv.tools.segmentizer.data.ContentAndMetaData;
import de.uni_leipzig.asv.tools.segmentizer.data.ContentMetaData;
import de.uni_leipzig.asv.tools.segmentizer.data.SegmentizerData;
import de.uni_leipzig.asv.tools.segmentizer.processor.BoundaryProcessor;
import de.uni_leipzig.asv.tools.segmentizer.processor.impl.util.BoundaryIndexAndLength;

public class AsvSegmentizerImpl {

	private static final String CR = "\n";
	private final SegmentizerData inputData;
	private final SegmentizerData outputData;
	private final StringBuilder processData = new StringBuilder();
	private final SegmentizerConfig config;
	private ContentMetaData contentMetaData = null;

	public AsvSegmentizerImpl(final SegmentizerData inputData,
			final SegmentizerData outputData,
			final SegmentizerConfig segmentizerConfig) {
		this.inputData = inputData;
		this.outputData = outputData;
		this.config = segmentizerConfig;
	}

	public void doSegmentation() {
		for (final BoundaryProcessor processor : config.getBoundaryProcessors()) {
			processor.initialize();
		}

		if (!config.isQuietMode()) {
			System.out.println((new Date()) + ": Segmentation started.");
		}

		int posInWindow = 0;
		int windowSize = config.getMinWindowSize();
		final int postBoundaryWindowSize = config
				.getMinPostBoundaryWindowSize();

		boolean doSegmentation = true;
		boolean doSourceSwitchOnce = true;
		while (doSegmentation) {

			fetchData(processData, windowSize, doSourceSwitchOnce);
			doSourceSwitchOnce = false;

			final String textWindow = processData.toString();

			final BoundaryIndexAndLength boundaryCandidate = findNextSentenceBoundary(
					textWindow, posInWindow);

			if (boundaryCandidate.getIndex() != -1) {

				String text = textWindow;
				if (text.length() - boundaryCandidate.getIndex() < postBoundaryWindowSize) {
					fetchData(processData, windowSize + postBoundaryWindowSize,
							false);
					text = processData.toString();
				}

				if (isSentenceBoundary(text, boundaryCandidate)) {
					moveToOutput(processData, boundaryCandidate.getIndex()
							+ boundaryCandidate.getLength());
					posInWindow = 0;
					windowSize = config.getMinWindowSize();
				} else {
					posInWindow = boundaryCandidate.getIndex()
							+ boundaryCandidate.getLength();
				}

			} else if (inputData.isSourceSwitch(contentMetaData)) {
				moveToOutput(processData, processData.length());
				doSourceSwitchOnce = true;
			} else if (inputData.isFinished() && !inputData.dataAvailable()) {
				moveToOutput(processData, processData.length());
				doSegmentation = false;
			} else {
				windowSize = windowSize + config.getMinWindowSize();
			}
		}
		outputData.setFinished(true);
		if (!config.isQuietMode()) {
			System.out.println((new Date()) + ": Segmentation done.");
		}
	}

	private void moveToOutput(final StringBuilder processData, final int length) {
		while (outputData.availableDataLines() > config.getFileThreadMaxLines()) {
			try {
				Thread.sleep(1);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		final String dataWithoutCR = processData.substring(0, length);
		outputData.addData(dataWithoutCR, contentMetaData);
		processData.delete(0, length);
	}

	public void fetchData(final StringBuilder strBuilder, final int windowSize,
			final boolean doSourceSwitchOnce) {

		while (!inputData.isFinished() && !inputData.dataAvailable()) {
			try {
				Thread.sleep(1);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		boolean doSourceSwitch = doSourceSwitchOnce;

		while (strBuilder.length() < windowSize && !(inputData.isFinished() //
				&& !inputData.dataAvailable())) {

			if (!doSourceSwitch && inputData.isSourceSwitch(contentMetaData)) {
				return;
			}

			if (inputData.dataAvailable()) {
				final ContentAndMetaData contentAndSource = inputData
						.fetchData();
				contentMetaData = contentAndSource.getContentMetaData();
				strBuilder.append(contentAndSource.getContent());
				doSourceSwitch = false;
			}
		}
	}

	protected BoundaryIndexAndLength findNextSentenceBoundary(
			String textWindow, final int posInWindow) {
		BoundaryIndexAndLength closestIndex = new BoundaryIndexAndLength(-1, -1);
		for (final String sentenceBoundary : config.getSentenceBoundaries()) {
			final int foundIndex = textWindow.indexOf(sentenceBoundary,
					posInWindow);
			if (closestIndex.getIndex() == -1
					|| (foundIndex != -1 && foundIndex < closestIndex
							.getIndex())) {
				closestIndex = new BoundaryIndexAndLength(foundIndex,
						sentenceBoundary.length());
				if (foundIndex != -1) {
					textWindow = textWindow.substring(0, foundIndex);
				}
			}
		}
		return closestIndex;
	}

	protected boolean isSentenceBoundary(final String textWindow,
			final BoundaryIndexAndLength boundaryCandidate) {
		if (textWindow.substring(boundaryCandidate.getIndex(),
				boundaryCandidate.getIndex() + boundaryCandidate.getLength())
				.equals(CR)) {
			return true;
		}
		for (final BoundaryProcessor processor : config.getBoundaryProcessors()) {
			if (processor.isNoSentenceBoundary(textWindow, boundaryCandidate)) {
				return false;
			}
		}
		return true;
	}
}
