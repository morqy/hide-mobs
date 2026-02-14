package name.modid.mixin.client;

import name.modid.EntityHideTracker;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Prevents rendering of entities that are marked as hidden.
 * Intercepts shouldRender to skip ALL visual output: model, death tilt, particles, nametag, etc.
 */
@Mixin(EntityRenderer.class)
public class LivingEntityRendererMixin {
    
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void cancelRenderIfHidden(Entity entity, Frustum frustum, double camX, double camY, double camZ,
                                       CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity && EntityHideTracker.isHidden(entity.getId())) {
            cir.setReturnValue(false);
        }
    }
}
