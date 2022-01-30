package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;


public class createNewBall {

    public float timer = 1.1f;
    public World world;
    public Body body;
    public boolean isDestroyed = false;
    public Vector2 target;
    public boolean setAngle = false;
    public float angle;
    public Sprite cannonBall;
    public float speed = 1.2f;
    public float damageDelt = 50f;

    createNewBall(World world, Sprite cannonBall, int width, int height, Vector2 position, Vector2 target, short categoryBits, short maskBit, short groupIndex){
        this.world = world;
        Body body;
        BodyDef def = new BodyDef();
        this.target = target;
        this.cannonBall = cannonBall;

       def.bullet = true;
       def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(position.x, position.y);


        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) / PIXEL_PER_METER, (height * 1.5f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef. density = 1f;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = (short) (maskBit);
        fixtureDef.filter.groupIndex = groupIndex;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        this.body = body;

    }
    createNewBall(World world, Sprite cannonBall, int width, int height, Vector2 position, float target, short categoryBits, short maskBit, short groupIndex){
        this.world = world;
        Body body;
        BodyDef def = new BodyDef();
        this.angle = target;
        this.cannonBall = cannonBall;


       def.bullet = true;
       def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(position.x, position.y);


        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) / PIXEL_PER_METER, (height * 1.5f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef. density = 1f;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = (short) (maskBit);
        fixtureDef.filter.groupIndex = groupIndex;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        this.body = body;
        this.body.setTransform(this.body.getPosition().x, this.body.getPosition().y, angle);
        this.setAngle = true;

    }


    public void update(Batch batch) {
            timer -= Gdx.graphics.getDeltaTime();
            if(timer <= 0 && !this.isDestroyed){
                this.world.destroyBody(this.body);
                this.isDestroyed = true;
                ballsManager.removeNext();
            }
            if(!setAngle){
                this.body.setTransform(this.body.getPosition().x, this.body.getPosition().y, (float)Math.atan2( this.body.getPosition().x-this.target.x,this.target.y- this.body.getPosition().y  ));
                this.setAngle = true;
                this.angle = this.body.getAngle();
            }
            if(this.body.getAngle() != this.angle | this.body.getPosition().x < 0 | this.body.getPosition().x > 1920 | this.body.getPosition().y < 0 | this.body.getPosition().y > 1080){
                this.body.setTransform(10000,10000,0);
            }

            this.body.applyForceToCenter(this.body.getWorldVector(new Vector2(0, 200079f)), true);
            Vector2 direction = new Vector2(this.body.getWorldPoint(new Vector2(0,this.cannonBall.getHeight())));
            Vector2 position = this.body.getPosition();
            position.x = position.x + (direction.x - position.x) * speed * PIXEL_PER_METER;
            position.y = position.y + (direction.y - position.y) * speed * PIXEL_PER_METER;
            this.body.setTransform(position, this.body.getAngle());
            this.cannonBall.setPosition(this.body.getPosition().x * PIXEL_PER_METER - (this.cannonBall.getWidth() / 2f), this.body.getPosition().y * PIXEL_PER_METER - (this.cannonBall.getHeight() / 2f));
            this.cannonBall.setRotation((float) Math.toDegrees(this.body.getAngle()));



            batch.begin();
            this.cannonBall.draw(batch);
            batch.end();
        }

    public float getDamageDelt() {
        return this.damageDelt;
    }
}
