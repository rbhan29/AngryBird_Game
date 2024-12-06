package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LoadingScreen implements Screen {

    private AngryBirds game;
    private Stage stage;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Texture loadingBar1, loadingBar2, loadingBar3, loadingBar4;
    private Image loadingImage;
    private float elapsedTime = 0f;
    private float frameDuration = 0.3f;
    private int frameIndex = 0;

    public LoadingScreen(final AngryBirds game) {
        this.game = game;
        stage = new Stage();
        backgroundTexture = new Texture(Gdx.files.internal("MainMenu.png"));
        backgroundImage = new Image(backgroundTexture);
        stage.addActor(backgroundImage);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        loadingBar1 = new Texture(Gdx.files.internal("load1.png"));
        loadingBar2 = new Texture(Gdx.files.internal("load2.png"));
        loadingBar3 = new Texture(Gdx.files.internal("load3.png"));
        loadingBar4 = new Texture(Gdx.files.internal("load4.png"));
        loadingImage = new Image(loadingBar1);
        loadingImage.setSize(200, 50);
        loadingImage.setPosition(Gdx.graphics.getWidth() / 2 - 100, 75);
        stage.addActor(loadingImage);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new MainMenu(game));
            }
        }, 1);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsedTime += delta;
        if (elapsedTime > frameDuration) {
            frameIndex = (frameIndex + 1) % 4;
            switch (frameIndex) {
                case 0:
                    loadingImage.setDrawable(new Image(loadingBar1).getDrawable());
                    break;
                case 1:
                    loadingImage.setDrawable(new Image(loadingBar2).getDrawable());
                    break;
                case 2:
                    loadingImage.setDrawable(new Image(loadingBar3).getDrawable());
                    break;
                case 3:
                    loadingImage.setDrawable(new Image(loadingBar4).getDrawable());
                    break;
            }
            elapsedTime = 0f;
        }
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        backgroundImage.setSize(width, height);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        loadingBar1.dispose();
        loadingBar2.dispose();
        loadingBar3.dispose();
        loadingBar4.dispose();
    }
}
