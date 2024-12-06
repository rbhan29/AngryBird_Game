package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsMenu implements Screen {

    private AngryBirds game;
    private Stage stage;
    private Texture backgroundTexture;
    private Texture exitButtonTexture;
    private Texture mainMenuButtonTexture;
    private Image backgroundImage;
    private Image exitButton;
    private Image mainMenuButton;

    public SettingsMenu(final AngryBirds game) {
        this.game = game;

        stage = new Stage();
        backgroundTexture = new Texture(Gdx.files.internal("pausebackground.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("quit.png"));
        mainMenuButtonTexture = new Texture(Gdx.files.internal("mainmenuicon.png"));


        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create buttons from textures
        exitButton = new Image(exitButtonTexture);
        mainMenuButton = new Image(mainMenuButtonTexture);

        // Set up button click listeners
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit(); // Exit the game
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game));
            }
        });

        // Increase button sizes
        float buttonWidth = Gdx.graphics.getWidth() * 0.3f; // 30% of screen width
        float exitButtonHeight = buttonWidth * getAspectRatio(exitButtonTexture);
        float mainMenuButtonHeight = buttonWidth * getAspectRatio(mainMenuButtonTexture);

        // Set button sizes
        exitButton.setSize(buttonWidth, exitButtonHeight);
        mainMenuButton.setSize(buttonWidth, mainMenuButtonHeight);


        Table table = new Table();
        table.setFillParent(true);
        table.center();


        table.add(mainMenuButton).padBottom(40).width(buttonWidth).height(mainMenuButtonHeight);
        table.row();
        table.add(exitButton).width(buttonWidth).height(exitButtonHeight);


        stage.addActor(backgroundImage);
        stage.addActor(table);
    }


    private float getAspectRatio(Texture texture) {
        return (float) texture.getHeight() / texture.getWidth();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
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
        exitButtonTexture.dispose();
        mainMenuButtonTexture.dispose();
    }
}
