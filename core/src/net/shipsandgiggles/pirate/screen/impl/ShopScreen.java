package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import net.shipsandgiggles.pirate.PirateGame;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.screen.ScreenType;
import net.shipsandgiggles.pirate.conf.Configuration;

/**
 * Shop screen
 * Accessed to purchase stuff from shop
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
public class ShopScreen implements Screen {
	// Main data store
	private Stage stage;
	private Table table;

	// Costs
	public int speedCost = 25;
	public int multiCost = 35;
	public int healthCost = 55;
	public int cooldownCost = 45;

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

		// Sets labels
		Label health = new Label(healthCost + "coins",Configuration.SKIN);
		Label multi = new Label(multiCost + "coins",Configuration.SKIN);
		Label speed = new Label(speedCost + "coins",Configuration.SKIN);
		Label cooldown = new Label(cooldownCost + "coins",Configuration.SKIN);

		// Constructs buttons
		TextButton backButton = new TextButton("Back", Configuration.SKIN);
		backButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						PirateGame.get().changeScreen(ScreenType.GAME);
					}});

		TextButton healthButton = new TextButton("Health + 20", Configuration.SKIN);
		healthButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						if (Currency.get().balance(Currency.Type.GOLD) >= healthCost) {
							Currency.get().take(Currency.Type.GOLD, healthCost);
							Ship.maxHealth += 20;
							healthCost +=  50;
							health.setText(healthCost + "coins");
						}
					}});

		TextButton multiButton = new TextButton("Multiplier + 1", Configuration.SKIN);
		multiButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						if (Currency.get().balance(Currency.Type.GOLD) >= multiCost) {
							Currency.get().take(Currency.Type.GOLD, multiCost);
							Ship.coinMulti += 1;
							multiCost +=  50;
							multi.setText(multiCost + "coins");
						}
					}});

		TextButton speedButton = new TextButton("Speed x 1.25", Configuration.SKIN);
		speedButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						if (Currency.get().balance(Currency.Type.GOLD) >= speedCost) {
							Currency.get().take(Currency.Type.GOLD, speedCost);
							Ship.maximumSpeed += 25f;
							speedCost +=  50;
							speed.setText(speedCost + "coins");
						}
					}});

		TextButton coolDownButton = new TextButton("Cooldown - 0.25", Configuration.SKIN);
		coolDownButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						if (Currency.get().balance(Currency.Type.GOLD) >= cooldownCost) {
							Currency.get().take(Currency.Type.GOLD, cooldownCost);
							Ship.burstCoolDown -= 0.25;
							cooldownCost +=  50;
							cooldown.setText(cooldownCost + "coins");
						}
					}});


		// Creates a uniform X/Y table.
		this.table.add(healthButton);
		this.table.add(health);
		this.table.row().pad(10, 0, 10, 0);
		this.table.add(multiButton);
		this.table.add(multi);
		this.table.row().pad(10, 0, 10, 0);
		this.table.add(speedButton);
		this.table.add(speed);
		this.table.row().pad(10, 0, 10, 0);
		this.table.add(coolDownButton);
		this.table.add(cooldown);
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