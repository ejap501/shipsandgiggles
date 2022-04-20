package net.shipsandgiggles.pirate.cache;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.concurrent.ConcurrentHashMap;

import net.shipsandgiggles.pirate.util.Preconditions;

/**
 * Cache
 * Class to temporarily store values in memory
 * Allows for storing and removal of data
 *
 * @author Team 23
 * @version 1.0
 */
public class Cache<K, V> {
	/** main data store */
	private final Map<K, V> cache;
	private final Predicate<V> argumentCheck;

	/**
	 * Initialises the cache
	 *
	 * @param argumentCheck : Store of predicate values
	 */
	public Cache(Predicate<V> argumentCheck) {
		this.argumentCheck = argumentCheck;
		this.cache = new ConcurrentHashMap<>();
	}

	/**
	 * Storing to the cache
	 * Checks validity of stored identifiers and values
	 *
	 * @param identifier : Identifier value data is to be stored under
	 * @param value : Data to be stored
	 */
	public void cache(K identifier, V value) {
		/** validity checks */
		Preconditions.checkNotNull(identifier, "Identifier for a cache cannot be null!");
		if (this.argumentCheck != null) {
			Preconditions.checkArgument(this.argumentCheck, "Entity {val} cannot be added as it has failed the predicate!", value);
		}

		/** containment check */
		if (this.cache.containsValue(value)) {
			throw new IllegalStateException("Cache already contains value " + value + "!");
		}

		/** stores data */
		this.cache.put(identifier, value);
	}

	/**
	 * Removing from the cache
	 * Checks validity of stored identifiers and values
	 *
	 * @param toRemove : Identifier value data is to be removed from
	 */
	public void remove(K toRemove) {
		/** validity check */
		Preconditions.checkNotNull(toRemove, "Identifier for removal cannot be null!");

		/** containment check */
		if (!this.cache.containsKey(toRemove)) {
			throw new NullPointerException("The value " + toRemove + " is not currently in the cache!");
		}

		/** removes data */
		this.cache.remove(toRemove);
	}

	/**
	 * Retieves from the cache
	 * Checks validity of stored identifiers and values
	 *
	 * @param toFind : Identifier value data is to be collected from
	 */
	public Optional<V> find(K toFind) {
		/** validity check */
		Preconditions.checkNotNull(toFind, "Identifier for a cache cannot be null!");

		/** returned data */
		return Optional.ofNullable(this.cache.get(toFind));
	}
}
