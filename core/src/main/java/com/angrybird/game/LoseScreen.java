package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class LoseScreen implements Screen {
    private AngryBirds game;
    private Stage stage;
    private Texture background;
    //private ImageButton restartButton;
    private ImageButton MainMenuButton;
    private InputMultiplexer multiplexer;

    public LoseScreen(final AngryBirds game) {
        this.game = game;
        background = new Texture(Gdx.files.internal("Losescreen.png"));
        stage = new Stage();

//        Texture playTexture = new Texture(Gdx.files.internal("restart.png"));
//        TextureRegionDrawable playDrawable = new TextureRegionDrawable(playTexture);
//        restartButton = new ImageButton(playDrawable);
//        restartButton.setSize(75, 75);
//        restartButton.setPosition(Gdx.graphics.getWidth() / 2 - 200, 75);

        Texture mainmenutexture = new Texture(Gdx.files.internal("saveandexiticon.png"));
        TextureRegionDrawable mainmenuDrawable = new TextureRegionDrawable(mainmenutexture);
        MainMenuButton = new ImageButton(mainmenuDrawable);
        MainMenuButton.setSize(90, 90);
        MainMenuButton.setPosition(Gdx.graphics.getWidth() / 2 -50, 75);

//        restartButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                game.setScreen(new Level1(game));
//            }
//        });

        MainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game));
            }
        });

        //stage.addActor(restartButton);
        stage.addActor(MainMenuButton);

        // Initialize InputMultiplexer and add the stage's input processor
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
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
