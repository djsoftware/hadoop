package de.uni_leipzig.asv.html2text.textextractor.helper;

public interface Function<I, O> {

	public O evaluate(I input);

}
