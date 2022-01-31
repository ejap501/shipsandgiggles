package net.shipsandgiggles.pirate.entity.type;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.cache.impl.BallHandler;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.EntityType;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

import java.util.UUID;

public class Ship extends ControlledEntity {

	private final float turnSpeed;
	private final float driftFactor;
	public boolean rapidShot = false;
	public float timeBetweenRapidShots = 0.2f;
	public float rapidShotCoolDown = 0.2f;
	public int numberOfShotsLeft = 3;
	public int shotsInRapidShot = 3;
	public float shootingCoolDown = 0.3f;
	public float burstCoolDown = 4f;
	public float shootingTimer = 0f;
	public float burstTimer = 0f;
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
		bodyDef.fixedRotation = type == Type.ENEMY;

		Body entityBody = GameScreen.world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((this.getWidth() / 2) / Configuration.PIXEL_PER_METER, (this.getHeight() / 2) / Configuration.PIXEL_PER_METER);

		//entityBody.createFixture(shape, 1f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.filter.categoryBits = (type == Type.PLAYER) ? Configuration.CAT_PLAYER : Configuration.CAT_ENEMY;
		entityBody.createFixture(fixtureDef).setUserData(this);

		if (type.equals(Type.ENEMY)) {
			MassData MassData = new MassData();
			MassData.mass = 6000f;
			MassData.center.set(this.getPosition().x / 2, this.getPosition().y / 2);
			entityBody.setMassData(MassData);
		}

		this.setBody(entityBody);
		shape.dispose();
		this.init(type);
	}

	@Override
	public void death() {

	}

	public void shoot(World world, Sprite cannonBallSprite, Camera cam, short categoryBits, short maskBit, short groupIndex) {
		Vector3 mouse_position = new Vector3(0, 0, 0);
		mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		cam.unproject(mouse_position);
		BallHandler.get().createBall(world, new Vector2(this.getBody().getPosition().x, this.getBody().getPosition().y), new Vector2(mouse_position.x, mouse_position.y), cannonBallSprite, categoryBits, maskBit, groupIndex);
	}

	public void burstShoot(World world, Sprite cannonBallSprite, Camera cam, short categoryBits, short maskBit, short groupIndex) {
		float angle = this.getBody().getAngle();
		System.out.println(Math.toDegrees(angle));
		BallHandler.get().createBallAtAngle(world, new Vector2(this.getBody().getPosition().x, this.getBody().getPosition().y), angle, cannonBallSprite, categoryBits, maskBit, groupIndex);
		BallHandler.get().createBallAtAngle(world, new Vector2(this.getBody().getPosition().x, this.getBody().getPosition().y), (float) Math.toRadians(Math.toDegrees(angle) - 180), cannonBallSprite, categoryBits, maskBit, groupIndex);
		this.rapidShot = true;
		this.numberOfShotsLeft = this.shotsInRapidShot;
	}

	public void rapidShot(World world, Sprite cannonBallSprite, Camera cam, short categoryBits, short maskBit, short groupIndex) {
		float angle = this.getBody().getAngle();
		if (this.rapidShot && this.timeBetweenRapidShots <= 0) {
			BallHandler.get().createBallAtAngle(world, new Vector2(this.getBody().getPosition().x, this.getBody().getPosition().y), (float) Math.toRadians(Math.toDegrees(angle) - 90), cannonBallSprite, categoryBits, maskBit, groupIndex);
			BallHandler.get().createBallAtAngle(world, new Vector2(this.getBody().getPosition().x, this.getBody().getPosition().y), (float) Math.toRadians(Math.toDegrees(angle) + 90), cannonBallSprite, categoryBits, maskBit, groupIndex);
			this.timeBetweenRapidShots = this.rapidShotCoolDown;
			this.numberOfShotsLeft--;
		}
		if (this.numberOfShotsLeft <= 0) {
			this.rapidShot = false;
		}
		if (this.timeBetweenRapidShots <= 0) {
			this.timeBetweenRapidShots = 0;
		}
		if (this.timeBetweenRapidShots >= 0) {
			this.timeBetweenRapidShots -= Gdx.graphics.getDeltaTime();
		}

	}

	public void updateShots(World world, Sprite cannonBallSprite, Camera cam, short categoryBits, short maskBit, short groupIndex) {
		rapidShot(world, cannonBallSprite, cam, categoryBits, maskBit, groupIndex);

		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && shootingTimer <= 0) {
			this.shoot(world, cannonBallSprite, cam, Configuration.CAT_PLAYER, Configuration.CAT_ENEMY, (short) 0);
			this.shootingTimer = shootingCoolDown;
		}

		if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && burstTimer <= 0) {
			this.burstShoot(world, cannonBallSprite, cam, Configuration.CAT_PLAYER, Configuration.CAT_ENEMY, (short) 0);
			this.burstTimer = burstCoolDown;
		}

		if (burstTimer >= 0) {
			burstTimer -= Gdx.graphics.getDeltaTime();
		} else {
			burstTimer = 0;
		}
		if (shootingTimer >= 0) {
			shootingTimer -= Gdx.graphics.getDeltaTime();
		} else {
			shootingTimer = 0;
		}

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
