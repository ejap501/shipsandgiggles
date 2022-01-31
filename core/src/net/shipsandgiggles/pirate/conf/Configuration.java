package net.shipsandgiggles.pirate.conf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

public class Configuration {

	public static final Skin SKIN = new Skin(Gdx.files.internal("skin/comic-ui.json"));
	public static final float PIXEL_PER_METER = 1f;
	public static final short Cat_Player = 1;
	public static final short Cat_walls = 2;
	public static final short Cat_Enemy = 4;
	public static final short Cat_College = 8;
	public static final World world = GameScreen.world;
	public static final Label SPACER_LABEL = new Label(" ", Configuration.SKIN);

}