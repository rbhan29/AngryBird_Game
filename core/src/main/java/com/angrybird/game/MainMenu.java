package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class MainMenu implements Screen {
    private AngryBirds game;
    private Stage stage;
    private Texture background;
    private ImageButton playButton;

    public MainMenu(final AngryBirds game) {
        this.game = game;
        background = new Texture(Gdx.files.internal("MainMenu.png"));
        stage = new Stage();

        Texture playTexture = new Texture(Gdx.files.internal("Playbutton.png"));
        TextureRegionDrawable playDrawable = new TextureRegionDrawable(playTexture);
        playButton = new ImageButton(playDrawable);
        playButton.setSize(100, 100);
        playButton.setPosition(Gdx.graphics.getWidth() / 2 - 50, 75);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ChooseLevelMenu(game));
            }
        });

        stage.addActor(playButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }
}
