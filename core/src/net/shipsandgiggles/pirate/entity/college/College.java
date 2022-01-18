package net.shipsandgiggles.pirate.entity.college;

import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.EntityType;

import java.io.File;
import java.util.UUID;

/**
 * College data that allows us to perform animations / fights more easily.
 */
public abstract class College implements Entity {

	private final UUID uniqueId;
	private final File skin;
	private final Type type;

	private double health;

	protected College(File skin, Type type) {
		this.skin = skin;
		this.uniqueId = type.getId();
		this.type = type;

		this.health = this.getMaximumHealth();
	}

	@Override
	public double getMaximumHealth() {
		return 20;
	}

	@Override
	public UUID getUniqueId() {
		return this.uniqueId;
	}

	@Override
	public File getSkin() {
		return this.skin;
	}

	@Override
	public void getLocation() {

	}

	@Override
	public EntityType getEntityType() {
		return EntityType.COLLEGE;
	}

	@Override
	public double getHealth() {
		return 20;
	}

	@Override
	public double damage(double damage) {
		return (this.health = this.health - damage);
	}

	@Override
	public double getSpeed() {
		return -1;
	}

	public Type getType() {
		return type;
	}

	public abstract boolean perform();

	/**
	 * Types of college - allows us to keep track.
	 */
	public enum Type {

		LANGWITH;

		private final UUID randomId;

		/**
		 * Assign static value at runtime, as value will not change and maximum of 1 college.
		 **/
		Type() {
			this.randomId = UUID.randomUUID();
		}

		/**
		 * @return Unique identifier associated with this UUID.
		 */
		public UUID getId() {
			return randomId;
		}
	}
}
