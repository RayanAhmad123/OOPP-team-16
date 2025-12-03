package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;

public class LoadSave {

    public static final String PLAYER_ATLAS = "Character.png";
    public static final String PLAYER_DEAD = "Dead.png";
    public static final String LEVEL_ATLAS = "MAP_TILES.png";
    public static final String OBJECT_ATLAS = "OBJ_TILES.png";

    public static final String BG_DATA = "Background.png";
    public static final String TRANSITION_IMG = "Transition.png";
    public static final String SPAWN_TUBE = "SpawnTube.png";
    public static final String LOCK = "Lock.png";

    //LEVEL1
    public static final String LEVEL_ONE_DATA = "Level1.png";
    public static final String LEVEL_ONE_OBSTACLE_DATA = "Level1Obstacles.png";
    public static final String LEVEL_ONE_OBJ_DATA = "Level1Objects.png";
    //LEVEL2
    public static final String LEVEL_TWO_DATA = "Level2.png";
    public static final String LEVEL_TWO_OBSTACLE_DATA = "Level2Obstacles.png";
    public static final String LEVEL_TWO_OBJ_DATA = "Level2Objects.png";
    //LEVEL3
    public static final String LEVEL_THREE_DATA = "Level3.png";
    public static final String LEVEL_THREE_OBSTACLE_DATA = "Level3Obstacles.png";
    public static final String LEVEL_THREE_OBJ_DATA = "Level3Objects.png";
    //LEVEL4
    public static final String LEVEL_FOUR_DATA = "Level4.png";
    public static final String LEVEL_FOUR_OBSTACLE_DATA = "Level4Obstacles.png";
    public static final String LEVEL_FOUR_OBJ_DATA = "Level4Objects.png";
    //LEVEL5
    public static final String LEVEL_FIVE_DATA = "Level5.png";
    public static final String LEVEL_FIVE_OBSTACLE_DATA = "Level5Obstacles.png";
    public static final String LEVEL_FIVE_OBJ_DATA = "Level5Objects.png";
    //LEVEL6
    public static final String LEVEL_SIX_DATA = "Level6.png";
    public static final String LEVEL_SIX_OBSTACLE_DATA = "Level6Obstacles.png";
    public static final String LEVEL_SIX_OBJ_DATA = "Level6Objects.png";
    //LEVEL7
    public static final String LEVEL_SEVEN_DATA = "Level7.png";
    public static final String LEVEL_SEVEN_OBSTACLE_DATA = "Level7Obstacles.png";
    public static final String LEVEL_SEVEN_OBJ_DATA = "Level7Objects.png";
    
    public static BufferedImage GetSpriteAtlas(String fileName){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);

        try {
            img = ImageIO.read(is);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static int[][] GetLevelData(String levelFileName){
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(levelFileName);


        for (int j = 0; j < (int)img.getHeight(); j++) {
           for (int i = 0; i < (int)img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 80 ) {
                    value = 80;
                }
                lvlData[j][i] = value;
           } 
        }
        return lvlData;
    }

    public static int[][] GetLevelObstacleData(String levelFileName){
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(levelFileName);


        for (int j = 0; j < (int)img.getHeight(); j++) {
           for (int i = 0; i < (int)img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value >= 80 ) {
                    value = 80;
                }
                lvlData[j][i] = value;
           } 
        }
        return lvlData;
    }

        public static int[][] GetLevelObjData(String levelFileName){
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(levelFileName);


        for (int j = 0; j < (int)img.getHeight(); j++) {
           for (int i = 0; i < (int)img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value >= 80 ) {
                    value = 80;
                }
                lvlData[j][i] = value;
           } 
        }
        return lvlData;
    }

}
