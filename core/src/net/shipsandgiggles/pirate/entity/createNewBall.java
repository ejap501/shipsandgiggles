package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;


public class createNewBall {

    public float timer = 5;
    public World world;
    public Body body;
    public boolean isDestroyed = false;
    public Vector2 target;
    public boolean setAngle = false;
    createNewBall(World world, int width, int height, Vector2 position, Vector2 target, short categoryBits, short maskBit, short groupIndex){
        this.world = world;
        Body body;
        BodyDef def = new BodyDef();
        this.target = target;


       def.bullet = true;
       def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(position.x, position.y);


        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) / PIXEL_PER_METER, (height / 2f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef. density = 1f;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = (short) (maskBit | Configuration.Cat_walls);
        fixtureDef.filter.groupIndex = groupIndex;
        body.createFixture(fixtureDef);
        shape.dispose();
        this.body = body;

    }


    public void update() {
        timer -= Gdx.graphics.getDeltaTime();
        if(timer <= 0 && !this.isDestroyed){
            this.world.destroyBody(this.body);
            this.isDestroyed = true;
            ballsManager.removeNext();
        }
        if(!setAngle){
            this.body.setTransform(this.body.getPosition().x, this.body.getPosition().y, (float)Math.atan2( this.body.getPosition().x-this.target.x,this.target.y- this.body.getPosition().y  ));
            this.setAngle = true;
        }

        this.body.applyForceToCenter(this.body.getWorldVector(new Vector2(0, 200079f)), true);

    }

    public void destroy() {
        this.world.destroyBody(this.body);
    }
}
