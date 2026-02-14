package name.modid;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;

public class DeathInvisClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        registerCommands();
    }
    
    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("di")
                .then(ClientCommandManager.literal("toggle")
                    .executes(context -> {
                        DeathInvisConfig.toggleMod();
                        String status = DeathInvisConfig.modEnabled ? "\u00A7aON" : "\u00A7cOFF";
                        context.getSource().sendFeedback(Component.literal("\u00A77[DeathInvis] " + status));
                        return 1;
                    })
                )
                .then(ClientCommandManager.literal("mode")
                    .then(ClientCommandManager.literal("hit")
                        .executes(context -> {
                            context.getSource().sendFeedback(Component.literal("\u00A7c[DeathInvis] On-hit mode doesn't work."));
                            return 1;
                        })
                    )
                    .then(ClientCommandManager.literal("death")
                        .executes(context -> {
                            DeathInvisConfig.hideOnHit = false;
                            context.getSource().sendFeedback(Component.literal("\u00A77[DeathInvis] Mode: \u00A7eON DEATH"));
                            return 1;
                        })
                    )
                    .executes(context -> {
                        String mode = DeathInvisConfig.hideOnHit ? "\u00A7eON HIT" : "\u00A7eON DEATH";
                        context.getSource().sendFeedback(Component.literal("\u00A77[DeathInvis] Current mode: " + mode));
                        return 1;
                    })
                )
                .then(ClientCommandManager.literal("help")
                    .executes(context -> {
                        sendHelp(context.getSource());
                        return 1;
                    })
                )
                .executes(context -> {
                    sendHelp(context.getSource());
                    return 1;
                })
            );
        });
    }
    
    private static void sendHelp(net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource source) {
        source.sendFeedback(Component.literal("\u00A77\u00A7m                    "));
        source.sendFeedback(Component.literal("\u00A76\u00A7lDeathInvis Commands:"));
        source.sendFeedback(Component.literal("\u00A77/di toggle \u00A7f- Enable/disable mod"));
        source.sendFeedback(Component.literal("\u00A77/di mode \u00A7f- Switch hide mode (on hit / on death)"));
        source.sendFeedback(Component.literal("\u00A77\u00A7m                    "));
    }
}