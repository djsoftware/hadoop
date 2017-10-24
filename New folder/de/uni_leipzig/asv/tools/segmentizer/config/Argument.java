package de.uni_leipzig.asv.tools.segmentizer.config;

public enum Argument {

	HELP("h", "help", true, "Prints this help."), //
	INPUT_FILE("i", "inputFile", false, "The input to be segmentized."), //
	OUTPUT_FILE("o", "outputFile", false, "The file the segmentized output should be written to."), //
	BOUNDARIES_FILE("bf", "boundariesFile", true,
			"The file specifying the sentence boundary candidates to be used. If not specified '.?!' are used."), // 
	PRE_BOUNDARY_LIST_FILE(
			"a",
			"preBoundaryListFile",
			false,
			"The file containing those tokens that should never be present directly in front of a sentence boundary (includes the sentence boundary chars; example: abbreviations like 'Dr.')."), //
	POST_BOUNDARY_LIST_FILE("b", "postBoundaryListFile", false,
			"The file containing those tokens that should never be present directly after a sentence boundary."), //
	PRE_BOUNDARY_RULES_FILE("r", "preBoundaryRulesFile", false,
			"The file containing the patterns that should/should not be recognized directly in front of a sentence boundary."), // 
	POST_BOUNDARY_RULES_FILE("s", "postBoundaryRulesFile", false,
			"The file containing the patterns that should/should not be recognized directly after a sentence boundary."), // 
	QUIET("q", "quiet", true, "Put no messages to console. Default: false."), //
	TRIM("t", "trim", true,
			"Trims all sentences (remove whitespaces at begin/end) before writing to the output. Default: true."), //
	NEW_SOURCE("nsm", "newSourceMode", true,
			"Use source specifications (<source><location>...</source>) in input-/output file. Default: false."), //
	NAME_SOURCE("n", "nameAndSource", true,
			"Use name&source specifications (<quelle><name>...) in input-/output file. Default: false."), //
	SENTENCE_ID_AND_HASH("ih", "idAndHash", true,
			"Calculates an id and hash for each sentence and puts it in front of each entry in the output. Default: false."), // 
	EMPTY_LINE("el", "emptyLine", true, "Interpret empty lines as sentence boundary. Default: true."), // 
	CARRIAGE_RETURN("c", "carriageReturn", true, "Interpret carriage returns as sentences boundary. Default: true."), // 
	ENCODING("e", "encoding", true, "Specifies the (java)encoding used in input/output. Default: UTF-8.");

	private final String shortName;
	private final String longName;
	private final boolean isOptional;
	private final String description;

	Argument(final String shortName, final String longName, final boolean isOptional, final String description) {
		this.shortName = shortName;
		this.longName = longName;
		this.isOptional = isOptional;
		this.description = description;
	}

	public String getShortName() {
		return shortName;
	}

	public String getLongName() {
		return longName;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "-" + getShortName() + " --" + getLongName();
	}

}
