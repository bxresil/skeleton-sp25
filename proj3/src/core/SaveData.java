package core;

import java.io.Serializable;

/**
 * Stores the saved state of the game:
 * - seed for world generation
 * - player position (x, y)
 */
public class SaveData implements Serializable {
    public long seed;
    public int playerX;
    public int playerY;

    public int score;
    public SaveData(long seed, int playerX, int playerY, int score) {
        this.seed = seed;
        this.playerX = playerX;
        this.playerY = playerY;
        this.score = score;
    }


}
