package net.shipsandgiggles.pirate.currency;

import net.shipsandgiggles.pirate.entity.Ship;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Currency {

	public static Currency INSTANCE;

	private final Map<Type, Integer> currencyValues;

	private Currency() {
		if (INSTANCE != null) {
			throw new IllegalStateException("Cannot initialise duplicate Singleton Class (Currency!)");
		}

		this.currencyValues = new ConcurrentHashMap<>();
	}

	/**
	 * Singleton class.
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
	 * @param type Currency Type you wish to receive.
	 * @return Current balance of that currency.
	 */
	public int balance(Type type) {
		return this.currencyValues.getOrDefault(type, 0);
	}

	/**
	 * @param type   Type of currency you wish to give.
	 * @param amount Amount you wish to give the player.
	 * @return New updated balance.
	 */
	public int give(Type type, int amount) {
		return this.currencyValues.compute(type, (ign, val) -> {
			if (val == null) {
				return amount;
			}
			if (type == Type.GOLD){

				return val + (amount * Ship.coinMulti);

			}


			return val + amount;
		});
	}

	/**
	 * @param type      Type of currency you wish to take.
	 * @param amount    Amount you wish to take.
	 */
	public void take(Type type, int amount) {
		int balance = this.balance(type);

		if (this.balance(type) >= amount) {

			this.currencyValues.put(type, balance - amount);

		}

	}

	/**
	 * Currency Types.
	 */
	public enum Type {
		COINS("Coins"),
		POINTS("Points"),
		GOLD("Gold");

		private final String fancyName;

		Type(String fancyName) {
			this.fancyName = fancyName;
		}

		/**
		 * @return Name of the currency to display to the player.
		 */
		public String getFancyName() {
			return fancyName;
		}
	}
}