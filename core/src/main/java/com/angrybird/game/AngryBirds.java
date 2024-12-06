package com.angrybird.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AngryBirds extends Game {
    public static final int V_WIDTH = 800; // Define the width of the viewport
    public static final int V_HEIGHT = 480; // Define the height of the viewport
    public static final float PPM = 30; // Define Pixels Per Meter

    public SpriteBatch batch;
    private Screen currentLevel;
    private OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
        this.setScreen(new LoadingScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    public void setCurrentLevel(Screen level) {
        this.currentLevel = level;
    }

    public Screen getCurrentLevel() {
        return currentLevel;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
