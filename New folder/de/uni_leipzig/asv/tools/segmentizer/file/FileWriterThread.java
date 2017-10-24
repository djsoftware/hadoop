package de.uni_leipzig.asv.tools.segmentizer.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import de.uni_leipzig.asv.clarin.common.hash.StringHash;
import de.uni_leipzig.asv.tools.segmentizer.config.Argument;
import de.uni_leipzig.asv.tools.segmentizer.config.SegmentizerConfig;
import de.uni_leipzig.asv.tools.segmentizer.data.ContentAndMetaData;
import de.uni_leipzig.asv.tools.segmentizer.data.ContentMetaData;
import de.uni_leipzig.asv.tools.segmentizer.data.SegmentizerData;

public class FileWriterThread extends Thread {

	private static final String SPACE = " ";
	private static final String DOUBLE_SPACE = "  ";
	private static final String CR = "\n";
	private final SegmentizerData segmentizerData;
	private final SegmentizerConfig config;
	private final StringHash hasher = new StringHash();

	public FileWriterThread(final SegmentizerData segmentizerData, final SegmentizerConfig config) {
		this.segmentizerData = segmentizerData;
		this.config = config;
	}

	@Override
	public void run() {
		final File file = config.getOutputFile();
		if (!config.isQuietMode()) {
			System.out.println((new Date()) + ": Writing to file: " + file + " using encoding " + config.getEncoding());
		}
		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			final OutputStream outputStream = new FileOutputStream(file);
			final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, config.getEncoding());

			if (!config.isNameAndSourceMode() && !config.isNewSourceMode()) {
				outputStreamWriter.write("<?xml version=\"1.0\" encoding=\"" + config.getEncoding() + "\"?>" + CR);
				outputStreamWriter.write("<text>" + CR);
			}

			int sentenceId = 1;
			ContentMetaData contentMetaData = null;
			while (!segmentizerData.isFinished() || segmentizerData.dataAvailable()) {
				while (!segmentizerData.isFinished() && !segmentizerData.dataAvailable()) {
					Thread.sleep(1);
				}
				while (segmentizerData.dataAvailable()) {
					final ContentAndMetaData contentAndSource = segmentizerData.fetchData();
					final String trimmedText = trimText(contentAndSource.getContent(), config);
					if (trimmedText.length() == 0) {
						continue;
					}
					if (!config.isNameAndSourceMode() && !config.isNewSourceMode()) {
						outputStreamWriter.write("<sentence>");
					} else if (contentAndSource.getContentMetaData() == null) {
						System.out.println("Error: " + Argument.NAME_SOURCE.getLongName() + "-option or "
								+ Argument.NEW_SOURCE.getLongName() + "-option"
								+ " was specified, but found no name/source tags in input.");
					} else if (!contentAndSource.getContentMetaData().equals(contentMetaData)) {
						contentMetaData = contentAndSource.getContentMetaData();
						writeMetaData(outputStreamWriter, contentMetaData);
					}
					if (config.isIdAndHashMode()) {
						outputStreamWriter.write(sentenceId + "\t" + hasher.getHash(trimmedText) + "\t");
					}

					outputStreamWriter.write(trimmedText);
					sentenceId++;

					if (!config.isNameAndSourceMode() && !config.isNewSourceMode()) {
						outputStreamWriter.write("</sentence>");
					}
					outputStreamWriter.write(CR);
				}
			}
			if (!config.isNameAndSourceMode() && !config.isNewSourceMode()) {
				outputStreamWriter.write("</text>" + CR);
			}
			outputStreamWriter.close();
			outputStream.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		if (!config.isQuietMode()) {
			System.out.println((new Date()) + ": Done writing to file: " + file);
		}
	}

	private void writeMetaData(final OutputStreamWriter outputStreamWriter, final ContentMetaData contentMetaData)
			throws IOException {
		if (config.isNameAndSourceMode() || config.isNewSourceMode()) {
			outputStreamWriter.write(contentMetaData.getMetadataContent());
			outputStreamWriter.write(CR);
		}

	}

	private String trimText(final String content, final SegmentizerConfig config) {
		String trimmedText = content;
		if (config.isTrimMode()) {
			while (trimmedText.contains(DOUBLE_SPACE)) {
				trimmedText = trimmedText.replace(DOUBLE_SPACE, SPACE);
			}
			trimmedText = trimmedText.trim();
		}
		return trimmedText;
	}
}
