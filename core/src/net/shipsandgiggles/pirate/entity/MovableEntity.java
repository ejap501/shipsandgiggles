package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

/** assigns the variables to any entity in the game that moves*/

public abstract class MovableEntity extends Entity {

	public static float maximumSpeed;
	public static float boostedSpeed;
	private static float originalSpeed;
	private float speed;

	public MovableEntity(UUID uuid, Sprite texture, Location location, EntityType entityType, float maximumHealth, float spawnSpeed, float maximumSpeed, float height, float width) {
		super(uuid, texture, location, entityType, maximumHealth, height, width);

		this.maximumSpeed = maximumSpeed;
		this.originalSpeed = spawnSpeed;
		this.speed = spawnSpeed;
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setSpeed(float speed, float boost) {
		this.speed = speed * boost;
	}

	public void setMaxSpeed(float change, float boost) {
		this.originalSpeed = change;
		this.boostedSpeed = change * boost;
	}

	public float getOriginalSpeed() {
		return this.originalSpeed;
	}

	public float getMaximumSpeed() {
		return this.maximumSpeed;
	}

	public static float getBoostedSpeed() {
		return boostedSpeed;
	}
}