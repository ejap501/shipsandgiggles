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
 * New based on existing layout from other class
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
	public static int speedCost = 25;
	public static int multiCost = 35;
	public static int healthCost = 55;
	public static int cooldownCost = 45;

	public static int speedTier = 1;
	public static int multiTier = 1;
	public static int healthTier = 1;
	public static int cooldownTier = 1;

	public static Label coinAmount = new Label(Currency.get().balance(Currency.Type.GOLD) + "coins",Configuration.SKIN,"big");
	public static Label health = new Label(healthCost + "coins",Configuration.SKIN,"big");
	public static Label multi = new Label(multiCost + "coins",Configuration.SKIN,"big");
	public static Label speed = new Label(speedCost + "coins",Configuration.SKIN,"big");
	public static Label cooldown = new Label(cooldownCost + "coins",Configuration.SKIN,"big");

	public static TextButton coolDownButton = new TextButton("Cooldown - 0.25 - Tier " + cooldownTier, Configuration.SKIN);
	public static TextButton speedButton = new TextButton("Speed x 1.25 - Tier " + speedTier, Configuration.SKIN);
	public static TextButton multiButton = new TextButton("Multiplier + 1 - Tier " + multiTier, Configuration.SKIN);
	public static TextButton backButton = new TextButton("Back", Configuration.SKIN);
	public static TextButton healthButton = new TextButton("Health + 20 - Tier " + healthTier, Configuration.SKIN);


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


		// Constructs buttons listeners
		backButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						LoadingScreen.soundController.playButtonPress();
						PirateGame.get().changeScreen(ScreenType.GAME);
					}});


		healthButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						LoadingScreen.soundController.playButtonPress();
						healthListener();
					}});


		multiButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						LoadingScreen.soundController.playButtonPress();
						multiplierListener();
					}});


		speedButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						LoadingScreen.soundController.playButtonPress();
						speedListener();
					}});

		coolDownButton.addListener(
				new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						LoadingScreen.soundController.playButtonPress();
						coolDownListener();
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

		Back.add(backButton).pad(0, 0, 0, 100);
		Back.add(coinAmount);
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

	public static void coolDownListener(){
		if (Currency.get().balance(Currency.Type.GOLD) >= cooldownCost) {
			Currency.get().take(Currency.Type.GOLD, cooldownCost);
			Ship.burstCoolDown -= 0.25;
			cooldownCost +=  50;
			cooldown.setText(cooldownCost + "coins");
			coinAmount.setText(Currency.get().balance(Currency.Type.GOLD) + "coins");
			cooldownTier +=1 ;
			coolDownButton.setText("Cooldown - 0.25 - Tier " + cooldownTier);
		}
	}
	public static void speedListener(){
		if (Currency.get().balance(Currency.Type.GOLD) >= speedCost) {
			Currency.get().take(Currency.Type.GOLD, speedCost);
			GameScreen.currentSpeed = GameScreen.maxSpeed * 1.25f;
			speedCost +=  50;
			speed.setText(speedCost + "coins");
			coinAmount.setText(Currency.get().balance(Currency.Type.GOLD) + "coins");
			speedTier +=1 ;
			speedButton.setText("Speed x 1.25 - Tier " + speedTier);
		}
	}

	public static void multiplierListener(){
		if (Currency.get().balance(Currency.Type.GOLD) >= multiCost) {
			Currency.get().take(Currency.Type.GOLD, multiCost);
			GameScreen.playerShips.setCoinMulti(1,false);
			multiCost +=  50;
			multi.setText(multiCost + "coins");
			coinAmount.setText(Currency.get().balance(Currency.Type.GOLD) + "coins");
			multiTier +=1 ;
			multiButton.setText("Multiplier + 1 - Tier " + multiTier);
		}
	}

	public static void healthListener(){
		if (Currency.get().balance(Currency.Type.GOLD) >= healthCost) {
			Currency.get().take(Currency.Type.GOLD, healthCost);
			Ship.maxHealth += 20;
			healthCost +=  50;
			health.setText(healthCost + "coins");
			coinAmount.setText(Currency.get().balance(Currency.Type.GOLD) + "coins");
			healthTier +=1 ;
			healthButton.setText("Health + 20 - Tier " + healthTier);
		}
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