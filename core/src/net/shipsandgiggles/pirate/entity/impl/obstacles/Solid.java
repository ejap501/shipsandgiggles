package net.shipsandgiggles.pirate.entity.impl.obstacles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.EntityType;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.collectible.Plunder;

import java.util.UUID;

public abstract class Solid extends Entity {
    public Body body;
    public boolean dead = false;
    public Rectangle hitBox;
    private final Solid.Type type;

    public Solid(UUID uuid, Solid.Type type, Sprite texture, Location location, float maximumHealth, float height, float width) {
        super(uuid, texture, location, EntityType.OBSTACLE, maximumHealth, height, width);

        this.type = type;
    }

    public Solid.Type getType() {
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
     * Types of solid - allows us to keep track.
     */
    public enum Type {

        STONE;

        private final UUID randomId;

        /**
         * Assign static value at runtime, as value will not change and maximum of 1 stone.
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
