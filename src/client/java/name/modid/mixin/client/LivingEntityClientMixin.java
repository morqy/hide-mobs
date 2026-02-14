package name.modid.mixin.client;

import name.modid.DeathInvisConfig;
import name.modid.EntityHideTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Client-side mixin on LivingEntity to:
 * - Detect death and discard entity from client — only if player-targeted
 * - Suppress death particles/sounds for hidden entities
 * 
 * "Player-targeted" means the player directly left-clicked the entity.
 * AoE/splash mobs that the player didn't directly attack are never hidden.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityClientMixin {
    
    /**
     * When tickDeath runs on client, completely remove the entity from the
     * client world if the player targeted it. This removes the model,
     * collision box, and all interactions — you can hit and mine through it.
     * The server still tracks the entity normally and sends a remove packet later.
     */
    @Inject(method = "tickDeath", at = @At("HEAD"), cancellable = true)
    private void onTickDeath(CallbackInfo ci) {
        if (!DeathInvisConfig.modEnabled) return;
        
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.level().isClientSide()) return;
        if (entity instanceof Player) return;
        
        // Only affect mobs the player directly attacked
        if (EntityHideTracker.isPlayerTargeted(entity.getId())) {
            EntityHideTracker.unmarkHidden(entity.getId());
            entity.discard(); // Fully remove from client world
            ci.cancel();
        }
    }
    
    /**
     * Clean up tracking when entity is removed from the world.
     */
    @Inject(method = "remove", at = @At("HEAD"))
    private void onRemove(net.minecraft.world.entity.Entity.RemovalReason reason, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.level().isClientSide()) {
            EntityHideTracker.unmarkHidden(entity.getId());
        }
    }
}
