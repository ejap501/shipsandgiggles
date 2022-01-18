package net.shipsandgiggles.pirate.entity;

import net.shipsandgiggles.pirate.entity.college.College;

import java.io.File;
import java.util.UUID;

public class Ship implements Entity {

	private final UUID uniqueId;
	private final File skin;

	private double health;

	protected Ship(File skin, College.Type type) {
		this.skin = skin;
		this.uniqueId = type.getId();

		this.health = this.getMaximumHealth();
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
		return EntityType.SHIP;
	}

	@Override
	public double getHealth() {
		return this.health;
	}

	@Override
	public double getMaximumHealth() {
		return 20;
	}

	@Override
	public double getSpeed() {
		return 1;
	}

	@Override
	public double damage(double damage) {
		return (this.health =- damage);
	}
}