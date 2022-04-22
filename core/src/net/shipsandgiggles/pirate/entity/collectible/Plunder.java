package net.shipsandgiggles.pirate.entity.collectible;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.EntityType;


/**
 * Plunder
 * Class to configure collectible data that allows us to set basic info for collectibles.
 * New based on existing layout from other class
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Edward Poulter
 * @version 1.0
 */
public abstract class Plunder extends Entity {
    // Main data store
    public Body body;
    public Rectangle hitBox;
    private final Plunder.Type type;

    // State of existence
    public boolean dead = false;

    /**
     * Instantiates the plunder type
     *
     * @param uuid : The unique id of the object
     * @param type : The type of plunder
     * @param texture : Image used for the object
     * @param location : Location of the object in the world
     * @param height : Height of the plunder
     * @param width : Width of the plunder
     * */
    public Plunder(UUID uuid, Plunder.Type type, Sprite texture, Location location, float height, float width) {
        super(uuid, texture, location, EntityType.PLUNDER, 1, height, width);
        this.type = type;
    }

    /** Fetches the type of plunder */
    public Type getType() {
        return type;
    }

    /** Fetches the body of the plunder collectible */
    public Body getBody(){
        return this.body;
    }

    /** Kills the collectible body */
    public void death() {
        // Checks plunder destruction
        if(this.dead) return;
        this.dead = true;
    }

    /** Determines the type of collectible - allows us to keep track. */
    public enum Type {
        COINS;
        private final UUID randomId;
        /** Assign static value at runtime, as value will not change and maximum of 1 college. */
        Type() {
            this.randomId = UUID.randomUUID();
        }

        /**
         * Retrieves the unique id for the plunder object
         *
         * @return Unique identifier associated with this UUID.
         */
        public UUID getId() {
            return randomId;
        }
    }

    public abstract boolean perform();
}
