package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
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

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.shipsandgiggles.pirate.*;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.*;
import net.shipsandgiggles.pirate.entity.npc.Duck;
import net.shipsandgiggles.pirate.pref.GamePreferences;
import net.shipsandgiggles.pirate.screen.ScreenType;
import net.shipsandgiggles.pirate.conf.Configuration;
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
 * Edited from original
 *
 * @author Team 23
 * @author Team 22: Ethan Alabaster, Adam Crook, Joe Dickinson, Sam Pearson, Tom Perry, Edward Poulter
 * @version 2.0
 */
public class GameScreen implements Screen {
	// Main game screen
	public static AlcuinCollege alcuin;
	public static ConstantineCollege constantine;
	public static GoodrickeCollege goodricke;
	public static LangwithCollege langwith;
	public static shop1 shop;
	public static float collegesKilled = 0;

	public static HUDmanager hud;
	private final ScreenViewport viewport;
	public DeathScreen deathScreen;

	public static ArrayList<ExplosionController> Explosions = new ArrayList<>();

	// Implement world
	public static World world;
	public static Ship playerShips;
	private static ArrayList<Coin> coinData = new ArrayList<>();
	private static ArrayList<powerUp> powerUpData = new ArrayList<>();
	private static ArrayList<Stone> stoneData = new ArrayList<>();
	private static ArrayList<EnemyShip> hostileShips = new ArrayList<>();
	public static ArrayList<Duck> ducks = new ArrayList<>();

	// Camera work
	private static OrthographicCamera camera;
	private final float Scale = 2;

	// Graphics
	private final SpriteBatch batch; // Batch of images "objects"
	public static Sprite playerModel;
	public static Sprite copperCoinModel;
	public static Sprite silverCoinModel;
	public static Sprite goldCoinModel;
	public static Sprite speedUpModel;
	public static Sprite invincibilityModel;
	public static Sprite incDamageModel;
	public static Sprite coinMulModel;
	public static Sprite pointMulModel;
	public static Sprite stoneModelA;
	public static Sprite stoneModelB;
	public static Sprite stoneModelC;
	public static Sprite enemyModelA;
	public static Sprite enemyModelB;
	public static Sprite enemyModelC;
	public static Sprite duckModel;
	public static Sprite bigDuckModel;
	public static Sprite alcuinCollegeSprite;
	public static Sprite constantineCollegeSprite;
	public static Sprite goodrickeCollegeSprite;
	public static Sprite langwithCollegeSprite;
	public static Sprite bobsSprite;
	public static Sprite shopSprite;

	private final Box2DDebugRenderer renderer;
	private final TiledMap map;
	private OrthogonalTiledMapRenderer maprender;
	int cameraState = 0;
	public static Sprite water;
	public boolean intro = false;
	public float zoomedAmount = 0;
	static EntityAi bob;
	static EntityAi player;
	public static int collegesCaptured = 0;
	private Weather weather;

	public static Sprite cannonBall;

	// Abilities
	static float currentSpeed = 100000f;
	float maxSpeed = 100000f;
	static float speedMul = 40f;
	int damageMul = 2;
	public static float speedTimer = -1f;
	public static float damageTimer = -1f;
	public static float invincibilityTimer = -1f;
	public static float coinTimer = -1f;
	public static float pointTimer = -1f;

	// Max Spawning
	static int maxCoins;
	static int maxPowerups;
	static int maxShips;
	static int maxDucks;
	static int maxStones;
	static int maxDuckKills;
	public int currentDuckKills = 0;
	private GamePreferences gamePreferences = GamePreferences.get();

	private Rain rain;

