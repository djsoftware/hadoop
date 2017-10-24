package de.uni_leipzig.asv.clarin.common.tuple;

public class LeftOrRight<L, R> {

	public static <L, R> LeftOrRight<L, R> makeLeft(final L left) {
		return new LeftOrRight<L, R>(left, (R) null);
	}

	public static <L, R> LeftOrRight<L, R> makeRight(final R right) {
		return new LeftOrRight<L, R>((L) null, right);
	}

	private LeftOrRight(final L left, final R right) {
		this.left = left;
		this.right = right;
	}

	// {{ left

	private final L left;

	public L left() {
		return left;
	}

	public boolean isLeft() {
		return left != null;
	}

	// }}

	// {{ right

	private final R right;

	public R right() {
		return right;
	}

	public boolean isRight() {
		return right != null;
	}

	// }}

}
