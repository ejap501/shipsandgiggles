package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.impl.collectible.Coin;
import net.shipsandgiggles.pirate.entity.impl.collectible.powerUp;
import net.shipsandgiggles.pirate.entity.impl.obstacles.Stone;
import net.shipsandgiggles.pirate.entity.impl.shop.shop1;
import net.shipsandgiggles.pirate.entity.npc.EnemyShip;
import net.shipsandgiggles.pirate.listener.WorldContactListener;
import net.shipsandgiggles.pirate.entity.EntityAi;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.BallsManager;
import net.shipsandgiggles.pirate.entity.impl.college.AlcuinCollege;
import net.shipsandgiggles.pirate.entity.impl.college.ConstantineCollege;
import net.shipsandgiggles.pirate.entity.impl.college.GoodrickeCollege;
import net.shipsandgiggles.pirate.entity.impl.college.LangwithCollege;
import net.shipsandgiggles.pirate.screen.ScreenType;

import java.util.ArrayList;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;





public class GameScreen implements Screen {

	/** main game screen*/

	public LangwithCollege langwith;
	public ConstantineCollege constantine;
	public AlcuinCollege alcuin;
	public GoodrickeCollege goodricke;
	public shop1 shop;
	public static float collegesKilled = 0;

	public static HUDmanager hud;
	public DeathScreen deathScreen;

	public static ArrayList<ExplosionController> Explosions = new ArrayList<>();

	/** implement world*/
	public static World world;
	private final Ship playerShips;
	private Coin coins;
	private Coin another;
	private ArrayList<Coin> coinData = new ArrayList<>();
	private ArrayList<powerUp> powerUpData = new ArrayList<>();
	private ArrayList<Stone> stoneData = new ArrayList<>();
	private ArrayList<EnemyShip> hostileShips = new ArrayList<>();
	/** camera work*/
	private final OrthographicCamera camera;
	private final float Scale = 2;
	/**graphics */
	private final SpriteBatch batch; /**batch of images "objects" */
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
	//public Sprite speedDownModel;

	private final Box2DDebugRenderer renderer;
	//private final OrthoCachedTiledMapRenderer tmr;
	private final TiledMap map;
	private OrthogonalTiledMapRenderer maprender;
	float recordedSpeed = 0;
	int cameraState = 0;
	public Sprite water;
	public boolean intro = false;
	public float zoomedAmount = 0;
	EntityAi bob, player;
	public static int collegesCaptured = 0;

	Sprite cannonBall;

	/** Abilities*/
	float maxSpeed = 100f;
	float speedMul = 2f;
	int damageMul = 2;
	int coinMul = 2;
	int pointMul = 2;
	int speedTimer = 0;
	int damageTimer = 0;
	int invincibilityTimer = 0;
	int coinTimer = 0;
	int pointTimer = 0;

