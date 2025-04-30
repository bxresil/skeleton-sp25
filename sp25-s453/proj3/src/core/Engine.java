// src/core/Engine.java
package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.Point;
import java.util.*;

/**
 * BYOW with Snake-style ambition: Player grows by collecting candies.
 */
public class Engine {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private static final int TARGET_LENGTH = 10; // win condition

    private TERenderer renderer;
    private TETile[][] world;
    private long seed;

    private Deque<Point> snakeBody;
    private int score;

    public Engine() {
        renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);
    }

    public void interactWithKeyboard() {
        Scanner scanner = new Scanner(System.in);
        printMainMenu();
        String input = scanner.nextLine().trim().toUpperCase();

        if (input.startsWith("N")) {
            seed = extractSeed(input);
            startNewGame();
            gameLoop();
        } else if (input.startsWith("L")) {
            SaveData data = SaveLoad.load();
            if (data != null) {
                this.seed = data.seed;
                world = World.generate(seed);
                snakeBody = new LinkedList<>();
                snakeBody.add(new Point(data.playerX, data.playerY));
                world[data.playerX][data.playerY] = Tileset.FLOWER;
                spawnCandies(5);
                score = data.score;
                renderer.renderFrame(world);
                gameLoop();
            } else {
                System.out.println("No save file found.");
            }
        } else {
            System.out.println("Goodbye.");
            System.exit(0);
        }
    }

    private void printMainMenu() {
        System.out.println("==== Welcome to SNAKE-BYOW ====");
        System.out.println("N - New Game");
        System.out.println("L - Load Game");
        System.out.println("Q - Quit");
        System.out.print("Enter your choice: ");
    }

    private long extractSeed(String input) {
        StringBuilder digits = new StringBuilder();
        for (int i = 1; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                digits.append(c);
            } else if (c == 'S') {
                break;
            }
        }
        return Long.parseLong(digits.toString());
    }

    private void startNewGame() {
        world = World.generate(seed);
        snakeBody = new LinkedList<>();
        score = 1;

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (world[x][y] != null && world[x][y].description().equals("floor")) {
                    snakeBody.add(new Point(x, y));
                    world[x][y] = Tileset.FLOWER;
                    spawnCandies(5);
                    renderer.renderFrame(world);
                    return;
                }
            }
        }
    }

    private void spawnCandies(int count) {
        Random rand = new Random(seed + score);
        int placed = 0;
        while (placed < count) {
            int x = rand.nextInt(WIDTH);
            int y = rand.nextInt(HEIGHT);
            if (world[x][y] != null && world[x][y].description().equals("floor")) {
                world[x][y] = Tileset.CANDY;
                placed++;
            }
        }
    }

    private void gameLoop() {
        StringBuilder commandHistory = new StringBuilder();

        while (true) {
            if (edu.princeton.cs.algs4.StdDraw.hasNextKeyTyped()) {
                char key = Character.toUpperCase(edu.princeton.cs.algs4.StdDraw.nextKeyTyped());
                commandHistory.append(key);

                if (commandHistory.toString().endsWith(":Q")) {
                    saveGame();
                    System.exit(0);
                } else if ("WASD".indexOf(key) >= 0) {
                    moveSnake(key);
                }

                if (score >= TARGET_LENGTH) {
                    System.out.println("You win! Final length: " + score);
                    System.exit(0);
                }

                renderer.renderFrame(world);
                HUD.draw(world);
            }
        }
    }

    private void moveSnake(char direction) {
        Point head = snakeBody.peekLast();
        int x = head.x;
        int y = head.y;

        switch (direction) {
            case 'W': y++; break;
            case 'S': y--; break;
            case 'A': x--; break;
            case 'D': x++; break;
        }

        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) return;
        if (!world[x][y].description().equals("floor") && !world[x][y].description().equals("candy")) return;

        Point newHead = new Point(x, y);
        snakeBody.addLast(newHead);
        boolean isCandy = world[x][y].description().equals("candy");
        world[x][y] = Tileset.FLOWER;

        if (!isCandy) {
            Point tail = snakeBody.removeFirst();
            world[tail.x][tail.y] = Tileset.FLOOR;
        } else {
            score++;
            spawnCandies(1);
        }
    }

    private void saveGame() {
        Point head = snakeBody.peekLast();
        SaveData data = new SaveData(seed, head.x, head.y, score);
        SaveLoad.save(data);
        System.out.println("Game saved.");
    }

    public String interactWithInputString(String input) {
        return "";
    }
}
