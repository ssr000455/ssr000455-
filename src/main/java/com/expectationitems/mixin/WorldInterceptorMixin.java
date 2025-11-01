package com.expectationitems.mixin;

import com.expectationitems.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class WorldInterceptorMixin {
    
    @Inject(method = "removeEntity", at = @At("HEAD"), cancellable = true)
    private void interceptServerRemove(Entity entity, Entity.RemovalReason reason, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof PlayerEntity player) {
            if (EventHandler.isDeathInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player)) {
                cir.setReturnValue(false);
                cir.cancel();
                player.setHealth(player.getMaxHealth());
                
                if (!player.getWorld().isClient) {
                    player.sendMessage(net.minecraft.text.Text.literal("🛡️ 服务器移除被拦截!"), false);
                }
            }
        }
    }
}
