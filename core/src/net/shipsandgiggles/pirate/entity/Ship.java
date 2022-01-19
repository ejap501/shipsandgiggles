package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.college.College;

import java.io.File;
import java.util.UUID;

public class Ship implements Entity {

	private final UUID uniqueId;
	private final File skin;

	private double health;

	float movementSpeed; // m per s
	Texture boatTexture;
	float xPosition, yPosition;
	int width, height;

	//protected Ship(File skin, College.Type type) {
	//	this.skin = skin;
	//	this.uniqueId = type.getId();
	//
	//
	//	}

	public Ship(Texture texture, float speed, float xPosition, float yPosition, int width, int height){
		this.uniqueId = UUID.randomUUID();
		this.boatTexture = texture;
		this.skin = null;
		this.health = this.getMaximumHealth();
		this.movementSpeed = speed;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.width = width;
		this.height = height;
	}

	public void draw(Batch batch){
		batch.draw(this.boatTexture, this.xPosition, this.yPosition, this.width, this.height);
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