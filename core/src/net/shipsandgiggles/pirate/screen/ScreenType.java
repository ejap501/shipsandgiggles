package net.shipsandgiggles.pirate.screen;

import com.badlogic.gdx.Screen;

import net.shipsandgiggles.pirate.PirateGame;
import net.shipsandgiggles.pirate.cache.Cache;
import net.shipsandgiggles.pirate.screen.impl.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Creation of types
 * Determines screen type
 *
 * @author Team 23
 * */
public enum ScreenType {
	// Sets screen types
	LOADING(LoadingScreen.class),
	GAME(GameScreen.class),
	PREFERENCE(PreferenceScreen.class),
	INFORMATION(InformationScreen.class),
	SHOP(ShopScreen.class),
	DIFFICULTY(DifficultyScreen.class);

	// Creation of different screen types
	private static final Cache<ScreenType, Screen> SCREEN_CACHE = new Cache<>(null);
	private final Class<? extends Screen> screen;
	ScreenType(Class<? extends Screen> screen) {
		this.screen = screen;
	}

	/**
	 * Creates a new screen
	 *
	 * @return The relevant type of screen
	 */
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
