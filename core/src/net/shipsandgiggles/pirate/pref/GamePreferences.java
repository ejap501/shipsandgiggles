package net.shipsandgiggles.pirate.pref;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Game Preferences
 * Determines saved option preferences.
 * Allows for the toggling of sound options
 *
 * @author Team 23
 * @version 1.0
 */
public class GamePreferences {
	// Sets naming conventions
	private static final String PREF_NAME = "ShipsAndGiggles";
	private static final String PREF_MUSIC_ENABLED = "music.enabled";
	private static final String PREF_VOLUME_ENABLED = "volume.enabled";
	private static final String PREF_VOLUME_LEVEL = "volume.level";
	private static final String PREF_MUSIC_LEVEL = "music.level";
	private static final String HAS_SAVE = "save.check";
	private static GamePreferences INSTANCE;

	/**
	 * Sets a new preference instance
	 */
	private GamePreferences() {
		if (INSTANCE != null) {
			throw new IllegalStateException("Cannot initialise a singleton class twice (GamePreferences)!");
		}
		INSTANCE = this;
	}

	/**
	 * Sets the selected preference options
	 *
	 * @return Game preferences
	 */
	public static GamePreferences get() {
		if (INSTANCE == null) {
			INSTANCE = new GamePreferences();
		}
		return INSTANCE;
	}

	public Preferences prefs() {
		return Gdx.app.getPreferences(PREF_NAME);
	}

	/**
	 * Check whether the music is enabled (i.e. background noise).
	 *
	 * @return State of music (true, enabled).
	 */
	public boolean isMusicEnabled() {
		return this.prefs().getBoolean(PREF_MUSIC_ENABLED, true);
	}

	/**
	 * Toggle the state of background music.
	 *
	 * @param enabled Whether to enable or disable it.
	 */
	public void setMusicEnabled(boolean enabled) {
		this.prefs().putBoolean(PREF_MUSIC_ENABLED, enabled);
		this.prefs().flush();
	}


	/**
	 * Check whether global volume is enabled.
	 *
	 * @return State of volume (true, enabled).
	 */
	public boolean isVolumeEnabled() {
		return this.prefs().getBoolean(PREF_VOLUME_ENABLED, true);
	}

	/**
	 * Toggle whether global volume is enabled.
	 *
	 * @param enabled Whether to enable or disable it.
	 */
	public void setVolumeEnabled(boolean enabled) {
		this.prefs().putBoolean(PREF_VOLUME_ENABLED, enabled);
		this.prefs().flush();
	}

	/**
	 * Get the volume level, where 1 is 100%, and 0 is 0%.
	 *
	 * @return Current volume level.
	 */
	public float getVolumeLevel() {
		return this.prefs().getFloat(PREF_VOLUME_LEVEL, 0.5F);
	}

	/**
	 * Set the volume level for all game noises.
	 *
	 * @param level Floating number to set it to (0 is off, 1 is full).
	 */
	public void setVolumeLevel(float level) {
		this.prefs().putFloat(PREF_VOLUME_LEVEL, level);
		this.prefs().flush();
	}

	/**
	 * Set the volume level for all game music.
	 *
	 * @param level Floating number to set it to (0 is off, 1 is full).
	 */
	public void setMusicVolumeLevel(float level) {
		this.prefs().putFloat(PREF_MUSIC_LEVEL, level);
		this.prefs().flush();
	}

	public void setHasSave(boolean hasSave){
		this.prefs().putBoolean(HAS_SAVE, hasSave);
		this.prefs().flush();

	}
	public boolean getHasSave(){
		return this.prefs().getBoolean(HAS_SAVE, false);

	}
}