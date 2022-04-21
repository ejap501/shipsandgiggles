package net.shipsandgiggles.pirate;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import net.shipsandgiggles.pirate.screen.ScreenType;

/**
 * Pirate Game
 * Creation of an instance of the game
 *
 * @author Team 23
 * @version 1.0
 */
public class PirateGame extends Game {
	// Main data store
	private static PirateGame INSTANCE;
	public static PirateGame get() {
		return INSTANCE;
	}

	/** Constructs the game */
	@Override
	public void create() {
		INSTANCE = this;
		this.setScreen(ScreenType.LOADING.create());
	}

	/** Disposes of game data */
	@Override
	public void dispose() {
		super.dispose();
		INSTANCE = null;
	}

	/** Changes displayed screen */
	public void changeScreen(ScreenType screenType) {
		Screen screen = screenType.create();
		this.setScreen(screen);
	}
}