package net.shipsandgiggles.pirate.entity.impl.obstacles;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.conf.Configuration;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * Stone
 * Class to configure data relevant to stone obstacles
 * New based on existing layout from other class
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Edward Poulter
 * @version 1.0
 */
public class Stone extends Solid{
    // World data
    public World world;

    /**
     * This is the class to control the stone obstacles
     * Used for the construction of stone objects
     *
     * @param texture : Sprite image data
     * @param location : Coordinate data for position in world
     * @param world : World data
     * */
    public Stone(Sprite texture, Location location, World world) {
        super(UUID.randomUUID(), Solid.Type.STONE, texture, location, texture.getHeight(), texture.getWidth());

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
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = Configuration.Cat_Collect; // Telling it what category it is
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
     * Draws the stone object in the world
     * Data assigned to the batch
     *
     * @param batch : The batch of sprite data
     * */
    @Override
    public void draw(Batch batch) {
        // Draws stone
        this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f)); /**sets position of the stone */
        this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));
        batch.begin();
        this.getSkin().draw(batch);
        batch.end();
    }

    @Override
    public void shootPlayer(Ship player) {

    }

    @Override
    public void death(){

    }
}
