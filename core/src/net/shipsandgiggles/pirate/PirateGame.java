package net.shipsandgiggles.pirate;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import net.shipsandgiggles.pirate.screen.ScreenType;

public class PirateGame extends Game {

	private static PirateGame INSTANCE;

	@Override
	public void create() {
		INSTANCE = this;

		this.setScreen(ScreenType.LOADING.create());
	}

	@Override
	public void dispose() {
		super.dispose();

		INSTANCE = null;
	}

	public void changeScreen(ScreenType screenType) {
		Screen screen = screenType.create();
		this.setScreen(screen);
	}

	public static PirateGame get() {
		return INSTANCE;
	}
}