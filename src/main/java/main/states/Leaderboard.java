package main.states;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import main.Game;
import utilz.LoadSave;

public class Leaderboard {
    private Game game;
    private Font titleFont = new Font("Arial", Font.BOLD, 48);
    private Font headerFont = new Font("Arial", Font.BOLD, 24);
    private Font rowFont = new Font("Arial", Font.PLAIN, 20);

    public Leaderboard(Game game) {
        this.game = game;
    }

    public void update() {
        //nothing dynamic yet
    }

    private static class Entry {
        final String name;
        final int level;
        final int deaths;
        final double timeMs;

        Entry(String name, int level, int deaths, double timeMs) {
            this.name = name;
            this.level = level;
            this.deaths = deaths;
            this.timeMs = timeMs;
        }
    }

    private List<Entry> loadAndSortEntries() {
        List<String> lines = LoadSave.readScoreFile();
        List<Entry> entries = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length < 4) continue;
            try {
                String name = parts[0];
                int level = Integer.parseInt(parts[1]);
                int deaths = Integer.parseInt(parts[2]);
                double time = Double.parseDouble(parts[3]);
                entries.add(new Entry(name, level, deaths, time));
            } catch (NumberFormatException ignored) {
            }
        }
        // Sort by: highest level, then fewest deaths, then lowest time
        entries.sort(Comparator
                .comparingInt((Entry e) -> -e.level)
                .thenComparingInt(e -> e.deaths)
                .thenComparingDouble(e -> e.timeMs));
        return entries;
    }

    public void draw(Graphics g) {
        // background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        // title
        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        String title = "LEADERBOARD";
        FontMetrics fm = g.getFontMetrics();
        int tx = (Game.GAME_WIDTH - fm.stringWidth(title)) / 2;
        g.drawString(title, tx, 100);

        // headers
        g.setFont(headerFont);
        int startX = 100;
        int startY = 160;
        int colRank = startX;
        int colName = startX + 80;
        int colLevel = startX + 260;
        int colDeaths = startX + 360;
        int colTime = startX + 480;

        g.drawString("#", colRank, startY);
        g.drawString("Name", colName, startY);
        g.drawString("Level", colLevel, startY);
        g.drawString("Deaths", colDeaths, startY);
        g.drawString("Time (ms)", colTime, startY);

        // rows
        g.setFont(rowFont);
        List<Entry> entries = loadAndSortEntries();
        int rowY = startY + 30;
        int maxRows = 10;
        for (int i = 0; i < entries.size() && i < maxRows; i++) {
            Entry e = entries.get(i);
            g.drawString(String.valueOf(i + 1), colRank, rowY);
            g.drawString(e.name, colName, rowY);
            g.drawString(String.valueOf(e.level), colLevel, rowY);
            g.drawString(String.valueOf(e.deaths), colDeaths, rowY);
            g.drawString(String.format("%.4f", e.timeMs), colTime, rowY);
            rowY += 26;
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Press ESC to return", 20, Game.GAME_HEIGHT - 30);
    }
}
