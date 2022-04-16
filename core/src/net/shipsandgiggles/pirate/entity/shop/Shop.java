package net.shipsandgiggles.pirate.entity.shop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.EntityType;
import net.shipsandgiggles.pirate.entity.Location;

import java.util.UUID;

public abstract class Shop extends Entity {
    public Body body;
    public float counter = 0;
    public static Rectangle hitBox;
    private final Type type;
    public float timer = 0f;


    public Shop(UUID uuid, Type type, Sprite texture, Location location, float maximumHealth, float height, float width) {
        super(uuid, texture, location, EntityType.SHOP, maximumHealth, height, width);

        this.type = type;


    }
    public Type getType() {
        return type;
    }

    public Body getBody(){
        return this.body;
    }




    public enum Type {

        SHOP;

        private final UUID randomId;

        /**
         * Assign static value at runtime, as value will not change and maximum of 1 shop.
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
