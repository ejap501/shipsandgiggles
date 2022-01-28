package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.shipsandgiggles.pirate.PirateGame;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.pref.GamePreferences;
import net.shipsandgiggles.pirate.screen.ScreenType;
import net.shipsandgiggles.pirate.task.ChangeScreenTask;

public class InformationScreen implements Screen {

	private Stage stage;
	private Table table;

	@Override
	public void show() {
		this.table = new Table();

		this.table.setFillParent(true);
		//this.table.setDebug(true);

		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(this.stage);
		this.stage.addActor(this.table);

		Label informationLabel = new Label("INFORMATION!", Configuration.SKIN, "title");
		informationLabel.setAlignment(Align.center);

		Label keysInformation = new Label("Use your arrow keys to move around.", Configuration.SKIN, "big");
		keysInformation.setAlignment(Align.center);

		Label shootingInformation = new Label("There are two methods of shooting, burst and singular.", Configuration.SKIN, "big");
		shootingInformation.setAlignment(Align.center);

		Label shootingInformationTwo = new Label("Click where you want to shoot!", Configuration.SKIN, "big");
		shootingInformationTwo.setAlignment(Align.center);

		Label burstShoot = new Label("Right-Click to burst shoot!", Configuration.SKIN, "big");
		burstShoot.setAlignment(Align.center);

		Label singularShoot = new Label("Left-Click to singular shoot!", Configuration.SKIN, "big");
		singularShoot.setAlignment(Align.center);

		// Creates a uniform X/Y table.
		this.table.add(informationLabel);
		this.table.row();
		this.table.add(Configuration.SPACER_LABEL);
		this.table.row();
		this.table.add(keysInformation);
		this.table.row();
		this.table.add(Configuration.SPACER_LABEL);
		this.table.row();
		this.table.add(shootingInformation);
		this.table.row();
		this.table.add(shootingInformationTwo);
		this.table.row();
		this.table.add(Configuration.SPACER_LABEL);
		this.table.row();
		this.table.add(burstShoot);
		this.table.row();
		this.table.add(singularShoot);
		this.table.row();

		Timer.schedule(new ChangeScreenTask(ScreenType.GAME), 5);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(165f / 255f, 220f / 255f, 236f / 255f, 1);
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
		this.stage.getRoot().getColor().a = 0;
		this.stage.getRoot().addAction(Actions.fadeOut(1));
	}

	@Override
	public void dispose() {
		this.stage.dispose();
	}
}