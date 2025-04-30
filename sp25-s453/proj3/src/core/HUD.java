package core;

import tileengine.TETile;
import edu.princeton.cs.algs4.StdDraw;

/** Displays tile description under the mouse. */
public class HUD {
    public static void draw(TETile[][] world) {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        int ix = (int) x;
        int iy = (int) y;

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(1, 1, getDescription(ix, iy, world));
        StdDraw.show();
    }

    private static String getDescription(int x, int y, TETile[][] world) {
        if (x < 0 || x >= world.length || y < 0 || y >= world[0].length) {
            return "";
        }
        TETile tile = world[x][y];
        if (tile == null) {
            return "";
        }
        return "Tile: " + tile.description();
    }
}
