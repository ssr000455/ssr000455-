package com.expectationitems.mixin;

import com.expectationitems.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class RemoveInterceptorMixin {
    
    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
    private void interceptRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        
        if (entity instanceof PlayerEntity player) {
            if (EventHandler.isDeathInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player)) {
                ci.cancel();
                player.setHealth(player.getMaxHealth());
                
                if (!player.getWorld().isClient) {
                    player.sendMessage(net.minecraft.text.Text.literal("🛡️ 实体移除被拦截!"), false);
                }
            }
        }
    }
    
    @Inject(method = "discard", at = @At("HEAD"), cancellable = true)
    private void interceptDiscard(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        
        if (entity instanceof PlayerEntity player) {
            if (EventHandler.isDeathInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player)) {
                ci.cancel();
                player.setHealth(player.getMaxHealth());
                
                if (!player.getWorld().isClient) {
                    player.sendMessage(net.minecraft.text.Text.literal("🛡️ 实体销毁被拦截!"), false);
                }
            }
        }
    }
}
