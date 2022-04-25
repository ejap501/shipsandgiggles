package net.shipsandgiggles.pirate.currency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

/**
 * Currency
 * Store of the currency based code
 * Used for code cleanup
 *
 * @author Team 23
 * @author Team 22: Sam Pearson
 * @version 2.0
 */
public class Currency {
	// Main data store
	public static Currency INSTANCE;
	private final Map<Type, Integer> currencyValues;

	/**
	 * Checks the validity of an instance and creates a store
	 */
	private Currency() {
		if (INSTANCE != null) {
			throw new IllegalStateException("Cannot initialise duplicate Singleton Class (Currency!)");
		}
		this.currencyValues = new ConcurrentHashMap<>();
	}

	/**
	 * Singleton class.
	 * Fetches the currency instance
	 *
	 * @return Instance of the Currency Manager
	 */
	public static Currency get() {
		if (INSTANCE == null) {
			INSTANCE = new Currency();
		}
		return INSTANCE;
	}




	/**
	 * Provides the instance balance
	 *
	 * @param type Currency Type you wish to receive.
	 * @return Current balance of that currency.
	 */
	public int balance(Type type) {
		return this.currencyValues.getOrDefault(type, 0);
	}

	/**
	 * Updates specific instance's balances
	 * Increments specified balances
	 * Edited from original
	 *
	 * @param type   Type of currency you wish to give.
	 * @param amount Amount you wish to give the player.
	 * @return New updated balance.
	 */
	public int give(Type type, int amount) {
		/*
		 * Returns the new value and its type for specific cases
		 * Allows for multiple value stores to be held and updated
		 * */
		return this.currencyValues.compute(type, (ign, val) -> {
			if (type == Type.GOLD){
				if (val == null){
					val = 0;
				}
				return (val + (amount * Ship.coinMulti));
			}else if (type == Type.POINTS) {

				if (val == null){
					val = 0;
				}
				return val + (amount * Ship.pointMulti);
			}
			if (val == null) {
				return amount;
			}
			return val + amount;
		});
	}

	/**
	 * Updates specific instance's balances
	 * Decrements specified balances
	 *
	 * @param type      Type of currency you wish to take.
	 * @param amount    Amount you wish to take.
	 */
	public void take(Type type, int amount) {
		// Retrieves the balance of a given type
		int balance = this.balance(type);

		// Reduces the value for its given type
		if (this.balance(type) >= amount) {
			this.currencyValues.put(type, balance - amount);
		}
	}

	/**
	 * Specifies currency Types.
	 */
	public enum Type {
		// Sets types
		POINTS("Points"),
		GOLD("Gold");

		// Assigns a relevent more detailed name
		private final String fancyName;
		Type(String fancyName) {
			this.fancyName = fancyName;
		}

		/**
		 * Retrieves the more detailed name for a given type
		 *
		 * @return Name of the currency to display to the player.
		 */
		public String getFancyName() {
			return fancyName;
		}
	}
}