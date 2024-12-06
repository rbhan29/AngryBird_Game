package com.angrybird.game;

import com.badlogic.gdx.math.Vector2;

public class SavedState {

    public String type;         // Type of entity (e.g., "bird", "pig", "block")
    public Vector2 position;    // Position in the game world
    public Vector2 velocity;    // Velocity in case it's moving
    public float health;        // Health for destructible entities
    public boolean isActive;    // Whether the entity is active (e.g., pig not destroyed)
    public String texturePath;  // Path to the texture
    public int damage;          // Damage value for blocks

    public SavedState() {
    }

    public SavedState(String type, Vector2 position, Vector2 velocity, float health, boolean isActive, String texturePath, int damage) {
        this.type = type;
        this.position = position;
        this.velocity = velocity;
        this.health = health;
        this.isActive = isActive;
        this.texturePath = texturePath;
        this.damage = damage;
    }
}
