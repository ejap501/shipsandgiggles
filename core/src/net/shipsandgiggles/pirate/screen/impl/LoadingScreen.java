package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.shipsandgiggles.pirate.PirateGame;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.screen.ScreenType;

public class LoadingScreen implements Screen {

	private Stage stage;
	private Table table;

	@Override
	public void show() {
		this.table = new Table();

		this.table.setFillParent(true);
		this.table.setDebug(true);

		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(this.stage);

		this.stage.addActor(this.table);

		// Initialise Buttons

		// New Game Button
		TextButton newGame = new TextButton("New Game", Configuration.SKIN);
		newGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				PirateGame.get().changeScreen(ScreenType.INFORMATION);
			}
		});

		// Preferences Button
		TextButton preferences = new TextButton("Preferences", Configuration.SKIN);
		preferences.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				PirateGame.get().changeScreen(ScreenType.PREFERENCE);
			}
		});

		// Exit Button
		TextButton exit = new TextButton("Exit", Configuration.SKIN);
		exit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});

		// Loading Screen Data

		// Creates a uniform X/Y table.
		table.add(newGame).fillX().uniformX();
		// Sets default gap between.
		table.row().pad(10, 0, 10, 0);
		table.add(preferences).fillX().uniformX();
		table.row();
		table.add(exit).fillX().uniformX();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		this.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		this.stage.getViewport().update(width, height, true);
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
		this.stage.dispose();
	}
}