	/**
	 * Initialises the Game Screen,
	 * generates the world data and data for entities that exist upon it,
	 */
	public GameScreen() {
		// Initialization of everything
		createSprites();

		renderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), true);
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		int _height = Gdx.graphics.getHeight();
		int _width = Gdx.graphics.getWidth();
		camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
		//camera.setToOrtho(false, _width / Scale, _height / Scale);
		batch = new SpriteBatch();

		weather = new Weather(this, 2000, 1600, 8);
		rain = new Rain();
		world.setContactListener(new WorldContactListener());
		camera.zoom = 2;


		Body body = createEnemy(false, new Vector2(_width / 3f, _height / 6f),world);
		createEntities(body,world,camera);

		// Map initialization
		map = new TmxMapLoader().load("models/map.tmx");
		maprender = new OrthogonalTiledMapRenderer(map, 1f);
		new WorldCreator(this);

		// Set up hud
		hud = new HUDmanager(batch);
		deathScreen = new DeathScreen(batch);
	}

	public static boolean createSprites(){
		world = new World(new Vector2(0, 0), true);
		alcuinCollegeSprite = new Sprite(new Texture("models/alcuin_castle.png"));
		constantineCollegeSprite = new Sprite(new Texture("models/constantine_castle.png"));
		goodrickeCollegeSprite = new Sprite(new Texture("models/goodricke_castle.png"));
		langwithCollegeSprite = new Sprite(new Texture("models/langwith_castle.png"));
		cannonBall = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));
		water = new Sprite(new Texture(Gdx.files.internal("models/water.jpg")));
		playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
		copperCoinModel = new Sprite(new Texture(Gdx.files.internal("models/bronze_1.png")));
		silverCoinModel = new Sprite(new Texture(Gdx.files.internal("models/silver_1.png")));
		goldCoinModel = new Sprite(new Texture(Gdx.files.internal("models/gold_1.png")));
		speedUpModel = new Sprite(new Texture(Gdx.files.internal("models/speed_up.png")));
		incDamageModel = new Sprite(new Texture(Gdx.files.internal("models/damage_increase.png")));
		invincibilityModel = new Sprite(new Texture(Gdx.files.internal("models/invincshield2.png")));
		coinMulModel = new Sprite(new Texture(Gdx.files.internal("models/coin_multiplier.png")));
		pointMulModel = new Sprite(new Texture(Gdx.files.internal("models/point_multiplier.png")));
		stoneModelA = new Sprite(new Texture(Gdx.files.internal("models/stone_1.png")));
		stoneModelB = new Sprite(new Texture(Gdx.files.internal("models/stone_2.png")));
		stoneModelC = new Sprite(new Texture(Gdx.files.internal("models/stone_3.png")));
		enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
		enemyModelB = new Sprite(new Texture(Gdx.files.internal("models/ship1.png")));
		enemyModelC = new Sprite(new Texture(Gdx.files.internal("models/dd.png")));
		duckModel = new Sprite(new Texture(Gdx.files.internal("models/duck_v1.png")));
		bigDuckModel = new Sprite(new Texture(Gdx.files.internal("models/long_boi_v1.png")));
		bobsSprite = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
		shopSprite = new Sprite(new Texture(Gdx.files.internal("models/castle.png")));
		return true; //Successful
	}

	public static void createEntities(Body bobBody,World world,OrthographicCamera camera){

		playerShips = new Ship(playerModel, currentSpeed, 100f, 0.3f, 1f, new Location(2000f, 1800f), playerModel.getHeight(), playerModel.getWidth(), camera,world);
		playerShips.createBody();

		// Creates damping to player
		playerShips.getEntityBody().setLinearDamping(0.5f);
		playerShips.setMaxSpeed(currentSpeed, speedMul);

		// Enemy creation "bob" and Entity AI controller
		bob = new EnemyShip(bobBody, bobsSprite, 300f, new Location(2000f, 1600f), 100, world);
		bob.setTarget(playerShips.getEntityBody());

		player = new EntityAi(playerShips.getEntityBody(), 30f);
		Steerable<Vector2> pp = player;

		// Status of entity AI
		Arrive<Vector2> arrives = new Arrive<>(bob, pp)
				.setTimeToTarget(0.01f)
				.setArrivalTolerance(175f)
				.setDecelerationRadius(50);
		bob.setBehavior(arrives);

		// Set up spawning
		goodricke = new GoodrickeCollege(goodrickeCollegeSprite, new Location(150f,4000f), 200f, world);
		alcuin = new AlcuinCollege(alcuinCollegeSprite, new Location(150f,151f), 200f, world);
		constantine = new ConstantineCollege(constantineCollegeSprite, new Location(3950f,4000f), 200f, world);
		langwith = new LangwithCollege(langwithCollegeSprite, new Location(3950f,151f), 200f, world);

		shop = new shop1(shopSprite, new Location(2050f,2050f),-1,world);
		spawn(world,  pp);
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
		renderer.render(world, camera.combined);
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

		for (powerUp powerUpDatum : powerUpData) {

			powerUpDatum.draw(batch);
			if (Objects.equals(powerUpDatum.getPowerUpType(), "Speed Up")) {
				if (powerUpDatum.rangeCheck(playerShips) && !powerUpDatum.dead) {
					speedTimer = 10;
				}
			} else if (Objects.equals(powerUpDatum.getPowerUpType(), "Invincible")) {
				if (powerUpDatum.rangeCheck(playerShips) && !powerUpDatum.dead) {
					invincibilityTimer = 10;
				}
			} else if (Objects.equals(powerUpDatum.getPowerUpType(), "Damage Increase")) {
				if (powerUpDatum.rangeCheck(playerShips) && !powerUpDatum.dead) {
					damageTimer = 10;
				}
			} else if (Objects.equals(powerUpDatum.getPowerUpType(), "Coin Multiplier")) {
				if (powerUpDatum.rangeCheck(playerShips) && !powerUpDatum.dead) {
					coinTimer = 10;
				}
			} else if (Objects.equals(powerUpDatum.getPowerUpType(), "Point Multiplier")) {
				if (powerUpDatum.rangeCheck(playerShips) && !powerUpDatum.dead) {
					pointTimer = 10;
				}
			}
			if (powerUpDatum.rangeCheck(playerShips) && !powerUpDatum.dead) {
				powerUpDatum.death();

			}
		}

		for (Stone stoneDatum : stoneData) {
			if (!stoneDatum.dead) {
				stoneDatum.draw(batch);
			}
		}

		if(!bob.dead) {
			bob.update(deltaTime, batch, playerShips, world);
		}
		for (EnemyShip hostileShip : hostileShips) {
			if (!hostileShip.dead) {
				hostileShip.update(deltaTime, batch, playerShips, world);
			}
		}

		for (Duck duck : ducks) {
			if (!duck.dead) {
				duck.update(deltaTime, batch, playerShips, world);
			}
		}




		// Update for the explosion
		updateExplosions();

		// Change of UI in case of victory or death or normal hud
		if(playerShips.dead){
			deathScreen.update(hud, 0);
			batch.setProjectionMatrix(deathScreen.stage.getCamera().combined);
			deathScreen.stage.draw();
			gamePreferences.setHasSave(false);
			return;
		}
		if(collegesCaptured == 4){
			deathScreen.update(hud, 1);
			batch.setProjectionMatrix(deathScreen.stage.getCamera().combined);
			deathScreen.stage.draw();
			gamePreferences.setHasSave(false);
			return;
		}
		if(collegesKilled == 4){
			deathScreen.update(hud, 2);
			batch.setProjectionMatrix(deathScreen.stage.getCamera().combined);
			deathScreen.stage.draw();
			gamePreferences.setHasSave(false);
			return;
		}

		weather.draw(batch);
		batch.setProjectionMatrix(hud.stage.getCamera().combined);
		rain.draw(batch, deltaTime);
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
		if (speedTimer >= 0) {
			speedTimer -= Gdx.graphics.getDeltaTime();
			currentSpeed = maxSpeed * speedMul;
		} else {
			currentSpeed = maxSpeed;
			speedTimer = -1f;
		}

		if (invincibilityTimer >= 0) {
			invincibilityTimer -= Gdx.graphics.getDeltaTime();
			playerShips.setInvincible(true);
		}else{
			playerShips.setInvincible(false);
			invincibilityTimer = -1f;
		}
		if (damageTimer >= 0){
			damageTimer -= Gdx.graphics.getDeltaTime();
			playerShips.setDamageMulti(damageMul);

		}else{
			playerShips.setDamageMulti(1);
		}

		if (coinTimer == 10){
			coinTimer -= Gdx.graphics.getDeltaTime();
			playerShips.setCoinMulti(1, true);
		}else if (coinTimer <= 0f && coinTimer != -1f){
			coinTimer = -1f;
			playerShips.setCoinMulti(-1, false);
		}
		else if (coinTimer >= 0f){
			coinTimer -= Gdx.graphics.getDeltaTime();
		}



		if (pointTimer == 10){
			pointTimer -= Gdx.graphics.getDeltaTime();
			playerShips.setPointMulti(playerShips.getPointMulti() * 3, true);
		}else if (pointTimer <= 0 && pointTimer !=-1){
			pointTimer = -1f;
			playerShips.setPointMulti(-1, false);
		}
		else if (pointTimer >= 0f){
			pointTimer -= Gdx.graphics.getDeltaTime();
		}

		for (Duck duck : ducks){
			if (duck.deadDuck == 1){
				currentDuckKills += duck.deadDuck;
				if (currentDuckKills >= maxDuckKills){
					duck.deadDuck = 3;
					duck.shooting = true;
					duck.maxHealth = 50000;
					duck.health = 50000;
					duck.texture = bigDuckModel;
					duck.hitBox =  new Rectangle(duck.body.getPosition().x - 300, duck.body.getPosition().y - 300, duck.texture.getWidth() + 600, duck.texture.getHeight() + 600);

					currentDuckKills = 0;
				}else{
					duck.deadDuck = 2;
					duck.death(world);
				}
			}
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
	 * Applies to key "W" "A" "S" "D" "UP" "DOWN" "LEFT" "RIGHT" "E" "P" "ESCAPE"  "NUM_1" "NUM_2"
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


		// creating zooming
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
			if(camera.zoom < 2)camera.zoom += 0.02f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
			if(camera.zoom > 1)camera.zoom -= 0.02f;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			closeAndSave();
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
			camera.position.x = player.body.getPosition().x;
			camera.position.y = player.body.getPosition().y;
			camera.update();
			maprender.setView(camera);
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
	 */
	public static void spawn(World world, Steerable<Vector2> pp){
		int longBoi = 0;
		if (DifficultyScreen.difficulty == 1){
			maxCoins = 250;
			maxPowerups = 100;
			maxShips = 10;
			maxDucks = 20;
			maxDuckKills = 15;
			maxStones = 30;
			longBoi = 0;
		}
		else if(DifficultyScreen.difficulty == 2){
			maxCoins = 200;
			maxPowerups = 75;
			maxShips = 10;
			maxDucks = 30;
			maxDuckKills = 10;
			maxStones = 40;
			longBoi = 0;
		}
		else if(DifficultyScreen.difficulty == 3){
			maxCoins = 150;
			maxPowerups = 50;
			maxShips = 15;
			maxDucks = 35;
			maxDuckKills = 5;
			maxStones = 50;
			longBoi = 0;
		}else{
			maxCoins = 150;
			maxPowerups = 25;
			maxShips = 0;
			maxDucks = 0;
			maxStones = 50;
			longBoi = 35;
		}
		// Initializing
		Random rn = new Random();
		int randX, randY, randModel, randHealth;
		String randType;
		Sprite model;


		// Coins
		for (int i = 0; i < maxCoins; i++){
			Boolean loop = true;
			Coin add = null;
			while (loop){
				Boolean nextloop = false;
				randX = 50 + rn.nextInt(3950);
				randY = 50 + rn.nextInt(3950);
				randModel = rn.nextInt(3);
				if (randModel == 0) {
					add = new Coin(copperCoinModel, new Location(randX, randY), "Copper", world);
				}else if (randModel == 1) {
					add = new Coin(silverCoinModel, new Location(randX, randY), "Silver", world);
				}else{
					add = new Coin(goldCoinModel, new Location(randX, randY), "Gold", world);
				}

				if (add.alcuinCheck(alcuin) || add.constantineCheck(constantine) || add.goodrickeCheck(goodricke) || add.langwithCheck(langwith) || add.shopCheck(shop)){
					nextloop = true;
				}

				for (powerUp powerUpDatum : powerUpData) {
					if (add.powerUpCheck(powerUpDatum)){
						nextloop = true;
						break;
					}
				}

				for (Coin coinDatum : coinData){
					if (add.coinCheck(coinDatum)){
						nextloop = true;
						break;
					}
				}

				for (Stone stoneDatum : stoneData){
					if (add.stoneCheck(stoneDatum)){
						nextloop  = true;
						break;
					}
				}

				if (!nextloop){
					loop = false;
				}else{
					add.kill();
				}
			}
			coinData.add(add);
		}

		// Giant Ducks
		for (int i = 0; i < longBoi; i++){
			randX = 50 + rn.nextInt(3950);
			randY = 50 + rn.nextInt(3950);
			Body body = createEnemy(false, new Vector2(randX, randY),world);
			Duck newDuck = new Duck(body, bigDuckModel, 3f, new Location(randX,randY), 50000, world);

			newDuck.setTarget(playerShips.getEntityBody());
			newDuck.cannonBallSprite = new Sprite(new Texture(Gdx.files.internal("models/duck_v4.png")));
			newDuck.shooting = true;


			// Status of entity AI
			Arrive<Vector2> arrives = new Arrive<>(newDuck, pp)
					.setTimeToTarget(0.01f)
					.setArrivalTolerance(175f)
					.setDecelerationRadius(50);
			newDuck.setBehavior(arrives);
			ducks.add(newDuck);
		}

		// Ducks
		for (int i = 0; i < maxDucks; i++){
			randX = 50 + rn.nextInt(3950);
			randY = 50 + rn.nextInt(3950);
			Body body = createEnemy(false, new Vector2(randX, randY),world);
			Duck newDuck = new Duck(body,duckModel, 300f, new Location(randX,randY), 5, world);

			newDuck.setTarget(playerShips.getEntityBody());


			// Status of entity AI
			Arrive<Vector2> arrives = new Arrive<>(newDuck, pp)
					.setTimeToTarget(10f)
					.setArrivalTolerance(300f)
					.setDecelerationRadius(500);
			newDuck.setBehavior(arrives);
			ducks.add(newDuck);
		}


		// Power-ups
		for (int i = 0; i < maxPowerups; i++) {
			Boolean loop = true;
			powerUp add = null;
			while (loop){
				Boolean nextloop = false;
				randX = 50 + rn.nextInt(3950);
				randY = 50 + rn.nextInt(3950);
				randModel = rn.nextInt(5);
				if (randModel == 0) {
					model = speedUpModel;
					randType = "Speed Up";
				} else if (randModel == 1) {
					model = incDamageModel;
					randType = "Damage Increase";
				} else if (randModel == 2) {
					model = invincibilityModel;
					randType = "Invincible";
				} else if (randModel == 3) {
					model = coinMulModel;
					randType = "Coin Multiplier";
				} else {
					model = pointMulModel;
					randType = "Point Multiplier";
				}
				add = new powerUp(model, new Location(randX, randY), randType, world);
				if (add.alcuinCheck(alcuin) || add.constantineCheck(constantine) || add.goodrickeCheck(goodricke) || add.langwithCheck(langwith) || add.shopCheck(shop)){
					nextloop = true;
				}

				for (powerUp powerUpDatum : powerUpData) {
					if (add.powerUpCheck(powerUpDatum)){
						nextloop = true;
						break;
					}
				}

				for (Coin coinDatum : coinData){
					if (add.coinCheck(coinDatum)){
						nextloop = true;
						break;
					}
				}

				for (Stone stoneDatum : stoneData){
					if (add.stoneCheck(stoneDatum)){
						nextloop  = true;
						break;
					}
				}

				if (!nextloop){
					loop = false;
				}else{
					add.death();
				}
			}
			powerUpData.add(add);
		}

		// Ships
		for (int i = 0; i < maxShips; i++){
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
			Body body = createEnemy(false, new Vector2(randX, randY),world);
			EnemyShip newEnemy = new EnemyShip(body,model, 300f, new Location(randX,randY), randHealth, world);
			newEnemy.setTarget(playerShips.getEntityBody());


			// Status of entity AI
			Arrive<Vector2> arrives = new Arrive<>(newEnemy, pp)
					.setTimeToTarget(5f)
					.setArrivalTolerance(300f)
					.setDecelerationRadius(500);
			newEnemy.setBehavior(arrives);

			hostileShips.add(newEnemy);
		}

		// Stones
		for (int i = 0; i < maxStones; i++){
			Boolean loop = true;
			Stone add = null;
			while (loop == true) {
				Boolean nextloop = false;
				randX = 50 + rn.nextInt(3950);
				randY = 50 + rn.nextInt(3950);
				randModel = rn.nextInt(3);
				if (randModel == 0) {
					model = stoneModelA;
				} else if (randModel == 1) {
					model = stoneModelB;
				} else {
					model = stoneModelC;
				}
				add = new Stone(model, new Location(randX, randY), world);
				if (add.alcuinCheck(alcuin) || add.constantineCheck(constantine) || add.goodrickeCheck(goodricke) || add.langwithCheck(langwith) || add.shopCheck(shop)){
					nextloop = true;
				}

				for (powerUp powerUpDatum : powerUpData) {
					if (add.powerUpCheck(powerUpDatum)){
						nextloop = true;
						break;
					}
				}

				for (Coin coinDatum : coinData){
					if (add.coinCheck(coinDatum)){
						nextloop = true;
						break;
					}
				}

				for (Stone stoneDatum : stoneData){
					if (add.stoneCheck(stoneDatum)){
						nextloop  = true;
						break;
					}
				}

				if (!nextloop){
					loop = false;
				}else{
					add.death();
				}
			}
			stoneData.add(add);
		}
	}

	public void closeAndSave(){

		Json currencySaveFile = new Json();

		String currencyInfo = currencySaveFile.toJson( Currency.get().balance(Currency.Type.GOLD)+ "\n"); //Coins amount
		currencyInfo += currencySaveFile.toJson( Currency.get().balance(Currency.Type.POINTS)); //Points amount

		FileHandle currencyFile = Gdx.files.local("saves/currencySaveFile.json");
		currencyFile.writeString(currencyInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json coinSaveFile = new Json();

		String coinInfo = "";

		for (Coin coinDatum : coinData) {
			coinInfo += coinSaveFile.toJson(
					coinDatum.body.getPosition().x 	//Location x
							+ "\n" +
							coinDatum.body.getPosition().y + "\n"); //Location y
			coinInfo += coinSaveFile.toJson(coinDatum.type+ "\n");
		}

		FileHandle coinFile = Gdx.files.local("saves/coinSaveFile.json");
		coinFile.writeString(coinInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json powerSaveFile = new Json();

		String powerInfo = "";

		for (powerUp powerUpDatum : powerUpData) {
			powerInfo += powerSaveFile.toJson(
					powerUpDatum.body.getPosition().x 	//Location x
							+ "\n" +
							powerUpDatum.body.getPosition().y + "\n"); //Location y
			powerInfo += powerSaveFile.toJson(powerUpDatum.powerUpType+ "\n"); //Type
		}

		FileHandle powerFile = Gdx.files.local("saves/powerSaveFile.json");
		powerFile.writeString(powerInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json stoneSaveFile = new Json();

		String stoneInfo = "";

		for (Stone stoneDatum : stoneData) {
			stoneInfo += stoneSaveFile.toJson(
					stoneDatum.body.getPosition().x 	//Location x
							+ "\n" +
							stoneDatum.body.getPosition().y + "\n"); //Location y
		}

		FileHandle stoneFile = Gdx.files.local("saves/stoneSaveFile.json");
		stoneFile.writeString(stoneInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json collegeSaveFile = new Json();

		String collegeInfo = "";
		collegeInfo += collegeSaveFile.toJson(alcuin.health + "\n"); 		//Health alcuin
		collegeInfo += collegeSaveFile.toJson(langwith.health + "\n");		//Health langwith
		collegeInfo += collegeSaveFile.toJson(goodricke.health + "\n");		//Health goodricke
		collegeInfo += collegeSaveFile.toJson(constantine.health + "\n");	//Health constantine


		FileHandle collegeFile = Gdx.files.local("saves/collegeSaveFile.json");
		collegeFile.writeString(collegeInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json duckSaveFile = new Json();

		String duckInfo = "";

		for (Duck duckDatum : ducks) {
			duckInfo += duckSaveFile.toJson(
					duckDatum.body.getPosition().x 	//Location x
							+ "\n" +
							duckDatum.body.getPosition().y + "\n"); //Location y
			duckInfo += duckSaveFile.toJson(duckDatum.getHealth()+ "\n"); //Health
		}

		FileHandle duckFile = Gdx.files.local("saves/duckSaveFile.json");
		duckFile.writeString(duckInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json enemySaveFile = new Json();

		String enemyInfo = "";

		for (EnemyShip ship : hostileShips) {
			enemyInfo += enemySaveFile.toJson(
					ship.body.getPosition().x 	//Location x
							+ "\n" +
							ship.body.getPosition().y + "\n"); //Location y
			enemyInfo += enemySaveFile.toJson(ship.getHealth()+ "\n"); //Health
			enemyInfo += enemySaveFile.toJson(ship.timer+ "\n"); 	//Next shot timer
		}

		FileHandle enemyFile = Gdx.files.local("saves/enemySaveFile.json");
		enemyFile.writeString(enemyInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json playerSaveFile = new Json();

		String playerInfo = playerSaveFile.toJson(playerShips.shootingTimer + "\n"); //Shoot timer
		playerInfo += playerSaveFile.toJson(Ship.burstTimer + "\n"); //Burst timer
		playerInfo += playerSaveFile.toJson(Ship.health + "\n"); //Health
		playerInfo += playerSaveFile.toJson(
				playerShips.getEntityBody().getPosition().x 	//Location x
				+ "\n" +
				playerShips.getEntityBody().getPosition().y + "\n"); //Location y

		playerInfo += playerSaveFile.toJson(playerShips.getCoinMulti() + "\n"); //Coin Multiplier
		playerInfo += playerSaveFile.toJson(playerShips.getPointMulti() + "\n"); //Point Multiplier
		playerInfo += playerSaveFile.toJson(playerShips.priorCoinMulti + "\n"); //Prior coin Multiplier

		FileHandle playerFile = Gdx.files.local("saves/playerSaveFile.json");
		playerFile.writeString(playerInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json difficulttySaveFile = new Json();

		String difficultyInfo = "";
		difficultyInfo += difficulttySaveFile.toJson(DifficultyScreen.difficulty + "\n");	//Difficulty


		FileHandle diffFile = Gdx.files.local("saves/difficultySaveFile.json");
		diffFile.writeString(difficultyInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json gameScreenSaveFile = new Json();

		String gameInfo = "";
		gameInfo += gameScreenSaveFile.toJson(GameScreen.collegesKilled + "\n");	//Colleges killed
		gameInfo += gameScreenSaveFile.toJson(GameScreen.speedTimer + "\n");		//Speed timer
		gameInfo += gameScreenSaveFile.toJson(GameScreen.damageTimer + "\n");	//Damage timer
		gameInfo += gameScreenSaveFile.toJson(GameScreen.invincibilityTimer + "\n");	//Invincibility timer
		gameInfo += gameScreenSaveFile.toJson(GameScreen.coinTimer + "\n");	//Coin timer
		gameInfo += gameScreenSaveFile.toJson(GameScreen.pointTimer + "\n"); //Point timer


		FileHandle gameFile = Gdx.files.local("saves/gameScreenSaveFile.json");
		gameFile.writeString(gameInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json shopSaveFile = new Json();

		String shopInfo = "";
		shopInfo += shopSaveFile.toJson(ShopScreen.speedCost+ "\n");
		shopInfo += shopSaveFile.toJson(ShopScreen.multiCost + "\n");
		shopInfo += shopSaveFile.toJson(ShopScreen.healthCost + "\n");
		shopInfo += shopSaveFile.toJson(ShopScreen.cooldownCost + "\n");
		shopInfo += shopSaveFile.toJson(ShopScreen.speedTier + "\n");
		shopInfo += shopSaveFile.toJson(ShopScreen.multiTier + "\n");
		shopInfo += shopSaveFile.toJson(ShopScreen.healthTier + "\n");
		shopInfo += shopSaveFile.toJson(ShopScreen.cooldownTier + "\n");

		FileHandle shopFile = Gdx.files.local("saves/shopSaveFile.json");
		shopFile.writeString(shopInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		gamePreferences.setHasSave(true);

		Gdx.app.exit();


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