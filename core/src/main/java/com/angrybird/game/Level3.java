package com.angrybird.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;

public class Level3 implements Screen {
    private AngryBirds game;
    private Texture background;
    private Stage stage;
    private Texture pauseButtonTexture;
    private Image pauseButton;

    private Texture redBirdTexture, blackBirdTexture, yellowBirdTexture, CatapultTexture, stoneblockTexture, medpigTexture,kingpigTexture,glassblockTexture;;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Bird currentBird;
    private boolean isBirdOnCatapult = false;
    private boolean isDragging = false;
    private Vector2 initialTouch = new Vector2();
    private Vector2 maxDragDistance = new Vector2(100, 100); // Adjust as needed
    private GameContactListener collisionListener;
    private boolean isGameOver = false;
    private float gameOverTime = 0;// Define the collisionListener

    private Level level; // Define the level object

    private List<Bird> birds = new ArrayList<>();
    private List<Pig> pigs = new ArrayList<Pig>();
    private List<Block> blocks = new ArrayList<Block>();
    private static final float BIRD_LIFETIME = 5.0f; // Bird lifetime in seconds

    public Level3(Game game) {
        this.game = (AngryBirds) game;

        background = new Texture(Gdx.files.internal("levelbackground.png"));
        stage = new Stage();

        pauseButtonTexture = new Texture(Gdx.files.internal("pauseicon.png"));
        pauseButton = new Image(pauseButtonTexture);
        pauseButton.setSize(64, 64);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new PauseGame((AngryBirds) game));
            }
        });

        Table table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.add(pauseButton).size(64, 64).padTop(10).padLeft(10);
        stage.addActor(table);

        redBirdTexture = new Texture(Gdx.files.internal("red.png"));
        blackBirdTexture = new Texture(Gdx.files.internal("black.png"));
        yellowBirdTexture = new Texture(Gdx.files.internal("yellow.png"));
        CatapultTexture = new Texture(Gdx.files.internal("catapault.png"));
        stoneblockTexture = new Texture(Gdx.files.internal("stoneblock.png"));
        glassblockTexture = new Texture(Gdx.files.internal("glassblock.png"));
        medpigTexture = new Texture(Gdx.files.internal("mediumpig.png"));
        kingpigTexture = new Texture(Gdx.files.internal("kingpig.png"));

        ((AngryBirds) game).setCurrentLevel(this);

        world = new World(new Vector2(0, -9.8f), true);
        collisionListener = new GameContactListener(); // Initialize the collisionListener
        world.setContactListener(collisionListener);
        b2dr = new Box2DDebugRenderer();
        createGround();
        createGameObjects();

        level = new Level(); // Initialize the level object
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        setupNextBird();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Sprite catapult = new Sprite(CatapultTexture);
        catapult.setSize(80, 150);
        catapult.setPosition(100, 125);
        catapult.draw(game.batch);

        drawGameObjects();
        game.batch.end();

        stage.act(delta);
        stage.draw();

        world.step(1 / 60f, 6, 2);

        handleInput();
        handleCollisions();

        if (currentBird != null) {
            currentBird.updateLaunchTime(delta);
            if (currentBird.isLaunched() && currentBird.getLaunchTime() > BIRD_LIFETIME) {
                world.destroyBody(currentBird.getBody());
                currentBird = null;
                isBirdOnCatapult = false;
                setupNextBird();
            }
        }

        checkWinLose(delta);

        if (isGameOver) {
            gameOverTime += delta;
            if (gameOverTime >= 3) {
                if (pigs.isEmpty()) {
                    game.setScreen(new WinScreen(game)); // Assuming score is 0 and level is 2
                } else {
                    game.setScreen(new LoseScreen(game)); // Assuming score is 0 and level is 2
                }
                dispose();
            }
        }
        //b2dr.render(world, game.getCamera().combined); // Render the Box2DDebugRenderer
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
        background.dispose();
        stage.dispose();
        pauseButtonTexture.dispose();
        redBirdTexture.dispose();
        blackBirdTexture.dispose();
        yellowBirdTexture.dispose();
        CatapultTexture.dispose();
        stoneblockTexture.dispose();
        glassblockTexture.dispose();
        medpigTexture.dispose();
        kingpigTexture.dispose();
        world.dispose();
        b2dr.dispose();
    }

    private void createGround() {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(new Vector2(0, 4.5f));

        ChainShape groundShape = new ChainShape();
        groundShape.createChain(new Vector2[]{
            new Vector2(0, 0),
            new Vector2(Gdx.graphics.getWidth() / AngryBirds.PPM, 0)
        });

        FixtureDef groundFixture = new FixtureDef();
        groundFixture.shape = groundShape;
        groundFixture.friction = 1f;
        groundFixture.restitution = 0.3f;

        world.createBody(groundBodyDef).createFixture(groundFixture);

        groundShape.dispose();
    }

    private void createGameObjects() {
        createGround();

        createBird(yellowBirdTexture, 140, 230, 35, 35);
        createBird(blackBirdTexture, 60, 150, 35,35);
        createBird(redBirdTexture, 20, 150, 35, 35);

        // Create blocks
        createStoneBlock(stoneblockTexture, 510, 170, 50, 50);
        createStoneBlock(stoneblockTexture, 560, 170, 50, 50);
        createStoneBlock(stoneblockTexture, 610, 170, 50, 50);
        createStoneBlock(stoneblockTexture, 660, 170, 50, 50);
        createStoneBlock(stoneblockTexture, 710, 170, 50, 50);
        createStoneBlock(stoneblockTexture, 510, 220, 50, 50);
        createGlassBlock(glassblockTexture, 610, 220, 50, 50);
        createStoneBlock(stoneblockTexture, 710, 220, 50, 50);
        createGlassBlock(glassblockTexture, 560, 270, 50, 50);
        createGlassBlock(glassblockTexture, 610, 270, 50, 50);
        createGlassBlock(glassblockTexture, 660, 270, 50, 50);

        // Create pigs
        createMediumPig(medpigTexture, 560, 220, 50, 50);
        createMediumPig(medpigTexture, 660, 220, 50, 50);
        createKingPig(kingpigTexture, 610, 320, 80, 80);

    }

    private void createStoneBlock(Texture texture, float x, float y, float width, float height) {
        Block stoneblock = new StoneBlock(texture);
        stoneblock.createBody(world, width / AngryBirds.PPM, height / AngryBirds.PPM);
        blocks.add(stoneblock);
        stoneblock.getBody().setTransform(x / AngryBirds.PPM, y / AngryBirds.PPM, 0);
        stoneblock.getBody().setType(BodyDef.BodyType.DynamicBody);
    }

    private void createGlassBlock(Texture texture, float x, float y, float width, float height) {
        Block glassblock = new GlassBlock(texture);
        glassblock.createBody(world, width / AngryBirds.PPM, height / AngryBirds.PPM);
        blocks.add(glassblock);
        glassblock.getBody().setTransform(x / AngryBirds.PPM, y / AngryBirds.PPM, 0);
        glassblock.getBody().setType(BodyDef.BodyType.DynamicBody);
    }

    private void createBird(Texture texture, float x, float y, float width, float height) {
        Bird bird = new Bird(texture);
        bird.createBody(world, width / AngryBirds.PPM, height / AngryBirds.PPM);
        birds.add(bird);
        bird.getBody().setTransform(x / AngryBirds.PPM, y / AngryBirds.PPM, 0);
        bird.getBody().setType(BodyDef.BodyType.StaticBody); // Set to StaticBody initially
        bird.setLinearDamping(0.75f); // Set linear damping
        bird.getBody().setFixedRotation(false); // Allow rotation
    }

    private void createMediumPig(Texture texture, float x, float y, float width, float height) {
        Pig mediumpig = new MediumPig(texture);
        mediumpig.createBody(world, width / AngryBirds.PPM, height / AngryBirds.PPM);
        pigs.add(mediumpig);
        mediumpig.getBody().setTransform(x / AngryBirds.PPM, y / AngryBirds.PPM, 0);
        mediumpig.getBody().setType(BodyDef.BodyType.DynamicBody);
        mediumpig.setLinearDamping(0.6f);
        mediumpig.getBody().setFixedRotation(false); // Allow rotation
    }

    private void createKingPig(Texture texture, float x, float y, float width, float height) {
        Pig kingpig = new KingPig(texture);
        kingpig.createBody(world, width / AngryBirds.PPM, height / AngryBirds.PPM);
        pigs.add(kingpig);
        kingpig.getBody().setTransform(x / AngryBirds.PPM, y / AngryBirds.PPM, 0);
        kingpig.getBody().setType(BodyDef.BodyType.DynamicBody);
        kingpig.setLinearDamping(0.6f);
        kingpig.getBody().setFixedRotation(false); // Allow rotation
    }

    private void drawGameObjects() {
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);

        for (Body body : bodies) {
            if (body.getUserData() instanceof Bird) {
                Bird bird = (Bird) body.getUserData();
                game.batch.draw(bird.getTexture(),
                    body.getPosition().x * AngryBirds.PPM - (bird.getWidth() * AngryBirds.PPM) / 2,
                    body.getPosition().y * AngryBirds.PPM - (bird.getHeight() * AngryBirds.PPM) / 2,
                    bird.getWidth() * AngryBirds.PPM, bird.getHeight() * AngryBirds.PPM);
            } else if (body.getUserData() instanceof Block) {
                Block block = (Block) body.getUserData();
                game.batch.draw(block.getTexture(),
                    body.getPosition().x * AngryBirds.PPM - (block.getWidth() * AngryBirds.PPM) / 2,
                    body.getPosition().y * AngryBirds.PPM - (block.getHeight() * AngryBirds.PPM) / 2,
                    block.getWidth() * AngryBirds.PPM, block.getHeight() * AngryBirds.PPM);
            } else if (body.getUserData() instanceof Pig) {
                Pig pig = (Pig) body.getUserData();
                game.batch.draw(pig.getTexture(),
                    body.getPosition().x * AngryBirds.PPM - (pig.getWidth() * AngryBirds.PPM) / 2,
                    body.getPosition().y * AngryBirds.PPM - (pig.getHeight() * AngryBirds.PPM) / 2,
                    pig.getWidth() * AngryBirds.PPM, pig.getHeight() * AngryBirds.PPM);
            } else if (body.getUserData() instanceof Catapult) {
                Catapult Catapult = (Catapult) body.getUserData();
                game.batch.draw(Catapult.getTexture(),
                    body.getPosition().x * AngryBirds.PPM - (Catapult.getWidth() * AngryBirds.PPM) / 2,
                    body.getPosition().y * AngryBirds.PPM - (Catapult.getHeight() * AngryBirds.PPM) / 2,
                    Catapult.getWidth() * AngryBirds.PPM, Catapult.getHeight() * AngryBirds.PPM);
            }
        }
    }
    private void checkWinLose(float delta) {
        if (isGameOver) {
            return;
        }
        // Check for win condition
        if (pigs.isEmpty()) {
            isGameOver = true;
            gameOverTime = 0;
        }

        if (birds.isEmpty() && currentBird == null && !pigs.isEmpty()) {
            isGameOver = true;
            gameOverTime = 0;
        }

    }

    private void setupNextBird() {
        if (!isBirdOnCatapult && !birds.isEmpty()) {
            currentBird = birds.remove(0);
            currentBird.getBody().setType(BodyDef.BodyType.StaticBody); // Set to StaticBody initially
            currentBird.getBody().setTransform(new Vector2(level.getCatapult().getX() / AngryBirds.PPM + 0.5f,
                level.getCatapult().getY() / AngryBirds.PPM + 3.5f), 0);
            isBirdOnCatapult = true;
        }
    }

    private void handleInput() {
        if (currentBird != null && !currentBird.isLaunched()) {
            if (Gdx.input.isTouched()) {
                Vector3 touchPos3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                game.getCamera().unproject(touchPos3D);
                Vector2 touchPos = new Vector2(touchPos3D.x, touchPos3D.y);

                if (!isDragging) {
                    if (currentBird.getBody().getFixtureList().first().testPoint(touchPos)) {
                        initialTouch.set(Gdx.input.getX(), Gdx.input.getY());
                        isDragging = true;
                    }
                } else {
                    Vector2 currentTouch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
                    Vector2 dragDistance = currentTouch.sub(initialTouch);
                    dragDistance.x = -dragDistance.x; // Invert the x-axis movement

                    float dragScaleFactor = 0.75f; // Adjust this value to scale the speed
                    dragDistance.scl(dragScaleFactor);

                    float maxDragLength = 1.6f; // Adjust this value to set the maximum drag length
                    if (dragDistance.len() > maxDragLength) {
                        dragDistance.setLength(maxDragLength);
                    }

                    Vector2 birdInitialPosition = new Vector2(level.getCatapult().getX() / AngryBirds.PPM + 0.5f,
                        level.getCatapult().getY() / AngryBirds.PPM + 3.5f);
                    currentBird.getBody().setTransform(birdInitialPosition.sub(dragDistance), 0);
                }
            } else if (isDragging) {
                isDragging = false;
                Vector2 birdInitialPosition = new Vector2(level.getCatapult().getX() / AngryBirds.PPM + 0.5f,
                    level.getCatapult().getY() / AngryBirds.PPM + 3.5f);
                Vector2 launchDirection = birdInitialPosition.sub(currentBird.getBody().getPosition());
                currentBird.getBody().setType(BodyDef.BodyType.DynamicBody); // Change to DynamicBody when launched
                launchBird(currentBird, launchDirection);
                isBirdOnCatapult = false;
            }
        }
    }

    private void launchBird(Bird bird, Vector2 launchDirection) {
        if (!bird.isLaunched()) {
            float launchForceMagnitude = launchDirection.len() * 75f;
            Vector2 launchForce = launchDirection.nor().scl(launchForceMagnitude);
            bird.getBody().applyLinearImpulse(launchForce, bird.getBody().getWorldCenter(), true);
            bird.setLaunched(true);
        }
    }

    private boolean hasBirdStopped(Bird bird) {
        float velocityThreshold = 0.05f;
        return bird.getBody().getLinearVelocity().len() < velocityThreshold;
    }

    private boolean isBirdOutOfScreen(Bird bird) {
        float birdX = bird.getBody().getPosition().x * AngryBirds.PPM;
        float birdY = bird.getBody().getPosition().y * AngryBirds.PPM;
        return birdX < -50 || birdX > Gdx.graphics.getWidth() + 50 || birdY < -50;
    }

    private void handleCollisions() {
        Array<Block> blocksToDestroy = collisionListener.getBlocksToDestroy();
        for (Block block : blocksToDestroy) {
            world.destroyBody(block.getBody());
            blocks.remove(block);
            level.getBlocks().remove(block);
        }

        Array<Pig> pigsToDestroy = collisionListener.getPigsToDestroy();
        for (Pig pig : pigsToDestroy) {
            world.destroyBody(pig.getBody());
            pigs.remove(pig);
            level.getPigs().remove(pig);
        }

        blocksToDestroy.clear();
        pigsToDestroy.clear();
    }

    public List<SavedState> getLevelData() {
        List<SavedState> data = new ArrayList<>();
        for (Bird bird : birds) {
            data.add(new SavedState("bird", bird.getBody().getPosition(), bird.getBody().getLinearVelocity(), bird.getHp(), true, bird.getTexture().toString(), bird.getDamage()));
        }
        for (Pig pig : pigs) {
            data.add(new SavedState("pig", pig.getBody().getPosition(), pig.getBody().getLinearVelocity(), pig.getHp(), true, pig.getTexture().toString(), 0));
        }
        for (Block block : blocks) {
            data.add(new SavedState("block", block.getBody().getPosition(), block.getBody().getLinearVelocity(), block.getHp(), true, block.getTexture().toString(), (int) block.getDamage()));
        }
        return data;
    }

    public void restoreLevelState(SaveLevel savedLevel) {
        // Restore slingshot position
        level.getCatapult().setPosition(savedLevel.slingshotPosition.x, savedLevel.slingshotPosition.y);

        // Clear current entities
        birds.clear();
        pigs.clear();
        blocks.clear();

        // Restore entities
        for (SavedState state : savedLevel.states) {
            switch (state.type) {
                case "bird":
                    Bird bird = new Bird(new Texture(Gdx.files.internal(state.texturePath)));
                    bird.createBody(world, state.position.x, state.position.y);
                    bird.getBody().setLinearVelocity(state.velocity);
                    bird.setHp(state.health);
                    birds.add(bird);
                    break;
                case "pig":
                    Pig pig = new Pig(new Texture(Gdx.files.internal(state.texturePath)), state.health);
                    pig.createBody(world, state.position.x, state.position.y);
                    pig.getBody().setLinearVelocity(state.velocity);
                    pigs.add(pig);
                    break;
                case "block":
                    Block block = new Block(new Texture(Gdx.files.internal(state.texturePath)), state.health, state.damage);
                    block.createBody(world, state.position.x, state.position.y);
                    block.getBody().setLinearVelocity(state.velocity);
                    blocks.add(block);
                    break;
            }
        }

        // Set the current bird in the slingshot
        if (savedLevel.currentBirdIndex >= 0 && savedLevel.currentBirdIndex < birds.size()) {
            currentBird = birds.get(savedLevel.currentBirdIndex);
            isBirdOnCatapult = true;
        } else {
            currentBird = null;
            isBirdOnCatapult = false;
        }
    }
}
