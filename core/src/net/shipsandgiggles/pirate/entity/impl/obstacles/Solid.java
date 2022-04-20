package net.shipsandgiggles.pirate.entity.impl.obstacles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.EntityType;

/**
 * Solid obstacles that provide collision for the player
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Edward Poulter
 * @version 1.0
 */
public abstract class Solid extends Entity {
    // Main data store
    public Body body;
    public boolean dead = false;
    public Rectangle hitBox;
    private final Solid.Type type;

    /**
     * Instantiates the solid type
     *
     * @param uuid : The unique id of the object
     * @param type : The type of plunder
     * @param texture : Image used for the object
     * @param location : Location of the object in the world
     * @param height : Height of the plunder
     * @param width : Width of the plunder
     * */
    public Solid(UUID uuid, Solid.Type type, Sprite texture, Location location, float height, float width) {
        super(uuid, texture, location, EntityType.OBSTACLE, 1, height, width);
        this.type = type;
    }

    /** Fetches the type of solid */
    public Solid.Type getType() {
        return type;
    }

    /** Fetches the body of the solid */
    public Body getBody(){
        return this.body;
    }

    /** Types of solid - allows us to keep track. */
    public enum Type {
        STONE;
        private final UUID randomId;

        /** Assign static value at runtime, as value will not change and maximum of 1 stone. */
        Type() {
            this.randomId = UUID.randomUUID();
        }

        /**
         * Retrieves the unique id for the solid object
         *
         * @return Unique identifier associated with this UUID.
         */
        public UUID getId() {
            return randomId;
        }
    }

    public abstract boolean perform();
}
