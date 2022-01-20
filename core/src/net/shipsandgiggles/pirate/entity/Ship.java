package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.college.College;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

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
	Body entityBody;

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
		Body body;
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(1000,1000);
		def.fixedRotation = true;
		body = GameScreen.world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((width/6) / Configuration.PixelPerMeter, (height/6)/ Configuration.PixelPerMeter);
		body.createFixture(shape, 1f);
		shape.dispose();
		this.entityBody = body;
	}

	public float getMovementSpeed(){return this.movementSpeed;}

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

	public Vector2 getPosition(){
		Vector2 position = new Vector2();
		position.x = this.xPosition;
		position.y = this.yPosition;
		return  position;
	}

	public Body getEntityBody(){return this.entityBody;}
}