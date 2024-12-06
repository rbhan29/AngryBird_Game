package com.angrybird.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

import static com.angrybird.game.AngryBirds.PPM;

public class Pig {
    private Texture texture;
    private Body body;
    private float width;
    private float height;
    private float hp ;
    private float lifeTime; // Timer to track how long the pig has been alive

    public float getHp() {
        return hp;
    }

    public void setHp(float damage) {
        this.hp = hp - damage;
    }

    public Pig(Texture texture, float hp) {
        this.texture = texture;
        this.width = 40 / PPM;
        this.height = 40 / PPM;
        this.lifeTime = 0;
        this.hp = hp;
    }

    public void createBody(World world, float width, float height) {
        this.width = width;
        this.height = height;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(width/2*0.9f, height/2*0.9f);

        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2 * 0.9f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 7f;
        fixtureDef.friction = 4f; // Adjusted friction
        fixtureDef.restitution = 0.5f; // Lower restitution to reduce bouncing
        body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        body.createFixture(fixtureDef);
        body.setUserData(this);
        body.setFixedRotation(false); // Allow rotation

        shape.dispose();
    }

    public void setLinearDamping(float damping) {
        if (body != null) {
            body.setLinearDamping(damping);
        }
    }

    public void updateLifeTime(float delta) {
        lifeTime += delta;
    }

    public float getLifeTime() {
        return lifeTime;
    }

    public Body getBody() {
        return body;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
