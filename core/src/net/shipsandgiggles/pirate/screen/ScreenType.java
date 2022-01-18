package net.shipsandgiggles.pirate.screen;

import com.badlogic.gdx.Screen;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import net.shipsandgiggles.pirate.screen.impl.LoadingScreen;
import net.shipsandgiggles.pirate.cache.Cache;
import net.shipsandgiggles.pirate.screen.impl.PreferenceScreen;

public enum ScreenType {

	LOADING(LoadingScreen.class),
	PREFERENCE(PreferenceScreen.class),
	GAME(GameScreen.class);

	private static final Cache<ScreenType, Screen> SCREEN_CACHE = new Cache<>(null);

	private final Class<? extends Screen> screen;

	ScreenType(Class<? extends Screen> screen) {
		this.screen = screen;
	}

	public Screen create() {
		return SCREEN_CACHE.find(this).orElseGet(() -> {
			try {
				Screen newScreen = this.screen.getConstructor().newInstance();
				SCREEN_CACHE.cache(this, newScreen);

				return newScreen;
			} catch (Exception e) {
				System.out.println("Unable to open screen: " + this);
				e.printStackTrace();
				return null;
			}
		});
	}
}
