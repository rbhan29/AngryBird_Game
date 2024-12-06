package com.angrybird.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoadSavedScreen implements Screen {
    private final Game game;
    private final Stage stage;
    private final SpriteBatch batch;
    private final Texture backgroundImage;
    private Skin skin;
    private ImageButton MainMenuButton;
    private TextButton loadGame1Button;
    private Texture MainMenuButtonTexture;

    public LoadSavedScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundImage = new Texture(Gdx.files.internal("pausebackground.png"));
        MainMenuButtonTexture = new Texture(Gdx.files.internal("saveandexiticon.png"));
        TextureRegionDrawable playDrawable = new TextureRegionDrawable(MainMenuButtonTexture);
        MainMenuButton = new ImageButton(playDrawable);
        MainMenuButton.setSize(70, 70);
        MainMenuButton.setPosition(Gdx.graphics.getWidth() / 2 - 350, 40);

        MainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu((AngryBirds) game));
            }
        });

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        loadGame1Button = new TextButton("Load Game 1", skin);

        loadGame1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SaveLevel savedLevel = SaveLevel.loadFromFile("savegame.json");
                if (savedLevel != null) {
                    Level1 level1 = new Level1(game);
                    level1.restoreLevelState(savedLevel);
                    game.setScreen(level1);
                }
            }
        });


        stage.addActor(MainMenuButton);
        stage.addActor(loadGame1Button);

        Table table = new Table();
        table.setFillParent(true);
        table.add(loadGame1Button).pad(30);
        table.row();

        stage.addActor(table);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        backgroundImage.dispose();
        skin.dispose();
    }
}
