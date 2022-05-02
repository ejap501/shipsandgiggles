package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

import java.util.UUID;

/**
 * Ship
 * Creation of the main player class
 *
 * @author Team 23
 * @author Team 22 : Edward Poulter, Ethan Alabaster, Sam Pearson
 * @version 2.0
 */
public class Ship extends MovableEntity {
	// Main data store
	private Body entityBody;
	private final float turnSpeed;
	private final float driftFactor;

	public float turnDirection;
	private float driveDirection;
	private Sprite texture;

	public boolean rapidShot = false;
	public float timeBetweenRapidShots = 0.2f;
	public float rapidShotCoolDown = 0.2f;
	public int numberOfShotsLeft = 3;
	public int shotsInRapidShot = 3;

	public static boolean buyMenuRange = false;

	public float shootingCoolDown = 0.6f;
	public static float burstCoolDown = 4f;
	public static float shootingTimer = 0f;
	public static float burstTimer = 0f;
	public World world;
	public boolean dead = false;
	public  boolean invincible = false;
	public Camera cam;
	public Vector2 deathPosition = new Vector2(0,0);

	public Location startlocation;
	public float height;
	public float width;

	public Rectangle hitBox;

	public static float health;
	public static float maxHealth = 200f;
	public float timeToRegen = 0;
	public float healSpeed = 30;
	public static int coinMulti = 1;
	public static int pointMulti = 1;
	public static int damageMulti = 1;
	public static int speedMulti = 1;
	public int priorCoinMulti = -1;
	public int priorPointMulti = -1;

	public static boolean inFog;

	/**
	 * Initialises the player ship
	 *
	 * @param texture : Image used for the object
	 * @param spawnSpeed : Max speed of ship
	 * @param maxSpeed : Acceleration to max speed of ship
	 * @param driftFactor : Drift of ship
	 * @param turnSpeed : Turn speed of the ship
	 * @param location : Coordinate position of the ship in the world
	 * @param height : Height of the ship
	 * @param width : Width of the ship
	 * @param cam : Camera
	 * @param world : World data
	 */
	public Ship(Sprite texture, float spawnSpeed, float maxSpeed, float driftFactor, float turnSpeed, Location location, float height, float width, Camera cam, World world) {
		super(UUID.randomUUID(), texture, location, EntityType.SHIP, 200, spawnSpeed, maxSpeed, height, width); // TODO: Implement health.
		// Constructor
		this.health = this.maxHealth;
		this.turnDirection = 0;
		this.driveDirection = 0;
		this.driftFactor = driftFactor;
		this.turnSpeed = turnSpeed;
		this.texture = texture;
		this.cam = cam;
		this.startlocation = location;
		this.height = height;
		this.width = width;
		this.world = world;
	}

	/** Creation of Body */
	public void createBody (){
		// Constructs body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(startlocation.getX(), startlocation.getY());
		bodyDef.fixedRotation = false;

		this.entityBody = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((width / 2) / Configuration.PIXEL_PER_METER, (height / 2) / Configuration.PIXEL_PER_METER);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.8f;
		fixtureDef.filter.categoryBits = Configuration.Cat_Player;
		this.entityBody.createFixture(fixtureDef).setUserData(this);
		shape.dispose();

		// Constructs hitbox detector
		this.hitBox = new Rectangle(startlocation.getX(), startlocation.getY(), texture.getWidth(), texture.getHeight());

		// Sets world
		this.world = GameScreen.world;
	}

	/**
	 * Draws player on screen
	 *
	 * @param batch : The batch of sprite data
	 */
	@Override
	public void draw(Batch batch) {
		if(dead) {
			//GameScreen.zoomOut(0.1f);
			return;
		}
		batch.begin();
		this.getSprite().draw(batch);
		batch.end();
		this.hitBox.setPosition(this.getEntityBody().getPosition());
	}

	/**
	 * Shoots the player
	 *
	 * @param player : Assigns the player
	 */
	@Override
	public void shootPlayer(Ship player) {}

	/**
	 * Checks if player is dead
	 * If not dead, then set them as dead and gets their last position
	 * */
	@Override
	public void death() {
		if(this.dead) return;
		this.deathPosition.x = this.getEntityBody().getPosition().x;
		this.deathPosition.y = this.getEntityBody().getPosition().y;
		this.dead = true;
	}

	/**
	 * Shooting function for a singular shot towards the mouse
	 *
	 * @param world : World data
	 * @param cannonBallSprite : Texture of cannonball
	 * @param cam : Camera
	 * @param categoryBits : Object categorisation type
	 * @param maskBit : Masking
	 * @param groupIndex : Position in cannonball array
	 */
	public void shoot(World world, Sprite cannonBallSprite, Camera cam, short categoryBits, short maskBit, short groupIndex){
		Vector3 mouse_position = new Vector3(0,0,0);
		mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		cam.unproject(mouse_position);
		BallsManager.createBall(world, new Vector2(this.getEntityBody().getPosition().x, this.getEntityBody().getPosition().y), new Vector2(mouse_position.x, mouse_position.y), damageMulti, cannonBallSprite, categoryBits, maskBit, groupIndex);
	}

