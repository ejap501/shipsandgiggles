package net.shipsandgiggles.pirate.conf;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.ExplosionController;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.college.College;
import net.shipsandgiggles.pirate.entity.createNewBall;
import net.shipsandgiggles.pirate.entity.impl.college.LangwithCollege;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

import java.util.ArrayList;

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
