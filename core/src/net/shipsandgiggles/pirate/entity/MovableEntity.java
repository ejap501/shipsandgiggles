package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.graphics.Texture;

import java.util.UUID;

public abstract class MovableEntity extends Entity {

	private final float maximumSpeed;
	private float speed;
	private final float originalSpeed;

	public MovableEntity(UUID uuid, Texture texture, Location location, EntityType entityType, float maximumHealth, float spawnSpeed, float maximumSpeed, float height, float width) {
		super(uuid, texture, location, entityType, maximumHealth, height, width);

		this.maximumSpeed = maximumSpeed;
		this.originalSpeed = spawnSpeed;
		this.speed = spawnSpeed;
	}

	public float getSpeed() {
		return this.speed;
	}
	public float getOriginalSpeed() {
		return this.originalSpeed;
	}
	public void setSpeed( float speed) {
		 this.speed = speed;
	}

	public float getMaximumSpeed() {
		return this.maximumSpeed;
	}
}