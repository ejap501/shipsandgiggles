package net.shipsandgiggles.pirate.util;

import java.util.function.Predicate;

public class Preconditions {

	public static <T> void checkNotNull(T value, String error) {
		if (value == null) {
			throw new NullPointerException(error);
		}
	}

	public static <T> void checkArgument(Predicate<T> test, String error, T value) {
		if (!test.test(value)) {
			throw new IllegalStateException(error.replace("{val}", value.toString()));
		}

		if (value == null) {
			throw new NullPointerException();
		}
	}
}