package core;

import tileengine.TERenderer;
import tileengine.TETile;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            // 3A demo: generate a world with a fixed seed
            long seed = 123456789L;
            TERenderer renderer = new TERenderer();
            renderer.initialize(80, 40);
            TETile[][] world = World.generate(seed);
            renderer.renderFrame(world);
        } else if (args[0].equalsIgnoreCase("interactive")) {
            // 3B demo: keyboard interactivity
            Engine engine = new Engine();
            engine.interactWithKeyboard();
        } else {
            // 3B input-string mode for autograder
            Engine engine = new Engine();
            engine.interactWithInputString(args[0]);
        }
    }
}
