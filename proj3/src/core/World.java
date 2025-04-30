package core;

import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Utility class to generate random worlds.
 * Creates rooms and hallways based on a seed, then adds walls around them.
 */
public class World {
    private static final int WIDTH = 80;   // world width in tiles
    private static final int HEIGHT = 40;  // world height in tiles
    private static Random randGen;         // random number generator

    /**
     * Generates a new world based on the given seed.
     */
    public static TETile[][] generate(long seed) {
        randGen = new Random(seed);                         // initialize RNG with seed
        TETile[][] tileGrid = new TETile[WIDTH][HEIGHT];    // grid to hold tiles

        // fill the grid with nothing tiles
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tileGrid[x][y] = Tileset.NOTHING;
            }
        }

        // choose a random number of rooms between 6 and 11
        int numRooms = RandomUtils.uniform(randGen, 6, 12);
        List<Room> rooms = new ArrayList<>();               // list to store room data

        // generate rooms and fill them with floor tiles
        for (int i = 0; i < numRooms; i++) {
            int roomWidth = RandomUtils.uniform(randGen, 3, 10);
            int roomHeight = RandomUtils.uniform(randGen, 3, 8);
            int roomX = RandomUtils.uniform(randGen, 1, WIDTH - roomWidth - 1);
            int roomY = RandomUtils.uniform(randGen, 1, HEIGHT - roomHeight - 1);
            Room room = new Room(roomX, roomY, roomWidth, roomHeight);
            rooms.add(room);                                 // keep track of rooms

            // fill the room area with floor
            for (int x = roomX; x < roomX + roomWidth; x++) {
                for (int y = roomY; y < roomY + roomHeight; y++) {
                    tileGrid[x][y] = Tileset.FLOOR;
                }
            }
        }

        // sort rooms by centerX to connect them in order
        Collections.sort(rooms, (r1, r2) -> Integer.compare(r1.centerX, r2.centerX));
        for (int i = 1; i < rooms.size(); i++) {
            Room prev = rooms.get(i - 1);
            Room curr = rooms.get(i);
            connectRooms(tileGrid, prev, curr);
        }

        // add walls around all floor tiles
        addWalls(tileGrid);
        return tileGrid;
    }

    /**
     * Connects two rooms with an L-shaped hallway.
     */
    private static void connectRooms(TETile[][] tileGrid, Room a, Room b) {
        int x1 = a.centerX;
        int y1 = a.centerY;
        int x2 = b.centerX;
        int y2 = b.centerY;

        // draw horizontal corridor
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            tileGrid[x][y1] = Tileset.FLOOR;
        }
        // draw vertical corridor
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            tileGrid[x2][y] = Tileset.FLOOR;
        }
    }

    /**
     * Surrounds each floor tile with walls if the adjacent tile is empty.
     */
    private static void addWalls(TETile[][] tileGrid) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (tileGrid[x][y] == Tileset.FLOOR) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int nx = x + dx;
                            int ny = y + dy;
                            if (nx >= 0 && nx < WIDTH && ny >= 0 && ny < HEIGHT) {
                                if (tileGrid[nx][ny] == Tileset.NOTHING) {
                                    tileGrid[nx][ny] = Tileset.WALL;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Helper class for room data.
     * Stores position and center of a room.
     */
    private static class Room {
        int roomX;     // bottom-left x coordinate
        int roomY;     // bottom-left y coordinate
        int roomWidth; // width of the room
        int roomHeight;// height of the room
        int centerX;   // x coordinate of room center
        int centerY;   // y coordinate of room center

        Room(int x, int y, int w, int h) {
            roomX = x;
            roomY = y;
            roomWidth = w;
            roomHeight = h;
            centerX = x + w / 2;
            centerY = y + h / 2;
        }
    }
}
