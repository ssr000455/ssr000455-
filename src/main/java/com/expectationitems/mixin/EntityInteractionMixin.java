package com.expectationitems.mixin;

import com.expectationitems.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityInteractionMixin {
    
    // 拦截模组生物的碰撞伤害或特殊效果
    @Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
    private void interceptPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        if (EventHandler.isEventInterceptorEnabled(player) && 
            EventHandler.isHoldingExpectationSword(player)) {
            ci.cancel();
        }
    }
    
    // 拦截实体攻击玩家的特殊效果
    @Inject(method = "onAttacking", at = @At("HEAD"), cancellable = true)
    private void interceptSpecialAttacks(Entity target, CallbackInfo ci) {
        if (target instanceof PlayerEntity player) {
            if (EventHandler.isEventInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player)) {
                ci.cancel();
            }
        }
    }
}
