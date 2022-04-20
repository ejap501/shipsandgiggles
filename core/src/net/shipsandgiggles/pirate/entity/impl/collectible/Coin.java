package net.shipsandgiggles.pirate.entity.impl.collectible;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.collectible.Plunder;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * Coin
 * Class to configure data relevant to the coin collectible.
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Edward Poulter
 * @version 1.0
 */
public class Coin extends Plunder {
    // World data
    public World world;

    /**
     * This is the class to control the coin
     * Used for the construction of coin objects
     *
     * @param texture : Sprite image data
     * @param location : Coordinate data for position in world
     * @param world : World data
     * */
    public Coin(Sprite texture, Location location, World world) {
        super(UUID.randomUUID(),Type.COINS, texture, location, texture.getHeight(), texture.getWidth());
        // Instantiating a body
        Body body;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(location.getX(), location.getY());

        // Creation of the body
        def.fixedRotation = true;
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((texture.getWidth() / 2f) / PIXEL_PER_METER, (texture.getHeight() / 2f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Configuration.Cat_Collect; /**telling it what category it is */
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        this.body = body;
        this.world = world;

        //Creation of a hitbox detector
        this.hitBox = new com.badlogic.gdx.math.Rectangle((int)location.getX() / PIXEL_PER_METER - 4,(int)location.getY() / PIXEL_PER_METER - 4 , ((texture.getWidth()) / PIXEL_PER_METER) + 8, ((texture.getHeight()) / PIXEL_PER_METER) + 8);
    }

    /** Sets animation to none */
    @Override
    public boolean perform() {
        return false;
    }

    /**
     * Draws the coin object in the world
     * Data assigned to the batch
     *
     * @param batch : The batch of sprite data
     * */
    @Override
    public void draw(Batch batch) {
        // Terminates if dead
        if(dead){return;}

        // Draws coin
        this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f)); /**sets position of the college */
        this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));
        batch.begin();
        this.getSkin().draw(batch);
        batch.end();
    }

    /** Oversees the death of the coin body */
    @Override
    public void death(){
        // Does nothing if already dead
        if(this.dead) return;

        // Gives money if collected
        Currency.get().give(Currency.Type.GOLD, 10);

        // Kills off the body
        world.destroyBody(body);
        this.dead = true;
    }

    /**
     * Checks for contact with player body
     *
     * @param player : The player body
     * */
    public boolean rangeCheck(Ship player){
        return player.hitBox.overlaps(this.hitBox);
    }

    @Override
    public void shootPlayer(Ship player) {

    }
}
