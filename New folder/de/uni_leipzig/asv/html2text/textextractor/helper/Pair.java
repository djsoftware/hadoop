package de.uni_leipzig.asv.html2text.textextractor.helper;

public class Pair<F, S> {

	private F first;
	private S second;

	public Pair(final F first, final S second) {
		this.first = first;
		this.second = second;
	}

	public static <F, S> Pair<F, S> create(final F first, final S second) {
		return new Pair<F, S>(first, second);
	}

	public F first() {
		return first;
	}

	public void setFirst(final F first) {
		this.first = first;
	}

	public S second() {
		return second;
	}

	public void setSecond(final S second) {
		this.second = second;
	}

}
