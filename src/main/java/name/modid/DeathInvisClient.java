package name.modid;

import net.fabricmc.api.ClientModInitializer;

public class DeathInvisClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        System.out.println("DeathInvis mod loaded! Type /di help for commands");
    }
}