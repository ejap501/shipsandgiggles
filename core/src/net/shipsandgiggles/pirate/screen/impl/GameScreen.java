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
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.CameraManager;
import net.shipsandgiggles.pirate.TiledObjectUtil;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.EntityAi;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.ballsManager;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;


// To Do:
// 2- upgrade Ai?
// 3- create proper map and ship models


public class GameScreen implements Screen {

	//implement world
	public static World world;
	private final int _height = Gdx.graphics.getHeight();
	private final int _width = Gdx.graphics.getWidth();
	//private Viewport viewport;
	//objects
	private final Ship playerShips;
	//camera work
	private final OrthographicCamera camera;
	private final float Scale = 2;
	//graphics
	private final SpriteBatch batch; //batch of images "objects"
	public Sprite playerModel;
	//private Texture background; changed to colour of "deep water"
	//private Body[] islands;
	//private Texture[] islandsTextures;
	private final Texture[] boats;
	private final Box2DDebugRenderer renderer;
	private final OrthoCachedTiledMapRenderer tmr;
	private final TiledMap map;
	float recordedSpeed = 0;
	int cameraState = 0;

	EntityAi bob, player;

	Sprite cannonBall;
	//private Ship enemyShips;


	public GameScreen() {
		//for (int x = 0; x < islands.length; x++) islands[x] = createBox(22, 34, true, new Vector2(29,39));
		//islands = new Body[1];
		//islandsTextures = new Texture[1];
		//islandsTextures[0] = new Texture(Gdx.files.internal("models/island1.png"));

		renderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), false);
		boats = new Texture[3];
		boats[0] = new Texture(Gdx.files.internal("models/ship1.png"));
		boats[1] = new Texture(Gdx.files.internal("models/ship2.png"));
		boats[2] = new Texture(Gdx.files.internal("models/ship3.png"));

		cannonBall = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, _width / Scale, _height / Scale);
		//viewport = new StretchViewport(_width, _height, camera);
		batch = new SpriteBatch();



		//objects setup

		//int random = (int) Math.floor((Math.random() * 2.99f)); //generate random boat

		playerModel = new Sprite(new Texture(Gdx.files.internal("models/ship1.png")));

		playerShips = new Ship(playerModel, 40000f, 80f, 0.3f, 2f, new Location(_width / 2f, _height / 4f), playerModel.getHeight(), playerModel.getWidth());


		playerShips.setTexture(playerModel);

		//islands[0] = createBox(islandsTextures[0].getWidth(), islandsTextures[0].getHeight(), true , new Vector2(300,300));
		//enemyShips = new Ship(boats[random], 10, _width / 2, _height* 3/ 4, 20, 40);
		map = new TmxMapLoader().load("models/map.tmx");
		tmr = new OrthoCachedTiledMapRenderer(map);



		TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("collider").getObjects());

		playerShips.getEntityBody().setLinearDamping(0.5f);

		Sprite bobsSprite = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));

		Body body = createEnemy((int)bobsSprite.getWidth(), (int)bobsSprite.getHeight(), false, new Vector2(_width / 3f, _height / 6f));
		bob = new EntityAi(body, 300f, bobsSprite);
		bob.setTarget(playerShips.getEntityBody());

		player = new EntityAi(playerShips.getEntityBody(), 3);
		Steerable<Vector2> pp = player;


		Arrive<Vector2> arrives = new Arrive<Vector2>(bob, pp)
				.setTimeToTarget(0.01f)
				.setArrivalTolerance(175f)
				.setDecelerationRadius(50);
		bob.setBehavior(arrives);
	}


	@Override
	public void show() {

	}

	@Override
	public void render(float deltaTime) { //yay c# less goooooo (i changed it to deltaTime cuz im used to it being that from c#)
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(.1f, .36f, .70f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		tmr.render();

		playerShips.getSprite().setPosition(playerShips.getEntityBody().getPosition().x * PIXEL_PER_METER - (playerShips.getSkin().getWidth() / 2f), playerShips.getEntityBody().getPosition().y * PIXEL_PER_METER - (playerShips.getSkin().getHeight() / 2f));
		playerShips.getSprite().setRotation((float) Math.toDegrees(playerShips.getEntityBody().getAngle()));

		batch.begin();

		playerShips.getSprite().draw(batch);

		//player
		//batch.draw(playerShips.getSkin(), playerShips.getEntityBody().getPosition().x * PIXEL_PER_METER - (playerShips.getSkin().getWidth() / 2f), playerShips.getEntityBody().getPosition().y * PIXEL_PER_METER - (playerShips.getSkin().getHeight() / 2f));
		//batch.draw(islandsTextures[0], islands[0].getPosition().x * PixelPerMeter - (islandsTextures[0].getWidth()/2), islands[0].getPosition().y * PixelPerMeter - (islandsTextures[0].getHeight()/2));
		//enemyShips.draw(batch);

		batch.end();

		renderer.render(world, camera.combined.scl(PIXEL_PER_METER));
		bob.update(deltaTime, batch);
	}

	public void update(float deltaTime) {
		world.step(1 / 60f, 6, 2);
		updateCamera();
		inputUpdate(deltaTime);
		processInput();
		handleDirft();
		tmr.setView(camera);
		batch.setProjectionMatrix(camera.combined);
		ballsManager.updateBalls();
	}

	public void inputUpdate(float deltaTime) {
		//int xForce = 0;
		//int yForce = 0;

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

		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			System.out.println("current ship position is x = " + playerShips.getEntityBody().getPosition().x + " and y = " + playerShips.getEntityBody().getPosition().y);
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

		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
			playerShips.shoot(world, cannonBall, camera, Configuration.Cat_Player, Configuration.Cat_Enemy, (short) 0);
		}


	}

	private void processInput() {
		Vector2 baseVector = new Vector2(0, 0);
		//System.out.println(playerShips.getEntityBody().getAngle());

		float turnPercentage = 0;
		if (playerShips.getEntityBody().getLinearVelocity().len() < (playerShips.getMaximumSpeed() / 2)) {
			turnPercentage = playerShips.getEntityBody().getLinearVelocity().len() / (playerShips.getMaximumSpeed());
		} else {
			turnPercentage = 1;
		}

		float currentTurnSpeed = playerShips.getTurnSpeed() * turnPercentage;


		if (playerShips.getTurnDirection() == 1) {
			playerShips.getEntityBody().setAngularVelocity(-currentTurnSpeed);
		} else if (playerShips.getTurnDirection() == 2) {
			playerShips.getEntityBody().setAngularVelocity(currentTurnSpeed);
		} else if (playerShips.getTurnDirection() == 0 && playerShips.getEntityBody().getAngularVelocity() != 0) {
			playerShips.getEntityBody().setAngularVelocity(0);
		}

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
		if (playerShips.getEntityBody().getLinearVelocity().len() > playerShips.getMaximumSpeed() / 3f) {
			playerShips.setSpeed(playerShips.getOriginalSpeed() * 2);
		} else {
			playerShips.setSpeed(playerShips.getOriginalSpeed());
		}
		if (!baseVector.isZero() && (playerShips.getEntityBody().getLinearVelocity().len() < playerShips.getMaximumSpeed())) {
			playerShips.getEntityBody().applyForceToCenter(playerShips.getEntityBody().getWorldVector(baseVector), true);
		}
	}

	private void handleDirft() {
		Vector2 forwardSpeed = playerShips.getForwardVelocity();
		Vector2 lateralSpeed = playerShips.getLateralVelocity();

		playerShips.getEntityBody().setLinearVelocity(forwardSpeed.x + lateralSpeed.x * playerShips.getDriftFactor(), forwardSpeed.y + lateralSpeed.y * playerShips.getDriftFactor());
	}

	@Override
	public void resize(int width, int height) {
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
		renderer.dispose();
		world.dispose();
		tmr.dispose();
		batch.dispose();
		map.dispose();
	}

	public void updateCamera() {
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

	public Body createEnemy(int width, int height, boolean isStatic, Vector2 position) {
		System.out.println("kk");
		Body body;
		BodyDef def = new BodyDef();

		if (isStatic) def.type = BodyDef.BodyType.StaticBody;
		else def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(position);

		def.fixedRotation = true;
		body = world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((width / 2f) / PIXEL_PER_METER, (height / 2f) / PIXEL_PER_METER);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef. density = 1f;
		fixtureDef.filter.categoryBits = Configuration.Cat_Enemy;
		body.createFixture(fixtureDef);
		shape.dispose();

		return body;
	}
}