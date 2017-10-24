package de.uni_leipzig.asv.tools.segmentizer.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

import de.uni_leipzig.asv.tools.segmentizer.config.SegmentizerConfig;
import de.uni_leipzig.asv.tools.segmentizer.data.ContentMetaData;
import de.uni_leipzig.asv.tools.segmentizer.data.SegmentizerData;

public class FileReaderThread extends Thread {

	private static final String CR = "\n";
	private boolean isEndOfFileReached = false;

	private final SegmentizerData segmentizerData;
	private final SegmentizerConfig config;

	public FileReaderThread(final SegmentizerData segmentizerData, final SegmentizerConfig config) {
		this.segmentizerData = segmentizerData;
		this.config = config;
	}

	@Override
	public void run() {
		final URL file = config.getInputFile();
		if (!config.isQuietMode()) {
			System.out.println((new Date()) + ": Reading from: " + file + " using encoding " + config.getEncoding());
		}
		try {

			final BufferedReader inBuff = new BufferedReader(new InputStreamReader(file.openStream(), config
					.getEncoding()));

			ContentMetaData metaData = null;
			final String sourceStartMarker = calculateSourceStartMarker();
			final String sourceEndMarker = calculateSourceEndMarker();

			while (!isEndOfFileReached) {
				while (segmentizerData.availableDataLines() > config.getFileThreadMaxLines()) {
					Thread.sleep(1);
				}

				StringBuffer strBuff = new StringBuffer(readLine(inBuff));
				while (strBuff.length() < config.getFileThreadBuffSize() && inBuff.ready()) {
					strBuff.append(readLine(inBuff));
				}

				if (config.isNameAndSourceMode() || config.isNewSourceMode()) {
					int start = -1;
					while ((start = strBuff.indexOf(sourceStartMarker)) != -1) {
						int end = -1;
						while (((end = strBuff.indexOf(sourceEndMarker, start)) == -1) && !isEndOfFileReached
								&& strBuff.length() < config.getFileThreadBuffSize()) {
							strBuff.append(readLine(inBuff));
						}

						// there was no closing source-tag to be found by us
						if (end == -1) {
							System.out.println("ERROR: No closing tag found near: " + strBuff.toString());
							end = strBuff.length() - sourceEndMarker.length();
						}

						if (config.isNameAndSourceMode() && strBuff.length() < config.getFileThreadBuffSize()) {

							// make sure there are at least two more lines for
							// (<datum>, <sachgebiet>
							for (int lines = 0; lines < 2; lines++) {
								if (isEndOfFileReached) {
									break;
								}
								strBuff.append(readLine(inBuff));
							}
						}
						end = findLastEndMarker(strBuff, end + sourceEndMarker.length(), sourceStartMarker, start);

						if (metaData != null) {
							addData(strBuff.substring(0, start), metaData);
						}

						metaData = createMetaDataFrom(strBuff.substring(start, end));

						strBuff = new StringBuffer(strBuff.substring(end, strBuff.length()));

					}
				}

				addData(strBuff.toString(), metaData);

			}
			inBuff.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		segmentizerData.setFinished(true);
		if (!config.isQuietMode()) {
			System.out.println((new Date()) + ": Done reading from file: " + file);
		}
	}

	private String calculateSourceEndMarker() {
		if (config.isNameAndSourceMode()) {
			return "</quelle>";
		}

		if (config.isNewSourceMode()) {
			return "</source>";
		}

		return null;
	}

	private String calculateSourceStartMarker() {
		if (config.isNameAndSourceMode()) {
			return "<quelle>";
		}

		if (config.isNewSourceMode()) {
			return "<source>";
		}

		return null;
	}

	private String readLine(final BufferedReader inBuff) throws IOException {
		final String line = inBuff.readLine();
		if (line == null) {
			isEndOfFileReached = true;
			return "";
		}
		if (line.isEmpty()) {
			return config.isUseEmptyLineAsBoundary() ? CR : line + " ";
		}
		return config.isUseCarriageReturnAsBoundary() ? line + CR : line + " ";
	}

	private int findLastEndMarker(final StringBuffer strBuff, final int curEndMarker, final String sourceStartMarker,
			final int curStartMarkerPos) {
		final String[] additionalMarkers = new String[] { "</datum>", "</sachgebiet>" };
		int lastMarkerPos = curEndMarker;

		int nextStartMarkerPos = strBuff.indexOf(sourceStartMarker, curStartMarkerPos + 1);
		if (nextStartMarkerPos == -1) {
			nextStartMarkerPos = strBuff.length();
		}

		for (final String additionalMarker : additionalMarkers) {
			final int pos = strBuff.indexOf(additionalMarker, lastMarkerPos);
			if (pos != -1 && pos > lastMarkerPos && lastMarkerPos < nextStartMarkerPos) {
				lastMarkerPos = pos + additionalMarker.length();
			}
		}

		return lastMarkerPos;
	}

	private ContentMetaData createMetaDataFrom(final String str) {
		if (!config.isNameAndSourceMode()) {
			return new ContentMetaData(str);
		}

		String metadataStr = str;
		metadataStr = metadataStr.replace("<datum>", CR + "<datum>");
		metadataStr = metadataStr.replace("<sachgebiet>", CR + "<sachgebiet>");

		return new ContentMetaData(metadataStr);

	}

	private void addData(final String content, final ContentMetaData contentMetaData) {
		segmentizerData.addData(content, contentMetaData);
	}

}
