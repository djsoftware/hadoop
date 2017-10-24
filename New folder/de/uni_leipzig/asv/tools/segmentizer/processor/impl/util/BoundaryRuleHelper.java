package de.uni_leipzig.asv.tools.segmentizer.processor.impl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import de.uni_leipzig.asv.tools.segmentizer.config.SegmentizerConfig;

public class BoundaryRuleHelper {

	public static void loadPatterns(final Map<Pattern, Boolean> compiledPatterns, final URL rulesFile,
			final SegmentizerConfig config) throws IOException, UnsupportedEncodingException {
		final InputStreamReader fileReader = new InputStreamReader(rulesFile.openStream());
		final BufferedReader bufferedReader = new BufferedReader(fileReader);
		while (bufferedReader.ready()) {
			final String prePattern = new String(bufferedReader.readLine().getBytes(), config.getEncoding());

			if (prePattern.startsWith("#") || prePattern.isEmpty()) {
				continue;
			}
			if (!prePattern.startsWith("- ") && !prePattern.startsWith("+ ")) {
				System.err
						.println("Unable to parse line "
								+ prePattern
								+ " in postBoundaryRules-file. Patterns need to specify a decision using '+ '/'- ' in front of the pattern.");
				continue;
			}
			try {
				final Pattern pattern = Pattern.compile(prePattern.substring(2, prePattern.length()));
				final boolean patternDecision = prePattern.startsWith("+ ");
				compiledPatterns.put(pattern, patternDecision);
			} catch (final PatternSyntaxException pse) {
				pse.printStackTrace();
			}
		}
		// LSW
		fileReader.close();
		bufferedReader.close();
	}

}
