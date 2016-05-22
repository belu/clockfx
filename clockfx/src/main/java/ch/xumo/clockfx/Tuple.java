package ch.xumo.clockfx;

import static com.google.common.base.Preconditions.checkNotNull;

public class Tuple<T> {

	private final T first;
	private final T second;

	public Tuple(final T first, final T second) {
		this.first = checkNotNull(first);
		this.second = checkNotNull(second);
	}

	public T getFirstEntry() {
		return first;
	}

	public T getSecondEntry() {
		return second;
	}
}
