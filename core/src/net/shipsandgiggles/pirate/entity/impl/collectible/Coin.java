package net.shipsandgiggles.pirate.entity.impl.collectible;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.collectible.Plunder;

import java.util.UUID;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

public class Coin extends Plunder{
    public World world;
    public Circle hitBox;

    public Coin(Sprite texture, Location location, World world) {
        super(UUID.randomUUID(), Type.COIN, texture, location, 1, texture.getHeight(), texture.getWidth());
        Body body;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(location.getX(), location.getY());
        def.fixedRotation = true;
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setRadius((texture.getWidth() / 2f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef. density = 1f;
        fixtureDef.filter.categoryBits = Configuration.Cat_Collect; /**telling it what category it is */
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        this.entityBody = body;
        this.world = world;

        //Needs hitbox

    }

    @Override
    public void draw(Batch batch) {
        if(collected){
            return;
        }
        batch.begin();
        this.getTexture().draw(batch);
        batch.end();
    }

    @Override
    public void shootPlayer(Ship player) {}
}