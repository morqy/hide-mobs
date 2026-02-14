package name.modid.mixin.client;

import name.modid.DeathInvisConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    
    @Inject(method = "handleChatInput", at = @At("HEAD"), cancellable = true)
    private void onChatInput(String message, boolean addToHistory, CallbackInfo ci) {
        if (message.startsWith("/di ") || message.equals("/di")) {
            handleCommand(message.substring(1)); // Remove leading "/"
            ci.cancel();
        }
    }
    
    private void handleCommand(String fullCommand) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;
        
        String[] parts = fullCommand.split(" ");
        String subcommand = parts.length > 1 ? parts[1] : "";
        
        switch (subcommand) {
            case "toggle":
                DeathInvisConfig.toggleMod();
                String status = DeathInvisConfig.modEnabled ? "§aON" : "§cOFF";
                client.player.displayClientMessage(Component.literal("§7[DeathInvis] " + status), false);
                break;
                
            case "mode":
                DeathInvisConfig.toggle();
                String mode = DeathInvisConfig.hideOnHit ? "§eON HIT" : "§eON DEATH";
                client.player.displayClientMessage(Component.literal("§7[DeathInvis] " + mode), false);
                break;
                
            case "threshold":
                if (parts.length >= 3) {
                    try {
                        float threshold = Float.parseFloat(parts[2]);
                        DeathInvisConfig.damageThreshold = threshold;
                        client.player.displayClientMessage(Component.literal(
                            "§7[DeathInvis] Threshold set to: §e" + formatNumber(threshold)
                        ), false);
                    } catch (NumberFormatException e) {
                        client.player.displayClientMessage(Component.literal(
                            "§c[DeathInvis] Invalid number! Usage: /di threshold <amount>"
                        ), false);
                    }
                } else {
                    client.player.displayClientMessage(Component.literal(
                        "§7[DeathInvis] Current threshold: §e" + formatNumber(DeathInvisConfig.damageThreshold)
                    ), false);
                }
                break;
                
            case "help":
            case "":
                client.player.displayClientMessage(Component.literal("§7§m                    "), false);
                client.player.displayClientMessage(Component.literal("§6§lDeathInvis Commands:"), false);
                client.player.displayClientMessage(Component.literal("§7/di toggle §f- Enable/disable mod"), false);
                client.player.displayClientMessage(Component.literal("§7/di mode §f- Switch hide mode"), false);
                client.player.displayClientMessage(Component.literal("§7/di threshold <amount> §f- Set damage threshold"), false);
                client.player.displayClientMessage(Component.literal("§7/di threshold §f- Show current threshold"), false);
                client.player.displayClientMessage(Component.literal("§7Examples: /di threshold 250000"), false);
                client.player.displayClientMessage(Component.literal("§7§m                    "), false);
                break;
                
            default:
                client.player.displayClientMessage(Component.literal(
                    "§c[DeathInvis] Unknown command! Use /di help"
                ), false);
        }
    }
    
    private String formatNumber(float num) {
        if (num == 0) return "0 (ALL HITS)";
        if (num >= 1000000) return (num / 1000000) + "M";
        if (num >= 1000) return (num / 1000) + "K";
        return String.valueOf((int)num);
    }
}
