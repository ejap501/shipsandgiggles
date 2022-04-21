package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

/**
 * Movable Entity
 * Assigns the variables to any entity in the game that moves
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Edward Poulter
 * @version 1.0
 */
public abstract class MovableEntity extends Entity {
	// Main data store
	public static float maximumSpeed;
	public static float boostedSpeed;
	private static float originalSpeed;
	private float speed;

	/**
	 * Instantiates the movable entity type
	 *
	 * @param uuid : The unique id of the object
	 * @param texture : Image used for the object
	 * @param location : Position of the object in the world
	 * @param entityType : The type of entity
	 * @param maximumHealth : Maximum health of the entity
	 * @param spawnSpeed : Speed of spawning
	 * @param maximumSpeed : Maximum speed of the entity
	 * @param height : Height of the entity
	 * @param width : Width of the entity
	 */
	public MovableEntity(UUID uuid, Sprite texture, Location location, EntityType entityType, float maximumHealth, float spawnSpeed, float maximumSpeed, float height, float width) {
		super(uuid, texture, location, entityType, maximumHealth, height, width);

		// Instantiates speed values
		this.maximumSpeed = maximumSpeed;
		this.originalSpeed = spawnSpeed;
		this.speed = spawnSpeed;
	}

	/**
	 * @return speed
	 */
	public float getSpeed() {
		return this.speed;
	}

	/**
	 * Sets the speed
	 *
	 * @param speed : Current speed
	 * @param boost : Boost factor to be applied
	 */
	public void setSpeed(float speed, float boost) {
		this.speed = speed * boost;
	}

	/**
	 * Sets the Max speed
	 *
	 * @param change : Changed speed value
	 * @param boost : Boost factor to be applied
	 */
	public void setMaxSpeed(float change, float boost) {
		this.originalSpeed = change;
		this.boostedSpeed = change * boost;
	}

	/**
	 * @return The original speed value
	 */
	public float getOriginalSpeed() {
		return this.originalSpeed;
	}

	/**
	 * @return Maximum speed value
	 */
	public float getMaximumSpeed() {
		return this.maximumSpeed;
	}

	/**
	 * @return Boosted speed value
	 */
	public static float getBoostedSpeed() {
		return boostedSpeed;
	}
}