package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Levels.LevelManager;
import audio.controller.AudioController;
import entities.Player;
import main.states.Leaderboard;
import main.states.MainMenu;
import utilz.LoadSave;

public class Game implements Runnable {

    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private Thread gametThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Player player;
    private LevelManager levelManager;
    private AudioController audioController; // audio

    public final static int TILES_DEAFULT_SIZE = 32;
    public final static float SCALE = 1.0f;
    public final static int TILES_IN_WIDTH = 40;
    public final static int TILES_IN_HEIGHT = 25;
    public final static int TILES_SIZE = (int) (TILES_DEAFULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public enum GameState {MENU, PLAYING, LEADERBOARD}

    private GameState gameState = GameState.MENU;

    public MainMenu mainMenu;
    public Leaderboard leaderboard;

    // Level transition
    private BufferedImage transitionImage;
    private boolean inTransition = false;
    private float transitionScale = 2f;
    private boolean scalingUp = true;
    private boolean levelLoaded = false;
    private final float TRANSITION_SPEED = 0.015f;
    
    // Track player death for platform reset
    private boolean playerWasDead = false;

    // Player name
    private String playerName = "Player1";

    // Run tracking
    private long runStartTimeNanos;
    private int totalDeathsForRun;

    // Pause flag that when true, gameplay updates are frozen
    private boolean paused = false;

    public Game() {
        audioController = new AudioController();
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        //menu music
        audioController.playMenuMusic();

        startGameLoop();
    }

    private void initClasses() {
        levelManager = new LevelManager(this);
        player = new Player(200, 550, (int) (32 * SCALE), (int) (32 * SCALE));
        player.setGame(this);
        loadPlayerForCurrentLevel();

        transitionImage = LoadSave.GetSpriteAtlas(LoadSave.TRANSITION_IMG);

        mainMenu = new MainMenu(this);
        leaderboard = new Leaderboard(this);
    }

    private void loadPlayerForCurrentLevel() {
        Levels.Level currentLevel = levelManager.getCurrentLvl();
        player.setSpawnPoint(currentLevel.getSpawnX(), currentLevel.getSpawnY());
        player.loadLvlData(currentLevel.getLevelData());
        player.setCurrentLevel(currentLevel);
        player.spawnAtLevelStart();
        currentLevel.resetPlatforms();
        currentLevel.clearDeathPositions();
    }

    private void update() {
        if (inTransition) {
            updateTransition();
            return;
        }

        switch (gameState) {
        case PLAYING:
            if (paused) {
                // When paused, skip updating player and level
                return;
            }
            boolean playerCurrentlyDead = player.getHitbox().x > 1500;
            if (!playerWasDead && playerCurrentlyDead) {
                // Player just died
                levelManager.getCurrentLvl().triggerSpawnPlatform();
            }
            if (playerWasDead && !playerCurrentlyDead) {
                // Player just respawned
                levelManager.getCurrentLvl().resetPlatforms();
            }
            playerWasDead = playerCurrentlyDead;

            player.update();
            levelManager.update();
            if (player.hasReachedLevelEnd()) {
                startLevelTransition();
            }
            break;
        case MENU:
            mainMenu.update();
            break;
        case LEADERBOARD:
            leaderboard.update();
            break;
        }
    }

    private void startLevelTransition() {
        inTransition = true;
        scalingUp = true;
        levelLoaded = false;
        transitionScale = 0f;
        player.resetLevelEnd();
    }

    private void updateTransition() {
        if (scalingUp) {
            transitionScale += TRANSITION_SPEED;
            if (transitionScale >= 2f) {
                transitionScale = 2f;
                if (!levelLoaded) {
                    // Save score for current level before loading next
                    levelManager.setLevelScore(player.getDeathCount());
                    player.resetDeathCount();
                    // Load the next level while screen is covered
                    levelManager.loadNextLevel();
                    loadPlayerForCurrentLevel();
                    levelLoaded = true;
                }
                scalingUp = false;
            }
        } else {
            transitionScale -= TRANSITION_SPEED;
            if (transitionScale <= 0f) {
                transitionScale = 0f;
                inTransition = false;
            }
        }
    }

    public void render(Graphics g) {
        switch (gameState) {
        case PLAYING:
            levelManager.draw(g);
            levelManager.drawObjectLayer(g); // Draw object layer on top of everything except player
            player.render(g); // Player on top of object layer
            levelManager.getCurrentLvl().drawSpawnPlatform(g); // Draw in front of player
            drawHUD(g);

            // If paused, draw overlay on top of gameplay
            if (paused) {
                drawPauseOverlay(g);
            }
            break;
        case MENU:
            mainMenu.draw(g);
            break;
        case LEADERBOARD:
            leaderboard.draw(g);
            break;
        }

        // Draw transition overlay on top
        if (inTransition && transitionImage != null) {
            drawTransition(g);
        }
    }

    private void drawTransition(Graphics g) {
        // Scale from 0 to cover the entire screen
        int scaledWidth = (int) (GAME_WIDTH * transitionScale * 1.5f);
        int scaledHeight = (int) (GAME_HEIGHT * transitionScale * 1.5f);

        // Center the image
        int x = (GAME_WIDTH - scaledWidth) / 2;
        int y = (GAME_HEIGHT - scaledHeight) / 2;

        g.drawImage(transitionImage, (int) (player.getHitbox().x - (scaledWidth / 2)), (int) player.getHitbox().y - (scaledHeight / 2), scaledWidth, scaledHeight, null);
    }

    private void drawHUD(Graphics g) {
        //Background
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(10, 10, 200, 60, 10, 10);

        //Text fonting
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Level: " + (levelManager.getCurrentLevelIndex() + 1), 20, 35);
        g.drawString("Deaths: " + player.getDeathCount(), 20, 55);
    }

    private void drawPauseOverlay(Graphics g) {
        // Semi-transparent black overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        //final centered "PAUSED" text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String text = "PAUSED";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int x = (GAME_WIDTH - textWidth) / 2;
        int y = (GAME_HEIGHT - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);

        //hint: "press to resume" text
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String hint = "Press P to resume game";
        fm = g.getFontMetrics();
        int hintWidth = fm.stringWidth(hint);
        int hx = (GAME_WIDTH - hintWidth) / 2;
        int hy = y + 40;
        g.drawString(hint, hx, hy);
    }

    private void startGameLoop() {
        gametThread = new Thread(this);
        gametThread.start();
    }


    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }
            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + "UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public GameState getGameState() {
        return gameState;
    }

    public AudioController getAudioController() {
        return audioController;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        if (playerName != null && !playerName.isBlank()) {
            this.playerName = playerName.trim();
        }
    }

    public void setGameState(GameState newState) {
        GameState oldState = this.gameState;
        this.gameState = newState;

        // Going back to main menu will reset
        if (newState == GameState.MENU && oldState == GameState.PLAYING) {
            //reset level index
            levelManager.resetToFirstLevel();
            //reset run stats
            totalDeathsForRun = 0;
            runStartTimeNanos = 0L;
            //reload player
            loadPlayerForCurrentLevel();
        }

        // If we are starting to play from the menu, start a fresh run (timer & deaths)
        // Speedfix to allow the game to reset if going back to mainmenu.
        if (newState == GameState.PLAYING && oldState == GameState.MENU) {
            totalDeathsForRun = 0;
            runStartTimeNanos = System.nanoTime();
        }

        switch (newState) {
        case MENU -> audioController.playMenuMusic();
        case PLAYING -> audioController.playGameMusic();
        case LEADERBOARD -> audioController.stopAll();
        }
    }

    public void onPlayerDeath() {
        totalDeathsForRun++;
    }

    public void onLevelCompleted() {
        long runEndTimeNanos = System.nanoTime();
        double timeMs = (runEndTimeNanos - runStartTimeNanos) / 1000000.0;
        int levelIndex = levelManager.getCurrentLevelIndex();
        LoadSave.appendToScoreFile(playerName, levelIndex, timeMs, totalDeathsForRun);
    }

    public void togglePause() {
        if (gameState == GameState.PLAYING && !inTransition) {
            paused = !paused;
        }
    }
}
