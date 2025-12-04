classDiagram
direction BT
class Game {
  + Game() 
  - GameState gameState
  - Player player
  - AudioController audioController
  - LevelManager levelManager
  - update() void
  + windowFocusLost() void
  - loadPlayerForCurrentLevel() void
  + render(Graphics) void
  + onPlayerDeath() void
  + levelCompletedScoringUpdate() void
  - initClasses() void
  - startGameLoop() void
  + renderGame(Graphics) void
  + reloadPlayerForCurrentLevel() void
  + updateGameState() void
  + run() void
  + togglePause() void
   GameState gameState
   Player player
   String playerName
   AudioController audioController
   LevelManager levelManager
}
class GameModel {
  + GameModel(Player, LevelManager) 
  - long runStartTimeNanos
  - GameState gameState
  - boolean inTransition
  - boolean paused
  - float transitionScale
  - LevelManager levelManager
  - int totalDeathsForRun
  - String playerName
  - Player player
  + checkIsEndOfLevel() boolean
  + togglePause() void
  + checkIsDead() boolean
  + checkIsRespawn() boolean
  + startNewRunTimer() void
  + onPlayerDeath() void
  + updateTransition() void
  + startLevelTransition() void
  + updatePlaying() void
  + resetRunStats() void
   long runStartTimeNanos
   boolean inTransition
   float transitionScale
   String playerName
   int totalDeathsForRun
   LevelManager levelManager
   GameState gameState
   Player player
   boolean paused
}
class GamePanel {
  + GamePanel(Game) 
  - Game game
  - setPanelSize() void
  + updateGame() void
  + paintComponent(Graphics) void
   Game game
}
class GameView {
  + GameView(GameModel) 
  - drawHUD(Graphics) void
  - drawPauseOverlay(Graphics) void
  + renderGame(Graphics) void
  + renderTransition(Graphics, BufferedImage) void
}
class GameWindow {
  + GameWindow(GamePanel) 
}
class MainClass {
  + MainClass() 
  + main(String[]) void
}
class node1
class node3

Game  ..>  GameModel : «create»
Game "1" *--> "model 1" GameModel 
Game  ..>  GamePanel : «create»
Game "1" *--> "gamePanel 1" GamePanel 
Game "1" *--> "view 1" GameView 
Game  ..>  GameView : «create»
Game  ..>  GameWindow : «create»
Game "1" *--> "gameWindow 1" GameWindow 
GamePanel "1" *--> "game 1" Game 
GameView "1" *--> "model 1" GameModel 
MainClass  ..>  Game : «create»
