package Levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.Game;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;
import static main.Game.TILES_SIZE;
import utilz.LoadSave;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private BufferedImage[] objectSprite;
    private List<Level> levels;
    private int currentLevelIndex = 0;


    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        buildAllLevels();
    }
    
    private void buildAllLevels() {
        levels = new ArrayList<>();
        
        // Level 1
        Level level1 = new Level(
            LoadSave.GetLevelData(LoadSave.LEVEL_ONE_DATA),
            LoadSave.GetLevelObstacleData(LoadSave.LEVEL_ONE_OBSTACLE_DATA),
            LoadSave.GetLevelObjData(LoadSave.LEVEL_ONE_OBJ_DATA),
            200, 550);
        level1.createTriggerPlatformsFromTile(1,73, 0, 200, 2f, levelSprite, true, false);
        level1.createTriggerPlatformsFromTile(2,72, 100, 50, 2f, levelSprite, true, true);
        // Create spikes from tile ID 3, using sprite 74 (adjust sprite ID to your spike sprite)
        level1.createSpikesFromTile(3, 41, objectSprite);
        levels.add(level1);
        
        // Level 2
        //Level level2 = new Level(LoadSave.GetLevelData(LoadSave.LEVEL_TWO_DATA), 100, 400);
        // Example: create trigger platforms from tile ID 50 in level 2
        // level2.createTriggerPlatformsFromTile(50, 100, 0, 1.5f, levelSprite);
        //levels.add(level2);
        
        // Level 3
        //Level level3 = new Level(LoadSave.GetLevelData(LoadSave.LEVEL_THREE_DATA), 100, 100);
        //levels.add(level3);
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS); 
        levelSprite = new BufferedImage[81];
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                int index = j*9 + i;
                levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }
        
        // Load decorational object sprites
        BufferedImage objImg = LoadSave.GetSpriteAtlas(LoadSave.OBJECT_ATLAS);
        objectSprite = new BufferedImage[48];
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 8; i++) {
                int index = j*8 + i;
                objectSprite[index] = objImg.getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }

    public void draw(Graphics g){
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.BG_DATA);
        g.drawImage(img, 0, 0,GAME_WIDTH,(int)(GAME_HEIGHT * 0.9f) , null); 
        
        Level currentLevel = getCurrentLvl();
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < Game.TILES_IN_WIDTH; i++) {
                int index = currentLevel.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i*TILES_SIZE, j*TILES_SIZE,TILES_SIZE,TILES_SIZE, null);
            }
        }
        
        // Draw decorational objects
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < Game.TILES_IN_WIDTH; i++) {
                int index = currentLevel.getObjectSpriteIndex(i, j);
                if (index > 0 && index < objectSprite.length) {
                    g.drawImage(objectSprite[index], i*TILES_SIZE, j*TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
                }
            }
        }
        
        // Draw moving platforms and spikes
        currentLevel.drawPlatforms(g);
        currentLevel.drawSpikes(g);
    }

    public void update(){
        // Update moving platforms (pass player for trigger detection)
        getCurrentLvl().updatePlatforms(game.getPlayer());
    }

    public Level getCurrentLvl() {
        return levels.get(currentLevelIndex);
    }
    
    public void loadNextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
            System.out.println("Loading level " + (currentLevelIndex + 1));
        } else {
            System.out.println("No more levels! Game complete.");
            // TODO: Handle game completion (e.g., show victory screen)
        }
    }
    
    public int getLevelCount() {
        return levels.size();
    }
    
    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public void setLevelScore(int death){
        Level currLevel = levels.get(getCurrentLevelIndex());
        currLevel.updateDeathScore(death);
    }
    
    public BufferedImage getSprite(int tileId) {
        if (tileId >= 0 && tileId < levelSprite.length) {
            return levelSprite[tileId];
        }
        return levelSprite[0];
    }
}
