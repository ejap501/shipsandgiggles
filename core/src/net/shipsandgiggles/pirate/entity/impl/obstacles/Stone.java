package net.shipsandgiggles.pirate.entity.impl.obstacles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;

import java.util.UUID;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

public class Stone extends Solid{
    public World world;

    /** this is the class to control the stone obstacles*/



    /** construction of coins*/

    public Stone(Sprite texture, Location location, float maximumHealth, World world) {
        super(UUID.randomUUID(), Solid.Type.STONE, texture, location, maximumHealth, texture.getHeight(), texture.getWidth());

        Body body;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(location.getX(), location.getY());

        /** creation of the body*/

        def.fixedRotation = true;
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((texture.getWidth() / 2f) / PIXEL_PER_METER, (texture.getHeight() / 2f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = Configuration.Cat_Collect; /**telling it what category it is */
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        this.body = body;
        this.world = world;

        this.hitBox = new com.badlogic.gdx.math.Rectangle((int)location.getX() / PIXEL_PER_METER - 4,(int)location.getY() / PIXEL_PER_METER - 4 , ((texture.getWidth()) / PIXEL_PER_METER) + 8, ((texture.getHeight()) / PIXEL_PER_METER) + 8);
    }

    @Override
    public boolean perform() {
        return false;
    }

    @Override
    public void draw(Batch batch) {
        this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f)); /**sets position of the college */
        this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));
        batch.begin();
        this.getSkin().draw(batch);//draws stone
        batch.end();

    }

    @Override
    public void shootPlayer(Ship player) {

    }

    @Override
    public void death(){

    }
}
