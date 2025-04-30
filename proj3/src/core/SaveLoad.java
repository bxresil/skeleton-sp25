package core;

import java.io.*;

/**
 * Provides static methods to save and load game data.
 */
public class SaveLoad {
    private static final String FILE_NAME = "savefile.dat";

    /** Saves the game state to a file */
    public static void save(SaveData data) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(data);
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    /** Loads and returns game state from file, or null if error */
    public static SaveData load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (SaveData) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading save: " + e.getMessage());
            return null;
        }
    }
}
