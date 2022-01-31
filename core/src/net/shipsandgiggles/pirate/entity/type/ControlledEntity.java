package net.shipsandgiggles.pirate.entity.type;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import net.shipsandgiggles.pirate.entity.EntityType;

import java.util.UUID;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

public abstract class ControlledEntity extends MovableEntity implements Steerable<Vector2> {

	private final float boundingBox;
	private SteeringBehavior<Vector2> behavior;
	private SteeringAcceleration<Vector2> steeringOutput;
	private boolean tagged;
	private float maxLinearSpeed, maxLinearAcceleration, maxAngularSpeed, maxAngularAcceleration;
	private float zeroLinearSpeedThreshold;
	private Type type;
	private Body target;
	private float amountOfRotations = 0;
	private boolean independentFacing = false; // defines if the entity can move in a direction other than the way it faces)
	private float angleToTarget = 0;

	public ControlledEntity(UUID uuid, Sprite texture, net.shipsandgiggles.pirate.entity.Location location, EntityType entityType, float maximumHealth, float spawnSpeed, float maximumSpeed, float height, float width, float boundingBox) {
		super(uuid, texture, location, entityType, maximumHealth, spawnSpeed, maximumSpeed, height, width);

		this.boundingBox = boundingBox;
	}

	public void init(Type type) {
		this.maxLinearSpeed = 5000;
		this.maxLinearAcceleration = 5000;
		this.maxAngularSpeed = 90;
		this.maxAngularAcceleration = 30;
		this.zeroLinearSpeedThreshold = (type == Type.PLAYER) ? 0.1f : 0.01f;
		this.type = type;
		this.tagged = false;
		this.steeringOutput = new SteeringAcceleration<>(new Vector2());

		this.getBody().setFixedRotation(type == Type.ENEMY);
		this.getBody().setUserData(this);
		this.getBody().setLinearDamping(1f);
	}

	public boolean isIndependentFacing() {
		return independentFacing;
	}

	public void setIndependentFacing(boolean independentFacing) {
		this.independentFacing = independentFacing;
	}

	protected void applySteering() {
		if (this.type.equals(Type.PLAYER)) {
			return;
		}

		boolean anyAccelerations = false;
		float speedMultiplier = this.type.getSpeedMultiplier();
		float turnMultiplier = this.type.getTurnMultiplier();

		// Update position and linear velocity.
		if (!this.steeringOutput.linear.isZero()) {
			// this method internally scales the force by deltaTime
			getBody().applyForceToCenter(new Vector2(this.steeringOutput.linear.x * speedMultiplier, (this.steeringOutput.linear.y * speedMultiplier)), true);
			anyAccelerations = true;
		}

		// Update orientation and angular velocity
		if (isIndependentFacing()) {
			if (this.steeringOutput.angular != 0) {
				// this method internally scales the torque by deltaTime
				this.getBody().applyTorque(this.steeringOutput.angular * turnMultiplier, true);
				anyAccelerations = true;
			}
		} else {
			// If we haven't got any velocity, then we can do nothing.
			if (this.steeringOutput.linear.len() > 25f) {
				float newOrientation = vectorToAngle(this.getLinearVelocity());
				//System.out.println();
				//body.setAngularVelocity((newOrientation - getAngularVelocity()) * turnMultiplier * deltaTime); // this is superfluous if independentFacing is always true
				//body.setTransform(body.getPosition(), newOrientation)
				//System.out.println(Math.toDegrees(((float)Math.atan2(this.target.getPosition().y - this.getPosition().y, this.target.getPosition().x - this.getPosition().x) )));

				//this.setAngleToTarget(this.getAngleToTarget() + ((float)Math.atan2(this.target.getPosition().y - this.getPosition().y, this.target.getPosition().x - this.getPosition().x) - 1.5708f - this.angleToTarget) * turnMultiplier * PIXEL_PER_METER);
				this.setAngleToTarget((float)Math.atan2(this.target.getPosition().y - this.getPosition().y, this.target.getPosition().x - this.getPosition().x) - 1.5708f);
				this.getBody().setTransform(this.getBody().getPosition().x, this.getBody().getPosition().y, this.getAngleToTarget());
			}
		}

		if (anyAccelerations) {
			// Cap the linear speed
			Vector2 velocity = getBody().getLinearVelocity();
			float currentSpeedSquare = velocity.len2();
			float maxLinearSpeed = getMaxLinearSpeed();
			if (currentSpeedSquare > (maxLinearSpeed * maxLinearSpeed)) {
				getBody().setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
			}
			// Cap the angular speed
			float maxAngVelocity = getMaxAngularSpeed();
			if (getBody().getAngularVelocity() > maxAngVelocity) {
				getBody().setAngularVelocity(maxAngVelocity);
			}
		}
	}

	public void update(float delta, Batch batch) {
		if (this.type.equals(Type.PLAYER)) {
			return;
		}

		if (behavior != null) {
			this.steeringOutput = behavior.calculateSteering(steeringOutput);
			this.applySteering();
		}

		this.draw(batch);
	}

	@Override
	public Vector2 getLinearVelocity() {
		return super.getBody().getPosition();
	}

	public float getAmountOfRotations() {
		return amountOfRotations;
	}

	public void setAmountOfRotations(float amountOfRotations) {
		this.amountOfRotations = amountOfRotations;
	}

	@Override
	public float getAngularVelocity() {
		return this.getBody().getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return this.boundingBox;
	}

	public float getAngleToTarget() {
		return angleToTarget;
	}

	public void setAngleToTarget(float angleToTarget) {
		this.angleToTarget = angleToTarget;
	}

	@Override
	public boolean isTagged() {
		return this.tagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}


	@Override
	public float getZeroLinearSpeedThreshold() {
		return this.zeroLinearSpeedThreshold;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		this.zeroLinearSpeedThreshold = value;
	}

	@Override
	public float getMaxLinearSpeed() {
		return this.maxLinearSpeed;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return this.maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearSpeed = maxLinearAcceleration;
	}

	public Body getTarget() {
		return target;
	}

	public void setTarget(Body body) {
		this.target = body;
	}

	@Override
	public float getMaxAngularSpeed() {
		return this.maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return this.maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getPosition() {
		return this.getBody().getPosition();
	}

	@Override
	public float getOrientation() {
		return this.getBody().getAngle();
	}

	@Override
	public void setOrientation(float orientation) {

	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return (float) Math.atan2(-vector.x, vector.y);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = -(float) Math.sin(angle);
		outVector.y = (float) Math.cos(angle);
		return outVector;
	}

	@Override
	public Location<Vector2> newLocation() {
		return null;
	}

	public SteeringBehavior<Vector2> getBehavior() {
		return this.behavior;
	}

	public void setBehavior(SteeringBehavior<Vector2> behavior) {
		this.behavior = behavior;
	}

	@Override
	public void draw(Batch batch) {
		if (!this.type.equals(Type.PLAYER)) {
			this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f));
			this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));

			batch.begin();
			this.getSkin().draw(batch);
			batch.end();
		}
	}

	public enum Type {

		ENEMY(60f, 0.01f),
		PLAYER(400f, 2000f);

		private final float speedMultiplier;
		private final float turnMultiplier;

		Type(float speedMultiplier, float turnMultiplier) {
			this.speedMultiplier = speedMultiplier;
			this.turnMultiplier = turnMultiplier;
		}

		public float getSpeedMultiplier() {
			return speedMultiplier;
		}

		public float getTurnMultiplier() {
			return turnMultiplier;
		}
	}
}