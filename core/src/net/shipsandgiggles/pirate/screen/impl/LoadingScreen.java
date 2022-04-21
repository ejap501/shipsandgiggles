package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import net.shipsandgiggles.pirate.PirateGame;
import net.shipsandgiggles.pirate.SoundController;
import net.shipsandgiggles.pirate.screen.ScreenType;
import net.shipsandgiggles.pirate.conf.Configuration;

/**
 * Loading Screen
 * The main screen
 *
 * @author Team 23
 * @version 1.0
 */
public class LoadingScreen implements Screen {
	// Main data store
	private Stage stage;
	private Table table;
	public static SoundController soundController;
	private final SpriteBatch batch = new SpriteBatch();
	public Sprite background = new Sprite(new Texture(Gdx.files.internal("models/background.PNG")));

	/** Displays the loading screen */
	@Override
	public void show() {
		// Construct table
		this.soundController = new SoundController();
		this.table = new Table();
		this.table.setFillParent(true);
		this.table.setDebug(true);
		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(this.stage);
		this.stage.addActor(this.table);

		/*
		Initialise Buttons
		 */

		// New Game Button
		TextButton newGame = new TextButton("New Game", Configuration.SKIN);
		newGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LoadingScreen.soundController.playButtonPress();
				PirateGame.get().changeScreen(ScreenType.INFORMATION);
			}
		});

		// Preferences Button
		TextButton preferences = new TextButton("Preferences", Configuration.SKIN);
		preferences.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LoadingScreen.soundController.playButtonPress();
				PirateGame.get().changeScreen(ScreenType.PREFERENCE);
			}
		});

		// Exit Button
		TextButton exit = new TextButton("Exit", Configuration.SKIN);
		exit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				LoadingScreen.soundController.playButtonPress();
				Gdx.app.exit();
			}
		});

		/*
		  Loading Screen Data
		 */

		// Creates a uniform X/Y table.
		table.add(newGame).fillX().uniformX();

		// Sets default gap between.
		table.row().pad(10, 0, 10, 0);
		table.add(preferences).fillX().uniformX();
		table.row();
		table.add(exit).fillX().uniformX();
	}

	/**
	 * Renders the loading screen to the world
	 *
	 * @param deltaTime : Delta time (elapsed time since last game tick)
	 */
	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(.98f, .91f, .761f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);
		batch.end();
		this.soundController.update();
		this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		this.stage.draw();
	}

	/**
	 * Resizes the loading screen to fit the viewport
	 *
	 * @param width : Width of the screen
	 * @param height : Height of the screen
	 */
	@Override
	public void resize(int width, int height) {
		this.stage.getViewport().update(width, height, true);
	}

	/** Disposing of the screen data */
	@Override
	public void dispose() {
		this.stage.dispose();
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