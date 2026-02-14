package name.modid;

public class DeathInvisConfig {
    public static boolean hideOnHit = true;
    public static boolean modEnabled = true;
    
    // Damage threshold - only hide mobs taking this much damage or more
    // For Hypixel Skyblock: set this high (e.g., 5000-10000) to avoid hiding splash damage mobs
    public static float damageThreshold = 5000.0f;
    
    public static void toggle() {
        hideOnHit = !hideOnHit;
    }
    
    public static void toggleMod() {
        modEnabled = !modEnabled;
    }
}