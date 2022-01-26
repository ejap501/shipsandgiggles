package net.shipsandgiggles.pirate.conf;

import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.entity.createNewBall;

public class worldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
//       Fixture fixtureA = contact.getFixtureA();
//       Fixture fixtureB = contact.getFixtureB();
       // System.out.println("pp");
//       if(fixtureA == null || fixtureB == null || fixtureB.getUserData() == null || fixtureA.getUserData() == null) return;
//       System.out.println("oo");
//       if(fixtureA.getUserData() instanceof createNewBall){
//           createNewBall ball = (createNewBall) fixtureA.getUserData();
//           ball.hitsSth();
//       }
//       if(fixtureB.getUserData() instanceof createNewBall){
//           createNewBall ball = (createNewBall) fixtureB.getUserData();
//           ball.hitsSth();
//       }

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
