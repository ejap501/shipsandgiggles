package net.shipsandgiggles.pirate.entity.impl.college;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.college.College;

import java.util.UUID;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

public class GoodrickCollege extends College {


    public GoodrickCollege(Sprite texture, Location location, float maximumHealth, World world) {
        super(UUID.randomUUID(), Type.LANGWITH, texture, location, maximumHealth, texture.getHeight(), texture.getWidth());

        Body body;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(location.getX(), location.getY());

        def.fixedRotation = true;
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((texture.getWidth() / 2f) / PIXEL_PER_METER, (texture.getHeight() / 2f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef. density = 1f;
        fixtureDef.filter.categoryBits = Configuration.Cat_College;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        this.body = body;

    }

    @Override
    public boolean perform() {
        return false;
    }

    @Override
    public void draw(Batch batch) {
        if(dead){
            return;
        }

        this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f));
        this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));
        batch.begin();

        this.getSkin().draw(batch);
        batch.end();
    }




}
