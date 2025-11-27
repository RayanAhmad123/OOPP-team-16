package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TriggerPlatform extends Entity {
    
    private float startX, startY;
    private float targetX, targetY;
    private float speed;
    private boolean triggered = false;
    private boolean reachedTarget = false;
    private BufferedImage sprite;
    private boolean shouldReturn;
    private boolean movingToTarget = true; // true = moving to target, false = returning to start
    private int spriteWidth, spriteHeight; // Original sprite size
    private float spriteOffsetX, spriteOffsetY; // Offset to center sprite in hitbox
    private boolean solid = false; // If true, player can stand on this platform
    
    public TriggerPlatform(float x, float y, float targetX, float targetY, 
                           int width, int height, float speed, BufferedImage sprite, boolean shouldReturn) {
        super(x, y, width, height);
        this.startX = x;
        this.startY = y;
        this.targetX = targetX;
        this.targetY = targetY;
        this.speed = speed;
        this.sprite = sprite;
        this.shouldReturn = shouldReturn;
        this.spriteWidth = width;
        this.spriteHeight = height;
        initHitbox(x, y, width, height);
        // Enlarge hitbox but keep sprite size the same
        setHitboxSize(width*2, height*2, (int)(x - width/2), (int)(y - height/2));
    }
    
    public void update() {
        if (!triggered || reachedTarget) {
            return;
        }
        
        // Determine current destination
        float destX = movingToTarget ? targetX : startX;
        float destY = movingToTarget ? targetY : startY;
        
        float dirX = destX - hitbox.x;
        float dirY = destY - hitbox.y;
        float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        
        if (distance < speed) {
            // Reached destination
            hitbox.x = destX;
            hitbox.y = destY;
            
            if (movingToTarget && shouldReturn) {
                // Reached target, now return to start
                movingToTarget = false;
            } else {
                // Done moving
                reachedTarget = true;
            }
        } else {
            // Move towards destination
            hitbox.x += (dirX / distance) * speed;
            hitbox.y += (dirY / distance) * speed;
        }
    }
    
    public void render(Graphics g) {
        // Draw sprite centered in the hitbox
        int spriteX = (int) (hitbox.x + (hitbox.width - spriteWidth) / 2);
        int spriteY = (int) (hitbox.y + (hitbox.height - spriteHeight) / 2);
        
        if (sprite != null) {
            g.drawImage(sprite, spriteX, spriteY, spriteWidth, spriteHeight, null);
        } else {
            g.setColor(java.awt.Color.ORANGE);
            g.fillRect(spriteX, spriteY, spriteWidth, spriteHeight);
        }
        // Uncomment to debug hitbox:
        // g.setColor(java.awt.Color.RED);
        // g.drawRect((int)hitbox.x, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }
    
    // Check if player is touching this platform
    public boolean checkPlayerCollision(Entity player) {
        return hitbox.intersects(player.getHitbox());
    }
    
    public void trigger() {
        triggered = true;
    }
    
    public boolean isTriggered() {
        return triggered;
    }
    
    public boolean hasReachedTarget() {
        return reachedTarget;
    }
    
    public void reset() {
        hitbox.x = startX;
        hitbox.y = startY;
        triggered = false;
        reachedTarget = false;
        movingToTarget = true;
    }
    
    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }
    
    public void setHitboxSize(int hitboxWidth, int hitboxHeight, int newX, int newY) {
        hitbox.width = hitboxWidth;
        hitbox.height = hitboxHeight;
        hitbox.x = newX;
        hitbox.y = newY;
        this.startX = newX;
        this.startY = newY;
    }
    
    public void setSolid(boolean solid) {
        this.solid = solid;
    }
    
    public boolean isSolid() {
        return solid;
    }
    
    // Get the sprite hitbox (the actual collidable area for standing)
    public java.awt.geom.Rectangle2D.Float getSpriteHitbox() {
        float spriteX = hitbox.x + (hitbox.width - spriteWidth) / 2;
        float spriteY = hitbox.y + (hitbox.height - spriteHeight) / 2;
        return new java.awt.geom.Rectangle2D.Float(spriteX, spriteY, spriteWidth, spriteHeight);
    }
}

