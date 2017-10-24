package de.uni_leipzig.asv.clarin.common.tuple;

public class Maybe<T> {

	public Maybe(T value) {
		this.value = value;
	}
	
	public static <S> Maybe<S> just(S value) {
		return new Maybe<S>(value);
	}

	public static <S> Maybe<S> nothing() {
		return new Maybe<S>(null);
	}

	public boolean isJust() {
		return value != null;
	}
	
	public boolean isNothing() {
		return value == null;
	}

	// {{ value
	private T value;

	public T getValue() {
		return value;
	}
	// }}
	
}