	/**
	 * Shooting function for a burst shot
	 *
	 * @param world : World data
	 * @param cannonBallSprite : Texture of cannonball
	 * @param cam : Camera
	 * @param categoryBits : Object categorisation type
	 * @param maskBit : Masking
	 * @param groupIndex : Position in cannonball array
	 */
	public void burstShoot(World world, Sprite cannonBallSprite, Camera cam, short categoryBits, short maskBit, short groupIndex) {
		float angle = this.getEntityBody().getAngle();
		System.out.println(Math.toDegrees(angle));
		BallsManager.createBallAtAngle(world, new Vector2(this.getEntityBody().getPosition().x, this.getEntityBody().getPosition().y), damageMulti ,angle, cannonBallSprite, categoryBits, maskBit, groupIndex);
		BallsManager.createBallAtAngle(world, new Vector2(this.getEntityBody().getPosition().x, this.getEntityBody().getPosition().y), damageMulti ,(float)Math.toRadians(Math.toDegrees(angle) -180), cannonBallSprite, categoryBits, maskBit, groupIndex);
		this.rapidShot = true;
		this.numberOfShotsLeft = this.shotsInRapidShot;
	}

	/**
	 * Shooting function for a rapid shot
	 *
	 * @param world : World data
	 * @param cannonBallSprite : Texture of cannonball
	 * @param cam : Camera
	 * @param categoryBits : Object categorisation type
	 * @param maskBit : Masking
	 * @param groupIndex : Position in cannonball array
	 */
	public void rapidShot(World world, Sprite cannonBallSprite, Camera cam, short categoryBits, short maskBit, short groupIndex){
		float angle = this.getEntityBody().getAngle();
		if(this.rapidShot && this.timeBetweenRapidShots <= 0){
			BallsManager.createBallAtAngle(world, new Vector2(this.getEntityBody().getPosition().x, this.getEntityBody().getPosition().y), damageMulti ,(float)Math.toRadians(Math.toDegrees(angle) -90), cannonBallSprite, categoryBits, maskBit, groupIndex);
			BallsManager.createBallAtAngle(world, new Vector2(this.getEntityBody().getPosition().x, this.getEntityBody().getPosition().y), damageMulti ,(float)Math.toRadians(Math.toDegrees(angle) + 90), cannonBallSprite, categoryBits, maskBit, groupIndex);
			this.timeBetweenRapidShots = this.rapidShotCoolDown;
			this.numberOfShotsLeft--;
		}

		// Cooldown
		if(this.numberOfShotsLeft <= 0){
			this.rapidShot = false;
		}
		if(this.timeBetweenRapidShots <= 0){
			this.timeBetweenRapidShots = 0;
		}
		if(this.timeBetweenRapidShots >= 0){
			this.timeBetweenRapidShots -= Gdx.graphics.getDeltaTime();
		}
	}

	/**
	 * Checks for updated shots and if the player shoots or not
	 *
	 * @param world : World data
	 * @param cannonBallSprite : Texture of cannonball
	 * @param cam : Camera
	 * @param categoryBits : Object categorisation type
	 * @param maskBit : Masking
	 * @param groupIndex : Position in cannonball array
	 */
	public void updateShots(World world, Sprite cannonBallSprite, Camera cam, short categoryBits, short maskBit, short groupIndex) {
		// Checks if dead
		if (this.dead) return;

		// Health management
		healthCheck(this);

		// Checks for rapid shots
		rapidShot(world, cannonBallSprite, cam, categoryBits, maskBit, groupIndex);

		// If player is shooting then shoot
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && shootingTimer <= 0) {
			this.shoot(world, cannonBallSprite, cam, Configuration.Cat_Player, (short) (Configuration.Cat_Enemy | Configuration.Cat_College), (short) 0);
			this.shootingTimer = shootingCoolDown;
		}

