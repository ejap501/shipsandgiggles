package net.shipsandgiggles.pirate.util;

import java.util.function.Predicate;

/**
 * Preconditions
 * Preconditions for the game to check if it's null or not
 *
 * @author Team 23
 * */
public class Preconditions {
	/**
	 * Determines if null
	 * @param value : Value stored in data type
	 * @param error : Error string to be thrown if null
	 * @param <T> : Data Type
	 */
	public static <T> void checkNotNull(T value, String error) {
		if (value == null) {
			throw new NullPointerException(error);
		}
	}

	/**
	 * Determines if test throws a state exception for given predicates
	 * Checks for null pointer exception
	 *
	 * @param test : Predicates to test
	 * @param error : Error string to be thrown if null
	 * @param value : Value stored in data type
	 * @param <T> : Data Type
	 */
	public static <T> void checkArgument(Predicate<T> test, String error, T value) {
		if (!test.test(value)) {
			throw new IllegalStateException(error.replace("{val}", value.toString()));
		}

		if (value == null) {
			throw new NullPointerException();
		}
	}
}