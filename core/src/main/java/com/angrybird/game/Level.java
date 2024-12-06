package com.angrybird.game;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.Texture;

public class Level {
    private List<Bird> birds;
    private List<Block> blocks;
    private List<Pig> pigs;
    private Catapult catapult;

    public Level() {
        birds = new ArrayList<>();
        blocks = new ArrayList<>();
        pigs = new ArrayList<>();
        catapult = new Catapult(new Texture("catapault.png"));
    }

    public List<Bird> getBirds() {
        return birds;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Pig> getPigs() {
        return pigs;
    }

    public Catapult getCatapult() {
        return catapult;
    }
}
