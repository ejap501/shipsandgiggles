package net.shipsandgiggles.pirate.entity.type;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.EntityType;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

import java.util.UUID;

public class Ship extends ControlledEntity {

	private final float turnSpeed;
	private final float driftFactor;

	private float turnDirection;
	private float driveDirection;
	private Sprite texture;

	public Ship(Sprite texture, float spawnSpeed, float maxSpeed, float driftFactor, float turnSpeed, Location location, float boundingBox, Type type) {
		super(UUID.randomUUID(), texture, location, EntityType.SHIP, 20, spawnSpeed, maxSpeed, texture.getHeight(), texture.getWidth(), boundingBox); // TODO: Implement health.

		this.turnDirection = 0;
		this.driveDirection = 0;
		this.driftFactor = driftFactor;
		this.turnSpeed = turnSpeed;
		this.texture = texture;

		// Creation of Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(location.getX(), location.getY());
		bodyDef.fixedRotation = false;

		Body entityBody = GameScreen.world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((this.getWidth() / 2) / Configuration.PIXEL_PER_METER, (this.getHeight() / 2) / Configuration.PIXEL_PER_METER);
		entityBody.createFixture(shape, 1f);
		this.setBody(entityBody);
		shape.dispose();

		this.init(type);
	}

	@Override
	public void death() {

	}

	public Vector2 getPosition() {
		Vector2 position = new Vector2();

		position.x = super.getLocation().getX();
		position.y = super.getLocation().getY();

		return position;
	}

	public Sprite getSprite() {
		return this.texture;
	}

	public Vector2 getForwardVelocity() {
		Vector2 currentNormal = this.getBody().getWorldVector(new Vector2(0, 1));
		float dotProduct = currentNormal.dot(this.getBody().getLinearVelocity());

		return multiply(dotProduct, currentNormal);
	}

	public Vector2 multiply(float a, Vector2 v) {
		return new Vector2(a * v.x, a * v.y);
	}

	public Vector2 getLateralVelocity() {
		Vector2 currentNormal = this.getBody().getWorldVector(new Vector2(1, 0));
		float dotProduct = currentNormal.dot(this.getBody().getLinearVelocity());

		return multiply(dotProduct, currentNormal);
	}

	public void setTexture(Sprite texture) {
		this.texture = texture;
	}

	public float getTurnDirection() {
		return this.turnDirection;
	}

	public void setTurnDirection(float turnDirection) {
		this.turnDirection = turnDirection;
	}

	public float getTurnSpeed() {
		return this.turnSpeed;
	}

	public float getDriveDirection() {
		return this.driveDirection;
	}

	public void setDriveDirection(float driveDirection) {
		this.driveDirection = driveDirection;
	}

	public float getDriftFactor() {
		return this.driftFactor;
	}
}