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
	float width, height;
	Body entityBody;
	float turnSpeed;
	float maxSpeed;
	float driftFactor;
	float turnDirection;
	float driveDirection;

	//protected Ship(File skin, College.Type type) {
	//	this.skin = skin;
	//	this.uniqueId = type.getId();
	//
	//
	//	}

	public Ship(Texture texture, float speed, float maxSpeed, float driftFactor, float turnSpeed, float xPosition, float yPosition, float width, float height){
		this.uniqueId = UUID.randomUUID();
		this.boatTexture = texture;
		this.skin = null;
		this.health = this.getMaximumHealth();
		this.movementSpeed = speed;
		this.turnDirection = 0;
		this.driveDirection = 0;
		this.maxSpeed = maxSpeed;
		this.driftFactor = driftFactor;
		this.turnSpeed = turnSpeed;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.width = width;
		this.height = height;
		Body body;
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(xPosition,yPosition);
		def.fixedRotation = false;
		body = GameScreen.world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((width/2) / Configuration.PixelPerMeter, (height/2)/ Configuration.PixelPerMeter);
		body.createFixture(shape, 1f);
		shape.dispose();
		this.entityBody = body;
	}

	public float getTurnDirection(){
		return this.turnDirection;
	}
	public float getTurnSpeed(){
		return this.turnSpeed;
	}
	public float getMaxSpeed(){
		return this.maxSpeed;
	}
	public float getDriveDirection(){
		return this.driveDirection;
	}
	public float getWidth(){
		return this.width;
	}
	public float getHeight(){
		return this.height;
	}
	public float getyPosition(){
		return this.yPosition;
	}
	public float getxPosition(){
		return this.xPosition;
	}
	public float getDriftFactor(){
		return this.driftFactor;
	}

	public void setTurnDirection(float turnDirection) {
		this.turnDirection = turnDirection;
	}

	public void setDriveDirection(float driveDirection) {
		this.driveDirection = driveDirection;
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

	public Texture getBoatTexture() {
		return boatTexture;
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

	public Vector2 getForwardVelocity(){
		Vector2 currentNormal = this.getEntityBody().getWorldVector(new Vector2(0,1));
		float dotProduct = currentNormal.dot(this.getEntityBody().getLinearVelocity());
		return multiply(dotProduct, currentNormal);
	}

	public Vector2 multiply(float a, Vector2 v){
		return new Vector2(a*v.x, a * v.y);
	}

	public Vector2 getLateralVelocity() {
		Vector2 currentNormal = this.getEntityBody().getWorldVector(new Vector2(1,0));
		float dotProduct = currentNormal.dot(this.getEntityBody().getLinearVelocity());
		return multiply(dotProduct, currentNormal);
	}
}