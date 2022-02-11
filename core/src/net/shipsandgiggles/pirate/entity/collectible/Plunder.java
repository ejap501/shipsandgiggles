package net.shipsandgiggles.pirate.entity.collectible;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.EntityType;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.college.College;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

import java.util.Random;
import java.util.UUID;


public abstract class Plunder extends Entity {
    public Body entityBody;
    private final Plunder.Type type;
    public Sprite texture;
    public boolean collected;

    public Plunder(UUID uuid, Plunder.Type type, Sprite texture, Location location, float maximumHealth, float height, float width) {
        super(uuid, texture, location, EntityType.PLUNDER, maximumHealth, height, width);
        this.type = type;
        this.collected = false;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(location.getX(), location.getY());
        this.entityBody = GameScreen.world.createBody(bodyDef);
        this.texture = texture;
    }

    public Type getType() {
        return type;
    }

    public Body getBody(){
        return this.entityBody;
    }

    public Sprite getTexture(){
        return this.texture;
    }
    public void setTexture(Sprite texture){
        this.texture = texture;
    }

    public void death(){
        if(this.collected) return;
        Random coins = new Random();
        this.collected = true;
        Currency.get().give(Currency.Type.GOLD, coins.nextInt(10) + 1);
    }

    public enum Type {

        COIN;

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
