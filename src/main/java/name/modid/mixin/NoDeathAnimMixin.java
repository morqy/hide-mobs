package name.modid.mixin;

import name.modid.DeathInvisConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class NoDeathAnimMixin {
    
    @Shadow public int deathTime;
    @Shadow public int hurtTime;
    
    @Unique
    private boolean shouldHide = false;
    
    // Track when entity takes significant damage
    @Inject(at = @At("HEAD"), method = "actuallyHurt")
    private void trackDamage(ServerLevel level, DamageSource source, float damage, CallbackInfo info) {
        if (DeathInvisConfig.hideOnHit && DeathInvisConfig.modEnabled) {
            // Mark for hiding only if damage is above threshold
            if (damage >= DeathInvisConfig.damageThreshold) {
                shouldHide = true;
                System.out.println("Hiding mob - damage: " + damage); // Debug
            }
        }
    }
    
    // Hide on death - runs EVERY tick during death animation
    @Inject(at = @At("HEAD"), method = "tickDeath")
    private void hideDeathAnim(CallbackInfo info) {
        if (!DeathInvisConfig.modEnabled) return;
        
        if (!DeathInvisConfig.hideOnHit) {
            LivingEntity entity = (LivingEntity) (Object) this;
            if (entity.level().isClientSide() && !(entity instanceof Player)) {
                makeCompletelyInvisible(entity);
            }
        }
    }
    
    // Check every tick if entity should be hidden
    @Inject(at = @At("HEAD"), method = "tick")
    private void checkHurt(CallbackInfo info) {
        if (!DeathInvisConfig.modEnabled) return;
        
        if (DeathInvisConfig.hideOnHit) {
            LivingEntity entity = (LivingEntity) (Object) this;
            // Only hide if marked by significant damage
            if (entity.level().isClientSide() && shouldHide && !(entity instanceof Player)) {
                makeCompletelyInvisible(entity);
            }
        }
    }
    
    private void makeCompletelyInvisible(LivingEntity entity) {
        entity.setInvisible(true);
        entity.setGlowingTag(false);
        entity.clearFire();
        entity.getActiveEffects().clear();
        entity.setPos(entity.getX(), -1000, entity.getZ());
    }
}