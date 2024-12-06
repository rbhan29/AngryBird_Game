package com.angrybird.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

import static com.angrybird.game.AngryBirds.PPM;

public class Bird {
    private Texture texture;
    private Body body;
    private float width;
    private float height;
    private boolean launched;
    private int damage = 20;
    private float launchTime; // Timer to track how long the bird has been launched
    private float hp = 100;


    public Bird(Texture texture) {
        this.texture = texture;
        this.width = 40 / PPM;
        this.height = 40 / PPM;
        this.launched = false;
        this.launchTime = 0;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public int getDamage() {
        return damage;
    }

    public void createBody(World world, float width, float height) {
        this.width = width;
        this.height = height;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(width / 2*0.9f, height / 2*0.9f);

        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2*0.9f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.75f;
        fixtureDef.friction = 0.5f; // Adjusted friction
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

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
        if (launched) {
            this.launchTime = 0; // Reset the timer when the bird is launched
        }
    }

    public void updateLaunchTime(float delta) {
        if (launched) {
            launchTime += delta;
        }
    }

    public float getLaunchTime() {
        return launchTime;
    }
}
