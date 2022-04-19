package net.shipsandgiggles.pirate.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.EntityType;
import net.shipsandgiggles.pirate.entity.Location;

import java.util.UUID;


/** NPC data that allows us to implement basic hostile classes*/
    public abstract class NPC extends Entity {
        private final NPC.Type type;
        public Body body;
        public float counter = 0;
        public boolean dead = false;
        public boolean removed = false;
        public Rectangle hitBox;
        public Sprite cannonBallSprite =  new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));
        public float cooldownTimer = 1f;
        public float timer = 0f;
        public Texture healthBar = new Texture("models/bar.png");

        public NPC(UUID uuid, NPC.Type type, Sprite texture, Location location, float maximumHealth, float height, float width) {
            super(uuid, texture, location, EntityType.NPC, maximumHealth, height, width);

            this.type = type;
        }


        public abstract boolean perform();

        public Body getBody(){
            return this.body;
        }
        public void death() {/** to give chance to player to keep the college alive*/
            if(this.dead) return;
            Currency.get().give(Currency.Type.POINTS, 25);
            Currency.get().give(Currency.Type.GOLD, 50);
            this.dead = true;
        }

        /**
         * Types of NPC - allows us to keep track.
         */
        public enum Type {

            HOSTILE;

            private final UUID randomId;

            /**
             * Assign static value at runtime, as value will not change and maximum of 1 college.
             **/
            Type() {
                this.randomId = UUID.randomUUID();
            }

            /**
             * @return Unique identifier associated with this UUID.
             */
            public UUID getId() {
                return randomId;
            }
        }
    }

