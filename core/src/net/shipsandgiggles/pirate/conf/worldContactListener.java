package net.shipsandgiggles.pirate.conf;

import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.entity.college.College;
import net.shipsandgiggles.pirate.entity.createNewBall;
import net.shipsandgiggles.pirate.entity.impl.college.LangwithCollege;

public class worldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA == null || fixtureB == null || fixtureB.getUserData() == null || fixtureA.getUserData() == null) return;

        if(fixtureB.getUserData() instanceof createNewBall){
            createNewBall ball = (createNewBall) fixtureB.getUserData();
            if(fixtureA.getUserData() instanceof College){
                College pp = (College) fixtureA.getUserData();
                pp.damage(ball.getDamageDelt());
            }
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
