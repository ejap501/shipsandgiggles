package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class EntityAi implements Steerable<Vector2> {

    Body body;
    boolean tagged;
    float maxLinearSpeed, maxLinearAcceleration, maxAngularSpeed, maxAngularAcceleration, boundingRadius, zeroLinearSpeedThreshold, speedMultiplier, turnMultiplier;

    SteeringBehavior<Vector2> behavior;
    SteeringAcceleration<Vector2> steeringOutput;


    public EntityAi(Body body, float boundingRadius){
        this.body = body;
        this.boundingRadius = boundingRadius;

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

    public void update(float delta){
        if(behavior != null){
            behavior.calculateSteering(steeringOutput);
            applySteering(delta);
        }

    }

    public void applySteering(float delta){
        boolean anyAcceleration = false;

        if(!steeringOutput.linear.isZero()){
            Vector2 force = steeringOutput.linear.scl(delta * this.speedMultiplier) ;
            body.applyForceToCenter(force, true);
            anyAcceleration = true;
        }

        if(steeringOutput.angular != 0){
            body.applyTorque(steeringOutput.angular * delta * this.turnMultiplier, true);
            anyAcceleration = true;
        }else{
            Vector2 lineVel = getLinearVelocity();
            if(!lineVel.isZero()){
                float newOrientation = vectorToAngle(lineVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * delta * this.turnMultiplier);
                body.setTransform(body.getPosition(), newOrientation);
            }
        }


        if(anyAcceleration){
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            if(currentSpeedSquare > maxLinearSpeed * maxLinearSpeed){
                body.setLinearVelocity(velocity.scl(maxLinearSpeed/ (float) Math.sqrt(currentSpeedSquare)));
            }

            if(body.getAngularVelocity() > maxAngularSpeed){
                body.setAngularVelocity(maxAngularSpeed);
            }
        }
    }


    @Override
    public Vector2 getLinearVelocity() {
        return this.body.getPosition();
    }

    @Override
    public float getAngularVelocity() {
        return this.body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return this.boundingRadius;
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
}
