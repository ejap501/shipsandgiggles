package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.currency.Currency;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

public class EntityAi implements Steerable<Vector2> {
    /** the creation and AI of the enemy boat*/
    public Body body;
    boolean tagged;
    float maxLinearSpeed, maxLinearAcceleration, maxAngularSpeed, maxAngularAcceleration, boundingRadius, zeroLinearSpeedThreshold, speedMultiplier, turnMultiplier;
    Sprite texture;
    boolean isPlayer;
    Body target;
    float amountOfRotations = 0;
    private boolean independentFacing = false; /**defines if the entity can move in a direction other than the way it faces) */
    float angleToTarget = 0;
    int health;
    int maxHealth;
    public boolean dead;
    public float counter = 0;
    public float timer = 0f;
    private Rectangle hitBox;
    public Sprite cannonBallSprite =  new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));

    SteeringBehavior<Vector2> behavior;
    public SteeringAcceleration<Vector2> steeringOutput;



    public EntityAi(Body body, float boundingRadius, Sprite texture, int width, int height){
        /** creation of the Ai of the enemy */
        this.body = body;
        this.boundingRadius = boundingRadius;
        this.texture = texture;

        this.isPlayer = false;
        this.maxLinearSpeed = 5000;
        this.maxLinearAcceleration = 5000;
        this.maxAngularSpeed = 90;
        this.maxAngularAcceleration = 30;
        this.zeroLinearSpeedThreshold = 0.1f;
        this.speedMultiplier = 60f;
        this.turnMultiplier = 0.01f;

        this.tagged = false;
        this.body.setFixedRotation(false);
        MassData MassData = new MassData();
        MassData.mass= 6000f;
        MassData.center.set(this.getPosition().x/2, this.getPosition().y/2);
        this.body.setMassData(MassData);

        this.steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
        this.body.setUserData(this);
        this.body.setLinearDamping(1f);

        this.health = 100;
        this.maxHealth = 100;
        this.dead = false;

        this.hitBox =  new Rectangle(this.body.getPosition().x, this.body.getPosition().y, texture.getWidth() + 350, texture.getHeight() + 350);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) / PIXEL_PER_METER, (height / 2f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = Configuration.Cat_Enemy;


        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();


    }

    public EntityAi(Body body, float boundingRadius){
        /**creation of the target for the player */

        this.body = body;
        this.boundingRadius = boundingRadius;

        this.isPlayer = true;
        this.maxLinearSpeed = 5000;
        this.maxLinearAcceleration = 5000;
        this.maxAngularSpeed = 90;
        this.maxAngularAcceleration = 30;
        this.zeroLinearSpeedThreshold = 0.01f;
        this.speedMultiplier = 400f;
        this.turnMultiplier = 2000f;

        this.tagged = false;
        this.body.setFixedRotation(false);



        this.steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
        this.body.setUserData(this);
        this.body.setLinearDamping(1f);
    }

    public void update(float delta, Batch batch,Ship player, World world){
        if(isPlayer){
            return;
        }

        if (health == 0 && !dead){
            death(world);
        }

        else if(behavior != null){
            this.steeringOutput = behavior.calculateSteering(steeringOutput); /** calculates if needs steering */
            applySteering(this.steeringOutput, delta);
            shootPlayer(player, world);

        }
        drawEntity(batch);

    }

    public void drawEntity(Batch batch){
        if(!this.isPlayer){
            this.getSprite().setPosition(this.body.getPosition().x * PIXEL_PER_METER - (this.getSprite().getWidth() / 2f), this.body.getPosition().y* PIXEL_PER_METER - (this.getSprite().getHeight() / 2f)); /**sets position and rotation of the boat */
            this.getSprite().setRotation((float) Math.toDegrees(this.getBody().getAngle()));

           // System.out.println(this.getSprite().getOriginY() + "" + this.body.getPosition());

            batch.begin();
            this.getSprite().draw(batch);
            batch.end();
        }
    }

    public Sprite getSprite(){
        return this.texture;
    }

    public void applySteering (SteeringAcceleration<Vector2> steeringOutput, float deltaTime) {
        if(isPlayer){
            return;
        }
        boolean anyAccelerations = false;

        /** Update position and linear velocity.*/
        if (!steeringOutput.linear.isZero()) {
            // this method internally scales the force by deltaTime
            body.applyForceToCenter(new Vector2(steeringOutput.linear.x * speedMultiplier , (steeringOutput.linear.y * speedMultiplier)), true);
            anyAccelerations = true;
        }

        /**  Update orientation and angular velocity*/
        if (isIndependentFacing()) {
            if (steeringOutput.angular != 0) {
                /** this method internally scales the torque by deltaTime*/
                body.applyTorque(steeringOutput.angular * turnMultiplier, true);
                anyAccelerations = true;
            }
        } else {
            /**  If we haven't got any velocity, then we can do nothing.*/
            Vector2 linVel = this.getLinearVelocity();
            if (steeringOutput.linear.len() > 25f) {


                float newOrientation = vectorToAngle(linVel);
                /** sets new angle towards player*/
               this.setAngleToTarget(this.getAngleToTarget() + ((float)Math.atan2(this.target.getPosition().y - this.getPosition().y, this.target.getPosition().x - this.getPosition().x) - 1.5708f - this.angleToTarget) * turnMultiplier * PIXEL_PER_METER);

                this.getBody().setTransform(this.body.getPosition().x, this.body.getPosition().y, this.getAngleToTarget());
                System.out.println(this.body.getPosition());

            }

        }

        if (anyAccelerations) {
            /**Cap the linear speed */
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > (maxLinearSpeed * maxLinearSpeed)) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
            }
            /** Cap the angular speed*/
            float maxAngVelocity = getMaxAngularSpeed();
            if (body.getAngularVelocity() > maxAngVelocity) {
                body.setAngularVelocity(maxAngVelocity);
            }
            this.hitBox.setPosition(this.body.getPosition());
        }
    }
    public void shootPlayer(Ship player, World world) {
        if(this.health == 1 && !this.dead){
            this.counter += Gdx.graphics.getDeltaTime();
            if(this.counter >= 1){
                Currency.get().give(Currency.Type.POINTS, 3);
                Currency.get().give(Currency.Type.GOLD, 5);
                this.counter = 0;
            }
        }
        if(this.hitBox.overlaps(player.hitBox) && timer <= 0 && !this.dead && this.health != 1) {/** creates shot and shoots*/
        System.out.println("ss");
            BallsManager.createBall(world, new Vector2(this.body.getPosition().x, this.body.getPosition().y), new Vector2(player.getEntityBody().getPosition().x, player.getEntityBody().getPosition().y), 1, cannonBallSprite, (short)(Configuration.Cat_Enemy | Configuration.Cat_College), Configuration.Cat_Player, (short) 0);
            this.timer = 4;
        }
        else if(timer <= 0) this.timer = 0; /** ensures that there is a cool down between every shot*/
        else this.timer -= Gdx.graphics.getDeltaTime();
    }


    public boolean isIndependentFacing () {
        return independentFacing;
    }

    public void setIndependentFacing (boolean independentFacing) {
        this.independentFacing = independentFacing;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return this.body.getPosition();
    }

    public void setAmountOfRotations(float amountOfRotations) {
        this.amountOfRotations = amountOfRotations;
    }

    public float getAmountOfRotations() {
        return amountOfRotations;
    }

    @Override
    public float getAngularVelocity() {
        return this.body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return this.boundingRadius;
    }

    public void setAngleToTarget(float angleToTarget){
        this.angleToTarget = angleToTarget;
    }

    public float getAngleToTarget() {
        return angleToTarget;
    }

    @Override
    public boolean isTagged() {
        return this.tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }


    @Override
    public float getZeroLinearSpeedThreshold() {
        return this.zeroLinearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        this.zeroLinearSpeedThreshold = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return this.maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return this.maxLinearAcceleration;
    }

    public void setTarget(Body body){
        this.target = body;
    }

    public Body getTarget() {
        return target;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearSpeed = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return this.maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return this.maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    @Override
    public float getOrientation() {
        return this.body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {

    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return null;
    }


    public Body getBody() {
        return this.body;
    }

    public void setBehavior(SteeringBehavior<Vector2> behavior){
        this.behavior = behavior;
    }

    public SteeringBehavior<Vector2> getBehavior(){
        return this.behavior;
    }

    public void death(World world){
        if(this.dead) return;
        Currency.get().give(Currency.Type.GOLD, 10); /** gives instant money if collected*/
        world.destroyBody(body);
        this.dead = true;
        }

    public void damage(float damageValue){
        health -= damageValue;

    }
}

