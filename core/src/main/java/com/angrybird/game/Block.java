package com.angrybird.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

import static com.angrybird.game.AngryBirds.PPM;

public class Block {
    private Texture texture;
    private Body body;
    private float width;
    private float height;
    private float hp ;
    private float damage ;

    public float getDamage() {
        return damage;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float damage) {
        this.hp = hp-damage;
    }

    public Block(Texture texture, float hp, float damage) {
        this.texture = texture;
        this.width = 60 / PPM;
        this.height = 40 / PPM;
        this.hp = hp;
        this.damage = damage;
    }

    public void createBody(World world, float width, float height) {
        this.width = width;
        this.height = height;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.7f;
        body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        shape.dispose();
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
