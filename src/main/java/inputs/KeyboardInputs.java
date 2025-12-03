package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Game;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;
    private boolean keyDown = false;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
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
            if (!keyDown) {
                gamePanel.getGame().getAudioController().playJump();
                keyDown = true;
            }
            gamePanel.getGame().getPlayer().setJump(true);
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
            keyDown = false; 
            gamePanel.getGame().getPlayer().setJump(false);
            break;
        default:
            //do nothing
            break;
        }
    }
}
