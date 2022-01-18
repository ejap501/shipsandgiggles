package net.shipsandgiggles.pirate.entity;

import java.io.File;
import java.util.UUID;

// TODO: Implement with actual engine (mainly return types).

/**
 * Base class of all entities in the game.
 */
public interface Entity {

	/**
	 * The randomly generated {@link UUID} representing the entity.
	 *
	 * @return Current UUID.
	 */
	UUID getUniqueId();

	/**
	 * The skin that should be displayed to the user for this entity.
	 *
	 * @return Skin to be displayed.
	 */
	File getSkin();

	/**
	 * Current location of the entity.
	 *
	 * @return X
	 */
	void getLocation();

	/**
	 * @return EntityType representing (e.g. Ship, College)
	 */
	EntityType getEntityType();

	/**
	 * Current health of the entity, where <= 0 represents a dead entity.
	 *
	 * @return Current Health.
	 */
	double getHealth();

	/**
	 * Maximum health of the entity when it spawns. If this is infinite, it will be -1.
	 *
	 * @return Defined maximum health.
	 */
	double getMaximumHealth();

	/**
	 * Speed of the entity when it is moving. If it is static, it will be -1.
	 *
	 * @return Current Speed.
	 */
	double getSpeed();

	/**
	 * @param damage Damage you wish the entity to take.
	 * @return Current health after damage (i.e. {@link #getHealth() - damage}
	 */
	double damage(double damage);

}
