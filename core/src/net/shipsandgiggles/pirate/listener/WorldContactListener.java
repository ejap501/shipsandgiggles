package net.shipsandgiggles.pirate.listener;

import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.college.College;
import net.shipsandgiggles.pirate.entity.CannonBall;

public class WorldContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA == null || fixtureB == null || fixtureB.getUserData() == null || fixtureA.getUserData() == null) return;

        if(fixtureB.getUserData() instanceof CannonBall){
            CannonBall ball = (CannonBall) fixtureB.getUserData();
            if(fixtureA.getUserData() instanceof College){
                College pp = (College) fixtureA.getUserData();
                pp.damage(ball.getDamageDelt());
            }
            if(fixtureA.getUserData() instanceof Ship){
                Ship pp = (Ship) fixtureA.getUserData();
                pp.takeDamage(ball.getDamageDelt());
            }
        }

    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
