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

import java.util.Arrays;
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
 * @author Team 22: Ethan Alabaster,  Joe Dickinson, Sam Pearson,  Edward Poulter
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
	public static ArrayList<Coin> coinData = new ArrayList<>();
	public static ArrayList<powerUp> powerUpData = new ArrayList<>();
	public static ArrayList<Stone> stoneData = new ArrayList<>();
	public static ArrayList<EnemyShip> hostileShips = new ArrayList<>();
	public static ArrayList<Duck> ducks = new ArrayList<>();

	// Camera work
	private static OrthographicCamera camera;

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
	public static Sprite angryDuckModel;
	public static Sprite angryDuckAttack;
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
	public static float currentSpeed = 100000f;
	public static float maxSpeed = 100000f;
	public static float speedMul = 40f;
	public static int damageMul = 2;
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
	public static int maxDuckKills;
	public static int currentDuckKills = 0;
	private static GamePreferences gamePreferences = GamePreferences.get();

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

		weather = new Weather(this, 2000, 1200, 16);
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
		angryDuckModel = new Sprite(new Texture(Gdx.files.internal("models/duck_v4.png")));
		angryDuckAttack = new Sprite(new Texture(Gdx.files.internal("models/lasers.png")));
		bobsSprite = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
		shopSprite = new Sprite(new Texture(Gdx.files.internal("models/castle.png")));
		return true; //Successful
	}

	public static void createEntities(Body bobBody,World world,OrthographicCamera camera){

		// Set up spawning
		goodricke = new GoodrickeCollege(goodrickeCollegeSprite, new Location(150f, 4000f), 200f, world);
		alcuin = new AlcuinCollege(alcuinCollegeSprite, new Location(150f, 151f), 200f, world);
		constantine = new ConstantineCollege(constantineCollegeSprite, new Location(3950f, 4000f), 200f, world);
		langwith = new LangwithCollege(langwithCollegeSprite, new Location(3950f, 151f), 200f, world);

		shop = new shop1(shopSprite, new Location(2050f, 2050f), -1, world);

		if (LoadingScreen.loadedGame){

			load(bobBody,world,camera);
		}
		else {

			playerShips = new Ship(playerModel, currentSpeed, 100f, 0.3f, 1f, new Location(2000f, 1800f), playerModel.getHeight(), playerModel.getWidth(), camera, world);
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


			spawn(world, pp);
		}
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

		for (Coin coinDatum : coinData) {
			coinDatum.draw(batch);
			if (coinDatum.rangeCheck(playerShips) && !coinDatum.dead) {
				coinDatum.death();
			}
		}
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


		for (powerUp powerUpDatum : powerUpData) {

			powerUpDatum.draw(batch);
			powerUpChecks(powerUpDatum, playerShips);
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

		powerUpUpdates(playerShips);
		duckUpdates(playerShips,world);

		//if(rain.isRaining) {

		//}
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
			Gdx.app.exit();
		}

	}

	/**
	 * Processes input based on an action
	 *
	 * @param playerShips : The player body
	 */
	public static void processInput(Ship playerShips) {
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
			maxDuckKills = -1;
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
			newDuck.cannonBallSprite = angryDuckModel;
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

	public static void closeAndSave(){

		Json currencySaveFile = new Json();

		String currencyInfo = "\"";
		currencyInfo +=	currencySaveFile.toJson( Currency.get().balance(Currency.Type.GOLD)); //Coins amount
		currencyInfo += currencySaveFile.toJson( "," );
		currencyInfo += currencySaveFile.toJson( Currency.get().balance(Currency.Type.POINTS)); //Points amount
		currencyInfo += currencySaveFile.toJson( "," );

		FileHandle currencyFile = Gdx.files.local("saves/currencySaveFile.json");
		currencyFile.writeString(currencyInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json coinSaveFile = new Json();

		String coinInfo = "\"";

		for (Coin coinDatum : coinData) {

			if (!coinDatum.dead) {
				coinInfo += coinSaveFile.toJson(coinDatum.body.getPosition().x) ;   //Location x
				coinInfo += coinSaveFile.toJson( "," );
				coinInfo += coinSaveFile.toJson(coinDatum.body.getPosition().y);  //location y
				coinInfo += coinSaveFile.toJson( "," );
				coinInfo += coinSaveFile.toJson(coinDatum.type);
				coinInfo += coinSaveFile.toJson( "," );//Coin type
			}
		}


		FileHandle coinFile = Gdx.files.local("saves/coinSaveFile.json");
		coinFile.writeString(coinInfo,false);



		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json powerSaveFile = new Json();

		String powerInfo = "\"";

		for (powerUp powerUpDatum : powerUpData) {

			if (!powerUpDatum.dead) {
				powerInfo += powerSaveFile.toJson(powerUpDatum.body.getPosition().x ); //Location x
				powerInfo += powerSaveFile.toJson("," );
				powerInfo += powerSaveFile.toJson(powerUpDatum.body.getPosition().y); //Location y
				powerInfo += powerSaveFile.toJson("," );
				powerInfo += powerSaveFile.toJson(powerUpDatum.powerUpType); //Type
				powerInfo += powerSaveFile.toJson("," );
			}
		}

		FileHandle powerFile = Gdx.files.local("saves/powerSaveFile.json");
		powerFile.writeString(powerInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json stoneSaveFile = new Json();

		String stoneInfo = "\"";

		for (Stone stoneDatum : stoneData) {
			stoneInfo += stoneSaveFile.toJson(stoneDatum.body.getPosition().x); //Location x
			stoneInfo += stoneSaveFile.toJson(",");
			stoneInfo += stoneSaveFile.toJson(stoneDatum.body.getPosition().y); //Location y
			stoneInfo += stoneSaveFile.toJson(",");

			if (stoneDatum.texture == stoneModelA){
				stoneInfo += stoneSaveFile.toJson("A");
				stoneInfo += stoneSaveFile.toJson(",");
			}
			else if (stoneDatum.texture == stoneModelB){
				stoneInfo += stoneSaveFile.toJson("B");
				stoneInfo += stoneSaveFile.toJson(",");
			}
			else{
				stoneInfo += stoneSaveFile.toJson("C");
				stoneInfo += stoneSaveFile.toJson(",");

			}

		}

		FileHandle stoneFile = Gdx.files.local("saves/stoneSaveFile.json");
		stoneFile.writeString(stoneInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json collegeSaveFile = new Json();

		String collegeInfo = "\"";
		collegeInfo += collegeSaveFile.toJson(alcuin.health ); 		//Health alcuin
		collegeInfo += collegeSaveFile.toJson("," );
		collegeInfo += collegeSaveFile.toJson(langwith.health);		//Health langwith
		collegeInfo += collegeSaveFile.toJson("," );
		collegeInfo += collegeSaveFile.toJson(goodricke.health);		//Health goodricke
		collegeInfo += collegeSaveFile.toJson("," );
		collegeInfo += collegeSaveFile.toJson(constantine.health);	//Health constantine
		collegeInfo += collegeSaveFile.toJson("," );


		FileHandle collegeFile = Gdx.files.local("saves/collegeSaveFile.json");
		collegeFile.writeString(collegeInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json duckSaveFile = new Json();

		String duckInfo = "\"";

		for (Duck duckDatum : ducks) {
			duckInfo += duckSaveFile.toJson(duckDatum.body.getPosition().x); 	//Location x
			duckInfo += duckSaveFile.toJson("," );
			duckInfo += duckSaveFile.toJson(duckDatum.body.getPosition().y); //Location y
			duckInfo += duckSaveFile.toJson("," );
			duckInfo += duckSaveFile.toJson(duckDatum.getHealth()); //Health
			duckInfo += duckSaveFile.toJson("," );
			if (duckDatum.texture == angryDuckModel) {
				duckInfo += duckSaveFile.toJson("A"); //Type
				duckInfo += duckSaveFile.toJson(",");
			}
			else if (duckDatum.texture == bigDuckModel) {
				duckInfo += duckSaveFile.toJson("B"); //Type
				duckInfo += duckSaveFile.toJson(",");
			}
			else {
				duckInfo += duckSaveFile.toJson("C"); //Type
				duckInfo += duckSaveFile.toJson(",");

			}
		}

		FileHandle duckFile = Gdx.files.local("saves/duckSaveFile.json");
		duckFile.writeString(duckInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json enemySaveFile = new Json();

		String enemyInfo = "\"";

		for (EnemyShip ship : hostileShips) {
			enemyInfo += enemySaveFile.toJson(ship.body.getPosition().x);	//Location x
			enemyInfo += enemySaveFile.toJson("," );
			enemyInfo += enemySaveFile.toJson(ship.body.getPosition().y); //Location y
			enemyInfo += enemySaveFile.toJson("," );
			enemyInfo += enemySaveFile.toJson(ship.getHealth()); //Health
			enemyInfo += enemySaveFile.toJson("," );
			enemyInfo += enemySaveFile.toJson(ship.timer); 	//Next shot timer
			enemyInfo += enemySaveFile.toJson("," );
			if (ship.texture == enemyModelA) {
				enemyInfo += enemySaveFile.toJson("A"); //Type
				enemyInfo += enemySaveFile.toJson(",");
			}
			else if (ship.texture == enemyModelB) {
				enemyInfo += enemySaveFile.toJson("B"); //Type
				enemyInfo += enemySaveFile.toJson(",");
			}
			else {
				enemyInfo += enemySaveFile.toJson("C"); //Type
				enemyInfo += enemySaveFile.toJson(",");

			}
			enemyInfo += enemySaveFile.toJson(ship.maxHealth); 	// max health
			enemyInfo += enemySaveFile.toJson("," );
		}

		FileHandle enemyFile = Gdx.files.local("saves/enemySaveFile.json");
		enemyFile.writeString(enemyInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json playerSaveFile = new Json();

		String playerInfo = "\"";
		playerInfo += playerSaveFile.toJson(playerShips.getEntityBody().getPosition().x); 	//Location x
		playerInfo += playerSaveFile.toJson("," );
		playerInfo += playerSaveFile.toJson(playerShips.getEntityBody().getPosition().y); //Location y
		playerInfo += playerSaveFile.toJson("," );
		playerInfo += playerSaveFile.toJson(playerShips.shootingTimer); //Shoot timer
		playerInfo += playerSaveFile.toJson("," );
		playerInfo += playerSaveFile.toJson(Ship.burstTimer); //Burst timer
		playerInfo += playerSaveFile.toJson("," );
		playerInfo += playerSaveFile.toJson(Ship.health); //Health
		playerInfo += playerSaveFile.toJson("," );

		playerInfo += playerSaveFile.toJson(playerShips.getCoinMulti()); //Coin Multiplier
		playerInfo += playerSaveFile.toJson("," );
		playerInfo += playerSaveFile.toJson(playerShips.getPointMulti()); //Point Multiplier
		playerInfo += playerSaveFile.toJson("," );
		playerInfo += playerSaveFile.toJson(playerShips.priorCoinMulti); //Prior coin Multiplier
		playerInfo += playerSaveFile.toJson("," );
		playerInfo += playerSaveFile.toJson(playerShips.timeToRegen); //Regen timer
		playerInfo += playerSaveFile.toJson("," );


		FileHandle playerFile = Gdx.files.local("saves/playerSaveFile.json");
		playerFile.writeString(playerInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json difficultySaveFile = new Json();

		String difficultyInfo = difficultySaveFile.toJson(DifficultyScreen.difficulty);	//Difficulty

		FileHandle diffFile = Gdx.files.local("saves/difficultySaveFile.json");
		diffFile.writeString(difficultyInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json gameScreenSaveFile = new Json();

		String gameInfo = "\"";
		gameInfo += gameScreenSaveFile.toJson(GameScreen.collegesKilled);	//Colleges killed
		gameInfo += gameScreenSaveFile.toJson("," );
		gameInfo += gameScreenSaveFile.toJson(GameScreen.speedTimer);		//Speed timer
		gameInfo += gameScreenSaveFile.toJson("," );
		gameInfo += gameScreenSaveFile.toJson(GameScreen.damageTimer);	//Damage timer
		gameInfo += gameScreenSaveFile.toJson("," );
		gameInfo += gameScreenSaveFile.toJson(GameScreen.invincibilityTimer);	//Invincibility timer
		gameInfo += gameScreenSaveFile.toJson("," );
		gameInfo += gameScreenSaveFile.toJson(GameScreen.coinTimer);	//Coin timer
		gameInfo += gameScreenSaveFile.toJson("," );
		gameInfo += gameScreenSaveFile.toJson(GameScreen.pointTimer); //Point timer
		gameInfo += gameScreenSaveFile.toJson("," );
		gameInfo += gameScreenSaveFile.toJson(GameScreen.currentDuckKills); //Duck kills
		gameInfo += gameScreenSaveFile.toJson("," );



		FileHandle gameFile = Gdx.files.local("saves/gameScreenSaveFile.json");
		gameFile.writeString(gameInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		Json shopSaveFile = new Json();

		String shopInfo = "\"";
		shopInfo += shopSaveFile.toJson(ShopScreen.speedCost);
		shopInfo += shopSaveFile.toJson("," );
		shopInfo += shopSaveFile.toJson(ShopScreen.multiCost);
		shopInfo += shopSaveFile.toJson("," );
		shopInfo += shopSaveFile.toJson(ShopScreen.healthCost);
		shopInfo += shopSaveFile.toJson("," );
		shopInfo += shopSaveFile.toJson(ShopScreen.cooldownCost);
		shopInfo += shopSaveFile.toJson("," );
		shopInfo += shopSaveFile.toJson(ShopScreen.speedTier);
		shopInfo += shopSaveFile.toJson("," );
		shopInfo += shopSaveFile.toJson(ShopScreen.multiTier);
		shopInfo += shopSaveFile.toJson("," );
		shopInfo += shopSaveFile.toJson(ShopScreen.healthTier);
		shopInfo += shopSaveFile.toJson("," );
		shopInfo += shopSaveFile.toJson(ShopScreen.cooldownTier);
		shopInfo += shopSaveFile.toJson("," );

		FileHandle shopFile = Gdx.files.local("saves/shopSaveFile.json");
		shopFile.writeString(shopInfo,false);

		/////////////////////////////////////////////////////////////////////////////////////////////

		Json bobSaveFile = new Json();

		String bobInfo = "\"";

		bobInfo += bobSaveFile.toJson(bob.body.getPosition().x);	//Location x
		bobInfo += bobSaveFile.toJson("," );
		bobInfo += bobSaveFile.toJson(bob.body.getPosition().y); //Location y
		bobInfo += bobSaveFile.toJson("," );
		bobInfo += bobSaveFile.toJson(bob.getHealth()); //Health
		bobInfo += bobSaveFile.toJson("," );
		bobInfo += bobSaveFile.toJson(bob.timer); 	//Next shot timer
		bobInfo += bobSaveFile.toJson("," );

		FileHandle bobFile = Gdx.files.local("saves/bobSaveFile.json");
		bobFile.writeString(bobInfo,false);



		/////////////////////////////////////////////////////////////////////////////////////////////////////

		gamePreferences.setHasSave(true);


	}

	public static void load(Body bobBody,World world,OrthographicCamera camera){
		//DIFFICULTY

		FileHandle difficultyFile = Gdx.files.local("saves/difficultySaveFile.json");
		String diffText = difficultyFile.readString();

		DifficultyScreen.difficulty = Integer.parseInt(diffText);
		if (DifficultyScreen.difficulty == 1){
			maxDuckKills = 15;
		}
		else if (DifficultyScreen.difficulty == 2){
			maxDuckKills = 10;
		}
		else if (DifficultyScreen.difficulty == 3){
			maxDuckKills = 5;
		}
		else{
			maxDuckKills = -1;
		}

		//PLAYER

		FileHandle playerFile = Gdx.files.local("saves/playerSaveFile.json");
		String playerText = playerFile.readString();
		String[] playerList = playerText.split(",");

		playerShips = new Ship(playerModel, currentSpeed, 100f, 0.3f, 1f,
				new Location(Float.parseFloat((playerList[0].substring(1,playerList[0].length() - 1))), Float.parseFloat((playerList[1].substring(1,playerList[1].length() - 1)))),
				playerModel.getHeight(), playerModel.getWidth(),
				camera,
				world);
		playerShips.createBody();

		// Creates damping to player
		playerShips.getEntityBody().setLinearDamping(0.5f);
		playerShips.setMaxSpeed(currentSpeed, speedMul);

		player = new EntityAi(playerShips.getEntityBody(), 30f);
		Steerable<Vector2> pp = player;

		playerShips.shootingTimer = Float.parseFloat((playerList[2].substring(1,playerList[2].length() - 1)));
		playerShips.burstTimer = Float.parseFloat((playerList[3].substring(1,playerList[3].length() - 1)));
		playerShips.health = Float.parseFloat((playerList[4].substring(1,playerList[4].length() - 1)));
		playerShips.coinMulti = Integer.parseInt((playerList[5].substring(1,playerList[5].length() - 1)));
		playerShips.pointMulti = Integer.parseInt((playerList[6].substring(1,playerList[6].length() - 1)));
		playerShips.priorCoinMulti = Integer.parseInt((playerList[7].substring(1,playerList[7].length() - 1)));
		playerShips.timeToRegen = Float.parseFloat((playerList[8].substring(1,playerList[8].length() - 1)));


		//CURRENCY

		FileHandle currencyFile = Gdx.files.local("saves/currencySaveFile.json");
		String currencyText = currencyFile.readString();
		String[] currencyList = currencyText.split(",");
		Currency.get().give(Currency.Type.GOLD,Integer.parseInt(currencyList[0].substring(1,currencyList[0].length() - 1)));
		Currency.get().give(Currency.Type.POINTS,Integer.parseInt(currencyList[1].substring(1,currencyList[1].length() - 1)));


		//COINS

		FileHandle coinFile = Gdx.files.local("saves/coinSaveFile.json");
		String coinText = coinFile.readString();
		String[] coinList = coinText.split(",");

		for(int i = 0; i < coinList.length - 1; i = i + 3) {

			if (coinList[i + 2].equals("\"Copper\"")) {
				coinData.add(new Coin(copperCoinModel, new Location(Integer.parseInt((coinList[i].substring(1,coinList[i].length() - 1))), Integer.parseInt((coinList[i + 1].substring(1,coinList[i + 1].length() - 1)))), "Copper", world));
			} else if (coinList[i + 2].equals("\"Silver\"")) {
				coinData.add(new Coin(silverCoinModel, new Location(Integer.parseInt((coinList[i].substring(1,coinList[i].length() - 1))), Integer.parseInt((coinList[i + 1].substring(1,coinList[i + 1].length() - 1)))), "Silver", world));
			} else {
				coinData.add(new Coin(goldCoinModel, new Location(Integer.parseInt((coinList[i].substring(1,coinList[i].length() - 1))), Integer.parseInt((coinList[i + 1].substring(1,coinList[i + 1].length() - 1)))), "Gold", world));
			}

		}
		//POWERUPS

		FileHandle powerUpFile = Gdx.files.local("saves/powerSaveFile.json");
		String powerText = powerUpFile.readString();
		String[] powerList = powerText.split(",");

		//System.out.println(Arrays.toString(powerList));


		for(int i = 0; i < powerList.length - 1; i = i + 3) {
			if (powerList[i + 2].equals("\"Speed Up\"")) {
				powerUpData.add(new powerUp(speedUpModel, new Location(Float.parseFloat(powerList[i].substring(1,powerList[i].length()-1)), Float.parseFloat(powerList[i + 1].substring(1,powerList[i + 1].length()-1))), "Speed Up", world));
			} else if (powerList[i + 2].equals("\"Damage Increase\"")) {
				powerUpData.add(new powerUp(incDamageModel, new Location(Float.parseFloat(powerList[i].substring(1,powerList[i].length()-1)), Float.parseFloat(powerList[i + 1].substring(1,powerList[i + 1].length()-1))), "Damage Increase", world));
			}
			else if (powerList[i + 2].equals("\"Invincible\"")) {
				powerUpData.add(new powerUp(invincibilityModel, new Location(Float.parseFloat(powerList[i].substring(1,powerList[i].length()-1)), Float.parseFloat(powerList[i + 1].substring(1,powerList[i + 1].length()-1))), "Invincible", world));
			}
			else if (powerList[i + 2].equals("\"Coin Multiplier\"")) {
				powerUpData.add(new powerUp(coinMulModel, new Location(Float.parseFloat(powerList[i].substring(1,powerList[i].length()-1)), Float.parseFloat(powerList[i + 1].substring(1,powerList[i + 1].length()-1))), "Coin Multiplier", world));
			}else {
				powerUpData.add(new powerUp(pointMulModel, new Location(Float.parseFloat(powerList[i].substring(1,powerList[i].length()-1)), Float.parseFloat(powerList[i + 1].substring(1,powerList[i + 1].length()-1))), "Point Multiplier", world));
			}

		}
		//STONE

		FileHandle stoneFile = Gdx.files.local("saves/stoneSaveFile.json");
		String stoneText = stoneFile.readString();
		String[] stoneList = stoneText.split(",");

		//System.out.println(Arrays.toString(stoneList));


		for(int i = 0; i < stoneList.length - 1; i = i + 3) {
			if (stoneList[i + 2].equals("\"A\"")) {
				stoneData.add(new Stone(stoneModelA, new Location(Float.parseFloat(stoneList[i].substring(1,stoneList[i].length()-1)), Float.parseFloat(stoneList[i + 1].substring(1,stoneList[i + 1].length()-1))),  world));
			} else if (stoneList[i + 2].equals("\"B\"")) {
				stoneData.add(new Stone(stoneModelB, new Location(Float.parseFloat(stoneList[i].substring(1,stoneList[i].length()-1)), Float.parseFloat(stoneList[i + 1].substring(1,stoneList[i + 1].length()-1))),  world));
			}
			else {
				stoneData.add(new Stone(stoneModelC, new Location(Float.parseFloat(stoneList[i].substring(1,stoneList[i].length()-1)), Float.parseFloat(stoneList[i + 1].substring(1,stoneList[i + 1].length()-1))),  world));
			}
		}

		//COLLEGE

		FileHandle collegeFile = Gdx.files.local("saves/collegeSaveFile.json");
		String collegeText = collegeFile.readString();
		String[] collegeList = collegeText.split(",");


		alcuin.health =Float.parseFloat(collegeList[0].substring(1,collegeList[0].length()-1));
		if (alcuin.health == 0 ){
			alcuin.dead = true;
		}
		langwith.health =  Float.parseFloat(collegeList[1].substring(1,collegeList[1].length()-1));
		if (langwith.health == 0 ){
			langwith.dead = true;
		}
		goodricke.health =  Float.parseFloat(collegeList[2].substring(1,collegeList[2].length()-1));
		if (goodricke.health == 0 ){
			goodricke.dead = true;
		}
		constantine.health =  Float.parseFloat(collegeList[3].substring(1,collegeList[3].length()-1));
		if (constantine.health == 0 ){
			constantine.dead = true;
		}
		//DUCKS

		FileHandle duckFile = Gdx.files.local("saves/duckSaveFile.json");
		String duckText = duckFile.readString();
		String[] duckList = duckText.split(",");
		Sprite duckTexture;
		int duckMaxHealth;
		boolean shooting = false;
		Sprite ballSprite = new Sprite();


		for(int i = 0; i < duckList.length - 1; i = i + 4) {
			if (duckList[i + 3].equals("\"A\"")) {
				duckTexture = angryDuckModel;
				duckMaxHealth = 333 * DifficultyScreen.difficulty;
				shooting = true;
				ballSprite = angryDuckAttack;
			} else if (duckList[i + 3].equals("\"B\"")) {
				duckTexture = bigDuckModel;
				duckMaxHealth = 50000;
				shooting = true;
				ballSprite = angryDuckModel;
			}
			else {
				duckTexture = duckModel;
				duckMaxHealth = 5;
			}

			Body body = createEnemy(false, new Vector2(Float.parseFloat((duckList[i].substring(1,duckList[i].length() - 1))), Float.parseFloat((duckList[i + 1].substring(1,duckList[i + 1].length() - 1)))),world);
			Duck newDuck = new Duck(body,duckTexture, 300f, new Location(Float.parseFloat((duckList[i].substring(1,duckList[i].length() - 1))),Float.parseFloat((duckList[i + 1].substring(1,duckList[i + 1].length() - 1)))), duckMaxHealth, world);

			newDuck.setTarget(playerShips.getEntityBody());

			newDuck.health = Integer.parseInt((duckList[i + 2].substring(1,duckList[i + 2].length() - 1)));


			if (duckList[i + 3].equals("\"A\"")) {
				newDuck.hitBox =  new Rectangle(newDuck.body.getPosition().x - 300, newDuck.body.getPosition().y - 300, newDuck.texture.getWidth() + 600, newDuck.texture.getHeight() + 600);

			}

			newDuck.shooting = shooting;
			newDuck.cannonBallSprite = ballSprite;

			// Status of entity AI
			Arrive<Vector2> arrives = new Arrive<>(newDuck, pp)
					.setTimeToTarget(10f)
					.setArrivalTolerance(300f)
					.setDecelerationRadius(500);
			newDuck.setBehavior(arrives);
			ducks.add(newDuck);
		}

		//ENEMY SHIP

		FileHandle enemyFile = Gdx.files.local("saves/enemySaveFile.json");
		String enemyText = enemyFile.readString();
		String[] enemyList = enemyText.split(",");


		Sprite shipTexture;

		for(int i = 0; i < enemyList.length - 1; i = i + 6) {

			if (enemyList[i + 4].equals("\"A\"")) {
				shipTexture = enemyModelA;

			} else if (enemyList[i + 4].equals("\"B\"")) {
				shipTexture = enemyModelB;
			}
			else {
				shipTexture = enemyModelC;
			}



			Body body = createEnemy(false, new Vector2(Float.parseFloat((enemyList[i].substring(1,enemyList[i].length() - 1))), Float.parseFloat((enemyList[i + 1].substring(1,enemyList[i + 1].length() - 1)))), world);
			EnemyShip newEnemy = new EnemyShip(body, shipTexture, 300f,
					new Location(Float.parseFloat((enemyList[i].substring(1,enemyList[i].length() - 1))), Float.parseFloat((enemyList[i + 1].substring(1,enemyList[i + 1].length() - 1)))),
					Integer.parseInt((enemyList[i + 5].substring(1,enemyList[i + 5].length() - 1))), world);
			newEnemy.setTarget(playerShips.getEntityBody());

			newEnemy.health = Integer.parseInt((enemyList[i + 2].substring(1,enemyList[i + 2].length() - 1)));
			newEnemy.timer = Float.parseFloat((enemyList[i + 3].substring(1,enemyList[i + 3].length() - 1)));


			// Status of entity AI
			Arrive<Vector2> arrives = new Arrive<>(newEnemy, pp)
					.setTimeToTarget(5f)
					.setArrivalTolerance(300f)
					.setDecelerationRadius(500);
			newEnemy.setBehavior(arrives);

			hostileShips.add(newEnemy);
		}

		//GAMESCREEN

		FileHandle gameFile = Gdx.files.local("saves/gameScreenSaveFile.json");
		String gameText = gameFile.readString();
		String[] gameList = gameText.split(",");



		collegesKilled = Integer.parseInt((gameList[0].substring(1,gameList[0].length() - 1)));
		speedTimer =Float.parseFloat((gameList[1].substring(1,gameList[1].length() - 1)));
		damageTimer = Float.parseFloat((gameList[2].substring(1,gameList[2].length() - 1)));
		invincibilityTimer = Float.parseFloat((gameList[3].substring(1,gameList[3].length() - 1)));
		coinTimer = Float.parseFloat((gameList[4].substring(1,gameList[4].length() - 1)));
		pointTimer = Float.parseFloat((gameList[5].substring(1,gameList[5].length() - 1)));
		currentDuckKills = Integer.parseInt((gameList[6].substring(1,gameList[6].length() - 1)));

		//SHOPSCREEN

		FileHandle shopFile = Gdx.files.local("saves/shopSaveFile.json");
		String shopText = shopFile.readString();
		String[] shopList = shopText.split(",");

		ShopScreen.speedCost = Integer.parseInt((shopList[0].substring(1,shopList[0].length() - 1)));
		ShopScreen.multiCost = Integer.parseInt((shopList[1].substring(1,shopList[1].length() - 1)));
		ShopScreen.healthCost = Integer.parseInt((shopList[2].substring(1,shopList[2].length() - 1)));
		ShopScreen.cooldownCost = Integer.parseInt((shopList[3].substring(1,shopList[3].length() - 1)));
		ShopScreen.speedTier = Integer.parseInt((shopList[4].substring(1,shopList[4].length() - 1)));
		ShopScreen.multiTier = Integer.parseInt((shopList[5].substring(1,shopList[5].length() - 1)));
		ShopScreen.healthTier = Integer.parseInt((shopList[6].substring(1,shopList[6].length() - 1)));
		ShopScreen.cooldownTier = Integer.parseInt((shopList[7].substring(1,shopList[7].length() - 1)));

		//BOB

		FileHandle bobFile = Gdx.files.local("saves/bobSaveFile.json");
		String bobText = bobFile.readString();
		String[] bobList = bobText.split(",");

		// Enemy creation "bob" and Entity AI controller
		bob = new EnemyShip(bobBody, bobsSprite, 300f, new Location(Float.parseFloat((bobList[0].substring(1,bobList[0].length() - 1))), Float.parseFloat((bobList[1].substring(1,bobList[1].length() - 1)))), 100, world);
		bob.setTarget(playerShips.getEntityBody());

		// Status of entity AI
		Arrive<Vector2> arrives = new Arrive<>(bob, pp)
				.setTimeToTarget(0.01f)
				.setArrivalTolerance(175f)
				.setDecelerationRadius(50);
		bob.setBehavior(arrives);

		bob.health = Integer.parseInt((bobList[2].substring(1,bobList[2].length() - 1)));
		bob.timer = Float.parseFloat((bobList[3].substring(1,bobList[3].length() - 1)));
	}

	public static void powerUpChecks(powerUp powerUpDatum, Ship player){

		if (Objects.equals(powerUpDatum.getPowerUpType(), "Speed Up")) {
		if (powerUpDatum.rangeCheck(player) && !powerUpDatum.dead) {
			speedTimer = 10;
		}
	} else if (Objects.equals(powerUpDatum.getPowerUpType(), "Invincible")) {
		if (powerUpDatum.rangeCheck(player) && !powerUpDatum.dead) {
			invincibilityTimer = 10;
		}
	} else if (Objects.equals(powerUpDatum.getPowerUpType(), "Damage Increase")) {
		if (powerUpDatum.rangeCheck(player) && !powerUpDatum.dead) {
			damageTimer = 10;
		}
	} else if (Objects.equals(powerUpDatum.getPowerUpType(), "Coin Multiplier")) {
		if (powerUpDatum.rangeCheck(player) && !powerUpDatum.dead) {
			coinTimer = 10;
		}
	} else if (Objects.equals(powerUpDatum.getPowerUpType(), "Point Multiplier")) {
		if (powerUpDatum.rangeCheck(player) && !powerUpDatum.dead) {
			pointTimer = 10;
		}
	}
		if (powerUpDatum.rangeCheck(player) && !powerUpDatum.dead) {
			powerUpDatum.death();

		}
	}

	public static void powerUpUpdates(Ship player){
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
			player.setInvincible(true);
		}else{
			player.setInvincible(false);
			invincibilityTimer = -1f;
		}
		if (damageTimer >= 0){
			damageTimer -= Gdx.graphics.getDeltaTime();
			player.setDamageMulti(damageMul);

		}else{
			player.setDamageMulti(1);
			damageTimer = -1f;
		}

		if (coinTimer == 10){
			coinTimer -= Gdx.graphics.getDeltaTime();
			player.setCoinMulti(1, true);
		}else if (coinTimer <= 0f && coinTimer != -1f){
			coinTimer = -1f;
			player.setCoinMulti(-1, false);
		}
		else if (coinTimer >= 0f){
			coinTimer -= Gdx.graphics.getDeltaTime();
		}



		if (pointTimer == 10){
			pointTimer -= Gdx.graphics.getDeltaTime();
			player.setPointMulti(2, true);
		}else if (pointTimer <= 0 && pointTimer !=-1){
			pointTimer = -1f;
			player.setPointMulti(-1, false);
		}
		else if (pointTimer >= 0f){
			pointTimer -= Gdx.graphics.getDeltaTime();
		}
	}

	public static void duckUpdates(Ship playerShip,World world){
		if (currentDuckKills >= 0){
			for (Duck duck : ducks){
				if (duck.deadDuck == 1){
					duck.deadDuck = 2;
					currentDuckKills += 1;
					duck.death(world);
				}
			}
		}

		if (currentDuckKills == maxDuckKills){
			currentDuckKills -= maxDuckKills;
			Body body = createEnemy(false, new Vector2(2000, 2000),world);
			Duck newDuck = new Duck(body, bigDuckModel, 3f, new Location(2000,2000), 50000, world);

			newDuck.setTarget(playerShip.getEntityBody());
			newDuck.cannonBallSprite = angryDuckModel;
			newDuck.shooting = true;


			// Status of entity AI
			Arrive<Vector2> arrives = new Arrive<>(newDuck, player)
					.setTimeToTarget(0.01f)
					.setArrivalTolerance(175f)
					.setDecelerationRadius(50);
			newDuck.setBehavior(arrives);
			ducks.add(newDuck);
			for (Duck duck : ducks){
				if (!duck.shooting && !duck.dead){
					duck.shooting = true;
					duck.health = 333 * DifficultyScreen.difficulty;
					duck.maxHealth = duck.health;
					duck.texture = angryDuckModel;
					duck.cannonBallSprite = angryDuckAttack;
					duck.hitBox =  new Rectangle(duck.body.getPosition().x - 300, duck.body.getPosition().y - 300, duck.texture.getWidth() + 600, duck.texture.getHeight() + 600);
				}
			}
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