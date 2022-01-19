package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.shipsandgiggles.pirate.entity.Ship;


public class GameScreen implements Screen {

	//camera work
	private OrthographicCamera camera;
	private Viewport viewport;

	//graphics
	private SpriteBatch batch; //batch of images "objects"
	//private Texture background; changed to colour of "deep water"
	private Texture[] islands;
	private Texture[] boats;

	//implement world size?
	private int _height = 256;
	private int _width = 256;

	//objects
	private Ship playerShips;



	public GameScreen(){
		boats = new Texture[3];
		boats[0] = new Texture(Gdx.files.internal("models/ship1.png"));
		boats[1] = new Texture(Gdx.files.internal("models/ship2.png"));
		boats[2] = new Texture(Gdx.files.internal("models/ship3.png"));
		camera = new OrthographicCamera();
		viewport = new StretchViewport(_width, _height, camera);
		batch = new SpriteBatch();

		//objects setup
		int random = (int) Math.floor((Math.random() * 2.99f)); //generate random boat
		playerShips = new Ship(boats[random], 10, viewport.getWorldHeight()/ 2, viewport.getWorldWidth()/ 2, 50, 100);
	}


	@Override
	public void show() {

	}

	@Override
	public void render(float deltaTime) { //yay c# less goooooo (i changed it to deltaTime cuz im used to it being that from c#)
		Gdx.gl.glClearColor(.1f,.36f,.70f,1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);




		batch.begin();
		//player
		playerShips.draw(batch);

		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width,height, true);
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

	}
}