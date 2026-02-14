package name.modid;

public class DeathInvisConfig {
    public static boolean hideOnHit = false;
    public static boolean modEnabled = true;
    
    public static void toggle() {
        hideOnHit = !hideOnHit;
    }
    
    public static void toggleMod() {
        modEnabled = !modEnabled;
    }
}