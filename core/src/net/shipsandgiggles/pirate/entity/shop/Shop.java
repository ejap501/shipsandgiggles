package net.shipsandgiggles.pirate.entity.shop;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.EntityType;

/**
 * Shop data that allows access to a shop window when within a given range
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
public abstract class Shop extends Entity {
    // Main data store
    public Body body;
    private final Type type;
    public static Rectangle hitBox;

    // Timers
    public float timer = 0f;

    /**
     * Instantiates the shop type
     *
     * @param uuid : The unique id of the object
     * @param type : The type of college
     * @param texture : Image used for the object
     * @param location : Position of the object in the world
     * @param maximumHealth : Maximum health of the entity
     * @param height : Height of the college
     * @param width : Width of the college
     * */
    public Shop(UUID uuid, Type type, Sprite texture, Location location, float maximumHealth, float height, float width) {
        super(uuid, texture, location, EntityType.SHOP, maximumHealth, height, width);
        this.type = type;
    }

    /** Fetches the type of shop */
    public Type getType() {
        return type;
    }

    /** Fetches the body of the shop */
    public Body getBody(){
        return this.body;
    }

    /** Shop type  - allows us to keep track. */
    public enum Type {
        SHOP;
        private final UUID randomId;

        /** Assign static value at runtime, as value will not change and maximum of 1 shop. */
        Type() {
            this.randomId = UUID.randomUUID();
        }

        /**
         * Retrieves the unique id for the college object
         *
         * @return Unique identifier associated with this UUID.
         */
        public UUID getId() {
            return randomId;
        }
    }
}
