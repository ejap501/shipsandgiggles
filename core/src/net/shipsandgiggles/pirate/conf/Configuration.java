package net.shipsandgiggles.pirate.conf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import net.shipsandgiggles.pirate.screen.impl.GameScreen;

/**
 * Configuration
 * Class to configure category identifiers and key values
 * Allows for the separation and management of objects
 *
 * @author Team 23
 * @author Team 22 : Edward Poulter, Sam Pearson
 * @version 2.0
 */
public class Configuration {
	// Global configs for variables commonly used
	public static final Skin SKIN = new Skin(Gdx.files.internal("skin/comic-ui.json"));
	public static final float PIXEL_PER_METER = 1f; /** ppi to scale down the world*/

	/**
	 * Categorised data
	 * Cats are just categories to tell the bodies what to interact with
	 * */
	public static final short Cat_Player = 1;
	public static final short Cat_walls = 2;
	public static final short Cat_Enemy = 4;
	public static final short Cat_College = 8;
	public static final short Cat_Collect = 16;
	public static final short Cat_Shop = 32;
	public static final short Cat_Weather = 64;
	public static final short Cat_Stone = 128;

	/** The world */
	public static final World world = GameScreen.world;

	/** Spacers for skin data */
	public static final Label SPACER_LABEL = new Label(" ", Configuration.SKIN);
}