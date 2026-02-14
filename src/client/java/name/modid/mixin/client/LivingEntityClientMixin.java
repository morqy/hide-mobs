package name.modid.mixin.client;

import name.modid.DeathInvisConfig;
import name.modid.EntityHideTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Client-side mixin on LivingEntity to:
 * - Detect death and mark entity hidden — only if player-targeted
 * - Suppress death particles/sounds for hidden entities
 * - Make hidden entities non-pickable so you can hit/mine through them
 * 
 * "Player-targeted" means the player directly left-clicked the entity.
 * AoE/splash mobs that the player didn't directly attack are never hidden.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityClientMixin {
    
    /**
     * Make hidden entities non-pickable so the client raycast skips them.
     * This lets you hit entities behind them and mine blocks through them.
     */
    @Inject(method = "isPickable", at = @At("HEAD"), cancellable = true)
    private void skipPickIfHidden(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.level().isClientSide() && EntityHideTracker.isHidden(entity.getId())) {
            cir.setReturnValue(false);
        }
    }
    
    /**
     * When tickDeath runs on client, hide the entity IF the player targeted it.
     * Cancel tickDeath to prevent death particles/sounds/animation.
     */
    @Inject(method = "tickDeath", at = @At("HEAD"), cancellable = true)
    private void onTickDeath(CallbackInfo ci) {
        if (!DeathInvisConfig.modEnabled) return;
        
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.level().isClientSide()) return;
        if (entity instanceof Player) return;
        
        // Only hide mobs the player directly attacked
        if (EntityHideTracker.isPlayerTargeted(entity.getId())) {
            EntityHideTracker.markHidden(entity.getId());
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