	public GameScreen() {

		/** initialization of everything*/

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



		/** objects setup*/



		playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
		coinModel = new Sprite(new Texture(Gdx.files.internal("models/gold_coin.png")));
		speedUpModel = new Sprite(new Texture(Gdx.files.internal("models/speed_up.png")));
		incDamageModel = new Sprite(new Texture(Gdx.files.internal("models/damage_increase.png")));
		invincibilityModel = new Sprite(new Texture(Gdx.files.internal("models/invincibility.png")));
		coinMulModel = new Sprite(new Texture(Gdx.files.internal("models/coin_multipler.png")));
		pointMulModel = new Sprite(new Texture(Gdx.files.internal("models/point_multiplier.png")));
		stoneModelA = new Sprite(new Texture(Gdx.files.internal("models/stone_1.png")));
		stoneModelB = new Sprite(new Texture(Gdx.files.internal("models/stone_2.png")));
		stoneModelC = new Sprite(new Texture(Gdx.files.internal("models/stone_3.png")));
		//speedDownModel = new Sprite(new Texture(Gdx.files.internal("models/gold_coin.png")));
		enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
		enemyModelB = new Sprite(new Texture(Gdx.files.internal("models/ship1.png")));
		enemyModelC = new Sprite(new Texture(Gdx.files.internal("models/dd.png")));
		playerShips = new Ship(playerModel, 40000f, 0f, 0.3f, 1f, new Location(_width / 2f, _height / 2f), playerModel.getHeight(), playerModel.getWidth(), camera);
		playerShips.createBody();

		/** map initialization */
		map = new TmxMapLoader().load("models/map.tmx");
		maprender = new OrthogonalTiledMapRenderer(map, 1f);
		new WorldCreator(this);

		//TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("collider").getObjects());

		/** creates damping to player */
		playerShips.getEntityBody().setLinearDamping(0.5f);
		playerShips.setMaxSpeed(maxSpeed, speedMul);

		Sprite bobsSprite = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));

		/** enemy creation "bob" and Entity ai controller*/
		Body body = createEnemy(false, new Vector2(_width / 3f, _height / 6f));
		bob = new EntityAi(body, 300f, bobsSprite,(int)bobsSprite.getWidth(),(int)bobsSprite.getHeight() );
		bob.setTarget(playerShips.getEntityBody());

		player = new EntityAi(playerShips.getEntityBody(), 3);
		Steerable<Vector2> pp = player;


		/** status of entity ai */
		Arrive<Vector2> arrives = new Arrive<Vector2>(bob, pp)
				.setTimeToTarget(0.01f)
				.setArrivalTolerance(175f)
				.setDecelerationRadius(50);
		bob.setBehavior(arrives);

		/** set up college*/
		goodricke = new GoodrickeCollege(goodrickeCollegeSprite, new Location(150f,975f), 200f, world);
		alcuin = new AlcuinCollege(alcuinCollegeSprite, new Location(1750f,151f), 200f, world);
		constantine = new ConstantineCollege(constantineCollegeSprite, new Location(1750f,975f), 200f, world);
		langwith = new LangwithCollege(langwithCollegeSprite, new Location(150f,151f), 200f, world);


		/** coins*/
		coinData.add(new Coin(coinModel, new Location(600f,600f), 1f, world));
		coinData.add(new Coin(coinModel, new Location(670f,600f), 1f, world));

		/** NPCs*/
		hostileShips.add(new EnemyShip(enemyModelA, new Location(900f,750f), 100f, world));
		hostileShips.add(new EnemyShip(enemyModelB, new Location(850f,750f), 100f, world));
		hostileShips.add(new EnemyShip(enemyModelC, new Location(950f,750f), 250f, world));

		//stoneData.add(new Stone(stoneModelA, new Location(810f,650f),1f, world));
		//stoneData.add(new Stone(stoneModelB, new Location(870f,650f),1f, world));
		//stoneData.add(new Stone(stoneModelC, new Location(930f,650f),1f, world));

		/** Powerups*/
		powerUpData.add(new powerUp(speedUpModel, new Location(740f,600f), "Speed Up",1f, world));
		//powerUpData.add(new powerUp(speedDownModel, new Location(790f,600f), "Speed Down",1f, world));
		powerUpData.add(new powerUp(invincibilityModel, new Location(810f,600f), "Invincible",1f, world));
		powerUpData.add(new powerUp(incDamageModel, new Location(880f,600f), "Damage Increase",1f, world));
		powerUpData.add(new powerUp(coinMulModel, new Location(950f,600f), "Coin Multiplier",1f, world));
		powerUpData.add(new powerUp(pointMulModel, new Location(1020f,600f), "Point Multiplier",1f, world));
		shop = new shop1(langwithCollegeSprite, new Location(500f,500f),-1,world);

		hud = new HUDmanager(batch);
		deathScreen = new DeathScreen(batch);


	}


	@Override
	public void show() {

	}

	@Override
	public void render(float deltaTime) {
		/** zoom controller for the intro*/
		if(!intro){
			camera.zoom -= 0.02f;
			zoomedAmount += 0.02;
			if(zoomedAmount >= 1){
				intro = true;
			}
		}


		/** does the update method*/
		update();
		/** colour creation for background*/
		Gdx.gl.glClearColor(.98f, .91f, .761f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		/** draws the water manually to use less resources*/
		//water.draw(batch);
		batch.end();
		maprender.render();
		//tmr.render();
		BallsManager.updateBalls(batch);

		/** setting ship position for the sprite of the player ship*/
		playerShips.getSprite().setPosition(playerShips.getEntityBody().getPosition().x * PIXEL_PER_METER - (playerShips.getSkin().getWidth() / 2f), playerShips.getEntityBody().getPosition().y * PIXEL_PER_METER - (playerShips.getSkin().getHeight() / 2f));
		playerShips.getSprite().setRotation((float) Math.toDegrees(playerShips.getEntityBody().getAngle()));




		//player
		//batch.draw(playerShips.getSkin(), playerShips.getEntityBody().getPosition().x * PIXEL_PER_METER - (playerShips.getSkin().getWidth() / 2f), playerShips.getEntityBody().getPosition().y * PIXEL_PER_METER - (playerShips.getSkin().getHeight() / 2f));
		//batch.draw(islandsTextures[0], islands[0].getPosition().x * PixelPerMeter - (islandsTextures[0].getWidth()/2), islands[0].getPosition().y * PixelPerMeter - (islandsTextures[0].getHeight()/2));
		//enemyShips.draw(batch);



		/** update all the colleges and entities*/
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
		for (int i = 0; i < coinData.size(); i++){
			coinData.get(i).draw(batch);
			if (coinData.get(i).rangeCheck(playerShips) && !coinData.get(i).dead){
				coinData.get(i).death();
			}
		}

		for (int i = 0; i < hostileShips.size(); i++){
			hostileShips.get(i).draw(batch);
			hostileShips.get(i).shootPlayer(playerShips);
		}

		for (int i = 0; i < powerUpData.size(); i++){
			powerUpData.get(i).draw(batch);
			if (powerUpData.get(i).getPowerUpType() == "Speed Up"){
				if (powerUpData.get(i).rangeCheck(playerShips) && powerUpData.get(i).dead){
					speedTimer = 600;
				}
			}else if(powerUpData.get(i).getPowerUpType() == "Invincible"){
				if (powerUpData.get(i).rangeCheck(playerShips) && powerUpData.get(i).dead){
					invincibilityTimer = 300;
				}
			}else if(powerUpData.get(i).getPowerUpType() == "Damage Increase"){
				if (powerUpData.get(i).rangeCheck(playerShips) && powerUpData.get(i).dead){
					damageTimer = 600;
				}
			}else if(powerUpData.get(i).getPowerUpType() == "Coin Multiplier"){
				if (powerUpData.get(i).rangeCheck(playerShips) && powerUpData.get(i).dead){
					coinTimer = 900;
				}
			}else if(powerUpData.get(i).getPowerUpType() == "Point Multiplier"){
				if (powerUpData.get(i).rangeCheck(playerShips) && powerUpData.get(i).dead){
					pointTimer = 900;
				}
			}
			if (powerUpData.get(i).rangeCheck(playerShips) && !powerUpData.get(i).dead) {
				powerUpData.get(i).death();
			}
		}

		for (int i = 0; i < stoneData.size(); i++){
			stoneData.get(i).draw(batch);
		}

		//renderer.render(world, camera.combined.scl(PIXEL_PER_METER));
		if(bob.dead == false) {
			bob.update(deltaTime, batch, playerShips, world);
		}
		/** update for the explosion*/
		updateExplosions();


		/** change of ui incase of victory or death or normal hud*/
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

	/** updating the explosions*/
	private void updateExplosions() {
		ArrayList<ExplosionController> removeExplosion = new ArrayList<ExplosionController>();
		for(ExplosionController explosion : Explosions){
			explosion.update();

			explosion.draw(batch);
			if(explosion.remove)removeExplosion.add(explosion);
		}
		Explosions.removeAll(removeExplosion);
	}

	public void update() {
		world.step(1 / 60f, 6, 2);
		if (speedTimer != 0) {
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
			playerShips.setCoinMulti(coinMul);
			coinTimer -= 1f;
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
		processInput();
		handleDirft();
		//tmr.setView(camera);
		maprender.setView(camera);
		batch.setProjectionMatrix(camera.combined);
		playerShips.updateShots(world, cannonBall, camera, Configuration.Cat_Player, (short)(Configuration.Cat_Enemy | Configuration.Cat_College), (short) 0);
	}

	public void inputUpdate() {
		/** checking for inputs*/
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
		/** creating zooming */
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
			if(camera.zoom < 2)camera.zoom += 0.02f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
			if(camera.zoom > 1)camera.zoom -= 0.02f;
		}

	}

	private void processInput() {
		/** processing the input created*/
		if (playerShips.dead) return;
		Vector2 baseVector = new Vector2(0, 0);

		/** apllying liner velocity to the player based on input*/
		float turnPercentage = 0;
		float speed = playerShips.getMaximumSpeed();
		if (speedTimer != 0) {
			speed = playerShips.getMaximumBoostedSpeed();
		}

		if (playerShips.getEntityBody().getLinearVelocity().len() < (speed / (2))) {
			turnPercentage = playerShips.getEntityBody().getLinearVelocity().len() / (speed);
		} else {
			turnPercentage = 1;
		}


		float currentTurnSpeed = playerShips.getTurnSpeed() * turnPercentage;




		/** applying angular velocity to the player based on input*/
		if (playerShips.getTurnDirection() == 1) {
			playerShips.getEntityBody().setAngularVelocity(-currentTurnSpeed);
		} else if (playerShips.getTurnDirection() == 2) {
			playerShips.getEntityBody().setAngularVelocity(currentTurnSpeed);
		} else if (playerShips.getTurnDirection() == 0 && playerShips.getEntityBody().getAngularVelocity() != 0) {
			playerShips.getEntityBody().setAngularVelocity(0);
		}

		/** applies speed to the player based on input*/
		if (playerShips.getDriveDirection() == 1) {
			baseVector.set(0, playerShips.getSpeed());
		} else if (playerShips.getDriveDirection() == 2) {
			baseVector.set(0, -playerShips.getSpeed() * 4 / 5);
		}
		if (playerShips.getEntityBody().getLinearVelocity().len() > 0 && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			playerShips.getEntityBody().setLinearDamping(1.75f);
		} else {
			playerShips.getEntityBody().setLinearDamping(0.5f);
		}
		//recordedSpeed = playerShips.getEntityBody().getLinearVelocity().len();
		if (playerShips.getEntityBody().getLinearVelocity().len() > speed/ 3f) {
			playerShips.setSpeed(playerShips.getOriginalSpeed() * 2);
		} else {
			playerShips.setSpeed(playerShips.getOriginalSpeed());
		}
		if (!baseVector.isZero() && (playerShips.getEntityBody().getLinearVelocity().len() < speed)) {
			playerShips.getEntityBody().applyForceToCenter(playerShips.getEntityBody().getWorldVector(baseVector), true);
		}
	}

	private void handleDirft() {
		/** handles drifts of the boat */
		Vector2 forwardSpeed = playerShips.getForwardVelocity();
		Vector2 lateralSpeed = playerShips.getLateralVelocity();

		playerShips.getEntityBody().setLinearVelocity(forwardSpeed.x + lateralSpeed.x * playerShips.getDriftFactor(), forwardSpeed.y + lateralSpeed.y * playerShips.getDriftFactor());
	}

	@Override
	public void resize(int width, int height) {
		/** resize function*/
		camera.setToOrtho(false, width / 2, height / 2);
		//viewport.update(width,height, true);
		//batch.setProjectionMatrix(camera.combined);
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

	@Override
	public void dispose() {
		/** disposing of everything*/
		renderer.dispose();
		world.dispose();
		//tmr.dispose();
		maprender.dispose();
		batch.dispose();
		map.dispose();
	}

	public void updateCamera() {
		/** updating camera based on states*/
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

	public Body createEnemy( boolean isStatic, Vector2 position) {/** creation of the body for the enemy*/
		Body body;
		BodyDef def = new BodyDef();

		if (isStatic) def.type = BodyDef.BodyType.StaticBody;
		else def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(position);

		def.fixedRotation = true;
		body = world.createBody(def);


		return body;
	}
	public TiledMap getMap() {
		return map;
	}

	public World getWorld(){
		return world;
	}
	public static void add(Vector2 pp){
		Explosions.add(new ExplosionController(pp));
	}

	public static void collegeKilled(){
		/** adds for each college killed to count for victory*/
		collegesKilled ++;
		collegesCaptured--;
	}
	/** adds college captured to check for victory*/
	public static void collegeCaptured(){collegesCaptured ++;}
}