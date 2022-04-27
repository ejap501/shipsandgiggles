package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

// TODO: Implement with actual engine (mainly return types).

/**
 * Entity
 * Base class of all entities in the game.
 *
 * @author Team 23
 * @version 1.0
 */
public abstract class Entity {
	// Main data store
	private final UUID uuid;
	public final Sprite texture;
	private final Location location;
	private final EntityType entityType;
	private final float maximumHealth;
	private final float height;
	private final float width;
	public float health;

	/**
	 * Instantiates the entity
	 *
	 * @param uuid : The unique id of the object
	 * @param texture : Image used for the object
	 * @param location : Position of the object in the world
	 * @param maximumHealth : Maximum health of the entity
	 * @param height : Height of the entity
	 * @param width : Width of the entity
	 */
	public Entity(UUID uuid, Sprite texture, Location location, EntityType entityType, float maximumHealth, float height, float width) {
		this.uuid = uuid;
		this.texture = texture;
		this.location = location;
		this.entityType = entityType;
		this.maximumHealth = maximumHealth;
		this.height = height;
		this.width = width;

		this.health = maximumHealth;
	}

	/**
	 * The randomly generated {@link UUID} representing the entity.
	 *
	 * @return Current UUID.
	 */
	public UUID getUniqueId() {
		return this.uuid;
	}

	/**
	 * The skin that should be displayed to the user for this entity.
	 *
	 * @return Skin to be displayed.
	 */
	public Sprite getSkin() {
		return this.texture;
	}

	/**
	 * Current location of the entity.
	 *
	 * @return X
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * @return EntityType representing (e.g. Ship, College)
	 */
	public EntityType getEntityType() {
		return this.entityType;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	/**
	 * Current health of the entity, where <= 0 represents a dead entity.
	 *
	 * @return Current Health.
	 */
	public double getHealth() {
		return this.health;
	}

	/**
	 * Maximum health of the entity when it spawns. If this is infinite, it will be -1.
	 *
	 * @return Defined maximum health.
	 */
	public double getMaximumHealth() {
		return this.maximumHealth;
	}

	/**
	 * @param damage Damage you wish the entity to take.
	 * @return Current health after damage (i.e. {@link #getHealth() - damage}
	 */
	public float damage(float damage) {
		if(this.health == 1){
			this.death();
			return 0f;
		}
		if ((this.health - damage) <= 0f) {
			this.health = 0;
			this.death();
			return 0f;
		}
		if (this.health == 0){
			return 0f;
		}

		this.health -= damage;

		return this.health;
	}

	public void draw(Batch batch) {
	}

	public abstract void shootPlayer(Ship player);

	public abstract void death();

}
