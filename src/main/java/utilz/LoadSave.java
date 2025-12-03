package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

    private static final String LEADERBOARD_FILE_NAME = "leaderboard.txt";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);

        try {
            if (is == null) {
                System.err.println("Could not load image: /" + fileName);
                return null;
            }
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return img;
    }

    public static int[][] GetLevelData(String levelFileName) {
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(levelFileName);
        if (img == null) {
            return lvlData;
        }
        for (int j = 0; j < (int) img.getHeight(); j++) {
            for (int i = 0; i < (int) img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 80) {
                    value = 80;
                }
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }

    public static int[][] GetLevelObstacleData(String levelFileName) {
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(levelFileName);
        if (img == null) {
            return lvlData;
        }
        for (int j = 0; j < (int) img.getHeight(); j++) {
            for (int i = 0; i < (int) img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value >= 80) {
                    value = 80;
                }
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }

    public static int[][] GetLevelObjData(String levelFileName) {
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(levelFileName);
        if (img == null) {
            return lvlData;
        }
        for (int j = 0; j < (int) img.getHeight(); j++) {
            for (int i = 0; i < (int) img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value >= 80) {
                    value = 80;
                }
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }

    private static Path getLeaderboardPath() {
        Path path = Paths.get("src", "main", "resources");

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path.resolve(LEADERBOARD_FILE_NAME);
    }

    public static void appendToScoreFile(String playerName, int levelIndex, double time, int deaths) {
        Path path = getLeaderboardPath();
        int humanLevel = levelIndex + 1;

        //; delimitter
        String line = playerName + ";" + humanLevel + ";" + deaths + ";" + String.format("%.4f", time);
        //monster code? yes, you are right, did not get it to work otherwise.
        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                java.nio.file.StandardOpenOption.CREATE,
                java.nio.file.StandardOpenOption.APPEND)) {
            writer.write(line);
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readScoreFile() {
        List<String> lines = new ArrayList<>();
        Path path = getLeaderboardPath();
        if (!Files.exists(path)) {
            return lines;
        }
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
