package main.events;

/** Listener notified when the current level index changes in the model. */
public interface LevelChangeListener {

    /**
     * Call this when model has advanced to a new level index.
     * @param newLevelIndex the newly selected level index
     */
    void onLevelChanged(int newLevelIndex);
}
