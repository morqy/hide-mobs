package name.modid.mixin.client;

import name.modid.DeathInvisConfig;
import name.modid.EntityHideTracker;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Tracks which entity the player directly attacks (left-click).
 * In hit mode, immediately hides the attacked mob.
 * Only directly-attacked entities are eligible for hiding,
 * preventing AoE/splash weapons from hiding bystander mobs.
 */
@Mixin(MultiPlayerGameMode.class)
public class AttackTrackingMixin {
    
    @Inject(method = "attack", at = @At("HEAD"))
    private void onAttack(Player player, Entity target, CallbackInfo ci) {
        if (!DeathInvisConfig.modEnabled) return;
        if (target instanceof LivingEntity && !(target instanceof Player)) {
            EntityHideTracker.markPlayerTargeted(target.getId());
            
            // Hit mode: immediately hide the mob on attack
            if (DeathInvisConfig.hideOnHit) {
                EntityHideTracker.markHidden(target.getId());
            }
        }
    }
}
