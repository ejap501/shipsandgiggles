package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.Objects;
import java.util.Random;
import java.util.ArrayList;

import net.shipsandgiggles.pirate.*;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.npc.Duck;
import net.shipsandgiggles.pirate.entity.EntityAi;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.screen.ScreenType;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.BallsManager;
import net.shipsandgiggles.pirate.entity.npc.EnemyShip;
import net.shipsandgiggles.pirate.entity.impl.shop.shop1;
import net.shipsandgiggles.pirate.entity.impl.obstacles.Stone;
import net.shipsandgiggles.pirate.entity.impl.collectible.Coin;
import net.shipsandgiggles.pirate.listener.WorldContactListener;
import net.shipsandgiggles.pirate.entity.impl.collectible.powerUp;
import net.shipsandgiggles.pirate.entity.impl.college.AlcuinCollege;
import net.shipsandgiggles.pirate.entity.impl.college.LangwithCollege;
import net.shipsandgiggles.pirate.entity.impl.college.GoodrickeCollege;
import net.shipsandgiggles.pirate.entity.impl.college.ConstantineCollege;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * Game Screen
 * Class to generate the various screens used to play the game.
 * Instantiates all screen types and displays current screen.
 *
 * @author Team 23
 * @author Team 22: Ethan Alabaster, Adam Crook, Joe Dickinson, Sam Pearson, Tom Perry, Edward Poulter
 * @version 2.0
 */
public class GameScreen implements Screen {
	// Main game screen
	public AlcuinCollege alcuin;
	public ConstantineCollege constantine;
	public GoodrickeCollege goodricke;
	public LangwithCollege langwith;
	public shop1 shop;
	public static float collegesKilled = 0;

	public static HUDmanager hud;
	public DeathScreen deathScreen;

	public static ArrayList<ExplosionController> Explosions = new ArrayList<>();

	// Implement world
	public static World world;
	private final Ship playerShips;
	private ArrayList<Coin> coinData = new ArrayList<>();
	private ArrayList<powerUp> powerUpData = new ArrayList<>();
	private ArrayList<Stone> stoneData = new ArrayList<>();
	private ArrayList<EnemyShip> hostileShips = new ArrayList<>();
	private ArrayList<Duck> ducks = new ArrayList<>();

	// Camera work
	private final OrthographicCamera camera;
	private final float Scale = 2;

	// Graphics
	private final SpriteBatch batch; // Batch of images "objects"
	public static Sprite playerModel;
	public Sprite coinModel;
	public Sprite speedUpModel;
	public Sprite invincibilityModel;
	public Sprite incDamageModel;
	public Sprite coinMulModel;
	public Sprite pointMulModel;
	public Sprite stoneModelA;
	public Sprite stoneModelB;
	public Sprite stoneModelC;
	public Sprite enemyModelA;
	public Sprite enemyModelB;
	public Sprite enemyModelC;
	public Sprite duckModel;

	private final Box2DDebugRenderer renderer;
	private final TiledMap map;
	private OrthogonalTiledMapRenderer maprender;
	int cameraState = 0;
	public Sprite water;
	public boolean intro = false;
	public float zoomedAmount = 0;
	EntityAi bob, player;
	public static int collegesCaptured = 0;

	Sprite cannonBall;

	// Abilities
	float currentSpeed = 100000f;
	float maxSpeed = 100000f;
	float speedMul = 40f;
	int damageMul = 2;
	int coinMul = 2;
	int pointMul = 2;
	int speedTimer = 0;
	int damageTimer = 0;
	int invincibilityTimer = 0;
	int coinTimer = 0;
	int pointTimer = 0;

	// Max Spawning
	int maxCoins = 200;
	int maxPowerups = 250;
	int maxShips = 15;
	int maxDucks = 15;
	int maxStones = 50;

