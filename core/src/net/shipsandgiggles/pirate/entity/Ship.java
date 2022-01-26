package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

import java.util.UUID;

public class Ship extends MovableEntity {

	private final Body entityBody;
	private final float turnSpeed;
	private final float driftFactor;

	private float turnDirection;
	private float driveDirection;
	private Sprite texture;
	createNewBall ball;

	public Ship(Sprite texture, float spawnSpeed, float maxSpeed, float driftFactor, float turnSpeed, Location location, float height, float width) {
		super(UUID.randomUUID(), texture, location, EntityType.SHIP, 20, spawnSpeed, maxSpeed, height, width); // TODO: Implement health.

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

		this.entityBody = GameScreen.world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((width / 2) / Configuration.PIXEL_PER_METER, (height / 2) / Configuration.PIXEL_PER_METER);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef. density = 1f;
		fixtureDef.filter.categoryBits = Configuration.Cat_Player;
		this.entityBody.createFixture(fixtureDef);
		shape.dispose();
	}

	@Override
	public void draw(Batch batch) {
		batch.draw(super.getSkin(), super.getLocation().getX(), super.getLocation().getY(), super.getWidth(), super.getHeight());
	}

	@Override
	public void death() {

	}

	public void shoot(World world, Sprite cannonBallSprite, Camera cam, short categoryBits, short maskBit, short groupIndex){
		Vector3 mouse_position = new Vector3(0,0,0);
		mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		cam.unproject(mouse_position);
		ballsManager.createBall(world, new Vector2(this.getEntityBody().getPosition().x, this.getEntityBody().getPosition().y), new Vector2(mouse_position.x, mouse_position.y), cannonBallSprite, categoryBits, maskBit, groupIndex);
	}




	public Vector2 getPosition() {
		Vector2 position = new Vector2();

		position.x = super.getLocation().getX();
		position.y = super.getLocation().getY();

		return position;
	}

	public Sprite getSprite(){
		return this.texture;
	}

	public Body getEntityBody() {
		return this.entityBody;
	}

	public Vector2 getForwardVelocity() {
		Vector2 currentNormal = this.getEntityBody().getWorldVector(new Vector2(0, 1));
		float dotProduct = currentNormal.dot(this.getEntityBody().getLinearVelocity());

		return multiply(dotProduct, currentNormal);
	}

	public Vector2 multiply(float a, Vector2 v) {
		return new Vector2(a * v.x, a * v.y);
	}

	public Vector2 getLateralVelocity() {
		Vector2 currentNormal = this.getEntityBody().getWorldVector(new Vector2(1, 0));
		float dotProduct = currentNormal.dot(this.getEntityBody().getLinearVelocity());

		return multiply(dotProduct, currentNormal);
	}

	public void setTexture(Sprite texture){
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