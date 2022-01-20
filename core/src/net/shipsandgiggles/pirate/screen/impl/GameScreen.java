package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.shipsandgiggles.pirate.CameraManager;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.Ship;
import static net.shipsandgiggles.pirate.conf.Configuration.PixelPerMeter;

public class GameScreen implements Screen {

	//camera work
	private OrthographicCamera camera;
	int cameraState = 0;
	private float Scale = 2;
	//private Viewport viewport;

	//graphics
	private SpriteBatch batch; //batch of images "objects"
	//private Texture background; changed to colour of "deep water"
	private Body[] islands;
	private Texture[] boats;

	//implement world
	public static World world;
	private Box2DDebugRenderer renderer;
	private int _height = Gdx.graphics.getHeight();
	private int _width = Gdx.graphics.getWidth();

	//objects
	private Ship playerShips;
	private Ship enemyShips;



	public GameScreen(){
		//for (int x = 0; x < islands.length; x++) islands[x] = createBox(22, 34, true, new Vector2(29,39));
		renderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0,0), false);
		boats = new Texture[3];
		boats[0] = new Texture(Gdx.files.internal("models/ship1.png"));
		boats[1] = new Texture(Gdx.files.internal("models/ship2.png"));
		boats[2] = new Texture(Gdx.files.internal("models/ship3.png"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, _width/Scale, _height/Scale);
		//viewport = new StretchViewport(_width, _height, camera);
		batch = new SpriteBatch();

		//objects setup
		int random = (int) Math.floor((Math.random() * 2.99f)); //generate random boat
		playerShips = new Ship(boats[0], 100, _width / 2, _height/ 2,  boats[0].getWidth() ,  boats[0].getHeight());
		//enemyShips = new Ship(boats[random], 10, _width / 2, _height* 3/ 4, 20, 40);
	}


	@Override
	public void show() {

	}

	@Override
	public void render(float deltaTime) { //yay c# less goooooo (i changed it to deltaTime cuz im used to it being that from c#)
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(.1f,.36f,.70f,1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		renderer.render(world, camera.combined.scl(PixelPerMeter));

		batch.begin();
		//player
		batch.draw(playerShips.getBoatTexture(), playerShips.getEntityBody().getPosition().x * PixelPerMeter - (playerShips.getBoatTexture().getWidth()/2), playerShips.getEntityBody().getPosition().y * PixelPerMeter - (playerShips.getBoatTexture().getHeight()/2));
		//enemyShips.draw(batch);

		batch.end();


	}

	public void update(float deltaTime){
		world.step(1/ 60f, 6,2);
		updateCamera();
		inputUpdate(deltaTime);
		batch.setProjectionMatrix(camera.combined);
	}

	public void inputUpdate(float deltaTime){
		int xForce = 0;
		int yForce = 0;

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) | Gdx.input.isKeyPressed(Input.Keys.A)){
			xForce -= 1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) | Gdx.input.isKeyPressed(Input.Keys.D)){
			xForce += 1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP) | Gdx.input.isKeyPressed(Input.Keys.W)){
			yForce += 1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN) | Gdx.input.isKeyPressed(Input.Keys.S)){
			yForce -=1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.P)){
			System.out.println("current ship position is x = " + playerShips.getEntityBody().getPosition().x + " and y = " + playerShips.getEntityBody().getPosition().y);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.C)){
			if(cameraState == 0) cameraState++;
			else cameraState--;
		}

		playerShips.getEntityBody().setLinearVelocity(new Vector2(xForce * playerShips.getMovementSpeed(),yForce * playerShips.getMovementSpeed()));
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width/2 , height/2);
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
	}

	public void updateCamera(){
		if(cameraState == 0){
			CameraManager.lerpOn(camera, playerShips.getEntityBody().getPosition(), 0.1f);
		}
	}

	public Body createBox(int width, int height, boolean isStatic, Vector2 position){
		Body body;
		BodyDef def = new BodyDef();

		if(isStatic) def.type = BodyDef.BodyType.StaticBody;
		else def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(position);
		def.fixedRotation = true;
		body = GameScreen.world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((width/6) / PixelPerMeter, (height/6)/ PixelPerMeter);
		body.createFixture(shape, 1f);
		shape.dispose();

		return  body;
	}
}