package de.uni_leipzig.asv.tools.segmentizer.app;

import java.io.File;
import java.net.MalformedURLException;

import de.uni_leipzig.asv.tools.segmentizer.config.Argument;
import de.uni_leipzig.asv.tools.segmentizer.config.SegmentizerConfig;
import de.uni_leipzig.asv.tools.segmentizer.config.impl.SegmentizerConfigImpl;
import de.uni_leipzig.asv.tools.segmentizer.data.SegmentizerData;
import de.uni_leipzig.asv.tools.segmentizer.file.FileReaderThread;
import de.uni_leipzig.asv.tools.segmentizer.file.FileWriterThread;

public class AsvSegmentizerApp {

	private static final String VERSION = "ASV Sentence Segmentizer 2010 v0.1.11 by Volker Boehlke (BoehlkeV@informatik.uni-leipzig.de)";

	public static void main(final String[] args) {
		final SegmentizerConfig config = buildConfigFromArgs(args);
		final AsvSegmentizerApp segmentizerMain = new AsvSegmentizerApp();
		segmentizerMain.doSegmentation(config);
	}

	public void doSegmentation(final SegmentizerConfig config) {
		if (!config.isQuietMode()) {
			printInfoText();
		}
		final SegmentizerData inputData = new SegmentizerData();
		final SegmentizerData outputData = new SegmentizerData();

		final FileReaderThread reader = new FileReaderThread(inputData, config);
		reader.setName("FileReaderThread");

		final FileWriterThread writer = new FileWriterThread(outputData, config);
		writer.setName("FileWriterThread");

		reader.start();
		writer.start();
		final AsvSegmentizerImpl segmentizer = new AsvSegmentizerImpl(inputData, outputData, config);
		segmentizer.doSegmentation();
	}

	private static String scanArgsFor(final Argument argument, final String[] args, final String errorMessage) {
		final String result = scanArgsFor(argument, args);
		if (result == null) {
			printErrorAndExit(errorMessage);
		}
		return result;
	}

	private static String scanArgsFor(final Argument argument, final String[] args) {
		String value = null;
		for (final String arg : args) {
			final String argumentShort = "-" + argument.getShortName();
			final String argumentLong = "--" + argument.getLongName();
			if (value != null && (arg.startsWith(argumentShort + "=") || arg.startsWith(argumentLong + "="))) {
				printErrorAndExit("Multiple definition of param " + argument.getLongName());
				System.exit(-1);
			} else if (arg.startsWith(argumentShort + "=")) {
				value = arg.substring(argumentShort.length() + 1, arg.length());
			} else if (arg.startsWith(argumentLong + "=")) {
				value = arg.substring(argumentLong.length() + 1, arg.length());
			} else if (arg.equals(argumentShort) || arg.equals(argumentLong)) {
				value = "true";
			}

		}
		return value;
	}

	private static SegmentizerConfig buildConfigFromArgs(final String[] args) {

		if (scanArgsFor(Argument.HELP, args) != null) {
			printHelpText();
			System.exit(0);
		}

		checkArguments(args);

		final SegmentizerConfigImpl config = new SegmentizerConfigImpl();

		if (scanArgsFor(Argument.QUIET, args) != null) {
			config.setQuietMode(fetchBooleanArg(scanArgsFor(Argument.QUIET, args)));
		}

		if (scanArgsFor(Argument.TRIM, args) != null) {
			config.setTrimMode(fetchBooleanArg(scanArgsFor(Argument.TRIM, args)));
		}

		if (scanArgsFor(Argument.NAME_SOURCE, args) != null) {
			config.setNameAndSourceMode(fetchBooleanArg(scanArgsFor(Argument.NAME_SOURCE, args)));
		} else if (scanArgsFor(Argument.NEW_SOURCE, args) != null) {
			config.setNewSourceMode(fetchBooleanArg(scanArgsFor(Argument.NEW_SOURCE, args)));
		}

		if (scanArgsFor(Argument.SENTENCE_ID_AND_HASH, args) != null) {
			config.setIdAndHashMode(fetchBooleanArg(scanArgsFor(Argument.SENTENCE_ID_AND_HASH, args)));
		}

		if (scanArgsFor(Argument.CARRIAGE_RETURN, args) != null) {
			config.setUseCarriageReturnAsBoundary(fetchBooleanArg(scanArgsFor(Argument.CARRIAGE_RETURN, args)));
		}

		if (scanArgsFor(Argument.EMPTY_LINE, args) != null) {
			config.setUseEmptyLineAsBoundary(fetchBooleanArg(scanArgsFor(Argument.EMPTY_LINE, args)));
		}

		try {
			config.setInputFile(new File(scanArgsFor(Argument.INPUT_FILE, args, "No input file specified!")).toURI()
					.toURL());
			config.setOutputFile(new File(scanArgsFor(Argument.OUTPUT_FILE, args, "No output file specified!")));
			config.setPreBoundaryListFile(new File(scanArgsFor(Argument.PRE_BOUNDARY_LIST_FILE, args,
					"No pre-boundary list file specified!")).toURI().toURL());
			config.setPreBoundaryRulesFile(new File(scanArgsFor(Argument.PRE_BOUNDARY_RULES_FILE, args,
					"No pre-boundary rules file specified!")).toURI().toURL());
			config.setPostBoundaryListFile(new File(scanArgsFor(Argument.POST_BOUNDARY_LIST_FILE, args,
					"No post-boundary list file specified!")).toURI().toURL());
			config.setPostBoundaryRulesFile(new File(scanArgsFor(Argument.POST_BOUNDARY_RULES_FILE, args,
					"No post-boundary rules file specified!")).toURI().toURL());

			if (scanArgsFor(Argument.BOUNDARIES_FILE, args) != null) {
				config.setSentenceBoundariesFile(new File(scanArgsFor(Argument.BOUNDARIES_FILE, args)).toURI().toURL());
			}
		} catch (final MalformedURLException e) {
			System.err.println(e);
			printErrorAndExit(e.getMessage());
		}

		final String encoding = scanArgsFor(Argument.ENCODING, args);
		if (encoding != null) {
			config.setEncoding(encoding);
		}

		return config;
	}