		// If player uses burst then shoot burst
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && burstTimer <= 0) {
			this.burstShoot(world, cannonBallSprite, cam, Configuration.Cat_Player, (short) (Configuration.Cat_Enemy | Configuration.Cat_College), (short) 0);
			this.burstTimer = burstCoolDown;
		}

		// Cool down management
		cooldownManagement();
	}

	/**
	 * Gets the position of the player
	 *
	 * @return Players position in the world
	 */
	public Vector2 getPosition() {
		return this.entityBody.getPosition();
	}

	/**
	 * @return Player texture
	 */
	public Sprite getSprite(){
		return this.texture;
	}

	/**
	 * @return Player body
	 */
	public Body getEntityBody() {
		return this.entityBody;
	}

	/**
	 * @return Forward velocity in 2d plane
	 */
	public Vector2 getForwardVelocity() {
		Vector2 currentNormal = this.getEntityBody().getWorldVector(new Vector2(0, 1));
		float dotProduct = currentNormal.dot(this.getEntityBody().getLinearVelocity());
		return multiply(dotProduct, currentNormal);
	}

	/**
	 * Maths for scalar multiplication of vector
	 *
	 * @param a : Scalar
	 * @param v : vector
	 * @return Product
	 */
	public Vector2 multiply(float a, Vector2 v) {
		return new Vector2(a * v.x, a * v.y);
	}

	public Vector2 getLateralVelocity() {
		Vector2 currentNormal = this.getEntityBody().getWorldVector(new Vector2(1, 0));
		float dotProduct = currentNormal.dot(this.getEntityBody().getLinearVelocity());

		return multiply(dotProduct, currentNormal);
	}

	/**
	 * @param texture : Texture to be applied
	 */
	public void setTexture(Sprite texture){
		this.texture = texture;
	}

	/**
	 * @return Direction player is facing
	 */
	public float getTurnDirection() {
		return this.turnDirection;
	}

	/**
	 * @param turnDirection : Apply a new turn direction
	 */
	public void setTurnDirection(float turnDirection) {
		if(!inFog) {
			this.turnDirection = turnDirection;
		}
		if(inFog) {
			if (turnDirection == 2){
				this.turnDirection = turnDirection - 1;
			}
			if (turnDirection == 1){
				this.turnDirection = turnDirection + 1;
			}
			if(turnDirection == 0){
				this.turnDirection = 0;
			}
		}
	}

	public void inFog() {
		inFog = true;
	}

	public void outFog() {
		inFog = false;
	}

	/**
	 * @return Speed of player turning
	 */
	public float getTurnSpeed() {
		return this.turnSpeed;
	}

	/**
	 * @return Direction of player drive
	 */
	public float getDriveDirection() {
		return this.driveDirection;
	}

	/**
	 * @param driveDirection : Set new drive direction
	 */
	public void setDriveDirection(float driveDirection) {
		this.driveDirection = driveDirection;
	}

	/**
	 * @return Player drift
	 */
	public float getDriftFactor() {
		return this.driftFactor;
	}

	/**
	 * @return Hitbox detector
	 */
	public Rectangle getHitBox() {return this.hitBox;}

	/**
	 * Takes damage from being hit
	 *
	 * @param damage : Damage dealt to the player
	 */
	public void takeDamage(float damage){
		timeToRegen = 5f;
		if (!invincible) {
			health -= damage * 0.8;
			if (health <= 0) {
				this.death();
			}
		}
	}

	/**
	 * Set player invincible state
	 *
	 * @param invincible : New state
	 */
	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}

	/**
	 * Multiplier for coin collection
	 *
	 * @param multiplier : New multiplier value
	 */
	public void setCoinMulti(int multiplier,boolean fromBoost){

		if (multiplier == -1){ //Reset after boost
			coinMulti = priorCoinMulti;
			priorCoinMulti = -1;
		}

		else if (!fromBoost && priorCoinMulti != -1){  //Increase while in a boost to return to
			priorCoinMulti += multiplier;

		}
		else if (fromBoost && priorCoinMulti == -1){ // From a boost, store previous value to return to
			priorCoinMulti = getCoinMulti();
			coinMulti += multiplier;
		}
		else{
			coinMulti += multiplier;
		}
	}

	public int getCoinMulti(){
		return coinMulti;
	}

	/**
	 * Multiplier for point collection
	 *
	 * @param multiplier : New multiplier value
	 */
	public void setPointMulti(int multiplier, boolean fromBoost){

		if (multiplier == -1){ //Reset after boost
			pointMulti = priorPointMulti;
			priorPointMulti = -1;
		}

		else if (!fromBoost && priorPointMulti != -1){  //Increase while in a boost to return to
			priorPointMulti += multiplier;

		}
		else if (fromBoost && priorPointMulti == -1){ // From a boost, store previous value to return to
			priorPointMulti = getPointMulti();
			pointMulti += multiplier;
		}
		else{
			pointMulti += multiplier;
	}}

	public int getPointMulti(){
		return pointMulti;
	}

	/**
	 * Damage multiplier for attack
	 *
	 * @param damageMulti : New multiplier value
	 */
	public void setDamageMulti(int damageMulti) {
		Ship.damageMulti = damageMulti;
	}

	/**
	 * Checks the health of the player to see if it needs regenerating. If so, regenerates it
	 *
	 * @param player : The player
	 */
	public void healthCheck(Ship player){
		if(player.timeToRegen > 0){
			player.timeToRegen -= Gdx.graphics.getDeltaTime();
		}
		else{
			if(player.health > player.maxHealth){
				player.health = player.maxHealth;
			}
			else if(player.timeToRegen <= 0 && player.health < player.maxHealth){
				player.timeToRegen = 0;
				player.health += player.healSpeed * Gdx.graphics.getDeltaTime();
			}
		}
	}

	/**
	 * Checks the cooldown of the player's cannon ball shooting. Updates it if necessary
	 *
	 */
	public void cooldownManagement(){
		if(burstTimer >= 0){
			burstTimer -= Gdx.graphics.getDeltaTime();
		}
		else{
			burstTimer = 0;
		}
		if(shootingTimer >= 0){
			shootingTimer -= Gdx.graphics.getDeltaTime();
		}
		else {
			shootingTimer = 0;
		}
	}

}



