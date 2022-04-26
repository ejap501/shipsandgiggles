package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.shipsandgiggles.pirate.PirateGame;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.screen.ScreenType;
import net.shipsandgiggles.pirate.task.ChangeScreenTask;

/**
 * Difficulty screen
 * Accessed to set difficulty
 * New based on existing layout from other class
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
public class DifficultyScreen implements Screen {
	// Main data store
	private Stage stage;
	private Table table;

	public static int difficulty = 0;
	public final Sprite background = new Sprite(new Texture(Gdx.files.internal("models/background.PNG")));
	private final SpriteBatch batch = new SpriteBatch();

	/** Displays the shop screen */
	@Override
	public void show() {
		// Construct table
		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(this.stage);
		this.table = new Table();
		this.table.setFillParent(true);
		this.stage.addActor(this.table);
		final Table Back = new Table();
		Back.setFillParent(true);
		stage.addActor(Back);

		// Constructs buttons
		TextButton backButton = new TextButton("Back", Configuration.SKIN);
		backButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						LoadingScreen.soundController.playButtonPress();
						PirateGame.get().changeScreen(ScreenType.LOADING);
					}});

		TextButton easyButton = new TextButton("Easy", Configuration.SKIN);
		easyButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						LoadingScreen.soundController.playButtonPress();
						difficulty = 1;
						PirateGame.get().changeScreen(ScreenType.INFORMATION);

					}});

		TextButton normalButton = new TextButton("Normal", Configuration.SKIN);
		normalButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						LoadingScreen.soundController.playButtonPress();
						difficulty = 2;
						PirateGame.get().changeScreen(ScreenType.INFORMATION);

					}});

		TextButton hardButton = new TextButton("Hard", Configuration.SKIN);
		hardButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						LoadingScreen.soundController.playButtonPress();
						difficulty = 3;
						PirateGame.get().changeScreen(ScreenType.INFORMATION);

						}
					});



		// Creates a uniform X/Y table.
		this.table.row().pad(10, 0, 10, 0);
		this.table.add(easyButton);
		this.table.row().pad(10, 0, 10, 0);
		this.table.add(normalButton);
		this.table.row().pad(10, 0, 10, 0);
		this.table.add(hardButton);
		this.table.row().pad(10, 0, 10, 0);

		Back.add(backButton);
		Back.top().left();
	}

	/**
	 * Renders the shop screen to the world
	 *
	 * @param deltaTime : Delta time (elapsed time since last game tick)
	 */
	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(165f / 255f, 220f / 255f, 236f / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);
		batch.end();
		this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		this.stage.draw();
	}

	/**
	 * Resizes the shop screen to fit the viewport
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