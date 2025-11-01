package com.expectationitems.mixin;

import com.expectationitems.EventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class DamageInterceptorMixin {
    
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void interceptDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        
        // 只保护持剑的玩家
        if (entity instanceof PlayerEntity player) {
            if (EventHandler.isEventInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player)) {
                cir.setReturnValue(false);
                cir.cancel();
                
                if (player.getHealth() < player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
            }
        }
    }
}
