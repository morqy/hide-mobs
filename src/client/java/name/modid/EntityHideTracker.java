package name.modid;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;

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
    
    // Track previous health for damage-based hiding
    private static final Int2FloatOpenHashMap previousHealth = new Int2FloatOpenHashMap();
    
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
        previousHealth.remove(entityId);
        playerTargeted.remove(entityId);
    }
    
    public static boolean isHidden(int entityId) {
        return hiddenEntities.contains(entityId);
    }
    
    /**
     * Track health and return the damage taken (positive = took damage).
     * Returns 0 if no previous health recorded.
     */
    public static float trackHealthChange(int entityId, float currentHealth) {
        if (!previousHealth.containsKey(entityId)) {
            previousHealth.put(entityId, currentHealth);
            return 0;
        }
        float prev = previousHealth.get(entityId);
        previousHealth.put(entityId, currentHealth);
        return prev - currentHealth; // positive = damage taken
    }
    
    public static void clear() {
        hiddenEntities.clear();
        previousHealth.clear();
        playerTargeted.clear();
    }
}
