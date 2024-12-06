package com.angrybird.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ChooseLevelMenu implements Screen {
    private final Game game;
    private final Stage stage;
    private final SpriteBatch batch;
    private final Texture backgroundImage;

    private Texture level1Icon, level2Icon, level3Icon, settingsIcon, loadSavedIcon;

    public ChooseLevelMenu(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundImage = new Texture(Gdx.files.internal("chooselevelbackground.png"));

        level1Icon = new Texture(Gdx.files.internal("click1.png"));
        level2Icon = new Texture(Gdx.files.internal("click2.png"));
        level3Icon = new Texture(Gdx.files.internal("click3.png"));
        settingsIcon = new Texture(Gdx.files.internal("settingsicon.png"));
        loadSavedIcon = new Texture(Gdx.files.internal("loadsaveicon.png"));

        ImageButton level1Button = createButton(level1Icon);
        ImageButton level2Button = createButton(level2Icon);
        ImageButton level3Button = createButton(level3Icon);
        ImageButton settingsButton = createButton(settingsIcon);
        ImageButton loadSavedButton = createButton(loadSavedIcon);

        level1Button.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new Level1(game));
            }
        });

        level2Button.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new Level2(game));
            }
        });

        level3Button.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new Level3(game));
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new SettingsMenu((AngryBirds) game));  // Navigate to SettingsScreen
            }
        });

        loadSavedButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new LoadSavedScreen(game));
            }
        });

        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.top().right();
        buttonTable.add(settingsButton).pad(10).size(80, 80).row();
        buttonTable.add(loadSavedButton).pad(10).size(80, 80).row();

        Table levelTable = new Table();
        levelTable.setFillParent(true);
        levelTable.center();
        levelTable.add(level1Button).pad(10).size(100, 100);
        levelTable.add(level2Button).pad(10).size(100, 100);
        levelTable.add(level3Button).pad(10).size(100, 100);

        stage.addActor(buttonTable);
        stage.addActor(levelTable);
    }

    private ImageButton createButton(Texture texture) {
        ImageButton button = new ImageButton(new TextureRegionDrawable(texture));
        button.getImage().setFillParent(true);
        return button;
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
        level1Icon.dispose();
        level2Icon.dispose();
        level3Icon.dispose();
        settingsIcon.dispose();
        loadSavedIcon.dispose();
    }
}