	private static void checkArguments(final String[] args) {
		for (final String arg : args) {
			boolean argumentKnown = false;
			for (final Argument argument : Argument.values()) {
				if (arg.startsWith("-" + argument.getShortName() + "=") //
						|| arg.equals("-" + argument.getShortName()) //
						|| arg.startsWith("--" + argument.getLongName() + "=") //
						|| arg.equals("--" + argument.getLongName())) {
					argumentKnown = true;
					break;
				}
			}
			if (argumentKnown == false) {
				printErrorAndExit("Error: Unknown parameter '" + arg + "'");
			}
		}

		for (final Argument argument : Argument.values()) {
			if (!argument.isOptional()) {
				scanArgsFor(argument, args, "Error: Unable to find mandatory parameter '" + argument.getShortName()
						+ "' / '" + argument.getLongName() + "'");
			}
		}
	}

	private static boolean fetchBooleanArg(final String value) {
		if (value.equalsIgnoreCase("true")) {
			return true;
		} else if (value.equalsIgnoreCase("false")) {
			return false;
		}
		printErrorAndExit("Unable to interpret value " + value);
		return false;
	}

	private static void printErrorAndExit(final String errorMessage) {
		System.out.println("ERROR: " + errorMessage + "\n");
		printHelpText();
		System.exit(-1);
	}

	private static void printInfoText() {
		System.out.println(VERSION);
	}

	private static void printHelpText() {
		printInfoText();
		final StringBuilder sb = new StringBuilder();

		sb.append("usage example: java -jar ASVSegmentizer.jar --trim=true -" //
				+ Argument.INPUT_FILE.getShortName() //
				+ "=MyInputFile.txt -" //
				+ Argument.OUTPUT_FILE.getShortName() //
				+ "=MyOutputFile.txt -" //
				+ Argument.PRE_BOUNDARY_LIST_FILE.getShortName() //
				+ "=MyPreListFile.txt -" // 
				+ Argument.POST_BOUNDARY_LIST_FILE.getShortName() //
				+ "=MyPostListFile.txt -" // 
				+ Argument.PRE_BOUNDARY_RULES_FILE.getShortName() //
				+ "=MyPreRulesFile.txt -" // 
				+ Argument.POST_BOUNDARY_RULES_FILE.getShortName() //
				+ "=MyPostListFile.txt");

		sb.append("\n");

		sb.append("arguments:\n");
		for (final Argument arg : Argument.values()) {
			if (!arg.isOptional()) {
				sb.append(" " + arg.getShortName() + " / " + arg.getLongName() + " : " + arg.getDescription() + "\n");
			}
		}

		sb.append("optional arguments:\n");
		for (final Argument arg : Argument.values()) {
			if (arg.isOptional()) {
				sb.append(" " + arg.getShortName() + " / " + arg.getLongName() + " : " + arg.getDescription() + "\n");
			}
		}

		System.out.println(sb.toString());
	}
}
