package net.shipsandgiggles.pirate.entity.impl.shop;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.shop.Shop;
import net.shipsandgiggles.pirate.conf.Configuration;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * shop1
 * Class to configure data relevant to the Shop collectible.
 * New based on existing layout from other class
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
public class shop1 extends Shop {
    // World data
    public World world;

    /**
     * This is the class to control the Shop
     * Used for the construction of coin objects
     * New based on existing layout from other class
     *
     * @param texture : Sprite image data
     * @param location : Coordinate data for position in world
     * @param world : World data
     * */
    public shop1(Sprite texture, Location location, float maximumHealth, World world) {
        super(UUID.randomUUID(), Type.SHOP, texture, location, maximumHealth,texture.getHeight(), texture.getWidth() );

        // Instantiating a body
        Body body;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(location.getX(), location.getY());

        // Creation of the body
        def.fixedRotation = true;
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((texture.getWidth() / 2f) / PIXEL_PER_METER, (texture.getHeight() / 2f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef. density = 1f;
        fixtureDef.filter.categoryBits = Configuration.Cat_Shop; // Telling it what category it is
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        this.body = body;
        this.world = world;

        //Creation of a hitbox detector
        this.hitBox = new com.badlogic.gdx.math.Rectangle((int)location.getX() - 200 ,(int)location.getY() -200 , 400,400);
    }

    /**
     * Draws the shop object in the world
     * Data assigned to the batch
     *
     * @param batch : The batch of sprite data
     * */
    @Override
    public void draw(Batch batch) {
        // Draws shop
        this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f)); /**sets position of the college */
        this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));
        batch.begin();
        this.getSkin().draw(batch);
        batch.end();
    }

    /**
     * Checks if player is in range to access the shop
     *
     * @param player : The player body
     * */
    public static void rangeCheck(Ship player){
        Ship.buyMenuRange = player.hitBox.overlaps(shop1.hitBox);
    }

    @Override
    public void shootPlayer(Ship player) {

    }

    @Override
    public void death() {

    }
}
