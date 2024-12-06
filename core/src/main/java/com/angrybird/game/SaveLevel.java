package com.angrybird.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// SaveLevel.java
public class SaveLevel {

    public List<SavedState> states; // List of all game objects (birds, pigs, blocks)
    public Vector2 slingshotPosition; // Position of the slingshot
    public int currentBirdIndex;      // Index of the bird currently in the slingshot

    public SaveLevel() {
        states = new ArrayList<>();
    }

    // Save the game state to a JSON file
    public void saveToFile(String filePath) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.prettyPrint(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the game state from a JSON file
    public static SaveLevel loadFromFile(String filePath) {
        Json json = new Json();
        try (FileReader reader = new FileReader(filePath)) {
            return json.fromJson(SaveLevel.class, reader);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to get the current level and retrieve data
    public void retrieveDataFromCurrentLevel(AngryBirds game) {
        Screen currentLevel = game.getCurrentLevel();
        if (currentLevel instanceof Level1) {
            Level1 level1 = (Level1) currentLevel;
            this.states = level1.getLevelData();
        } else if (currentLevel instanceof Level2) {
            Level2 level2 = (Level2) currentLevel;
            this.states = level2.getLevelData();
        } else if (currentLevel instanceof Level3) {
            Level3 level3 = (Level3) currentLevel;
            this.states = level3.getLevelData();
        }
    }
}
