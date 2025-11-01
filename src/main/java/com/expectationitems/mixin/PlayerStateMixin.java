package com.expectationitems.mixin;

import com.expectationitems.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerStateMixin {
    
    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    private void preventHealthReduction(float health, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (EventHandler.isDeathInterceptorEnabled(player) && 
            EventHandler.isHoldingExpectationSword(player) && health <= 0) {
            ci.cancel();
            player.setHealth(player.getMaxHealth());
            
            if (!player.getWorld().isClient) {
                player.sendMessage(net.minecraft.text.Text.literal("💖 生命值被保护!"), false);
            }
        }
    }
}
