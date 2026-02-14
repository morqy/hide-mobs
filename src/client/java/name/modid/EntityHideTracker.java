package name.modid;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Tracks which entities should be visually hidden on the client.
 * Only entities the player directly attacked can be hidden,
 * preventing AoE/splash damage from hiding bystander mobs.
 */
public class EntityHideTracker {
    // Set of entity IDs that should not be rendered
    private static final IntSet hiddenEntities = new IntOpenHashSet();
    
    // Entities the player has directly attacked (click-targeted)
    // These are the ONLY entities eligible for hiding
    private static final IntSet playerTargeted = new IntOpenHashSet();
    
    /**
     * Called when the player directly attacks an entity (left-click).
     * Marks it as eligible for hiding.
     */
    public static void markPlayerTargeted(int entityId) {
        playerTargeted.add(entityId);
    }
    
    /**
     * Check if the player directly attacked this entity.
     */
    public static boolean isPlayerTargeted(int entityId) {
        return playerTargeted.contains(entityId);
    }
    
    public static void markHidden(int entityId) {
        hiddenEntities.add(entityId);
    }
    
    public static void unmarkHidden(int entityId) {
        hiddenEntities.remove(entityId);
        playerTargeted.remove(entityId);
    }
    
    public static boolean isHidden(int entityId) {
        return hiddenEntities.contains(entityId);
    }
    
    public static void clear() {
        hiddenEntities.clear();
        playerTargeted.clear();
    }
}
