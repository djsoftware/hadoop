package de.uni_leipzig.asv.clarin.common.tuple;

public class Pair<S, T> {

	public static <S, T> Pair<S, T> create(final S first, final T second) {
		return new Pair<S, T>(first, second);
	}

	protected Pair(final S first, final T second) {
		this.first = first;
		this.second = second;
	}

	// {{
	private final S first;

	public S first() {
		return first;
	}

	// }}

	// {{
	private final T second;

	public T second() {
		return second;
	}
	// }}

}
