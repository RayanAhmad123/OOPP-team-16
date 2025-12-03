package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Game;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }


    @Override
    public void keyTyped(KeyEvent e) {
        // when editing name in main menu, collect characters here
        if (gamePanel.getGame().getGameState() == Game.GameState.MENU &&
            gamePanel.getGame().mainMenu != null &&
            gamePanel.getGame().mainMenu.isEditingName()) {
            gamePanel.getGame().mainMenu.handleNameKeyPressed(0, e.getKeyChar());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gamePanel.getGame().getGameState() == Game.GameState.MENU &&
            gamePanel.getGame().mainMenu != null &&
            gamePanel.getGame().mainMenu.isEditingName()) {
            int code = e.getKeyCode();
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_BACK_SPACE) {
                gamePanel.getGame().mainMenu.handleNameKeyPressed(code, '\0');
            }
            return;
        }

        switch (e.getKeyCode()) {
        case KeyEvent.VK_A:
        case KeyEvent.VK_LEFT:
            gamePanel.getGame().getPlayer().setLeft(true);
            break;
        case KeyEvent.VK_D:
        case KeyEvent.VK_RIGHT:
            gamePanel.getGame().getPlayer().setRight(true);
            break;
        case KeyEvent.VK_SPACE:
        case KeyEvent.VK_UP:
        case KeyEvent.VK_W:
            //Jump sound
            gamePanel.getGame().getAudioController().playJump();
            gamePanel.getGame().getPlayer().setJump(true);
            break;
        case KeyEvent.VK_P:
            // toggle pause when pressing 'P'
            gamePanel.getGame().togglePause();
            break;
        case KeyEvent.VK_ESCAPE:
            gamePanel.getGame().setGameState(Game.GameState.MENU);
            break;
        default:
            // do nothing for unhandled keys
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gamePanel.getGame().getGameState() == Game.GameState.MENU &&
            gamePanel.getGame().mainMenu != null &&
            gamePanel.getGame().mainMenu.isEditingName()) {
            return;
        }

        switch (e.getKeyCode()) {
        case KeyEvent.VK_A:
        case KeyEvent.VK_LEFT:
            gamePanel.getGame().getPlayer().setLeft(false);
            break;
        case KeyEvent.VK_D:
        case KeyEvent.VK_RIGHT:
            gamePanel.getGame().getPlayer().setRight(false);
            break;
        case KeyEvent.VK_SPACE:
        case KeyEvent.VK_UP:
        case KeyEvent.VK_W:
            gamePanel.getGame().getPlayer().setJump(false);
            break;
        default:
            //do nothing
            break;
        }
    }
}
