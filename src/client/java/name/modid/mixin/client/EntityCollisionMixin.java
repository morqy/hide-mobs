package name.modid.mixin.client;

import name.modid.EntityHideTracker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Remove all collision from hidden entities so you can walk, hit, and mine through them.
 */
@Mixin(Entity.class)
public class EntityCollisionMixin {
    
    @Inject(method = "isPickable", at = @At("HEAD"), cancellable = true)
    private void skipPickIfHidden(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity && entity.level().isClientSide()
                && EntityHideTracker.isHidden(entity.getId())) {
            cir.setReturnValue(false);
        }
    }
    
    @Inject(method = "canBeCollidedWith", at = @At("HEAD"), cancellable = true)
    private void skipCollisionIfHidden(Entity other, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity && entity.level().isClientSide()
                && EntityHideTracker.isHidden(entity.getId())) {
            cir.setReturnValue(false);
        }
    }
    
    @Inject(method = "canCollideWith", at = @At("HEAD"), cancellable = true)
    private void skipCollideWithIfHidden(Entity other, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity && entity.level().isClientSide()
                && EntityHideTracker.isHidden(entity.getId())) {
            cir.setReturnValue(false);
        }
    }
}
