package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import net.shipsandgiggles.pirate.PirateGame;
import net.shipsandgiggles.pirate.screen.ScreenType;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.pref.GamePreferences;

/**
 * Preference Screen
 * Preferences in the main menu
 *
 * @author Team 23
 * @version 1.0
 */
public class PreferenceScreen implements Screen {
	// Main data store
	private Stage stage;
	public static Table table;

	/** Displays the preference screen */
	@Override
	public void show() {
		// Construct table
		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(this.stage);
		createParts();
		this.stage.addActor(table);


	}

	/**
	 * Renders the preference screen to the world
	 *
	 * @param deltaTime : Delta time (elapsed time since last game tick)
	 */
	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(165f / 255f, 220f / 255f, 236f / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		LoadingScreen.soundController.update();
		this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		this.stage.draw();
	}

	public static void createParts(){
		table = new Table();
		table.setFillParent(true);
		GamePreferences gamePreferences = GamePreferences.get();

		// Sets labels
		Label preferencesLabel = new Label("Game Preferences", Configuration.SKIN, "big");
		preferencesLabel.setAlignment(Align.center);

		// Music Volume Settings
		Slider VolumeSlider = new Slider(0f, 1f, 0.1f, false, Configuration.SKIN);
		VolumeSlider.setValue(gamePreferences.getVolumeLevel());
		VolumeSlider.addListener(event -> {
			gamePreferences.setVolumeLevel(VolumeSlider.getValue());
			return true;
		});

		Label VolumeLabel = new Label("Volume", Configuration.SKIN);

		Slider musicVolumeSlider = new Slider(0f, 1f, 0.1f, false, Configuration.SKIN);
		musicVolumeSlider.setValue(gamePreferences.getVolumeLevel());
		musicVolumeSlider.addListener(event -> {
			gamePreferences.setMusicVolumeLevel(musicVolumeSlider.getValue());
			return true;
		});

		Label musicVolumeLabel = new Label("Music Volume", Configuration.SKIN);

		// Music Enabled Settings
		CheckBox musicEnabled = new CheckBox(null, Configuration.SKIN);
		musicEnabled.setChecked(gamePreferences.isMusicEnabled());
		musicEnabled.addListener(event -> {

			boolean enabled = musicEnabled.isChecked();
			gamePreferences.setMusicEnabled(enabled);
			return true;
		});

		Label musicEnabledLabel = new Label("Music Enabled", Configuration.SKIN);

		// Volume Enabled Settings
		CheckBox volumeEnabled = new CheckBox(null, Configuration.SKIN);
		volumeEnabled.setChecked(gamePreferences.isVolumeEnabled());
		volumeEnabled.addListener(event -> {
			boolean enabled = volumeEnabled.isChecked();
			gamePreferences.setVolumeEnabled(enabled);
			return true;
		});

		Label volumeLabel = new Label("Volume Enabled", Configuration.SKIN);

		// The extra argument here "small" is used to set the button to the smaller version instead of the big default version
		TextButton backButton = new TextButton("Back", Configuration.SKIN);
		backButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						LoadingScreen.soundController.pauseAll();
						PirateGame.get().changeScreen(ScreenType.LOADING);
					}
				});

		// Creates a uniform X/Y table.
		table.add(preferencesLabel);
		table.row();
		table.add(Configuration.SPACER_LABEL);
		table.row();
		table.add(VolumeLabel);
		table.add(VolumeSlider);
		table.row();
		table.add(musicVolumeLabel);
		table.add(musicVolumeSlider);
		table.row();
		table.add(musicEnabledLabel);
		table.add(musicEnabled);
		table.row();
		table.add(volumeLabel);
		table.add(volumeEnabled);
		table.row();
		table.add(Configuration.SPACER_LABEL);
		table.row();
		table.add(Configuration.SPACER_LABEL);
		table.row();
		table.add(backButton);

	}

	/**
	 * Resizes the preference screen to fit the viewport
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