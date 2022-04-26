package net.shipsandgiggles.pirate.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

import net.shipsandgiggles.pirate.entity.EntityAi;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.BallsManager;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * Enemy Ship
 * This is the class to control the enemy ship entities.
 * New based on existing layout from other class
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Edward Poulter
 * @version 1.0
 */
public class EnemyShip extends EntityAi {
    // World data
    public World world;

    /**
     * This is the class to control the construction of enemy ships
     *
     * @param texture : Sprite image data
     * @param location : Coordinate data for position in world
     * @param maximumHealth : Maximum health of the college, used for combat
     * @param world : World data
     * */
    public EnemyShip(Body body, Sprite texture, float boundingRadius, Location location, int maximumHealth, World world) {
        super(body,boundingRadius, texture,  maximumHealth, location,(int) texture.getWidth(), (int) texture.getHeight());

    }
}