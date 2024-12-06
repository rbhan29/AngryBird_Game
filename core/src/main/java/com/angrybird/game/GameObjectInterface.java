package com.angrybird.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public interface GameObjectInterface {
    void createBody(World world, float width, float height);
    Body getBody();
    void setBody(Body body);
    Texture getTexture();
    Sprite getSprite();
}
