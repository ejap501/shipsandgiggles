package net.shipsandgiggles.pirate.entity.impl.shop;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.shop.Shop;

import java.util.UUID;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

public class shop1 extends Shop {
    public World world;

    public shop1(Sprite texture, Location location, float maximumHealth, World world) {
        super(UUID.randomUUID(), Type.SHOP, texture, location, maximumHealth,texture.getHeight(), texture.getWidth() );
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
        fixtureDef. density = 1f;
        fixtureDef.filter.categoryBits = Configuration.Cat_Shop; /**telling it what category it is */
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        this.body = body;
        this.world = world;

        this.hitBox = new com.badlogic.gdx.math.Rectangle((int)location.getX() - 200 ,(int)location.getY() -200 , 400,400);

    }

    @Override
    public void draw(Batch batch) {
        this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f)); /**sets position of the college */
        this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));
        batch.begin();

        this.getSkin().draw(batch);//draws college
        batch.end();


    }

    @Override
    public void shootPlayer(Ship player) {

    }

    @Override
    public void death() {

    }

    public static void rangeCheck(Ship player){
        Ship.buyMenuRange = player.hitBox.overlaps(shop1.hitBox);

    }
}
