package net.shipsandgiggles.pirate.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;
import java.util.UUID;

import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.EntityAi;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.conf.Configuration;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * Duck
 * Class to configure data relevant to the duck entity.
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Edward Poulter
 * @version 1.0
 */
public class Duck extends EntityAi {
    // World data
    public World world;
    public int deadDuck = 0;

    /**
     * This is the class to control the ducks
     * Used for the construction of duck entities
     * New based on existing layout from other class
     *
     * @param texture : Sprite image data
     * @param location : Coordinate data for position in world
     * @param world : World data
     * */
    public Duck(Body body ,Sprite texture, float boundingRadius, Location location, int maximumHealth, World world) {
        super(body, boundingRadius, texture, maximumHealth, location,(int) texture.getWidth(), (int) texture.getHeight());
        this.shooting = false;
    }

    /**
     * Kills the enemy
     *
     * @param world : World data
     */
    @Override
    public void death(World world){
        // Checks if dead
        if(this.dead) return;

        if (deadDuck == 2){
            // Kills enemy
            world.destroyBody(body);
            this.dead = true;
            return;
        }

        // Gives instant money if collected
        Currency.get().give(Currency.Type.GOLD, 0);
        if (!shooting){
            deadDuck = 1;
            Currency.get().give(Currency.Type.POINTS, 25);
        }else{
            deadDuck = 2;
            Currency.get().give(Currency.Type.POINTS, 10000);
        }


    }
}