package net.shipsandgiggles.pirate.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Weather;
import net.shipsandgiggles.pirate.entity.EntityAi;
import net.shipsandgiggles.pirate.entity.CannonBall;
import net.shipsandgiggles.pirate.entity.college.College;
import net.shipsandgiggles.pirate.entity.impl.obstacles.Stone;

/**
 * World contact listener
 * Checks for world collisions
 *
 * @author Team 23
 * @author Team 22 - Ethan Alabaster, Sam Pearson
 * @version 2.0
 */
public class WorldContactListener implements ContactListener {
    /**
     * Performs actions based on contact type
     *
     * @param contact : Contact instance check
     */
    @Override
    public void beginContact(Contact contact) {
        // Sets fixtures
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA == null || fixtureB == null || fixtureB.getUserData() == null || fixtureA.getUserData() == null) return;

        // Checks if the collider is a cannonball
        if(fixtureB.getUserData() instanceof CannonBall){
            CannonBall ball = (CannonBall) fixtureB.getUserData();
            // Checks for a college collision
            if(fixtureA.getUserData() instanceof College){
                College college = (College) fixtureA.getUserData();
                // Applies damage to the college
                college.damage(ball.getDamageDelt());
                ball.setToDestroy();
                Gdx.app.log("cannonball - college", "collision");
            }
            // Checks for a player collision
            if(fixtureA.getUserData() instanceof Ship){
                Ship ship = (Ship) fixtureA.getUserData();
                // Applies damage to the ship
                ship.takeDamage(ball.getDamageDelt());
                ball.setToDestroy();
                Gdx.app.log("cannonball - ship", "collision");
            }
            // Checks for a NPC collision
            if(fixtureA.getUserData() instanceof EntityAi){
                EntityAi npc = (EntityAi)  fixtureA.getUserData();
                // Applies damage to the NPC
                npc.damage(ball.getDamageDelt());
                ball.setToDestroy();
                Gdx.app.log("cannonball - NPC", "collision");

            }

        }
        if(fixtureB.getUserData() instanceof Ship){
            Ship ship = (Ship) fixtureB.getUserData();
            if(fixtureA.getUserData() instanceof Weather){
                Weather weather = (Weather) fixtureA.getUserData();
                ship.inFog();
                Gdx.app.log("Weather", "collision");
            }
        }

        if(fixtureB.getUserData() instanceof Stone){
            Stone stone = (Stone) fixtureB.getUserData();
            if(fixtureA.getUserData() instanceof Ship){
                Ship ship = (Ship) fixtureA.getUserData();
                Gdx.app.log("ship - stone", "collision");
            }
        }


    }

    /**
     * Terminates contact
     *
     * @param contact : Contact instance
     */
    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA == null || fixtureB == null || fixtureB.getUserData() == null || fixtureA.getUserData() == null) return;

        if(fixtureB.getUserData() instanceof Ship){
            Ship ship = (Ship) fixtureB.getUserData();
            if(fixtureA.getUserData() instanceof Weather){
                Weather weather = (Weather) fixtureA.getUserData();
                ship.outFog();
                Gdx.app.log("Weather", "collision");
            }
        }
    }

    /**
     * Terminates contact with manifold
     *
     * @param contact : Contact instance
     * @param oldManifold : Manifold used
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    /**
     * Terminates contact with impulse
     *
     * @param contact : Contact instance
     * @param impulse : Impulse used
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
