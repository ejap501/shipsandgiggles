package net.shipsandgiggles.pirate.entity.collectible;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.EntityType;
import net.shipsandgiggles.pirate.entity.Location;

import java.util.UUID;

/**
 * Collectible data that allows us to set basic info for collectibles.
 */
public abstract class Plunder extends Entity {
    public Body body;
    public boolean dead = false;
    public Rectangle hitBox;
    private final Plunder.Type type;

    public Plunder(UUID uuid, Plunder.Type type, Sprite texture, Location location, float maximumHealth, float height, float width) {
        super(uuid, texture, location, EntityType.PLUNDER, maximumHealth, height, width);

        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public abstract boolean perform();

    public Body getBody(){
        return this.body;
    }
    public void death() {/** checks coin destruction*/
        if(this.dead) return;
        this.dead = true;
    }

    /**
     * Types of collectible - allows us to keep track.
     */
    public enum Type {

        COINS;

        private final UUID randomId;

        /**
         * Assign static value at runtime, as value will not change and maximum of 1 college.
         **/
        Type() {
            this.randomId = UUID.randomUUID();
        }

        /**
         * @return Unique identifier associated with this UUID.
         */
        public UUID getId() {
            return randomId;
        }
    }
}
