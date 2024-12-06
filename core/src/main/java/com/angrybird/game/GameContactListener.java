package com.angrybird.game;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;


public class GameContactListener implements ContactListener {
    private Array<Block> blocksToDestroy;
    private Array<Pig> pigsToDestroy;

    public GameContactListener() {
        blocksToDestroy = new Array<>();
        pigsToDestroy = new Array<>();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Handle collision logic here
        if (fixtureA.getUserData() instanceof Bird && fixtureB.getUserData() instanceof Block) {
            handleBirdBlockCollision((Bird) fixtureA.getUserData(), (Block) fixtureB.getUserData());
        } else if (fixtureA.getUserData() instanceof Block && fixtureB.getUserData() instanceof Bird) {
            handleBirdBlockCollision((Bird) fixtureB.getUserData(), (Block) fixtureA.getUserData());
        } else if (fixtureA.getUserData() instanceof Bird && fixtureB.getUserData() instanceof Pig) {
            handlepigBirdCollision((Pig) fixtureB.getUserData(), (Bird) fixtureA.getUserData());
        } else if (fixtureA.getUserData() instanceof Pig && fixtureB.getUserData() instanceof Bird) {
            handlepigBirdCollision((Pig) fixtureA.getUserData(), (Bird) fixtureB.getUserData());
        } else if (fixtureA.getUserData() instanceof Pig && fixtureB.getUserData() instanceof Block) {
            handlepigBlockCollision((Pig) fixtureA.getUserData(), (Block) fixtureB.getUserData());
        } else if (fixtureA.getUserData() instanceof Block && fixtureB.getUserData() instanceof Pig) {
            handlepigBlockCollision((Pig) fixtureB.getUserData(), (Block) fixtureA.getUserData());
        }
    }

    private void handleBirdBlockCollision(Bird bird, Block block) {
        block.setHp(bird.getDamage());
        if (block.getHp() <= 0) {
            blocksToDestroy.add(block);
        }
    }

    private void handlepigBirdCollision(Pig pig, Bird bird) {
        pig.setHp(bird.getDamage());
        if (pig.getHp() <= 0) {
            pigsToDestroy.add(pig);
        }
    }

    private void handlepigBlockCollision(Pig pig, Block block) {
        pig.setHp(block.getDamage());
        if (pig.getHp() <= 0) {
            pigsToDestroy.add(pig);
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Handle end contact logic if needed
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public Array<Block> getBlocksToDestroy() {
        return blocksToDestroy;
    }

    public Array<Pig> getPigsToDestroy() {
        return pigsToDestroy;
    }
}