	/**
	 * Initialises the Game Screen,
	 * generates the world data and data for entities that exist upon it,
	 */
	public GameScreen() {
		// Initialization of everything
		renderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), true);
		Sprite alcuinCollegeSprite = new Sprite(new Texture("models/alcuin_castle.png"));
		Sprite constantineCollegeSprite = new Sprite(new Texture("models/constantine_castle.png"));
		Sprite goodrickeCollegeSprite = new Sprite(new Texture("models/goodricke_castle.png"));
		Sprite langwithCollegeSprite = new Sprite(new Texture("models/langwith_castle.png"));

		cannonBall = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));
		water = new Sprite(new Texture(Gdx.files.internal("models/water.jpg")));

		camera = new OrthographicCamera();
		int _height = Gdx.graphics.getHeight();
		int _width = Gdx.graphics.getWidth();
		camera.setToOrtho(false, _width / Scale, _height / Scale);
		batch = new SpriteBatch();

		world.setContactListener(new WorldContactListener());
		camera.zoom = 2;

		// Objects setup
		playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
		coinModel = new Sprite(new Texture(Gdx.files.internal("models/gold_coin.png")));
		speedUpModel = new Sprite(new Texture(Gdx.files.internal("models/speed_up.png")));
		incDamageModel = new Sprite(new Texture(Gdx.files.internal("models/damage_increase.png")));
		invincibilityModel = new Sprite(new Texture(Gdx.files.internal("models/invincibility.png")));
		coinMulModel = new Sprite(new Texture(Gdx.files.internal("models/coin_multiplier.png")));
		pointMulModel = new Sprite(new Texture(Gdx.files.internal("models/point_multiplier.png")));
		stoneModelA = new Sprite(new Texture(Gdx.files.internal("models/stone_1.png")));
		stoneModelB = new Sprite(new Texture(Gdx.files.internal("models/stone_2.png")));
		stoneModelC = new Sprite(new Texture(Gdx.files.internal("models/stone_3.png")));
		enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
		enemyModelB = new Sprite(new Texture(Gdx.files.internal("models/ship1.png")));
		enemyModelC = new Sprite(new Texture(Gdx.files.internal("models/dd.png")));
		duckModel = new Sprite(new Texture(Gdx.files.internal("models/duck_v1.png")));
		playerShips = new Ship(playerModel, currentSpeed, 100f, 0.3f, 1f, new Location(2000f, 1800f), playerModel.getHeight(), playerModel.getWidth(), camera,world);
		playerShips.createBody();

		// Map initialization
		map = new TmxMapLoader().load("models/map.tmx");
		maprender = new OrthogonalTiledMapRenderer(map, 1f);
		new WorldCreator(this);

		// Creates damping to player
		playerShips.getEntityBody().setLinearDamping(0.5f);
		playerShips.setMaxSpeed(currentSpeed, speedMul);

		Sprite bobsSprite = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));

		// Enemy creation "bob" and Entity AI controller
		Body body = createEnemy(false, new Vector2(_width / 3f, _height / 6f),world);
		bob = new EntityAi(body, 300f, bobsSprite,(int)bobsSprite.getWidth(),(int)bobsSprite.getHeight() );
		bob.setTarget(playerShips.getEntityBody());

		player = new EntityAi(playerShips.getEntityBody(), 3);
		Steerable<Vector2> pp = player;

		// Status of entity AI
		Arrive<Vector2> arrives = new Arrive<>(bob, pp)
				.setTimeToTarget(0.01f)
				.setArrivalTolerance(175f)
				.setDecelerationRadius(50);
		bob.setBehavior(arrives);

		// Set up spawning
		goodricke = new GoodrickeCollege(goodrickeCollegeSprite, new Location(150f,4000f), 200f, world);
		alcuin = new AlcuinCollege(alcuinCollegeSprite, new Location(1750f,151f), 200f, world);
		constantine = new ConstantineCollege(constantineCollegeSprite, new Location(3950f,4000f), 200f, world);
		langwith = new LangwithCollege(langwithCollegeSprite, new Location(150f,151f), 200f, world);

		shop = new shop1(langwithCollegeSprite, new Location(2000f,2000f),-1,world);
		spawn(maxCoins, maxDucks, maxPowerups, maxShips, maxStones);

		// Set up hud
		hud = new HUDmanager(batch);
		deathScreen = new DeathScreen(batch);
	}

	/**
	 * Renders the visual data for all objects
	 * Changes and renders new visual data for ships
	 *
	 * @param deltaTime : Delta time (elapsed time since last game tick)
	 */
	@Override
	public void render(float deltaTime) {
		// Zoom controller for the intro
		if(!intro){
			camera.zoom -= 0.02f;
			zoomedAmount += 0.02;
			if(zoomedAmount >= 1){
				intro = true;
			}
		}

		// Does the update method
		update();

		// Colour creation for background
		Gdx.gl.glClearColor(.98f, .91f, .761f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		maprender.render();
		BallsManager.updateBalls(batch);

		// Setting ship position for the sprite of the player ship
		playerShips.getSprite().setPosition(playerShips.getEntityBody().getPosition().x * PIXEL_PER_METER - (playerShips.getSkin().getWidth() / 2f), playerShips.getEntityBody().getPosition().y * PIXEL_PER_METER - (playerShips.getSkin().getHeight() / 2f));
		playerShips.getSprite().setRotation((float) Math.toDegrees(playerShips.getEntityBody().getAngle()));

		//player
		//batch.draw(playerShips.getSkin(), playerShips.getEntityBody().getPosition().x * PIXEL_PER_METER - (playerShips.getSkin().getWidth() / 2f), playerShips.getEntityBody().getPosition().y * PIXEL_PER_METER - (playerShips.getSkin().getHeight() / 2f));
		//batch.draw(islandsTextures[0], islands[0].getPosition().x * PixelPerMeter - (islandsTextures[0].getWidth()/2), islands[0].getPosition().y * PixelPerMeter - (islandsTextures[0].getHeight()/2));
		//enemyShips.draw(batch);

		// Update all the colleges and entities
		playerShips.draw(batch);
		langwith.draw(batch);
		langwith.shootPlayer(playerShips);
		constantine.draw(batch);
		constantine.shootPlayer(playerShips);
		goodricke.draw(batch);
		goodricke.shootPlayer(playerShips);
		alcuin.draw(batch);
		alcuin.shootPlayer(playerShips);
		shop.draw(batch);
		shop.rangeCheck(playerShips);
		for (Coin coinDatum : coinData) {
			coinDatum.draw(batch);
			if (coinDatum.rangeCheck(playerShips) && !coinDatum.dead) {
				coinDatum.death();
			}
		}

		for (EnemyShip hostileShip : hostileShips) {
			hostileShip.draw(batch);
			hostileShip.shootPlayer(playerShips);
		}

		for (Duck duck : ducks) {
			duck.draw(batch);
		}

		for (powerUp powerUpDatum : powerUpData) {
			powerUpDatum.draw(batch);
			if (Objects.equals(powerUpDatum.getPowerUpType(), "Speed Up")) {
				if (powerUpDatum.rangeCheck(playerShips) && powerUpDatum.dead) {
					speedTimer = 600;
					currentSpeed = maxSpeed * speedMul;
				} else {
					currentSpeed = maxSpeed;
				}
			} else if (Objects.equals(powerUpDatum.getPowerUpType(), "Invincible")) {
				if (powerUpDatum.rangeCheck(playerShips) && powerUpDatum.dead) {
					invincibilityTimer = 300;
				}
			} else if (Objects.equals(powerUpDatum.getPowerUpType(), "Damage Increase")) {
				if (powerUpDatum.rangeCheck(playerShips) && powerUpDatum.dead) {
					damageTimer = 600;
				}
			} else if (Objects.equals(powerUpDatum.getPowerUpType(), "Coin Multiplier")) {
				if (powerUpDatum.rangeCheck(playerShips) && powerUpDatum.dead) {
					coinTimer = 900;
				}
			} else if (Objects.equals(powerUpDatum.getPowerUpType(), "Point Multiplier")) {
				if (powerUpDatum.rangeCheck(playerShips) && powerUpDatum.dead) {
					pointTimer = 900;
				}
			}
			if (powerUpDatum.rangeCheck(playerShips) && !powerUpDatum.dead) {
				powerUpDatum.death();
			}
		}

		for (Stone stoneDatum : stoneData) {
			stoneDatum.draw(batch);
		}

		if(!bob.dead) {
			bob.update(deltaTime, batch, playerShips, world);
		}

		// Update for the explosion
		updateExplosions();

		// Change of UI in case of victory or death or normal hud
		if(playerShips.dead){
			deathScreen.update(hud, 0);
			batch.setProjectionMatrix(deathScreen.stage.getCamera().combined);
			deathScreen.stage.draw();
			return;
		}
		if(collegesCaptured == 4){
			deathScreen.update(hud, 1);
			batch.setProjectionMatrix(deathScreen.stage.getCamera().combined);
			deathScreen.stage.draw();
			return;
		}
		if(collegesKilled == 4){
			deathScreen.update(hud, 2);
			batch.setProjectionMatrix(deathScreen.stage.getCamera().combined);
			deathScreen.stage.draw();
			return;
		}
		batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		hud.updateLabels(batch);
	}

	/**
	 * Updating the explosions
	 * Adds explosions
	 * Removes explosions
	 * */
	private void updateExplosions() {
		ArrayList<ExplosionController> removeExplosion = new ArrayList<ExplosionController>();
		for(ExplosionController explosion : Explosions){
			explosion.update();
			explosion.draw(batch);
			if(explosion.remove)removeExplosion.add(explosion);
		}
		Explosions.removeAll(removeExplosion);
	}

	/**
	 * Updates the state of each object
	 */
	public void update() {
		world.step(1 / 60f, 6, 2);

		// Updates power-up timers
		if (speedTimer > 0) {
			speedTimer -= 1f;
		}
		if (invincibilityTimer != 0) {
			invincibilityTimer -= 1f;
			playerShips.setInvincible(true);
		}else{
			playerShips.setInvincible(false);
		}
		if (damageTimer != 0){
			damageTimer -= 1f;
			playerShips.setDamageMulti(damageMul);
		}else{
			playerShips.setDamageMulti(1);
		}
		if (coinTimer != 0){
			coinTimer -= 1f;
			playerShips.setCoinMulti(coinMul);
		}else{
			playerShips.setCoinMulti(1);
		}
		if (pointTimer != 0){
			pointTimer -= 1f;
			playerShips.setPointMulti(pointMul);
		}else{
			playerShips.setPointMulti(1);
		}

		updateCamera();
		inputUpdate();
		processInput(playerShips);
		handleDrift();
		maprender.setView(camera);
		batch.setProjectionMatrix(camera.combined);
		playerShips.updateShots(world, cannonBall, camera, Configuration.Cat_Player, (short)(Configuration.Cat_Enemy | Configuration.Cat_College), (short) 0);
	}

	/**
	 * Checks for input and performs an action
	 * Applies to key "W" "A" "S" "D" "UP" "DOWN" "LEFT" "RIGHT" "E" "P" "C" "X" "NUM_1" "NUM_2"
	 */
	public void inputUpdate() {
		/*
		 * Checking for user inputs
		 * Sets player direction and use of abilities and menus
		 * */
		if (playerShips.dead) return;
		if (playerShips.getEntityBody().getLinearVelocity().len() > 20f) {
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) | Gdx.input.isKeyPressed(Input.Keys.A)) {
				playerShips.setTurnDirection(2);
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) | Gdx.input.isKeyPressed(Input.Keys.D)) {
				playerShips.setTurnDirection(1);
			} else {
				playerShips.setTurnDirection(0);
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.UP) | Gdx.input.isKeyPressed(Input.Keys.W)) {
			playerShips.setDriveDirection(1);
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) | Gdx.input.isKeyPressed(Input.Keys.S)) {
			playerShips.setDriveDirection(2);
		} else {
			playerShips.setDriveDirection(0);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.E )) {
			if (Ship.buyMenuRange){
				PirateGame.get().changeScreen(ScreenType.SHOP);
			}
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			System.out.println(playerShips.getEntityBody().getPosition());
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			if (cameraState == 0) cameraState = 1;
			else if (cameraState == 1) cameraState = 0;
			else if (cameraState == -1) cameraState = 1;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
			if (cameraState == 0) cameraState = -1;
			else if (cameraState == 1) cameraState = -1;
			else if (cameraState == -1) cameraState = 0;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
			if(cameraState == 5){
				cameraState = 0;
			}
			else{
				cameraState = 5;
			}

		}
		// creating zooming
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
			if(camera.zoom < 2)camera.zoom += 0.02f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
			if(camera.zoom > 1)camera.zoom -= 0.02f;
		}

	}

	/**
	 * Processes input based on an action
	 *
	 * @param playerShips : The player body
	 */
	private void processInput(Ship playerShips) {
		// Processing the input created
		float speedMulSet = 1;
		if (speedTimer > 0){
			speedMulSet = speedMul;
		}
		if (playerShips.dead) return;
		Vector2 baseVector = new Vector2(0, 0);

		// Applying liner velocity to the player based on input
		float turnPercentage = 1;
		float speed = currentSpeed * speedMulSet;

		if (playerShips.getEntityBody().getLinearVelocity().len() < (50)) {
			turnPercentage = playerShips.getEntityBody().getLinearVelocity().len() / (100);
		}

		float currentTurnSpeed = playerShips.getTurnSpeed() * turnPercentage;

		// Applying angular velocity to the player based on input
		if (playerShips.getTurnDirection() == 1) {
			playerShips.getEntityBody().setAngularVelocity(-currentTurnSpeed);
		} else if (playerShips.getTurnDirection() == 2) {
			playerShips.getEntityBody().setAngularVelocity(currentTurnSpeed);
		} else if (playerShips.getTurnDirection() == 0 && playerShips.getEntityBody().getAngularVelocity() != 0) {
			playerShips.getEntityBody().setAngularVelocity(0);
		}

		// Applies speed to the player based on input
		if (playerShips.getDriveDirection() == 1) {
			baseVector.set(0, speed);
		} else if (playerShips.getDriveDirection() == 2) {
			baseVector.set(0, -speed * 4 / 5);
		}
		if (playerShips.getEntityBody().getLinearVelocity().len() > 0 && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			playerShips.getEntityBody().setLinearDamping(1.75f);
		} else {
			playerShips.getEntityBody().setLinearDamping(0.5f);
		}

		playerShips.setSpeed(currentSpeed, speedMulSet);
		if (!baseVector.isZero() && (playerShips.getEntityBody().getLinearVelocity().len() < speed)) {
			playerShips.getEntityBody().applyForceToCenter(playerShips.getEntityBody().getWorldVector(baseVector), true);
		}
	}

	/**
	 * Handles drift to improve handling
	 */
	private void handleDrift() {
		// Handle drifts of the boat
		Vector2 forwardSpeed = playerShips.getForwardVelocity();
		Vector2 lateralSpeed = playerShips.getLateralVelocity();

		playerShips.getEntityBody().setLinearVelocity(forwardSpeed.x + lateralSpeed.x * playerShips.getDriftFactor(), forwardSpeed.y + lateralSpeed.y * playerShips.getDriftFactor());
	}

	/**
	 * Resizes the camera
	 */
	@Override
	public void resize(int width, int height) {
		// Resize function
		camera.setToOrtho(false, width / 2, height / 2);
	}

	/**
	 * Disposes of data
	 */
	@Override
	public void dispose() {
		// Disposing of everything
		renderer.dispose();
		world.dispose();
		maprender.dispose();
		batch.dispose();
		map.dispose();
	}

	/**
	 * Updates camera position
	 */
	public void updateCamera() {
		// Updating camera based on states
		if(playerShips.dead) {
			if(camera.zoom < 2)camera.zoom += 0.005f;
			CameraManager.lerpOn(camera, playerShips.deathPosition, 0.1f);
			return;
		}
		if (cameraState == 0) {
			CameraManager.lerpOn(camera, playerShips.getEntityBody().getPosition(), 0.1f);
		}
		if (cameraState == -1) {
			CameraManager.lockOn(camera, playerShips.getEntityBody().getPosition());
		}
		if (cameraState == 5) {
			CameraManager.lerpOn(camera, bob.getBody().getPosition(), 0.1f);
		}
	}

	/**
	 * Constructs an enemy body
	 */
	public static Body createEnemy(boolean isStatic, Vector2 position, World world) {
		// Creation of the body for the enemy
		Body body;
		BodyDef def = new BodyDef();

		if (isStatic) def.type = BodyDef.BodyType.StaticBody;
		else def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(position);

		def.fixedRotation = true;
		body = world.createBody(def);

		return body;
	}

	/**
	 * Returns the map
	 */
	public TiledMap getMap() {
		return map;
	}

	/**
	 * Returns the world
	 */
	public World getWorld(){
		return world;
	}

	/**
	 * Adds a new explosion to the world
	 *
	 * @param pp : Position of explosion
	 */
	public static void add(Vector2 pp){
		Explosions.add(new ExplosionController(pp));
	}

	/**
	 * Increments killed college count
	 * Decrements remaining college count
	 */
	public static void collegeKilled(){
		// Adds for each college killed to count for victory
		collegesKilled ++;
		collegesCaptured--;
	}

	/** Adds college captured to check for victory */
	public static void collegeCaptured(){collegesCaptured ++;}

	/**
	 * Determines spawning
	 * Manages spawning of coins, ships, powerups, stones, and ducks
	 *
	 * @param coins : Number of coins to be added
	 * @param duck : Number of ducks to be added
	 * @param powerups : Number of power-ups to be added
	 * @param ships : Number of ships to be added
	 * @param stone : Number of stones to be added
	 */
	public void spawn(int coins, int duck, int powerups , int ships, int stone){
		// Initializing
		Random rn = new Random();
		int randX, randY, randModel, randHealth;
		String randType;
		Sprite model;

		// Coins
		for (int i = 0; i < coins; i++){
			randX = 50 + rn.nextInt(3950);
			randY = 50 + rn.nextInt(3950);
			coinData.add(new Coin(coinModel, new Location(randX,randY), world));
		}

		// Ducks
		for (int i = 0; i < duck; i++){
			randX = 50 + rn.nextInt(3950);
			randY = 50 + rn.nextInt(3950);
			ducks.add(new Duck(duckModel, new Location(randX,randY), 5, world));
		}

		// Power-ups
		for (int i = 0; i < powerups; i++){
			randX = 50 + rn.nextInt(3950);
			randY = 50 + rn.nextInt(3950);
			randModel = rn.nextInt(5);
			if (randModel == 0){
				model = speedUpModel;
				randType = "Speed Up";
			}else if (randModel == 1){
				model = incDamageModel;
				randType = "Damage Increase";
			}else if (randModel == 2){
				model = invincibilityModel;
				randType = "Invincible";
			}else if (randModel == 3){
				model = coinMulModel;
				randType = "Coin Multiplier";
			}else {
				model = pointMulModel;
				randType = "Point Multiplier";
			}
			powerUpData.add(new powerUp(model, new Location(randX,randY), randType, world));
		}

		// Ships
		for (int i = 0; i < ships; i++){
			randX = 50 + rn.nextInt(3950);
			randY = 50 + rn.nextInt(3950);
			randModel = rn.nextInt(3);
			if (randModel == 0){
				model = enemyModelA;
				randHealth = 80 + rn.nextInt(40);
			}else if (randModel == 1){
				model = enemyModelB;
				randHealth = 140 + rn.nextInt(20);
			}else {
				model = enemyModelC;
				randHealth = 200 + rn.nextInt(50);
			}
			hostileShips.add(new EnemyShip(model, new Location(randX,randY), randHealth, world, playerShips));
		}

		// Stones
		for (int i = 0; i < stone; i++){
			randX = 50 + rn.nextInt(3950);
			randY = 50 + rn.nextInt(3950);
			randModel = rn.nextInt(3);
			if (randModel == 0){
				model = stoneModelA;
			}else if (randModel == 1){
				model = stoneModelB;
			}else {
				model = stoneModelC;
			}
			stoneData.add(new Stone(model, new Location(randX,randY), world));
		}
	}

	@Override
	public void show() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}
}